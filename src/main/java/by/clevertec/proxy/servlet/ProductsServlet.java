package by.clevertec.proxy.servlet;

import by.clevertec.proxy.util.helper.ProductHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class ProductsServlet extends HttpServlet {

    private final ProductHelper productHelper;

    @Override
    @GetMapping("/products")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        productHelper.setProductsGetResponse(req, resp);
    }
}
