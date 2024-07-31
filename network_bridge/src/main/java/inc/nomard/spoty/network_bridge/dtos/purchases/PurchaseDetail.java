package inc.nomard.spoty.network_bridge.dtos.purchases;

import inc.nomard.spoty.network_bridge.dtos.*;
import javafx.beans.property.*;
import lombok.*;
import lombok.extern.java.*;

// TODO: Remove total and Quantity and add them to PurchaseMaster.
// TODO: In place of total create purchasePrice.
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class PurchaseDetail {
    private Long id;
    private Product product;
    private int quantity;
    private double subTotalCost;
    private transient SimpleBooleanProperty selected = new SimpleBooleanProperty(false);

    public String getProductName() {
        return product.getName();
    }
}
