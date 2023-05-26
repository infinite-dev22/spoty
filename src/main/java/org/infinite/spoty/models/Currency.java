package org.infinite.spoty.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.util.Objects;

@Deprecated
public class Currency implements Serializable {
    private final StringProperty currencyName = new SimpleStringProperty("");
    private final StringProperty currencyCode = new SimpleStringProperty("");
    private final StringProperty currencySymbol = new SimpleStringProperty("");

    public String getCurrencyName() {
        return this.currencyName.get();
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName.set(currencyName);
    }

    public String getCurrencyCode() {
        return this.currencyCode.get();
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode.set(currencyCode);
    }

    public String getCurrencySymbol() {
        return this.currencySymbol.get();
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol.set(currencySymbol);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final Currency other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$currencyName = this.getCurrencyName();
        final Object other$currencyName = other.getCurrencyName();
        if (!Objects.equals(this$currencyName, other$currencyName))
            return false;
        final Object this$currencyCode = this.getCurrencyCode();
        final Object other$currencyCode = other.getCurrencyCode();
        if (!Objects.equals(this$currencyCode, other$currencyCode))
            return false;
        final Object this$currencySymbol = this.getCurrencySymbol();
        final Object other$currencySymbol = other.getCurrencySymbol();
        return Objects.equals(this$currencySymbol, other$currencySymbol);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Currency;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $currencyName = this.getCurrencyName();
        result = result * PRIME + ($currencyName == null ? 43 : $currencyName.hashCode());
        final Object $currencyCode = this.getCurrencyCode();
        result = result * PRIME + ($currencyCode == null ? 43 : $currencyCode.hashCode());
        final Object $currencySymbol = this.getCurrencySymbol();
        result = result * PRIME + ($currencySymbol == null ? 43 : $currencySymbol.hashCode());
        return result;
    }

    public String toString() {
        return "Currency(currencyName=" + this.getCurrencyName() + ", currencyCode=" + this.getCurrencyCode() + ", currencySymbol=" + this.getCurrencySymbol() + ")";
    }
}
