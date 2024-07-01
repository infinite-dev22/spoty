package inc.nomard.spoty.core.components.navigation;

import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.core.views.dashboard.*;
import inc.nomard.spoty.core.views.pos.*;
import inc.nomard.spoty.core.views.settings.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.flavouring.*;
import java.util.*;
import javafx.animation.*;
import javafx.beans.property.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class Navigation {
    static final int PAGE_TRANSITION_DURATION = 300;
    private static StackPane viewWindow;
    private static Navigation instance;
    private static Stage stage = null;
    private static final Map<String, NavTree.NavTreeItem> NAV_TREE = createNavItems();
    private final AppFlavor flavor = AppConfig.getActiveFlavor();
    private final ReadOnlyObjectWrapper<NavTree.NavTreeItem> navTree =
            new ReadOnlyObjectWrapper<>(createTree());

    private Navigation(Stage stage, StackPane viewWindow) {
        Navigation.stage = stage;
        Navigation.viewWindow = viewWindow;
    }

    public static Navigation getInstance(Stage stage, StackPane viewWindow) {
        if (instance == null) {
            instance = new Navigation(stage, viewWindow);
            instance.loadView(new DashboardPage());
        }
        return instance;
    }

    public static Map<String, NavTree.NavTreeItem> createNavItems() {
        var map = new HashMap<String, NavTree.NavTreeItem>();
        // People
        map.put("SUPPLIERS", NavTree.NavTreeItem.page("Suppliers", new SupplierPage(stage)));
        map.put("CUSTOMERS", NavTree.NavTreeItem.page("Customers", new CustomerPage(stage)));
        // Deductions
        map.put("TAXES", NavTree.NavTreeItem.page("Taxes", new TaxPage(stage)));
        map.put("DISCOUNTS", NavTree.NavTreeItem.page("Discounts", new DiscountPage(stage)));
        // Sales
        map.put("POINT_OF_SALE", NavTree.NavTreeItem.page("Point Of Sale", new PointOfSalePage(stage)));
        map.put("ORDERS", NavTree.NavTreeItem.page("Orders", new OrderPage(stage)));
        map.put("SALE_RETURN", NavTree.NavTreeItem.page("Sales Returns", new SaleReturnPage(stage)));
        map.put("SALE_TERMS", NavTree.NavTreeItem.page("Sale Terms", new SaleTermPage()));
        // Inventory
        map.put("CATEGORY", NavTree.NavTreeItem.page("Category", new ProductCategoryPage(stage)));
        map.put("BRAND", NavTree.NavTreeItem.page("Brand", new BrandPage(stage)));
        map.put("UNIT", NavTree.NavTreeItem.page("Unit", new UnitOfMeasurePage(stage)));
        map.put("PRODUCTS", NavTree.NavTreeItem.page("Products", new ProductPage(stage)));
        map.put("REQUISITIONS", NavTree.NavTreeItem.page("Requisitions", new RequisitionPage(stage)));
        map.put("STOCK_INS", NavTree.NavTreeItem.page("Stock Ins", new StockInPage(stage)));
        map.put("TRANSFERS", NavTree.NavTreeItem.page("Transfers", new TransferPage(stage)));
        map.put("ADJUSTMENTS", NavTree.NavTreeItem.page("Adjustments", new AdjustmentPage(stage)));
        // Accounting
        map.put("ACCOUNTS", NavTree.NavTreeItem.page("Accounts", new AccountPage(Navigation.stage)));
        map.put(
                "EXPENSE_CATEGORY", NavTree.NavTreeItem.page("Expense Category", new ExpenseCategoryPage(stage)));
        map.put("EXPENSE", NavTree.NavTreeItem.page("Expenses", new ExpensePage(stage)));
        map.put("TRANSACTIONS", NavTree.NavTreeItem.page("Transactions", new AccountTransactionPage(stage)));
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
        map.put("SHIPPING_COST_REPORT", NavTree.NavTreeItem.page("Shipping Cost Report", Pages.getShippingCostReportPane()));
        map.put("TAX_REPORT", NavTree.NavTreeItem.page("Tax Report", Pages.getTaxReportPane()));
        map.put("USER_SALES_REPORT", NavTree.NavTreeItem.page("User Sales Report", Pages.getUserSalesReportPane()));
        // HUMAN RESOURCE
        // HRM
        map.put("DESIGNATION", NavTree.NavTreeItem.page("Designation", new DesignationPage(stage)));
        map.put("EMPLOYEES", NavTree.NavTreeItem.page("Employees", new EmployeePage(stage)));
        map.put("EMPLOYMENT_STATUS", NavTree.NavTreeItem.page("Employment Statuses", new EmploymentStatusPage(stage)));
        // Leave
        map.put("LEAVE_REQUEST", NavTree.NavTreeItem.page("Leave Requests", new LeaveRequestPage(stage)));
        map.put("CALENDAR", NavTree.NavTreeItem.page("Calendar", new CalendarPage()));
        // PayRoll
        map.put("PAY_SLIPS", NavTree.NavTreeItem.page("Pay Slips", new PaySlipPage(stage)));
        map.put("BENEFICIARY_BADGE", NavTree.NavTreeItem.page("Beneficiary Badge", new BeneficiaryBadgePage(stage)));
        map.put("BENEFICIARY_TYPE", NavTree.NavTreeItem.page("Beneficiary Type", new BeneficiaryTypePage(stage)));
        // Purchases
        map.put("PURCHASES", NavTree.NavTreeItem.page("Purchases", new PurchasePage(stage)));
        map.put("PURCHASE_RETURNS", NavTree.NavTreeItem.page("Purchases Returns", new PurchaseReturnPage(stage)));
        // SETTINGS
        map.put("APP_SETTINGS", NavTree.NavTreeItem.page("App Settings", new AppSettingPage(stage)));
        map.put("BRANCHES", NavTree.NavTreeItem.page("Branches", new BranchPage(stage)));
        map.put("COMPANY", NavTree.NavTreeItem.page("Company Details", new CompanyDetailPage()));
        map.put("CURRENCIES", NavTree.NavTreeItem.page("Currencies", new CurrencyPage(stage)));
        map.put("ROLES", NavTree.NavTreeItem.page("Roles", new RolePage(stage)));

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
                NavTree.NavTreeItem.mainPage("Dashboard", "fas-chart-simple", new DashboardPage());

        NavTree.NavTreeItem sale;
        if (flavor == AppFlavor.TRACTION) {
            sale = NavTree.NavTreeItem.group("Sale", "fas-scale-balanced");
            sale
                    .getChildren()
                    .setAll(
                            NAV_TREE.get("POINT_OF_SALE"),
                            NAV_TREE.get("ORDERS"),
                            NAV_TREE.get("SALE_RETURN"),
                            NAV_TREE.get("SALE_TERMS"));
        } else {
            sale = NavTree.NavTreeItem.group("Sale", "fas-scale-balanced");
            sale
                    .getChildren()
                    .setAll(
                            NAV_TREE.get("POINT_OF_SALE"),
                            NAV_TREE.get("ORDERS"));
        }

        var people = NavTree.NavTreeItem.group("People", "fas-users");
        people
                .getChildren()
                .setAll(
                        NAV_TREE.get("SUPPLIERS"),
                        NAV_TREE.get("CUSTOMERS"));

        NavTree.NavTreeItem purchase;
        if (flavor == AppFlavor.TRACTION) {
            purchase = NavTree.NavTreeItem.group("Purchase");
            purchase
                    .getChildren()
                    .setAll(
                            NAV_TREE.get("PURCHASES"),
                            NAV_TREE.get("PURCHASE_RETURNS"));
        } else {
            purchase = NavTree.NavTreeItem.page("Purchases", new PurchasePage(stage));
        }

        var inventory = NavTree.NavTreeItem.group("Inventory", "fas-cubes");
        if (flavor == AppFlavor.TRACTION) {
            inventory
                    .getChildren()
                    .setAll(
                            NAV_TREE.get("CATEGORY"),
                            NAV_TREE.get("BRAND"),
                            NAV_TREE.get("UNIT"),
                            NAV_TREE.get("PRODUCTS"),
                            NAV_TREE.get("REQUISITIONS"),
                            purchase,
                            NAV_TREE.get("STOCK_INS"),
                            NAV_TREE.get("TRANSFERS"),
                            NAV_TREE.get("ADJUSTMENTS"));
        } else {
            inventory
                    .getChildren()
                    .setAll(
                            NAV_TREE.get("CATEGORY"),
                            NAV_TREE.get("BRAND"),
                            NAV_TREE.get("UNIT"),
                            NAV_TREE.get("PRODUCTS"),
                            purchase,
                            NAV_TREE.get("STOCK_INS"),
                            NAV_TREE.get("ADJUSTMENTS"));
        }

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
                        NAV_TREE.get("BENEFICIARY_TYPE"),
                        NAV_TREE.get("BENEFICIARY_BADGE"));

        var humanResource = NavTree.NavTreeItem.group("Human Resource", "fas-user-tie");
        if (flavor == AppFlavor.TRACTION) {
            humanResource
                    .getChildren()
                    .setAll(humanResourceManagement,
                            leave,
                            payRoll);
        } else {
            humanResource
                    .getChildren()
                    .setAll(
                            NAV_TREE.get("DESIGNATION"),
                            NAV_TREE.get("EMPLOYMENT_STATUS"),
                            NAV_TREE.get("EMPLOYEES"));
        }

        var accounts = NavTree.NavTreeItem.group("Accounting", "fas-coins");
        accounts.getChildren().setAll(
                NAV_TREE.get("ACCOUNTS"),
                NAV_TREE.get("EXPENSE_CATEGORY"),
                NAV_TREE.get("EXPENSE"),
                NAV_TREE.get("TRANSACTIONS"));

        var quotation = NavTree.NavTreeItem.mainPage("Quotations", "fas-receipt", new QuotationPage(stage));

        var deductions = NavTree.NavTreeItem.group("Deductions", "fas-coins");
        deductions.getChildren().setAll(
                NAV_TREE.get("TAXES"),
                NAV_TREE.get("DISCOUNTS"));

        var settings = NavTree.NavTreeItem.group("Settings", "fas-gears");

        if (flavor == AppFlavor.TRACTION) {
            settings
                    .getChildren()
                    .setAll(
                            NAV_TREE.get("BRANCHES"),
                            NAV_TREE.get("CURRENCIES"),
                            NAV_TREE.get("APP_SETTINGS"),
                            NAV_TREE.get("COMPANY"),
                            NAV_TREE.get("ROLES"));
        } else {
            settings
                    .getChildren()
                    .setAll(
                            NAV_TREE.get("APP_SETTINGS"),
                            NAV_TREE.get("COMPANY"));
        }

        var root = NavTree.NavTreeItem.root();
        if (flavor == AppFlavor.PROD) {
            root.getChildren()
                    .addAll(
                            dashboard,
                            people,
                            deductions,
                            inventory,
                            quotation,
                            sale,
                            accounts);
        } else if (flavor == AppFlavor.DEV) {
            root.getChildren()
                    .addAll(
                            dashboard,
                            humanResource,
                            people,
                            inventory,
                            quotation,
                            sale,
                            accounts,
                            reports,
                            settings);
        } else if (flavor == AppFlavor.TRACTION) {
            root.getChildren()
                    .addAll(dashboard,
                            humanResource,
                            people,
                            inventory,
                            quotation,
                            sale,
                            accounts,
                            reports,
                            settings);
        } else if (flavor == AppFlavor.MVP) {
            root.getChildren()
                    .addAll(
                            people,
                            inventory,
                            quotation,
                            sale);
        }
        return root;
    }

    public void navigate(Pane view) {
        loadView(view);
    }

    private void loadView(Pane view) {
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
