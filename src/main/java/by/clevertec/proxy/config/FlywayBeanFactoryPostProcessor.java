package by.clevertec.proxy.config;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlywayBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private final AppConfig appConfig;
    private final BasicDataSource dataSource;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String, Object> flywayProperties = appConfig.getProperty("Flyway");
        boolean enabled = (boolean) flywayProperties.get("enabled");
        if (enabled) {
            Flyway flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .locations("classpath:db/migration")
                    .load();
            flyway.baseline();
            flyway.migrate();
            beanFactory.registerSingleton("flyway", flyway);
        }
    }
}
