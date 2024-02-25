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

package inc.normad.spoty.core.views.forms;

import inc.normad.spoty.core.components.navigation.Navigation;
import inc.normad.spoty.core.components.navigation.Pages;
import inc.normad.spoty.core.components.notification.SimpleNotification;
import inc.normad.spoty.core.components.notification.SimpleNotificationHolder;
import inc.normad.spoty.core.components.notification.enums.NotificationDuration;
import inc.normad.spoty.core.components.notification.enums.NotificationVariants;
import inc.normad.spoty.core.viewModels.PermissionsViewModel;
import inc.normad.spoty.core.viewModels.RoleViewModel;
import inc.normad.spoty.utils.SpotyLogger;
import inc.normad.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import io.github.palexdev.mfxcomponents.controls.checkbox.MFXCheckbox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

public class RoleSettingsFormController implements Initializable {
    private static RoleSettingsFormController instance;
    @FXML
    public MFXTextField roleDescriptionInputField;
    // <editor-fold desc="Lots of MFXCheckboxes here 👇️">
    @FXML
    private MFXCheckbox dashboardCheckbox, accessPOSCheckbox;
    // Users
    @FXML
    private MFXCheckbox viewUsersCheckbox, editUsersCheckbox, createUserCheckbox, deleteUserCheckbox;
    // User Permissions
    @FXML
    private MFXCheckbox viewRoleCheckbox, editRoleCheckbox, createRoleCheckbox, deleteRoleCheckbox;
    // Products
    @FXML
    private MFXCheckbox viewProductsCheckbox,
            editProductsCheckbox,
            createProductCheckbox,
            deleteProductCheckbox,
            productBarcodesCheckbox,
            productCategoriesCheckbox,
            productUnitsCheckbox,
            productImportsCheckbox,
            productBrandsCheckbox;
    // Adjustments
    @FXML
    private MFXCheckbox viewAdjustmentsCheckbox,
            editAdjustmentsCheckbox,
            createAdjustmentCheckbox,
            deleteAdjustmentCheckbox;
    // Transfers
    @FXML
    private MFXCheckbox viewTransfersCheckbox,
            editTransfersCheckbox,
            createTransferCheckbox,
            deleteTransferCheckbox;
    // Expenses
    @FXML
    private MFXCheckbox viewExpensesCheckbox,
            editExpensesCheckbox,
            createExpenseCheckbox,
            deleteExpenseCheckbox;
    // Sales
    @FXML
    private MFXCheckbox viewSalesCheckbox, editSalesCheckbox, createSaleCheckbox, deleteSaleCheckbox;
    // Purchases
    @FXML
    private MFXCheckbox viewPurchasesCheckbox,
            editPurchasesCheckbox,
            createPurchaseCheckbox,
            deletePurchaseCheckbox;
    // Requisitions
    @FXML
    private MFXCheckbox viewRequisitionCheckbox,
            editRequisitionCheckbox,
            createRequisitionCheckbox,
            deleteRequisitionCheckbox;
    // StockIns
    @FXML
    private MFXCheckbox viewStockInCheckbox,
            editStockInCheckbox,
            createStockInCheckbox,
            deleteStockInCheckbox;
    // Quotations
    @FXML
    private MFXCheckbox viewQuotationsCheckbox,
            editQuotationsCheckbox,
            createQuotationCheckbox,
            deleteQuotationCheckbox;
    // Sale Returns
    @FXML
    private MFXCheckbox viewSaleReturnsCheckbox,
            editSaleReturnsCheckbox,
            createSaleReturnCheckbox,
            deleteSaleReturnCheckbox;
    // Purchase Returns
    @FXML
    private MFXCheckbox viewPurchaseReturnsCheckbox,
            editPurchaseReturnsCheckbox,
            createPurchaseReturnCheckbox,
            deletePurchaseReturnCheckbox;
    // Customers
    @FXML
    private MFXCheckbox viewCustomersCheckbox,
            editCustomersCheckbox,
            createCustomerCheckbox,
            deleteCustomerCheckbox,
            importCustomersCheckbox;
    // Suppliers
    @FXML
    private MFXCheckbox viewSuppliersCheckbox,
            editSuppliersCheckbox,
            createSupplierCheckbox,
            deleteSupplierCheckbox,
            importSuppliersCheckbox;
    // Reports
    @FXML
    private MFXCheckbox paymentSalesCheckbox,
            paymentPurchasesCheckbox,
            saleReturnPaymentsCheckbox,
            purchaseReturnPaymentsCheckbox,
            saleReportCheckbox,
            purchaseReportCheckbox,
            customerReportCheckbox,
            supplierReportCheckbox,
            profitAndLossCheckbox,
            productQuantityAlertsCheckbox,
            branchStockChartCheckbox,
            topSellingProductsCheckbox,
            customerRankingCheckbox,
            usersReportCheckbox,
            stockReportCheckbox,
            productReportCheckbox,
            productSalesReportCheckbox,
            productPurchasesReportCheckbox;
    // Settings
    @FXML
    private MFXCheckbox accessSystemSettingCheckbox,
            AccessPOSSettingsCheckbox,
            accessCurrencyCheckbox,
            accessBranchCheckbox,
            accessBackupCheckbox;
    // Select All.
    @FXML
    private MFXCheckbox selectAllRequisitionsCheckbox,
            selectAllPurchasesCheckbox,
            selectAllTransfersCheckbox,
            selectAllStockInsCheckbox,
            selectAllQuotationsCheckbox,
            selectAllSalesCheckbox,
            selectAllExpensesCheckbox,
            selectAllAdjustmentsCheckbox,
            selectAllPurchasesReturnsCheckbox,
            selectAllRolesCheckbox,
            selectAllUserManagementsCheckbox,
            selectAllSalesReturnsCheckbox,
            selectAllSettingsCheckbox,
            selectAllSuppliersCheckbox,
            selectAllCustomersCheckbox,
            selectAllProductsCheckbox,
            selectAllReportsCheckbox;
    private List<Constraint> constraints;
    @FXML
    private Label errorLabel;
    @FXML
    private BorderPane roleSettingsHolder;
    @FXML
    private MFXTextField roleNameInputField;

