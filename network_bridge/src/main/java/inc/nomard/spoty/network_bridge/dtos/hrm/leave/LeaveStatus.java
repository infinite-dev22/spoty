package inc.nomard.spoty.network_bridge.dtos.hrm.leave;

import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import java.text.*;
import java.time.*;
import java.util.*;
import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Log
public class LeaveStatus {
    private Long id;
    private User employee;
    private Designation designation;
    private String description;
    private Date startDate;
    private Date endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Duration duration;
    private LeaveType leaveType;
    private String attachment;
    private char status;  // P - Pending, R - Rejected, A - Approved, E - Returned, V - Viewed

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
