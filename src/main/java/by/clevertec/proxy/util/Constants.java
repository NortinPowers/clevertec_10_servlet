package by.clevertec.proxy.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    /*
     * Константы при использовании изображения (.jpg) в качестве фона страницы.
     */
//    public static final String BACKGROUND_PATH = "static/img/";
//    public static final String BACKGROUND_IMG = "Clevertec_Template.jpg";
    /*
     * Константы при использовании PDF (.pdf) в качестве фона страницы.
     */
    public static final String BACKGROUND_PATH = "static/pdf/";
    public static final String BACKGROUND_IMG = "Clevertec_Template.pdf";
    /*
     * Общие константы.
     */
    public static final String USER_DIR = "user.dir";
    public static final String DATA_REPORT_FORMAT = "dd.MM.yy HH-mm-ss";
    public static final String REPORT_DIR = "report";
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final String PRODUCT_SERVICE = "productService";
    public static final String PRODUCTS_HELPER = "productsHelper";
    public static final String DATA_SOURCE = "dataSource";

    @UtilityClass
    public class RequestParameter {

        public static final String PAGE_NUMBER = "pageNumber";
        public static final String PAGE_SIZE = "pageSize";
        public static final String PRODUCT_UUID = "uuid";
    }

    @UtilityClass
    public class ResponseConstant {

        public static final String PRODUCT_NOT_FOUND = "Product not found";
        public static final String INVALID_REQUEST = "Invalid request";

    }

    @UtilityClass
    public class MediaTypeConstant {

        public static final String MEDIA_TYPE_JSON = "application/json";
        public static final String MEDIA_TYPE_PDF = "application/pdf";
    }
}
