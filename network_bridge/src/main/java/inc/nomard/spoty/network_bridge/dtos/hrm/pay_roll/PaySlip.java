package inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll;

import lombok.*;
import lombok.extern.java.Log;

import java.text.DateFormat;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Log
public class PaySlip {
    private Long id;
    private Date startDate;
    private Date endDate;
    private int salariesQuantity;
    private char status;  // P - Pending, R - Rejected, A - Approved, E - Returned, V - Viewed, G - Generated, g - Generating, S - Sent
    private Date createdOn;
    private String message;

    public String getLocaleStartDate() {
        return DateFormat.getDateInstance().format(startDate);
    }

    public String getLocaleEndDate() {
        return DateFormat.getDateInstance().format(endDate);
    }

    public String getLocaleCreatedDate() {
        return DateFormat.getDateInstance().format(createdOn);
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
