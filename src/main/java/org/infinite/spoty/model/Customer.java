package org.infinite.spoty.model;

import javafx.beans.property.*;

import java.io.Serializable;
import java.util.Objects;

public class Customer implements Serializable {
    private final IntegerProperty customerCode = new SimpleIntegerProperty(0);
    private final StringProperty customerName = new SimpleStringProperty("");
    private final StringProperty customerPhoneNumber = new SimpleStringProperty("");
    private final StringProperty customerEmail = new SimpleStringProperty("");
    private final StringProperty customerTaxNumber = new SimpleStringProperty("");
    private final DoubleProperty customerTotalSaleDue = new SimpleDoubleProperty(0.00);
    private final DoubleProperty customerTotalSellReturnDue = new SimpleDoubleProperty(0.00);

    public int getCustomerCode() {
        return this.customerCode.get();
    }

    public String getCustomerName() {
        return this.customerName.get();
    }

    public String getCustomerPhoneNumber() {
        return this.customerPhoneNumber.get();
    }

    public String getCustomerEmail() {
        return this.customerEmail.get();
    }

    public String getCustomerTaxNumber() {
        return this.customerTaxNumber.get();
    }

    public double getCustomerTotalSaleDue() {
        return this.customerTotalSaleDue.get();
    }

    public double getCustomerTotalSellReturnDue() {
        return this.customerTotalSellReturnDue.get();
    }

    public void setCustomerCode(int customerCode) {
        this.customerCode.set(customerCode);
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber.set(customerPhoneNumber);
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail.set(customerEmail);
    }

    public void setCustomerTaxNumber(String customerTaxNumber) {
        this.customerTaxNumber.set(customerTaxNumber);
    }

    public void setCustomerTotalSaleDue(double customerTotalSaleDue) {
        this.customerTotalSaleDue.set(customerTotalSaleDue);
    }

    public void setCustomerTotalSellReturnDue(double customerTotalSellReturnDue) {
        this.customerTotalSellReturnDue.set(customerTotalSellReturnDue);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final Customer other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$customerCode = this.getCustomerCode();
        final Object other$customerCode = other.getCustomerCode();
        if (!this$customerCode.equals(other$customerCode))
            return false;
        final Object this$customerName = this.getCustomerName();
        final Object other$customerName = other.getCustomerName();
        if (!Objects.equals(this$customerName, other$customerName))
            return false;
        final Object this$customerPhoneNumber = this.getCustomerPhoneNumber();
        final Object other$customerPhoneNumber = other.getCustomerPhoneNumber();
        if (!Objects.equals(this$customerPhoneNumber, other$customerPhoneNumber))
            return false;
        final Object this$customerEmail = this.getCustomerEmail();
        final Object other$customerEmail = other.getCustomerEmail();
        if (!Objects.equals(this$customerEmail, other$customerEmail))
            return false;
        final Object this$customerTaxNumber = this.getCustomerTaxNumber();
        final Object other$customerTaxNumber = other.getCustomerTaxNumber();
        if (!Objects.equals(this$customerTaxNumber, other$customerTaxNumber))
            return false;
        final Object this$customerTotalSaleDue = this.getCustomerTotalSaleDue();
        final Object other$customerTotalSaleDue = other.getCustomerTotalSaleDue();
        if (!this$customerTotalSaleDue.equals(other$customerTotalSaleDue))
            return false;
        final Object this$customerTotalSellReturnDue = this.getCustomerTotalSellReturnDue();
        final Object other$customerTotalSellReturnDue = other.getCustomerTotalSellReturnDue();
        return this$customerTotalSellReturnDue.equals(other$customerTotalSellReturnDue);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Customer;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $customerCode = this.getCustomerCode();
        result = result * PRIME + $customerCode.hashCode();
        final Object $customerName = this.getCustomerName();
        result = result * PRIME + ($customerName == null ? 43 : $customerName.hashCode());
        final Object $customerPhoneNumber = this.getCustomerPhoneNumber();
        result = result * PRIME + ($customerPhoneNumber == null ? 43 : $customerPhoneNumber.hashCode());
        final Object $customerEmail = this.getCustomerEmail();
        result = result * PRIME + ($customerEmail == null ? 43 : $customerEmail.hashCode());
        final Object $customerTaxNumber = this.getCustomerTaxNumber();
        result = result * PRIME + ($customerTaxNumber == null ? 43 : $customerTaxNumber.hashCode());
        final Object $customerTotalSaleDue = this.getCustomerTotalSaleDue();
        result = result * PRIME + $customerTotalSaleDue.hashCode();
        final Object $customerTotalSellReturnDue = this.getCustomerTotalSellReturnDue();
        result = result * PRIME + $customerTotalSellReturnDue.hashCode();
        return result;
    }

    public String toString() {
        return "Customer(customerCode=" + this.getCustomerCode() + ", customerName=" + this.getCustomerName() + ", customerPhoneNumber=" + this.getCustomerPhoneNumber() + ", customerEmail=" + this.getCustomerEmail() + ", customerTaxNumber=" + this.getCustomerTaxNumber() + ", customerTotalSaleDue=" + this.getCustomerTotalSaleDue() + ", customerTotalSellReturnDue=" + this.getCustomerTotalSellReturnDue() + ")";
    }
}
