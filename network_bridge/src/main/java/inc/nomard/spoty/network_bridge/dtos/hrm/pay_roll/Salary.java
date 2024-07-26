package inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll;

import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class Salary {
    private Long id;
    private User employee;
    private PaySlip paySlip;
    private String status;  // P - Pending, R - Rejected, A - Approved, E - Returned, V - Viewed, G - Generated, S - Sent
    private Double salary;
    private Double netSalary;

    public String getEmployeeName() {
        return employee.getName();
    }

    public String getStatusName() {
        return switch (status) {
            case "P" -> "Pending";
            case "R" -> "Rejected";
            case "A" -> "Approved";
            case "E" -> "Returned";
            case "V" -> "Viewed";
            default -> throw new IllegalStateException("Unexpected value: " + status);
        };
    }
}
