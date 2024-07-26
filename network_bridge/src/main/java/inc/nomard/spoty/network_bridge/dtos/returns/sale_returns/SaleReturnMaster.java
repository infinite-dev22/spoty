package inc.nomard.spoty.network_bridge.dtos.returns.sale_returns;

import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import inc.nomard.spoty.network_bridge.dtos.sales.*;
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
public class SaleReturnMaster {
    private Long id;
    private String ref;
    private Customer customer;
    private List<SaleDetail> saleReturnDetails;
    private Tax tax;
    private Discount discount;
    private double subTotal;
    private double total;
    private double amountPaid;
    private double amountDue;
    private double changeAmount;
    private double shippingFee;
    private String saleStatus;
    private String paymentStatus;
    private String notes;
    private User createdBy;
    private LocalDateTime createdAt;
    private User updatedBy;
    private LocalDateTime updatedAt;

    public String getCustomerName() {
        return customer.getName();
    }

    public String getLocaleDate() {
        return DateFormat.getDateInstance().format(createdAt);
    }
}
