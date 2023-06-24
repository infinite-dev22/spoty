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

package org.infinite.spoty.components.navigation;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.infinite.spoty.views.dashboard.DashboardController;
import org.infinite.spoty.views.expenses.category.ExpenseCategoryController;
import org.infinite.spoty.views.expenses.expense.ExpenseController;
import org.infinite.spoty.views.inventory.adjustment.AdjustmentController;
import org.infinite.spoty.views.inventory.brand.BrandController;
import org.infinite.spoty.views.inventory.category.ProductCategoryController;
import org.infinite.spoty.views.inventory.products.ProductController;
import org.infinite.spoty.views.inventory.quotation.QuotationController;
import org.infinite.spoty.views.inventory.unit_of_measure.UnitOfMeasureController;
import org.infinite.spoty.views.people.customers.CustomerController;
import org.infinite.spoty.views.people.suppliers.SupplierController;
import org.infinite.spoty.views.people.users.UsersController;
import org.infinite.spoty.views.purchases.PurchasesController;
import org.infinite.spoty.views.requisition.RequisitionController;
import org.infinite.spoty.views.returns.purchases.PurchaseReturnController;
import org.infinite.spoty.views.returns.sales.SaleReturnsController;
import org.infinite.spoty.views.sales.SalesController;
import org.infinite.spoty.views.stock_in.StockInController;
import org.infinite.spoty.views.transfer.TransferController;

import java.io.IOException;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

public class Pages {
    static FXMLLoader dashboardLoader = fxmlLoader("fxml/dashboard/Dashboard.fxml");
    static FXMLLoader productCategoryLoader = fxmlLoader("fxml/inventory/category/ProductCategory.fxml");
    static FXMLLoader brandLoader = fxmlLoader("fxml/inventory/brand/Brand.fxml");
    static FXMLLoader unitLoader = fxmlLoader("fxml/inventory/unit_of_measure/UnitOfMeasure.fxml");
    static FXMLLoader productLoader = fxmlLoader("fxml/inventory/products/Products.fxml");
    static FXMLLoader adjustmentLoader = fxmlLoader("fxml/inventory/adjustment/Adjustment.fxml");
    static FXMLLoader quotationLoader = fxmlLoader("fxml/inventory/quotation/Quotation.fxml");
    static FXMLLoader requisitionLoader = fxmlLoader("fxml/requisition/Requisition.fxml");
    static FXMLLoader purchaseLoader = fxmlLoader("fxml/purchases/Purchases.fxml");
    static FXMLLoader transferLoader = fxmlLoader("fxml/transfer/Transfer.fxml");
    static FXMLLoader stockInLoader = fxmlLoader("fxml/stock_in/StockIn.fxml");
    static FXMLLoader saleLoader = fxmlLoader("fxml/sales/Sales.fxml");
    static FXMLLoader saleReturnLoader = fxmlLoader("fxml/returns/sales/Sales.fxml");
    static FXMLLoader purchaseReturnLoader = fxmlLoader("fxml/returns/purchases/Purchases.fxml");
    static FXMLLoader expenseCategoryLoader = fxmlLoader("fxml/expenses/category/Category.fxml");
    static FXMLLoader expenseLoader = fxmlLoader("fxml/expenses/expense/Expense.fxml");
    static FXMLLoader customerLoader = fxmlLoader("fxml/people/customers/Customers.fxml");
    static FXMLLoader supplierLoader = fxmlLoader("fxml/people/suppliers/Suppliers.fxml");
    static FXMLLoader userLoader = fxmlLoader("fxml/people/users/Users.fxml");
    private static BorderPane dashboardPane;
    private static BorderPane productCategoryPane;
    private static BorderPane brandPane;
    private static BorderPane unitPane;
    private static BorderPane productPane;
    private static BorderPane adjustmentPane;
    private static BorderPane quotationPane;
    private static BorderPane requisitionPane;
    private static BorderPane purchasePane;
    private static BorderPane transferPane;
    private static BorderPane stockInPane;
    private static BorderPane salePane;
    private static BorderPane saleReturnPane;
    private static BorderPane purchaseReturnPane;
    private static BorderPane expenseCategoryPane;
    private static BorderPane expensePane;
    private static BorderPane customerPane;
    private static BorderPane supplierPane;
    private static BorderPane userPane;

    private static void setDashboard() {
        dashboardLoader.setControllerFactory(e -> new DashboardController());
    }

