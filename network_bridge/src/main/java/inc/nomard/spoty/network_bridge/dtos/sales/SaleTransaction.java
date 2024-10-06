package inc.nomard.spoty.network_bridge.dtos.sales;

import inc.nomard.spoty.network_bridge.dtos.Branch;
import inc.nomard.spoty.network_bridge.dtos.Product;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class SaleTransaction {
    private Long id;
    private ArrayList<Branch> branches;
    private Product product;
    private SaleDetail saleDetail;
    private LocalDateTime date;
    private long saleQuantity;
}
