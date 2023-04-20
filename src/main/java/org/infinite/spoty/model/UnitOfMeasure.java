package org.infinite.spoty.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.util.Objects;

public class UnitOfMeasure implements Serializable {
    private final StringProperty uomName = new SimpleStringProperty("");
    private final StringProperty uomShortName = new SimpleStringProperty("");
    private final StringProperty uomBaseUnit = new SimpleStringProperty("");
    private final StringProperty uomOperator = new SimpleStringProperty(""); // Arithmetic Calculations Operator against the BaseUnit
    private final DoubleProperty uomOperationValue = new SimpleDoubleProperty(0);

    public String getUomName() {
        return this.uomName.get();
    }

    public String getUomShortName() {
        return this.uomShortName.get();
    }

    public String getUomBaseUnit() {
        return this.uomBaseUnit.get();
    }

    public String getUomOperator() {
        return this.uomOperator.get();
    }

    public Double getUomOperationValue() {
        return this.uomOperationValue.get();
    }

    public void setUomName(String uomName) {
        this.uomName.set(uomName);
    }

    public void setUomShortName(String uomShortName) {
        this.uomShortName.set(uomShortName);
    }

    public void setUomBaseUnit(String uomBaseUnit) {
        this.uomBaseUnit.set(uomBaseUnit);
    }

    public void setUomOperator(String uomOperator) {
        this.uomOperator.set(uomOperator);
    }

    public void setUomOperationValue(Double uomOperationValue) {
        this.uomOperationValue.set(uomOperationValue);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final UnitOfMeasure other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$uomName = this.getUomName();
        final Object other$uomName = other.getUomName();
        if (!Objects.equals(this$uomName, other$uomName)) return false;
        final Object this$uomShortName = this.getUomShortName();
        final Object other$uomShortName = other.getUomShortName();
        if (!Objects.equals(this$uomShortName, other$uomShortName))
            return false;
        final Object this$uomBaseUnit = this.getUomBaseUnit();
        final Object other$uomBaseUnit = other.getUomBaseUnit();
        if (!Objects.equals(this$uomBaseUnit, other$uomBaseUnit))
            return false;
        final Object this$uomOperator = this.getUomOperator();
        final Object other$uomOperator = other.getUomOperator();
        if (!Objects.equals(this$uomOperator, other$uomOperator))
            return false;
        final Object this$uomOperationValue = this.getUomOperationValue();
        final Object other$uomOperationValue = other.getUomOperationValue();
        return Objects.equals(this$uomOperationValue, other$uomOperationValue);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof UnitOfMeasure;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $uomName = this.getUomName();
        result = result * PRIME + ($uomName == null ? 43 : $uomName.hashCode());
        final Object $uomShortName = this.getUomShortName();
        result = result * PRIME + ($uomShortName == null ? 43 : $uomShortName.hashCode());
        final Object $uomBaseUnit = this.getUomBaseUnit();
        result = result * PRIME + ($uomBaseUnit == null ? 43 : $uomBaseUnit.hashCode());
        final Object $uomOperator = this.getUomOperator();
        result = result * PRIME + ($uomOperator == null ? 43 : $uomOperator.hashCode());
        final Object $uomOperationValue = this.getUomOperationValue();
        result = result * PRIME + ($uomOperationValue == null ? 43 : $uomOperationValue.hashCode());
        return result;
    }

    public String toString() {
        return "UnitOfMeasure(uomName=" + this.getUomName() + ", uomShortName=" + this.getUomShortName() + ", uomBaseUnit=" + this.getUomBaseUnit() + ", uomOperator=" + this.getUomOperator() + ", uomOperationValue=" + this.getUomOperationValue() + ")";
    }
}
