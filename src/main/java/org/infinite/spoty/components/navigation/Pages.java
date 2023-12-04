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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.infinite.spoty.forms.*;
import org.infinite.spoty.views.account.BankReconciliationController;
import org.infinite.spoty.views.account.*;
import org.infinite.spoty.views.account.report.*;
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
import org.infinite.spoty.views.people.users.UserController;
import org.infinite.spoty.views.purchases.PurchasesController;
import org.infinite.spoty.views.requisition.RequisitionController;
import org.infinite.spoty.views.returns.purchases.PurchaseReturnController;
import org.infinite.spoty.views.returns.sales.SaleReturnsController;
import org.infinite.spoty.views.sales.SaleTermsController;
import org.infinite.spoty.views.sales.SalesController;
import org.infinite.spoty.views.sales.pos.PointOfSaleController;
import org.infinite.spoty.views.settings.SettingsController;
import org.infinite.spoty.views.settings.branches.BranchController;
import org.infinite.spoty.views.settings.currency.CurrencyController;
import org.infinite.spoty.views.settings.export.ExportController;
import org.infinite.spoty.views.settings.pos.POSController;
import org.infinite.spoty.views.settings.roles.RoleController;
import org.infinite.spoty.views.settings.system.SystemController;
import org.infinite.spoty.views.stock_in.StockInController;
import org.infinite.spoty.views.transfer.TransferController;

import java.io.IOException;

import static org.infinite.spoty.SpotyResourceLoader.fxmlLoader;

public class Pages {
    //Dashboard
    private static final FXMLLoader dashboardLoader = fxmlLoader("fxml/dashboard/Dashboard.fxml");
    // Sale
    private static final FXMLLoader saleLoader = fxmlLoader("fxml/sales/Sales.fxml");
    private static final FXMLLoader posLoader = fxmlLoader("fxml/sales/PointOfSale.fxml");
    private static final FXMLLoader salesTermLoader = fxmlLoader("fxml/sales/SaleTerms.fxml");
    // Customer
    private static final FXMLLoader customerLoader =
            fxmlLoader("fxml/people/customers/Customers.fxml");
    // Supplier
    private static final FXMLLoader supplierLoader =
            fxmlLoader("fxml/people/suppliers/Suppliers.fxml");
    // Account
    private static final FXMLLoader bankReconciliationLoader = fxmlLoader("fxml/account/BankReconciliation.fxml");
    private static final FXMLLoader cashAdjustmentLoader = fxmlLoader("fxml/account/CashAdjustment.fxml");
    private static final FXMLLoader cashPaymentLoader = fxmlLoader("fxml/account/CashPayment.fxml");
    private static final FXMLLoader chartOfAccountLoader = fxmlLoader("fxml/account/ChartOfAccount.fxml");
    private static final FXMLLoader contraVoucherLoader = fxmlLoader("fxml/account/ContraVoucher.fxml");
    private static final FXMLLoader creditVoucherLoader = fxmlLoader("fxml/account/CreditVoucher.fxml");
    private static final FXMLLoader customerReceiveLoader = fxmlLoader("fxml/account/CustomerReceive.fxml");
    private static final FXMLLoader debitVoucherLoader = fxmlLoader("fxml/account/DebitVoucher.fxml");
    private static final FXMLLoader financialYearLoader = fxmlLoader("fxml/account/FinancialYear.fxml");
    private static final FXMLLoader journalVoucherLoader = fxmlLoader("fxml/account/JournalVoucher.fxml");
    private static final FXMLLoader openingBalanceLoader = fxmlLoader("fxml/account/OpeningBalance.fxml");
    private static final FXMLLoader paymentMethodsLoader = fxmlLoader("fxml/account/PaymentMethods.fxml");
    private static final FXMLLoader preDefinedAccountsLoader = fxmlLoader("fxml/account/PreDefinedAccounts.fxml");
    private static final FXMLLoader servicePaymentLoader = fxmlLoader("fxml/account/ServicePayment.fxml");
    private static final FXMLLoader subAccountLoader = fxmlLoader("fxml/account/SubAccount.fxml");
    private static final FXMLLoader supplierPaymentLoader = fxmlLoader("fxml/account/SupplierPayment.fxml");
    private static final FXMLLoader voucherApprovalLoader = fxmlLoader("fxml/account/VoucherApproval.fxml");
    // Account Report
    private static final FXMLLoader balanceSheetLoader = fxmlLoader("fxml/account/report/BalanceSheet.fxml");
    private static final FXMLLoader bankBookLoader = fxmlLoader("fxml/account/report/BankBook.fxml");
    private static final FXMLLoader cashBookLoader = fxmlLoader("fxml/account/report/CashBook.fxml");
    private static final FXMLLoader coaPrintLoader = fxmlLoader("fxml/account/report/CoaPrint.fxml");
    private static final FXMLLoader dayBookLoader = fxmlLoader("fxml/account/report/DayBook.fxml");
    private static final FXMLLoader expenditureStatementLoader = fxmlLoader("fxml/account/report/ExpenditureStatement.fxml");
    private static final FXMLLoader fixedAssetScheduleLoader = fxmlLoader("fxml/account/report/FixedAssetSchedule.fxml");
    private static final FXMLLoader generalLedgerLoader = fxmlLoader("fxml/account/report/GeneralLedger.fxml");
    private static final FXMLLoader incomeStatementLoader = fxmlLoader("fxml/account/report/IncomeStatement.fxml");
    private static final FXMLLoader profitLossLoader = fxmlLoader("fxml/account/report/ProfitLoss.fxml");
    private static final FXMLLoader receiptAndPaymentLoader = fxmlLoader("fxml/account/report/ReceiptAndPayment.fxml");
    private static final FXMLLoader subLedgerLoader = fxmlLoader("fxml/account/report/SubLedger.fxml");
    private static final FXMLLoader trialBalanceLoader = fxmlLoader("fxml/account/report/TrialBalance.fxml");
    private static final FXMLLoader bankReconciliationReportLoader = fxmlLoader("fxml/account/report/BankReconciliationReport.fxml");


