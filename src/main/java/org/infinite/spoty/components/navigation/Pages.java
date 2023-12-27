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
import org.infinite.spoty.forms.*;
import org.infinite.spoty.views.account.*;
import org.infinite.spoty.views.account.report.*;
import org.infinite.spoty.views.bank.BankController;
import org.infinite.spoty.views.customers.CustomerController;
import org.infinite.spoty.views.dashboard.DashboardController;
import org.infinite.spoty.views.expenses.category.ExpenseCategoryController;
import org.infinite.spoty.views.expenses.expense.ExpenseController;
import org.infinite.spoty.views.human_resource.attendance.AttendanceController;
import org.infinite.spoty.views.human_resource.attendance.AttendanceReportController;
import org.infinite.spoty.views.human_resource.attendance.CheckInController;
import org.infinite.spoty.views.human_resource.hrm.DesignationsController;
import org.infinite.spoty.views.human_resource.hrm.EmployeesController;
import org.infinite.spoty.views.human_resource.pay_roll.SalariesController;
import org.infinite.spoty.views.human_resource.pay_roll.SalaryAdvancesController;
import org.infinite.spoty.views.human_resource.pay_roll.SalaryGenerateController;
import org.infinite.spoty.views.inventory.adjustment.AdjustmentController;
import org.infinite.spoty.views.inventory.brand.BrandController;
import org.infinite.spoty.views.inventory.category.ProductCategoryController;
import org.infinite.spoty.views.inventory.products.ProductController;
import org.infinite.spoty.views.inventory.unit_of_measure.UnitOfMeasureController;
import org.infinite.spoty.views.login.LoginController;
import org.infinite.spoty.views.purchases.PurchasesController;
import org.infinite.spoty.views.quotation.QuotationController;
import org.infinite.spoty.views.report.*;
import org.infinite.spoty.views.requisition.RequisitionController;
import org.infinite.spoty.views.returns.purchases.PurchaseReturnController;
import org.infinite.spoty.views.returns.sales.SaleReturnsController;
import org.infinite.spoty.views.sales.SaleTermsController;
import org.infinite.spoty.views.sales.SalesController;
import org.infinite.spoty.views.sales.pos.PointOfSaleController;
import org.infinite.spoty.views.service.ServiceInvoicesController;
import org.infinite.spoty.views.service.ServicesController;
import org.infinite.spoty.views.settings.data_synchronizer.BackupController;
import org.infinite.spoty.views.settings.data_synchronizer.ExportController;
import org.infinite.spoty.views.settings.data_synchronizer.ImportController;
import org.infinite.spoty.views.settings.data_synchronizer.RestoreController;
import org.infinite.spoty.views.settings.role_permission.AssignUserRoleController;
import org.infinite.spoty.views.settings.role_permission.RolesController;
import org.infinite.spoty.views.settings.system_settings.*;
import org.infinite.spoty.views.stock_in.StockInController;
import org.infinite.spoty.views.stock_report.StockReportController;
import org.infinite.spoty.views.suppliers.SupplierController;
import org.infinite.spoty.views.tax.TaxSettingsController;
import org.infinite.spoty.views.tax.TaxesController;
import org.infinite.spoty.views.transfer.TransferController;

import java.io.IOException;

import static org.infinite.spoty.SpotyResourceLoader.fxmlLoader;

