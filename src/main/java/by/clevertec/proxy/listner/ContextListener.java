package by.clevertec.proxy.listner;

import static by.clevertec.proxy.util.Constants.DATA_SOURCE;
import static by.clevertec.proxy.util.Constants.PRODUCTS_HELPER;
import static by.clevertec.proxy.util.Constants.PRODUCT_SERVICE;
import static by.clevertec.proxy.util.LogUtil.getErrorMessageToLog;

import by.clevertec.proxy.config.AppConfig;
import by.clevertec.proxy.mapper.ProductMapper;
import by.clevertec.proxy.mapper.ProductMapperImpl;
import by.clevertec.proxy.pdf.WebPdfPrinter;
import by.clevertec.proxy.repository.ProductRepository;
import by.clevertec.proxy.repository.impl.ProductRepositoryImpl;
import by.clevertec.proxy.repository.util.DataSource;
import by.clevertec.proxy.service.ProductService;
import by.clevertec.proxy.service.impl.ProductServiceImpl;
import by.clevertec.proxy.util.helper.ProductHelper;
import by.clevertec.proxy.validator.ProductDtoValidator;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.SQLException;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;

@WebListener
@Log4j2
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ProductDtoValidator validator = new ProductDtoValidator();
        ProductRepository productRepository = new ProductRepositoryImpl();
        ProductMapper productMapper = new ProductMapperImpl();
        ProductService productService = new ProductServiceImpl(productMapper, productRepository, validator);
        sce.getServletContext().setAttribute(PRODUCT_SERVICE, productService);
        WebPdfPrinter printer = new WebPdfPrinter();
        ProductHelper productHelper = new ProductHelper(productService, printer);
        sce.getServletContext().setAttribute(PRODUCTS_HELPER, productHelper);
        AppConfig appConfig = new AppConfig();
        Map<String, Object> flywayProperties = appConfig.getProperty("Flyway");
        performFlywayInitialization(flywayProperties);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        BasicDataSource dataSource = (BasicDataSource) sce.getServletContext().getAttribute(DATA_SOURCE);
        try {
            dataSource.close();
        } catch (SQLException exception) {
            log.error(getErrorMessageToLog("contextDestroyed()", ContextListener.class), exception);
        }
    }

    /**
     * Инициализирует Flyway для выполнения миграций базы данных на основе предоставленных свойств.
     * Если свойство "enabled" равно true, инициализирует и запускает Flyway для выполнения миграций.
     *
     * @param flywayProperties свойства Flyway в виде Map<String, Object>, содержащие параметры конфигурации Flyway.
     */
    private void performFlywayInitialization(Map<String, Object> flywayProperties) {
        boolean enabled = (boolean) flywayProperties.get("enabled");
        if (enabled) {
            Flyway flyway = Flyway.configure()
                    .dataSource(DataSource.getDataSource())
                    .locations("classpath:db/migration")
                    .load();
            flyway.baseline();
            flyway.migrate();
        }
    }
}