    private static final FXMLLoader saleMasterFormLoader = fxmlLoader("forms/SaleMasterForm.fxml");
    private static final FXMLLoader productCategoryLoader =
            fxmlLoader("fxml/inventory/category/ProductCategory.fxml");
    private static final FXMLLoader brandLoader = fxmlLoader("fxml/inventory/brand/Brand.fxml");
    private static final FXMLLoader unitLoader =
            fxmlLoader("fxml/inventory/unit_of_measure/UnitOfMeasure.fxml");
    private static final FXMLLoader productLoader =
            fxmlLoader("fxml/inventory/products/Products.fxml");
    private static final FXMLLoader adjustmentLoader =
            fxmlLoader("fxml/inventory/adjustment/Adjustment.fxml");
    private static final FXMLLoader quotationLoader =
            fxmlLoader("fxml/inventory/quotation/Quotation.fxml");
    private static final FXMLLoader requisitionLoader =
            fxmlLoader("fxml/requisition/Requisition.fxml");
    private static final FXMLLoader purchaseLoader = fxmlLoader("fxml/purchases/Purchases.fxml");
    private static final FXMLLoader transferLoader = fxmlLoader("fxml/transfer/Transfer.fxml");
    private static final FXMLLoader stockInLoader = fxmlLoader("fxml/stock_in/StockIn.fxml");
    private static final FXMLLoader saleReturnLoader = fxmlLoader("fxml/returns/sales/Sales.fxml");
    private static final FXMLLoader purchaseReturnLoader =
            fxmlLoader("fxml/returns/purchases/Purchases.fxml");
    private static final FXMLLoader expenseCategoryLoader =
            fxmlLoader("fxml/expenses/category/Category.fxml");
    private static final FXMLLoader expenseLoader = fxmlLoader("fxml/expenses/expense/Expense.fxml");
    private static final FXMLLoader userLoader = fxmlLoader("fxml/people/users/Users.fxml");
    private static final FXMLLoader settingsLoader = fxmlLoader("fxml/settings/Settings.fxml");
    private static final FXMLLoader systemSettingsLoader =
            fxmlLoader("fxml/settings/system/System.fxml");
    private static final FXMLLoader posSettingsLoader = fxmlLoader("fxml/settings/pos/POS.fxml");
    private static final FXMLLoader roleSettingsLoader = fxmlLoader("fxml/settings/roles/Roles.fxml");
    private static final FXMLLoader branchSettingsLoader =
            fxmlLoader("fxml/settings/branches/Branches.fxml");
    private static final FXMLLoader currencySettingsLoader =
            fxmlLoader("fxml/settings/currency/Currency.fxml");
    private static final FXMLLoader exportSettingsLoader =
            fxmlLoader("fxml/settings/export/Export.fxml");
    private static final FXMLLoader quotationMasterFormLoader =
            fxmlLoader("forms/QuotationMasterForm.fxml");
    private static final FXMLLoader purchaseMasterFormLoader =
            fxmlLoader("forms/PurchaseMasterForm.fxml");
    private static final FXMLLoader requisitionMasterFormLoader =
            fxmlLoader("forms/RequisitionMasterForm.fxml");
    private static final FXMLLoader stockinMasterFormLoader =
            fxmlLoader("forms/StockInMasterForm.fxml");
    private static final FXMLLoader transferMasterFormLoader =
            fxmlLoader("forms/TransferMasterForm.fxml");
    private static final FXMLLoader adjustmentMasterFormLoader =
            fxmlLoader("forms/AdjustmentMasterForm.fxml");
    private static final FXMLLoader roleSettingsFormLoader =
            fxmlLoader("forms/RoleSettingsForm.fxml");

