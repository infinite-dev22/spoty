package org.infinite.spoty.data_source.dtos.hrm.pay_roll;

import lombok.*;
import org.infinite.spoty.data_source.dtos.hrm.employee.Designation;
import org.infinite.spoty.data_source.dtos.hrm.employee.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Salary {
    private Long id;
    private User employee;
    private Designation designation;
    private String period;
    private PaySlipType paySlipType;
    private char status;  // P - Pending, R - Rejected, A - Approved, E - Returned, V - Viewed, G - Generated, S - Sent
    private String salary;
    private String netSalary;

    public String getLeaveTypeName() {
        return paySlipType.getName();
    }

    public String getEmployeeName() {
        return employee.getName();
    }

    public String getDesignationName() {
        return designation.getName();
    }

    public String getStatusName() {
        return switch (status) {
            case 'P' -> "Pending";
            case 'R' -> "Rejected";
            case 'A' -> "Approved";
            case 'E' -> "Returned";
            case 'V' -> "Viewed";
            default -> throw new IllegalStateException("Unexpected value: " + status);
        };
    }
}
