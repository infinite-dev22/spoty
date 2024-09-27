package inc.nomard.spoty.network_bridge.dtos.transfers;

import inc.nomard.spoty.network_bridge.dtos.Product;
import lombok.*;
import lombok.extern.java.Log;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class TransferDetail {
    private Long id;
    private Product product;
    private int quantity;

    public String getProductName() {
        return product.getName();
    }

    public Long getProductQuantity() {
        return product.getQuantity();
    }
}
