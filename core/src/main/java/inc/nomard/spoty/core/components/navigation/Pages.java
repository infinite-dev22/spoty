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

package inc.nomard.spoty.core.components.navigation;

import inc.nomard.spoty.core.views.bank.BankController;
import inc.nomard.spoty.core.views.customers.CustomerController;
import inc.nomard.spoty.core.views.dashboard.DashboardController;
import inc.nomard.spoty.core.views.expenses.category.ExpenseCategoryController;
import inc.nomard.spoty.core.views.expenses.expense.ExpenseController;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.core.views.human_resource.hrm.DesignationsController;
import inc.nomard.spoty.core.views.human_resource.hrm.EmployeesController;
import inc.nomard.spoty.core.views.human_resource.hrm.EmploymentStatusController;
import inc.nomard.spoty.core.views.human_resource.leave.CalendarController;
import inc.nomard.spoty.core.views.human_resource.leave.LeaveRequestController;
import inc.nomard.spoty.core.views.human_resource.pay_roll.BeneficiaryBadgeController;
import inc.nomard.spoty.core.views.human_resource.pay_roll.BeneficiaryTypeController;
import inc.nomard.spoty.core.views.human_resource.pay_roll.SalariesController;
import inc.nomard.spoty.core.views.human_resource.pay_roll.SalaryAdvancesController;
import inc.nomard.spoty.core.views.human_resource.pay_roll.pay_slip.PaySlipsController;
import inc.nomard.spoty.core.views.inventory.adjustment.AdjustmentController;
import inc.nomard.spoty.core.views.inventory.brand.BrandController;
import inc.nomard.spoty.core.views.inventory.category.ProductCategoryController;
import inc.nomard.spoty.core.views.inventory.products.ProductController;
import inc.nomard.spoty.core.views.inventory.unit_of_measure.UnitOfMeasureController;
import inc.nomard.spoty.core.views.login.LoginController;
import inc.nomard.spoty.core.views.previews.people.UserPreviewController;
import inc.nomard.spoty.core.views.purchases.PurchaseReturnController;
import inc.nomard.spoty.core.views.purchases.PurchasesController;
import inc.nomard.spoty.core.views.quotation.QuotationController;
import inc.nomard.spoty.core.views.report.*;
import inc.nomard.spoty.core.views.requisition.RequisitionController;
import inc.nomard.spoty.core.views.sales.OrdersController;
import inc.nomard.spoty.core.views.sales.SaleReturnsController;
import inc.nomard.spoty.core.views.sales.SaleTermsController;
import inc.nomard.spoty.core.views.sales.pos.PointOfSaleController;
import inc.nomard.spoty.core.views.settings.data_synchronizer.ExportController;
import inc.nomard.spoty.core.views.settings.data_synchronizer.ImportController;
import inc.nomard.spoty.core.views.settings.role_permission.AssignUserRoleController;
import inc.nomard.spoty.core.views.settings.role_permission.RolesController;
import inc.nomard.spoty.core.views.settings.system_settings.AppSettingsController;
import inc.nomard.spoty.core.views.settings.system_settings.BranchController;
import inc.nomard.spoty.core.views.settings.system_settings.CompanyDetailsController;
import inc.nomard.spoty.core.views.settings.system_settings.CurrencyController;
import inc.nomard.spoty.core.views.stock_in.StockInController;
import inc.nomard.spoty.core.views.suppliers.SupplierController;
import inc.nomard.spoty.core.views.tax.TaxSettingsController;
import inc.nomard.spoty.core.views.tax.TaxesController;
import inc.nomard.spoty.core.views.transfer.TransferController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.fxmlLoader;

