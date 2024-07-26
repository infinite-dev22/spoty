package inc.nomard.spoty.network_bridge.dtos.purchases;

import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
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
public class PurchaseMaster {
    private Long id;
    private String ref;
    private LocalDate date;
    private Supplier supplier;
    private List<PurchaseDetail> purchaseDetails;
    private Tax tax;
    private Discount discount;
    private double amountPaid;
    private double total;
    private double amountDue;
    private double subTotal;
    private String purchaseStatus;
    private String paymentStatus;
    private String notes;
    private User createdBy;
    private LocalDateTime createdAt;
    private User updatedBy;
    private LocalDateTime updatedAt;

    public String getSupplierName() {
        return supplier.getName();
    }

    public String getLocaleDate() {
        return DateFormat.getDateInstance().format(date);
    }
}
