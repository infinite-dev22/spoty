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
public class PurchaseReturn implements Serializable {
    private final StringProperty purchaseReturnReference = new SimpleStringProperty("");
    private final StringProperty purchaseReturnSupplier = new SimpleStringProperty("");
    private final StringProperty purchaseReturnBranch = new SimpleStringProperty("");
    private final StringProperty purchaseReturnPurchaseRef = new SimpleStringProperty("");
    private final StringProperty purchaseReturnPurchaseStatus = new SimpleStringProperty("");
    private final DoubleProperty purchaseReturnGrandTotal = new SimpleDoubleProperty(0);
    private final DoubleProperty purchaseReturnAmountPaid = new SimpleDoubleProperty(0);
    private final DoubleProperty purchaseReturnAmountDue = new SimpleDoubleProperty(0);
    private final StringProperty purchaseReturnPaymentStatus = new SimpleStringProperty("");
    private Date purchaseReturnDate = new Date();

    public String getPurchaseReturnDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(this.purchaseReturnDate);
    }

    public void setPurchaseReturnDate(Date date) {
        this.purchaseReturnDate = date;
    }

    public String getPurchaseReturnReference() {
        return this.purchaseReturnReference.get();
    }

    public void setPurchaseReturnReference(String reference) {
        this.purchaseReturnReference.set(reference);
    }

    public String getPurchaseReturnSupplier() {
        return this.purchaseReturnSupplier.get();
    }

    public void setPurchaseReturnSupplier(String supplier) {
        this.purchaseReturnSupplier.set(supplier);
    }

    public String getPurchaseReturnBranch() {
        return this.purchaseReturnBranch.get();
    }

    public void setPurchaseReturnBranch(String warehouse) {
        this.purchaseReturnBranch.set(warehouse);
    }

    public String getPurchaseReturnRef() {
        return this.purchaseReturnPurchaseRef.get();
    }

    public void setPurchaseReturnRef(String purchaseReturnRef) {
        this.purchaseReturnPurchaseRef.set(purchaseReturnRef);
    }

    public String getPurchaseReturnStatus() {
        return this.purchaseReturnPurchaseStatus.get();
    }

    public void setPurchaseReturnStatus(String purchaseReturnStatus) {
        this.purchaseReturnPurchaseStatus.set(purchaseReturnStatus);
    }

    public Double getPurchaseReturnGrandTotal() {
        return this.purchaseReturnGrandTotal.get();
    }

    public void setPurchaseReturnGrandTotal(double grandTotal) {
        this.purchaseReturnGrandTotal.set(grandTotal);
    }

    public Double getPurchaseReturnAmountPaid() {
        return this.purchaseReturnAmountPaid.get();
    }

    public void setPurchaseReturnAmountPaid(double amountPaid) {
        this.purchaseReturnAmountPaid.set(amountPaid);
    }

    public Double getPurchaseReturnAmountDue() {
        return this.purchaseReturnAmountDue.get();
    }

    public void setPurchaseReturnAmountDue(double amountDue) {
        this.purchaseReturnAmountDue.set(amountDue);
    }

    public String getPurchaseReturnPaymentStatus() {
        return this.purchaseReturnPaymentStatus.get();
    }

    public void setPurchaseReturnPaymentStatus(String paymentStatus) {
        this.purchaseReturnPaymentStatus.set(paymentStatus);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final PurchaseReturn other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$purchaseReturnDate = this.getPurchaseReturnDate();
        final Object other$purchaseReturnDate = other.getPurchaseReturnDate();
        if (!Objects.equals(this$purchaseReturnDate, other$purchaseReturnDate))
            return false;
        final Object this$purchaseReturnReference = this.getPurchaseReturnReference();
        final Object other$purchaseReturnReference = other.getPurchaseReturnReference();
        if (!Objects.equals(this$purchaseReturnReference, other$purchaseReturnReference))
            return false;
        final Object this$purchaseReturnSupplier = this.getPurchaseReturnSupplier();
        final Object other$purchaseReturnSupplier = other.getPurchaseReturnSupplier();
        if (!Objects.equals(this$purchaseReturnSupplier, other$purchaseReturnSupplier))
            return false;
        final Object this$purchaseReturnBranch = this.getPurchaseReturnBranch();
        final Object other$purchaseReturnBranch = other.getPurchaseReturnBranch();
        if (!Objects.equals(this$purchaseReturnBranch, other$purchaseReturnBranch))
            return false;
        final Object this$purchaseReturnRef = this.getPurchaseReturnRef();
        final Object other$purchaseReturnRef = other.getPurchaseReturnRef();
        if (!Objects.equals(this$purchaseReturnRef, other$purchaseReturnRef))
            return false;
        final Object this$purchaseReturnStatus = this.getPurchaseReturnStatus();
        final Object other$purchaseReturnStatus = other.getPurchaseReturnStatus();
        if (!Objects.equals(this$purchaseReturnStatus, other$purchaseReturnStatus))
            return false;
        final Object this$purchaseReturnGrandTotal = this.getPurchaseReturnGrandTotal();
        final Object other$purchaseReturnGrandTotal = other.getPurchaseReturnGrandTotal();
        if (!Objects.equals(this$purchaseReturnGrandTotal, other$purchaseReturnGrandTotal))
            return false;
        final Object this$purchaseReturnAmountPaid = this.getPurchaseReturnAmountPaid();
        final Object other$purchaseReturnAmountPaid = other.getPurchaseReturnAmountPaid();
        if (!Objects.equals(this$purchaseReturnAmountPaid, other$purchaseReturnAmountPaid))
            return false;
        final Object this$purchaseReturnAmountDue = this.getPurchaseReturnAmountDue();
        final Object other$purchaseReturnAmountDue = other.getPurchaseReturnAmountDue();
        if (!Objects.equals(this$purchaseReturnAmountDue, other$purchaseReturnAmountDue))
            return false;
        final Object this$purchaseReturnPaymentStatus = this.getPurchaseReturnPaymentStatus();
        final Object other$purchaseReturnPaymentStatus = other.getPurchaseReturnPaymentStatus();
        return Objects.equals(this$purchaseReturnPaymentStatus, other$purchaseReturnPaymentStatus);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PurchaseReturn;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $purchaseReturnDate = this.getPurchaseReturnDate();
        result = result * PRIME + ($purchaseReturnDate == null ? 43 : $purchaseReturnDate.hashCode());
        final Object $purchaseReturnReference = this.getPurchaseReturnReference();
        result = result * PRIME + ($purchaseReturnReference == null ? 43 : $purchaseReturnReference.hashCode());
        final Object $purchaseReturnSupplier = this.getPurchaseReturnSupplier();
        result = result * PRIME + ($purchaseReturnSupplier == null ? 43 : $purchaseReturnSupplier.hashCode());
        final Object $purchaseReturnBranch = this.getPurchaseReturnBranch();
        result = result * PRIME + ($purchaseReturnBranch == null ? 43 : $purchaseReturnBranch.hashCode());
        final Object $purchaseReturnRef = this.getPurchaseReturnRef();
        result = result * PRIME + ($purchaseReturnRef == null ? 43 : $purchaseReturnRef.hashCode());
        final Object $purchaseReturnStatus = this.getPurchaseReturnStatus();
        result = result * PRIME + ($purchaseReturnStatus == null ? 43 : $purchaseReturnStatus.hashCode());
        final Object $purchaseReturnGrandTotal = this.getPurchaseReturnGrandTotal();
        result = result * PRIME + ($purchaseReturnGrandTotal == null ? 43 : $purchaseReturnGrandTotal.hashCode());
        final Object $purchaseReturnAmountPaid = this.getPurchaseReturnAmountPaid();
        result = result * PRIME + ($purchaseReturnAmountPaid == null ? 43 : $purchaseReturnAmountPaid.hashCode());
        final Object $purchaseReturnAmountDue = this.getPurchaseReturnAmountDue();
        result = result * PRIME + ($purchaseReturnAmountDue == null ? 43 : $purchaseReturnAmountDue.hashCode());
        final Object $purchaseReturnPaymentStatus = this.getPurchaseReturnPaymentStatus();
        result = result * PRIME + ($purchaseReturnPaymentStatus == null ? 43 : $purchaseReturnPaymentStatus.hashCode());
        return result;
    }

    public String toString() {
        return "PurchaseReturnMaster(purchaseReturnDate=" + this.getPurchaseReturnDate() + ", purchaseReturnReference=" + this.getPurchaseReturnReference() + ", purchaseReturnSupplier=" + this.getPurchaseReturnSupplier() + ", purchaseReturnBranch=" + this.getPurchaseReturnBranch() + ", purchaseReturnRef=" + this.getPurchaseReturnRef() + ", purchaseReturnStatus=" + this.getPurchaseReturnStatus() + ", purchaseReturnGrandTotal=" + this.getPurchaseReturnGrandTotal() + ", purchaseReturnAmountPaid=" + this.getPurchaseReturnAmountPaid() + ", purchaseReturnAmountDue=" + this.getPurchaseReturnAmountDue() + ", purchaseReturnPaymentStatus=" + this.getPurchaseReturnPaymentStatus() + ")";
    }
}
