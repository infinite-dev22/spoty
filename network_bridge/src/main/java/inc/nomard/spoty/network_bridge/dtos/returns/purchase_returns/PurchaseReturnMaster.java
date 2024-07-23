package inc.nomard.spoty.network_bridge.dtos.returns.purchase_returns;

import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import inc.nomard.spoty.network_bridge.dtos.purchases.*;
import java.text.*;
import java.time.*;

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
public class PurchaseReturnMaster {
    private Long id;
    private String ref;
    private LocalDateTime date;
    private Supplier supplier;
    private ArrayList<Branch> branches;
    private List<PurchaseDetail> purchaseDetails;
    private double taxRate;
    private double netTax;
    private double discount;
    private String shipping;
    private double amountPaid;
    private double total;
    private double amountDue;
    private double changeAmount;
    private double balanceAmount;
    private double shippingFee;
    private double subTotal;
    private String status;
    private String paymentStatus;
    private String notes;
    private User createdBy;

    public String getSupplierName() {
        return supplier.getName();
    }

    public String getLocaleDate() {
        return DateFormat.getDateInstance().format(date);
    }

    public String doneBy() {
        return createdBy.getUserProfile().getName();
    }
}
