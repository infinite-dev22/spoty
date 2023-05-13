package org.infinite.spoty.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class Purchase implements Serializable {
    private final StringProperty purchaseReference = new SimpleStringProperty("");
    private final StringProperty purchaseSupplier = new SimpleStringProperty("");
    private final StringProperty purchaseBranch = new SimpleStringProperty("");
    private final StringProperty purchasePurchaseStatus = new SimpleStringProperty("");
    private final DoubleProperty purchaseGrandTotal = new SimpleDoubleProperty(0);
    private final DoubleProperty purchaseAmountPaid = new SimpleDoubleProperty(0);
    private final DoubleProperty purchaseAmountDue = new SimpleDoubleProperty(0);
    private final StringProperty purchasePaymentStatus = new SimpleStringProperty("");
    private Date purchaseDate = new Date();

    public String getPurchaseDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(this.purchaseDate);
    }

    public void setPurchaseDate(Date date) {
        this.purchaseDate = date;
    }

    public String getPurchaseReference() {
        return this.purchaseReference.get();
    }

    public void setPurchaseReference(String reference) {
        this.purchaseReference.set(reference);
    }

    public String getPurchaseSupplier() {
        return this.purchaseSupplier.get();
    }

    public void setPurchaseSupplier(String supplier) {
        this.purchaseSupplier.set(supplier);
    }

    public String getPurchaseBranch() {
        return this.purchaseBranch.get();
    }

    public void setPurchaseBranch(String warehouse) {
        this.purchaseBranch.set(warehouse);
    }

    public String getPurchaseStatus() {
        return this.purchasePurchaseStatus.get();
    }

    public void setPurchaseStatus(String purchaseStatus) {
        this.purchasePurchaseStatus.set(purchaseStatus);
    }

    public Double getPurchaseGrandTotal() {
        return this.purchaseGrandTotal.get();
    }

    public void setPurchaseGrandTotal(double grandTotal) {
        this.purchaseGrandTotal.set(grandTotal);
    }

    public Double getPurchaseAmountPaid() {
        return this.purchaseAmountPaid.get();
    }

    public void setPurchaseAmountPaid(double amountPaid) {
        this.purchaseAmountPaid.set(amountPaid);
    }

    public Double getPurchaseAmountDue() {
        return this.purchaseAmountDue.get();
    }

    public void setPurchaseAmountDue(double amountDue) {
        this.purchaseAmountDue.set(amountDue);
    }

    public String getPurchasePaymentStatus() {
        return this.purchasePaymentStatus.get();
    }

    public void setPurchasePaymentStatus(String paymentStatus) {
        this.purchasePaymentStatus.set(paymentStatus);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final Purchase other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$purchaseDate = this.getPurchaseDate();
        final Object other$purchaseDate = other.getPurchaseDate();
        if (!Objects.equals(this$purchaseDate, other$purchaseDate))
            return false;
        final Object this$purchaseReference = this.getPurchaseReference();
        final Object other$purchaseReference = other.getPurchaseReference();
        if (!Objects.equals(this$purchaseReference, other$purchaseReference))
            return false;
        final Object this$purchaseSupplier = this.getPurchaseSupplier();
        final Object other$purchaseSupplier = other.getPurchaseSupplier();
        if (!Objects.equals(this$purchaseSupplier, other$purchaseSupplier))
            return false;
        final Object this$purchaseBranch = this.getPurchaseBranch();
        final Object other$purchaseBranch = other.getPurchaseBranch();
        if (!Objects.equals(this$purchaseBranch, other$purchaseBranch))
            return false;
        final Object this$purchaseStatus = this.getPurchaseStatus();
        final Object other$purchaseStatus = other.getPurchaseStatus();
        if (!Objects.equals(this$purchaseStatus, other$purchaseStatus))
            return false;
        final Object this$purchaseGrandTotal = this.getPurchaseGrandTotal();
        final Object other$purchaseGrandTotal = other.getPurchaseGrandTotal();
        if (!Objects.equals(this$purchaseGrandTotal, other$purchaseGrandTotal))
            return false;
        final Object this$purchaseAmountPaid = this.getPurchaseAmountPaid();
        final Object other$purchaseAmountPaid = other.getPurchaseAmountPaid();
        if (!Objects.equals(this$purchaseAmountPaid, other$purchaseAmountPaid))
            return false;
        final Object this$purchaseAmountDue = this.getPurchaseAmountDue();
        final Object other$purchaseAmountDue = other.getPurchaseAmountDue();
        if (!Objects.equals(this$purchaseAmountDue, other$purchaseAmountDue))
            return false;
        final Object this$purchasePaymentStatus = this.getPurchasePaymentStatus();
        final Object other$purchasePaymentStatus = other.getPurchasePaymentStatus();
        return Objects.equals(this$purchasePaymentStatus, other$purchasePaymentStatus);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Purchase;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $purchaseDate = this.getPurchaseDate();
        result = result * PRIME + ($purchaseDate == null ? 43 : $purchaseDate.hashCode());
        final Object $purchaseReference = this.getPurchaseReference();
        result = result * PRIME + ($purchaseReference == null ? 43 : $purchaseReference.hashCode());
        final Object $purchaseSupplier = this.getPurchaseSupplier();
        result = result * PRIME + ($purchaseSupplier == null ? 43 : $purchaseSupplier.hashCode());
        final Object $purchaseBranch = this.getPurchaseBranch();
        result = result * PRIME + ($purchaseBranch == null ? 43 : $purchaseBranch.hashCode());
        final Object $purchaseStatus = this.getPurchaseStatus();
        result = result * PRIME + ($purchaseStatus == null ? 43 : $purchaseStatus.hashCode());
        final Object $purchaseGrandTotal = this.getPurchaseGrandTotal();
        result = result * PRIME + ($purchaseGrandTotal == null ? 43 : $purchaseGrandTotal.hashCode());
        final Object $purchaseAmountPaid = this.getPurchaseAmountPaid();
        result = result * PRIME + ($purchaseAmountPaid == null ? 43 : $purchaseAmountPaid.hashCode());
        final Object $purchaseAmountDue = this.getPurchaseAmountDue();
        result = result * PRIME + ($purchaseAmountDue == null ? 43 : $purchaseAmountDue.hashCode());
        final Object $purchasePaymentStatus = this.getPurchasePaymentStatus();
        result = result * PRIME + ($purchasePaymentStatus == null ? 43 : $purchasePaymentStatus.hashCode());
        return result;
    }

    public String toString() {
        return "PurchaseMaster(purchaseDate=" + this.getPurchaseDate() + ", purchaseReference=" + this.getPurchaseReference() + ", purchaseSupplier=" + this.getPurchaseSupplier() + ", purchaseBranch=" + this.getPurchaseBranch() + ", purchaseStatus=" + this.getPurchaseStatus() + ", purchaseGrandTotal=" + this.getPurchaseGrandTotal() + ", purchaseAmountPaid=" + this.getPurchaseAmountPaid() + ", purchaseAmountDue=" + this.getPurchaseAmountDue() + ", purchasePaymentStatus=" + this.getPurchasePaymentStatus() + ")";
    }
}
