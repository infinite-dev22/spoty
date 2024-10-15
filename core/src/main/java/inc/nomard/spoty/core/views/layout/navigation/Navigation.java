package inc.nomard.spoty.core.views.layout.navigation;

import inc.nomard.spoty.core.views.dashboard.DashboardPage;
import inc.nomard.spoty.core.views.pages.*;
import inc.nomard.spoty.core.views.pages.leave.LeaveMainPage;
import inc.nomard.spoty.core.views.pages.purchase.PurchaseMainPage;
import inc.nomard.spoty.core.views.pages.sale.SalesMainPage;
import inc.nomard.spoty.core.views.pos.PointOfSalePage;
import inc.nomard.spoty.core.views.settings.AppSettingPage;
import inc.nomard.spoty.core.views.settings.BranchPage;
import inc.nomard.spoty.core.views.settings.RolePage;
import inc.nomard.spoty.core.views.settings.tenant_settings.TenantSettingsPage;
import inc.nomard.spoty.core.views.util.Page;
import inc.nomard.spoty.utils.flavouring.AppConfig;
import inc.nomard.spoty.utils.flavouring.AppFlavor;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.kordamp.ikonli.fontawesome5.FontAwesomeSolid.*;

@Log4j2
public class Navigation {
    public static final Class<? extends Page> DEFAULT_PAGE = DashboardPage.class;
    private static final Map<Class<? extends Page>, NavTree.NavTreeItem> NAV_TREE = createNavItems();
    private final AppFlavor flavor = AppConfig.getActiveFlavor();
    private final ReadOnlyObjectWrapper<Class<? extends Page>> selectedPage = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyObjectWrapper<NavTree.NavTreeItem> navTree = new ReadOnlyObjectWrapper<>(createTree());

    private static Map<Class<? extends Page>, NavTree.NavTreeItem> createNavItems() {
        return Map.ofEntries(Map.entry(DepartmentPage.class, NavTree.NavTreeItem.page("Departments", DepartmentPage.class)),
                Map.entry(DesignationPage.class, NavTree.NavTreeItem.page("Designation", DesignationPage.class)),
                Map.entry(EmployeePage.class, NavTree.NavTreeItem.page("Employees", EmployeePage.class)),
                Map.entry(EmploymentStatusPage.class, NavTree.NavTreeItem.page("Employment Statuses", EmploymentStatusPage.class)),
                Map.entry(LeaveMainPage.class, NavTree.NavTreeItem.page("Leave", LeaveMainPage.class)),
                Map.entry(PaySlipPage.class, NavTree.NavTreeItem.page("PayRoll", PaySlipPage.class)),
                Map.entry(SalaryPage.class, NavTree.NavTreeItem.page("Salaries", SalaryPage.class)),
                Map.entry(SupplierPage.class, NavTree.NavTreeItem.page("Suppliers", SupplierPage.class)),
                Map.entry(CustomerPage.class, NavTree.NavTreeItem.page("Customers", CustomerPage.class)),
                Map.entry(TaxPage.class, NavTree.NavTreeItem.page("Taxes", TaxPage.class)),
                Map.entry(DiscountPage.class, NavTree.NavTreeItem.page("Discounts", DiscountPage.class)),
                Map.entry(ProductCategoryPage.class, NavTree.NavTreeItem.page("Category", ProductCategoryPage.class)),
                Map.entry(BrandPage.class, NavTree.NavTreeItem.page("Brand", BrandPage.class)),
                Map.entry(UnitOfMeasurePage.class, NavTree.NavTreeItem.page("Unit", UnitOfMeasurePage.class)),
                Map.entry(ProductPage.class, NavTree.NavTreeItem.page("Products", ProductPage.class)),
                Map.entry(RequisitionPage.class, NavTree.NavTreeItem.page("Requisitions", RequisitionPage.class)),
                Map.entry(PurchaseMainPage.class, NavTree.NavTreeItem.page("Purchases", PurchaseMainPage.class)),
                Map.entry(StockInPage.class, NavTree.NavTreeItem.page("Stock Ins", StockInPage.class)),
                Map.entry(AdjustmentPage.class, NavTree.NavTreeItem.page("Adjustments", AdjustmentPage.class)),
                Map.entry(TransferPage.class, NavTree.NavTreeItem.page("Transfers", TransferPage.class)),
                Map.entry(QuotationPage.class, NavTree.NavTreeItem.page("Quotations", QuotationPage.class)),
                Map.entry(PointOfSalePage.class, NavTree.NavTreeItem.page("Point Of Sale", PointOfSalePage.class)),
                Map.entry(SalesMainPage.class, NavTree.NavTreeItem.page("Orders", SalesMainPage.class)),
                Map.entry(AccountPage.class, NavTree.NavTreeItem.page("Accounts", AccountPage.class)),
                Map.entry(ExpensePage.class, NavTree.NavTreeItem.page("Expenses", ExpensePage.class)),
                Map.entry(AccountTransactionPage.class, NavTree.NavTreeItem.page("Transactions", AccountTransactionPage.class)),
                Map.entry(AppSettingPage.class, NavTree.NavTreeItem.page("App Settings", AppSettingPage.class)),
                Map.entry(BranchPage.class, NavTree.NavTreeItem.page("Branches", BranchPage.class)),
                Map.entry(RolePage.class, NavTree.NavTreeItem.page("Roles", RolePage.class)),
                Map.entry(TenantSettingsPage.class, NavTree.NavTreeItem.page("Company Details", TenantSettingsPage.class)));  // Immutable map
    }

