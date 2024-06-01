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
public class SalaryAdvance {
    private Long id;
    private User employee;
    private PaySlip paySlip;
    private String status;
    private double salary;
    private double netSalary;

    public String getEmployeeName() {
        return employee.getName();
    }

    public String getPaySlipPeriod() {
        return paySlip.getLocaleStartDate() + "-" + paySlip.getLocaleEndDate();
    }
}
