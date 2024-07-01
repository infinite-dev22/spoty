package inc.nomard.spoty.network_bridge.dtos.sales;

import inc.nomard.spoty.network_bridge.dtos.*;
import java.util.*;
import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Log
public class SaleTransaction {
    private Long id;
    private ArrayList<Branch> branches;
    private Product product;
    private SaleDetail saleDetail;
    private Date date;
    private long saleQuantity;
}
