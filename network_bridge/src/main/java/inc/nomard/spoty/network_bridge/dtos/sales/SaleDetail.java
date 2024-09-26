package inc.nomard.spoty.network_bridge.dtos.sales;

import inc.nomard.spoty.network_bridge.dtos.*;
import javafx.beans.property.*;
import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
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
