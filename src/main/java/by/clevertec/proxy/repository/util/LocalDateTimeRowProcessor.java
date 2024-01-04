package by.clevertec.proxy.repository.util;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.springframework.stereotype.Component;

@Component
public class LocalDateTimeRowProcessor extends BasicRowProcessor {

    public LocalDateTimeRowProcessor(LocalDateTimeProcessor processor) {
        super(processor);
    }
}
