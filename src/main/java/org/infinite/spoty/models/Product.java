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

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.util.Objects;

@Deprecated
public class Product implements Serializable {
    private final StringProperty productName = new SimpleStringProperty("");
    private final IntegerProperty productCode = new SimpleIntegerProperty(0);
    private final StringProperty productCategory = new SimpleStringProperty("");
    private final StringProperty productBrand = new SimpleStringProperty("");
    private final StringProperty productBarcodeType = new SimpleStringProperty("");

    public String getProductName() {
        return this.productName.get();
    }

    public void setProductName(String name) {
        this.productName.set(name);
    }

    public Integer getProductCode() {
        return this.productCode.get();
    }

    public void setProductCode(int code) {
        this.productCode.set(code);
    }

    public String getProductCategory() {
        return this.productCategory.get();
    }

    public void setProductCategory(String category) {
        this.productCategory.set(category);
    }

    public String getProductBrand() {
        return this.productBrand.get();
    }

    public void setProductBrand(String brand) {
        this.productBrand.set(brand);
    }

    public String getProductBarcodeType() {
        return this.productBarcodeType.get();
    }

    public void setProductBarcodeType(String barcodeType) {
        this.productBarcodeType.set(barcodeType);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final Product other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$productName = this.getProductName();
        final Object other$productName = other.getProductName();
        if (!Objects.equals(this$productName, other$productName))
            return false;
        final Object this$productCode = this.getProductCode();
        final Object other$productCode = other.getProductCode();
        if (!Objects.equals(this$productCode, other$productCode))
            return false;
        final Object this$productCategory = this.getProductCategory();
        final Object other$productCategory = other.getProductCategory();
        if (!Objects.equals(this$productCategory, other$productCategory))
            return false;
        final Object this$productBrand = this.getProductBrand();
        final Object other$productBrand = other.getProductBrand();
        if (!Objects.equals(this$productBrand, other$productBrand))
            return false;
        final Object this$productBarcodeType = this.getProductBarcodeType();
        final Object other$productBarcodeType = other.getProductBarcodeType();
        return (!Objects.equals(this$productBarcodeType, other$productBarcodeType));
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Product;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $productName = this.getProductName();
        result = result * PRIME + ($productName == null ? 43 : $productName.hashCode());
        final Object $productCode = this.getProductCode();
        result = result * PRIME + ($productCode == null ? 43 : $productCode.hashCode());
        final Object $productCategory = this.getProductCategory();
        result = result * PRIME + ($productCategory == null ? 43 : $productCategory.hashCode());
        final Object $productBrand = this.getProductBrand();
        result = result * PRIME + ($productBrand == null ? 43 : $productBrand.hashCode());
        final Object $productBarcodeType = this.getProductBarcodeType();
        result = result * PRIME + ($productBarcodeType == null ? 43 : $productBarcodeType.hashCode());
        return result;
    }

    public String toString() {
        return "Products(productName=" + this.getProductName() + ", productCode=" + this.getProductCode() + ", productCategory=" + this.getProductCategory() + ", productBrand=" + this.getProductBrand() + ", productBarcodeType=" + this.getProductBarcodeType() + ")";
    }
}
