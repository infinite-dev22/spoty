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

package org.infinite.spoty.database.dao;


class PermissionTest {
//  @Test
//  void dashboardAccess() {
//    try {
//      var accessDashboard =
//          new Permission(
//              "access_dashboard", "Access Dashboard", "User can have access to a Dashboard");
//      PermissionDao.savePermission(accessDashboard);
//    } catch (Exception e) {
//      throw new AssertionError(e);
//    }
//  }
//  @Test
//  void posAccess() {
//    try {
//      var accessPOS =
//          new Permission(
//              "access_pos", "Access POS", "User can have access to a POS");
//      PermissionDao.savePermission(accessPOS);
//    } catch (Exception e) {
//      throw new AssertionError(e);
//    }
//  }
//
//  @Test
//  void purchasesAccess() {
//    try {
//      var createPurchases =
//          new Permission(
//              "create_purchases", "Create Purchases", "User can have create purchases");
//      var viewPurchases =
//          new Permission(
//              "view_purchases", "View Purchases", "User can have view purchases");
//      var editPurchases =
//          new Permission(
//              "edit_purchases", "Edit Purchases", "User can have edit purchases");
//      var deletePurchases =
//          new Permission(
//              "delete_purchases", "Delete Purchases", "User can have view purchases");
//
//      PermissionDao.savePermission(createPurchases);
//      PermissionDao.savePermission(viewPurchases);
//      PermissionDao.savePermission(editPurchases);
//      PermissionDao.savePermission(deletePurchases);
//    } catch (Exception e) {
//      throw new AssertionError(e);
//    }
//  }
//
//  @Test
//  void adjustmentsAccess() {
//    try {
//      var createAdjustments =
//          new Permission(
//              "create_adjustments", "Create Adjustments", "User can have create adjustments");
//      var viewAdjustments =
//          new Permission(
//              "view_adjustments", "View Adjustments", "User can have view adjustments");
//      var editAdjustments =
//          new Permission(
//              "edit_adjustments", "Edit Adjustments", "User can have edit adjustments");
//      var deleteAdjustments =
//          new Permission(
//              "delete_adjustments", "Delete Adjustments", "User can have view adjustments");
//
//      PermissionDao.savePermission(createAdjustments);
//      PermissionDao.savePermission(viewAdjustments);
//      PermissionDao.savePermission(editAdjustments);
//      PermissionDao.savePermission(deleteAdjustments);
//    } catch (Exception e) {
//      throw new AssertionError(e);
//    }
//  }
//
//  @Test
//  void transfersAccess() {
//    try {
//      var createTransfers =
//          new Permission(
//              "create_transfers", "Create Transfers", "User can have create transfers");
//      var viewTransfers =
//          new Permission(
//              "view_transfers", "View Transfers", "User can have view transfers");
//      var editTransfers =
//          new Permission(
//              "edit_transfers", "Edit Transfers", "User can have edit transfers");
//      var deleteTransfers =
//          new Permission(
//              "delete_transfers", "Delete Transfers", "User can have view transfers");
//
//      PermissionDao.savePermission(createTransfers);
//      PermissionDao.savePermission(viewTransfers);
//      PermissionDao.savePermission(editTransfers);
//      PermissionDao.savePermission(deleteTransfers);
//    } catch (Exception e) {
//      throw new AssertionError(e);
//    }
//  }
//
//  @Test
//  void quotationsAccess() {
//    try {
//      var createQuotations =
//          new Permission(
//              "create_quotations", "Create Quotations", "User can have create quotations");
//      var viewQuotations =
//          new Permission(
//              "view_quotations", "View Quotations", "User can have view quotations");
//      var editQuotations =
//          new Permission(
//              "edit_quotations", "Edit Quotations", "User can have edit quotations");
//      var deleteQuotations =
//          new Permission(
//              "delete_quotations", "Delete Quotations", "User can have view quotations");
//
//      PermissionDao.savePermission(createQuotations);
//      PermissionDao.savePermission(viewQuotations);
//      PermissionDao.savePermission(editQuotations);
//      PermissionDao.savePermission(deleteQuotations);
//    } catch (Exception e) {
//      throw new AssertionError(e);
//    }
//  }
//
//  @Test
//  void saleReturnsAccess() {
//    try {
//      var createSaleReturns =
//          new Permission(
//              "create_sale_returns", "Create Sale returns", "User can have create sale returns");
//      var viewSaleReturns =
//          new Permission(
//              "view_sale_returns", "View Sale returns", "User can have view sale returns");
//      var editSaleReturns =
//          new Permission(
//              "edit_sale_returns", "Edit Sale returns", "User can have edit sale returns");
//      var deleteSaleReturns =
//          new Permission(
//              "delete_sale_returns", "Delete Sale returns", "User can have view sale returns");
//
//      PermissionDao.savePermission(createSaleReturns);
//      PermissionDao.savePermission(viewSaleReturns);
//      PermissionDao.savePermission(editSaleReturns);
//      PermissionDao.savePermission(deleteSaleReturns);
//    } catch (Exception e) {
//      throw new AssertionError(e);
//    }
//  }
//
//  @Test
//  void paymentSalesAccess() {
//    try {
//      var createPaymentSales =
//          new Permission(
//              "create_payment_sales", "Create Sale returns", "User can have create payment sales");
//      var viewPaymentSales =
//          new Permission(
//              "view_payment_sales", "View Sale returns", "User can have view payment sales");
//      var editPaymentSales =
//          new Permission(
//              "edit_payment_sales", "Edit Sale returns", "User can have edit payment sales");
//      var deletePaymentSales =
//          new Permission(
//              "delete_payment_sales", "Delete Sale returns", "User can have view payment sales");
//
//      PermissionDao.savePermission(createPaymentSales);
//      PermissionDao.savePermission(viewPaymentSales);
//      PermissionDao.savePermission(editPaymentSales);
//      PermissionDao.savePermission(deletePaymentSales);
//    } catch (Exception e) {
//      throw new AssertionError(e);
//    }
//  }
//
//  @Test
//  void paymentPurchasesAccess() {
//    try {
//      var createPaymentPurchases =
//          new Permission(
//              "create_payment_purchases", "Create Purchase returns", "User can have create payment purchases");
//      var viewPaymentPurchases =
//          new Permission(
//              "view_payment_purchases", "View Purchase returns", "User can have view payment purchases");
//      var editPaymentPurchases =
//          new Permission(
//              "edit_payment_purchases", "Edit Purchase returns", "User can have edit payment purchases");
//      var deletePaymentPurchases =
//          new Permission(
//              "delete_payment_purchases", "Delete Purchase returns", "User can have view payment purchases");
//
//      PermissionDao.savePermission(createPaymentPurchases);
//      PermissionDao.savePermission(viewPaymentPurchases);
//      PermissionDao.savePermission(editPaymentPurchases);
//      PermissionDao.savePermission(deletePaymentPurchases);
//    } catch (Exception e) {
//      throw new AssertionError(e);
//    }
//  }
//
//  @Test
//  void purchaseReturnsAccess() {
//    try {
//      var createPurchaseReturns =
//          new Permission(
//              "create_purchase_returns", "Create Purchase returns", "User can have create purchase returns");
//      var viewPurchaseReturns =
//          new Permission(
//              "view_purchase_returns", "View Purchase returns", "User can have view purchase returns");
//      var editPurchaseReturns =
//          new Permission(
//              "edit_purchase_returns", "Edit Purchase returns", "User can have edit purchase returns");
//      var deletePurchaseReturns =
//          new Permission(
//              "delete_purchase_returns", "Delete Purchase returns", "User can have view purchase returns");
//
//      PermissionDao.savePermission(createPurchaseReturns);
//      PermissionDao.savePermission(viewPurchaseReturns);
//      PermissionDao.savePermission(editPurchaseReturns);
//      PermissionDao.savePermission(deletePurchaseReturns);
//    } catch (Exception e) {
//      throw new AssertionError(e);
//    }
//  }
//
//  @Test
//  void paymentReturnsAccess() {
//    try {
//      var createPaymentReturns =
//          new Permission(
//              "create_payment_returns", "Create Payment returns", "User can have create payment returns");
//      var viewPaymentReturns =
//          new Permission(
//              "view_payment_returns", "View Payment returns", "User can have view payment returns");
//      var editPaymentReturns =
//          new Permission(
//              "edit_payment_returns", "Edit Payment returns", "User can have edit payment returns");
//      var deletePaymentReturns =
//          new Permission(
//              "delete_payment_returns", "Delete Payment returns", "User can have view payment returns");
//
//      PermissionDao.savePermission(createPaymentReturns);
//      PermissionDao.savePermission(viewPaymentReturns);
//      PermissionDao.savePermission(editPaymentReturns);
//      PermissionDao.savePermission(deletePaymentReturns);
//    } catch (Exception e) {
//      throw new AssertionError(e);
//    }
//  }
//
//  @Test
//  void expensesAccess() {
//    try {
//      var createExpenses =
//          new Permission(
//              "create_expenses", "Create Expenses", "User can have create expenses");
//      var viewExpenses =
//          new Permission(
//              "view_expenses", "View Expenses", "User can have view expenses");
//      var editExpenses =
//          new Permission(
//              "edit_expenses", "Edit Expenses", "User can have edit expenses");
//      var deleteExpenses =
//          new Permission(
//              "delete_expenses", "Delete Expenses", "User can have view expenses");
//
//      PermissionDao.savePermission(createExpenses);
//      PermissionDao.savePermission(viewExpenses);
//      PermissionDao.savePermission(editExpenses);
//      PermissionDao.savePermission(deleteExpenses);
//    } catch (Exception e) {
//      throw new AssertionError(e);
//    }
//  }
//
//  @Test
//  void permissionsAccess() {
//    try {
//      var createPermissions =
//          new Permission(
//              "create_permissions", "Create Permissions", "User can have create permissions");
//      var viewPermissions =
//          new Permission(
//              "view_permissions", "View Permissions", "User can have view permissions");
//      var editPermissions =
//          new Permission(
//              "edit_permissions", "Edit Permissions", "User can have edit permissions");
//      var deletePermissions =
//          new Permission(
//              "delete_permissions", "Delete Permissions", "User can have view permissions");
//
//      PermissionDao.savePermission(createPermissions);
//      PermissionDao.savePermission(viewPermissions);
//      PermissionDao.savePermission(editPermissions);
//      PermissionDao.savePermission(deletePermissions);
//    } catch (Exception e) {
//      throw new AssertionError(e);
//    }
//  }
//
//  @Test
//  void usersAccess() {
//    try {
//      var createUsers =
//          new Permission(
//              "create_users", "Create Users", "User can have create users");
//      var viewUsers =
//          new Permission(
//              "view_users", "View Users", "User can have view users");
//      var editUsers =
//          new Permission(
//              "edit_users", "Edit Users", "User can have edit users");
//      var deleteUsers =
//          new Permission(
//              "delete_users", "Delete Users", "User can have view users");
//
//      PermissionDao.savePermission(createUsers);
//      PermissionDao.savePermission(viewUsers);
//      PermissionDao.savePermission(editUsers);
//      PermissionDao.savePermission(deleteUsers);
//    } catch (Exception e) {
//      throw new AssertionError(e);
//    }
//  }
//
//  @Test
//  void salesAccess() {
//    try {
//      var createSales =
//          new Permission(
//              "create_sales", "Create Sales", "User can have create sales");
//      var viewSales =
//          new Permission(
//              "view_sales", "View Sales", "User can have view sales");
//      var editSales =
//          new Permission(
//              "edit_sales", "Edit Sales", "User can have edit sales");
//      var deleteSales =
//          new Permission(
//              "delete_sales", "Delete Sales", "User can have view sales");
//
//      PermissionDao.savePermission(createSales);
//      PermissionDao.savePermission(viewSales);
//      PermissionDao.savePermission(editSales);
//      PermissionDao.savePermission(deleteSales);
//    } catch (Exception e) {
//      throw new AssertionError(e);
//    }
//  }
//
//  @Test
//  void settingsAccess() {
//    try {
//      var createSettings =
//          new Permission(
//              "create_settings", "Create Settings", "User can have create settings");
//      var viewSettings =
//          new Permission(
//              "view_settings", "View Settings", "User can have view settings");
//      var editSettings =
//          new Permission(
//              "edit_settings", "Edit Settings", "User can have edit settings");
//      var deleteSettings =
//          new Permission(
//              "delete_settings", "Delete Settings", "User can have view settings");
//
//      PermissionDao.savePermission(createSettings);
//      PermissionDao.savePermission(viewSettings);
//      PermissionDao.savePermission(editSettings);
//      PermissionDao.savePermission(deleteSettings);
//    } catch (Exception e) {
//      throw new AssertionError(e);
//    }
//  }
//
//  @Test
//  void customersAccess() {
//    try {
//      var createCustomers =
//          new Permission(
//              "create_customers", "Create Customers", "User can have create customers");
//      var viewCustomers =
//          new Permission(
//              "view_customers", "View Customers", "User can have view customers");
//      var editCustomers =
//          new Permission(
//              "edit_customers", "Edit Customers", "User can have edit customers");
//      var deleteCustomers =
//          new Permission(
//              "delete_customers", "Delete Customers", "User can have view customers");
//
//      PermissionDao.savePermission(createCustomers);
//      PermissionDao.savePermission(viewCustomers);
//      PermissionDao.savePermission(editCustomers);
//      PermissionDao.savePermission(deleteCustomers);
//    } catch (Exception e) {
//      throw new AssertionError(e);
//    }
//  }
//
//  @Test
//  void reportsAccess() {
//    try {
//      var createReports =
//          new Permission(
//              "create_reports", "Create Reports", "User can have create reports");
//      var viewReports =
//          new Permission(
//              "view_reports", "View Reports", "User can have view reports");
//      var editReports =
//          new Permission(
//              "edit_reports", "Edit Reports", "User can have edit reports");
//      var deleteReports =
//          new Permission(
//              "delete_reports", "Delete Reports", "User can have view reports");
//
//      PermissionDao.savePermission(createReports);
//      PermissionDao.savePermission(viewReports);
//      PermissionDao.savePermission(editReports);
//      PermissionDao.savePermission(deleteReports);
//    } catch (Exception e) {
//      throw new AssertionError(e);
//    }
//  }
}
