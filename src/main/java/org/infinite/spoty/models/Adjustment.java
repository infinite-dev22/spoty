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

package org.infinite.spoty.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Deprecated
public class Adjustment implements Serializable {
    private final StringProperty adjustmentReference = new SimpleStringProperty("");
    private final StringProperty adjustmentBranch = new SimpleStringProperty("");
    private final DoubleProperty adjustmentTotalProducts = new SimpleDoubleProperty(0);
    private Date adjustmentDate = new Date();

    public String getAdjustmentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(this.adjustmentDate);
    }

    public void setAdjustmentDate(Date adjustmentDate) {
        this.adjustmentDate = adjustmentDate;
    }

    public String getAdjustmentReference() {
        return this.adjustmentReference.get();
    }

    public void setAdjustmentReference(String adjustmentReference) {
        this.adjustmentReference.set(adjustmentReference);
    }

    public String getAdjustmentBranch() {
        return this.adjustmentBranch.get();
    }

    public void setAdjustmentBranch(String adjustmentBranch) {
        this.adjustmentBranch.set(adjustmentBranch);
    }

    public double getAdjustmentTotalProducts() {
        return this.adjustmentTotalProducts.get();
    }

    public void setAdjustmentTotalProducts(double adjustmentTotalProducts) {
        this.adjustmentTotalProducts.set(adjustmentTotalProducts);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final Adjustment other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$adjustmentDate = this.getAdjustmentDate();
        final Object other$adjustmentDate = other.getAdjustmentDate();
        if (!Objects.equals(this$adjustmentDate, other$adjustmentDate))
            return false;
        final Object this$adjustmentReference = this.getAdjustmentReference();
        final Object other$adjustmentReference = other.getAdjustmentReference();
        if (!Objects.equals(this$adjustmentReference, other$adjustmentReference))
            return false;
        final Object this$adjustmentBranch = this.getAdjustmentBranch();
        final Object other$adjustmentBranch = other.getAdjustmentBranch();
        if (!Objects.equals(this$adjustmentBranch, other$adjustmentBranch))
            return false;
        final Object this$adjustmentTotalProducts = this.getAdjustmentTotalProducts();
        final Object other$adjustmentTotalProducts = other.getAdjustmentTotalProducts();
        return Objects.equals(this$adjustmentTotalProducts, other$adjustmentTotalProducts);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Adjustment;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $adjustmentDate = this.getAdjustmentDate();
        result = result * PRIME + ($adjustmentDate == null ? 43 : $adjustmentDate.hashCode());
        final Object $adjustmentReference = this.getAdjustmentReference();
        result = result * PRIME + ($adjustmentReference == null ? 43 : $adjustmentReference.hashCode());
        final Object $adjustmentBranch = this.getAdjustmentBranch();
        result = result * PRIME + ($adjustmentBranch == null ? 43 : $adjustmentBranch.hashCode());
        final Object $adjustmentTotalProducts = this.getAdjustmentTotalProducts();
        result = result * PRIME + $adjustmentTotalProducts.hashCode();
        return result;
    }

    public String toString() {
        return "Adjustment(adjustmentDate=" + this.getAdjustmentDate() + ", adjustmentReference=" + this.getAdjustmentReference() + ", adjustmentBranch=" + this.getAdjustmentBranch() + ", adjustmentTotalProducts=" + this.getAdjustmentTotalProducts() + ")";
    }
}
