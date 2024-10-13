package inc.nomard.spoty.network_bridge.dtos.quotations;

import inc.nomard.spoty.network_bridge.dtos.Product;
import lombok.*;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class QuotationDetail {
    private Long id;
    private Product product;
    private int quantity;
    private double unitPrice;
    private double totalPrice;

    public String getProductName() {
        return product.getName();
    }

    public Double getProductPrice() {
        return product.getSalePrice();
    }
}
