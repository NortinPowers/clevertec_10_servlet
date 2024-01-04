package by.clevertec.proxy.config;

import by.clevertec.proxy.mapper.ProductMapper;
import by.clevertec.proxy.mapper.ProductMapperImpl;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("by.clevertec.proxy")
@EnableAspectJAutoProxy
public class SpringConfig implements WebMvcConfigurer {

    @Bean
    public QueryRunner queryRunner(BasicDataSource dataSource) {
        return new QueryRunner(dataSource);
    }

    @Bean
    public ProductMapper mapper() {
        return new ProductMapperImpl();
    }
}
