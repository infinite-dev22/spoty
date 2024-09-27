package inc.nomard.spoty.network_bridge.dtos.returns.purchase_returns;

import inc.nomard.spoty.network_bridge.dtos.Discount;
import inc.nomard.spoty.network_bridge.dtos.Supplier;
import inc.nomard.spoty.network_bridge.dtos.Tax;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Employee;
import inc.nomard.spoty.network_bridge.dtos.purchases.PurchaseDetail;
import lombok.*;
import lombok.extern.java.Log;

import java.time.LocalDate;
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
public class PurchaseReturnMaster {
    private Long id;
    private String ref;
    private LocalDate date;
    private Supplier supplier;
    private List<PurchaseDetail> purchaseReturnDetails;
    private Tax tax;
    private Discount discount;
    private double amountPaid;
    private double amountDue;
    private double shippingFee;
    private double total;
    private double subTotal;
    private String approvalStatus;
    private String purchaseStatus;
    private String paymentStatus;
    private String notes;
    private Employee createdBy;
    private LocalDateTime createdAt;
    private Employee updatedBy;
    private LocalDateTime updatedAt;

    public String getSupplierName() {
        return supplier.getFirstName() + " " + supplier.getOtherName() + " " + supplier.getLastName();
    }

    public String getLocaleDate() {
        return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault()));
    }
}