    public RoleSettingsFormController() {
    }
    // </editor-fold>

    public static RoleSettingsFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new RoleSettingsFormController();
        return instance;
    }

    public void resetCheckboxes() {
        dashboardCheckbox.setSelected(false);
        accessPOSCheckbox.setSelected(false);
        viewUsersCheckbox.setSelected(false);
        editUsersCheckbox.setSelected(false);
        createUserCheckbox.setSelected(false);
        deleteUserCheckbox.setSelected(false);
        viewRoleCheckbox.setSelected(false);
        editRoleCheckbox.setSelected(false);
        createRoleCheckbox.setSelected(false);
        deleteRoleCheckbox.setSelected(false);
        viewProductsCheckbox.setSelected(false);
        editProductsCheckbox.setSelected(false);
        createProductCheckbox.setSelected(false);
        deleteProductCheckbox.setSelected(false);
        productBarcodesCheckbox.setSelected(false);
        productCategoriesCheckbox.setSelected(false);
        productUnitsCheckbox.setSelected(false);
        productImportsCheckbox.setSelected(false);
        productBrandsCheckbox.setSelected(false);
        viewAdjustmentsCheckbox.setSelected(false);
        editAdjustmentsCheckbox.setSelected(false);
        createAdjustmentCheckbox.setSelected(false);
        deleteAdjustmentCheckbox.setSelected(false);
        viewTransfersCheckbox.setSelected(false);
        editTransfersCheckbox.setSelected(false);
        createTransferCheckbox.setSelected(false);
        deleteTransferCheckbox.setSelected(false);
        viewExpensesCheckbox.setSelected(false);
        editExpensesCheckbox.setSelected(false);
        createExpenseCheckbox.setSelected(false);
        deleteExpenseCheckbox.setSelected(false);
        viewSalesCheckbox.setSelected(false);
        editSalesCheckbox.setSelected(false);
        createSaleCheckbox.setSelected(false);
        deleteSaleCheckbox.setSelected(false);
        viewPurchasesCheckbox.setSelected(false);
        editPurchasesCheckbox.setSelected(false);
        createPurchaseCheckbox.setSelected(false);
        deletePurchaseCheckbox.setSelected(false);
        viewRequisitionCheckbox.setSelected(false);
        editRequisitionCheckbox.setSelected(false);
        createRequisitionCheckbox.setSelected(false);
        deleteRequisitionCheckbox.setSelected(false);
        viewStockInCheckbox.setSelected(false);
        editStockInCheckbox.setSelected(false);
        createStockInCheckbox.setSelected(false);
        deleteStockInCheckbox.setSelected(false);
        viewQuotationsCheckbox.setSelected(false);
        editQuotationsCheckbox.setSelected(false);
        createQuotationCheckbox.setSelected(false);
        deleteQuotationCheckbox.setSelected(false);
        viewSaleReturnsCheckbox.setSelected(false);
        editSaleReturnsCheckbox.setSelected(false);
        createSaleReturnCheckbox.setSelected(false);
        deleteSaleReturnCheckbox.setSelected(false);
        viewPurchaseReturnsCheckbox.setSelected(false);
        editPurchaseReturnsCheckbox.setSelected(false);
        createPurchaseReturnCheckbox.setSelected(false);
        deletePurchaseReturnCheckbox.setSelected(false);
        viewCustomersCheckbox.setSelected(false);
        editCustomersCheckbox.setSelected(false);
        createCustomerCheckbox.setSelected(false);
        deleteCustomerCheckbox.setSelected(false);
        importCustomersCheckbox.setSelected(false);
        viewSuppliersCheckbox.setSelected(false);
        editSuppliersCheckbox.setSelected(false);
        createSupplierCheckbox.setSelected(false);
        deleteSupplierCheckbox.setSelected(false);
        importSuppliersCheckbox.setSelected(false);
        paymentSalesCheckbox.setSelected(false);
        paymentPurchasesCheckbox.setSelected(false);
        saleReturnPaymentsCheckbox.setSelected(false);
        purchaseReturnPaymentsCheckbox.setSelected(false);
        saleReportCheckbox.setSelected(false);
        purchaseReportCheckbox.setSelected(false);
        customerReportCheckbox.setSelected(false);
        supplierReportCheckbox.setSelected(false);
        profitAndLossCheckbox.setSelected(false);
        productQuantityAlertsCheckbox.setSelected(false);
        branchStockChartCheckbox.setSelected(false);
        topSellingProductsCheckbox.setSelected(false);
        customerRankingCheckbox.setSelected(false);
        usersReportCheckbox.setSelected(false);
        stockReportCheckbox.setSelected(false);
        productReportCheckbox.setSelected(false);
        productSalesReportCheckbox.setSelected(false);
        productPurchasesReportCheckbox.setSelected(false);
        accessSystemSettingCheckbox.setSelected(false);
        AccessPOSSettingsCheckbox.setSelected(false);
        accessCurrencyCheckbox.setSelected(false);
        accessBranchCheckbox.setSelected(false);
        accessBackupCheckbox.setSelected(false);
        selectAllRequisitionsCheckbox.setSelected(false);
        selectAllPurchasesCheckbox.setSelected(false);
        selectAllTransfersCheckbox.setSelected(false);
        selectAllStockInsCheckbox.setSelected(false);
        selectAllQuotationsCheckbox.setSelected(false);
        selectAllSalesCheckbox.setSelected(false);
        selectAllExpensesCheckbox.setSelected(false);
        selectAllAdjustmentsCheckbox.setSelected(false);
        selectAllPurchasesReturnsCheckbox.setSelected(false);
        selectAllRolesCheckbox.setSelected(false);
        selectAllUserManagementsCheckbox.setSelected(false);
        selectAllSalesReturnsCheckbox.setSelected(false);
        selectAllSettingsCheckbox.setSelected(false);
        selectAllSuppliersCheckbox.setSelected(false);
        selectAllCustomersCheckbox.setSelected(false);
        selectAllProductsCheckbox.setSelected(false);
        selectAllReportsCheckbox.setSelected(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindProperties();

        getDashBoardSetting();
        getPOSSettings();
        getRequisitionSetting();
        getPurchaseSetting();
        getTransferSetting();
        getStockInSetting();
        getQuotationSetting();
        getSaleSetting();
        getAdjustmentSetting();
        getPurchaseReturnSetting();
        getExpenseSetting();
        getRolesSetting();
        getUserMgtSetting();
        getSaleReturnSetting();
        getSettingSetting();
        getSupplierSetting();
        getCustomerSetting();
        getProductsSetting();
        getReportSetting();

        validationConstraints();
        validationWrapper();
    }

    private void bindProperties() {
        roleNameInputField.textProperty().bindBidirectional(RoleViewModel.nameProperty());
        roleDescriptionInputField.textProperty().bindBidirectional(RoleViewModel.descriptionProperty());
    }

    private void getDashBoardSetting() {
        dashboardCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                PermissionsViewModel.removePermission(PermissionsViewModel.getDashboardAccess());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getDashboardAccess());
                            }
                        });
    }

    private void getPOSSettings() {
        accessPOSCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                PermissionsViewModel.removePermission(PermissionsViewModel.getPosAccess());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getPosAccess());
                            }
                        });
    }

    private void getUserMgtSetting() {
        selectAllUserManagementsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (selectAllUserManagementsCheckbox.isFocused()) {
                                viewUsersCheckbox.setSelected(newVal);
                                editUsersCheckbox.setSelected(newVal);
                                createUserCheckbox.setSelected(newVal);
                                deleteUserCheckbox.setSelected(newVal);
                            }
                        });

        viewUsersCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllUserManagementsCheckbox.isSelected())
                                    selectAllUserManagementsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getViewUsers());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getViewUsers());
                            }
                        });
        editUsersCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllUserManagementsCheckbox.isSelected())
                                    selectAllUserManagementsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getEditUsers());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getEditUsers());
                            }
                        });
        createUserCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllUserManagementsCheckbox.isSelected())
                                    selectAllUserManagementsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getCreateUsers());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getCreateUsers());
                            }
                        });
        deleteUserCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllUserManagementsCheckbox.isSelected())
                                    selectAllUserManagementsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getDeleteUsers());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getDeleteUsers());
                            }
                        });
    }

    private void getRolesSetting() {
        selectAllRolesCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (selectAllRolesCheckbox.isFocused()) {
                                viewRoleCheckbox.setSelected(newVal);
                                editRoleCheckbox.setSelected(newVal);
                                createRoleCheckbox.setSelected(newVal);
                                deleteRoleCheckbox.setSelected(newVal);
                            }
                        });

        viewRoleCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllRolesCheckbox.isSelected()) selectAllRolesCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getViewPermissions());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getViewPermissions());
                            }
                        });
        editRoleCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllRolesCheckbox.isSelected()) selectAllRolesCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getEditPermissions());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getEditPermissions());
                            }
                        });
        createRoleCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllRolesCheckbox.isSelected()) selectAllRolesCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getCreatePermissions());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getCreatePermissions());
                            }
                        });
        deleteRoleCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllRolesCheckbox.isSelected()) selectAllRolesCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getDeletePermissions());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getDeletePermissions());
                            }
                        });
    }

    private void getProductsSetting() {
        selectAllProductsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (selectAllProductsCheckbox.isFocused()) {
                                viewProductsCheckbox.setSelected(newVal);
                                editProductsCheckbox.setSelected(newVal);
                                productBarcodesCheckbox.setSelected(newVal);
                                productCategoriesCheckbox.setSelected(newVal);
                                productUnitsCheckbox.setSelected(newVal);
                                createProductCheckbox.setSelected(newVal);
                                deleteProductCheckbox.setSelected(newVal);
                                productImportsCheckbox.setSelected(newVal);
                                productBrandsCheckbox.setSelected(newVal);
                            }
                        });

        viewProductsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllProductsCheckbox.isSelected())
                                    selectAllProductsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getViewProducts());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getViewProducts());
                            }
                        });
        editProductsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllProductsCheckbox.isSelected())
                                    selectAllProductsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getEditProducts());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getEditProducts());
                            }
                        });
        productBarcodesCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) selectAllProductsCheckbox.setSelected(false);
                        });
        productCategoriesCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllProductsCheckbox.isSelected())
                                    selectAllProductsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getAccessProductCategories());
                            } else {
                                PermissionsViewModel.addPermission(
                                        PermissionsViewModel.getAccessProductCategories());
                            }
                        });
        productUnitsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllProductsCheckbox.isSelected())
                                    selectAllProductsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getAccessUnitsOfMeasure());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getAccessUnitsOfMeasure());
                            }
                        });
        createProductCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllProductsCheckbox.isSelected())
                                    selectAllProductsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getCreateProducts());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getCreateProducts());
                            }
                        });
        deleteProductCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllProductsCheckbox.isSelected())
                                    selectAllProductsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getDeleteProducts());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getDeleteProducts());
                            }
                        });
        productImportsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllProductsCheckbox.isSelected())
                                    selectAllProductsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getImportProducts());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getImportProducts());
                            }
                        });
        productBrandsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllProductsCheckbox.isSelected())
                                    selectAllProductsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getAccessBrands());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getAccessBrands());
                            }
                        });
    }

    private void getAdjustmentSetting() {
        selectAllAdjustmentsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (selectAllAdjustmentsCheckbox.isFocused()) {
                                viewAdjustmentsCheckbox.setSelected(newVal);
                                editAdjustmentsCheckbox.setSelected(newVal);
                                createAdjustmentCheckbox.setSelected(newVal);
                                deleteAdjustmentCheckbox.setSelected(newVal);
                            }
                        });

        viewAdjustmentsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllAdjustmentsCheckbox.isSelected())
                                    selectAllAdjustmentsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getViewAdjustments());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getViewAdjustments());
                            }
                        });
        editAdjustmentsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllAdjustmentsCheckbox.isSelected())
                                    selectAllAdjustmentsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getEditAdjustments());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getEditAdjustments());
                            }
                        });
        createAdjustmentCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllAdjustmentsCheckbox.isSelected())
                                    selectAllAdjustmentsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getCreateAdjustments());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getCreateAdjustments());
                            }
                        });
        deleteAdjustmentCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllAdjustmentsCheckbox.isSelected())
                                    selectAllAdjustmentsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getDeleteAdjustments());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getDeleteAdjustments());
                            }
                        });
    }

    private void getTransferSetting() {
        selectAllTransfersCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (selectAllTransfersCheckbox.isFocused()) {
                                viewTransfersCheckbox.setSelected(newVal);
                                editTransfersCheckbox.setSelected(newVal);
                                createTransferCheckbox.setSelected(newVal);
                                deleteTransferCheckbox.setSelected(newVal);
                            }
                        });

        viewTransfersCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllTransfersCheckbox.isSelected())
                                    selectAllTransfersCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getViewTransfers());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getViewTransfers());
                            }
                        });
        editTransfersCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllTransfersCheckbox.isSelected())
                                    selectAllTransfersCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getEditTransfers());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getEditTransfers());
                            }
                        });
        createTransferCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllTransfersCheckbox.isSelected())
                                    selectAllTransfersCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getCreateTransfers());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getCreateTransfers());
                            }
                        });
        deleteTransferCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllTransfersCheckbox.isSelected())
                                    selectAllTransfersCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getDeleteTransfers());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getDeleteTransfers());
                            }
                        });
    }

    private void getExpenseSetting() {
        selectAllExpensesCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (selectAllExpensesCheckbox.isFocused()) {
                                viewExpensesCheckbox.setSelected(newVal);
                                editExpensesCheckbox.setSelected(newVal);
                                createExpenseCheckbox.setSelected(newVal);
                                deleteExpenseCheckbox.setSelected(newVal);
                            }
                        });

        viewExpensesCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllExpensesCheckbox.isSelected())
                                    selectAllExpensesCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getViewExpenses());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getViewExpenses());
                            }
                        });
        editExpensesCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllExpensesCheckbox.isSelected())
                                    selectAllExpensesCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getEditExpenses());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getEditExpenses());
                            }
                        });
        createExpenseCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllExpensesCheckbox.isSelected())
                                    selectAllExpensesCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getCreateExpenses());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getCreateExpenses());
                            }
                        });
        deleteExpenseCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllExpensesCheckbox.isSelected())
                                    selectAllExpensesCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getDeleteExpenses());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getDeleteExpenses());
                            }
                        });
    }

    private void getSaleSetting() {
        selectAllSalesCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (selectAllSalesCheckbox.isFocused()) {
                                viewSalesCheckbox.setSelected(newVal);
                                editSalesCheckbox.setSelected(newVal);
                                createSaleCheckbox.setSelected(newVal);
                                deleteSaleCheckbox.setSelected(newVal);
                            }
                        });

        viewSalesCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllSalesCheckbox.isSelected()) selectAllSalesCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getViewSales());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getViewSales());
                            }
                        });
        editSalesCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllSalesCheckbox.isSelected()) selectAllSalesCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getEditSales());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getEditSales());
                            }
                        });
        createSaleCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllSalesCheckbox.isSelected()) selectAllSalesCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getCreateSales());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getCreateSales());
                            }
                        });
        deleteSaleCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllSalesCheckbox.isSelected()) selectAllSalesCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getDeleteSales());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getDeleteSales());
                            }
                        });
    }

    private void getPurchaseSetting() {
        selectAllPurchasesCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (selectAllPurchasesCheckbox.isFocused()) {
                                viewPurchasesCheckbox.setSelected(newVal);
                                editPurchasesCheckbox.setSelected(newVal);
                                createPurchaseCheckbox.setSelected(newVal);
                                deletePurchaseCheckbox.setSelected(newVal);
                            }
                        });

        viewPurchasesCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllPurchasesCheckbox.isSelected())
                                    selectAllPurchasesCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getViewPurchases());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getViewPurchases());
                            }
                        });
        editPurchasesCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllPurchasesCheckbox.isSelected())
                                    selectAllPurchasesCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getEditPurchases());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getEditPurchases());
                            }
                        });
        createPurchaseCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllPurchasesCheckbox.isSelected())
                                    selectAllPurchasesCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getCreatePurchases());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getCreatePurchases());
                            }
                        });
        deletePurchaseCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllPurchasesCheckbox.isSelected())
                                    selectAllPurchasesCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getDeletePurchases());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getDeletePurchases());
                            }
                        });
    }

    private void getRequisitionSetting() {
        selectAllRequisitionsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (selectAllRequisitionsCheckbox.isFocused()) {
                                viewRequisitionCheckbox.setSelected(newVal);
                                editRequisitionCheckbox.setSelected(newVal);
                                createRequisitionCheckbox.setSelected(newVal);
                                deleteRequisitionCheckbox.setSelected(newVal);
                            }
                        });

        viewRequisitionCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllRequisitionsCheckbox.isSelected())
                                    selectAllRequisitionsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getViewRequisition());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getViewRequisition());
                            }
                        });
        editRequisitionCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllRequisitionsCheckbox.isSelected())
                                    selectAllRequisitionsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getEditRequisition());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getEditRequisition());
                            }
                        });
        createRequisitionCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllRequisitionsCheckbox.isSelected())
                                    selectAllRequisitionsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getCreateRequisition());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getCreateRequisition());
                            }
                        });
        deleteRequisitionCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllRequisitionsCheckbox.isSelected())
                                    selectAllRequisitionsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getDeleteRequisition());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getDeleteRequisition());
                            }
                        });
    }

    private void getStockInSetting() {
        selectAllStockInsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (selectAllStockInsCheckbox.isFocused()) {
                                viewStockInCheckbox.setSelected(newVal);
                                editStockInCheckbox.setSelected(newVal);
                                createStockInCheckbox.setSelected(newVal);
                                deleteStockInCheckbox.setSelected(newVal);
                            }
                        });

        viewStockInCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllStockInsCheckbox.isSelected())
                                    selectAllStockInsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getViewStockIn());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getViewStockIn());
                            }
                        });
        editStockInCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllStockInsCheckbox.isSelected())
                                    selectAllStockInsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getEditStockIn());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getEditStockIn());
                            }
                        });
        createStockInCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllStockInsCheckbox.isSelected())
                                    selectAllStockInsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getCreateStockIn());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getCreateStockIn());
                            }
                        });
        deleteStockInCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllStockInsCheckbox.isSelected())
                                    selectAllStockInsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getDeleteStockIn());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getDeleteStockIn());
                            }
                        });
    }

    private void getQuotationSetting() {
        selectAllQuotationsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (selectAllQuotationsCheckbox.isFocused()) {
                                viewQuotationsCheckbox.setSelected(newVal);
                                editQuotationsCheckbox.setSelected(newVal);
                                createQuotationCheckbox.setSelected(newVal);
                                deleteQuotationCheckbox.setSelected(newVal);
                            }
                        });

        viewQuotationsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllQuotationsCheckbox.isSelected())
                                    selectAllQuotationsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getViewQuotations());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getViewQuotations());
                            }
                        });
        editQuotationsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllQuotationsCheckbox.isSelected())
                                    selectAllQuotationsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getEditQuotations());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getEditQuotations());
                            }
                        });
        createQuotationCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllQuotationsCheckbox.isSelected())
                                    selectAllQuotationsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getCreateQuotations());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getCreateQuotations());
                            }
                        });
        deleteQuotationCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllQuotationsCheckbox.isSelected())
                                    selectAllQuotationsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getDeleteQuotations());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getDeleteQuotations());
                            }
                        });
    }

    private void getSaleReturnSetting() {
        selectAllSalesReturnsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (selectAllSalesReturnsCheckbox.isFocused()) {
                                viewSaleReturnsCheckbox.setSelected(newVal);
                                editSaleReturnsCheckbox.setSelected(newVal);
                                createSaleReturnCheckbox.setSelected(newVal);
                                deleteSaleReturnCheckbox.setSelected(newVal);
                            }
                        });

        viewSaleReturnsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllSalesReturnsCheckbox.isSelected())
                                    selectAllSalesReturnsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getViewSaleReturns());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getViewSaleReturns());
                            }
                        });
        editSaleReturnsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllSalesReturnsCheckbox.isSelected())
                                    selectAllSalesReturnsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getEditSaleReturns());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getEditSaleReturns());
                            }
                        });
        createSaleReturnCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllSalesReturnsCheckbox.isSelected())
                                    selectAllSalesReturnsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getCreateSaleReturns());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getCreateSaleReturns());
                            }
                        });
        deleteSaleReturnCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllSalesReturnsCheckbox.isSelected())
                                    selectAllSalesReturnsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getDeleteSaleReturns());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getDeleteSaleReturns());
                            }
                        });
    }

    private void getPurchaseReturnSetting() {
        selectAllPurchasesReturnsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (selectAllPurchasesReturnsCheckbox.isFocused()) {
                                viewPurchaseReturnsCheckbox.setSelected(newVal);
                                editPurchaseReturnsCheckbox.setSelected(newVal);
                                createPurchaseReturnCheckbox.setSelected(newVal);
                                deletePurchaseReturnCheckbox.setSelected(newVal);
                            }
                        });

        viewPurchaseReturnsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllPurchasesReturnsCheckbox.isSelected())
                                    selectAllPurchasesReturnsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getViewPurchaseReturns());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getViewPurchaseReturns());
                            }
                        });
        editPurchaseReturnsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllPurchasesReturnsCheckbox.isSelected())
                                    selectAllPurchasesReturnsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getEditPurchaseReturns());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getEditPurchaseReturns());
                            }
                        });
        createPurchaseReturnCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllPurchasesReturnsCheckbox.isSelected())
                                    selectAllPurchasesReturnsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getCreatePurchaseReturns());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getCreatePurchaseReturns());
                            }
                        });
        deletePurchaseReturnCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllPurchasesReturnsCheckbox.isSelected())
                                    selectAllPurchasesReturnsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getDeletePurchaseReturns());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getDeletePurchaseReturns());
                            }
                        });
    }

    private void getCustomerSetting() {
        selectAllCustomersCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (selectAllCustomersCheckbox.isFocused()) {
                                viewCustomersCheckbox.setSelected(newVal);
                                editCustomersCheckbox.setSelected(newVal);
                                importCustomersCheckbox.setSelected(newVal);
                                createCustomerCheckbox.setSelected(newVal);
                                deleteCustomerCheckbox.setSelected(newVal);
                            }
                        });

        viewCustomersCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllCustomersCheckbox.isSelected())
                                    selectAllCustomersCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getViewCustomers());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getViewCustomers());
                            }
                        });
        editCustomersCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllCustomersCheckbox.isSelected())
                                    selectAllCustomersCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getEditCustomers());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getEditCustomers());
                            }
                        });
        importCustomersCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllCustomersCheckbox.isSelected())
                                    selectAllCustomersCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getImportCustomers());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getImportCustomers());
                            }
                        });
        createCustomerCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllCustomersCheckbox.isSelected())
                                    selectAllCustomersCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getCreateCustomers());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getCreateCustomers());
                            }
                        });
        deleteCustomerCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllCustomersCheckbox.isSelected())
                                    selectAllCustomersCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getDeleteCustomers());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getDeleteCustomers());
                            }
                        });
    }

    private void getSupplierSetting() {
        selectAllSuppliersCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (selectAllSuppliersCheckbox.isFocused()) {
                                viewSuppliersCheckbox.setSelected(newVal);
                                editSuppliersCheckbox.setSelected(newVal);
                                importSuppliersCheckbox.setSelected(newVal);
                                createSupplierCheckbox.setSelected(newVal);
                                deleteSupplierCheckbox.setSelected(newVal);
                            }
                        });

        viewSuppliersCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllSuppliersCheckbox.isSelected())
                                    selectAllSuppliersCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getViewSuppliers());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getViewSuppliers());
                            }
                        });
        editSuppliersCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllSuppliersCheckbox.isSelected())
                                    selectAllSuppliersCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getEditSuppliers());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getEditSuppliers());
                            }
                        });
        importSuppliersCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllSuppliersCheckbox.isSelected())
                                    selectAllSuppliersCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getImportSuppliers());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getImportSuppliers());
                            }
                        });
        createSupplierCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllSuppliersCheckbox.isSelected())
                                    selectAllSuppliersCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getCreateSuppliers());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getCreateSuppliers());
                            }
                        });
        deleteSupplierCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllSuppliersCheckbox.isSelected())
                                    selectAllSuppliersCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getDeleteSuppliers());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getDeleteSuppliers());
                            }
                        });
    }

    private void getReportSetting() {
        selectAllReportsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (selectAllReportsCheckbox.isFocused()) {
                                paymentSalesCheckbox.setSelected(newVal);
                                paymentPurchasesCheckbox.setSelected(newVal);
                                saleReturnPaymentsCheckbox.setSelected(newVal);
                                purchaseReturnPaymentsCheckbox.setSelected(newVal);
                                saleReportCheckbox.setSelected(newVal);
                                purchaseReportCheckbox.setSelected(newVal);
                                customerReportCheckbox.setSelected(newVal);
                                supplierReportCheckbox.setSelected(newVal);
                                profitAndLossCheckbox.setSelected(newVal);
                                productQuantityAlertsCheckbox.setSelected(newVal);
                                branchStockChartCheckbox.setSelected(newVal);
                                topSellingProductsCheckbox.setSelected(newVal);
                                customerRankingCheckbox.setSelected(newVal);
                                usersReportCheckbox.setSelected(newVal);
                                stockReportCheckbox.setSelected(newVal);
                                productReportCheckbox.setSelected(newVal);
                                productSalesReportCheckbox.setSelected(newVal);
                                productPurchasesReportCheckbox.setSelected(newVal);
                            }
                        });

        paymentSalesCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllReportsCheckbox.isSelected())
                                    selectAllReportsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getAccessPaymentsSalesReports());
                            } else {
                                PermissionsViewModel.addPermission(
                                        PermissionsViewModel.getAccessPaymentsSalesReports());
                            }
                        });
        paymentPurchasesCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllReportsCheckbox.isSelected())
                                    selectAllReportsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getAccessPaymentsPurchasesReports());
                            } else {
                                PermissionsViewModel.addPermission(
                                        PermissionsViewModel.getAccessPaymentsPurchasesReports());
                            }
                        });
        saleReturnPaymentsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllReportsCheckbox.isSelected())
                                    selectAllReportsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getAccessSalesReturnsPaymentsReports());
                            } else {
                                PermissionsViewModel.addPermission(
                                        PermissionsViewModel.getAccessSalesReturnsPaymentsReports());
                            }
                        });
        purchaseReturnPaymentsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllReportsCheckbox.isSelected())
                                    selectAllReportsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getAccessPurchasesReturnsPaymentsReports());
                            } else {
                                PermissionsViewModel.addPermission(
                                        PermissionsViewModel.getAccessPurchasesReturnsPaymentsReports());
                            }
                        });
        saleReportCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllReportsCheckbox.isSelected())
                                    selectAllReportsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getAccessSalesReports());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getAccessSalesReports());
                            }
                        });
        purchaseReportCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllReportsCheckbox.isSelected())
                                    selectAllReportsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getAccessPurchasesReports());
                            } else {
                                PermissionsViewModel.addPermission(
                                        PermissionsViewModel.getAccessPurchasesReports());
                            }
                        });
        customerReportCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllReportsCheckbox.isSelected())
                                    selectAllReportsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getAccessCustomersReports());
                            } else {
                                PermissionsViewModel.addPermission(
                                        PermissionsViewModel.getAccessCustomersReports());
                            }
                        });
        supplierReportCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllReportsCheckbox.isSelected())
                                    selectAllReportsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getAccessSuppliersReports());
                            } else {
                                PermissionsViewModel.addPermission(
                                        PermissionsViewModel.getAccessSuppliersReports());
                            }
                        });
        profitAndLossCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllReportsCheckbox.isSelected())
                                    selectAllReportsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getAccessProfitsAndLossesReports());
                            } else {
                                PermissionsViewModel.addPermission(
                                        PermissionsViewModel.getAccessProfitsAndLossesReports());
                            }
                        });
        productQuantityAlertsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllReportsCheckbox.isSelected())
                                    selectAllReportsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getAccessProductQuantityReports());
                            } else {
                                PermissionsViewModel.addPermission(
                                        PermissionsViewModel.getAccessProductQuantityReports());
                            }
                        });
        branchStockChartCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllReportsCheckbox.isSelected())
                                    selectAllReportsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getAccessBranchStockChartsReports());
                            } else {
                                PermissionsViewModel.addPermission(
                                        PermissionsViewModel.getAccessBranchStockChartsReports());
                            }
                        });
        topSellingProductsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllReportsCheckbox.isSelected())
                                    selectAllReportsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getAccessTopSellingProductsReports());
                            } else {
                                PermissionsViewModel.addPermission(
                                        PermissionsViewModel.getAccessTopSellingProductsReports());
                            }
                        });
        customerRankingCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllReportsCheckbox.isSelected())
                                    selectAllReportsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getAccessCustomerRankingsReports());
                            } else {
                                PermissionsViewModel.addPermission(
                                        PermissionsViewModel.getAccessCustomerRankingsReports());
                            }
                        });
        usersReportCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllReportsCheckbox.isSelected())
                                    selectAllReportsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getAccessUsersReports());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getAccessUsersReports());
                            }
                        });
        stockReportCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllReportsCheckbox.isSelected())
                                    selectAllReportsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getAccessStocksReports());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getAccessStocksReports());
                            }
                        });
        productReportCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllReportsCheckbox.isSelected())
                                    selectAllReportsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getAccessProductsReports());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getAccessProductsReports());
                            }
                        });
        productSalesReportCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllReportsCheckbox.isSelected())
                                    selectAllReportsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getAccessProductSalesReports());
                            } else {
                                PermissionsViewModel.addPermission(
                                        PermissionsViewModel.getAccessProductSalesReports());
                            }
                        });
        productPurchasesReportCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllReportsCheckbox.isSelected())
                                    selectAllReportsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getAccessProductPurchasesReports());
                            } else {
                                PermissionsViewModel.addPermission(
                                        PermissionsViewModel.getAccessProductPurchasesReports());
                            }
                        });
    }

    private void getSettingSetting() {
        selectAllSettingsCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (selectAllSettingsCheckbox.isFocused()) {
                                accessSystemSettingCheckbox.setSelected(newVal);
                                AccessPOSSettingsCheckbox.setSelected(newVal);
                                accessCurrencyCheckbox.setSelected(newVal);
                                accessBranchCheckbox.setSelected(newVal);
                                accessBackupCheckbox.setSelected(newVal);
                            }
                        });
        accessSystemSettingCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllSettingsCheckbox.isSelected())
                                    selectAllSettingsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getViewSystemSettings());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getViewSystemSettings());
                            }
                        });
        AccessPOSSettingsCheckbox.selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllSettingsCheckbox.isSelected())
                                    selectAllSettingsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getViewPOSSettings());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getViewPOSSettings());
                            }
                        });
        accessCurrencyCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllSettingsCheckbox.isSelected())
                                    selectAllSettingsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getAccessCurrencySettings());
                            } else {
                                PermissionsViewModel.addPermission(
                                        PermissionsViewModel.getAccessCurrencySettings());
                            }
                        });
        accessBranchCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllSettingsCheckbox.isSelected())
                                    selectAllSettingsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(
                                        PermissionsViewModel.getAccessBranchSettings());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getAccessBranchSettings());
                            }
                        });
        accessBackupCheckbox
                .selectedProperty()
                .addListener(
                        (observableVal, oldVal, newVal) -> {
                            if (!newVal) {
                                if (selectAllSettingsCheckbox.isSelected())
                                    selectAllSettingsCheckbox.setSelected(false);
                                PermissionsViewModel.removePermission(PermissionsViewModel.getViewBackupSettings());
                            } else {
                                PermissionsViewModel.addPermission(PermissionsViewModel.getViewBackupSettings());
                            }
                        });
    }

    public void save() {
        SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

        constraints = roleNameInputField.validate();
        if (!constraints.isEmpty()) {
            errorLabel.setText(constraints.get(0).getMessage());
            errorLabel.setVisible(true);
        }

        if (PermissionsViewModel.getPermissionsList().isEmpty()) {
            SimpleNotification notification =
                    new SimpleNotification.NotificationBuilder("Role has no permissions")
                            .duration(NotificationDuration.SHORT)
                            .icon("fas-triangle-exclamation")
                            .type(NotificationVariants.ERROR)
                            .build();

            notificationHolder.addNotification(notification);

            return;
        }

        if (!errorLabel.isVisible()) {
            if (RoleViewModel.getId() > 0) {
                SpotyThreader.spotyThreadPool(() -> {
                    try {
                        RoleViewModel.updateItem(RoleViewModel.getId());
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, this.getClass());
                    }
                });

                SimpleNotification notification =
                        new SimpleNotification.NotificationBuilder("Role updated successfully")
                                .duration(NotificationDuration.MEDIUM)
                                .icon("fas-circle-check")
                                .type(NotificationVariants.SUCCESS)
                                .build();

                notificationHolder.addNotification(notification);
                close();

                return;
            }

            SpotyThreader.spotyThreadPool(() -> {
                try {
                    RoleViewModel.saveRole();
                } catch (Exception e) {
                    SpotyLogger.writeToFile(e, this.getClass());
                }
            });

            SimpleNotification notification =
                    new SimpleNotification.NotificationBuilder("Role saved successfully")
                            .duration(NotificationDuration.MEDIUM)
                            .icon("fas-circle-check")
                            .type(NotificationVariants.SUCCESS)
                            .build();

            notificationHolder.addNotification(notification);
            close();

            return;
        }

        SimpleNotification notification =
                new SimpleNotification.NotificationBuilder("Required fields missing")
                        .duration(NotificationDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(NotificationVariants.ERROR)
                        .build();

        notificationHolder.addNotification(notification);
    }

    public void close() {
        if (!Objects.equals(constraints, null) && !constraints.isEmpty()) {
            constraints.clear();
            errorLabel.setVisible(false);
        }
        Navigation.navigate(Pages.getRolesPane(), (StackPane) roleSettingsHolder.getParent());
        RoleViewModel.resetRoleProperties();
    }

    protected void validationWrapper() {
        errorLabel.getStyleClass().add("input-validation-error");

        roleNameInputField
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                errorLabel.setVisible(false);
                                roleNameInputField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        roleNameInputField
                .delegateFocusedProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (oldValue && !newValue) {
                                constraints = roleNameInputField.validate();
                                if (!constraints.isEmpty()) {
                                    roleNameInputField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                                    errorLabel.setText(constraints.get(0).getMessage());
                                    errorLabel.setVisible(true);
                                }
                            }
                        });
    }

    private void validationConstraints() {
        Constraint emptyConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Field required")
                        .setCondition(roleNameInputField.textProperty().length().greaterThan(0))
                        .get();
        Constraint lengthConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Role name must be at least 4 Characters long")
                        .setCondition(roleNameInputField.textProperty().length().greaterThan(3))
                        .get();

        roleNameInputField.getValidator().constraint(emptyConstraint).constraint(lengthConstraint);
    }
}
