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
import lombok.Getter;
import org.infinite.spoty.views.forms.*;
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
import org.infinite.spoty.views.product.adjustment.AdjustmentController;
import org.infinite.spoty.views.product.brand.BrandController;
import org.infinite.spoty.views.product.category.ProductCategoryController;
import org.infinite.spoty.views.product.products.ProductController;
import org.infinite.spoty.views.product.unit_of_measure.UnitOfMeasureController;
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
import org.infinite.spoty.views.service.ServiceInvoiceController;
import org.infinite.spoty.views.service.ServiceController;
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
    private static final FXMLLoader loginLoader = fxmlLoader("views/login/Login.fxml");
    //Dashboard
    private static final FXMLLoader dashboardLoader = fxmlLoader("views/dashboard/Dashboard.fxml");
    // Sale
    private static final FXMLLoader saleLoader = fxmlLoader("views/sales/Sales.fxml");
    private static final FXMLLoader posLoader = fxmlLoader("views/sales/PointOfSale.fxml");
    private static final FXMLLoader salesTermLoader = fxmlLoader("views/sales/SaleTerms.fxml");
    // Customer
    private static final FXMLLoader customerLoader =
            fxmlLoader("views/customers/Customers.fxml");
    // Supplier
    private static final FXMLLoader supplierLoader =
            fxmlLoader("views/suppliers/Suppliers.fxml");
    // Stock Report
    private static final FXMLLoader stockReportLoader = fxmlLoader("views/stock_report/StockReport.fxml");
    // Account
    private static final FXMLLoader bankReconciliationLoader = fxmlLoader("views/account/BankReconciliation.fxml");
    private static final FXMLLoader cashAdjustmentLoader = fxmlLoader("views/account/CashAdjustment.fxml");
    private static final FXMLLoader cashPaymentLoader = fxmlLoader("views/account/CashPayment.fxml");
    private static final FXMLLoader chartOfAccountLoader = fxmlLoader("views/account/ChartOfAccount.fxml");
    private static final FXMLLoader contraVoucherLoader = fxmlLoader("views/account/ContraVoucher.fxml");
    private static final FXMLLoader creditVoucherLoader = fxmlLoader("views/account/CreditVoucher.fxml");
    private static final FXMLLoader customerReceiveLoader = fxmlLoader("views/account/CustomerReceive.fxml");
    private static final FXMLLoader debitVoucherLoader = fxmlLoader("views/account/DebitVoucher.fxml");
    private static final FXMLLoader financialYearLoader = fxmlLoader("views/account/FinancialYear.fxml");
    private static final FXMLLoader journalVoucherLoader = fxmlLoader("views/account/JournalVoucher.fxml");
    private static final FXMLLoader openingBalanceLoader = fxmlLoader("views/account/OpeningBalance.fxml");
    private static final FXMLLoader paymentMethodsLoader = fxmlLoader("views/account/PaymentMethods.fxml");
    private static final FXMLLoader preDefinedAccountsLoader = fxmlLoader("views/account/PreDefinedAccounts.fxml");
    private static final FXMLLoader servicePaymentLoader = fxmlLoader("views/account/ServicePayment.fxml");
    private static final FXMLLoader subAccountLoader = fxmlLoader("views/account/SubAccount.fxml");
    private static final FXMLLoader supplierPaymentLoader = fxmlLoader("views/account/SupplierPayment.fxml");
    private static final FXMLLoader voucherApprovalLoader = fxmlLoader("views/account/VoucherApproval.fxml");
    // Account Report
    private static final FXMLLoader balanceSheetLoader = fxmlLoader("views/account/report/BalanceSheet.fxml");
    private static final FXMLLoader bankBookLoader = fxmlLoader("views/account/report/BankBook.fxml");
    private static final FXMLLoader cashBookLoader = fxmlLoader("views/account/report/CashBook.fxml");
    private static final FXMLLoader coaPrintLoader = fxmlLoader("views/account/report/CoaPrint.fxml");
    private static final FXMLLoader dayBookLoader = fxmlLoader("views/account/report/DayBook.fxml");
    private static final FXMLLoader expenditureStatementLoader = fxmlLoader("views/account/report/ExpenditureStatement.fxml");
    private static final FXMLLoader fixedAssetScheduleLoader = fxmlLoader("views/account/report/FixedAssetSchedule.fxml");
    private static final FXMLLoader generalLedgerLoader = fxmlLoader("views/account/report/GeneralLedger.fxml");
    private static final FXMLLoader incomeStatementLoader = fxmlLoader("views/account/report/IncomeStatement.fxml");
    private static final FXMLLoader profitLossLoader = fxmlLoader("views/account/report/ProfitLoss.fxml");
    private static final FXMLLoader receiptAndPaymentLoader = fxmlLoader("views/account/report/ReceiptAndPayment.fxml");
    private static final FXMLLoader subLedgerLoader = fxmlLoader("views/account/report/SubLedger.fxml");
    private static final FXMLLoader trialBalanceLoader = fxmlLoader("views/account/report/TrialBalance.fxml");
    private static final FXMLLoader bankReconciliationReportLoader = fxmlLoader("views/account/report/BankReconciliationReport.fxml");
    // Reports
    private static final FXMLLoader closingLoader = fxmlLoader("views/report/Closing.fxml");
    private static final FXMLLoader closingReportLoader = fxmlLoader("views/report/ClosingReport.fxml");
    private static final FXMLLoader dailyCustomerReportLoader = fxmlLoader("views/report/DailyCustomerReport.fxml");
    private static final FXMLLoader dailyReportLoader = fxmlLoader("views/report/DailyReport.fxml");
    private static final FXMLLoader dueReportLoader = fxmlLoader("views/report/DueReport.fxml");
    private static final FXMLLoader profitReportLoader = fxmlLoader("views/report/ProfitReport.fxml");
    private static final FXMLLoader purchaseReportLoader = fxmlLoader("views/report/PurchaseReport.fxml");
    private static final FXMLLoader salesReportLoader = fxmlLoader("views/report/SalesReport.fxml");
    private static final FXMLLoader salesReturnLoader = fxmlLoader("views/report/SalesReturn.fxml");
    private static final FXMLLoader shippingCostReportLoader = fxmlLoader("views/report/ShippingCostReport.fxml");
    private static final FXMLLoader taxReportLoader = fxmlLoader("views/report/TaxReport.fxml");
    private static final FXMLLoader userSalesReportLoader = fxmlLoader("views/report/UserSalesReport.fxml");
    // HUMAN RESOURCE
    // Attendance
    private static final FXMLLoader attendanceLoader = fxmlLoader("views/human_resource/attendance/Attendance.fxml");
    private static final FXMLLoader attendanceReportLoader = fxmlLoader("views/human_resource/attendance/AttendanceReport.fxml");
    private static final FXMLLoader checkInsLoader = fxmlLoader("views/human_resource/attendance/CheckIn.fxml");
    // Human Resource Management
    private static final FXMLLoader designationsLoader = fxmlLoader("views/human_resource/hrm/Designations.fxml");
    private static final FXMLLoader employeesLoader = fxmlLoader("views/human_resource/hrm/Employees.fxml");
    // PayRoll
    private static final FXMLLoader salariesLoader = fxmlLoader("views/human_resource/pay_roll/Salaries.fxml");
    private static final FXMLLoader salaryAdvancesLoader = fxmlLoader("views/human_resource/pay_roll/SalaryAdvances.fxml");
    private static final FXMLLoader salaryGenerateLoader = fxmlLoader("views/human_resource/pay_roll/SalaryGenerate.fxml");
    // Bank
    private static final FXMLLoader banksLoader = fxmlLoader("views/bank/Bank.fxml");
    // Service
    private static final FXMLLoader servicesLoader = fxmlLoader("views/service/Services.fxml");
    private static final FXMLLoader serviceInvoicesLoader = fxmlLoader("views/service/ServiceInvoices.fxml");
    // Quotation
    private static final FXMLLoader quotationLoader =
            fxmlLoader("views/quotation/Quotation.fxml");
    // Tax
    private static final FXMLLoader taxesLoader =
            fxmlLoader("views/tax/Taxes.fxml");
    private static final FXMLLoader taxSettingsLoader =
            fxmlLoader("views/tax/TaxSettings.fxml");
    // SETTINGS
    // Data Synchronizer
    private static final FXMLLoader backUpLoader = fxmlLoader("views/settings/data_synchronizer/Backup.fxml");
    private static final FXMLLoader exportLoader = fxmlLoader("views/settings/data_synchronizer/Export.fxml");
    private static final FXMLLoader importLoader = fxmlLoader("views/settings/data_synchronizer/Import.fxml");
    private static final FXMLLoader restoreLoader = fxmlLoader("views/settings/data_synchronizer/Restore.fxml");
    // Role Permission
    private static final FXMLLoader assignUserRoleLoader = fxmlLoader("views/settings/role_permission/AssignUserRole.fxml");
    private static final FXMLLoader rolesLoader = fxmlLoader("views/settings/role_permission/Roles.fxml");
    // System Settings
    private static final FXMLLoader appSettingsLoader = fxmlLoader("views/settings/system_settings/AppSettings.fxml");
    private static final FXMLLoader branchesLoader = fxmlLoader("views/settings/system_settings/Branches.fxml");
    private static final FXMLLoader companySettingsLoader = fxmlLoader("views/settings/system_settings/CompanyDetails.fxml");
    private static final FXMLLoader currencyLoader = fxmlLoader("views/settings/system_settings/Currency.fxml");
    private static final FXMLLoader languagesLoader = fxmlLoader("views/settings/system_settings/Languages.fxml");
    private static final FXMLLoader mailSettingsLoader = fxmlLoader("views/settings/system_settings/MailSettings.fxml");
    private static final FXMLLoader posSettingsLoader = fxmlLoader("views/settings/system_settings/POS.fxml");
    private static final FXMLLoader printSettingsLoader = fxmlLoader("views/settings/system_settings/PrintSettings.fxml");
    private static final FXMLLoader settingsLoader = fxmlLoader("views/settings/system_settings/Settings.fxml");
    private static final FXMLLoader systemLoader = fxmlLoader("views/settings/system_settings/System.fxml");
    private static final FXMLLoader usersLoader = fxmlLoader("views/settings/system_settings/Users.fxml");

    private static final FXMLLoader saleMasterFormLoader = fxmlLoader("views/forms/SaleMasterForm.fxml");
    private static final FXMLLoader productCategoryLoader =
            fxmlLoader("views/product/category/ProductCategory.fxml");
    private static final FXMLLoader brandLoader = fxmlLoader("views/product/brand/Brand.fxml");
    private static final FXMLLoader unitLoader =
            fxmlLoader("views/product/unit_of_measure/UnitOfMeasure.fxml");
    private static final FXMLLoader productLoader =
            fxmlLoader("views/product/products/Products.fxml");
    private static final FXMLLoader adjustmentLoader =
            fxmlLoader("views/product/adjustment/Adjustment.fxml");
    private static final FXMLLoader requisitionLoader =
            fxmlLoader("views/requisition/Requisition.fxml");
    private static final FXMLLoader purchaseLoader = fxmlLoader("views/purchases/Purchases.fxml");
    private static final FXMLLoader transferLoader = fxmlLoader("views/transfer/Transfer.fxml");
    private static final FXMLLoader stockInLoader = fxmlLoader("views/stock_in/StockIn.fxml");
    private static final FXMLLoader saleReturnLoader = fxmlLoader("views/returns/sales/Sales.fxml");
    private static final FXMLLoader purchaseReturnLoader =
            fxmlLoader("views/returns/purchases/Purchases.fxml");
    private static final FXMLLoader expenseCategoryLoader =
            fxmlLoader("views/expenses/category/Category.fxml");
    private static final FXMLLoader expenseLoader = fxmlLoader("views/expenses/expense/Expense.fxml");
    private static final FXMLLoader quotationMasterFormLoader =
            fxmlLoader("views/forms/QuotationMasterForm.fxml");
    private static final FXMLLoader purchaseMasterFormLoader =
            fxmlLoader("views/forms/PurchaseMasterForm.fxml");
    private static final FXMLLoader requisitionMasterFormLoader =
            fxmlLoader("views/forms/RequisitionMasterForm.fxml");
    private static final FXMLLoader stockInMasterFormLoader =
            fxmlLoader("views/forms/StockInMasterForm.fxml");
    private static final FXMLLoader transferMasterFormLoader =
            fxmlLoader("views/forms/TransferMasterForm.fxml");
    private static final FXMLLoader adjustmentMasterFormLoader =
            fxmlLoader("views/forms/AdjustmentMasterForm.fxml");
    private static final FXMLLoader roleSettingsFormLoader =
            fxmlLoader("views/forms/RoleSettingsForm.fxml");

    // Login
    @Getter
    private static BorderPane loginPane;
    // Dashboard
    @Getter
    private static BorderPane dashboardPane;

    @Getter
    // Sale
    private static BorderPane salePane;

    @Getter
    private static BorderPane posPane;

    @Getter
    private static BorderPane salesTermPane;

    @Getter
    // Customer
    private static BorderPane customerPane;

    @Getter
    // Supplier
    private static BorderPane supplierPane;

    @Getter
    // Stock Report
    private static BorderPane stockReportPane;

    @Getter
    // Account
    private static BorderPane bankReconciliationPane;

    @Getter
    private static BorderPane cashAdjustmentPane;

    @Getter
    private static BorderPane cashPaymentPane;

    @Getter
    private static BorderPane chartOfAccountPane;

    @Getter
    private static BorderPane contraVoucherPane;

    @Getter
    private static BorderPane creditVoucherPane;

    @Getter
    private static BorderPane customerReceivePane;

    @Getter
    private static BorderPane debitVoucherPane;

    @Getter
    private static BorderPane financialYearPane;

    @Getter
    private static BorderPane journalVoucherPane;

    @Getter
    private static BorderPane openingBalancePane;

    @Getter
    private static BorderPane paymentMethodsPane;

    @Getter
    private static BorderPane preDefinedAccountsPane;

    @Getter
    private static BorderPane servicePaymentPane;

    @Getter
    private static BorderPane subAccountPane;

    @Getter
    private static BorderPane supplierPaymentPane;

    @Getter
    private static BorderPane voucherApprovalPane;

    @Getter
    // Account Report
    private static BorderPane balanceSheetPane;

    @Getter
    private static BorderPane bankBookPane;

    @Getter
    private static BorderPane cashBookPane;

    @Getter
    private static BorderPane coaPrintPane;

    @Getter
    private static BorderPane dayBookPane;

    @Getter
    private static BorderPane expenditureStatementPane;

    @Getter
    private static BorderPane fixedAssetSchedulePane;

    @Getter
    private static BorderPane generalLedgerPane;

    @Getter
    private static BorderPane incomeStatementPane;

    @Getter
    private static BorderPane profitLossPane;

    @Getter
    private static BorderPane receiptAndPaymentPane;

    @Getter
    private static BorderPane subLedgerPane;

    @Getter
    private static BorderPane trialBalancePane;

    @Getter
    private static BorderPane bankReconciliationReportPane;

    @Getter
    // Reports
    private static BorderPane closingPane;

    @Getter
    private static BorderPane closingReportPane;

    @Getter
    private static BorderPane dailyCustomerReportPane;

    @Getter
    private static BorderPane dailyReportPane;

    @Getter
    private static BorderPane dueReportPane;

    @Getter
    private static BorderPane profitReportPane;

    @Getter
    private static BorderPane purchaseReportPane;

    @Getter
    private static BorderPane salesReportPane;

    @Getter
    private static BorderPane salesReturnPane;

    @Getter
    private static BorderPane shippingCostReportPane;

    @Getter
    private static BorderPane taxReportPane;

    @Getter
    private static BorderPane userSalesReportPane;

    @Getter
    // HUMAN RESOURCE
    // Attendance
    private static BorderPane attendancePane;

    @Getter
    private static BorderPane attendanceReportPane;

    @Getter
    private static BorderPane checkInPane;

    @Getter
    // Human Resource Management
    private static BorderPane designationsPane;

    @Getter
    private static BorderPane employeesPane;

    @Getter
    // PayRoll
    private static BorderPane salariesPane;

    @Getter
    private static BorderPane salaryAdvancesPane;

    @Getter
    private static BorderPane salaryGeneratePane;

    @Getter
    // Bank
    private static BorderPane bankPane;

    @Getter
    // Service
    private static BorderPane servicesPane;

    @Getter
    private static BorderPane serviceInvoicesPane;

    @Getter
    // Quotation
    private static BorderPane quotationPane;

    @Getter
    // Quotation
    private static BorderPane taxesPane;

    @Getter
    private static BorderPane taxSettingsPane;

    @Getter
    // SETTINGS
    // Data Synchronizer
    private static BorderPane backUpPane;

    @Getter
    private static BorderPane exportPane;

    @Getter
    private static BorderPane importPane;

    @Getter
    private static BorderPane restorePane;

    @Getter
    // Role Permission
    private static BorderPane assignUserRolePane;

    @Getter
    private static BorderPane rolesPane;

    @Getter
    // System Settings
    private static BorderPane appSettingsPane;

    @Getter
    private static BorderPane branchesPane;

    @Getter
    private static BorderPane companySettingsPane;

    @Getter
    private static BorderPane currencyPane;

    @Getter
    private static BorderPane languagesPane;

    @Getter
    private static BorderPane mailSettingsPane;

    @Getter
    private static BorderPane printSettingsPane;

    @Getter
    private static BorderPane systemPane;

    @Getter
    private static BorderPane usersPane;

    @Getter


    private static BorderPane productCategoryPane;

    @Getter
    private static BorderPane brandPane;

    @Getter
    private static BorderPane unitPane;

    @Getter
    private static BorderPane productPane;

    @Getter
    private static BorderPane adjustmentPane;

    @Getter
    private static BorderPane requisitionPane;

    @Getter
    private static BorderPane purchasePane;

    @Getter
    private static BorderPane transferPane;

    @Getter
    private static BorderPane stockInPane;

    @Getter
    private static BorderPane saleReturnPane;

    @Getter
    private static BorderPane purchaseReturnPane;

    @Getter
    private static BorderPane expenseCategoryPane;

    @Getter
    private static BorderPane expensePane;

    @Getter
    private static BorderPane settingsPane;

    @Getter
    private static BorderPane adjustmentMasterFormPane;

    @Getter
    private static BorderPane quotationMasterFormPane;

    @Getter
    private static BorderPane purchaseMasterFormPane;

    @Getter
    private static BorderPane requisitionMasterFormPane;

    @Getter
    private static BorderPane saleMasterFormPane;

    @Getter
    private static BorderPane stockInMasterFormPane;

    @Getter
    private static BorderPane transferMasterFormPane;

    @Getter
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
        banksLoader.setControllerFactory(e -> BankController.getInstance(stage));
    }

    private static void setService(Stage stage) {
        servicesLoader.setControllerFactory(e -> ServiceController.getInstance(stage));
        serviceInvoicesLoader.setControllerFactory(e -> ServiceInvoiceController.getInstance(stage));
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
}
