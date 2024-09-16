package inc.nomard.spoty.network_bridge.dtos.sales;

import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class SaleMaster {
    private Long id;
    private String ref;
    private Customer customer;
    private List<SaleDetail> saleDetails;
    private Tax tax;
    private Discount discount;
    private double subTotal;
    private double total;
    private double amountPaid;
    private double amountDue;
    private double changeAmount;
    private double shippingFee;
    private String paymentStatus;
    private String saleStatus;
    private String notes;
    private Employee createdBy;
    private LocalDateTime createdAt;
    private Employee updatedBy;
    private LocalDateTime updatedAt;

    public String getCustomerName() {
        return customer.getName();
    }

    public String getLocaleDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());
        return createdAt.format(dtf);
    }
}
