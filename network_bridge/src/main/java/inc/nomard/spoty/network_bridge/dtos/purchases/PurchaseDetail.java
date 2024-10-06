package inc.nomard.spoty.network_bridge.dtos.purchases;

import inc.nomard.spoty.network_bridge.dtos.Product;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.*;
import lombok.extern.log4j.Log4j2;

// TODO: Remove total and Quantity and add them to PurchaseMaster.
// TODO: In place of total create purchasePrice.
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
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
