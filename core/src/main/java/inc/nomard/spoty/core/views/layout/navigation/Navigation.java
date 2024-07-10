package inc.nomard.spoty.core.views.layout.navigation;

import inc.nomard.spoty.core.views.dashboard.*;
import static inc.nomard.spoty.core.views.layout.navigation.Navigation.SubLayer.*;
import inc.nomard.spoty.core.views.pages.*;
import inc.nomard.spoty.core.views.pos.*;
import inc.nomard.spoty.core.views.settings.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.utils.flavouring.*;
import java.util.*;
import javafx.beans.property.*;
import javafx.scene.control.*;
import lombok.extern.java.*;

@Log
public class Navigation {
    public static final Class<? extends Page> DEFAULT_PAGE = DashboardPage.class;
    private static final Map<String, NavTree.NavTreeItem> NAV_TREE = createNavItems();
    private final AppFlavor flavor = AppConfig.getActiveFlavor();
    private final ReadOnlyObjectWrapper<Class<? extends Page>> selectedPage = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyObjectWrapper<SubLayer> currentSubLayer = new ReadOnlyObjectWrapper<>(PAGE);
    private final ReadOnlyObjectWrapper<NavTree.NavTreeItem> navTree = new ReadOnlyObjectWrapper<>(createTree());

    public static Map<String, NavTree.NavTreeItem> createNavItems() {
        var map = new HashMap<String, NavTree.NavTreeItem>();
        // People
        map.put("SUPPLIERS", NavTree.NavTreeItem.page("Suppliers", SupplierPage.class));
        map.put("CUSTOMERS", NavTree.NavTreeItem.page("Customers", CustomerPage.class));
        // Deductions
        map.put("TAXES", NavTree.NavTreeItem.page("Taxes", TaxPage.class));
        map.put("DISCOUNTS", NavTree.NavTreeItem.page("Discounts", DiscountPage.class));
        // Sales
        map.put("POINT_OF_SALE", NavTree.NavTreeItem.page("Point Of Sale", PointOfSalePage.class));
        map.put("ORDERS", NavTree.NavTreeItem.page("Orders", OrderPage.class));
        map.put("SALE_RETURN", NavTree.NavTreeItem.page("Sales Returns", SaleReturnPage.class));
        map.put("SALE_TERMS", NavTree.NavTreeItem.page("Sale Terms", SaleTermPage.class));       // Inventory
        map.put("CATEGORY", NavTree.NavTreeItem.page("Category", ProductCategoryPage.class));
        map.put("BRAND", NavTree.NavTreeItem.page("Brand", BrandPage.class));
        map.put("UNIT", NavTree.NavTreeItem.page("Unit", UnitOfMeasurePage.class));
        map.put("PRODUCTS", NavTree.NavTreeItem.page("Products", ProductPage.class));
        map.put("REQUISITIONS", NavTree.NavTreeItem.page("Requisitions", RequisitionPage.class));
        map.put("STOCK_INS", NavTree.NavTreeItem.page("Stock Ins", StockInPage.class));
        map.put("TRANSFERS", NavTree.NavTreeItem.page("Transfers", TransferPage.class));
        map.put("ADJUSTMENTS", NavTree.NavTreeItem.page("Adjustments", AdjustmentPage.class));
        // Accounting
        map.put("ACCOUNTS", NavTree.NavTreeItem.page("Accounts", AccountPage.class));
        map.put(
                "EXPENSE_CATEGORY", NavTree.NavTreeItem.page("Expense Category", ExpenseCategoryPage.class));
        map.put("EXPENSE", NavTree.NavTreeItem.page("Expenses", ExpensePage.class));
        map.put("TRANSACTIONS", NavTree.NavTreeItem.page("Transactions", AccountTransactionPage.class));
        // HUMAN RESOURCE
        // HRM
        map.put("DESIGNATION", NavTree.NavTreeItem.page("Designation", DesignationPage.class));
        map.put("EMPLOYEES", NavTree.NavTreeItem.page("Employees", EmployeePage.class));
        map.put("EMPLOYMENT_STATUS", NavTree.NavTreeItem.page("Employment Statuses", EmploymentStatusPage.class));
        // Leave
        map.put("LEAVE_REQUEST", NavTree.NavTreeItem.page("Leave Requests", LeaveRequestPage.class));
        map.put("CALENDAR", NavTree.NavTreeItem.page("Calendar", CalendarPage.class));       // PayRoll
        map.put("PAY_SLIPS", NavTree.NavTreeItem.page("Pay Slips", PaySlipPage.class));
        map.put("BENEFICIARY_BADGE", NavTree.NavTreeItem.page("Beneficiary Badge", BeneficiaryBadgePage.class));
        map.put("BENEFICIARY_TYPE", NavTree.NavTreeItem.page("Beneficiary Type", BeneficiaryTypePage.class));
        // Purchases
        map.put("PURCHASES", NavTree.NavTreeItem.page("Purchases", PurchasePage.class));
        map.put("PURCHASE_RETURNS", NavTree.NavTreeItem.page("Purchases Returns", PurchaseReturnPage.class));
        // SETTINGS
        map.put("APP_SETTINGS", NavTree.NavTreeItem.page("App Settings", AppSettingPage.class));
        map.put("BRANCHES", NavTree.NavTreeItem.page("Branches", BranchPage.class));
        map.put("COMPANY", NavTree.NavTreeItem.page("Company Details", CompanyDetailPage.class));
        map.put("CURRENCIES", NavTree.NavTreeItem.page("Currencies", CurrencyPage.class));
        map.put("ROLES", NavTree.NavTreeItem.page("Roles", RolePage.class));

        return map;
    }

    NavTree.NavTreeItem getTreeItemForPage(Class<? extends Page> pageClass) {
        return NAV_TREE.getOrDefault(pageClass, NAV_TREE.get(DEFAULT_PAGE));
    }

    List<NavTree.NavTreeItem> findPages(String filter) {
        return NAV_TREE.values().stream()
                .filter(item -> item.getValue() != null && item.getValue().matches(filter))
                .toList();
    }

    public TreeView<Nav> createNavigation() {
        return new NavTree(this);
    }

    public ReadOnlyObjectProperty<Class<? extends Page>> selectedPageProperty() {
        return selectedPage.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<SubLayer> currentSubLayerProperty() {
        return currentSubLayer.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<NavTree.NavTreeItem> navTreeProperty() {
        return navTree.getReadOnlyProperty();
    }

    private NavTree.NavTreeItem createTree() {
        var dashboard =
                NavTree.NavTreeItem.mainPage("Dashboard", "fas-chart-simple", DashboardPage.class);

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
            purchase = NavTree.NavTreeItem.page("Purchases", PurchasePage.class);
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

        var quotation = NavTree.NavTreeItem.mainPage("Quotations", "fas-receipt", QuotationPage.class);

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
                            deductions,
                            inventory,
                            quotation,
                            sale,
                            accounts,
                            settings);
        } else if (flavor == AppFlavor.TRACTION) {
            root.getChildren()
                    .addAll(dashboard,
                            humanResource,
                            people,
                            deductions,
                            inventory,
                            quotation,
                            sale,
                            accounts,
                            settings);
        } else if (flavor == AppFlavor.MVP) {
            root.getChildren()
                    .addAll(
                            people,
                            deductions,
                            inventory,
                            quotation,
                            sale);
        }
        return root;
    }

    public void navigate(Class<? extends Page> page) {
        selectedPage.set(Objects.requireNonNull(page));
        currentSubLayer.set(PAGE);
    }

    public enum SubLayer {
        PAGE,
        SOURCE_CODE
    }
}
