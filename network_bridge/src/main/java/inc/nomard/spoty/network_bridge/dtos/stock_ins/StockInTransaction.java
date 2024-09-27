package inc.nomard.spoty.network_bridge.dtos.stock_ins;

import inc.nomard.spoty.network_bridge.dtos.Branch;
import inc.nomard.spoty.network_bridge.dtos.Product;
import lombok.*;
import lombok.extern.java.Log;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class StockInTransaction {
    private Long id;
    private ArrayList<Branch> branches;
    private Product product;
    private StockInDetail stockInDetail;
    private LocalDateTime date;
    private long stockInQuantity;
}
