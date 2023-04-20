package org.infinite.spoty.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.util.Objects;

public class Brand implements Serializable {
    private final StringProperty brandName = new SimpleStringProperty("");
    private final StringProperty brandDescription = new SimpleStringProperty("");

    public String getBrandName() {
        return this.brandName.get();
    }

    public String getBrandDescription() {
        return this.brandDescription.get();
    }

    public void setBrandName(String brandName) {
        this.brandName.set(brandName);
    }

    public void setBrandDescription(String brandDescription) {
        this.brandDescription.set(brandDescription);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final Brand other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$brandName = this.getBrandName();
        final Object other$brandName = other.getBrandName();
        if (!Objects.equals(this$brandName, other$brandName)) return false;
        final Object this$brandDescription = this.getBrandDescription();
        final Object other$brandDescription = other.getBrandDescription();
        return Objects.equals(this$brandDescription, other$brandDescription);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Brand;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $brandName = this.getBrandName();
        result = result * PRIME + ($brandName == null ? 43 : $brandName.hashCode());
        final Object $brandDescription = this.getBrandDescription();
        result = result * PRIME + ($brandDescription == null ? 43 : $brandDescription.hashCode());
        return result;
    }

    public String toString() {
        return "Brand(brandName=" + this.getBrandName() + ", brandDescription=" + this.getBrandDescription() + ")";
    }
}