public class Pages {
    //Login
    private static final FXMLLoader loginLoader = fxmlLoader("fxml/login/Login.fxml");
    //Dashboard
    private static final FXMLLoader dashboardLoader = fxmlLoader("fxml/dashboard/Dashboard.fxml");
    // Sale
    private static final FXMLLoader saleLoader = fxmlLoader("fxml/sales/Sales.fxml");
    private static final FXMLLoader posLoader = fxmlLoader("fxml/sales/PointOfSale.fxml");
    private static final FXMLLoader salesTermLoader = fxmlLoader("fxml/sales/SaleTerms.fxml");
    // Customer
    private static final FXMLLoader customerLoader =
            fxmlLoader("fxml/customers/Customers.fxml");
    // Supplier
    private static final FXMLLoader supplierLoader =
            fxmlLoader("fxml/suppliers/Suppliers.fxml");
    // Stock Report
    private static final FXMLLoader stockReportLoader = fxmlLoader("fxml/stock_report/StockReport.fxml");
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
    // Reports
    private static final FXMLLoader closingLoader = fxmlLoader("fxml/report/Closing.fxml");
    private static final FXMLLoader closingReportLoader = fxmlLoader("fxml/report/ClosingReport.fxml");
    private static final FXMLLoader dailyCustomerReportLoader = fxmlLoader("fxml/report/DailyCustomerReport.fxml");
    private static final FXMLLoader dailyReportLoader = fxmlLoader("fxml/report/DailyReport.fxml");
    private static final FXMLLoader dueReportLoader = fxmlLoader("fxml/report/DueReport.fxml");
    private static final FXMLLoader profitReportLoader = fxmlLoader("fxml/report/ProfitReport.fxml");
    private static final FXMLLoader purchaseReportLoader = fxmlLoader("fxml/report/PurchaseReport.fxml");
    private static final FXMLLoader salesReportLoader = fxmlLoader("fxml/report/SalesReport.fxml");
    private static final FXMLLoader salesReturnLoader = fxmlLoader("fxml/report/SalesReturn.fxml");
    private static final FXMLLoader shippingCostReportLoader = fxmlLoader("fxml/report/ShippingCostReport.fxml");
    private static final FXMLLoader taxReportLoader = fxmlLoader("fxml/report/TaxReport.fxml");
    private static final FXMLLoader userSalesReportLoader = fxmlLoader("fxml/report/UserSalesReport.fxml");
    // HUMAN RESOURCE
    // Attendance
    private static final FXMLLoader attendanceLoader = fxmlLoader("fxml/human_resource/attendance/Attendance.fxml");
    private static final FXMLLoader attendanceReportLoader = fxmlLoader("fxml/human_resource/attendance/AttendanceReport.fxml");
    private static final FXMLLoader checkInsLoader = fxmlLoader("fxml/human_resource/attendance/CheckIn.fxml");
    // Human Resource Management
    private static final FXMLLoader designationsLoader = fxmlLoader("fxml/human_resource/hrm/Designations.fxml");
    private static final FXMLLoader employeesLoader = fxmlLoader("fxml/human_resource/hrm/Employees.fxml");
    // PayRoll
    private static final FXMLLoader salariesLoader = fxmlLoader("fxml/human_resource/pay_roll/Salaries.fxml");
    private static final FXMLLoader salaryAdvancesLoader = fxmlLoader("fxml/human_resource/pay_roll/SalaryAdvances.fxml");
    private static final FXMLLoader salaryGenerateLoader = fxmlLoader("fxml/human_resource/pay_roll/SalaryGenerate.fxml");
    // Bank
    private static final FXMLLoader banksLoader = fxmlLoader("fxml/bank/Bank.fxml");
    // Service
    private static final FXMLLoader servicesLoader = fxmlLoader("fxml/service/Services.fxml");
    private static final FXMLLoader serviceInvoicesLoader = fxmlLoader("fxml/service/ServiceInvoices.fxml");
    // Quotation
    private static final FXMLLoader quotationLoader =
            fxmlLoader("fxml/quotation/Quotation.fxml");
    // Tax
    private static final FXMLLoader taxesLoader =
            fxmlLoader("fxml/tax/Taxes.fxml");
    private static final FXMLLoader taxSettingsLoader =
            fxmlLoader("fxml/tax/TaxSettings.fxml");
    // SETTINGS
    // Data Synchronizer
    private static final FXMLLoader backUpLoader = fxmlLoader("fxml/settings/data_synchronizer/Backup.fxml");
    private static final FXMLLoader exportLoader = fxmlLoader("fxml/settings/data_synchronizer/Export.fxml");
    private static final FXMLLoader importLoader = fxmlLoader("fxml/settings/data_synchronizer/Import.fxml");
    private static final FXMLLoader restoreLoader = fxmlLoader("fxml/settings/data_synchronizer/Restore.fxml");
    // Role Permission
    private static final FXMLLoader assignUserRoleLoader = fxmlLoader("fxml/settings/role_permission/AssignUserRole.fxml");
    private static final FXMLLoader rolesLoader = fxmlLoader("fxml/settings/role_permission/Roles.fxml");
    // System Settings
    private static final FXMLLoader appSettingsLoader = fxmlLoader("fxml/settings/system_settings/AppSettings.fxml");
    private static final FXMLLoader branchesLoader = fxmlLoader("fxml/settings/system_settings/Branches.fxml");
    private static final FXMLLoader companySettingsLoader = fxmlLoader("fxml/settings/system_settings/CompanyDetails.fxml");
    private static final FXMLLoader currencyLoader = fxmlLoader("fxml/settings/system_settings/Currency.fxml");
    private static final FXMLLoader languagesLoader = fxmlLoader("fxml/settings/system_settings/Languages.fxml");
    private static final FXMLLoader mailSettingsLoader = fxmlLoader("fxml/settings/system_settings/MailSettings.fxml");
    private static final FXMLLoader posSettingsLoader = fxmlLoader("fxml/settings/system_settings/POS.fxml");
    private static final FXMLLoader printSettingsLoader = fxmlLoader("fxml/settings/system_settings/PrintSettings.fxml");
    private static final FXMLLoader settingsLoader = fxmlLoader("fxml/settings/system_settings/Settings.fxml");
    private static final FXMLLoader systemLoader = fxmlLoader("fxml/settings/system_settings/System.fxml");
    private static final FXMLLoader usersLoader = fxmlLoader("fxml/settings/system_settings/Users.fxml");

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
    private static final FXMLLoader quotationMasterFormLoader =
            fxmlLoader("forms/QuotationMasterForm.fxml");
    private static final FXMLLoader purchaseMasterFormLoader =
            fxmlLoader("forms/PurchaseMasterForm.fxml");
    private static final FXMLLoader requisitionMasterFormLoader =
            fxmlLoader("forms/RequisitionMasterForm.fxml");
    private static final FXMLLoader stockInMasterFormLoader =
            fxmlLoader("forms/StockInMasterForm.fxml");
    private static final FXMLLoader transferMasterFormLoader =
            fxmlLoader("forms/TransferMasterForm.fxml");
    private static final FXMLLoader adjustmentMasterFormLoader =
            fxmlLoader("forms/AdjustmentMasterForm.fxml");
    private static final FXMLLoader roleSettingsFormLoader =
            fxmlLoader("forms/RoleSettingsForm.fxml");

