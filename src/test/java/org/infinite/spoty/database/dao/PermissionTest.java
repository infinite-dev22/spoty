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

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import org.infinite.spoty.database.connection.SQLiteConnection;
import org.infinite.spoty.database.models.Permission;
import org.junit.jupiter.api.Test;

class PermissionTest {
  SQLiteConnection connection = SQLiteConnection.getInstance();
  ConnectionSource connectionSource = connection.getConnection();
  Dao<Permission, Long> permissionDao;

  public PermissionTest() {
    try {
      permissionDao = DaoManager.createDao(connectionSource, Permission.class);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void dashboardAccess() {
    try {
      var accessDashboard =
          new Permission("access_dashboard", "Access Dashboard", "User can access to a Dashboard");
      permissionDao.createOrUpdate(accessDashboard);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void posAccess() {
    try {
      var accessPOS = new Permission("access_pos", "Access POS", "User can access to a POS");
      permissionDao.createOrUpdate(accessPOS);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void purchasesAccess() {
    try {
      var createPurchases =
          new Permission("create_purchases", "Create Purchases", "User can create purchases");
      var viewPurchases =
          new Permission("view_purchases", "View Purchases", "User can view purchases");
      var editPurchases =
          new Permission("edit_purchases", "Edit Purchases", "User can edit purchases");
      var deletePurchases =
          new Permission("delete_purchases", "Delete Purchases", "User can delete purchases");

      permissionDao.createOrUpdate(createPurchases);
      permissionDao.createOrUpdate(viewPurchases);
      permissionDao.createOrUpdate(editPurchases);
      permissionDao.createOrUpdate(deletePurchases);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void requisitionsAccess() {
    try {
      var createRequisitions =
          new Permission("create_requisitions", "Create Requisitions", "User can create requisitions");
      var viewRequisitions =
          new Permission("view_requisitions", "View Requisitions", "User can view requisitions");
      var editRequisitions =
          new Permission("edit_requisitions", "Edit Requisitions", "User can edit requisitions");
      var deleteRequisitions =
          new Permission("delete_requisitions", "Delete Requisitions", "User can delete requisitions");

      permissionDao.createOrUpdate(createRequisitions);
      permissionDao.createOrUpdate(viewRequisitions);
      permissionDao.createOrUpdate(editRequisitions);
      permissionDao.createOrUpdate(deleteRequisitions);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void stockInsAccess() {
    try {
      var createStockIns =
          new Permission("create_stock_ins", "Create Stock Ins", "User can create stock ins");
      var viewStockIns =
          new Permission("view_stock_ins", "View Stock Ins", "User can view stock ins");
      var editStockIns =
          new Permission("edit_stock_ins", "Edit Stock Ins", "User can edit stock ins");
      var deleteStockIns =
          new Permission("delete_stock_ins", "Delete Stock Ins", "User can delete stock ins");

      permissionDao.createOrUpdate(createStockIns);
      permissionDao.createOrUpdate(viewStockIns);
      permissionDao.createOrUpdate(editStockIns);
      permissionDao.createOrUpdate(deleteStockIns);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void productCategoriesAccess() {
    try {
      var createProductCategories =
          new Permission("create_product_categories", "Create Product Categories", "User can create product categories");
      var viewProductCategories =
          new Permission("view_product_categories", "View Product Categories", "User can view product categories");
      var editProductCategories =
          new Permission("edit_product_categories", "Edit Product Categories", "User can edit product categories");
      var deleteProductCategories =
          new Permission("delete_product_categories", "Delete Product Categories", "User can delete product categories");
      var accessProductCategories =
          new Permission("access_product_categories", "Access Product Categories", "User can access product categories");

      permissionDao.createOrUpdate(createProductCategories);
      permissionDao.createOrUpdate(viewProductCategories);
      permissionDao.createOrUpdate(editProductCategories);
      permissionDao.createOrUpdate(deleteProductCategories);
      permissionDao.createOrUpdate(accessProductCategories);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void unitsOfMeasureAccess() {
    try {
      var createUnitsOfMeasure =
          new Permission("create_units_of_measure", "Create Units Of Measure", "User can create units of measure");
      var viewUnitsOfMeasure =
          new Permission("view_units_of_measure", "View Units Of Measure", "User can view units of measure");
      var editUnitsOfMeasure =
          new Permission("edit_units_of_measure", "Edit Units Of Measure", "User can edit units of measure");
      var deleteUnitsOfMeasure =
          new Permission("delete_units_of_measure", "Delete Units Of Measure", "User can delete units of measure");
      var accessUnitsOfMeasure =
          new Permission("access_units_of_measure", "Access Units Of Measure", "User can access units of measure");

      permissionDao.createOrUpdate(createUnitsOfMeasure);
      permissionDao.createOrUpdate(viewUnitsOfMeasure);
      permissionDao.createOrUpdate(editUnitsOfMeasure);
      permissionDao.createOrUpdate(deleteUnitsOfMeasure);
      permissionDao.createOrUpdate(accessUnitsOfMeasure);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void adjustmentsAccess() {
    try {
      var createAdjustments =
          new Permission("create_adjustments", "Create Adjustments", "User can create adjustments");
      var viewAdjustments =
          new Permission("view_adjustments", "View Adjustments", "User can view adjustments");
      var editAdjustments =
          new Permission("edit_adjustments", "Edit Adjustments", "User can edit adjustments");
      var deleteAdjustments =
          new Permission("delete_adjustments", "Delete Adjustments", "User can delete adjustments");

      permissionDao.createOrUpdate(createAdjustments);
      permissionDao.createOrUpdate(viewAdjustments);
      permissionDao.createOrUpdate(editAdjustments);
      permissionDao.createOrUpdate(deleteAdjustments);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void transfersAccess() {
    try {
      var createTransfers =
          new Permission("create_transfers", "Create Transfers", "User can create transfers");
      var viewTransfers =
          new Permission("view_transfers", "View Transfers", "User can view transfers");
      var editTransfers =
          new Permission("edit_transfers", "Edit Transfers", "User can edit transfers");
      var deleteTransfers =
          new Permission("delete_transfers", "Delete Transfers", "User can delete transfers");

      permissionDao.createOrUpdate(createTransfers);
      permissionDao.createOrUpdate(viewTransfers);
      permissionDao.createOrUpdate(editTransfers);
      permissionDao.createOrUpdate(deleteTransfers);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void quotationsAccess() {
    try {
      var createQuotations =
          new Permission("create_quotations", "Create Quotations", "User can create quotations");
      var viewQuotations =
          new Permission("view_quotations", "View Quotations", "User can view quotations");
      var editQuotations =
          new Permission("edit_quotations", "Edit Quotations", "User can edit quotations");
      var deleteQuotations =
          new Permission("delete_quotations", "Delete Quotations", "User can delete quotations");

      permissionDao.createOrUpdate(createQuotations);
      permissionDao.createOrUpdate(viewQuotations);
      permissionDao.createOrUpdate(editQuotations);
      permissionDao.createOrUpdate(deleteQuotations);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void saleReturnsAccess() {
    try {
      var createSaleReturns =
          new Permission(
              "create_sale_returns", "Create Sale returns", "User can create sale returns");
      var viewSaleReturns =
          new Permission("view_sale_returns", "View Sale returns", "User can view sale returns");
      var editSaleReturns =
          new Permission("edit_sale_returns", "Edit Sale returns", "User can edit sale returns");
      var deleteSaleReturns =
          new Permission(
              "delete_sale_returns", "Delete Sale returns", "User can delete sale returns");

      permissionDao.createOrUpdate(createSaleReturns);
      permissionDao.createOrUpdate(viewSaleReturns);
      permissionDao.createOrUpdate(editSaleReturns);
      permissionDao.createOrUpdate(deleteSaleReturns);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void paymentSalesAccess() {
    try {
      var createPaymentSales =
          new Permission(
              "create_payment_sales", "Create Sale returns", "User can create payment sales");
      var viewPaymentSales =
          new Permission("view_payment_sales", "View Sale returns", "User can view payment sales");
      var editPaymentSales =
          new Permission("edit_payment_sales", "Edit Sale returns", "User can edit payment sales");
      var deletePaymentSales =
          new Permission(
              "delete_payment_sales", "Delete Sale returns", "User can delete payment sales");

      permissionDao.createOrUpdate(createPaymentSales);
      permissionDao.createOrUpdate(viewPaymentSales);
      permissionDao.createOrUpdate(editPaymentSales);
      permissionDao.createOrUpdate(deletePaymentSales);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void paymentPurchasesAccess() {
    try {
      var createPaymentPurchases =
          new Permission(
              "create_payment_purchases",
              "Create Purchase returns",
              "User can create payment purchases");
      var viewPaymentPurchases =
          new Permission(
              "view_payment_purchases", "View Purchase returns", "User can view payment purchases");
      var editPaymentPurchases =
          new Permission(
              "edit_payment_purchases", "Edit Purchase returns", "User can edit payment purchases");
      var deletePaymentPurchases =
          new Permission(
              "delete_payment_purchases",
              "Delete Purchase returns",
              "User can delete payment purchases");

      permissionDao.createOrUpdate(createPaymentPurchases);
      permissionDao.createOrUpdate(viewPaymentPurchases);
      permissionDao.createOrUpdate(editPaymentPurchases);
      permissionDao.createOrUpdate(deletePaymentPurchases);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void purchaseReturnsAccess() {
    try {
      var createPurchaseReturns =
          new Permission(
              "create_purchase_returns",
              "Create Purchase returns",
              "User can create purchase returns");
      var viewPurchaseReturns =
          new Permission(
              "view_purchase_returns", "View Purchase returns", "User can view purchase returns");
      var editPurchaseReturns =
          new Permission(
              "edit_purchase_returns", "Edit Purchase returns", "User can edit purchase returns");
      var deletePurchaseReturns =
          new Permission(
              "delete_purchase_returns",
              "Delete Purchase returns",
              "User can delete purchase returns");

      permissionDao.createOrUpdate(createPurchaseReturns);
      permissionDao.createOrUpdate(viewPurchaseReturns);
      permissionDao.createOrUpdate(editPurchaseReturns);
      permissionDao.createOrUpdate(deletePurchaseReturns);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void paymentReturnsAccess() {
    try {
      var createPaymentReturns =
          new Permission(
              "create_payment_returns",
              "Create Payment returns",
              "User can create payment returns");
      var viewPaymentReturns =
          new Permission(
              "view_payment_returns", "View Payment returns", "User can view payment returns");
      var editPaymentReturns =
          new Permission(
              "edit_payment_returns", "Edit Payment returns", "User can edit payment returns");
      var deletePaymentReturns =
          new Permission(
              "delete_payment_returns", "Delete Payment returns", "User can delete payment returns");

      permissionDao.createOrUpdate(createPaymentReturns);
      permissionDao.createOrUpdate(viewPaymentReturns);
      permissionDao.createOrUpdate(editPaymentReturns);
      permissionDao.createOrUpdate(deletePaymentReturns);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void expensesAccess() {
    try {
      var createExpenses =
          new Permission("create_expenses", "Create Expenses", "User can create expenses");
      var viewExpenses = new Permission("view_expenses", "View Expenses", "User can view expenses");
      var editExpenses = new Permission("edit_expenses", "Edit Expenses", "User can edit expenses");
      var deleteExpenses =
          new Permission("delete_expenses", "Delete Expenses", "User can delete expenses");

      permissionDao.createOrUpdate(createExpenses);
      permissionDao.createOrUpdate(viewExpenses);
      permissionDao.createOrUpdate(editExpenses);
      permissionDao.createOrUpdate(deleteExpenses);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void permissionsAccess() {
    try {
      var createPermissions =
          new Permission("create_permissions", "Create Permissions", "User can create permissions");
      var viewPermissions =
          new Permission("view_permissions", "View Permissions", "User can view permissions");
      var editPermissions =
          new Permission("edit_permissions", "Edit Permissions", "User can edit permissions");
      var deletePermissions =
          new Permission("delete_permissions", "Delete Permissions", "User can delete permissions");

      permissionDao.createOrUpdate(createPermissions);
      permissionDao.createOrUpdate(viewPermissions);
      permissionDao.createOrUpdate(editPermissions);
      permissionDao.createOrUpdate(deletePermissions);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void usersAccess() {
    try {
      var createUsers = new Permission("create_users", "Create Users", "User can create users");
      var viewUsers = new Permission("view_users", "View Users", "User can view users");
      var editUsers = new Permission("edit_users", "Edit Users", "User can edit users");
      var deleteUsers = new Permission("delete_users", "Delete Users", "User can delete users");

      permissionDao.createOrUpdate(createUsers);
      permissionDao.createOrUpdate(viewUsers);
      permissionDao.createOrUpdate(editUsers);
      permissionDao.createOrUpdate(deleteUsers);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void brandsAccess() {
    try {
      var createBrands = new Permission("create_brands", "Create Brands", "User can create brands");
      var viewBrands = new Permission("view_brands", "View Brands", "User can view brands");
      var editBrands = new Permission("edit_brands", "Edit Brands", "User can edit brands");
      var deleteBrands = new Permission("delete_brands", "Delete Brands", "User can delete brands");
      var accessBrands = new Permission("access_brands", "Access Brands", "User can access brands");

      permissionDao.createOrUpdate(createBrands);
      permissionDao.createOrUpdate(viewBrands);
      permissionDao.createOrUpdate(editBrands);
      permissionDao.createOrUpdate(deleteBrands);
      permissionDao.createOrUpdate(accessBrands);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void salesAccess() {
    try {
      var createSales = new Permission("create_sales", "Create Sales", "User can create sales");
      var viewSales = new Permission("view_sales", "View Sales", "User can view sales");
      var editSales = new Permission("edit_sales", "Edit Sales", "User can edit sales");
      var deleteSales = new Permission("delete_sales", "Delete Sales", "User can delete sales");

      permissionDao.createOrUpdate(createSales);
      permissionDao.createOrUpdate(viewSales);
      permissionDao.createOrUpdate(editSales);
      permissionDao.createOrUpdate(deleteSales);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void currenciesAccess() {
    try {
      var createCurrencies =
          new Permission("create_currencies", "Create Currencies", "User can create currencies");
      var viewCurrencies =
          new Permission("view_currencies", "View Currencies", "User can view currencies");
      var editCurrencies =
          new Permission("edit_currencies", "Edit Currencies", "User can edit currencies");
      var deleteCurrencies =
          new Permission("delete_currencies", "Delete Currencies", "User can delete currencies");

      permissionDao.createOrUpdate(createCurrencies);
      permissionDao.createOrUpdate(viewCurrencies);
      permissionDao.createOrUpdate(editCurrencies);
      permissionDao.createOrUpdate(deleteCurrencies);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void branchesAccess() {
    try {
      var createBranches =
          new Permission("create_branches", "Create Branches", "User can create branches");
      var viewBranches = new Permission("view_branches", "View Branches", "User can view branches");
      var editBranches = new Permission("edit_branches", "Edit Branches", "User can edit branches");
      var deleteBranches =
          new Permission("delete_branches", "Delete Branches", "User can delete branches");

      permissionDao.createOrUpdate(createBranches);
      permissionDao.createOrUpdate(viewBranches);
      permissionDao.createOrUpdate(editBranches);
      permissionDao.createOrUpdate(deleteBranches);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void settingsAccess() {
    try {
      var viewSystemSettings =
          new Permission(
              "view_system_settings", "View System Settings", "User can view system settings");
      var editSystemSettings =
          new Permission(
              "edit_system_settings", "Edit System Settings", "User can edit system settings");
      var accessCurrencySettings =
          new Permission(
              "access_currency_settings",
              "Access Currency Settings",
              "User can access currency settings");
      var viewPOSSettings =
          new Permission("view_pos_settings", "View POS Settings", "User can view pos settings");
      var editPOSSettings =
          new Permission("edit_pos_settings", "Edit POS Settings", "User can edit pos settings");
      var accessBranchSettings =
          new Permission(
              "access_branch_settings",
              "Access Branch Settings",
              "User can access branch settings");
      var viewBackupSettings =
          new Permission(
              "view_backup_settings",
              "View Backup Settings",
              "User can delete backup settings");
      var createBackup =
          new Permission(
              "create_backup",
              "Create Backup",
              "User can create backup");

      permissionDao.createOrUpdate(viewSystemSettings);
      permissionDao.createOrUpdate(editSystemSettings);
      permissionDao.createOrUpdate(accessCurrencySettings);
      permissionDao.createOrUpdate(viewPOSSettings);
      permissionDao.createOrUpdate(editPOSSettings);
      permissionDao.createOrUpdate(accessBranchSettings);
      permissionDao.createOrUpdate(viewBackupSettings);
      permissionDao.createOrUpdate(createBackup);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void customersAccess() {
    try {
      var createCustomers =
          new Permission("create_customers", "Create Customers", "User can create customers");
      var viewCustomers =
          new Permission("view_customers", "View Customers", "User can view customers");
      var editCustomers =
          new Permission("edit_customers", "Edit Customers", "User can edit customers");
      var deleteCustomers =
          new Permission("delete_customers", "Delete Customers", "User can delete customers");
      var importCustomers =
          new Permission("import_customers", "Import Customers", "User can import customers");

      permissionDao.createOrUpdate(createCustomers);
      permissionDao.createOrUpdate(viewCustomers);
      permissionDao.createOrUpdate(editCustomers);
      permissionDao.createOrUpdate(deleteCustomers);
      permissionDao.createOrUpdate(importCustomers);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void suppliersAccess() {
    try {
      var createSuppliers =
          new Permission("create_suppliers", "Create Suppliers", "User can create suppliers");
      var viewSuppliers =
          new Permission("view_suppliers", "View Suppliers", "User can view suppliers");
      var editSuppliers =
          new Permission("edit_suppliers", "Edit Suppliers", "User can edit suppliers");
      var deleteSuppliers =
          new Permission("delete_suppliers", "Delete Suppliers", "User can delete suppliers");
      var importSuppliers =
          new Permission("import_suppliers", "Import Suppliers", "User can import suppliers");

      permissionDao.createOrUpdate(createSuppliers);
      permissionDao.createOrUpdate(viewSuppliers);
      permissionDao.createOrUpdate(editSuppliers);
      permissionDao.createOrUpdate(deleteSuppliers);
      permissionDao.createOrUpdate(importSuppliers);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void productsAccess() {
    try {
      var createProducts =
          new Permission("create_products", "Create Products", "User can create products");
      var viewProducts =
          new Permission("view_products", "View Products", "User can view products");
      var editProducts =
          new Permission("edit_products", "Edit Products", "User can edit products");
      var deleteProducts =
          new Permission("delete_products", "Delete Products", "User can delete products");
      var importProducts =
          new Permission("import_products", "Import Products", "User can import products");

      permissionDao.createOrUpdate(createProducts);
      permissionDao.createOrUpdate(viewProducts);
      permissionDao.createOrUpdate(editProducts);
      permissionDao.createOrUpdate(deleteProducts);
      permissionDao.createOrUpdate(importProducts);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  @Test
  void reportsAccess() {
    try {
      var accessPaymentsSalesReports =
          new Permission(
              "access_payments_sales_reports",
              "Access Payments Sales Reports",
              "User can access payments sales reports");
      var accessPaymentsPurchasesReports =
          new Permission(
              "access_payments_purchases_reports",
              "Access Payments Purchases Reports",
              "User can access payments purchases reports");
      var accessSalesReturnsPaymentsReports =
          new Permission(
              "access_sales_returns_payments_reports",
              "Access Sales Returns Payments Reports",
              "User can access sales returns payments reports");
      var accessPurchasesReturnsPaymentsReports =
          new Permission(
              "access_purchases_returns_payments_reports",
              "Access Purchases Returns Payments Reports",
              "User can access purchases returns payments reports");
      var accessSalesReports =
          new Permission(
              "access_sales_reports", "Access Sales Reports", "User can access sales reports");
      var accessPurchasesReports =
          new Permission(
              "access_purchases_reports",
              "Access Purchases Reports",
              "User can access purchases reports");
      var accessCustomersReports =
          new Permission(
              "access_customers_reports",
              "Access Customers Reports",
              "User can access customers reports");
      var accessSuppliersReports =
          new Permission(
              "access_suppliers_reports",
              "Access Suppliers Reports",
              "User can access suppliers reports");
      var accessProfitsAndLossesReports =
          new Permission(
              "access_profits_and_losses_reports",
              "Access Profits And Losses Reports",
              "User can access profits and losses reports");
      var accessProductQuantityReports =
          new Permission(
              "access_product_quantity_reports",
              "Access Product Quantity Reports",
              "User can access product quantity reports");
      var accessBranchStockChartsReports =
          new Permission(
              "access_branch_stock_charts_reports",
              "Access Branch Stock Charts Reports",
              "User can access branch stock charts reports");
      var accessTopSellingProductsReports =
          new Permission(
              "access_top_selling_products_reports",
              "Access Top Selling Products Reports",
              "User can access top selling products reports");
      var accessCustomerRankingsReports =
          new Permission(
              "access_customer_rankings_reports",
              "Access Customer Rankings Reports",
              "User can access customer rankings reports");
      var accessUsersReports =
          new Permission(
              "access_users_reports", "Access Users Reports", "User can access users reports");
      var accessStocksReports =
          new Permission(
              "access_stocks_reports", "Access Stocks Reports", "User can access stocks reports");
      var accessProductsReports =
          new Permission(
              "access_products_reports",
              "Access Products Reports",
              "User can access products reports");
      var accessProductSalesReports =
          new Permission(
              "access_product_sales_reports",
              "Access Product Sales Reports",
              "User can access product sales reports");
      var accessProductPurchasesReports =
          new Permission(
              "access_product_purchases_reports",
              "Access Product Purchases Reports",
              "User can access product purchases reports");

      permissionDao.createOrUpdate(accessPaymentsSalesReports);
      permissionDao.createOrUpdate(accessPaymentsPurchasesReports);
      permissionDao.createOrUpdate(accessSalesReturnsPaymentsReports);
      permissionDao.createOrUpdate(accessPurchasesReturnsPaymentsReports);
      permissionDao.createOrUpdate(accessSalesReports);
      permissionDao.createOrUpdate(accessPurchasesReports);
      permissionDao.createOrUpdate(accessCustomersReports);
      permissionDao.createOrUpdate(accessSuppliersReports);
      permissionDao.createOrUpdate(accessProfitsAndLossesReports);
      permissionDao.createOrUpdate(accessProductQuantityReports);
      permissionDao.createOrUpdate(accessBranchStockChartsReports);
      permissionDao.createOrUpdate(accessTopSellingProductsReports);
      permissionDao.createOrUpdate(accessCustomerRankingsReports);
      permissionDao.createOrUpdate(accessUsersReports);
      permissionDao.createOrUpdate(accessStocksReports);
      permissionDao.createOrUpdate(accessProductsReports);
      permissionDao.createOrUpdate(accessProductSalesReports);
      permissionDao.createOrUpdate(accessProductPurchasesReports);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }
}
