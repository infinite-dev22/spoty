package inc.nomard.spoty.network_bridge.dtos.stock_ins;

import inc.nomard.spoty.network_bridge.dtos.*;
import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Log
public class StockInDetail {
    private Long id;
    private Product product;
    private int quantity;
    private String description;

    public String getProductName() {
        return product.getName();
    }
}
