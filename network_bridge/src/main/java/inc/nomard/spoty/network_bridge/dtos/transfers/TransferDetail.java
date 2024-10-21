package inc.nomard.spoty.network_bridge.dtos.transfers;

import inc.nomard.spoty.network_bridge.dtos.Product;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
