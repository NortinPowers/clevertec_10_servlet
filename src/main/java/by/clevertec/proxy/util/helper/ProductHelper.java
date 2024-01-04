package by.clevertec.proxy.util.helper;

import static by.clevertec.proxy.util.Constants.MediaTypeConstant.MEDIA_TYPE_PDF;
import static by.clevertec.proxy.util.Constants.RequestParameter.PAGE_NUMBER;
import static by.clevertec.proxy.util.Constants.RequestParameter.PAGE_SIZE;
import static by.clevertec.proxy.util.Constants.RequestParameter.PRODUCT_UUID;
import static by.clevertec.proxy.util.Constants.ResponseConstant.INVALID_REQUEST;
import static by.clevertec.proxy.util.Constants.ResponseConstant.PRODUCT_NOT_FOUND;
import static by.clevertec.proxy.util.LogUtil.getErrorMessageToLog;

import by.clevertec.proxy.data.InfoProductDto;
import by.clevertec.proxy.pdf.WebPdfPrinter;
import by.clevertec.proxy.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class ProductHelper {

    private final ProductService productService;
    private final WebPdfPrinter printer;

    /**
     * Устанавливает ответ на запрос получения продуктов для указанных запроса и ответа.
     * Использует параметры запроса для получения номера страницы и размера страницы,
     * затем формирует JSON-строку продуктов и конфигурирует ответ.
     *
     * @param req  объект HttpServletRequest, представляющий запрос.
     * @param resp объект HttpServletResponse, представляющий ответ.
     * @throws IOException если возникает ошибка ввода/вывода при работе с ответом.
     */
    public void setProductsGetResponse(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int pageNumber = getRequestIntParameter(req, PAGE_NUMBER);
        int pageSize = getRequestIntParameter(req, PAGE_SIZE);
        String productsJson = getRequestString(pageNumber, pageSize);
        configResponse(resp, productsJson);
    }

    /**
     * Устанавливает ответ на запрос получения информации о продукте для указанных запроса и ответа.
     * Использует параметр запроса для получения идентификатора продукта,
     * затем получает информацию о продукте, формирует PDF-файл с квитанцией и конфигурирует ответ.
     *
     * @param req  объект HttpServletRequest, представляющий запрос.
     * @param resp объект HttpServletResponse, представляющий ответ.
     * @throws IOException если возникает ошибка ввода/вывода при работе с ответом.
     */
    public void setProductsReceiptGetResponse(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getParameter(PRODUCT_UUID) != null) {
            UUID uuid = UUID.fromString(req.getParameter(PRODUCT_UUID));
            InfoProductDto infoProductDto = productService.get(uuid);
            if (infoProductDto != null) {
                setProductsReceiptGetSuccessResponse(resp, infoProductDto);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, PRODUCT_NOT_FOUND);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, INVALID_REQUEST);
        }
    }

    /**
     * Конфигурирует ответ на запрос, устанавливая переданную JSON-строку продуктов и статус ответа.
     *
     * @param resp         объект HttpServletResponse, представляющий ответ.
     * @param productsJson JSON-строка продуктов.
     * @throws IOException если возникает ошибка ввода/вывода при работе с ответом.
     */
    private void configResponse(HttpServletResponse resp, String productsJson) throws IOException {
        resp.getWriter().write(productsJson);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Возвращает JSON-строку продуктов с учетом указанных параметров пагинации.
     *
     * @param pageNumber номер страницы, начиная с 0.
     * @param pageSize   количество элементов на странице.
     * @return JSON-строка продуктов, соответствующих указанным параметрам пагинации.
     * @throws JsonProcessingException если возникает ошибка при преобразовании списка продуктов в JSON-строку.
     */
    private String getRequestString(int pageNumber, int pageSize) throws JsonProcessingException {
        List<InfoProductDto> products = productService.getAllWithPagination(pageNumber, pageSize);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(products);
    }

    /**
     * Возвращает целочисленное значение параметра запроса с указанным именем.
     * Если параметр не задан или имеет неверное значение, возвращает 0.
     *
     * @param req   объект HttpServletRequest, представляющий запрос.
     * @param value имя параметра запроса.
     * @return целочисленное значение параметра запроса или 0, если параметр не задан или имеет неверное значение.
     */
    private int getRequestIntParameter(HttpServletRequest req, String value) {
        int parameter = 0;
        if (req.getParameter(value) != null) {
            int requestParameter = Integer.parseInt(req.getParameter(value));
            if (requestParameter > 0) {
                parameter = requestParameter;
            }
        }
        return parameter;
    }

    /**
     * Устанавливает успешный ответ на запрос получения информации о продукте и формирует PDF-файл с квитанцией.
     *
     * @param resp           объект HttpServletResponse, представляющий ответ.
     * @param infoProductDto объект InfoProductDto, содержащий информацию о продукте.
     * @throws IOException если возникает ошибка ввода/вывода при работе с ответом или PDF-файлом.
     */
    private void setProductsReceiptGetSuccessResponse(HttpServletResponse resp, InfoProductDto infoProductDto) throws IOException {
        resp.setContentType(MEDIA_TYPE_PDF);
        resp.setStatus(HttpServletResponse.SC_OK);
        Document document = new Document();
        try {
            printer.createPdfReceipt(resp, infoProductDto, document);
        } catch (DocumentException | URISyntaxException exception) {
            log.error(getErrorMessageToLog("setProductsReceiptGetSuccessResponse()", ProductHelper.class), exception);
        } finally {
            document.close();
            resp.getOutputStream().close();
        }
    }
}
