package inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll;

import lombok.*;
import lombok.extern.java.Log;

import java.text.DateFormat;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class PaySlip {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int salariesQuantity;
    private String status;  // P - Pending, R - Rejected, A - Approved, E - Returned, V - Viewed, G - Generated, g - Generating, S - Sent
    private LocalDateTime createdOn;
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
}
