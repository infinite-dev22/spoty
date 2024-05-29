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

package inc.nomard.spoty.network_bridge.dtos.purchases;

import inc.nomard.spoty.network_bridge.dtos.Supplier;
import lombok.*;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Slf4j
public class PurchaseMaster {
    private Long id;
    private String ref;
    private Date date;
    private Supplier supplier;
    private List<PurchaseDetail> purchaseDetails;
    private double taxRate;
    private double netTax;
    private double discount;
    private double amountPaid;
    private double total;
    private double amountDue;
    private double subTotal;
    private String purchaseStatus;
    private String paymentStatus;
    private String notes;

    public String getSupplierName() {
        return supplier.getName();
    }

    public String getLocaleDate() {
        return DateFormat.getDateInstance().format(date);
    }
}
