package by.clevertec.proxy.pdf;

import static by.clevertec.proxy.util.PrintUtil.addEmptyLine;
import static by.clevertec.proxy.util.PrintUtil.getPath;

import by.clevertec.proxy.data.InfoProductDto;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class WebPdfPrinter {

    /**
     * Создает PDF чек на основе информации о продукте и отправляет его в HttpServletResponse.
     *
     * @param resp           объект HttpServletResponse, в который будет записан созданный PDF.
     * @param infoProductDto объект InfoProductDto с информацией о продукте.
     * @param document       объект Document, представляющий создаваемый PDF документ.
     * @throws DocumentException  если возникает ошибка при работе с документом PDF.
     * @throws IOException        если возникает ошибка ввода/вывода при работе с HttpServletResponse.
     * @throws URISyntaxException если возникает ошибка при работе с URI/URL.
     */
    public void createPdfReceipt(HttpServletResponse resp, InfoProductDto infoProductDto, Document document) throws DocumentException, IOException, URISyntaxException {
        PdfWriter writer = PdfWriter.getInstance(document, resp.getOutputStream());
        document.open();
        setPdfTemplate(writer);
        Paragraph emptyLines = new Paragraph();
        addEmptyLine(emptyLines, 10);
        document.add(emptyLines);
        document.add(new Paragraph(infoProductDto.uuid().toString()));
        document.add(new Paragraph(new String(infoProductDto.name().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1)));
        document.add(new Paragraph(infoProductDto.price().toString()));
    }

    /**
     * Устанавливает шаблон PDF для указанного объекта PdfWriter.
     *
     * @param writer объект PdfWriter, для которого будет установлен шаблон PDF.
     * @throws IOException        если возникает ошибка ввода/вывода при работе с файлом шаблона PDF.
     * @throws URISyntaxException если возникает ошибка при работе с URI/URL шаблона PDF.
     */
    private void setPdfTemplate(PdfWriter writer) throws IOException, URISyntaxException {
        PdfReader reader = new PdfReader(String.valueOf(getPath()));
        PdfImportedPage page = writer.getImportedPage(reader, 1);
        PdfContentByte contentByte = writer.getDirectContentUnder();
        contentByte.addTemplate(page, 0, 0);
    }
}
