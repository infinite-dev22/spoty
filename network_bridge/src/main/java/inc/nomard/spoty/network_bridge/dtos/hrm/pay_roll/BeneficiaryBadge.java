package inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll;

import inc.nomard.spoty.network_bridge.dtos.Branch;
import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
