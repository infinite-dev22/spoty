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

import javafx.animation.FadeTransition;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.infinite.spoty.utils.SpotyLogger;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Navigation {
    static final int PAGE_TRANSITION_DURATION = 300;
    private static final Map<String, NavTree.NavTreeItem> NAV_TREE = createNavItems();
    private static StackPane viewWindow;
    private static Navigation instance;
    private final ReadOnlyObjectWrapper<NavTree.NavTreeItem> navTree =
            new ReadOnlyObjectWrapper<>(createTree());

    private Navigation(StackPane viewWindow) {
        Navigation.viewWindow = viewWindow;
    }

    public static Navigation getInstance(StackPane viewWindow) {
        if (instance == null) {
            instance = new Navigation(viewWindow);
            instance.loadView(Pages.getDashboardPane());
        }
        return instance;
    }

    public static @NotNull Map<String, NavTree.NavTreeItem> createNavItems() {
        var map = new HashMap<String, NavTree.NavTreeItem>();
        // Sale
        map.put("SALE", NavTree.NavTreeItem.page("Sale", Pages.getSalePane()));
        map.put("POINT_OF_SALE", NavTree.NavTreeItem.page("Point Of Sale", Pages.getPosPane()));
        map.put("SALE_TERMS", NavTree.NavTreeItem.page("Sale Terms", Pages.getSalesTermPane()));
        // Product
        map.put("CATEGORY", NavTree.NavTreeItem.page("Category", Pages.getProductCategoryPane()));
        map.put("BRAND", NavTree.NavTreeItem.page("Brand", Pages.getBrandPane()));
        map.put("UNIT", NavTree.NavTreeItem.page("Unit", Pages.getUnitPane()));
        map.put("PRODUCTS", NavTree.NavTreeItem.page("Products", Pages.getProductPane()));
        map.put("ADJUSTMENTS", NavTree.NavTreeItem.page("Adjustments", Pages.getAdjustmentPane()));
        // Accounts
        map.put("CHART_OF_ACCOUNT", NavTree.NavTreeItem.page("Chart Of Account", Pages.getChartOfAccountPane()));
        map.put("SUB_ACCOUNT", NavTree.NavTreeItem.page("Sub Account", Pages.getSubAccountPane()));
        map.put("PREDEFINED_ACCOUNTS", NavTree.NavTreeItem.page("Predefined Accounts", Pages.getPreDefinedAccountsPane()));
        map.put("FINANCIAL_YEAR", NavTree.NavTreeItem.page("Financial Year", Pages.getFinancialYearPane()));
        map.put("OPENING_BALANCE", NavTree.NavTreeItem.page("Opening Balance", Pages.getOpeningBalancePane()));
        map.put("DEBIT_VOUCHER", NavTree.NavTreeItem.page("Debit Voucher", Pages.getDebitVoucherPane()));
        map.put("CREDIT_VOUCHER", NavTree.NavTreeItem.page("Credit Voucher", Pages.getCreditVoucherPane()));
        map.put("CONTRA_VOUCHER", NavTree.NavTreeItem.page("Contra Voucher", Pages.getContraVoucherPane()));
        map.put("JOURNAL_VOUCHER", NavTree.NavTreeItem.page("Journal Voucher", Pages.getJournalVoucherPane()));
        map.put("BANK_RECONCILIATION", NavTree.NavTreeItem.page("Bank Reconciliation", Pages.getBankReconciliationPane()));
        map.put("PAYMENT_METHODS", NavTree.NavTreeItem.page("Payment Methods", Pages.getPaymentMethodsPane()));
        map.put("SUPPLIER_PAYMENT", NavTree.NavTreeItem.page("Supplier Payments", Pages.getSupplierPaymentPane()));
        map.put("CUSTOMER_RECEIVE", NavTree.NavTreeItem.page("Customer Receive", Pages.getCustomerReceivePane()));
        map.put("SERVICE_PAYMENT", NavTree.NavTreeItem.page("Service Payments", Pages.getServicePaymentPane()));
        map.put("CASH_ADJUSTMENT", NavTree.NavTreeItem.page("Cash Adjustment", Pages.getCashAdjustmentPane()));
        map.put("VOUCHER_APPROVAL", NavTree.NavTreeItem.page("Voucher Approval", Pages.getVoucherApprovalPane()));
        // Account Reports
        map.put("CASH_BOOK", NavTree.NavTreeItem.page("Cash Book", Pages.getCashBookPane()));
        map.put("BANK_BOOK", NavTree.NavTreeItem.page("Bank Book", Pages.getBankBookPane()));
        map.put("DAY_BOOK", NavTree.NavTreeItem.page("Day Book", Pages.getDayBookPane()));
        map.put("GENERAL_LEDGER", NavTree.NavTreeItem.page("General Ledger", Pages.getGeneralLedgerPane()));
        map.put("SUB_LEDGER", NavTree.NavTreeItem.page("Sub Ledger", Pages.getSubLedgerPane()));
        map.put("TRIAL_BALANCE", NavTree.NavTreeItem.page("Trial Balance", Pages.getTrialBalancePane()));
        map.put("INCOME_STATEMENT", NavTree.NavTreeItem.page("Income Statement", Pages.getIncomeStatementPane()));
        map.put("EXPENDITURE_STATEMENT", NavTree.NavTreeItem.page("Expenditure Statement", Pages.getExpenditureStatementPane()));
        map.put("PROFIT_LOSS", NavTree.NavTreeItem.page("Profits & Losses", Pages.getProfitLossPane()));
        map.put("BALANCE_SHEET", NavTree.NavTreeItem.page("Balance Sheet", Pages.getBalanceSheetPane()));
        map.put("FIXED_ASSET_SCHEDULE", NavTree.NavTreeItem.page("Fixed Asset Schedule", Pages.getFixedAssetSchedulePane()));
        map.put("RECEIPT_PAYMENT", NavTree.NavTreeItem.page("Receipt & Payment", Pages.getReceiptAndPaymentPane()));
        map.put("BANK_RECONCILIATION_REPORT", NavTree.NavTreeItem.page("Bank Reconciliation", Pages.getBankReconciliationReportPane()));
        map.put("COA_PRINT", NavTree.NavTreeItem.page("Coa Print", Pages.getCoaPrintPane()));
        // Reports
        map.put("CLOSING", NavTree.NavTreeItem.page("Closing", Pages.getClosingPane()));
        map.put("CLOSING_REPORT", NavTree.NavTreeItem.page("Closing Report", Pages.getClosingReportPane()));
        map.put("DAILY_CUSTOMER_REPORT", NavTree.NavTreeItem.page("Daily Customer Report", Pages.getDailyCustomerReportPane()));
        map.put("DAILY_REPORT", NavTree.NavTreeItem.page("Daily Report", Pages.getDailyReportPane()));
        map.put("DUE_REPORT", NavTree.NavTreeItem.page("Due Report", Pages.getDueReportPane()));
        map.put("PROFIT_REPORT", NavTree.NavTreeItem.page("Profit Report", Pages.getProfitReportPane()));
        map.put("PURCHASE_REPORT", NavTree.NavTreeItem.page("Purchase Report", Pages.getPurchaseReportPane()));
        map.put("SALES_REPORT", NavTree.NavTreeItem.page("Sales Report", Pages.getSalesReportPane()));
        map.put("SALES_RETURN", NavTree.NavTreeItem.page("Sales Return", Pages.getSalesReturnPane()));
        map.put("SHIPPING_COST_REPORT", NavTree.NavTreeItem.page("Shipping Cost Report", Pages.getShippingCostReportPane()));
        map.put("TAX_REPORT", NavTree.NavTreeItem.page("Tax Report", Pages.getTaxReportPane()));
        map.put("USER_SALES_REPORT", NavTree.NavTreeItem.page("User Sales Report", Pages.getUserSalesReportPane()));
        // HUMAN RESOURCE
        // Attendance
        map.put("ATTENDANCE", NavTree.NavTreeItem.page("Attendance", Pages.getAttendancePane()));
        map.put("ATTENDANCE_REPORT", NavTree.NavTreeItem.page("Attendance Report", Pages.getAttendanceReportPane()));
        // Human Resource Management
        map.put("DESIGNATION", NavTree.NavTreeItem.page("Designation", Pages.getDesignationsPane()));
        map.put("EMPLOYEES", NavTree.NavTreeItem.page("Employees", Pages.getEmployeesPane()));
        map.put("EMPLOYMENT_STATUS", NavTree.NavTreeItem.page("Employment Statuses", Pages.getEmploymentStatusPane()));
        // Leave
        map.put("LEAVE_STATUS", NavTree.NavTreeItem.page("Leave Status", Pages.getLeaveStatusPane()));
        map.put("LEAVE_REQUEST", NavTree.NavTreeItem.page("Leave Request", Pages.getEmployeesPane()));
        map.put("CALENDAR", NavTree.NavTreeItem.page("Calendar", Pages.getEmploymentStatusPane()));
        // PayRoll
        map.put("SALARIES", NavTree.NavTreeItem.page("Salaries", Pages.getSalariesPane()));
        map.put("SALARY_ADVANCES", NavTree.NavTreeItem.page("Salary Advances", Pages.getSalaryAdvancesPane()));
        map.put("SALARY_GENERATE", NavTree.NavTreeItem.page("Salary Generate", Pages.getSalaryGeneratePane()));
        // Service
        map.put("SERVICES", NavTree.NavTreeItem.page("Services", Pages.getServicesPane()));
        map.put("SERVICE_INVOICE", NavTree.NavTreeItem.page("Service Invoices", Pages.getServiceInvoicesPane()));
        // Tax
        map.put("TAXES", NavTree.NavTreeItem.page("Taxes", Pages.getTaxesPane()));
        map.put("TAX_SETTINGS", NavTree.NavTreeItem.page("Tax Settings", Pages.getTaxSettingsPane()));
        // Returns
        map.put("SALE_RETURN", NavTree.NavTreeItem.page("Sales Returns", Pages.getSaleReturnPane()));
        map.put("PURCHASE_RETURN", NavTree.NavTreeItem.page("Purchases Returns", Pages.getPurchaseReturnPane()));
        // Expenses
        map.put(
                "EXPENSE_CATEGORY", NavTree.NavTreeItem.page("Category", Pages.getExpenseCategoryPane()));
        map.put("EXPENSE", NavTree.NavTreeItem.page("Expenses", Pages.getExpensePane()));
        // SETTINGS
        // Data Synchronizer
        map.put("BACK_UP", NavTree.NavTreeItem.page("Back up", Pages.getBackUpPane()));
        map.put("EXPORT", NavTree.NavTreeItem.page("Export", Pages.getExportPane()));
        map.put("IMPORT", NavTree.NavTreeItem.page("Import", Pages.getImportPane()));
        map.put("RESTORE", NavTree.NavTreeItem.page("Restore", Pages.getRestorePane()));
        // Role Permission
        map.put("ASSIGN_USER_ROLE", NavTree.NavTreeItem.page("Users & roles", Pages.getAssignUserRolePane()));
        map.put("ROLES", NavTree.NavTreeItem.page("Roles", Pages.getRolesPane()));
        // System Settings
        map.put("APP_SETTINGS", NavTree.NavTreeItem.page("App Settings", Pages.getAppSettingsPane()));
        map.put("BRANCHES", NavTree.NavTreeItem.page("Branches", Pages.getBranchesPane()));
        map.put("COMPANY", NavTree.NavTreeItem.page("Company Details", Pages.getCompanySettingsPane()));
        map.put("CURRENCIES", NavTree.NavTreeItem.page("Currencies", Pages.getCurrencyPane()));
        map.put("LANGUAGES", NavTree.NavTreeItem.page("Languages & Input", Pages.getLanguagesPane()));
        map.put("MAIL", NavTree.NavTreeItem.page("Mail", Pages.getMailSettingsPane()));
        map.put("POS", NavTree.NavTreeItem.page("POS", Pages.getPosPane()));
        map.put("PRINT", NavTree.NavTreeItem.page("Print", Pages.getPrintSettingsPane()));
        map.put("SETTINGS", NavTree.NavTreeItem.page("Settings", Pages.getSettingsPane()));
        map.put("SYSTEM", NavTree.NavTreeItem.page("System", Pages.getSystemPane()));

        return map;
    }

    public static void navigate(Pane view, Pane viewWindow) {
        loadIntoView(view, viewWindow);
    }

    private static void loadIntoView(Pane view, Pane viewWindow) {
        try {
            final Pane prevWindow =
                    (Pane)
                            viewWindow.getChildren().stream()
                                    .filter(c -> c instanceof Pane)
                                    .findFirst()
                                    .orElse(null);
            final Pane nextWindow = view;
            Pane existingNextChild =
                    (Pane)
                            viewWindow.getChildren().stream().findAny().filter(c -> c.equals(view)).orElse(null);

            if (Objects.equals(prevWindow, nextWindow)) return;

            if (viewWindow.getChildren().isEmpty()) {
                viewWindow.getChildren().add(nextWindow);
                return;
            } else if (!Objects.equals(existingNextChild, null)) {
                var transition =
                        new FadeTransition(Duration.millis(PAGE_TRANSITION_DURATION), existingNextChild);
                transition.setFromValue(0.0);
                transition.setToValue(1.0);
                transition.setOnFinished(
                        event -> viewWindow.getChildren().get((int) existingNextChild.getViewOrder()).toFront());
                transition.play();
                return;
            }

            Objects.requireNonNull(prevWindow);

            viewWindow.getChildren().add(nextWindow);
            viewWindow.getChildren().remove(prevWindow);
            var transition = new FadeTransition(Duration.millis(PAGE_TRANSITION_DURATION), nextWindow);
            transition.setFromValue(0.0);
            transition.setToValue(1.0);
            transition.setOnFinished(event -> nextWindow.toFront());
            transition.play();
        } catch (RuntimeException e) {
            SpotyLogger.writeToFile(e, Navigation.class);
        }
    }

    public ReadOnlyObjectProperty<NavTree.NavTreeItem> navTreeProperty() {
        return navTree.getReadOnlyProperty();
    }

    public TreeView<Nav> createNavigation() {
        return new NavTree(this);
    }

    private NavTree.@NotNull NavTreeItem createTree() {
        var dashboard =
                NavTree.NavTreeItem.mainPage("Dashboard", "fas-chart-simple", Pages.getDashboardPane());

        var sale = NavTree.NavTreeItem.group("Sale", "fas-scale-balanced");
        sale
                .getChildren()
                .setAll(
                        NAV_TREE.get("SALE"),
                        NAV_TREE.get("POINT_OF_SALE"),
                        NAV_TREE.get("SALE_TERMS"));

        var customers =
                NavTree.NavTreeItem.mainPage("Customers", "fas-users", Pages.getCustomerPane());

        var suppliers =
                NavTree.NavTreeItem.mainPage("Suppliers", "fas-users-line", Pages.getSupplierPane());

        var product = NavTree.NavTreeItem.group("Product", "fas-cubes");
        product
                .getChildren()
                .setAll(
                        NAV_TREE.get("CATEGORY"),
                        NAV_TREE.get("BRAND"),
                        NAV_TREE.get("UNIT"),
                        NAV_TREE.get("PRODUCTS"),
                        NAV_TREE.get("ADJUSTMENTS"));

        var purchase =
                NavTree.NavTreeItem.mainPage("Purchases", "fas-cart-plus", Pages.getPurchasePane());

        var stockReport =
                NavTree.NavTreeItem.mainPage("Stock Report", "fas-chart-line", Pages.getStockReportPane());

        var account = NavTree.NavTreeItem.group("Account", "fas-credit-card");
        var accountReports = NavTree.NavTreeItem.group("Reports");
        accountReports
                .getChildren()
                .setAll(
                        NAV_TREE.get("CASH_BOOK"),
                        NAV_TREE.get("BANK_BOOK"),
                        NAV_TREE.get("DAY_BOOK"),
                        NAV_TREE.get("GENERAL_LEDGER"),
                        NAV_TREE.get("SUB_LEDGER"),
                        NAV_TREE.get("TRIAL_BALANCE"),
                        NAV_TREE.get("INCOME_STATEMENT"),
                        NAV_TREE.get("EXPENDITURE_STATEMENT"),
                        NAV_TREE.get("PROFIT_LOSS"),
                        NAV_TREE.get("BALANCE_SHEET"),
                        NAV_TREE.get("FIXED_ASSET_SCHEDULE"),
                        NAV_TREE.get("BANK_RECONCILIATION_REPORT"),
                        NAV_TREE.get("COA_PRINT"));
        account
                .getChildren()
                .setAll(
                        NAV_TREE.get("CHART_OF_ACCOUNT"),
                        NAV_TREE.get("SUB_ACCOUNT"),
                        NAV_TREE.get("PREDEFINED_ACCOUNTS"),
                        NAV_TREE.get("FINANCIAL_YEAR"),
                        NAV_TREE.get("OPENING_BALANCE"),
                        NAV_TREE.get("DEBIT_VOUCHER"),
                        NAV_TREE.get("CREDIT_VOUCHER"),
                        NAV_TREE.get("CONTRA_VOUCHER"),
                        NAV_TREE.get("JOURNAL_VOUCHER"),
                        NAV_TREE.get("BANK_RECONCILIATION"),
                        NAV_TREE.get("PAYMENT_METHODS"),
                        NAV_TREE.get("SUPPLIER_PAYMENT"),
                        NAV_TREE.get("CUSTOMER_RECEIVE"),
                        NAV_TREE.get("SERVICE_PAYMENT"),
                        NAV_TREE.get("CASH_ADJUSTMENT"),
                        NAV_TREE.get("VOUCHER_APPROVAL"),
                        accountReports);

        var reports = NavTree.NavTreeItem.group("Reports", "fas-clipboard-list");
        reports
                .getChildren()
                .setAll(
                        NAV_TREE.get("CLOSING"),
                        NAV_TREE.get("CLOSING_REPORT"),
                        NAV_TREE.get("DAILY_CUSTOMER_REPORT"),
                        NAV_TREE.get("DAILY_REPORT"),
                        NAV_TREE.get("DUE_REPORT"),
                        NAV_TREE.get("PROFIT_REPORT"),
                        NAV_TREE.get("PURCHASE_REPORT"),
                        NAV_TREE.get("SALES_REPORT"),
                        NAV_TREE.get("SALES_RETURN"),
                        NAV_TREE.get("SHIPPING_COST_REPORT"),
                        NAV_TREE.get("TAX_REPORT"),
                        NAV_TREE.get("USER_SALES_REPORT"));

        var attendance = NavTree.NavTreeItem.group("Attendance");
        attendance
                .getChildren()
                .setAll(
                        NAV_TREE.get("ATTENDANCE"),
                        NAV_TREE.get("ATTENDANCE_REPORT"));

        var humanResourceManagement = NavTree.NavTreeItem.group("HRM");
        humanResourceManagement
                .getChildren()
                .setAll(
                        NAV_TREE.get("DESIGNATION"),
                        NAV_TREE.get("EMPLOYEES"),
                        NAV_TREE.get("EMPLOYMENT_STATUS"));

        var leave = NavTree.NavTreeItem.group("Leave");
        leave
                .getChildren()
                .setAll(
                        NAV_TREE.get("LEAVE_STATUS"),
                        NAV_TREE.get("LEAVE_REQUEST"),
                        NAV_TREE.get("CALENDAR"));

        var payRoll = NavTree.NavTreeItem.group("PayRoll");
        payRoll
                .getChildren()
                .setAll(
                        NAV_TREE.get("SALARIES"),
                        NAV_TREE.get("SALARY_ADVANCES"),
                        NAV_TREE.get("SALARY_GENERATE"));
        var humanResource = NavTree.NavTreeItem.group("Human Resource", "fas-user-tie");
        humanResource
                .getChildren()
                .setAll(humanResourceManagement,
                        leave,
                        attendance,
                        payRoll);

        var bank =
                NavTree.NavTreeItem.mainPage(
                        "Banks", "fas-building-columns", Pages.getBankPane());

        var service = NavTree.NavTreeItem.group("Service", "fas-handshake");
        service
                .getChildren()
                .setAll(
                        NAV_TREE.get("SERVICES"),
                        NAV_TREE.get("SERVICE_INVOICE"));

        var quotation = NavTree.NavTreeItem.mainPage("Quotations", "fas-receipt", Pages.getQuotationPane());

        var tax = NavTree.NavTreeItem.group("Tax", "fas-money-bill-wheat");
        tax.getChildren()
                .setAll(
                        NAV_TREE.get("TAXES"),
                        NAV_TREE.get("TAX_SETTINGS"));

        var returns = NavTree.NavTreeItem.group("Return", "fas-retweet");
        returns.getChildren().setAll(NAV_TREE.get("SALE_RETURN"), NAV_TREE.get("PURCHASE_RETURN"));

        var requisition =
                NavTree.NavTreeItem.mainPage(
                        "Requisitions", "fas-hand-holding", Pages.getRequisitionPane());
        var transfer =
                NavTree.NavTreeItem.mainPage(
                        "Transfers", "fas-arrow-right-arrow-left", Pages.getTransferPane());
        var stockIn =
                NavTree.NavTreeItem.mainPage("Stock In", "fas-cart-arrow-down", Pages.getStockInPane());

        var expense = NavTree.NavTreeItem.group("Expenses", "fas-wallet");
        expense.getChildren().setAll(NAV_TREE.get("EXPENSE_CATEGORY"), NAV_TREE.get("EXPENSE"));

        var dataSynchronizer = NavTree.NavTreeItem.group("Data Synchronizer");
        dataSynchronizer
                .getChildren()
                .setAll(
                        NAV_TREE.get("BACK_UP"),
                        NAV_TREE.get("EXPORT"),
                        NAV_TREE.get("IMPORT"),
                        NAV_TREE.get("RESTORE"));
        var rolePermission = NavTree.NavTreeItem.group("Role Permission");
        rolePermission
                .getChildren()
                .setAll(
                        NAV_TREE.get("ASSIGN_USER_ROLE"),
                        NAV_TREE.get("ROLES"));
        var systemSettings = NavTree.NavTreeItem.group("System Settings");
        systemSettings
                .getChildren()
                .setAll(
                        NAV_TREE.get("APP_SETTINGS"),
                        NAV_TREE.get("BRANCHES"),
                        NAV_TREE.get("COMPANY"),
                        NAV_TREE.get("CURRENCIES"),
                        NAV_TREE.get("LANGUAGES"),
                        NAV_TREE.get("MAIL"),
                        NAV_TREE.get("POS"),
                        NAV_TREE.get("PRINT"),
                        NAV_TREE.get("SETTINGS"),
                        NAV_TREE.get("SYSTEM"));
        var settings = NavTree.NavTreeItem.group("Settings", "fas-gears");
        settings
                .getChildren()
                .setAll(
                        systemSettings,
                        rolePermission,
                        dataSynchronizer);

        var root = NavTree.NavTreeItem.root();
        root.getChildren()
                .addAll(
                        dashboard,
                        sale,
                        customers,
                        suppliers,
                        product,
                        purchase,
                        stockReport,
                        account,
                        reports,
                        humanResource,
                        bank,
                        service,
                        quotation,
                        tax,
                        returns,
                        requisition,
                        transfer,
                        stockIn,
                        expense,
                        settings);
        return root;
    }

    public void navigate(BorderPane view) {
        loadView(view);
    }

    private void loadView(BorderPane view) {
        try {
            final BorderPane prevWindow =
                    (BorderPane)
                            viewWindow.getChildren().stream()
                                    .filter(c -> c instanceof BorderPane)
                                    .findFirst()
                                    .orElse(null);
            final BorderPane nextWindow = view;
            BorderPane existingNextChild =
                    (BorderPane)
                            viewWindow.getChildren().stream().findAny().filter(c -> c.equals(view)).orElse(null);

            if (Objects.equals(prevWindow, nextWindow)) return;

            if (viewWindow.getChildren().isEmpty()) {
                viewWindow.getChildren().add(nextWindow);
                return;
            } else if (!Objects.equals(existingNextChild, null)) {
                var transition =
                        new FadeTransition(Duration.millis(PAGE_TRANSITION_DURATION), existingNextChild);
                transition.setFromValue(0.0);
                transition.setToValue(1.0);
                transition.setOnFinished(
                        t -> viewWindow.getChildren().get((int) existingNextChild.getViewOrder()).toFront());
                transition.play();
                return;
            }

            Objects.requireNonNull(prevWindow);

            viewWindow.getChildren().add(nextWindow);
            viewWindow.getChildren().remove(prevWindow);
            var transition = new FadeTransition(Duration.millis(PAGE_TRANSITION_DURATION), nextWindow);
            transition.setFromValue(0.0);
            transition.setToValue(1.0);
            transition.setOnFinished(t -> nextWindow.toFront());
            transition.play();
        } catch (RuntimeException e) {
            SpotyLogger.writeToFile(e, Navigation.class);
        }
    }
}
