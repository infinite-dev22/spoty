package inc.nomard.spoty.network_bridge.dtos.returns.purchase_returns;

import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import inc.nomard.spoty.network_bridge.dtos.purchases.*;
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
public class PurchaseReturnMaster {
    private Long id;
    private String ref;
    private LocalDate date;
    private Supplier supplier;
    private List<PurchaseDetail> purchaseReturnDetails;
    private Tax tax;
    private Discount discount;
    private double amountPaid;
    private double total;
    private double amountDue;
    private double subTotal;
    private String purchaseStatus;
    private String paymentStatus;
    private String notes;
    private Employee createdBy;
    private LocalDateTime createdAt;
    private Employee updatedBy;
    private LocalDateTime updatedAt;

    public String getSupplierName() {
        return supplier.getName();
    }

    public String getLocaleDate() {
        return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault()));
    }
}