    // Login
    private static BorderPane loginPane;
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
    // Stock Report
    private static BorderPane stockReportPane;
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
    // Reports
    private static BorderPane closingPane;
    private static BorderPane closingReportPane;
    private static BorderPane dailyCustomerReportPane;
    private static BorderPane dailyReportPane;
    private static BorderPane dueReportPane;
    private static BorderPane profitReportPane;
    private static BorderPane purchaseReportPane;
    private static BorderPane salesReportPane;
    private static BorderPane salesReturnPane;
    private static BorderPane shippingCostReportPane;
    private static BorderPane taxReportPane;
    private static BorderPane userSalesReportPane;
    // HUMAN RESOURCE
    // Attendance
    private static BorderPane attendancePane;
    private static BorderPane attendanceReportPane;
    private static BorderPane checkInPane;
    // Human Resource Management
    private static BorderPane designationsPane;
    private static BorderPane employeesPane;
    // PayRoll
    private static BorderPane salariesPane;
    private static BorderPane salaryAdvancesPane;
    private static BorderPane salaryGeneratePane;
    // Bank
    private static BorderPane bankPane;
    // Service
    private static BorderPane servicesPane;
    private static BorderPane serviceInvoicesPane;
    // Quotation
    private static BorderPane quotationPane;
    // Quotation
    private static BorderPane taxesPane;
    private static BorderPane taxSettingsPane;
    // SETTINGS
    // Data Synchronizer
    private static BorderPane backUpPane;
    private static BorderPane exportPane;
    private static BorderPane importPane;
    private static BorderPane restorePane;
    // Role Permission
    private static BorderPane assignUserRolePane;
    private static BorderPane rolesPane;
    // System Settings
    private static BorderPane appSettingsPane;
    private static BorderPane branchesPane;
    private static BorderPane companySettingsPane;
    private static BorderPane currencyPane;
    private static BorderPane languagesPane;
    private static BorderPane mailSettingsPane;
    private static BorderPane printSettingsPane;
    private static BorderPane systemPane;
    private static BorderPane usersPane;