    // Dashboard
    private static BorderPane dashboardPane;
    // Sale
    private static BorderPane salePane;
    private static BorderPane posPane;
    private static BorderPane salesTermPane;
    // Customer
    private static BorderPane customerPane;
    // Supplier
    private static BorderPane supplierPane;
    // Account
    private static BorderPane bankReconciliationPane;
    private static BorderPane cashAdjustmentPane;
    private static BorderPane cashPaymentPane;
    private static BorderPane chartOfAccountPane;
    private static BorderPane contraVoucherPane;
    private static BorderPane creditVoucherPane;
    private static BorderPane customerReceivePane;
    private static BorderPane debitVoucherPane;
    private static BorderPane financialYearPane;
    private static BorderPane journalVoucherPane;
    private static BorderPane openingBalancePane;
    private static BorderPane paymentMethodsPane;
    private static BorderPane preDefinedAccountsPane;
    private static BorderPane servicePaymentPane;
    private static BorderPane subAccountPane;
    private static BorderPane supplierPaymentPane;
    private static BorderPane voucherApprovalPane;
    // Account Report
    private static BorderPane balanceSheetPane;
    private static BorderPane bankBookPane;
    private static BorderPane cashBookPane;
    private static BorderPane coaPrintPane;
    private static BorderPane dayBookPane;
    private static BorderPane expenditureStatementPane;
    private static BorderPane fixedAssetSchedulePane;
    private static BorderPane generalLedgerPane;
    private static BorderPane incomeStatementPane;
    private static BorderPane profitLossPane;
    private static BorderPane receiptAndPaymentPane;
    private static BorderPane subLedgerPane;
    private static BorderPane trialBalancePane;
    private static BorderPane bankReconciliationReportPane;


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
    private static BorderPane saleReturnPane;
    private static BorderPane purchaseReturnPane;
    private static BorderPane expenseCategoryPane;
    private static BorderPane expensePane;
    private static BorderPane userPane;
    private static BorderPane settingsPane;
    private static GridPane systemSettingsPane;
    private static AnchorPane posSettingsPane;
    private static BorderPane roleSettingsPane;
    private static BorderPane branchSettingsPane;
    private static BorderPane currencySettingsPane;
    private static BorderPane exportSettingsPane;
    private static BorderPane adjustmentMasterFormPane;
    private static BorderPane quotationMasterFormPane;
    private static BorderPane purchaseMasterFormPane;
    private static BorderPane requisitionMasterFormPane;
    private static BorderPane saleMasterFormPane;
    private static BorderPane stockinMasterFormPane;
    private static BorderPane transferMasterFormPane;
    private static BorderPane roleSettingsFormPane;

