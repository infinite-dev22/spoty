package inc.nomard.spoty.network_bridge.dtos.returns.sale_returns;

import inc.nomard.spoty.network_bridge.dtos.*;
import java.text.*;
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
    private Date date;
    private String ref;
    private Customer customer;
    private ArrayList<Branch> branches;
    private List<SaleReturnDetail> saleReturnDetails;
    private double taxRate;
    private double netTax;
    private double discount;
    private double subTotal;
    private double total;
    private double amountPaid;
    private double amountDue;
    private double changeAmount;
    private double balanceAmount;
    private double shippingFee;
    private String paymentStatus;
    private String saleStatus;
    private String notes;

    public String getCustomerName() {
        return customer.getName();
    }

    public String getLocaleDate() {
        return DateFormat.getDateInstance().format(date);
    }
}
