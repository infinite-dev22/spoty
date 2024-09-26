package inc.nomard.spoty.network_bridge.dtos.quotations;

import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
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
public class QuotationMaster {
    private Long id;
    private String ref;
    private Customer customer;
    private List<QuotationDetail> quotationDetails;
    private Tax tax;
    private Discount discount;
    private double shippingFee;
    private double total;
    private String status;
    private String approvalStatus;
    private String notes;
    private Employee createdBy;
    private LocalDateTime createdAt;
    private Employee updatedBy;
    private LocalDateTime updatedAt;

    public String getCustomerName() {
        return customer.getName();
    }
}
