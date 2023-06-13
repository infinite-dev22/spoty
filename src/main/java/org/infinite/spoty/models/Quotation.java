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
public class Quotation implements Serializable {
    private final StringProperty quotationReference = new SimpleStringProperty("");
    private final StringProperty quotationCustomer = new SimpleStringProperty("");
    private final StringProperty quotationBranch = new SimpleStringProperty("");
    private final StringProperty quotationStatus = new SimpleStringProperty("");
    private final DoubleProperty quotationGrandTotal = new SimpleDoubleProperty(0);
    private Date quotationDate = new Date();

    public String getQuotationDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(this.quotationDate);
    }

    public void setQuotationDate(Date date) {
        this.quotationDate = date;
    }

    public String getQuotationReference() {
        return this.quotationReference.get();
    }

    public void setQuotationReference(String reference) {
        this.quotationReference.set(reference);
    }

    public String getQuotationCustomer() {
        return this.quotationCustomer.get();
    }

    public void setQuotationCustomer(String customer) {
        this.quotationCustomer.set(customer);
    }

    public String getQuotationBranch() {
        return this.quotationBranch.get();
    }

    public void setQuotationBranch(String warehouse) {
        this.quotationBranch.set(warehouse);
    }

    public String getQuotationStatus() {
        return this.quotationStatus.get();
    }

    public void setQuotationStatus(String status) {
        this.quotationStatus.set(status);
    }

    public double getQuotationGrandTotal() {
        return this.quotationGrandTotal.get();
    }

    public void setQuotationGrandTotal(double grandTotal) {
        this.quotationGrandTotal.set(grandTotal);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final Quotation other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$quotationDate = this.getQuotationDate();
        final Object other$quotationDate = other.getQuotationDate();
        if (!Objects.equals(this$quotationDate, other$quotationDate))
            return false;
        final Object this$quotationReference = this.getQuotationReference();
        final Object other$quotationReference = other.getQuotationReference();
        if (!Objects.equals(this$quotationReference, other$quotationReference))
            return false;
        final Object this$quotationCustomer = this.getQuotationCustomer();
        final Object other$quotationCustomer = other.getQuotationCustomer();
        if (!Objects.equals(this$quotationCustomer, other$quotationCustomer))
            return false;
        final Object this$quotationBranch = this.getQuotationBranch();
        final Object other$quotationBranch = other.getQuotationBranch();
        if (!Objects.equals(this$quotationBranch, other$quotationBranch))
            return false;
        final Object this$quotationStatus = this.getQuotationStatus();
        final Object other$quotationStatus = other.getQuotationStatus();
        if (!Objects.equals(this$quotationStatus, other$quotationStatus))
            return false;
        final Object this$quotationGrandTotal = this.getQuotationGrandTotal();
        final Object other$quotationGrandTotal = other.getQuotationGrandTotal();
        return this$quotationGrandTotal.equals(other$quotationGrandTotal);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Quotation;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $quotationDate = this.getQuotationDate();
        result = result * PRIME + ($quotationDate == null ? 43 : $quotationDate.hashCode());
        final Object $quotationReference = this.getQuotationReference();
        result = result * PRIME + ($quotationReference == null ? 43 : $quotationReference.hashCode());
        final Object $quotationCustomer = this.getQuotationCustomer();
        result = result * PRIME + ($quotationCustomer == null ? 43 : $quotationCustomer.hashCode());
        final Object $quotationBranch = this.getQuotationBranch();
        result = result * PRIME + ($quotationBranch == null ? 43 : $quotationBranch.hashCode());
        final Object $quotationStatus = this.getQuotationStatus();
        result = result * PRIME + ($quotationStatus == null ? 43 : $quotationStatus.hashCode());
        final Object $quotationGrandTotal = this.getQuotationGrandTotal();
        result = result * PRIME + $quotationGrandTotal.hashCode();
        return result;
    }

    public String toString() {
        return "Quotation(quotationDate=" + this.getQuotationDate() + ", quotationReference=" + this.getQuotationReference() + ", quotationCustomer=" + this.getQuotationCustomer() + ", quotationBranch=" + this.getQuotationBranch() + ", quotationStatus=" + this.getQuotationStatus() + ", quotationGrandTotal=" + this.getQuotationGrandTotal() + ")";
    }
}
