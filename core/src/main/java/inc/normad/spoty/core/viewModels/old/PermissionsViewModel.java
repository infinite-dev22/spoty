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

package inc.normad.spoty.core.viewModels.old;

import inc.normad.spoty.core.views.forms.RoleSettingsFormController;
import inc.normad.spoty.network_bridge.dtos.Permission;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.Nullable;


public class PermissionsViewModel {
    private static final ObjectProperty<Permission> dashboardAccess = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> posAccess = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> createPurchases = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> viewPurchases = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> editPurchases = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> deletePurchases = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> createAdjustments = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> viewAdjustments = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> editAdjustments = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> deleteAdjustments = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> createTransfers = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> viewTransfers = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> editTransfers = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> deleteTransfers = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> createQuotations = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> viewQuotations = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> editQuotations = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> deleteQuotations = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> createSaleReturns = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> viewSaleReturns = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> editSaleReturns = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> deleteSaleReturns = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> createPurchaseReturns =
            new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> viewPurchaseReturns =
            new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> editPurchaseReturns =
            new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> deletePurchaseReturns =
            new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> createExpenses = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> viewExpenses = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> editExpenses = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> deleteExpenses = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> createPermissions = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> viewPermissions = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> editPermissions = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> deletePermissions = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> createUsers = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> viewUsers = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> editUsers = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> deleteUsers = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> createSales = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> viewSales = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> editSales = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> deleteSales = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> viewSystemSettings = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> editSystemSettings = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> viewPOSSettings = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> editPOSSettings = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> accessCurrencySettings =
            new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> viewCurrency = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> createCurrency = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> editCurrency = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> deleteCurrency = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> accessBranchSettings =
            new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> viewStockIn = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> createStockIn = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> editStockIn = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> deleteStockIn = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> viewRequisition = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> createRequisition = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> editRequisition = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> deleteRequisition = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> viewBranch = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> createBranch = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> editBranch = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> deleteBranch = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> viewBackupSettings = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> createBackup = new SimpleObjectProperty<>();
    // Products Permissions.
    private static final ObjectProperty<Permission> createProducts = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> viewProducts = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> editProducts = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> deleteProducts = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> importProducts = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> accessProductCategories =
            new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> accessBrands = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> accessUnitsOfMeasure =
            new SimpleObjectProperty<>();
    // Customer Permissions.
    private static final ObjectProperty<Permission> createCustomers = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> viewCustomers = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> editCustomers = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> deleteCustomers = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> importCustomers = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> createSuppliers = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> viewSuppliers = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> editSuppliers = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> deleteSuppliers = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> importSuppliers = new SimpleObjectProperty<>();
    // Reports Permissions.
    private static final ObjectProperty<Permission> accessPaymentsSalesReports =
            new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> accessPaymentsPurchasesReports =
            new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> accessSalesReturnsPaymentsReports =
            new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> accessPurchasesReturnsPaymentsReports =
            new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> accessSalesReports = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> accessPurchasesReports =
            new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> accessCustomersReports =
            new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> accessSuppliersReports =
            new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> accessProfitsAndLossesReports =
            new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> accessProductQuantityReports =
            new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> accessBranchStockChartsReports =
            new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> accessTopSellingProductsReports =
            new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> accessCustomerRankingsReports =
            new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> accessUsersReports = new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> accessStocksReports =
            new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> accessProductsReports =
            new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> accessProductSalesReports =
            new SimpleObjectProperty<>();
    private static final ObjectProperty<Permission> accessProductPurchasesReports =
            new SimpleObjectProperty<>();
    private static final ObservableList<Permission> permissionsList =
            FXCollections.observableArrayList();

    public static Permission getDashboardAccess() {
        return dashboardAccess.get();
    }

    public static void addPermission(Permission permission) {
        permissionsList.addAll(permission);
    }

    public static void removePermission(Permission permission) {
        permissionsList.remove(permission);
    }

