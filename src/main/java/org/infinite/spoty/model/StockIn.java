package org.infinite.spoty.model;

import java.util.Date;

public record StockIn(
        Date stockInDate,
        String stockInRef,
        String stockInBranch,
        String stockInBy,
        Date approvalDate,
        String approvedBy,
        String purchaseOrRequestRef,
        String itemName,
        String unitOfMeasure,
        double itemQuantity,
        double unitCost,
        double totalCost
        ) {
}
