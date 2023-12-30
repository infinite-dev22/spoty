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

package org.infinite.spoty.data_source.daos.transfers;

import lombok.*;
import org.infinite.spoty.data_source.daos.Branch;
import org.infinite.spoty.data_source.daos.User;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferMaster {
    private Long id;
    private String ref;
    private Date date;
    private Branch fromBranch;
    private Branch toBranch;
    private List<TransferDetail> transferDetails;
    private String shipping;
    private double total;
    private String status;
    private User approvedBy;
    private User receivedBy;
    private Date approvalDate;
    private Date receiveDate;
    private String notes;

    public String getToBranchName() {
        return toBranch.getName();
    }

    public String getFromBranchName() {
        return fromBranch.getName();
    }

    public String getLocaleDate() {
        return DateFormat.getDateInstance().format(date);
    }
}
