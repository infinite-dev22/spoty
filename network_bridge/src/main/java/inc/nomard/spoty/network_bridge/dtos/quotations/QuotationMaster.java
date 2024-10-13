package inc.nomard.spoty.network_bridge.dtos.quotations;

import inc.nomard.spoty.network_bridge.dtos.Customer;
import inc.nomard.spoty.network_bridge.dtos.Discount;
import inc.nomard.spoty.network_bridge.dtos.Tax;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Employee;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
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
