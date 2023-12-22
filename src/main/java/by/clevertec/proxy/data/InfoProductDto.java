package by.clevertec.proxy.data;

import java.math.BigDecimal;
import java.util.UUID;

public record InfoProductDto(
        UUID uuid,
        String name,
        String description,
        BigDecimal price) {

    @Override
    public String toString() {
        return "Product"
                + "\n-uuid: " + uuid
                + "\n-name: " + name
                + "\n-description: " + description
                + "\n-price: " + price;
    }
}
