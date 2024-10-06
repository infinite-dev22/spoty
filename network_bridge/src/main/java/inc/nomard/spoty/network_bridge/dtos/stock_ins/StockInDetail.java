package inc.nomard.spoty.network_bridge.dtos.stock_ins;

import inc.nomard.spoty.network_bridge.dtos.Product;
import lombok.*;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class StockInDetail {
    private Long id;
    private Product product;
    private int quantity;
    private String description;

    public String getProductName() {
        return product.getName();
    }
}
