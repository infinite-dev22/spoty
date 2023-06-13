/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

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