    public static ObservableList<Permission> getPermissionsList() {
        return permissionsList;
    }

    public static void setDashboardAccess() {
        PermissionsViewModel.dashboardAccess.set(selectWhere("access_dashboard"));
    }

    public static Permission getPosAccess() {
        return posAccess.get();
    }

    public static void setPosAccess() {
        PermissionsViewModel.posAccess.set(selectWhere("access_pos"));
    }

    public static Permission getCreatePurchases() {
        return createPurchases.get();
    }

    public static void setCreatePurchases() {
        PermissionsViewModel.createPurchases.set(selectWhere("create_purchases"));
    }

    public static Permission getCreateAdjustments() {
        return createAdjustments.get();
    }

    public static void setCreateAdjustments() {
        PermissionsViewModel.createAdjustments.set(selectWhere("create_adjustments"));
    }

    public static Permission getCreateQuotations() {
        return createQuotations.get();
    }

    public static void setCreateQuotations() {
        PermissionsViewModel.createQuotations.set(selectWhere("create_quotations"));
    }

    public static Permission getCreateSaleReturns() {
        return createSaleReturns.get();
    }

    public static void setCreateSaleReturns() {
        PermissionsViewModel.createSaleReturns.set(selectWhere("create_sale_returns"));
    }

    public static Permission getCreateTransfers() {
        return createTransfers.get();
    }

    public static void setCreateTransfers() {
        PermissionsViewModel.createTransfers.set(selectWhere("create_transfers"));
    }

    public static Permission getDeleteAdjustments() {
        return deleteAdjustments.get();
    }

    public static void setDeleteAdjustments() {
        PermissionsViewModel.deleteAdjustments.set(selectWhere("delete_adjustments"));
    }

    public static Permission getViewPurchases() {
        return viewPurchases.get();
    }

    public static void setViewPurchases() {
        PermissionsViewModel.viewPurchases.set(selectWhere("view_purchases"));
    }

    public static Permission getDeletePurchases() {
        return deletePurchases.get();
    }

    public static void setDeletePurchases() {
        PermissionsViewModel.deletePurchases.set(selectWhere("delete_purchases"));
    }

    public static Permission getEditPurchases() {
        return editPurchases.get();
    }

    public static void setEditPurchases() {
        PermissionsViewModel.editPurchases.set(selectWhere("edit_purchases"));
    }

    public static Permission getCreatePurchaseReturns() {
        return createPurchaseReturns.get();
    }

    public static void setCreatePurchaseReturns() {
        PermissionsViewModel.createPurchaseReturns.set(selectWhere("create_purchase_returns"));
    }

    public static Permission getDeleteQuotations() {
        return deleteQuotations.get();
    }

    public static void setDeleteQuotations() {
        PermissionsViewModel.deleteQuotations.set(selectWhere("delete_quotations"));
    }

    public static Permission getCreateExpenses() {
        return createExpenses.get();
    }

    public static void setCreateExpenses() {
        PermissionsViewModel.createExpenses.set(selectWhere("create_expenses"));
    }

    public static Permission getDeletePurchaseReturns() {
        return deletePurchaseReturns.get();
    }

    public static void setDeletePurchaseReturns() {
        PermissionsViewModel.deletePurchaseReturns.set(selectWhere("delete_purchase_returns"));
    }

    public static Permission getDeleteSaleReturns() {
        return deleteSaleReturns.get();
    }

    public static void setDeleteSaleReturns() {
        PermissionsViewModel.deleteSaleReturns.set(selectWhere("delete_sale_returns"));
    }

    public static Permission getCreatePermissions() {
        return createPermissions.get();
    }

    public static void setCreatePermissions() {
        PermissionsViewModel.createPermissions.set(selectWhere("create_permissions"));
    }

    public static Permission getDeleteExpenses() {
        return deleteExpenses.get();
    }

    public static void setDeleteExpenses() {
        PermissionsViewModel.deleteExpenses.set(selectWhere("delete_expenses"));
    }

