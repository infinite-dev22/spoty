package org.infinite.spoty.data_source.dtos.hrm.pay_roll;

import io.github.palexdev.mfxcore.base.properties.CharProperty;
import lombok.*;
import org.infinite.spoty.data_source.dtos.Branch;
import org.infinite.spoty.viewModels.hrm.pay_roll.PaySlipViewModel;

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
