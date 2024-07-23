package inc.nomard.spoty.network_bridge.dtos.stock_ins;

import inc.nomard.spoty.network_bridge.dtos.*;
import java.time.*;

import java.time.*;
import java.util.*;
import lombok.*;
import lombok.extern.java.*;

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
