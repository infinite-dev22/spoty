package inc.nomard.spoty.network_bridge.dtos.transfers;

import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import java.text.*;
import java.util.*;
import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Log
public class TransferMaster {
    private Long id;
    private String ref;
    private Date date;
    private Branch fromBranch;
    private Branch toBranch;
    private List<TransferDetail> transferDetails;
    private String notes;

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