public class Pages {
    //Login
    private static final FXMLLoader loginLoader = fxmlLoader("views/login/Login.fxml");
    //Dashboard
    private static final FXMLLoader dashboardLoader = fxmlLoader("views/dashboard/Dashboard.fxml");
    // Sale
    private static final FXMLLoader posLoader = fxmlLoader("views/sales/PointOfSale.fxml");
    private static final FXMLLoader saleLoader = fxmlLoader("views/sales/Orders.fxml");
    private static final FXMLLoader saleReturnLoader = fxmlLoader("views/sales/SaleReturns.fxml");
    private static final FXMLLoader salesTermLoader = fxmlLoader("views/sales/SaleTerms.fxml");
    // Customer
    private static final FXMLLoader customerLoader =
            fxmlLoader("views/customers/Customers.fxml");
    // Supplier
    private static final FXMLLoader supplierLoader =
            fxmlLoader("views/suppliers/Suppliers.fxml");
    // Purchases
    private static final FXMLLoader purchaseLoader = fxmlLoader("views/purchases/Purchases.fxml");
    private static final FXMLLoader purchaseReturnLoader =
            fxmlLoader("views/purchases/PurchaseReturns.fxml");
    // Reports
    private static final FXMLLoader stockReportLoader = fxmlLoader("views/report/StockReport.fxml");
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
    // Human Resource Management
    private static final FXMLLoader designationsLoader = fxmlLoader("views/human_resource/hrm/Designations.fxml");
    private static final FXMLLoader userProfileLoader = fxmlLoader("views/previews/people/UserPreview.fxml");
    private static final FXMLLoader employeesLoader = fxmlLoader("views/human_resource/hrm/Employees.fxml");
    private static final FXMLLoader employmentStatusLoader = fxmlLoader("views/human_resource/hrm/EmploymentStatus.fxml");
    // Leave
    private static final FXMLLoader leaveRequestLoader = fxmlLoader("views/human_resource/leave/LeaveRequest.fxml");
    private static final FXMLLoader calendarLoader = fxmlLoader("views/human_resource/leave/Calendar.fxml");
    // PayRoll
    private static final FXMLLoader paySlipsLoader = fxmlLoader("views/human_resource/pay_roll/pay_slip/PaySlips.fxml");
    private static final FXMLLoader salariesLoader = fxmlLoader("views/human_resource/pay_roll/Salaries.fxml");
    private static final FXMLLoader salaryAdvancesLoader = fxmlLoader("views/human_resource/pay_roll/SalaryAdvances.fxml");
    private static final FXMLLoader beneficiaryBadgeLoader = fxmlLoader("views/human_resource/pay_roll/BeneficiaryBadge.fxml");
    private static final FXMLLoader beneficiaryTypeLoader = fxmlLoader("views/human_resource/pay_roll/BeneficiaryType.fxml");
    // Bank
    private static final FXMLLoader banksLoader = fxmlLoader("views/bank/Bank.fxml");
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
    private static final FXMLLoader exportLoader = fxmlLoader("views/settings/data_synchronizer/Export.fxml");
    private static final FXMLLoader importLoader = fxmlLoader("views/settings/data_synchronizer/Import.fxml");
    // Role Permission
    private static final FXMLLoader assignUserRoleLoader = fxmlLoader("views/settings/role_permission/AssignUserRole.fxml");
    private static final FXMLLoader rolesLoader = fxmlLoader("views/settings/role_permission/Roles.fxml");
    // System Settings
    private static final FXMLLoader appSettingsLoader = fxmlLoader("views/settings/system_settings/AppSettings.fxml");
    private static final FXMLLoader branchesLoader = fxmlLoader("views/settings/system_settings/Branches.fxml");
    private static final FXMLLoader companySettingsLoader = fxmlLoader("views/settings/system_settings/CompanyDetails.fxml");
    private static final FXMLLoader currencyLoader = fxmlLoader("views/settings/system_settings/Currency.fxml");