    private static BorderPane productCategoryPane;
    private static BorderPane brandPane;
    private static BorderPane unitPane;
    private static BorderPane productPane;
    private static BorderPane adjustmentPane;
    private static BorderPane requisitionPane;
    private static BorderPane purchasePane;
    private static BorderPane transferPane;
    private static BorderPane stockInPane;
    private static BorderPane saleReturnPane;
    private static BorderPane purchaseReturnPane;
    private static BorderPane expenseCategoryPane;
    private static BorderPane expensePane;
    private static BorderPane settingsPane;
    private static BorderPane adjustmentMasterFormPane;
    private static BorderPane quotationMasterFormPane;
    private static BorderPane purchaseMasterFormPane;
    private static BorderPane requisitionMasterFormPane;
    private static BorderPane saleMasterFormPane;
    private static BorderPane stockInMasterFormPane;
    private static BorderPane transferMasterFormPane;
    private static BorderPane roleSettingsFormPane;

    private static void setLogin(Stage stage) {
        loginLoader.setControllerFactory(e -> new LoginController(stage));
    }

    private static void setDashboard() {
        dashboardLoader.setControllerFactory(e -> new DashboardController());
    }

    private static void setSales(Stage stage) {
        saleLoader.setControllerFactory(e -> SalesController.getInstance());
        posLoader.setControllerFactory(e -> new PointOfSaleController());
        salesTermLoader.setControllerFactory(e -> SaleTermsController.getInstance(stage));
    }

    private static void setCustomer(Stage stage) {
        customerLoader.setControllerFactory(e -> CustomerController.getInstance(stage));
    }

    private static void setSupplier(Stage stage) {
        supplierLoader.setControllerFactory(e -> SupplierController.getInstance(stage));
    }

    private static void setProduct(Stage stage) {
        productCategoryLoader.setControllerFactory(e -> ProductCategoryController.getInstance(stage));
        brandLoader.setControllerFactory(e -> BrandController.getInstance(stage));
        unitLoader.setControllerFactory(e -> UnitOfMeasureController.getInstance(stage));
        productLoader.setControllerFactory(e -> ProductController.getInstance(stage));
        adjustmentLoader.setControllerFactory(e -> AdjustmentController.getInstance());
    }

