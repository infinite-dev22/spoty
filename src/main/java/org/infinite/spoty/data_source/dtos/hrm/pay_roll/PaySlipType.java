package org.infinite.spoty.data_source.dtos.hrm.pay_roll;

import lombok.*;
import org.infinite.spoty.data_source.dtos.Branch;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaySlipType {
    private Long id;
    private ArrayList<Branch> branches;
    private String name;
    private String color;
    private String description;
}
