package org.infinite.spoty.model;

import javafx.beans.property.*;

import java.io.Serializable;
import java.util.Objects;

public class Product implements Serializable {
    private final StringProperty productName = new SimpleStringProperty("");
    private final IntegerProperty productCode = new SimpleIntegerProperty(0);
    private final StringProperty productCategory = new SimpleStringProperty("");
    private final StringProperty productBrand = new SimpleStringProperty("");
    private  final DoubleProperty productPrice = new SimpleDoubleProperty(0);
    private final StringProperty productUnit = new SimpleStringProperty("");
    private final DoubleProperty productQuantity = new SimpleDoubleProperty(0);

    public String getProductName() {
        return this.productName.get();
    }

    public Integer getProductCode() {
        return this.productCode.get();
    }

    public String getProductCategory() {
        return this.productCategory.get();
    }

    public String getProductBrand() {
        return this.productBrand.get();
    }

    public Double getProductPrice() {
        return this.productPrice.get();
    }

    public String getProductUnit() {
        return this.productUnit.get();
    }

    public Double getProductQuantity() {
        return this.productQuantity.get();
    }

    public void setProductName(String name) {
        this.productName.set(name);
    }

    public void setProductCode(int code) {
        this.productCode.set(code);
    }

    public void setProductCategory(String category) {
        this.productCategory.set(category);
    }

    public void setProductBrand(String brand) {
        this.productBrand.set(brand);
    }

    public void setProductPrice(double price) {
        this.productPrice.set(price);
    }

    public void setProductUnit(String unit) {
        this.productUnit.set(unit);
    }

    public void setProductQuantity(double quantity) {
        this.productQuantity.set(quantity);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final Product other)) return false;
        if (!other.canEqual((Object) this)) return false;
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
        final Object this$productPrice = this.getProductPrice();
        final Object other$productPrice = other.getProductPrice();
        if (!Objects.equals(this$productPrice, other$productPrice))
            return false;
        final Object this$productUnit = this.getProductUnit();
        final Object other$productUnit = other.getProductUnit();
        if (!Objects.equals(this$productUnit, other$productUnit))
            return false;
        final Object this$productQuantity = this.getProductQuantity();
        final Object other$productQuantity = other.getProductQuantity();
        return Objects.equals(this$productQuantity, other$productQuantity);
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
        final Object $productPrice = this.getProductPrice();
        result = result * PRIME + ($productPrice == null ? 43 : $productPrice.hashCode());
        final Object $productUnit = this.getProductUnit();
        result = result * PRIME + ($productUnit == null ? 43 : $productUnit.hashCode());
        final Object $productQuantity = this.getProductQuantity();
        result = result * PRIME + ($productQuantity == null ? 43 : $productQuantity.hashCode());
        return result;
    }

    public String toString() {
        return "Products(productName=" + this.getProductName() + ", productCode=" + this.getProductCode() + ", productCategory=" + this.getProductCategory() + ", productBrand=" + this.getProductBrand() + ", productPrice=" + this.getProductPrice() + ", productUnit=" + this.getProductUnit() + ", productQuantity=" + this.getProductQuantity() + ")";
    }
}
