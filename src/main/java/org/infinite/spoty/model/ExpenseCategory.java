package org.infinite.spoty.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.util.Objects;

public class ExpenseCategory implements Serializable {
        private final StringProperty categoryName = new SimpleStringProperty("");
    private final StringProperty categoryDescription = new SimpleStringProperty("");

    public String getCategoryName() {
        return this.categoryName.get();
    }

    public String getCategoryDescription() {
        return this.categoryDescription.get();
    }

    public void setCategoryName(String categoryName) {
        this.categoryName.set(categoryName);
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription.set(categoryDescription);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final ExpenseCategory other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$categoryName = this.getCategoryName();
        final Object other$categoryName = other.getCategoryName();
        if (!Objects.equals(this$categoryName, other$categoryName))
            return false;
        final Object this$categoryDescription = this.getCategoryDescription();
        final Object other$categoryDescription = other.getCategoryDescription();
        return Objects.equals(this$categoryDescription, other$categoryDescription);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ExpenseCategory;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $categoryName = this.getCategoryName();
        result = result * PRIME + ($categoryName == null ? 43 : $categoryName.hashCode());
        final Object $categoryDescription = this.getCategoryDescription();
        result = result * PRIME + ($categoryDescription == null ? 43 : $categoryDescription.hashCode());
        return result;
    }

    public String toString() {
        return "ExpenseCategory(categoryName=" + this.getCategoryName() + ", categoryDescription=" + this.getCategoryDescription() + ")";
    }
}
