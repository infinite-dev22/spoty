package org.infinite.spoty.data_source.dtos.hrm.leave;

import lombok.*;
import org.infinite.spoty.data_source.dtos.hrm.employee.Designation;
import org.infinite.spoty.data_source.dtos.hrm.employee.User;

import java.text.DateFormat;
import java.time.Duration;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeaveStatus {
    private Long id;
    private User employee;
    private Designation designation;
    private String description;
    private Date startDate;
    private Date endDate;
    private Duration duration;
    private LeaveType leaveType;
    private String attachment;

    public String getLocaleStartDate() {
        return DateFormat.getDateInstance().format(startDate);
    }

    public String getLocaleEndDate() {
        return DateFormat.getDateInstance().format(endDate);
    }

    public String getLocaleDuration() {
        return duration.toString();
    }

    public String getLeaveTypeName() {
        return leaveType.getName();
    }

    public String getEmployeeName() {
        return employee.getName();
    }

    public String getDesignationName() {
        return designation.getName();
    }
}
