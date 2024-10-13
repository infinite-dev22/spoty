package inc.nomard.spoty.network_bridge.dtos.purchases;

import inc.nomard.spoty.network_bridge.dtos.Discount;
import inc.nomard.spoty.network_bridge.dtos.Supplier;
import inc.nomard.spoty.network_bridge.dtos.Tax;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Employee;
import lombok.*;
import lombok.extern.log4j.Log4j2;

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
@Log4j2
public class PurchaseMaster {
    private Long id;
    private String ref;
    private LocalDate date;
    private Supplier supplier;
    private List<PurchaseDetail> purchaseDetails;
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());
        return date.format(dtf);
    }
}
