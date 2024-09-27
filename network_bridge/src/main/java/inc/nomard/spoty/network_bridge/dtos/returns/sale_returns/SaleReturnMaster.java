package inc.nomard.spoty.network_bridge.dtos.returns.sale_returns;

import inc.nomard.spoty.network_bridge.dtos.Customer;
import inc.nomard.spoty.network_bridge.dtos.Discount;
import inc.nomard.spoty.network_bridge.dtos.Tax;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Employee;
import inc.nomard.spoty.network_bridge.dtos.sales.SaleDetail;
import lombok.*;
import lombok.extern.java.Log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

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
    private double shippingFee;
    private String saleStatus;
    private String paymentStatus;
    private String notes;
    private Employee createdBy;
    private LocalDateTime createdAt;
    private Employee updatedBy;
    private LocalDateTime updatedAt;

    public String getCustomerName() {
        return customer.getName();
    }

    public String getLocaleDate() {
        return createdAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault()));
    }
}