    private static void setStockReport(Stage stage) {
        stockReportLoader.setControllerFactory(e -> new StockReportController());
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

    private static void setReports(Stage stage) {
        closingLoader.setControllerFactory(e -> new ClosingController());
        closingReportLoader.setControllerFactory(e -> new ClosingReportController());
        dailyCustomerReportLoader.setControllerFactory(e -> new DailyCustomerReportController());
        dailyReportLoader.setControllerFactory(e -> new DailyReportController());
        dueReportLoader.setControllerFactory(e -> new DueReportController());
        profitReportLoader.setControllerFactory(e -> new ProfitReportController());
        purchaseReportLoader.setControllerFactory(e -> new PurchaseReportController());
        salesReportLoader.setControllerFactory(e -> new SalesReportController());
        salesReturnLoader.setControllerFactory(e -> new SalesReturnController());
        shippingCostReportLoader.setControllerFactory(e -> new ShippingCostReportController());
        taxReportLoader.setControllerFactory(e -> new TaxReportController());
        userSalesReportLoader.setControllerFactory(e -> new UserSalesReportController());
    }

    private static void setAttendance(Stage stage) {
        attendanceLoader.setControllerFactory(e -> new AttendanceController());
        attendanceReportLoader.setControllerFactory(e -> new AttendanceReportController());
        checkInsLoader.setControllerFactory(e -> new CheckInController());
    }

    private static void setHRM(Stage stage) {
        designationsLoader.setControllerFactory(e -> new DesignationsController());
        employeesLoader.setControllerFactory(e -> new EmployeesController());
    }

    private static void setPayRoll(Stage stage) {
        salariesLoader.setControllerFactory(e -> new SalariesController());
        salaryAdvancesLoader.setControllerFactory(e -> new SalaryAdvancesController());
        salaryGenerateLoader.setControllerFactory(e -> new SalaryGenerateController());
    }

    private static void setBank(Stage stage) {
        banksLoader.setControllerFactory(e -> new BankController());
    }

    private static void setService(Stage stage) {
        servicesLoader.setControllerFactory(e -> new ServicesController());
        serviceInvoicesLoader.setControllerFactory(e -> new ServiceInvoicesController());
    }

    private static void setQuotation(Stage stage) {
        quotationLoader.setControllerFactory(e -> QuotationController.getInstance());
    }

    private static void setTax(Stage stage) {
        taxesLoader.setControllerFactory(e -> new TaxesController());
        taxSettingsLoader.setControllerFactory(e -> new TaxSettingsController());
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

    private static void setDataSynchronizer(Stage stage) {
        backUpLoader.setControllerFactory(c -> new BackupController());
        exportLoader.setControllerFactory(c -> new ExportController());
        importLoader.setControllerFactory(c -> new ImportController());
        restoreLoader.setControllerFactory(c -> new RestoreController());
    }

    private static void setRolePermission(Stage stage) {
        assignUserRoleLoader.setControllerFactory(c -> new AssignUserRoleController());
        rolesLoader.setControllerFactory(c -> new RolesController());
    }

    private static void setSystemSettings(Stage stage) {
        appSettingsLoader.setControllerFactory(c -> new AppSettingsController());
        branchesLoader.setControllerFactory(c -> BranchController.getInstance(stage));
        companySettingsLoader.setControllerFactory(c -> new CompanyDetailsController());
        currencyLoader.setControllerFactory(c -> CurrencyController.getInstance(stage));
        languagesLoader.setControllerFactory(c -> new LanguagesController());
        mailSettingsLoader.setControllerFactory(c -> new MailSettingsController());
        posSettingsLoader.setControllerFactory(c -> POSController.getInstance());
        printSettingsLoader.setControllerFactory(c -> new PrintSettingsController());
        settingsLoader.setControllerFactory(c -> new SettingsController());
        systemLoader.setControllerFactory(c -> new SystemController());
        usersLoader.setControllerFactory(c -> UserController.getInstance(stage));
    }

    private static void setExpenses(Stage stage) {
        expenseCategoryLoader.setControllerFactory(e -> ExpenseCategoryController.getInstance(stage));
        expenseLoader.setControllerFactory(e -> ExpenseController.getInstance(stage));
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
        stockInMasterFormLoader.setControllerFactory(
                c -> StockInMasterFormController.getInstance(stage));
        transferMasterFormLoader.setControllerFactory(
                c -> TransferMasterFormController.getInstance(stage));
    }

    public static void setPanes() throws IOException {
        // Login
        loginPane = loginLoader.load();
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
        // Stock Report
        stockReportPane = stockReportLoader.load();
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
        // Reports
        closingPane = closingLoader.load();
        closingReportPane = closingReportLoader.load();
        dailyCustomerReportPane = dailyCustomerReportLoader.load();
        dailyReportPane = dailyReportLoader.load();
        dueReportPane = dueReportLoader.load();
        profitReportPane = profitReportLoader.load();
        purchaseReportPane = purchaseReportLoader.load();
        salesReportPane = salesReportLoader.load();
        salesReturnPane = salesReturnLoader.load();
        shippingCostReportPane = shippingCostReportLoader.load();
        taxReportPane = taxReportLoader.load();
        userSalesReportPane = userSalesReportLoader.load();
        // HUMAN RESOURCE
        // Attendance
        attendancePane = attendanceLoader.load();
        attendanceReportPane = attendanceReportLoader.load();
        checkInPane = checkInsLoader.load();
        // HUMAN RESOURCE
        // Human Resource Management
        designationsPane = designationsLoader.load();
        employeesPane = employeesLoader.load();
        // HUMAN RESOURCE
        // PayRoll
        salariesPane = salariesLoader.load();
        salaryAdvancesPane = salaryAdvancesLoader.load();
        salaryGeneratePane = salaryGenerateLoader.load();
        // Bank
        bankPane = banksLoader.load();
        // Service
        servicesPane = servicesLoader.load();
        serviceInvoicesPane = serviceInvoicesLoader.load();
        // Quotation
        quotationPane = quotationLoader.load();
        // Tax
        taxesPane = taxesLoader.load();
        taxSettingsPane = taxSettingsLoader.load();
        // SETTINGS
        // Data Synchronizer
        backUpPane = backUpLoader.load();
        exportPane = exportLoader.load();
        importPane = importLoader.load();
        restorePane = restoreLoader.load();
        // Role Permission
        assignUserRolePane = assignUserRoleLoader.load();
        rolesPane = rolesLoader.load();
        // System Settings
        appSettingsPane = appSettingsLoader.load();
        branchesPane = branchesLoader.load();
        companySettingsPane = companySettingsLoader.load();
        currencyPane = currencyLoader.load();
        languagesPane = languagesLoader.load();
        mailSettingsPane = mailSettingsLoader.load();
        printSettingsPane = printSettingsLoader.load();
        systemPane = systemLoader.load();
        usersPane = usersLoader.load();

        productCategoryPane = productCategoryLoader.load();
        brandPane = brandLoader.load();
        unitPane = unitLoader.load();
        productPane = productLoader.load();
        adjustmentPane = adjustmentLoader.load();

        requisitionPane = requisitionLoader.load();
        purchasePane = purchaseLoader.load();
        transferPane = transferLoader.load();
        stockInPane = stockInLoader.load();

        saleReturnPane = saleReturnLoader.load();
        purchaseReturnPane = purchaseReturnLoader.load();

        expenseCategoryPane = expenseCategoryLoader.load();
        expensePane = expenseLoader.load();

        settingsPane = settingsLoader.load();
        roleSettingsFormPane = roleSettingsFormLoader.load();

        adjustmentMasterFormPane = adjustmentMasterFormLoader.load();
        quotationMasterFormPane = quotationMasterFormLoader.load();
        purchaseMasterFormPane = purchaseMasterFormLoader.load();
        requisitionMasterFormPane = requisitionMasterFormLoader.load();
        saleMasterFormPane = saleMasterFormLoader.load();
        stockInMasterFormPane = stockInMasterFormLoader.load();
        transferMasterFormPane = transferMasterFormLoader.load();
    }

    public static void setControllers(Stage stage) {
        setLogin(stage);
        setDashboard();
        setSales(stage);
        setCustomer(stage);
        setSupplier(stage);
        setProduct(stage);
        setStockReport(stage);
        setAccounts(stage);
        setAccountReports(stage);
        setReports(stage);
        setAttendance(stage);
        setHRM(stage);
        setPayRoll(stage);
        setBank(stage);
        setService(stage);
        setQuotation(stage);
        setTax(stage);
        setSingleItems();
        setReturns();
        setExpenses(stage);
        setMasterForms(stage);
        setDataSynchronizer(stage);
        setRolePermission(stage);
        setSystemSettings(stage);
    }

    // Login
    public static BorderPane getLoginPane() {
        return loginPane;
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

    // Supplier
    public static BorderPane getStockReportPane() {
        return stockReportPane;
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

    // Reports
    public static BorderPane getClosingPane() {
        return closingPane;
    }

    public static BorderPane getClosingReportPane() {
        return closingReportPane;
    }

    public static BorderPane getDailyCustomerReportPane() {
        return dailyCustomerReportPane;
    }

    public static BorderPane getDailyReportPane() {
        return dailyReportPane;
    }

    public static BorderPane getDueReportPane() {
        return dueReportPane;
    }

    public static BorderPane getProfitReportPane() {
        return profitReportPane;
    }

    public static BorderPane getPurchaseReportPane() {
        return purchaseReportPane;
    }

    public static BorderPane getSalesReportPane() {
        return salesReportPane;
    }

    public static BorderPane getSalesReturnPane() {
        return salesReturnPane;
    }

    public static BorderPane getShippingCostReportPane() {
        return shippingCostReportPane;
    }

    public static BorderPane getTaxReportPane() {
        return taxReportPane;
    }

    public static BorderPane getUserSalesReportPane() {
        return userSalesReportPane;
    }

    // HUMAN RESOURCE
    // Attendance
    public static BorderPane getAttendancePane() {
        return attendancePane;
    }

    public static BorderPane getAttendanceReportPane() {
        return attendanceReportPane;
    }

    public static BorderPane getCheckInPane() {
        return checkInPane;
    }

    // Human Resource Management
    public static BorderPane getDesignationsPane() {
        return designationsPane;
    }

    public static BorderPane getEmployeesPane() {
        return employeesPane;
    }

    // PayRoll
    public static BorderPane getSalariesPane() {
        return salariesPane;
    }

    public static BorderPane getSalaryAdvancesPane() {
        return salaryAdvancesPane;
    }

    public static BorderPane getSalaryGeneratePane() {
        return salaryGeneratePane;
    }

    // Bank
    public static BorderPane getBankPane() {
        return bankPane;
    }

    // Service
    public static BorderPane getServicesPane() {
        return servicesPane;
    }

    public static BorderPane getServiceInvoicesPane() {
        return serviceInvoicesPane;
    }

    // Quotation
    public static BorderPane getQuotationPane() {
        return quotationPane;
    }

    // Tax
    public static BorderPane getTaxesPane() {
        return taxesPane;
    }

    public static BorderPane getTaxSettingsPane() {
        return taxSettingsPane;
    }

    // SETTINGS
    // Data Synchronizer
    public static BorderPane getBackUpPane() {
        return backUpPane;
    }

    public static BorderPane getExportPane() {
        return exportPane;
    }

    public static BorderPane getImportPane() {
        return importPane;
    }

    public static BorderPane getRestorePane() {
        return restorePane;
    }

    // Role Permission
    public static BorderPane getAssignUserRolePane() {
        return assignUserRolePane;
    }

    public static BorderPane getRolesPane() {
        return rolesPane;
    }

    // System Settings
    public static BorderPane getAppSettingsPane() {
        return appSettingsPane;
    }

    public static BorderPane getBranchesPane() {
        return branchesPane;
    }

    public static BorderPane getCompanyDetailsPane() {
        return companySettingsPane;
    }

    public static BorderPane getCurrencyPane() {
        return currencyPane;
    }

    public static BorderPane getLanguagesPane() {
        return languagesPane;
    }

    public static BorderPane getMailSettingsPane() {
        return mailSettingsPane;
    }

    public static BorderPane getPrintSettingsPane() {
        return printSettingsPane;
    }

    public static BorderPane getSystemPane() {
        return systemPane;
    }

    public static BorderPane getUsersPane() {
        return usersPane;
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

    public static BorderPane getSettingsPane() {
        return settingsPane;
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
        return stockInMasterFormPane;
    }

    public static BorderPane getTransferMasterFormPane() {
        return transferMasterFormPane;
    }

    public static BorderPane getRoleSettingsFormPane() {
        return roleSettingsFormPane;
    }
}