    private static void setDashboard() {
        dashboardLoader.setControllerFactory(e -> new DashboardController());
    }

    private static void setSales(Stage stage) {
        saleLoader.setControllerFactory(e -> SalesController.getInstance());
        posLoader.setControllerFactory(e -> new PointOfSaleController());
        salesTermLoader.setControllerFactory(e -> new SaleTermsController());
    }

    private static void setCustomer(Stage stage) {
        customerLoader.setControllerFactory(e -> CustomerController.getInstance(stage));
    }

    private static void setSupplier(Stage stage) {
        supplierLoader.setControllerFactory(e -> SupplierController.getInstance(stage));
    }

    private static void setAccounts(Stage stage) {
        bankReconciliationLoader.setControllerFactory(e -> new BankReconciliationController());
        cashAdjustmentLoader.setControllerFactory(e -> new CashAdjustmentController());
        cashPaymentLoader.setControllerFactory(e -> new CashPaymentController());
        chartOfAccountLoader.setControllerFactory(e -> new ChartOfAccountController());
        contraVoucherLoader.setControllerFactory(e -> new ContraVoucherController());
        creditVoucherLoader.setControllerFactory(e -> new CreditVoucherController());
        customerReceiveLoader.setControllerFactory(e -> new CustomerReceiveController());
        debitVoucherLoader.setControllerFactory(e -> new DebitVoucherController());
        financialYearLoader.setControllerFactory(e -> new FinancialYearController());
        journalVoucherLoader.setControllerFactory(e -> new JournalVoucherController());
        openingBalanceLoader.setControllerFactory(e -> new OpeningBalanceController());
        paymentMethodsLoader.setControllerFactory(e -> new PaymentMethodsController());
        preDefinedAccountsLoader.setControllerFactory(e -> new PreDefinedAccountsController());
        servicePaymentLoader.setControllerFactory(e -> new ServicePaymentController());
        subAccountLoader.setControllerFactory(e -> new SubAccountController());
        supplierPaymentLoader.setControllerFactory(e -> new ServicePaymentController());
        voucherApprovalLoader.setControllerFactory(e -> new VoucherApprovalController());
    }

    private static void setAccountReports(Stage stage) {
        balanceSheetLoader.setControllerFactory(e -> new BalanceSheetController());
        bankBookLoader.setControllerFactory(e -> new BankBookController());
        cashBookLoader.setControllerFactory(e -> new CashBookController());
        coaPrintLoader.setControllerFactory(e -> new CoaPrintController());
        dayBookLoader.setControllerFactory(e -> new DayBookController());
        expenditureStatementLoader.setControllerFactory(e -> new ExpenditureStatementController());
        fixedAssetScheduleLoader.setControllerFactory(e -> new FixedAssetScheduleController());
        generalLedgerLoader.setControllerFactory(e -> new GeneralLedgerController());
        incomeStatementLoader.setControllerFactory(e -> new IncomeStatementController());
        profitLossLoader.setControllerFactory(e -> new ProfitLossController());
        receiptAndPaymentLoader.setControllerFactory(e -> new ReceiptAndPaymentController());
        subLedgerLoader.setControllerFactory(e -> new SubLedgerController());
        trialBalanceLoader.setControllerFactory(e -> new TrialBalanceController());
        bankReconciliationReportLoader.setControllerFactory(e -> new BankReconciliationReportController());
    }