    public static Permission getDeleteTransfers() {
        return deleteTransfers.get();
    }

    public static void setDeleteTransfers() {
        PermissionsViewModel.deleteTransfers.set(selectWhere("delete_transfers"));
    }

    public static Permission getEditAdjustments() {
        return editAdjustments.get();
    }

    public static void setEditAdjustments() {
        PermissionsViewModel.editAdjustments.set(selectWhere("edit_adjustments"));
    }

    public static Permission getEditPurchaseReturns() {
        return editPurchaseReturns.get();
    }

    public static void setEditPurchaseReturns() {
        PermissionsViewModel.editPurchaseReturns.set(selectWhere("edit_purchase_returns"));
    }

    public static Permission getEditQuotations() {
        return editQuotations.get();
    }

    public static void setEditQuotations() {
        PermissionsViewModel.editQuotations.set(selectWhere("edit_quotations"));
    }

    public static Permission getEditSaleReturns() {
        return editSaleReturns.get();
    }

    public static void setEditSaleReturns() {
        PermissionsViewModel.editSaleReturns.set(selectWhere("edit_sale_returns"));
    }

    public static Permission getEditExpenses() {
        return editExpenses.get();
    }

    public static void setEditExpenses() {
        PermissionsViewModel.editExpenses.set(selectWhere("edit_expenses"));
    }

    public static Permission getEditTransfers() {
        return editTransfers.get();
    }

    public static void setEditTransfers() {
        PermissionsViewModel.editTransfers.set(selectWhere("edit_transfers"));
    }

    public static Permission getViewAdjustments() {
        return viewAdjustments.get();
    }

    public static void setViewAdjustments() {
        PermissionsViewModel.viewAdjustments.set(selectWhere("view_adjustments"));
    }

    public static Permission getViewExpenses() {
        return viewExpenses.get();
    }

    public static void setViewExpenses() {
        PermissionsViewModel.viewExpenses.set(selectWhere("view_expenses"));
    }

    public static Permission getViewPurchaseReturns() {
        return viewPurchaseReturns.get();
    }

    public static void setViewPurchaseReturns() {
        PermissionsViewModel.viewPurchaseReturns.set(selectWhere("view_purchase_returns"));
    }

    public static Permission getViewPermissions() {
        return viewPermissions.get();
    }

    public static void setViewPermissions() {
        PermissionsViewModel.viewPermissions.set(selectWhere("view_permissions"));
    }

    public static Permission getViewQuotations() {
        return viewQuotations.get();
    }

    public static void setViewQuotations() {
        PermissionsViewModel.viewQuotations.set(selectWhere("view_quotations"));
    }

    public static Permission getCreateUsers() {
        return createUsers.get();
    }

    public static void setCreateUsers() {
        PermissionsViewModel.createUsers.set(selectWhere("create_users"));
    }

    public static Permission getDeletePermissions() {
        return deletePermissions.get();
    }

    public static void setDeletePermissions() {
        PermissionsViewModel.deletePermissions.set(selectWhere("delete_permissions"));
    }

    public static Permission getViewSaleReturns() {
        return viewSaleReturns.get();
    }

    public static void setViewSaleReturns() {
        PermissionsViewModel.viewSaleReturns.set(selectWhere("view_sale_returns"));
    }

    public static Permission getEditPermissions() {
        return editPermissions.get();
    }

    public static void setEditPermissions() {
        PermissionsViewModel.editPermissions.set(selectWhere("edit_permissions"));
    }

    public static Permission getViewTransfers() {
        return viewTransfers.get();
    }

    public static void setViewTransfers() {
        PermissionsViewModel.viewTransfers.set(selectWhere("view_transfers"));
    }

    public static Permission getAccessCurrencySettings() {
        return accessCurrencySettings.get();
    }

    public static void setAccessCurrencySettings() {
        PermissionsViewModel.accessCurrencySettings.set(selectWhere("access_currency_settings"));
    }

