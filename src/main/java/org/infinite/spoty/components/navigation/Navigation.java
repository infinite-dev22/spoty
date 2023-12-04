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
        map.put("QUOTATIONS", NavTree.NavTreeItem.page("Quotations", Pages.getQuotationPane()));
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
        map.put("CUSTOMER_PAYMENT", NavTree.NavTreeItem.page("Customer Payments", Pages.getCashPaymentPane()));
        map.put("SERVICE_PAYMENT", NavTree.NavTreeItem.page("Service Payments", Pages.getServicePaymentPane()));
        map.put("CASH_ADJUSTMENT", NavTree.NavTreeItem.page("Cash Adjustment", Pages.getCashAdjustmentPane()));
        map.put("VOUCHER_APPROVAL", NavTree.NavTreeItem.page("Voucher Approval", Pages.getVoucherApprovalPane()));
        // Account Reports
        map.put("CASH_BOOK", NavTree.NavTreeItem.page("Cash Book", Pages.getCashBookPane()));
        map.put("BANK_BOOK", NavTree.NavTreeItem.page("Bank Book", Pages.getBankBookPane()));
        map.put("DAY_BOOK", NavTree.NavTreeItem.page("Day Book", Pages.getDayBookPane()));
        map.put("GENERAL_LEDGER", NavTree.NavTreeItem.page("General Ledger", Pages.getGeneralLedgerPane()));
        map.put("TRIAL_BALANCE", NavTree.NavTreeItem.page("Trial Balance", Pages.getTrialBalancePane()));
        map.put("INCOME_STATEMENT", NavTree.NavTreeItem.page("Income Statement", Pages.getIncomeStatementPane()));
        map.put("EXPENDITURE_STATEMENT", NavTree.NavTreeItem.page("Expenditure Statement", Pages.getExpenditureStatementPane()));
        map.put("PROFIT_LOSS", NavTree.NavTreeItem.page("Profits & Losses", Pages.getProfitLossPane()));
        map.put("BALANCE_SHEET", NavTree.NavTreeItem.page("Balance Sheet", Pages.getBalanceSheetPane()));
        map.put("FIXED_ASSET_SCHEDULE", NavTree.NavTreeItem.page("Fixed Asset Schedule", Pages.getFixedAssetSchedulePane()));
        map.put("RECEIPT_PAYMENT", NavTree.NavTreeItem.page("Receipt & Payment", Pages.getReceiptAndPaymentPane()));
        map.put("BANK_RECONCILIATION_REPORT", NavTree.NavTreeItem.page("Bank Reconciliation", Pages.getBankReconciliationReportPane()));
        map.put("COA_PRINT", NavTree.NavTreeItem.page("Coa Print", Pages.getCoaPrintPane()));

        map.put("SALE RETURN", NavTree.NavTreeItem.page("Sales", Pages.getSaleReturnPane()));
        map.put("PURCHASE RETURN", NavTree.NavTreeItem.page("Purchases", Pages.getPurchaseReturnPane()));

        map.put(
                "EXPENSE CATEGORY", NavTree.NavTreeItem.page("Category", Pages.getExpenseCategoryPane()));
        map.put("EXPENSE", NavTree.NavTreeItem.page("Expenses", Pages.getExpensePane()));

        map.put("SUPPLIER", NavTree.NavTreeItem.page("Suppliers", Pages.getSupplierPane()));
        map.put("USER", NavTree.NavTreeItem.page("Users", Pages.getUserPane()));

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

        var sale = NavTree.NavTreeItem.group("Sale", "fas-cash-register");
        sale
                .getChildren()
                .setAll(
                        NAV_TREE.get("SALE"),
                        NAV_TREE.get("POINT_OF_SALE"),
                        NAV_TREE.get("SALE_TERMS"));

        var customers =
                NavTree.NavTreeItem.mainPage("Customers", "fas-users", Pages.getCustomerPane());

        var suppliers =
                NavTree.NavTreeItem.mainPage("Suppliers", "fas-users", Pages.getSupplierPane());

        var product = NavTree.NavTreeItem.group("Product", "fas-cubes");
        product
                .getChildren()
                .setAll(
                        NAV_TREE.get("CATEGORY"),
                        NAV_TREE.get("BRAND"),
                        NAV_TREE.get("UNIT"),
                        NAV_TREE.get("PRODUCTS"),
                        NAV_TREE.get("ADJUSTMENTS"),
                        NAV_TREE.get("QUOTATIONS"));

        var purchase =
                NavTree.NavTreeItem.mainPage("Purchases", "fas-cart-plus", Pages.getPurchasePane());

        var account = NavTree.NavTreeItem.group("Account", "fas-cubes");
        var reports = NavTree.NavTreeItem.group("Reports");
        reports
                .getChildren()
                .setAll(
                        NAV_TREE.get("CASH_BOOK"),
                        NAV_TREE.get("BANK_BOOK"),
                        NAV_TREE.get("DAY_BOOK"),
                        NAV_TREE.get("GENERAL_LEDGER"),
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
                        NAV_TREE.get("CUSTOMER_PAYMENT"),
                        NAV_TREE.get("SERVICE_PAYMENT"),
                        NAV_TREE.get("CASH_ADJUSTMENT"),
                        NAV_TREE.get("VOUCHER_APPROVAL"),
                        reports);

        var requisition =
                NavTree.NavTreeItem.mainPage(
                        "Requisitions", "fas-hand-holding", Pages.getRequisitionPane());
        var transfer =
                NavTree.NavTreeItem.mainPage(
                        "Transfers", "fas-arrow-right-arrow-left", Pages.getTransferPane());
        var stockIn =
                NavTree.NavTreeItem.mainPage("Stock In", "fas-cart-flatbed", Pages.getStockInPane());
        var returns = NavTree.NavTreeItem.group("Returns", "fas-retweet");
        returns.getChildren().setAll(NAV_TREE.get("SALE RETURN"), NAV_TREE.get("PURCHASE RETURN"));

        var expense = NavTree.NavTreeItem.group("Expenses", "fas-wallet");
        expense.getChildren().setAll(NAV_TREE.get("EXPENSE CATEGORY"), NAV_TREE.get("EXPENSE"));

        var people = NavTree.NavTreeItem.group("People", "fas-users");
        people
                .getChildren()
                .setAll(NAV_TREE.get("USER"));

        var root = NavTree.NavTreeItem.root();
        root.getChildren()
                .addAll(
                        dashboard,
                        sale,
                        customers,
                        suppliers,
                        product,
                        purchase,
                        account,
                        requisition,
                        transfer,
                        stockIn,
                        returns,
                        expense,
                        people);
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