    private static void setProduct(Stage stage) {
        productCategoryLoader.setControllerFactory(e -> ProductCategoryController.getInstance(stage));
        brandLoader.setControllerFactory(e -> BrandController.getInstance(stage));
        unitLoader.setControllerFactory(e -> UnitOfMeasureController.getInstance(stage));
        productLoader.setControllerFactory(e -> ProductController.getInstance(stage));
        adjustmentLoader.setControllerFactory(e -> AdjustmentController.getInstance());
        quotationLoader.setControllerFactory(e -> QuotationController.getInstance());
    }

    private static void setSingleItems() {
        requisitionLoader.setControllerFactory(e -> RequisitionController.getInstance());
        purchaseLoader.setControllerFactory(e -> PurchasesController.getInstance());
        transferLoader.setControllerFactory(e -> TransferController.getInstance());
        stockInLoader.setControllerFactory(e -> StockInController.getInstance());
    }

    private static void setReturns() {
        saleReturnLoader.setControllerFactory(e -> new SaleReturnsController());
        purchaseReturnLoader.setControllerFactory(e -> new PurchaseReturnController());
    }

    private static void setExpenses(Stage stage) {
        expenseCategoryLoader.setControllerFactory(e -> ExpenseCategoryController.getInstance(stage));
        expenseLoader.setControllerFactory(e -> ExpenseController.getInstance(stage));
    }

    private static void setPeople(Stage stage) {
        userLoader.setControllerFactory(e -> UserController.getInstance(stage));
    }

    private static void setSettings(Stage stage) {
        settingsLoader.setControllerFactory(c -> SettingsController.getInstance());
        systemSettingsLoader.setControllerFactory(c -> SystemController.getInstance());
        posSettingsLoader.setControllerFactory(c -> POSController.getInstance());
        roleSettingsLoader.setControllerFactory(c -> RoleController.getInstance());
        branchSettingsLoader.setControllerFactory(c -> BranchController.getInstance(stage));
        currencySettingsLoader.setControllerFactory(c -> CurrencyController.getInstance(stage));
        exportSettingsLoader.setControllerFactory(c -> ExportController.getInstance());
        roleSettingsFormLoader.setControllerFactory(c -> RoleSettingsFormController.getInstance());
    }

    private static void setMasterForms(Stage stage) {
        adjustmentMasterFormLoader.setControllerFactory(
                c -> AdjustmentMasterFormController.getInstance(stage));
        quotationMasterFormLoader.setControllerFactory(
                c -> QuotationMasterFormController.getInstance(stage));
        purchaseMasterFormLoader.setControllerFactory(
                c -> PurchaseMasterFormController.getInstance(stage));
        requisitionMasterFormLoader.setControllerFactory(
                c -> RequisitionMasterFormController.getInstance(stage));
        saleMasterFormLoader.setControllerFactory(c -> SaleMasterFormController.getInstance(stage));
        stockinMasterFormLoader.setControllerFactory(
                c -> StockInMasterFormController.getInstance(stage));
        transferMasterFormLoader.setControllerFactory(
                c -> TransferMasterFormController.getInstance(stage));
    }

