package inc.nomard.spoty.network_bridge.dtos.adjustments;

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
public class AdjustmentMaster {
    private Long id;
    private String ref;
    private ArrayList<Branch> branches;
    private List<AdjustmentDetail> adjustmentDetails;
    private String notes;
    private String status;
    private Double Total;
    private User createdBy;
}
