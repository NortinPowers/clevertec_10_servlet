package by.clevertec.proxy.pdf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import by.clevertec.proxy.data.InfoProductDto;
import by.clevertec.proxy.util.ProductTestBuilder;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WebPdfPrinterTest {

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private WebPdfPrinter printer;

    @Test
    void createPdfReceiptShouldCreatePdf_withOnePage() throws DocumentException, IOException, URISyntaxException {
        InfoProductDto infoProductDto = ProductTestBuilder.builder().build().buildInfoProductDto();
        Document document = new Document();
        int expectedPageNumber = 0;

        printer.createPdfReceipt(response, infoProductDto, document);

        verify(response).getOutputStream();
        assertEquals(expectedPageNumber, document.getPageNumber());
    }
}
