package inc.nomard.spoty.network_bridge.dtos.sales;

import inc.nomard.spoty.network_bridge.dtos.Product;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleDetail {
    private Long id;
    private Product product;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    @Builder.Default
    private transient SimpleBooleanProperty selected = new SimpleBooleanProperty(false);

    public String getProductName() {
        return product.getName();
    }

    public Double getProductPrice() {
        return product.getSalePrice();
    }
}
