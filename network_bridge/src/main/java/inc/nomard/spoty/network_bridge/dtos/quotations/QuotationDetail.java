package inc.nomard.spoty.network_bridge.dtos.quotations;

import inc.nomard.spoty.network_bridge.dtos.Product;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
