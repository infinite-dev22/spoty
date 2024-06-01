/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

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