    private static final FXMLLoader saleMasterFormLoader = fxmlLoader("views/forms/SaleMasterForm.fxml");
    private static final FXMLLoader productCategoryLoader =
            fxmlLoader("views/inventory/category/ProductCategory.fxml");
    private static final FXMLLoader brandLoader = fxmlLoader("views/inventory/brand/Brand.fxml");
    private static final FXMLLoader unitLoader =
            fxmlLoader("views/inventory/unit_of_measure/UnitOfMeasure.fxml");
    private static final FXMLLoader productLoader =
            fxmlLoader("views/inventory/products/Products.fxml");
    private static final FXMLLoader adjustmentLoader =
            fxmlLoader("views/inventory/adjustment/Adjustment.fxml");
    private static final FXMLLoader requisitionLoader =
            fxmlLoader("views/requisition/Requisition.fxml");
    private static final FXMLLoader transferLoader = fxmlLoader("views/transfer/Transfer.fxml");
    private static final FXMLLoader stockInLoader = fxmlLoader("views/stock_in/StockIn.fxml");
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

    // Reports
    @Getter
    private static BorderPane stockReportPane;

    @Getter
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

    // HUMAN RESOURCE
    @Getter
    // Human Resource Management
    private static BorderPane designationsPane;

    @Getter
    private static BorderPane employeesPane;

    @Getter
    private static BorderPane userProfilePane;

    @Getter
    private static BorderPane employmentStatusPane;

    // Leave
    @Getter
    private static BorderPane leaveRequestPane;

    @Getter
    private static BorderPane calendarPane;

    @Getter
    // PayRoll
    private static BorderPane paySlipsPane;

    @Getter
    private static BorderPane salariesPane;

    @Getter
    private static BorderPane salaryAdvancesPane;

    @Getter
    private static BorderPane beneficiaryBadgePane;

    @Getter
    private static BorderPane beneficiaryTypePane;

    @Getter
    // Bank
    private static BorderPane bankPane;

    @Getter
    // Quotation
    private static BorderPane quotationPane;

    @Getter
    // Quotation
    private static BorderPane taxesPane;

    @Getter
    private static BorderPane taxSettingsPane;

    // SETTINGS
    // Data Synchronizer
    @Getter
    private static BorderPane exportPane;