    public static Permission getAccessBranchSettings() {
        return accessBranchSettings.get();
    }

    public static void setAccessBranchSettings() {
        PermissionsViewModel.accessBranchSettings.set(selectWhere("access_branch_settings"));
    }

    public static Permission getCreateBranch() {
        return createBranch.get();
    }

    public static void setCreateBranch() {
        PermissionsViewModel.createBranch.set(selectWhere("create_branches"));
    }

    public static void setCreateBackup() {
        PermissionsViewModel.createBackup.set(selectWhere("create_backup"));
    }

    public static Permission getCreateCurrency() {
        return createCurrency.get();
    }

    public static void setCreateCurrency() {
        PermissionsViewModel.createCurrency.set(selectWhere("create_currencies"));
    }

    public static Permission getCreateSales() {
        return createSales.get();
    }

    public static void setCreateSales() {
        PermissionsViewModel.createSales.set(selectWhere("create_sales"));
    }

    public static Permission getDeleteCurrency() {
        return deleteCurrency.get();
    }

    public static void setDeleteCurrency() {
        PermissionsViewModel.deleteCurrency.set(selectWhere("delete_currencies"));
    }

    public static Permission getDeleteSales() {
        return deleteSales.get();
    }

    public static void setDeleteSales() {
        PermissionsViewModel.deleteSales.set(selectWhere("delete_sales"));
    }

    public static Permission getDeleteUsers() {
        return deleteUsers.get();
    }

    public static void setDeleteUsers() {
        PermissionsViewModel.deleteUsers.set(selectWhere("delete_users"));
    }

    public static Permission getCreateProducts() {
        return createProducts.get();
    }

    public static void setCreateProducts() {
        PermissionsViewModel.createProducts.set(selectWhere("create_products"));
    }

    public static Permission getDeleteBranch() {
        return deleteBranch.get();
    }

    public static void setDeleteBranch() {
        PermissionsViewModel.deleteBranch.set(selectWhere("delete_branches"));
    }

    public static Permission getEditBranch() {
        return editBranch.get();
    }

    public static void setEditBranch() {
        PermissionsViewModel.editBranch.set(selectWhere("edit_branches"));
    }

    public static Permission getEditCurrency() {
        return editCurrency.get();
    }

    public static void setEditCurrency() {
        PermissionsViewModel.editCurrency.set(selectWhere("edit_currencies"));
    }

    public static Permission getEditPOSSettings() {
        return editPOSSettings.get();
    }

    public static void setEditPOSSettings() {
        PermissionsViewModel.editPOSSettings.set(selectWhere("edit_pos_settings"));
    }

    public static Permission getEditSales() {
        return editSales.get();
    }

    public static void setEditSales() {
        PermissionsViewModel.editSales.set(selectWhere("edit_sales"));
    }

    public static Permission getEditSystemSettings() {
        return editSystemSettings.get();
    }

    public static void setEditSystemSettings() {
        PermissionsViewModel.editSystemSettings.set(selectWhere("edit_system_settings"));
    }

    public static Permission getEditUsers() {
        return editUsers.get();
    }

    public static void setEditUsers() {
        PermissionsViewModel.editUsers.set(selectWhere("edit_users"));
    }

    public static Permission getViewBackupSettings() {
        return viewBackupSettings.get();
    }

    public static void setViewBackupSettings() {
        PermissionsViewModel.viewBackupSettings.set(selectWhere("view_backup_settings"));
    }

    public static Permission getViewBranch() {
        return viewBranch.get();
    }

    public static void setViewBranch() {
        PermissionsViewModel.viewBranch.set(selectWhere("view_branches"));
    }

    public static Permission getViewCurrency() {
        return viewCurrency.get();
    }

    public static void setViewCurrency() {
        PermissionsViewModel.viewCurrency.set(selectWhere("view_currencies"));
    }

    public static Permission getViewPOSSettings() {
        return viewPOSSettings.get();
    }

