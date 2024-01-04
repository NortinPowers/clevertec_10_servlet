package by.clevertec.proxy.servlet;

import by.clevertec.proxy.util.helper.ProductHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/products")
public class ReceiptServlet extends HttpServlet {

    private final ProductHelper productHelper;

    @Override
    @GetMapping("/receipt")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        productHelper.setProductsReceiptGetResponse(req, resp);
    }
}