    @Getter
    private static BorderPane importPane;

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
        saleLoader.setControllerFactory(e -> OrdersController.getInstance(stage));
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
        adjustmentLoader.setControllerFactory(e -> AdjustmentController.getInstance(stage));
    }

    private static void setStockReport(Stage stage) {
        stockReportLoader.setControllerFactory(e -> new StockReportController());
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

    private static void setHRM(Stage stage) {
        designationsLoader.setControllerFactory(e -> DesignationsController.getInstance(stage));
        employeesLoader.setControllerFactory(e -> EmployeesController.getInstance(stage));
        userProfileLoader.setControllerFactory(e -> UserPreviewController.getInstance(stage));
        employmentStatusLoader.setControllerFactory(e -> EmploymentStatusController.getInstance(stage));
    }

    private static void setLeave(Stage stage) {
        leaveRequestLoader.setControllerFactory(e -> LeaveRequestController.getInstance(stage));
        calendarLoader.setControllerFactory(e -> CalendarController.getInstance());
    }

    private static void setPayRoll(Stage stage) {
        paySlipsLoader.setControllerFactory(e -> new PaySlipsController());
        salariesLoader.setControllerFactory(e -> new SalariesController());
        salaryAdvancesLoader.setControllerFactory(e -> new SalaryAdvancesController());
        beneficiaryBadgeLoader.setControllerFactory(e -> BeneficiaryBadgeController.getInstance(stage));
        beneficiaryTypeLoader.setControllerFactory(e -> BeneficiaryTypeController.getInstance(stage));
    }

    private static void setBank(Stage stage) {
        banksLoader.setControllerFactory(e -> BankController.getInstance(stage));
    }

    private static void setQuotation(Stage stage) {
        quotationLoader.setControllerFactory(e -> QuotationController.getInstance(stage));
    }

    private static void setTax(Stage stage) {
        taxesLoader.setControllerFactory(e -> new TaxesController());
        taxSettingsLoader.setControllerFactory(e -> new TaxSettingsController());
    }

    private static void setSingleItems(Stage stage) {
        requisitionLoader.setControllerFactory(e -> RequisitionController.getInstance(stage));
        purchaseLoader.setControllerFactory(e -> PurchasesController.getInstance(stage));
        transferLoader.setControllerFactory(e -> TransferController.getInstance(stage));
        stockInLoader.setControllerFactory(e -> StockInController.getInstance(stage));
    }

    private static void setReturns(Stage stage) {
        saleReturnLoader.setControllerFactory(e -> SaleReturnsController.getInstance(stage));
        purchaseReturnLoader.setControllerFactory(e -> PurchaseReturnController.getInstance(stage));
    }

    private static void setDataSynchronizer(Stage stage) {
        exportLoader.setControllerFactory(c -> new ExportController());
        importLoader.setControllerFactory(c -> new ImportController());
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
        posPane = posLoader.load();
        salePane = saleLoader.load();
        saleReturnPane = saleReturnLoader.load();
        salesTermPane = salesTermLoader.load();
        // Customer
        customerPane = customerLoader.load();
        // Supplier
        supplierPane = supplierLoader.load();
        // Reports
        stockReportPane = stockReportLoader.load();
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
        // HUMAN RESOURCE
        // Human Resource Management
        designationsPane = designationsLoader.load();
        employeesPane = employeesLoader.load();
        userProfilePane = userProfileLoader.load();
        employmentStatusPane = employmentStatusLoader.load();
        // Purchase
        purchasePane = purchaseLoader.load();
        purchaseReturnPane = purchaseReturnLoader.load();
        // HUMAN RESOURCE
        // Leave
        leaveRequestPane = leaveRequestLoader.load();
        calendarPane = calendarLoader.load();
        // HUMAN RESOURCE
        // PayRoll
        paySlipsPane = paySlipsLoader.load();
        salariesPane = salariesLoader.load();
        salaryAdvancesPane = salaryAdvancesLoader.load();
        beneficiaryBadgePane = beneficiaryBadgeLoader.load();
        beneficiaryTypePane = beneficiaryTypeLoader.load();
        // Bank
        bankPane = banksLoader.load();
        // Quotation
        quotationPane = quotationLoader.load();
        // Tax
        taxesPane = taxesLoader.load();
        taxSettingsPane = taxSettingsLoader.load();
        // SETTINGS
        // Data Synchronizer
        exportPane = exportLoader.load();
        importPane = importLoader.load();
        // Role Permission
        assignUserRolePane = assignUserRoleLoader.load();
        rolesPane = rolesLoader.load();
        // System Settings
        appSettingsPane = appSettingsLoader.load();
        branchesPane = branchesLoader.load();
        companySettingsPane = companySettingsLoader.load();
        currencyPane = currencyLoader.load();

        productCategoryPane = productCategoryLoader.load();
        brandPane = brandLoader.load();
        unitPane = unitLoader.load();
        productPane = productLoader.load();
        adjustmentPane = adjustmentLoader.load();

        requisitionPane = requisitionLoader.load();
        transferPane = transferLoader.load();
        stockInPane = stockInLoader.load();

        expenseCategoryPane = expenseCategoryLoader.load();
        expensePane = expenseLoader.load();

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
        setReports(stage);
        setHRM(stage);
        setLeave(stage);
        setPayRoll(stage);
        setBank(stage);
        setQuotation(stage);
        setTax(stage);
        setSingleItems(stage);
        setReturns(stage);
        setExpenses(stage);
        setMasterForms(stage);
        setDataSynchronizer(stage);
        setRolePermission(stage);
        setSystemSettings(stage);
    }
}
