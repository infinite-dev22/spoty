package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Salary {
    private Long id;
    private ArrayList<Branch> branches;
    private String salaryMonth;
    private String employeeName;
    private Double salary;
}
