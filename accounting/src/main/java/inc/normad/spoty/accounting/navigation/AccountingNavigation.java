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

package inc.normad.spoty.accounting.navigation;

import inc.normad.spoty.reporting.navigation.ReportsNav;
import inc.normad.spoty.utils.navigation.NavTree;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AccountingNavigation {
    private static final Map<String, NavTree.NavTreeItem> NAV_TREE = createNavItems();

    public static @NotNull Map<String, NavTree.NavTreeItem> createNavItems() {
        var map = new HashMap<String, NavTree.NavTreeItem>();
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

        return map;
    }

    public static NavTree.NavTreeItem createTree() {
        var reportsNav = new ReportsNav();

        var account = NavTree.NavTreeItem.group("Account", "fas-credit-card");
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
                        reportsNav.createAccountingReportsTree());
        return account;
    }
}
