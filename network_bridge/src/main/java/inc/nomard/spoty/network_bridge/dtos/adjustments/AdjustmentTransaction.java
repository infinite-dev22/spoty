package inc.nomard.spoty.network_bridge.dtos.adjustments;

import inc.nomard.spoty.network_bridge.dtos.Branch;
import inc.nomard.spoty.network_bridge.dtos.Product;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdjustmentTransaction {
    private Long id;
    private ArrayList<Branch> branches;
    private Product product;
    private AdjustmentDetail adjustmentDetail;
    private LocalDateTime date;
    private long adjustQuantity;
    private String adjustmentType;
}
