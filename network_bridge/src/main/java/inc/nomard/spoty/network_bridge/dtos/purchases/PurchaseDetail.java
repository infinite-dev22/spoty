package inc.nomard.spoty.network_bridge.dtos.purchases;

import inc.nomard.spoty.network_bridge.dtos.Product;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseDetail {
    private Long id;
    private Product product;
    private int quantity;
    private double unitCost;
    @Builder.Default
    private transient SimpleBooleanProperty selected = new SimpleBooleanProperty(false);

    public String getProductName() {
        return product.getName();
    }
}
