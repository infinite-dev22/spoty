package inc.nomard.spoty.network_bridge.dtos.stock_ins;

import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
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
public class StockInMaster {
    private Long id;
    private String ref;
    private ArrayList<Branch> branches;
    private List<StockInDetail> stockInDetails;
    private String shipping;
    private Double total;
    private User approvedBy;
    private User recordedBy;
    private LocalDateTime approvalDate;
    private LocalDateTime recordDate;
    private String notes;
}