    public static void setViewPOSSettings() {
        PermissionsViewModel.viewPOSSettings.set(selectWhere("view_pos_settings"));
    }

    public static Permission getViewSales() {
        return viewSales.get();
    }

    public static void setViewSales() {
        PermissionsViewModel.viewSales.set(selectWhere("view_sales"));
    }

    public static Permission getViewSystemSettings() {
        return viewSystemSettings.get();
    }

    public static void setViewSystemSettings() {
        PermissionsViewModel.viewSystemSettings.set(selectWhere("view_system_settings"));
    }

    public static Permission getViewUsers() {
        return viewUsers.get();
    }

    public static void setViewUsers() {
        PermissionsViewModel.viewUsers.set(selectWhere("view_users"));
    }

    public static Permission getAccessBrands() {
        return accessBrands.get();
    }

    public static void setAccessBrands() {
        PermissionsViewModel.accessBrands.set(selectWhere("access_brands"));
    }

    public static Permission getAccessPaymentsPurchasesReports() {
        return accessPaymentsPurchasesReports.get();
    }

    public static void setAccessPaymentsPurchasesReports() {
        PermissionsViewModel.accessPaymentsPurchasesReports.set(
                selectWhere("access_payments_purchases_reports"));
    }

    public static Permission getAccessPaymentsSalesReports() {
        return accessPaymentsSalesReports.get();
    }

    public static void setAccessPaymentsSalesReports() {
        PermissionsViewModel.accessPaymentsSalesReports.set(
                selectWhere("access_payments_sales_reports"));
    }

    public static Permission getAccessProductCategories() {
        return accessProductCategories.get();
    }

    public static void setAccessProductCategories() {
        PermissionsViewModel.accessProductCategories.set(selectWhere("access_product_categories"));
    }

    public static Permission getAccessUnitsOfMeasure() {
        return accessUnitsOfMeasure.get();
    }

    public static void setAccessUnitsOfMeasure() {
        PermissionsViewModel.accessUnitsOfMeasure.set(selectWhere("access_units_of_measure"));
    }

    public static Permission getAccessPurchasesReports() {
        return accessPurchasesReports.get();
    }

    public static void setAccessPurchasesReports() {
        PermissionsViewModel.accessPurchasesReports.set(selectWhere("access_purchases_reports"));
    }

    public static Permission getAccessPurchasesReturnsPaymentsReports() {
        return accessPurchasesReturnsPaymentsReports.get();
    }

    public static void setAccessPurchasesReturnsPaymentsReports() {
        PermissionsViewModel.accessPurchasesReturnsPaymentsReports.set(
                selectWhere("access_purchases_returns_payments_reports"));
    }

    public static Permission getAccessCustomersReports() {
        return accessCustomersReports.get();
    }

    public static void setAccessCustomersReports() {
        PermissionsViewModel.accessCustomersReports.set(selectWhere("access_customers_reports"));
    }

    public static Permission getAccessSalesReports() {
        return accessSalesReports.get();
    }

    public static void setAccessSalesReports() {
        PermissionsViewModel.accessSalesReports.set(selectWhere("access_sales_reports"));
    }

    public static Permission getAccessSalesReturnsPaymentsReports() {
        return accessSalesReturnsPaymentsReports.get();
    }

    public static void setAccessSalesReturnsPaymentsReports() {
        PermissionsViewModel.accessSalesReturnsPaymentsReports.set(
                selectWhere("access_sales_returns_payments_reports"));
    }

    public static Permission getCreateCustomers() {
        return createCustomers.get();
    }

    public static void setCreateCustomers() {
        PermissionsViewModel.createCustomers.set(selectWhere("create_customers"));
    }

    public static Permission getCreateSuppliers() {
        return createSuppliers.get();
    }

    public static void setCreateSuppliers() {
        PermissionsViewModel.createSuppliers.set(selectWhere("create_suppliers"));
    }

