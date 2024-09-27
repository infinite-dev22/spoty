package inc.nomard.spoty.network_bridge.dtos.requisitions;

import inc.nomard.spoty.network_bridge.dtos.Supplier;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Employee;
import lombok.*;
import lombok.extern.java.Log;

import java.time.LocalDateTime;
import java.util.List;

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
    private String approvalStatus;
    private Employee createdBy;
    private LocalDateTime createdAt;
    private Employee updatedBy;
    private LocalDateTime updatedAt;

    public String getSupplierName() {
        return supplier.getFirstName() + " " + supplier.getOtherName() + " " + supplier.getLastName();
    }
}
