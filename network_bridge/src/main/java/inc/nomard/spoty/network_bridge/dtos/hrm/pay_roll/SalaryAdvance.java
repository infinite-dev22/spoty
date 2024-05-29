package inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll;

import inc.nomard.spoty.network_bridge.dtos.hrm.employee.User;
import lombok.*;
import lombok.extern.slf4j.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Slf4j
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
