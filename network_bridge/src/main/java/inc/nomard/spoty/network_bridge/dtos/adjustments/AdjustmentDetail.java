package inc.nomard.spoty.network_bridge.dtos.adjustments;

import inc.nomard.spoty.network_bridge.dtos.Product;
import lombok.*;
import lombok.extern.java.Log;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class AdjustmentDetail {
    private Long id;
    private Product product;
    private Long quantity;
    private String adjustmentType;

    public String getProductName() {
        return product.getName();
    }
}
