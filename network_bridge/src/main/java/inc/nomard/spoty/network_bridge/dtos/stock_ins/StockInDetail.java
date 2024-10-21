package inc.nomard.spoty.network_bridge.dtos.stock_ins;

import inc.nomard.spoty.network_bridge.dtos.Product;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockInDetail {
    private Long id;
    private Product product;
    private int quantity;
    private String description;

    public String getProductName() {
        return product.getName();
    }
}
