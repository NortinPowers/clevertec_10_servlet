package by.clevertec.proxy.servlet;

import static by.clevertec.proxy.util.Constants.PRODUCTS_HELPER;

import by.clevertec.proxy.util.helper.ProductHelper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;

@WebServlet("/products/receipt")
@Log4j2
public class ReceiptServlet extends HttpServlet {

    private ProductHelper productHelper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productHelper = (ProductHelper) config.getServletContext().getAttribute(PRODUCTS_HELPER);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        productHelper.setProductsReceiptGetResponse(req, resp);
    }
}
