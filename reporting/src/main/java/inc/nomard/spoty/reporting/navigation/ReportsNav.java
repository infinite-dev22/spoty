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

package inc.nomard.spoty.reporting.navigation;

import inc.nomard.spoty.utils.navigation.Nav;
import inc.nomard.spoty.utils.navigation.NavTree;
import javafx.scene.control.TreeItem;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ReportsNav {
    private static final Map<String, NavTree.NavTreeItem> NAV_TREE = createNavItems();

    public static @NotNull Map<String, NavTree.NavTreeItem> createNavItems() {
        var map = new HashMap<String, NavTree.NavTreeItem>();
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
        map.put("STOCK_REPORT", NavTree.NavTreeItem.page("Stock Report", Pages.getStockReportPane()));

        return map;
    }

    private NavTree.@NotNull NavTreeItem createAllReportsTree() {
        var accountReports = NavTree.NavTreeItem.group("Accounting Reports", "fas-clipboard-list");
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
                        NAV_TREE.get("USER_SALES_REPORT"),
                        NAV_TREE.get("STOCK_REPORT"));

        var root = NavTree.NavTreeItem.root();
        root.getChildren()
                .addAll(accountReports,
                        reports);
        return root;
    }

    public NavTree.@NotNull NavTreeItem createAccountingReportsTree() {
        var accountReports = NavTree.NavTreeItem.group("Accounting Reports", "fas-clipboard-list");
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

        var root = NavTree.NavTreeItem.root();
        root.getChildren().addAll(accountReports);
        return root;
    }

    private NavTree.@NotNull NavTreeItem createGeneralReportsTree() {
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
                        NAV_TREE.get("USER_SALES_REPORT"),
                        NAV_TREE.get("STOCK_REPORT"));

        var root = NavTree.NavTreeItem.root();
        root.getChildren().addAll(reports);
        return root;
    }

    public TreeItem<Nav> createGeneralReportsItems() {
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
                        NAV_TREE.get("USER_SALES_REPORT"),
                        NAV_TREE.get("STOCK_REPORT"));

        return reports;
    }
}
