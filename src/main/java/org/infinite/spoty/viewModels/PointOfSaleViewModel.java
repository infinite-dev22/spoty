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

package org.infinite.spoty.viewModels;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.models.Product;

public class PointOfSaleViewModel {
    public static final LongProperty itemQuantity = new SimpleLongProperty(1);
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<Product> customer = new SimpleObjectProperty<>();
    private static final ObjectProperty<Product> branch = new SimpleObjectProperty<>();
    private static final DoubleProperty subTotal = new SimpleDoubleProperty();
    private static final DoubleProperty total = new SimpleDoubleProperty();
    private static final DoubleProperty price = new SimpleDoubleProperty();
    private static final DoubleProperty tax = new SimpleDoubleProperty();
    private static final DoubleProperty discount = new SimpleDoubleProperty();
    private static final ObservableList<Product> posCartList = FXCollections.observableArrayList();

    public static Long getItemQuantity() {
        return itemQuantity.get();
    }

    public static void setItemQuantity(long itemQuantity) {
        PointOfSaleViewModel.itemQuantity.set(itemQuantity);
    }

    public static DoubleProperty discountProperty() {
        return discount;
    }

    public static DoubleProperty priceProperty() {
        return price;
    }

    public static DoubleProperty subTotalProperty() {
        return subTotal;
    }

    public static DoubleProperty taxProperty() {
        return tax;
    }

    public static DoubleProperty totalProperty() {
        return total;
    }

    public static LongProperty itemQuantityProperty() {
        return itemQuantity;
    }

    public static ObjectProperty<Product> branchProperty() {
        return branch;
    }

    public static ObjectProperty<Product> customerProperty() {
        return customer;
    }

    public static ObjectProperty<Product> productProperty() {
        return product;
    }

    public static ObservableList<Product> getPosCartList() {
        return posCartList;
    }

    public static Product getCustomer() {
        return customer.get();
    }

    public static void setCustomer(Product customer) {
        PointOfSaleViewModel.customer.set(customer);
    }

    public static Product getProduct() {
        return product.get();
    }

    public static void setProduct(Product product) {
        PointOfSaleViewModel.product.set(product);
    }

    public static Product getBranch() {
        return branch.get();
    }

    public static void setBranch(Product branch) {
        PointOfSaleViewModel.branch.set(branch);
    }

    public static double getDiscount() {
        return discount.get();
    }

    public static void setDiscount(double discount) {
        PointOfSaleViewModel.discount.set(discount);
    }

    public static double getPrice() {
        return price.get();
    }

    public static void setPrice(double price) {
        PointOfSaleViewModel.price.set(price);
    }

    public static double getSubTotal() {
        return subTotal.get();
    }

    public static void setSubTotal(double subTotal) {
        PointOfSaleViewModel.subTotal.set(subTotal);
    }

    public static double getTax() {
        return tax.get();
    }

    public static void setTax(double tax) {
        PointOfSaleViewModel.tax.set(tax);
    }

    public static double getTotal() {
        return total.get();
    }

    public static void setTotal(double total) {
        PointOfSaleViewModel.total.set(total);
    }
}
