package inc.nomard.spoty.network_bridge.dtos.transfers;

import inc.nomard.spoty.network_bridge.dtos.Branch;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Employee;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferMaster {
    private Long id;
    private String ref;
    private LocalDate date;
    private Branch fromBranch;
    private Branch toBranch;
    private List<TransferDetail> transferDetails;
    private String notes;
    private Employee createdBy;
    private LocalDateTime createdAt;
    private Employee updatedBy;
    private LocalDateTime updatedAt;

    public String getToBranchName() {
        return toBranch.getName();
    }

    public String getFromBranchName() {
        return fromBranch.getName();
    }

    public String getLocaleDate() {
        return DateFormat.getDateInstance().format(date);
    }
}
