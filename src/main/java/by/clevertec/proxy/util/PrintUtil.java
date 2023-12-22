package by.clevertec.proxy.util;

import static by.clevertec.proxy.util.Constants.BACKGROUND_IMG;
import static by.clevertec.proxy.util.Constants.BACKGROUND_PATH;

import com.itextpdf.text.Paragraph;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PrintUtil {

    /**
     * Возвращает путь к изображению подложки.
     *
     * @return Path объект указания местоположения
     * @throws URISyntaxException выбрасываемое исключение
     */
    public static Path getPath() throws URISyntaxException {
        URL resourceUrl = PrintUtil.class.getClassLoader().getResource(BACKGROUND_PATH + BACKGROUND_IMG);
        return Paths.get(resourceUrl.toURI());
    }

    /**
     * Добавляет пустые строки.
     *
     * @param paragraph текстовый блок
     * @param number    количество необходимых пустых строк
     */
    public static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
