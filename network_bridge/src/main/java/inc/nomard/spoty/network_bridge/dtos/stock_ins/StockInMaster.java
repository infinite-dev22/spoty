package inc.nomard.spoty.network_bridge.dtos.stock_ins;

import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import java.util.*;
import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Log
public class StockInMaster {
    private Long id;
    private String ref;
    private ArrayList<Branch> branches;
    private List<StockInDetail> stockInDetails;
    private String shipping;
    private Double total;
    private User approvedBy;
    private User recordedBy;
    private Date approvalDate;
    private Date recordDate;
    private String notes;
}
