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
