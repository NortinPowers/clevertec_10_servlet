package by.clevertec.proxy.config;

import jakarta.servlet.Filter;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        boolean done = registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
        if (!done) {
            throw new RuntimeException();
        }
    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[]{new DelegatingFilterProxy("webEncodingFilter")};
    }
}
