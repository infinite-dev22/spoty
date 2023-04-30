package org.infinite.spoty.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class SaleReturn implements Serializable {
    private Date saleReturnDate = new Date();
    private final StringProperty saleReturnReference = new SimpleStringProperty("");
    private final StringProperty saleReturnCustomer = new SimpleStringProperty("");
    private final StringProperty saleReturnBranch = new SimpleStringProperty("");
    private final StringProperty saleReturnSaleRef = new SimpleStringProperty("");
    private final StringProperty saleReturnStatus = new SimpleStringProperty("");
    private final DoubleProperty saleReturnGrandTotal = new SimpleDoubleProperty(0);
    private final DoubleProperty saleReturnAmountPaid = new SimpleDoubleProperty(0);
    private final DoubleProperty saleReturnAmountDue = new SimpleDoubleProperty(0);
    private final StringProperty saleReturnPaymentStatus = new SimpleStringProperty("");

    public String getSaleReturnDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(this.saleReturnDate);
    }

    public String getSaleReturnReference() {
        return this.saleReturnReference.get();
    }

    public String getSaleReturnCustomer() {
        return this.saleReturnCustomer.get();
    }

    public String getSaleReturnBranch() {
        return this.saleReturnBranch.get();
    }

    public String getSaleReturnSaleRef() {
        return this.saleReturnSaleRef.get();
    }

    public String getSaleReturnStatus() {
        return this.saleReturnStatus.get();
    }

    public Double getSaleReturnGrandTotal() {
        return this.saleReturnGrandTotal.get();
    }

    public Double getSaleReturnAmountPaid() {
        return this.saleReturnAmountPaid.get();
    }

    public Double getSaleReturnAmountDue() {
        return this.saleReturnAmountDue.get();
    }

    public String getSaleReturnPaymentStatus() {
        return this.saleReturnPaymentStatus.get();
    }

    public void setSaleReturnDate(Date date) {
        this.saleReturnDate = date;
    }

    public void setSaleReturnReference(String reference) {
        this.saleReturnReference.set(reference);
    }

    public void setSaleReturnCustomer(String customer) {
        this.saleReturnCustomer.set(customer);
    }

    public void setSaleReturnBranch(String warehouse) {
        this.saleReturnBranch.set(warehouse);
    }

    public void setSaleReturnSaleRef(String saleReturnSaleRef) {
        this.saleReturnStatus.set(saleReturnSaleRef);
    }

    public void setSaleReturnStatus(String saleReturnStatus) {
        this.saleReturnStatus.set(saleReturnStatus);
    }

    public void setSaleReturnGrandTotal(double grandTotal) {
        this.saleReturnGrandTotal.set(grandTotal);
    }

    public void setSaleReturnAmountPaid(double amountPaid) {
        this.saleReturnAmountPaid.set(amountPaid);
    }

    public void setSaleReturnAmountDue(double amountDue) {
        this.saleReturnAmountDue.set(amountDue);
    }

    public void setSaleReturnPaymentStatus(String paymentStatus) {
        this.saleReturnPaymentStatus.set(paymentStatus);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final SaleReturn other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$saleReturnDate = this.getSaleReturnDate();
        final Object other$saleReturnDate = other.getSaleReturnDate();
        if (!Objects.equals(this$saleReturnDate, other$saleReturnDate))
            return false;
        final Object this$saleReturnReference = this.getSaleReturnReference();
        final Object other$saleReturnReference = other.getSaleReturnReference();
        if (!Objects.equals(this$saleReturnReference, other$saleReturnReference))
            return false;
        final Object this$saleReturnCustomer = this.getSaleReturnCustomer();
        final Object other$saleReturnCustomer = other.getSaleReturnCustomer();
        if (!Objects.equals(this$saleReturnCustomer, other$saleReturnCustomer))
            return false;
        final Object this$saleReturnBranch = this.getSaleReturnBranch();
        final Object other$saleReturnBranch = other.getSaleReturnBranch();
        if (!Objects.equals(this$saleReturnBranch, other$saleReturnBranch))
            return false;
        final Object this$saleReturnSaleRef = this.getSaleReturnSaleRef();
        final Object other$saleReturnSaleRef = other.getSaleReturnSaleRef();
        if (!Objects.equals(this$saleReturnSaleRef, other$saleReturnSaleRef))
            return false;
        final Object this$saleReturnStatus = this.getSaleReturnStatus();
        final Object other$saleReturnStatus = other.getSaleReturnStatus();
        if (!Objects.equals(this$saleReturnStatus, other$saleReturnStatus))
            return false;
        final Object this$saleReturnGrandTotal = this.getSaleReturnGrandTotal();
        final Object other$saleReturnGrandTotal = other.getSaleReturnGrandTotal();
        if (!Objects.equals(this$saleReturnGrandTotal, other$saleReturnGrandTotal))
            return false;
        final Object this$saleReturnAmountPaid = this.getSaleReturnAmountPaid();
        final Object other$saleReturnAmountPaid = other.getSaleReturnAmountPaid();
        if (!Objects.equals(this$saleReturnAmountPaid, other$saleReturnAmountPaid))
            return false;
        final Object this$saleReturnAmountDue = this.getSaleReturnAmountDue();
        final Object other$saleReturnAmountDue = other.getSaleReturnAmountDue();
        if (!Objects.equals(this$saleReturnAmountDue, other$saleReturnAmountDue))
            return false;
        final Object this$saleReturnPaymentStatus = this.getSaleReturnPaymentStatus();
        final Object other$saleReturnPaymentStatus = other.getSaleReturnPaymentStatus();
        return Objects.equals(this$saleReturnPaymentStatus, other$saleReturnPaymentStatus);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SaleReturn;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $saleReturnDate = this.getSaleReturnDate();
        result = result * PRIME + ($saleReturnDate == null ? 43 : $saleReturnDate.hashCode());
        final Object $saleReturnReference = this.getSaleReturnReference();
        result = result * PRIME + ($saleReturnReference == null ? 43 : $saleReturnReference.hashCode());
        final Object $saleReturnCustomer = this.getSaleReturnCustomer();
        result = result * PRIME + ($saleReturnCustomer == null ? 43 : $saleReturnCustomer.hashCode());
        final Object $saleReturnBranch = this.getSaleReturnBranch();
        result = result * PRIME + ($saleReturnBranch == null ? 43 : $saleReturnBranch.hashCode());
        final Object $saleReturnSaleRef = this.getSaleReturnSaleRef();
        result = result * PRIME + ($saleReturnSaleRef == null ? 43 : $saleReturnSaleRef.hashCode());
        final Object $saleReturnStatus = this.getSaleReturnStatus();
        result = result * PRIME + ($saleReturnStatus == null ? 43 : $saleReturnStatus.hashCode());
        final Object $saleReturnGrandTotal = this.getSaleReturnGrandTotal();
        result = result * PRIME + ($saleReturnGrandTotal == null ? 43 : $saleReturnGrandTotal.hashCode());
        final Object $saleReturnAmountPaid = this.getSaleReturnAmountPaid();
        result = result * PRIME + ($saleReturnAmountPaid == null ? 43 : $saleReturnAmountPaid.hashCode());
        final Object $saleReturnAmountDue = this.getSaleReturnAmountDue();
        result = result * PRIME + ($saleReturnAmountDue == null ? 43 : $saleReturnAmountDue.hashCode());
        final Object $saleReturnPaymentStatus = this.getSaleReturnPaymentStatus();
        result = result * PRIME + ($saleReturnPaymentStatus == null ? 43 : $saleReturnPaymentStatus.hashCode());
        return result;
    }

    public String toString() {
        return "SaleReturn(saleReturnDate=" + this.getSaleReturnDate() + ", saleReturnReference=" + this.getSaleReturnReference() + ", saleReturnCustomer=" + this.getSaleReturnCustomer() + ", saleReturnBranch=" + this.getSaleReturnBranch() + ", saleReturnAddedBy=" + this.getSaleReturnSaleRef() + ", saleReturnStatus=" + this.getSaleReturnStatus() + ", saleReturnGrandTotal=" + this.getSaleReturnGrandTotal() + ", saleReturnAmountPaid=" + this.getSaleReturnAmountPaid() + ", saleReturnAmountDue=" + this.getSaleReturnAmountDue() + ", saleReturnPaymentStatus=" + this.getSaleReturnPaymentStatus() + ")";
    }
}
