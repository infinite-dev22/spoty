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

import inc.nomard.spoty.utils.*;
import java.util.*;
import javafx.animation.*;
import javafx.beans.property.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;

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

    public static Map<String, NavTree.NavTreeItem> createNavItems() {
        var map = new HashMap<String, NavTree.NavTreeItem>();
        // People
        map.put("SUPPLIERS", NavTree.NavTreeItem.page("Suppliers", Pages.getSupplierPane()));
        map.put("CUSTOMERS", NavTree.NavTreeItem.page("Customers", Pages.getCustomerPane()));
        // Sales
        map.put("POINT_OF_SALE", NavTree.NavTreeItem.page("Point Of Sale", Pages.getPosPane()));
        map.put("ORDERS", NavTree.NavTreeItem.page("Orders", Pages.getSalePane()));
        map.put("SALE_RETURN", NavTree.NavTreeItem.page("Sales Returns", Pages.getSaleReturnPane()));
        map.put("SALE_TERMS", NavTree.NavTreeItem.page("Sale Terms", Pages.getSalesTermPane()));
        // Inventory
        map.put("CATEGORY", NavTree.NavTreeItem.page("Category", Pages.getProductCategoryPane()));
        map.put("BRAND", NavTree.NavTreeItem.page("Brand", Pages.getBrandPane()));
        map.put("UNIT", NavTree.NavTreeItem.page("Unit", Pages.getUnitPane()));
        map.put("PRODUCTS", NavTree.NavTreeItem.page("Products", Pages.getProductPane()));
        map.put("REQUISITIONS", NavTree.NavTreeItem.page("Requisitions", Pages.getRequisitionPane()));
        map.put("STOCK_INS", NavTree.NavTreeItem.page("Stock Ins", Pages.getStockInPane()));
        map.put("TRANSFERS", NavTree.NavTreeItem.page("Transfers", Pages.getTransferPane()));
        map.put("ADJUSTMENTS", NavTree.NavTreeItem.page("Adjustments", Pages.getAdjustmentPane()));
        // Accounting
        map.put("BANK_ACCOUNTS", NavTree.NavTreeItem.page("Bank Accounts", Pages.getBankPane()));
        map.put(
                "EXPENSE_CATEGORY", NavTree.NavTreeItem.page("Expense Category", Pages.getExpenseCategoryPane()));
        map.put("EXPENSE", NavTree.NavTreeItem.page("Expenses", Pages.getExpensePane()));
        // Reports
        map.put("STOCK_REPORT", NavTree.NavTreeItem.page("Stock Report", Pages.getStockReportPane()));
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
        // HRM
        map.put("DESIGNATION", NavTree.NavTreeItem.page("Designation", Pages.getDesignationsPane()));
        map.put("EMPLOYEES", NavTree.NavTreeItem.page("Employees", Pages.getEmployeesPane()));
        map.put("EMPLOYMENT_STATUS", NavTree.NavTreeItem.page("Employment Statuses", Pages.getEmploymentStatusPane()));
        // Leave
        map.put("LEAVE_REQUEST", NavTree.NavTreeItem.page("Leave Requests", Pages.getLeaveRequestPane()));
        map.put("CALENDAR", NavTree.NavTreeItem.page("Calendar", Pages.getCalendarPane()));
        // PayRoll
        map.put("PAY_SLIPS", NavTree.NavTreeItem.page("Pay Slips", Pages.getPaySlipsPane()));
        map.put("BENEFICIARY_BADGE", NavTree.NavTreeItem.page("Beneficiary Badge", Pages.getBeneficiaryBadgePane()));
        map.put("BENEFICIARY_TYPE", NavTree.NavTreeItem.page("Beneficiary Type", Pages.getBeneficiaryTypePane()));
        // Purchases
        map.put("PURCHASES", NavTree.NavTreeItem.page("Purchases", Pages.getPurchasePane()));
        map.put("PURCHASE_RETURNS", NavTree.NavTreeItem.page("Purchases Returns", Pages.getPurchaseReturnPane()));
        // SETTINGS
        map.put("APP_SETTINGS", NavTree.NavTreeItem.page("App Settings", Pages.getAppSettingsPane()));
        map.put("BRANCHES", NavTree.NavTreeItem.page("Branches", Pages.getBranchesPane()));
        map.put("COMPANY", NavTree.NavTreeItem.page("Company Details", Pages.getCompanySettingsPane()));
        map.put("CURRENCIES", NavTree.NavTreeItem.page("Currencies", Pages.getCurrencyPane()));
        map.put("ROLES", NavTree.NavTreeItem.page("Roles", Pages.getRolesPane()));

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

    private NavTree.NavTreeItem createTree() {
        var dashboard =
                NavTree.NavTreeItem.mainPage("Dashboard", "fas-chart-simple", Pages.getDashboardPane());

        var sale = NavTree.NavTreeItem.group("Sale", "fas-scale-balanced");
        sale
                .getChildren()
                .setAll(
                        NAV_TREE.get("POINT_OF_SALE"),
                        NAV_TREE.get("ORDERS"),
                        NAV_TREE.get("SALE_RETURN"),
                        NAV_TREE.get("SALE_TERMS"));

        var people = NavTree.NavTreeItem.group("People", "fas-users");
        people
                .getChildren()
                .setAll(
                        NAV_TREE.get("SUPPLIERS"),
                        NAV_TREE.get("CUSTOMERS"));

        var purchase = NavTree.NavTreeItem.group("Purchase");
        purchase
                .getChildren()
                .setAll(
                        NAV_TREE.get("PURCHASES"),
                        NAV_TREE.get("PURCHASE_RETURNS"));

        var inventory = NavTree.NavTreeItem.group("Inventory", "fas-cubes");
        inventory
                .getChildren()
                .setAll(
                        NAV_TREE.get("CATEGORY"),
                        NAV_TREE.get("BRAND"),
                        NAV_TREE.get("UNIT"),
                        NAV_TREE.get("PRODUCTS"),
                        // NAV_TREE.get("REQUISITIONS"), Will be added if gains traction.
                        purchase,
                        NAV_TREE.get("STOCK_INS"),
                        NAV_TREE.get("TRANSFERS"),
                        NAV_TREE.get("ADJUSTMENTS"));

        var reports = NavTree.NavTreeItem.group("Reports", "fas-clipboard-list");
        reports
                .getChildren()
                .setAll(
                        NAV_TREE.get("STOCK_REPORT"),
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

        var humanResourceManagement = NavTree.NavTreeItem.group("HRM");
        humanResourceManagement
                .getChildren()
                .setAll(
                        NAV_TREE.get("DESIGNATION"),
                        NAV_TREE.get("EMPLOYMENT_STATUS"),
                        NAV_TREE.get("EMPLOYEES"));

        var leave = NavTree.NavTreeItem.group("Leave");
        leave
                .getChildren()
                .setAll(
                        NAV_TREE.get("LEAVE_REQUEST"),
                        NAV_TREE.get("CALENDAR"));

        var payRoll = NavTree.NavTreeItem.group("PayRoll");
        payRoll
                .getChildren()
                .setAll(
                        NAV_TREE.get("PAY_SLIPS"),
                        NAV_TREE.get("BENEFICIARY_BADGE"),
                        NAV_TREE.get("BENEFICIARY_TYPE"));
        var humanResource = NavTree.NavTreeItem.group("Human Resource", "fas-user-tie");
        humanResource
                .getChildren()
                .setAll(humanResourceManagement,
                        leave,
                        payRoll);

        var accounts = NavTree.NavTreeItem.group("Accounting", "fas-coins");
        accounts.getChildren().setAll(
                NAV_TREE.get("BANK_ACCOUNTS"),
                NAV_TREE.get("EXPENSE_CATEGORY"),
                NAV_TREE.get("EXPENSE"));

        var quotation = NavTree.NavTreeItem.mainPage("Quotations", "fas-receipt", Pages.getQuotationPane());

        var tax = NavTree.NavTreeItem.mainPage("Tax", "fas-money-bill-wheat", Pages.getTaxesPane());
        var settings = NavTree.NavTreeItem.group("Settings", "fas-gears");
        settings
                .getChildren()
                .setAll(
                        NAV_TREE.get("BRANCHES"),
                        NAV_TREE.get("CURRENCIES"),
                        NAV_TREE.get("APP_SETTINGS"),
                        NAV_TREE.get("COMPANY"),
                        NAV_TREE.get("ROLES"));

        var root = NavTree.NavTreeItem.root();
        root.getChildren()
                .addAll(
                        dashboard,
                        humanResource,
                        people,
                        inventory,
                        quotation,
                        sale,
                        accounts,
                        tax,
                        reports,
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
