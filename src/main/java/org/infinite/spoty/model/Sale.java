package org.infinite.spoty.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Sale implements Serializable {
    private Date saleDate = new Date();
    private final StringProperty saleReference = new SimpleStringProperty("");
    private final StringProperty saleAddedBy = new SimpleStringProperty("");
    private final StringProperty saleCustomer = new SimpleStringProperty("");
    private final StringProperty saleWarehouse = new SimpleStringProperty("");
    private final StringProperty saleSaleStatus = new SimpleStringProperty("");
    private final DoubleProperty saleGrandTotal = new SimpleDoubleProperty(0);
    private final DoubleProperty saleAmountPaid = new SimpleDoubleProperty(0);
    private final DoubleProperty saleAmountDue = new SimpleDoubleProperty(0);
    private final StringProperty salePaymentStatus = new SimpleStringProperty("");

    public String getSaleDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(this.saleDate);
    }

    public String getSaleReference() {
        return this.saleReference.get();
    }

    public String getSaleAddedBy() {
        return this.saleAddedBy.get();
    }

    public String getSaleCustomer() {
        return this.saleCustomer.get();
    }

    public String getSaleWarehouse() {
        return this.saleWarehouse.get();
    }

    public String getSaleStatus() {
        return this.saleSaleStatus.get();
    }

    public Double getSaleGrandTotal() {
        return this.saleGrandTotal.get();
    }

    public Double getSaleAmountPaid() {
        return this.saleAmountPaid.get();
    }

    public Double getSaleAmountDue() {
        return this.saleAmountDue.get();
    }

    public String getSalePaymentStatus() {
        return this.salePaymentStatus.get();
    }

    public void setSaleDate(Date date) {
        this.saleDate = date;
    }

    public void setSaleReference(String reference) {
        this.saleReference.set(reference);
    }

    public void setSaleAddedBy(String addedBy) {
        this.saleAddedBy.set(addedBy);
    }

    public void setSaleCustomer(String customer) {
        this.saleCustomer.set(customer);
    }

    public void setSaleWarehouse(String warehouse) {
        this.saleWarehouse.set(warehouse);
    }

    public void setSaleStatus(String saleStatus) {
        this.saleSaleStatus.set(saleStatus);
    }

    public void setSaleGrandTotal(double grandTotal) {
        this.saleGrandTotal.set(grandTotal);
    }

    public void setSaleAmountPaid(double amountPaid) {
        this.saleAmountPaid.set(amountPaid);
    }

    public void setSaleAmountDue(double amountDue) {
        this.saleAmountDue.set(amountDue);
    }

    public void setSalePaymentStatus(String paymentStatus) {
        this.salePaymentStatus.set(paymentStatus);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final Sale other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$saleDate = this.getSaleDate();
        final Object other$saleDate = other.getSaleDate();
        if (!Objects.equals(this$saleDate, other$saleDate))
            return false;
        final Object this$saleReference = this.getSaleReference();
        final Object other$saleReference = other.getSaleReference();
        if (!Objects.equals(this$saleReference, other$saleReference))
            return false;
        final Object this$saleAddedBy = this.getSaleAddedBy();
        final Object other$saleAddedBy = other.getSaleAddedBy();
        if (!Objects.equals(this$saleAddedBy, other$saleAddedBy))
            return false;
        final Object this$saleCustomer = this.getSaleCustomer();
        final Object other$saleCustomer = other.getSaleCustomer();
        if (!Objects.equals(this$saleCustomer, other$saleCustomer))
            return false;
        final Object this$saleWarehouse = this.getSaleWarehouse();
        final Object other$saleWarehouse = other.getSaleWarehouse();
        if (!Objects.equals(this$saleWarehouse, other$saleWarehouse))
            return false;
        final Object this$saleSaleStatus = this.getSaleStatus();
        final Object other$saleSaleStatus = other.getSaleStatus();
        if (!Objects.equals(this$saleSaleStatus, other$saleSaleStatus))
            return false;
        final Object this$saleGrandTotal = this.getSaleGrandTotal();
        final Object other$saleGrandTotal = other.getSaleGrandTotal();
        if (!Objects.equals(this$saleGrandTotal, other$saleGrandTotal))
            return false;
        final Object this$saleAmountPaid = this.getSaleAmountPaid();
        final Object other$saleAmountPaid = other.getSaleAmountPaid();
        if (!Objects.equals(this$saleAmountPaid, other$saleAmountPaid))
            return false;
        final Object this$saleAmountDue = this.getSaleAmountDue();
        final Object other$saleAmountDue = other.getSaleAmountDue();
        if (!Objects.equals(this$saleAmountDue, other$saleAmountDue))
            return false;
        final Object this$salePaymentStatus = this.getSalePaymentStatus();
        final Object other$salePaymentStatus = other.getSalePaymentStatus();
        return Objects.equals(this$salePaymentStatus, other$salePaymentStatus);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Sale;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $saleDate = this.getSaleDate();
        result = result * PRIME + ($saleDate == null ? 43 : $saleDate.hashCode());
        final Object $saleReference = this.getSaleReference();
        result = result * PRIME + ($saleReference == null ? 43 : $saleReference.hashCode());
        final Object $saleAddedBy = this.getSaleAddedBy();
        result = result * PRIME + ($saleAddedBy == null ? 43 : $saleAddedBy.hashCode());
        final Object $saleCustomer = this.getSaleCustomer();
        result = result * PRIME + ($saleCustomer == null ? 43 : $saleCustomer.hashCode());
        final Object $saleWarehouse = this.getSaleWarehouse();
        result = result * PRIME + ($saleWarehouse == null ? 43 : $saleWarehouse.hashCode());
        final Object $saleSaleStatus = this.getSaleStatus();
        result = result * PRIME + ($saleSaleStatus == null ? 43 : $saleSaleStatus.hashCode());
        final Object $saleGrandTotal = this.getSaleGrandTotal();
        result = result * PRIME + ($saleGrandTotal == null ? 43 : $saleGrandTotal.hashCode());
        final Object $saleAmountPaid = this.getSaleAmountPaid();
        result = result * PRIME + ($saleAmountPaid == null ? 43 : $saleAmountPaid.hashCode());
        final Object $saleAmountDue = this.getSaleAmountDue();
        result = result * PRIME + ($saleAmountDue == null ? 43 : $saleAmountDue.hashCode());
        final Object $salePaymentStatus = this.getSalePaymentStatus();
        result = result * PRIME + ($salePaymentStatus == null ? 43 : $salePaymentStatus.hashCode());
        return result;
    }

    public String toString() {
        return "Sale(saleDate=" + this.getSaleDate() + ", saleReference=" + this.getSaleReference() + ", saleAddedBy=" + this.getSaleAddedBy() + ", saleCustomer=" + this.getSaleCustomer() + ", saleWarehouse=" + this.getSaleWarehouse() + ", saleSaleStatus=" + this.getSaleStatus() + ", saleGrandTotal=" + this.getSaleGrandTotal() + ", saleAmountPaid=" + this.getSaleAmountPaid() + ", saleAmountDue=" + this.getSaleAmountDue() + ", salePaymentStatus=" + this.getSalePaymentStatus() + ")";
    }
}