    public NavTree createNavigation() {
        return new NavTree(this);
    }

    public ReadOnlyObjectProperty<Class<? extends Page>> selectedPageProperty() {
        return selectedPage.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<NavTree.NavTreeItem> navTreeProperty() {
        return navTree.getReadOnlyProperty();
    }

    private NavTree.NavTreeItem createTree() {
        var root = NavTree.NavTreeItem.root();
        root.getChildren().addAll(
                createDashboardPage(),
                createHumanResourceGroup(),
                createPeopleGroup(),
                createDeductionsGroup(),
                createInventoryGroup(),
                createQuotationPage(),
                createSalesGroup(),
                createAccountsGroup(),
                createSettingsGroup());
        return root;
    }

    // Example method breakdown for Sales Group
    private NavTree.NavTreeItem createSalesGroup() {
        var sale = NavTree.NavTreeItem.group("Sale", BALANCE_SCALE);
        sale.getChildren().setAll(
                NAV_TREE.get(PointOfSalePage.class),
                NAV_TREE.get(SalesMainPage.class));
        return sale;
    }

    private NavTree.NavTreeItem createPeopleGroup() {
        var people = NavTree.NavTreeItem.group("People", USERS);
        people.getChildren().setAll(
                NAV_TREE.get(SupplierPage.class),
                NAV_TREE.get(CustomerPage.class));
        return people;
    }

    private NavTree.NavTreeItem createDeductionsGroup() {
        var deductions = NavTree.NavTreeItem.group("Deductions", MONEY_BILL);
        deductions.getChildren().setAll(
                NAV_TREE.get(TaxPage.class),
                NAV_TREE.get(DiscountPage.class));
        return deductions;
    }

    private NavTree.NavTreeItem createInventoryGroup() {
        var inventory = NavTree.NavTreeItem.group("Inventory", CUBES);
        inventory.getChildren().setAll(getInventoryPages());
        return inventory;
    }

    private List<NavTree.NavTreeItem> getInventoryPages() {
        List<NavTree.NavTreeItem> items = new ArrayList<>(List.of(
                NAV_TREE.get(ProductCategoryPage.class),
                NAV_TREE.get(BrandPage.class),
                NAV_TREE.get(UnitOfMeasurePage.class),
                NAV_TREE.get(ProductPage.class),
                NAV_TREE.get(RequisitionPage.class),
                NAV_TREE.get(PurchaseMainPage.class),
                NAV_TREE.get(StockInPage.class),
                NAV_TREE.get(AdjustmentPage.class)
        ));
        if (flavor == AppFlavor.DEV) {
            items.add(NAV_TREE.get(TransferPage.class));
        }
        return items;
    }

    private NavTree.NavTreeItem createHumanResourceGroup() {
        var humanResource = NavTree.NavTreeItem.group("Human Resource", USER_TIE);
        humanResource.getChildren().setAll(getHumanResourcePages());
        return humanResource;
    }

    private List<NavTree.NavTreeItem> getHumanResourcePages() {
        List<NavTree.NavTreeItem> items = new ArrayList<>(List.of(
                NAV_TREE.get(DepartmentPage.class),
                NAV_TREE.get(DesignationPage.class),
                NAV_TREE.get(EmploymentStatusPage.class),
                NAV_TREE.get(EmployeePage.class)
        ));
        if (flavor == AppFlavor.DEV) {
            items.add(NAV_TREE.get(LeaveMainPage.class));
            items.add(NAV_TREE.get(PaySlipPage.class));
        }
        return items;
    }

    private NavTree.NavTreeItem createAccountsGroup() {
        var accounts = NavTree.NavTreeItem.group("Accounts", MONEY_CHECK_ALT);
        accounts.getChildren().setAll(
                NAV_TREE.get(AccountPage.class),
                NAV_TREE.get(ExpensePage.class),
                NAV_TREE.get(AccountTransactionPage.class));
        return accounts;
    }

    private NavTree.NavTreeItem createSettingsGroup() {
        var settings = NavTree.NavTreeItem.group("Settings", COG);
        if (flavor == AppFlavor.DEV) {
            settings.getChildren().addAll(
                    NAV_TREE.get(AppSettingPage.class),
                    NAV_TREE.get(BranchPage.class));
        }
        settings.getChildren().setAll(
                NAV_TREE.get(TenantSettingsPage.class),
                NAV_TREE.get(RolePage.class));
        return settings;
    }

    private NavTree.NavTreeItem createQuotationPage() {
        return NavTree.NavTreeItem.mainPage("Quotation", CLIPBOARD_LIST, QuotationPage.class);
    }

    private NavTree.NavTreeItem createDashboardPage() {
        return NavTree.NavTreeItem.mainPage("Dashboard", CHART_BAR, DashboardPage.class);
    }

    public void navigate(Class<? extends Page> page) {
        Objects.requireNonNull(page, "Page cannot be null");
        selectedPage.set(page);
        log.info(() -> "Navigated to: " + page.getSimpleName());
    }

    NavTree.NavTreeItem getTreeItemForPage(Class<? extends Page> pageClass) {
        return NAV_TREE.getOrDefault(pageClass, NAV_TREE.get(DashboardPage.class));
    }
}