    public static Permission getDeleteCustomers() {
        return deleteCustomers.get();
    }

    public static void setDeleteCustomers() {
        PermissionsViewModel.deleteCustomers.set(selectWhere("delete_customers"));
    }

    public static Permission getAccessBranchStockChartsReports() {
        return accessBranchStockChartsReports.get();
    }

    public static void setAccessBranchStockChartsReports() {
        PermissionsViewModel.accessBranchStockChartsReports.set(
                selectWhere("access_branch_stock_charts_reports"));
    }

    public static Permission getDeleteProducts() {
        return deleteProducts.get();
    }

    public static void setDeleteProducts() {
        PermissionsViewModel.deleteProducts.set(selectWhere("delete_products"));
    }

    public static Permission getDeleteSuppliers() {
        return deleteSuppliers.get();
    }

    public static void setDeleteSuppliers() {
        PermissionsViewModel.deleteSuppliers.set(selectWhere("delete_suppliers"));
    }

    public static Permission getAccessProductQuantityReports() {
        return accessProductQuantityReports.get();
    }

    public static void setAccessProductQuantityReports() {
        PermissionsViewModel.accessProductQuantityReports.set(
                selectWhere("access_product_quantity_reports"));
    }

    public static Permission getEditCustomers() {
        return editCustomers.get();
    }

    public static void setEditCustomers() {
        PermissionsViewModel.editCustomers.set(selectWhere("edit_customers"));
    }

    public static Permission getEditProducts() {
        return editProducts.get();
    }

    public static void setEditProducts() {
        PermissionsViewModel.editProducts.set(selectWhere("edit_products"));
    }

    public static Permission getEditSuppliers() {
        return editSuppliers.get();
    }

    public static void setEditSuppliers() {
        PermissionsViewModel.editSuppliers.set(selectWhere("edit_suppliers"));
    }

    public static Permission getAccessSuppliersReports() {
        return accessSuppliersReports.get();
    }

    public static void setAccessSuppliersReports() {
        PermissionsViewModel.accessSuppliersReports.set(selectWhere("access_suppliers_reports"));
    }

    public static Permission getImportCustomers() {
        return importCustomers.get();
    }

    public static void setImportCustomers() {
        PermissionsViewModel.importCustomers.set(selectWhere("import_customers"));
    }

    public static Permission getImportProducts() {
        return importProducts.get();
    }

    public static void setImportProducts() {
        PermissionsViewModel.importProducts.set(selectWhere("import_products"));
    }

    public static Permission getImportSuppliers() {
        return importSuppliers.get();
    }

    public static void setImportSuppliers() {
        PermissionsViewModel.importSuppliers.set(selectWhere("import_suppliers"));
    }

    public static Permission getViewCustomers() {
        return viewCustomers.get();
    }

    public static void setViewCustomers() {
        PermissionsViewModel.viewCustomers.set(selectWhere("view_customers"));
    }

    public static Permission getViewProducts() {
        return viewProducts.get();
    }

    public static void setViewProducts() {
        PermissionsViewModel.viewProducts.set(selectWhere("view_products"));
    }

    public static Permission getAccessCustomerRankingsReports() {
        return accessCustomerRankingsReports.get();
    }

    public static void setAccessCustomerRankingsReports() {
        PermissionsViewModel.accessCustomerRankingsReports.set(
                selectWhere("access_customer_rankings_reports"));
    }

    public static Permission getAccessProductSalesReports() {
        return accessProductSalesReports.get();
    }

    public static void setAccessProductSalesReports() {
        PermissionsViewModel.accessProductSalesReports.set(selectWhere("access_product_sales_reports"));
    }

    public static Permission getAccessProfitsAndLossesReports() {
        return accessProfitsAndLossesReports.get();
    }

    public static void setAccessProfitsAndLossesReports() {
        PermissionsViewModel.accessProfitsAndLossesReports.set(
                selectWhere("access_profits_and_losses_reports"));
    }

