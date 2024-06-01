package inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll;

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
public class BeneficiaryBadge {
    private Long id;
    private ArrayList<Branch> branches;
    private String name;
    private BeneficiaryType beneficiaryType;
    private String color;
    private String description;

    public String getBeneficiaryTypeName() {
        return beneficiaryType.getName();
    }
}
