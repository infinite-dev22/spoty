package org.infinite.spoty.model;

import javafx.beans.property.*;

import java.io.Serializable;
import java.util.Objects;

public class Supplier  implements Serializable {
    private final IntegerProperty supplierCode = new SimpleIntegerProperty(0);
    private final StringProperty supplierName = new SimpleStringProperty("");
    private final StringProperty supplierPhoneNumber = new SimpleStringProperty("");
    private final StringProperty supplierEmail = new SimpleStringProperty("");
    private final StringProperty supplierTaxNumber = new SimpleStringProperty("");
    private final DoubleProperty supplierTotalPurchaseDue = new SimpleDoubleProperty(0.00);
    private final DoubleProperty supplierTotalPurchaseReturnDue = new SimpleDoubleProperty(0.00);

    public int getSupplierCode() {
        return this.supplierCode.get();
    }

    public String getSupplierName() {
        return this.supplierName.get();
    }

    public String getSupplierPhoneNumber() {
        return this.supplierPhoneNumber.get();
    }

    public String getSupplierEmail() {
        return this.supplierEmail.get();
    }

    public String getSupplierTaxNumber() {
        return this.supplierTaxNumber.get();
    }

    public double getSupplierTotalPurchaseDue() {
        return this.supplierTotalPurchaseDue.get();
    }

    public double getSupplierTotalPurchaseReturnDue() {
        return this.supplierTotalPurchaseReturnDue.get();
    }

    public void setSupplierCode(int supplierCode) {
        this.supplierCode.set(supplierCode);
    }

    public void setSupplierName(String supplierName) {
        this.supplierName.set(supplierName);
    }

    public void setSupplierPhoneNumber(String supplierPhoneNumber) {
        this.supplierPhoneNumber.set(supplierPhoneNumber);
    }

    public void setSupplierEmail(String supplierEmail) {
        this.supplierEmail.set(supplierEmail);
    }

    public void setSupplierTaxNumber(String supplierTaxNumber) {
        this.supplierTaxNumber.set(supplierTaxNumber);
    }

    public void setSupplierTotalPurchaseDue(double supplierTotalPurchaseDue) {
        this.supplierTotalPurchaseDue.set(supplierTotalPurchaseDue);
    }

    public void setSupplierTotalPurchaseReturnDue(double supplierTotalPurchaseReturnDue) {
        this.supplierTotalPurchaseReturnDue.set(supplierTotalPurchaseReturnDue);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final Supplier other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$supplierCode = this.getSupplierCode();
        final Object other$supplierCode = other.getSupplierCode();
        if (!this$supplierCode.equals(other$supplierCode))
            return false;
        final Object this$supplierName = this.getSupplierName();
        final Object other$supplierName = other.getSupplierName();
        if (!Objects.equals(this$supplierName, other$supplierName))
            return false;
        final Object this$supplierPhoneNumber = this.getSupplierPhoneNumber();
        final Object other$supplierPhoneNumber = other.getSupplierPhoneNumber();
        if (!Objects.equals(this$supplierPhoneNumber, other$supplierPhoneNumber))
            return false;
        final Object this$supplierEmail = this.getSupplierEmail();
        final Object other$supplierEmail = other.getSupplierEmail();
        if (!Objects.equals(this$supplierEmail, other$supplierEmail))
            return false;
        final Object this$supplierTaxNumber = this.getSupplierTaxNumber();
        final Object other$supplierTaxNumber = other.getSupplierTaxNumber();
        if (!Objects.equals(this$supplierTaxNumber, other$supplierTaxNumber))
            return false;
        final Object this$supplierTotalPurchaseDue = this.getSupplierTotalPurchaseDue();
        final Object other$supplierTotalPurchaseDue = other.getSupplierTotalPurchaseDue();
        if (!this$supplierTotalPurchaseDue.equals(other$supplierTotalPurchaseDue))
            return false;
        final Object this$supplierTotalPurchaseReturnDue = this.getSupplierTotalPurchaseReturnDue();
        final Object other$supplierTotalPurchaseReturnDue = other.getSupplierTotalPurchaseReturnDue();
        return this$supplierTotalPurchaseReturnDue.equals(other$supplierTotalPurchaseReturnDue);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Supplier;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $supplierCode = this.getSupplierCode();
        result = result * PRIME + $supplierCode.hashCode();
        final Object $supplierName = this.getSupplierName();
        result = result * PRIME + ($supplierName == null ? 43 : $supplierName.hashCode());
        final Object $supplierPhoneNumber = this.getSupplierPhoneNumber();
        result = result * PRIME + ($supplierPhoneNumber == null ? 43 : $supplierPhoneNumber.hashCode());
        final Object $supplierEmail = this.getSupplierEmail();
        result = result * PRIME + ($supplierEmail == null ? 43 : $supplierEmail.hashCode());
        final Object $supplierTaxNumber = this.getSupplierTaxNumber();
        result = result * PRIME + ($supplierTaxNumber == null ? 43 : $supplierTaxNumber.hashCode());
        final Object $supplierTotalPurchaseDue = this.getSupplierTotalPurchaseDue();
        result = result * PRIME + $supplierTotalPurchaseDue.hashCode();
        final Object $supplierTotalPurchaseReturnDue = this.getSupplierTotalPurchaseReturnDue();
        result = result * PRIME + $supplierTotalPurchaseReturnDue.hashCode();
        return result;
    }

    public String toString() {
        return "Supplier(supplierCode=" + this.getSupplierCode() + ", supplierName=" + this.getSupplierName() + ", supplierPhoneNumber=" + this.getSupplierPhoneNumber() + ", supplierEmail=" + this.getSupplierEmail() + ", supplierTaxNumber=" + this.getSupplierTaxNumber() + ", supplierTotalPurchaseDue=" + this.getSupplierTotalPurchaseDue() + ", supplierTotalPurchaseReturnDue=" + this.getSupplierTotalPurchaseReturnDue() + ")";
    }
}
