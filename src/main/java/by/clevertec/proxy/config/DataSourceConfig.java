package by.clevertec.proxy.config;

import by.clevertec.proxy.cache.Cache;
import by.clevertec.proxy.cache.impl.LfuCache;
import by.clevertec.proxy.cache.impl.LruCache;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    @Bean
    public BasicDataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        Map<String, Object> databaseProperties = appConfig().getProperty("database");
        dataSource.setDriverClassName((String) databaseProperties.get("driver-class-name"));
        dataSource.setUrl((String) databaseProperties.get("url"));
        dataSource.setUsername((String) databaseProperties.get("username"));
        dataSource.setPassword((String) databaseProperties.get("password"));
        dataSource.setInitialSize((Integer) databaseProperties.get("min-connection"));
        dataSource.setMaxTotal((Integer) databaseProperties.get("max-connection"));
        return dataSource;
    }

    @Bean
    public FlywayBeanFactoryPostProcessor flywayBeanFactoryPostProcessor() {
        return new FlywayBeanFactoryPostProcessor(appConfig(), dataSource());
    }

    @Bean
    public AppConfig appConfig() {
        return new AppConfig();
    }

    @Bean
    public Cache<?, ?> cache() {
        Map<String, Object> cacheProperties = appConfig().getProperty("Cache");
        String algorithm = (String) cacheProperties.get("algorithm");
        Integer maxCollectionSize = (Integer) cacheProperties.get("maxCollectionSize");
        Cache<UUID, Object> cache;
        if ("LFU".equals(algorithm)) {
            cache = new LfuCache<>(maxCollectionSize);
        } else {
            cache = new LruCache<>(maxCollectionSize);
        }
        return cache;
    }
}