    private static void setInventory(Stage stage) {
        productCategoryLoader.setControllerFactory(e -> ProductCategoryController.getInstance(stage));
        brandLoader.setControllerFactory(e -> BrandController.getInstance(stage));
        unitLoader.setControllerFactory(e -> UnitOfMeasureController.getInstance(stage));
        productLoader.setControllerFactory(e -> ProductController.getInstance(stage));
        adjustmentLoader.setControllerFactory(e -> AdjustmentController.getInstance(stage));
        quotationLoader.setControllerFactory(e -> QuotationController.getInstance(stage));
    }

    private static void setSingleItems(Stage stage) {
        requisitionLoader.setControllerFactory(e -> RequisitionController.getInstance(stage));
        purchaseLoader.setControllerFactory(e -> PurchasesController.getInstance(stage));
        transferLoader.setControllerFactory(e -> TransferController.getInstance(stage));
        stockInLoader.setControllerFactory(e -> StockInController.getInstance(stage));
        saleLoader.setControllerFactory(e -> SalesController.getInstance(stage));
    }

    private static void setReturns(Stage stage) {
        saleReturnLoader.setControllerFactory(e -> new SaleReturnsController());
        purchaseReturnLoader.setControllerFactory(e -> new PurchaseReturnController());
    }

    private static void setExpenses(Stage stage) {
        expenseCategoryLoader.setControllerFactory(e -> ExpenseCategoryController.getInstance(stage));
        expenseLoader.setControllerFactory(e -> ExpenseController.getInstance(stage));
    }

    private static void setPeople(Stage stage) {
        customerLoader.setControllerFactory(e -> CustomerController.getInstance(stage));
        supplierLoader.setControllerFactory(e -> SupplierController.getInstance(stage));
        userLoader.setControllerFactory(e -> UsersController.getInstance(stage));
    }

    public static void setPanes() throws IOException {
        dashboardPane = dashboardLoader.load();

        productCategoryPane = productCategoryLoader.load();
        brandPane = brandLoader.load();
        unitPane = unitLoader.load();
        productPane = productLoader.load();
        adjustmentPane = adjustmentLoader.load();
        quotationPane = quotationLoader.load();

        requisitionPane = requisitionLoader.load();
        purchasePane = purchaseLoader.load();
        transferPane = transferLoader.load();
        stockInPane = stockInLoader.load();
        salePane = saleLoader.load();

        saleReturnPane = saleReturnLoader.load();
        purchaseReturnPane = purchaseReturnLoader.load();

        expenseCategoryPane = expenseCategoryLoader.load();
        expensePane = expenseLoader.load();

        customerPane = customerLoader.load();
        supplierPane = supplierLoader.load();
        userPane = userLoader.load();
    }

    public static void setControllers(Stage stage) {
        setDashboard();
        setInventory(stage);
        setSingleItems(stage);
        setReturns(stage);
        setExpenses(stage);
        setPeople(stage);
    }

    public static BorderPane getDashboardPane() {
        return dashboardPane;
    }

    public static BorderPane getProductCategoryPane() {
        return productCategoryPane;
    }

    public static BorderPane getBrandPane() {
        return brandPane;
    }

    public static BorderPane getUnitPane() {
        return unitPane;
    }

    public static BorderPane getProductPane() {
        return productPane;
    }

    public static BorderPane getAdjustmentPane() {
        return adjustmentPane;
    }

    public static BorderPane getQuotationPane() {
        return quotationPane;
    }

    public static BorderPane getRequisitionPane() {
        return requisitionPane;
    }

    public static BorderPane getPurchasePane() {
        return purchasePane;
    }

    public static BorderPane getTransferPane() {
        return transferPane;
    }

    public static BorderPane getStockInPane() {
        return stockInPane;
    }

    public static BorderPane getSalePane() {
        return salePane;
    }

    public static BorderPane getSaleReturnPane() {
        return saleReturnPane;
    }

    public static BorderPane getPurchaseReturnPane() {
        return purchaseReturnPane;
    }

    public static BorderPane getExpenseCategoryPane() {
        return expenseCategoryPane;
    }

    public static BorderPane getExpensePane() {
        return expensePane;
    }

    public static BorderPane getCustomerPane() {
        return customerPane;
    }

    public static BorderPane getSupplierPane() {
        return supplierPane;
    }

    public static BorderPane getUserPane() {
        return userPane;
    }
}
