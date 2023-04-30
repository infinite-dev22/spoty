package org.infinite.spoty.model;

import java.util.Date;

public record Requisition(
        Date requestDate,
        String requestRef,
        String requestBranch,
        String requestBy,
        String requestedSupplier,
        Date approvalDate,
        String approvedBy,
        double itemNumber,
        String itemName,
        String description,
        double itemQuantity,
        String unitOfMeasure,
        double unitCost,
        double totalCost
        ) {
}
