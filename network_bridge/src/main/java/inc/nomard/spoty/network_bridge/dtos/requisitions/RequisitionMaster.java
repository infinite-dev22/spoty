package inc.nomard.spoty.network_bridge.dtos.requisitions;

import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;

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
public class RequisitionMaster {
    private Long id;
    private String ref;
    private Supplier supplier;
    private List<RequisitionDetail> requisitionDetails;
    private String notes;
    private String status;
    private Employee createdBy;
    private LocalDateTime createdAt;
    private Employee updatedBy;
    private LocalDateTime updatedAt;

    public String getSupplierName() {
        return supplier.getName();
    }

    public String doneBy() {
        return createdBy.getName();
    }
}
