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

package org.infinite.spoty.dataShare;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.models.AdjustmentProduct;
import org.infinite.spoty.models.PurchaseProduct;
import org.infinite.spoty.models.QuotationProduct;
import org.infinite.spoty.models.SaleProduct;

public class DataShare {
    private static final ObservableList<AdjustmentProduct> adjustmentProductsList = FXCollections.observableArrayList();
    private static final ObservableList<QuotationProduct> quotationProductsList = FXCollections.observableArrayList();
    private static final ObservableList<PurchaseProduct> purchaseProductsList = FXCollections.observableArrayList();
    private static final ObservableList<SaleProduct> saleProductsList = FXCollections.observableArrayList();

    public static void createAdjustmentProduct(String product, double quantity, String adjustmentType) {
        AdjustmentProduct adjustmentProduct = new AdjustmentProduct(product, quantity, adjustmentType);
        adjustmentProductsList.add(adjustmentProduct);
    }

    public static ObservableList<AdjustmentProduct> getAdjustmentProducts() {
        return adjustmentProductsList;
    }

    public static void createQuotationProduct(double quantity, String product, double tax, double discount) {
        QuotationProduct quotationProduct = new QuotationProduct(quantity, product, tax, discount);
        quotationProductsList.add(quotationProduct);
    }

    public static ObservableList<QuotationProduct> getQuotationProducts() {
        return quotationProductsList;
    }

    public static void createPurchaseProduct(double quantity, String product, double tax, double discount) {
        PurchaseProduct purchaseProduct = new PurchaseProduct(quantity, product, tax, discount);
        purchaseProductsList.add(purchaseProduct);
    }

    public static ObservableList<PurchaseProduct> getPurchaseProducts() {
        return purchaseProductsList;
    }

    public static void createSaleProduct(double quantity, String product, double tax, double discount) {
        SaleProduct saleProduct = new SaleProduct(quantity, product, tax, discount);
        saleProductsList.add(saleProduct);
    }

    public static ObservableList<SaleProduct> getSaleProducts() {
        return saleProductsList;
    }
}
