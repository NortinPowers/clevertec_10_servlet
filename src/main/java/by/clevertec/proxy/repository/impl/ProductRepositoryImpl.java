package by.clevertec.proxy.repository.impl;

import static by.clevertec.proxy.util.LogUtil.getErrorMessageToLog;

import by.clevertec.proxy.entity.Product;
import by.clevertec.proxy.repository.ProductRepository;
import by.clevertec.proxy.repository.util.LocalDateTimeRowProcessor;
import by.clevertec.proxy.repository.util.Page;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private static final String GET_ALL_PRODUCTS = "select * from products";
    private static final String GET_PRODUCT_BY_UUID = "select * from products where uuid = ?";
    private static final String SAVE_PRODUCT = "insert into products (name, description, price) values (?, ?, ?)";
    private static final String DELETE_PRODUCT = "delete from products where uuid = ?";
    private static final String UPDATE_PRODUCT = "update products set name = ?, description = ?, price = ? where uuid = ?";

    private final QueryRunner queryRunner;
    private final LocalDateTimeRowProcessor rowProcessor;

    @Override
    public Optional<Product> findById(UUID uuid) {
        try {
            Product product = queryRunner.query(GET_PRODUCT_BY_UUID, new BeanHandler<>(Product.class, rowProcessor), uuid);
            return Optional.ofNullable(product);
        } catch (Exception exception) {
            log.error(getErrorMessageToLog("findById()", ProductRepositoryImpl.class), exception);
            return Optional.empty();
        }
    }

    @Override
    public Page<Product> findAll(int pageNumber, int pageSize) {
        List<Product> products = new ArrayList<>();
        int totalEltments = 0;
        try {
            products = queryRunner.query(GET_ALL_PRODUCTS, new BeanListHandler<>(Product.class, rowProcessor));
            totalEltments = products.size();
            int fromIndex = pageNumber * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalEltments);
            products = products.subList(fromIndex, toIndex);
        } catch (Exception exception) {
            log.error(getErrorMessageToLog("findAll()", ProductRepositoryImpl.class), exception);
        }
        return new Page<>(products, pageNumber, pageSize, totalEltments);
    }

    @Override
    public Product save(Product product) {
        try {
            ScalarHandler<Object> scalarHandler = new ScalarHandler<>();
            UUID generatedKey = (UUID) queryRunner.insert(SAVE_PRODUCT, scalarHandler,
                    product.getName(), product.getDescription(), product.getPrice());
            if (generatedKey != null) {
                return findById(generatedKey).orElse(null);
            }
        } catch (Exception exception) {
            log.error(getErrorMessageToLog("save()", ProductRepositoryImpl.class), exception);
        }
        return null;
    }

    @Override
    public void delete(UUID uuid) {
        try {
            queryRunner.query(DELETE_PRODUCT, new BeanHandler<>(Product.class, rowProcessor), uuid);
        } catch (Exception exception) {
            log.error(getErrorMessageToLog("delete()", ProductRepositoryImpl.class), exception);
        }
    }

    @Override
    public void update(UUID uuid, Product product) {
        try {
            queryRunner.query(UPDATE_PRODUCT, new BeanHandler<>(Product.class, rowProcessor),
                    product.getName(), product.getDescription(), product.getPrice(), uuid);
        } catch (Exception exception) {
            log.error(getErrorMessageToLog("update()", ProductRepositoryImpl.class), exception);
        }
    }

    /**
     * Устанавливает новый источник данных для выполнения запросов.
     *
     * @param newDataSource новый источник данных, который будет использоваться для выполнения запросов.
     */
    public void setDataSource(BasicDataSource newDataSource) {
        try {
            Field field = ProductRepositoryImpl.class.getDeclaredField("queryRunner");
            field.setAccessible(true);
            field.set(this, new QueryRunner(newDataSource));
        } catch (Exception exception) {
            log.error(getErrorMessageToLog("setDataSource()", ProductRepositoryImpl.class), exception);
        }
    }
}
