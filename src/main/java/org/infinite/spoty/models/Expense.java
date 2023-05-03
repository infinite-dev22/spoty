package org.infinite.spoty.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Expense implements Serializable {
    private final StringProperty expenseReference = new SimpleStringProperty("");
    private final StringProperty expenseName = new SimpleStringProperty("");
    private final DoubleProperty expenseAmount = new SimpleDoubleProperty(0);
    private final StringProperty expenseCategory = new SimpleStringProperty("");
    private final StringProperty expenseBranch = new SimpleStringProperty("");
    private final StringProperty expenseDetails = new SimpleStringProperty("");
    private Date expenseDate = new Date();

    public String getExpenseDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(this.expenseDate);
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getExpenseReference() {
        return this.expenseReference.get();
    }

    public void setExpenseReference(String expenseReference) {
        this.expenseReference.set(expenseReference);
    }

    public String getExpenseName() {
        return this.expenseName.get();
    }

    public void setExpenseName(String expenseName) {
        this.expenseName.set(expenseName);
    }

    public double getExpenseAmount() {
        return this.expenseAmount.get();
    }

    public void setExpenseAmount(double expenseAmount) {
        this.expenseAmount.set(expenseAmount);
    }

    public String getExpenseCategory() {
        return this.expenseCategory.get();
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory.set(expenseCategory);
    }

    public String getExpenseBranch() {
        return this.expenseBranch.get();
    }

    public void setExpenseBranch(String expenseBranch) {
        this.expenseBranch.set(expenseBranch);
    }

    public String getExpenseDetails() {
        return this.expenseDetails.get();
    }

    public void setExpenseDetails(String expenseDetails) {
        this.expenseDetails.set(expenseDetails);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final Expense other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$expenseDate = this.getExpenseDate();
        final Object other$expenseDate = other.getExpenseDate();
        if (!Objects.equals(this$expenseDate, other$expenseDate))
            return false;
        final Object this$expenseReference = this.getExpenseReference();
        final Object other$expenseReference = other.getExpenseReference();
        if (!Objects.equals(this$expenseReference, other$expenseReference))
            return false;
        final Object this$expenseName = this.getExpenseName();
        final Object other$expenseName = other.getExpenseName();
        if (!Objects.equals(this$expenseName, other$expenseName))
            return false;
        final Object this$expenseAmount = this.getExpenseAmount();
        final Object other$expenseAmount = other.getExpenseAmount();
        if (!Objects.equals(this$expenseAmount, other$expenseAmount))
            return false;
        final Object this$expenseCategory = this.getExpenseCategory();
        final Object other$expenseCategory = other.getExpenseCategory();
        if (!Objects.equals(this$expenseCategory, other$expenseCategory))
            return false;
        final Object this$expenseDetails = this.getExpenseDetails();
        final Object other$expenseDetails = other.getExpenseDetails();
        if (!Objects.equals(this$expenseDetails, other$expenseDetails))
            return false;
        final Object this$expenseBranch = this.getExpenseBranch();
        final Object other$expenseBranch = other.getExpenseBranch();
        return Objects.equals(this$expenseBranch, other$expenseBranch);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Expense;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $expenseDate = this.getExpenseDate();
        result = result * PRIME + ($expenseDate == null ? 43 : $expenseDate.hashCode());
        final Object $expenseReference = this.getExpenseReference();
        result = result * PRIME + ($expenseReference == null ? 43 : $expenseReference.hashCode());
        final Object $expenseName = this.getExpenseName();
        result = result * PRIME + ($expenseName == null ? 43 : $expenseName.hashCode());
        final Object $expenseAmount = this.getExpenseAmount();
        result = result * PRIME + $expenseAmount.hashCode();
        final Object $expenseCategory = this.getExpenseCategory();
        result = result * PRIME + ($expenseCategory == null ? 43 : $expenseCategory.hashCode());
        final Object $expenseDetails = this.getExpenseDetails();
        result = result * PRIME + ($expenseDetails == null ? 43 : $expenseDetails.hashCode());
        final Object $expenseBranch = this.getExpenseBranch();
        result = result * PRIME + ($expenseBranch == null ? 43 : $expenseBranch.hashCode());
        return result;
    }

    public String toString() {
        return "Expense(expenseDate=" + this.getExpenseDate() + ", expenseName=" + this.getExpenseName() + ", expenseAmount=" + this.getExpenseAmount() + ", expenseCategory=" + this.getExpenseCategory() + ", expenseBranch=" + this.getExpenseBranch() + ")";
    }
}