    public static Permission getAccessProductPurchasesReports() {
        return accessProductPurchasesReports.get();
    }

    public static void setAccessProductPurchasesReports() {
        PermissionsViewModel.accessProductPurchasesReports.set(
                selectWhere("access_product_purchases_reports"));
    }

    public static Permission getAccessProductsReports() {
        return accessProductsReports.get();
    }

    public static void setAccessProductsReports() {
        PermissionsViewModel.accessProductsReports.set(selectWhere("access_products_reports"));
    }

    public static Permission getAccessStocksReports() {
        return accessStocksReports.get();
    }

    public static void setAccessStocksReports() {
        PermissionsViewModel.accessStocksReports.set(selectWhere("access_stocks_reports"));
    }

    public static Permission getAccessTopSellingProductsReports() {
        return accessTopSellingProductsReports.get();
    }

    public static void setAccessTopSellingProductsReports() {
        PermissionsViewModel.accessTopSellingProductsReports.set(
                selectWhere("access_top_selling_products_reports"));
    }

    public static Permission getAccessUsersReports() {
        return accessUsersReports.get();
    }

    public static void setAccessUsersReports() {
        PermissionsViewModel.accessUsersReports.set(selectWhere("access_users_reports"));
    }

    public static Permission getViewSuppliers() {
        return viewSuppliers.get();
    }

    public static void setViewSuppliers() {
        PermissionsViewModel.viewSuppliers.set(selectWhere("view_suppliers"));
    }

    public static Permission getViewStockIn() {
        return viewStockIn.get();
    }

    public static Permission getCreateStockIn() {
        return createStockIn.get();
    }

    public static Permission getDeleteStockIn() {
        return deleteStockIn.get();
    }

    public static Permission getEditStockIn() {
        return editStockIn.get();
    }

    public static Permission getViewRequisition() {
        return viewRequisition.get();
    }

    public static Permission getCreateRequisition() {
        return createRequisition.get();
    }

    public static Permission getDeleteRequisition() {
        return deleteRequisition.get();
    }

    public static Permission getEditRequisition() {
        return editRequisition.get();
    }

    public static void setViewStockIn() {
        PermissionsViewModel.viewStockIn.set(selectWhere("view_stock_ins"));
    }

    public static void setCreateStockIn() {
        PermissionsViewModel.createStockIn.set(selectWhere("create_stock_ins"));
    }

    public static void setEditStockIn() {
        PermissionsViewModel.editStockIn.set(selectWhere("edit_stock_ins"));
    }

    public static void setDeleteStockIn() {
        PermissionsViewModel.deleteStockIn.set(selectWhere("delete_stock_ins"));
    }

    public static void setViewRequisition() {
        PermissionsViewModel.viewRequisition.set(selectWhere("view_requisitions"));
    }

    public static void setCreateRequisition() {
        PermissionsViewModel.createRequisition.set(selectWhere("create_requisitions"));
    }

    public static void setEditRequisition() {
        PermissionsViewModel.editRequisition.set(selectWhere("edit_requisitions"));
    }

    public static void setDeleteRequisition() {
        PermissionsViewModel.deleteRequisition.set(selectWhere("delete_requisitions"));
    }

    public static @Nullable Permission selectWhere(String itemIs) {
//        if (connection == null) {
//            connection = SQLiteConnection.getInstance();
//        }
//        if (connectionSource == null) {
//            connectionSource = connection.getConnection();
//        }
//
//        try {
//            if (dao == null) {
//                dao = DaoManager.createDao(connectionSource, Permission.class);
//            }
//            return dao.queryBuilder().where().eq("name", itemIs).queryForFirst();
//        } catch (Exception e) {
//            SpotyLogger.writeToFile(e, PermissionsViewModel.class);
//        }
        return null;
    }

    public static void resetPermissionsProperties() {
        RoleSettingsFormController.getInstance().resetCheckboxes();
    }
}