    public static void setPanes() throws IOException {
        // Dashboard
        dashboardPane = dashboardLoader.load();
        // Sales
        salePane = saleLoader.load();
        posPane = posLoader.load();
        salesTermPane = salesTermLoader.load();
        // Customer
        customerPane = customerLoader.load();
        // Supplier
        supplierPane = supplierLoader.load();
        // Account
        bankReconciliationPane = bankReconciliationLoader.load();
        cashAdjustmentPane = cashAdjustmentLoader.load();
        cashPaymentPane = cashPaymentLoader.load();
        chartOfAccountPane = chartOfAccountLoader.load();
        contraVoucherPane = contraVoucherLoader.load();
        creditVoucherPane = creditVoucherLoader.load();
        customerReceivePane = customerReceiveLoader.load();
        debitVoucherPane = debitVoucherLoader.load();
        financialYearPane = financialYearLoader.load();
        journalVoucherPane = journalVoucherLoader.load();
        openingBalancePane = openingBalanceLoader.load();
        paymentMethodsPane = paymentMethodsLoader.load();
        preDefinedAccountsPane = preDefinedAccountsLoader.load();
        servicePaymentPane = servicePaymentLoader.load();
        subAccountPane = subAccountLoader.load();
        supplierPaymentPane = supplierPaymentLoader.load();
        voucherApprovalPane = voucherApprovalLoader.load();
        // Account Report
        balanceSheetPane = balanceSheetLoader.load();
        bankBookPane = bankBookLoader.load();
        cashBookPane = cashBookLoader.load();
        coaPrintPane = coaPrintLoader.load();
        dayBookPane = dayBookLoader.load();
        expenditureStatementPane = expenditureStatementLoader.load();
        fixedAssetSchedulePane = fixedAssetScheduleLoader.load();
        generalLedgerPane = generalLedgerLoader.load();
        incomeStatementPane = incomeStatementLoader.load();
        profitLossPane = profitLossLoader.load();
        receiptAndPaymentPane = receiptAndPaymentLoader.load();
        subLedgerPane = subLedgerLoader.load();
        trialBalancePane = trialBalanceLoader.load();
        bankReconciliationReportPane = bankReconciliationReportLoader.load();

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

        saleReturnPane = saleReturnLoader.load();
        purchaseReturnPane = purchaseReturnLoader.load();

        expenseCategoryPane = expenseCategoryLoader.load();
        expensePane = expenseLoader.load();

        userPane = userLoader.load();

        settingsPane = settingsLoader.load();
        systemSettingsPane = systemSettingsLoader.load();
        posSettingsPane = posSettingsLoader.load();
        roleSettingsPane = roleSettingsLoader.load();
        branchSettingsPane = branchSettingsLoader.load();
        currencySettingsPane = currencySettingsLoader.load();
        exportSettingsPane = exportSettingsLoader.load();
        roleSettingsFormPane = roleSettingsFormLoader.load();

        adjustmentMasterFormPane = adjustmentMasterFormLoader.load();
        quotationMasterFormPane = quotationMasterFormLoader.load();
        purchaseMasterFormPane = purchaseMasterFormLoader.load();
        requisitionMasterFormPane = requisitionMasterFormLoader.load();
        saleMasterFormPane = saleMasterFormLoader.load();
        stockinMasterFormPane = stockinMasterFormLoader.load();
        transferMasterFormPane = transferMasterFormLoader.load();
    }

    public static void setControllers(Stage stage) {
        setDashboard();
        setSales(stage);
        setCustomer(stage);
        setSupplier(stage);
        setProduct(stage);
        setAccounts(stage);
        setAccountReports(stage);
        setSingleItems();
        setReturns();
        setExpenses(stage);
        setPeople(stage);
        setMasterForms(stage);
        setSettings(stage);
    }

    // Dashboard
    public static BorderPane getDashboardPane() {
        return dashboardPane;
    }

    // Sale
    public static BorderPane getSalePane() {
        return salePane;
    }

    public static BorderPane getPosPane() {
        return posPane;
    }

    public static BorderPane getSalesTermPane() {
        return salesTermPane;
    }

    // Customer
    public static BorderPane getCustomerPane() {
        return customerPane;
    }

    // Supplier
    public static BorderPane getSupplierPane() {
        return supplierPane;
    }

    // Account
    public static BorderPane getBankReconciliationPane() {
        return bankReconciliationPane;
    }

    public static BorderPane getCashAdjustmentPane() {
        return cashAdjustmentPane;
    }

    public static BorderPane getCashPaymentPane() {
        return cashPaymentPane;
    }

    public static BorderPane getChartOfAccountPane() {
        return chartOfAccountPane;
    }

