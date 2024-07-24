package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.components.title.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.components.label_components.controls.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.util.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import io.github.palexdev.mfxcomponents.controls.buttons.*;
import io.github.palexdev.mfxcomponents.controls.checkbox.*;
import java.net.*;
import java.util.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class RoleFormController implements Initializable {
    @FXML
    public LabeledTextField name;
    @FXML
    public LabeledTextArea roleDescriptionInputField;
    @FXML
    public Button saveBtn,
            cancelBtn;
    // <editor-fold desc="Lots of MFXCheckboxes here 👇️">
    @FXML
    private MFXCheckbox dashboardCheckbox,
            accessPOSCheckbox,
            viewUsersCheckbox,
            editUsersCheckbox,
            createUserCheckbox,
            deleteUserCheckbox,
            viewRoleCheckbox,
            editRoleCheckbox,
            createRoleCheckbox,
            deleteRoleCheckbox,
            viewProductsCheckbox,
            editProductsCheckbox,
            createProductCheckbox,
            deleteProductCheckbox,
            productBarcodesCheckbox,
            productCategoriesCheckbox,
            productUnitsCheckbox,
            productBrandsCheckbox,
            viewAdjustmentsCheckbox,
            editAdjustmentsCheckbox,
            createAdjustmentCheckbox,
            deleteAdjustmentCheckbox,
            viewTransfersCheckbox,
            editTransfersCheckbox,
            createTransferCheckbox,
            deleteTransferCheckbox,
            viewExpensesCheckbox,
            editExpensesCheckbox,
            createExpenseCheckbox,
            deleteExpenseCheckbox,
            viewSalesCheckbox,
            editSalesCheckbox,
            createSaleCheckbox,
            deleteSaleCheckbox,
            viewPurchasesCheckbox,
            editPurchasesCheckbox,
            createPurchaseCheckbox,
            deletePurchaseCheckbox,
            viewRequisitionCheckbox,
            editRequisitionCheckbox,
            createRequisitionCheckbox,
            deleteRequisitionCheckbox,
            viewStockInCheckbox,
            editStockInCheckbox,
            createStockInCheckbox,
            deleteStockInCheckbox,
            viewQuotationsCheckbox,
            editQuotationsCheckbox,
            createQuotationCheckbox,
            deleteQuotationCheckbox,
            viewSaleReturnsCheckbox,
            editSaleReturnsCheckbox,
            createSaleReturnCheckbox,
            deleteSaleReturnCheckbox,
            viewPurchaseReturnsCheckbox,
            editPurchaseReturnsCheckbox,
            createPurchaseReturnCheckbox,
            deletePurchaseReturnCheckbox,
            viewCustomersCheckbox,
            editCustomersCheckbox,
            createCustomerCheckbox,
            deleteCustomerCheckbox,
            viewSuppliersCheckbox,
            editSuppliersCheckbox,
            createSupplierCheckbox,
            deleteSupplierCheckbox,
            accessSystemSettingCheckbox,
            AccessPOSSettingsCheckbox,
            accessCurrencyCheckbox,
            accessBranchCheckbox,
            accessBackupCheckbox,
            selectAllRequisitionsCheckbox,
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
            selectAllProductsCheckbox;
    private List<Constraint> constraints;
    @FXML
    private Label errorLabel;
    private ActionEvent actionEvent = null;

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
        viewSuppliersCheckbox.setSelected(false);
        editSuppliersCheckbox.setSelected(false);
        createSupplierCheckbox.setSelected(false);
        deleteSupplierCheckbox.setSelected(false);
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
    }

    private void initialiseCheckboxes() {
        dashboardCheckbox = new MFXCheckbox("Access Dashboard");
        accessPOSCheckbox = new MFXCheckbox("Access Point Of Sale");
        viewUsersCheckbox = new MFXCheckbox("Allow View");
        editUsersCheckbox = new MFXCheckbox("Allow Edit");
        createUserCheckbox = new MFXCheckbox("Allow Create/Add");
        deleteUserCheckbox = new MFXCheckbox("Allow Delete/Remove");
        viewRoleCheckbox = new MFXCheckbox("Allow View");
        editRoleCheckbox = new MFXCheckbox("Allow Edit");
        createRoleCheckbox = new MFXCheckbox("Allow Create/Add");
        deleteRoleCheckbox = new MFXCheckbox("Allow Delete/Remove");
        viewProductsCheckbox = new MFXCheckbox("Allow View");
        editProductsCheckbox = new MFXCheckbox("Allow Edit");
        createProductCheckbox = new MFXCheckbox("Allow Create/Add");
        deleteProductCheckbox = new MFXCheckbox("Allow Delete/Remove");
        productBarcodesCheckbox = new MFXCheckbox("Edit barcodes");
        productCategoriesCheckbox = new MFXCheckbox("Access Product Categories");
        productUnitsCheckbox = new MFXCheckbox("Access Product Units");
        productBrandsCheckbox = new MFXCheckbox("Access Product Brands");
        viewAdjustmentsCheckbox = new MFXCheckbox("Allow View");
        editAdjustmentsCheckbox = new MFXCheckbox("Allow Edit");
        createAdjustmentCheckbox = new MFXCheckbox("Allow Create/Add");
        deleteAdjustmentCheckbox = new MFXCheckbox("Allow Delete/Remove");
        viewTransfersCheckbox = new MFXCheckbox("Allow View");
        editTransfersCheckbox = new MFXCheckbox("Allow Edit");
        createTransferCheckbox = new MFXCheckbox("Allow Create/Add");
        deleteTransferCheckbox = new MFXCheckbox("Allow Delete/Remove");
        viewExpensesCheckbox = new MFXCheckbox("Allow View");
        editExpensesCheckbox = new MFXCheckbox("Allow Edit");
        createExpenseCheckbox = new MFXCheckbox("Allow Create/Add");
        deleteExpenseCheckbox = new MFXCheckbox("Allow Delete/Remove");
        viewSalesCheckbox = new MFXCheckbox("Allow View");
        editSalesCheckbox = new MFXCheckbox("Allow Edit");
        createSaleCheckbox = new MFXCheckbox("Allow Create/Add");
        deleteSaleCheckbox = new MFXCheckbox("Allow Delete/Remove");
        viewPurchasesCheckbox = new MFXCheckbox("Allow View");
        editPurchasesCheckbox = new MFXCheckbox("Allow Edit");
        createPurchaseCheckbox = new MFXCheckbox("Allow Create/Add");
        deletePurchaseCheckbox = new MFXCheckbox("Allow Delete/Remove");
        viewRequisitionCheckbox = new MFXCheckbox("Allow View");
        editRequisitionCheckbox = new MFXCheckbox("Allow Edit");
        createRequisitionCheckbox = new MFXCheckbox("Allow Create/Add");
        deleteRequisitionCheckbox = new MFXCheckbox("Allow Delete/Remove");
        viewStockInCheckbox = new MFXCheckbox("Allow View");
        editStockInCheckbox = new MFXCheckbox("Allow Edit");
        createStockInCheckbox = new MFXCheckbox("Allow Create/Add");
        deleteStockInCheckbox = new MFXCheckbox("Allow Delete/Remove");
        viewQuotationsCheckbox = new MFXCheckbox("Allow View");
        editQuotationsCheckbox = new MFXCheckbox("Allow Edit");
        createQuotationCheckbox = new MFXCheckbox("Allow Create/Add");
        deleteQuotationCheckbox = new MFXCheckbox("Allow Delete/Remove");
        viewSaleReturnsCheckbox = new MFXCheckbox("Allow View");
        editSaleReturnsCheckbox = new MFXCheckbox("Allow Edit");
        createSaleReturnCheckbox = new MFXCheckbox("Allow Create/Add");
        deleteSaleReturnCheckbox = new MFXCheckbox("Allow Delete/Remove");
        viewPurchaseReturnsCheckbox = new MFXCheckbox("Allow View");
        editPurchaseReturnsCheckbox = new MFXCheckbox("Allow Edit");
        createPurchaseReturnCheckbox = new MFXCheckbox("Allow Create/Add");
        deletePurchaseReturnCheckbox = new MFXCheckbox("Allow Delete/Remove");
        viewCustomersCheckbox = new MFXCheckbox("Allow View");
        editCustomersCheckbox = new MFXCheckbox("Allow Edit");
        createCustomerCheckbox = new MFXCheckbox("Allow Create/Add");
        deleteCustomerCheckbox = new MFXCheckbox("Allow Delete/Remove");
        viewSuppliersCheckbox = new MFXCheckbox("Allow View");
        editSuppliersCheckbox = new MFXCheckbox("Allow Edit");
        createSupplierCheckbox = new MFXCheckbox("Allow Create/Add");
        deleteSupplierCheckbox = new MFXCheckbox("Allow Delete/Remove");
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
        validationWrapper();
        dialogOnActions();
        requiredValidator();
    }

    private VBox createSectionVBox(Integer rowIndex, Integer columnIndex, Title title, Separator separator, GridPane gridPane) {
        VBox vbox = new VBox();
        vbox.setSpacing(5d);
        vbox.getStyleClass().add("card-flat");
        vbox.setPadding(new Insets(5d));
        GridPane.setRowIndex(vbox, rowIndex);
        GridPane.setColumnIndex(vbox, columnIndex);
        vbox.getChildren().addAll(title, separator, gridPane);
        return vbox;
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    RoleViewModel.resetRoleProperties();
                    closeDialog(event);

                    errorLabel.setVisible(false);
                    errorLabel.setManaged(false);
                    name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    constraints = name.validate();
                    if (!constraints.isEmpty()) {
                        errorLabel.setManaged(true);
                        errorLabel.setVisible(true);
                        errorLabel.setText(constraints.getFirst().getMessage());
                        name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) name.getScene().getWindow();
                        dialog.sizeToScene();
                        return;
                    }
                    if (PermissionsViewModel.getPermissionsList().isEmpty()) {
                        errorMessage("Role has no permissions");
                        return;
                    }
                    if (RoleViewModel.getId() > 0) {
                        RoleViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                        actionEvent = event;
                        return;
                    }
                    RoleViewModel.saveRole(this::onSuccess, this::successMessage, this::errorMessage);
                    actionEvent = event;
                });
    }

    private void bindProperties() {
        name.textProperty().bindBidirectional(RoleViewModel.nameProperty());
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

    protected void validationWrapper() {
        errorLabel.getStyleClass().add("input-validation-error");

        name
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                errorLabel.setVisible(false);
                                name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }

    private void onAction() {
        cancelBtn.setDisable(true);
        saveBtn.setDisable(true);
    }

    private void onSuccess() {
        resetCheckboxes();
        closeDialog(actionEvent);
        RoleViewModel.getAllRoles(null, null);
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint requiredConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Name is required")
                        .setCondition(name.textProperty().length().greaterThan(0))
                        .get();
        Constraint lengthConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Role name must be at least 4 Characters long")
                        .setCondition(name.textProperty().length().greaterThan(3))
                        .get();
        name.getValidator().constraint(requiredConstraint).constraint(lengthConstraint);
        // Display error.
        name
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                errorLabel.setManaged(false);
                                errorLabel.setVisible(false);
                                name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }

    private void successMessage(String message) {
        displayNotification(message, MessageVariants.SUCCESS, "fas-circle-check");
    }

    private void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, "fas-triangle-exclamation");
    }

    private void displayNotification(String message, MessageVariants type, String icon) {
        SpotyMessage notification = new SpotyMessage.MessageBuilder(message)
                .duration(MessageDuration.SHORT)
                .icon(icon)
                .type(type)
                .height(60)
                .build();
        AnchorPane.setTopAnchor(notification, 5.0);
        AnchorPane.setRightAnchor(notification, 5.0);

        var in = Animations.slideInDown(notification, Duration.millis(250));
        if (!AppManager.getMorphPane().getChildren().contains(notification)) {
            AppManager.getMorphPane().getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> SpotyMessage.delay(notification));
        }
    }
}
