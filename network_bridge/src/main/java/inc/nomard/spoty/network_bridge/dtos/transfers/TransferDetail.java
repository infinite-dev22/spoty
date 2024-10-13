package inc.nomard.spoty.network_bridge.dtos.transfers;

import inc.nomard.spoty.network_bridge.dtos.Product;
import lombok.*;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
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