    public static BorderPane getContraVoucherPane() {
        return contraVoucherPane;
    }

    public static BorderPane getCreditVoucherPane() {
        return creditVoucherPane;
    }

    public static BorderPane getCustomerReceivePane() {
        return customerReceivePane;
    }

    public static BorderPane getDebitVoucherPane() {
        return debitVoucherPane;
    }

    public static BorderPane getFinancialYearPane() {
        return financialYearPane;
    }

    public static BorderPane getJournalVoucherPane() {
        return journalVoucherPane;
    }

    public static BorderPane getOpeningBalancePane() {
        return openingBalancePane;
    }

    public static BorderPane getPaymentMethodsPane() {
        return paymentMethodsPane;
    }

    public static BorderPane getPreDefinedAccountsPane() {
        return preDefinedAccountsPane;
    }

    public static BorderPane getServicePaymentPane() {
        return servicePaymentPane;
    }

    public static BorderPane getSubAccountPane() {
        return subAccountPane;
    }

    public static BorderPane getSupplierPaymentPane() {
        return supplierPaymentPane;
    }

    public static BorderPane getVoucherApprovalPane() {
        return voucherApprovalPane;
    }

    // Account Report
    public static BorderPane getBalanceSheetPane() {
        return balanceSheetPane;
    }

    public static BorderPane getBankBookPane() {
        return bankBookPane;
    }

    public static BorderPane getCashBookPane() {
        return cashBookPane;
    }

    public static BorderPane getCoaPrintPane() {
        return coaPrintPane;
    }

    public static BorderPane getDayBookPane() {
        return dayBookPane;
    }

    public static BorderPane getExpenditureStatementPane() {
        return expenditureStatementPane;
    }

    public static BorderPane getFixedAssetSchedulePane() {
        return fixedAssetSchedulePane;
    }

    public static BorderPane getGeneralLedgerPane() {
        return generalLedgerPane;
    }

    public static BorderPane getIncomeStatementPane() {
        return incomeStatementPane;
    }

    public static BorderPane getProfitLossPane() {
        return profitLossPane;
    }

    public static BorderPane getReceiptAndPaymentPane() {
        return receiptAndPaymentPane;
    }

    public static BorderPane getBankReconciliationReportPane() {
        return bankReconciliationReportPane;
    }

    public static BorderPane getSubLedgerPane() {
        return subLedgerPane;
    }

    public static BorderPane getTrialBalancePane() {
        return trialBalancePane;
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

    public static BorderPane getUserPane() {
        return userPane;
    }

    public static BorderPane getSettingsPane() {
        return settingsPane;
    }

    public static GridPane getSystemSettingsPane() {
        return systemSettingsPane;
    }

    public static AnchorPane getPosSettingsPane() {
        return posSettingsPane;
    }

    public static BorderPane getRoleSettingsPane() {
        return roleSettingsPane;
    }

    public static BorderPane getBranchSettingsPane() {
        return branchSettingsPane;
    }

    public static BorderPane getCurrencySettingsPane() {
        return currencySettingsPane;
    }

    public static BorderPane getExportSettingsPane() {
        return exportSettingsPane;
    }

    public static BorderPane getAdjustmentMasterFormPane() {
        return adjustmentMasterFormPane;
    }

    public static BorderPane getQuotationMasterFormPane() {
        return quotationMasterFormPane;
    }

    public static BorderPane getPurchaseMasterFormPane() {
        return purchaseMasterFormPane;
    }

    public static BorderPane getRequisitionMasterFormPane() {
        return requisitionMasterFormPane;
    }

    public static BorderPane getSaleMasterFormPane() {
        return saleMasterFormPane;
    }

    public static BorderPane getStockInMasterFormPane() {
        return stockinMasterFormPane;
    }

    public static BorderPane getTransferMasterFormPane() {
        return transferMasterFormPane;
    }

    public static BorderPane getRoleSettingsFormPane() {
        return roleSettingsFormPane;
    }
}
