package inc.nomard.spoty.network_bridge.dtos.adjustments;

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
public class AdjustmentTransaction {
    private Long id;
    private ArrayList<Branch> branches;
    private Product product;
    private AdjustmentDetail adjustmentDetail;
    private Date date;
    private long adjustQuantity;
    private String adjustmentType;
}
