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

package org.infinite.spoty.forms;

import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;
import static org.infinite.spoty.values.numbers.Constants.GRID_HGAP;
import static org.infinite.spoty.values.numbers.Constants.GRID_VGAP;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.enums.FloatMode;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.MFXValidator;
import io.github.palexdev.materialfx.validation.Severity;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxcomponents.controls.checkbox.MFXCheckBox;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import org.infinite.spoty.components.navigation.Navigation;
import org.infinite.spoty.components.navigation.Pages;
import org.infinite.spoty.components.navigation.Spacer;

@SuppressWarnings("unused")
public class RoleSettingsFormController implements Initializable {
  private static RoleSettingsFormController instance;
  private final MFXTextField roleNameInputField;
  // <editor-fold desc="Lots of MFXCheckBoxes here 👇️">
  private final MFXCheckBox dashboardCheckbox, accessPOSCheckbox;
  // Users
  private final MFXCheckBox viewUsersCheckbox,
      editUsersCheckbox,
      createUserCheckbox,
      deleteUserCheckbox;
  // User Permissions
  private final MFXCheckBox viewUserPermissionsCheckbox,
      editUserPermissionsCheckbox,
      createUserPermissionCheckbox,
      deleteUserPermissionCheckbox;
  // Products
  private final MFXCheckBox viewProductsCheckbox,
      editProductsCheckbox,
      createProductCheckbox,
      deleteProductCheckbox,
      productBarcodesCheckbox,
      productCategoriesCheckbox,
      productUnitsCheckbox,
      productImportsCheckbox,
      productBrandsCheckbox;
  // Adjustments
  private final MFXCheckBox viewAdjustmentsCheckbox,
      editAdjustmentsCheckbox,
      createAdjustmentCheckbox,
      deleteAdjustmentCheckbox;
  // Transfers
  private final MFXCheckBox viewTransfersCheckbox,
      editTransfersCheckbox,
      createTransferCheckbox,
      deleteTransferCheckbox;
  // Expenses
  private final MFXCheckBox viewExpensesCheckbox,
      editExpensesCheckbox,
      createExpenseCheckbox,
      deleteExpenseCheckbox;
  // Sales
  private final MFXCheckBox viewSalesCheckbox,
      editSalesCheckbox,
      createSaleCheckbox,
      deleteSaleCheckbox;
  // Purchases
  private final MFXCheckBox viewPurchasesCheckbox,
      editPurchasesCheckbox,
      createPurchaseCheckbox,
      deletePurchaseCheckbox;
  // Requisitions
  private final MFXCheckBox viewRequisitionCheckbox,
      editRequisitionCheckbox,
      createRequisitionCheckbox,
      deleteRequisitionCheckbox;
  // StockIns
  private final MFXCheckBox viewStockInCheckbox,
      editStockInCheckbox,
      createStockInCheckbox,
      deleteStockInCheckbox;
  // Quotations
  private final MFXCheckBox viewQuotationsCheckbox,
      editQuotationsCheckbox,
      createQuotationCheckbox,
      deleteQuotationCheckbox;
  // Sale Returns
  private final MFXCheckBox viewSaleReturnsCheckbox,
      editSaleReturnsCheckbox,
      createSaleReturnCheckbox,
      deleteSaleReturnCheckbox;
  // Purchase Returns
  private final MFXCheckBox viewPurchaseReturnsCheckbox,
      editPurchaseReturnsCheckbox,
      createPurchaseReturnCheckbox,
      deletePurchaseReturnCheckbox;
  // Payments Sales
  private final MFXCheckBox viewPaymentsSalesCheckbox,
      editPaymentsSalesCheckbox,
      createPaymentsSaleCheckbox,
      deletePaymentsSaleCheckbox;
  // Payments Purchases
  private final MFXCheckBox viewPaymentsPurchasesCheckbox,
      editPaymentsPurchasesCheckbox,
      createPaymentsPurchaseCheckbox,
      deletePaymentsPurchaseCheckbox;
  // Payments Returns
  private final MFXCheckBox viewPaymentsReturnsCheckbox,
      editPaymentsReturnsCheckbox,
      createPaymentsReturnCheckbox,
      deletePaymentsReturnCheckbox;
  // Customers
  private final MFXCheckBox viewCustomersCheckbox,
      editCustomersCheckbox,
      createCustomerCheckbox,
      deleteCustomerCheckbox,
      importCustomersCheckbox,
      payAllSellDueCheckbox,
      payAllSellReturnDueCheckbox;
  // Suppliers
  private final MFXCheckBox viewSuppliersCheckbox,
      editSuppliersCheckbox,
      createSupplierCheckbox,
      deleteSupplierCheckbox,
      importSuppliersCheckbox,
      payAllPurchaseDueCheckbox,
      payAllPurchaseReturnDueCheckbox;
  // Reports
  private final MFXCheckBox paymentSalesCheckbox,
      paymentPurchasesCheckbox,
      saleReturnPaymentsCheckbox,
      purchaseReturnPaymentsCheckbox,
      saleReportCheckbox,
      purchaseReportCheckbox,
      customerReportCheckbox,
      supplierReportCheckbox,
      profitAndLossCheckbox,
      productQuantityAlertsCheckbox,
      warehouseStockChartCheckbox,
      topSellingProductsCheckbox,
      bestCustomersCheckbox,
      usersReportCheckbox,
      stockReportCheckbox,
      productReportCheckbox,
      productSalesReportCheckbox,
      productPurchasesReportCheckbox;
  // HRM
  private final MFXCheckBox viewEmployeeCheckbox,
      editEmployeeCheckbox,
      createEmployeeCheckbox,
      deleteEmployeeCheckbox,
      CompanyCheckbox,
      departmentCheckbox,
      designationCheckbox,
      officeShiftCheckbox,
      attendanceCheckbox,
      leaveRequestCheckbox,
      holidayCheckbox;
  // Settings
  private final MFXCheckBox viewSystemSettingCheckbox,
      viewPOSSettingsCheckbox,
      viewCurrencyCheckbox,
      viewBranchCheckbox,
      viewBackupCheckbox;
  // Select All.
  private final MFXCheckBox selectAllRequisitionsCheckbox,
      selectAllPurchasesCheckbox,
      selectAllTransfersCheckbox,
      selectAllStockInsCheckbox,
      selectAllQuotationsCheckbox,
      selectAllSalesCheckbox,
      selectAllExpensesCheckbox,
      selectAllAdjustmentsCheckbox,
      selectAllPurchasesReturnsCheckbox,
      selectAllPaymentsPurchasesCheckbox,
      selectAllPaymentsSalesCheckbox,
      selectAllPaymentsReturnsCheckbox,
      selectAllRolePermissionsCheckbox,
      selectAllUserManagementsCheckbox,
      selectAllSalesReturnsCheckbox,
      selectAllSettingsCheckbox,
      selectAllHRMCheckbox,
      selectAllSuppliersCheckbox,
      selectAllCustomersCheckbox,
      selectAllProductsCheckbox,
      selectAllReportsCheckbox;
  // </editor-fold>
  @FXML public MFXButton roleSaveBtn;
  @FXML public MFXButton roleCancelBtn;
  @FXML public VBox roleHolder;
  // <editor-fold desc="Lots of VBoxes here 👇️">
  VBox settingSetting,
      hrmSetting,
      reportSetting,
      supplierSetting,
      paymentReturnSetting,
      paymentPurchaseSetting,
      paymentSaleSetting,
      purchaseReturnSetting,
      saleReturnSetting,
      quotationSetting,
      purchaseSetting,
      requisitionSetting,
      stockInSetting,
      saleSetting,
      expenseSetting,
      transferSetting,
      adjustmentSetting,
      productsSetting,
      userPermissionsSetting,
      dashboardSetting,
      posSetting,
      userMgtSetting,
      customerSetting;
  // </editor-fold>
  // <editor-fold desc="Lots of GridPanes here 👇️">
  GridPane userMgtSettingCheckboxes,
      userPermissionsSettingCheckboxes,
      productsSettingCheckboxes,
      adjustmentSettingCheckboxes,
      transferSettingCheckboxes,
      expenseSettingCheckboxes,
      saleSettingCheckboxes,
      purchaseSettingCheckboxes,
      requisitionSettingCheckboxes,
      stockInSettingCheckboxes,
      quotationSettingCheckboxes,
      saleReturnSettingCheckboxes,
      purchaseReturnSettingCheckboxes,
      paymentSaleSettingCheckboxes,
      paymentPurchaseSettingCheckboxes,
      paymentReturnSettingCheckboxes,
      supplierSettingCheckboxes,
      reportSettingCheckboxes,
      hrmSettingCheckboxes,
      settingSettingCheckboxes,
      customerSettingCheckboxes;
  Label errorLabel;
  VBox container;
  MFXValidator validator;
  List<Constraint> validate;
  Constraint emptyConstraint;
  Constraint lengthConstraint;
  ColumnConstraints col1;
  ColumnConstraints col2;
  // </editor-fold>
  @FXML private GridPane roleSettings;
  @FXML private BorderPane roleSettingsHolder;

  public RoleSettingsFormController() {
    settingSetting = new VBox();
    hrmSetting = new VBox();
    reportSetting = new VBox();
    customerSetting = new VBox();
    supplierSetting = new VBox();
    paymentReturnSetting = new VBox();
    paymentPurchaseSetting = new VBox();
    paymentSaleSetting = new VBox();
    purchaseReturnSetting = new VBox();
    saleReturnSetting = new VBox();
    quotationSetting = new VBox();
    purchaseSetting = new VBox();
    requisitionSetting = new VBox();
    stockInSetting = new VBox();
    saleSetting = new VBox();
    expenseSetting = new VBox();
    transferSetting = new VBox();
    adjustmentSetting = new VBox();
    productsSetting = new VBox();
    userPermissionsSetting = new VBox();
    dashboardSetting = new VBox();
    posSetting = new VBox();
    userMgtSetting = new VBox();
    roleHolder = new VBox();

    userMgtSettingCheckboxes = new GridPane();
    userPermissionsSettingCheckboxes = new GridPane();
    productsSettingCheckboxes = new GridPane();
    adjustmentSettingCheckboxes = new GridPane();
    transferSettingCheckboxes = new GridPane();
    expenseSettingCheckboxes = new GridPane();
    saleSettingCheckboxes = new GridPane();
    purchaseSettingCheckboxes = new GridPane();
    requisitionSettingCheckboxes = new GridPane();
    stockInSettingCheckboxes = new GridPane();
    quotationSettingCheckboxes = new GridPane();
    saleReturnSettingCheckboxes = new GridPane();
    purchaseReturnSettingCheckboxes = new GridPane();
    paymentSaleSettingCheckboxes = new GridPane();
    paymentPurchaseSettingCheckboxes = new GridPane();
    paymentReturnSettingCheckboxes = new GridPane();
    supplierSettingCheckboxes = new GridPane();
    reportSettingCheckboxes = new GridPane();
    hrmSettingCheckboxes = new GridPane();
    settingSettingCheckboxes = new GridPane();
    customerSettingCheckboxes = new GridPane();

    roleNameInputField = new MFXTextField();

    dashboardCheckbox = new MFXCheckBox();
    accessPOSCheckbox = new MFXCheckBox();

    viewUsersCheckbox = new MFXCheckBox();
    editUsersCheckbox = new MFXCheckBox();
    createUserCheckbox = new MFXCheckBox();
    deleteUserCheckbox = new MFXCheckBox();

    viewUserPermissionsCheckbox = new MFXCheckBox();
    editUserPermissionsCheckbox = new MFXCheckBox();
    createUserPermissionCheckbox = new MFXCheckBox();
    deleteUserPermissionCheckbox = new MFXCheckBox();

    viewProductsCheckbox = new MFXCheckBox();
    editProductsCheckbox = new MFXCheckBox();
    createProductCheckbox = new MFXCheckBox();
    deleteProductCheckbox = new MFXCheckBox();
    productBarcodesCheckbox = new MFXCheckBox();
    productCategoriesCheckbox = new MFXCheckBox();
    productUnitsCheckbox = new MFXCheckBox();
    productImportsCheckbox = new MFXCheckBox();
    productBrandsCheckbox = new MFXCheckBox();

    viewAdjustmentsCheckbox = new MFXCheckBox();
    editAdjustmentsCheckbox = new MFXCheckBox();
    createAdjustmentCheckbox = new MFXCheckBox();
    deleteAdjustmentCheckbox = new MFXCheckBox();

    viewTransfersCheckbox = new MFXCheckBox();
    editTransfersCheckbox = new MFXCheckBox();
    createTransferCheckbox = new MFXCheckBox();
    deleteTransferCheckbox = new MFXCheckBox();

    viewExpensesCheckbox = new MFXCheckBox();
    editExpensesCheckbox = new MFXCheckBox();
    createExpenseCheckbox = new MFXCheckBox();
    deleteExpenseCheckbox = new MFXCheckBox();

    viewSalesCheckbox = new MFXCheckBox();
    editSalesCheckbox = new MFXCheckBox();
    createSaleCheckbox = new MFXCheckBox();
    deleteSaleCheckbox = new MFXCheckBox();

    viewPurchasesCheckbox = new MFXCheckBox();
    editPurchasesCheckbox = new MFXCheckBox();
    createPurchaseCheckbox = new MFXCheckBox();
    deletePurchaseCheckbox = new MFXCheckBox();

    viewRequisitionCheckbox = new MFXCheckBox();
    editRequisitionCheckbox = new MFXCheckBox();
    createRequisitionCheckbox = new MFXCheckBox();
    deleteRequisitionCheckbox = new MFXCheckBox();

    viewStockInCheckbox = new MFXCheckBox();
    editStockInCheckbox = new MFXCheckBox();
    createStockInCheckbox = new MFXCheckBox();
    deleteStockInCheckbox = new MFXCheckBox();

    viewQuotationsCheckbox = new MFXCheckBox();
    editQuotationsCheckbox = new MFXCheckBox();
    createQuotationCheckbox = new MFXCheckBox();
    deleteQuotationCheckbox = new MFXCheckBox();

    viewSaleReturnsCheckbox = new MFXCheckBox();
    editSaleReturnsCheckbox = new MFXCheckBox();
    createSaleReturnCheckbox = new MFXCheckBox();
    deleteSaleReturnCheckbox = new MFXCheckBox();

    viewPurchaseReturnsCheckbox = new MFXCheckBox();
    editPurchaseReturnsCheckbox = new MFXCheckBox();
    createPurchaseReturnCheckbox = new MFXCheckBox();
    deletePurchaseReturnCheckbox = new MFXCheckBox();

    viewPaymentsSalesCheckbox = new MFXCheckBox();
    editPaymentsSalesCheckbox = new MFXCheckBox();
    createPaymentsSaleCheckbox = new MFXCheckBox();
    deletePaymentsSaleCheckbox = new MFXCheckBox();

    viewPaymentsPurchasesCheckbox = new MFXCheckBox();
    editPaymentsPurchasesCheckbox = new MFXCheckBox();
    createPaymentsPurchaseCheckbox = new MFXCheckBox();
    deletePaymentsPurchaseCheckbox = new MFXCheckBox();

    viewPaymentsReturnsCheckbox = new MFXCheckBox();
    editPaymentsReturnsCheckbox = new MFXCheckBox();
    createPaymentsReturnCheckbox = new MFXCheckBox();
    deletePaymentsReturnCheckbox = new MFXCheckBox();

    viewCustomersCheckbox = new MFXCheckBox();
    editCustomersCheckbox = new MFXCheckBox();
    createCustomerCheckbox = new MFXCheckBox();
    deleteCustomerCheckbox = new MFXCheckBox();
    importCustomersCheckbox = new MFXCheckBox();
    payAllSellDueCheckbox = new MFXCheckBox();
    payAllSellReturnDueCheckbox = new MFXCheckBox();

    viewSuppliersCheckbox = new MFXCheckBox();
    editSuppliersCheckbox = new MFXCheckBox();
    createSupplierCheckbox = new MFXCheckBox();
    deleteSupplierCheckbox = new MFXCheckBox();
    importSuppliersCheckbox = new MFXCheckBox();
    payAllPurchaseDueCheckbox = new MFXCheckBox();
    payAllPurchaseReturnDueCheckbox = new MFXCheckBox();

    paymentSalesCheckbox = new MFXCheckBox();
    paymentPurchasesCheckbox = new MFXCheckBox();
    saleReturnPaymentsCheckbox = new MFXCheckBox();
    purchaseReturnPaymentsCheckbox = new MFXCheckBox();
    saleReportCheckbox = new MFXCheckBox();
    purchaseReportCheckbox = new MFXCheckBox();
    customerReportCheckbox = new MFXCheckBox();
    supplierReportCheckbox = new MFXCheckBox();
    profitAndLossCheckbox = new MFXCheckBox();
    productQuantityAlertsCheckbox = new MFXCheckBox();
    warehouseStockChartCheckbox = new MFXCheckBox();
    topSellingProductsCheckbox = new MFXCheckBox();
    bestCustomersCheckbox = new MFXCheckBox();
    usersReportCheckbox = new MFXCheckBox();
    stockReportCheckbox = new MFXCheckBox();
    productReportCheckbox = new MFXCheckBox();
    productSalesReportCheckbox = new MFXCheckBox();
    productPurchasesReportCheckbox = new MFXCheckBox();

    viewEmployeeCheckbox = new MFXCheckBox();
    editEmployeeCheckbox = new MFXCheckBox();
    createEmployeeCheckbox = new MFXCheckBox();
    deleteEmployeeCheckbox = new MFXCheckBox();
    CompanyCheckbox = new MFXCheckBox();
    departmentCheckbox = new MFXCheckBox();
    designationCheckbox = new MFXCheckBox();
    officeShiftCheckbox = new MFXCheckBox();
    attendanceCheckbox = new MFXCheckBox();
    leaveRequestCheckbox = new MFXCheckBox();
    holidayCheckbox = new MFXCheckBox();

    viewSystemSettingCheckbox = new MFXCheckBox();
    viewPOSSettingsCheckbox = new MFXCheckBox();
    viewCurrencyCheckbox = new MFXCheckBox();
    viewBranchCheckbox = new MFXCheckBox();
    viewBackupCheckbox = new MFXCheckBox();

    selectAllRequisitionsCheckbox = new MFXCheckBox("Select All");
    selectAllPurchasesCheckbox = new MFXCheckBox("Select All");
    selectAllTransfersCheckbox = new MFXCheckBox("Select All");
    selectAllStockInsCheckbox = new MFXCheckBox("Select All");
    selectAllQuotationsCheckbox = new MFXCheckBox("Select All");
    selectAllSalesCheckbox = new MFXCheckBox("Select All");
    selectAllExpensesCheckbox = new MFXCheckBox("Select All");
    selectAllAdjustmentsCheckbox = new MFXCheckBox("Select All");
    selectAllPurchasesReturnsCheckbox = new MFXCheckBox("Select All");
    selectAllPaymentsPurchasesCheckbox = new MFXCheckBox("Select All");
    selectAllPaymentsSalesCheckbox = new MFXCheckBox("Select All");
    selectAllPaymentsReturnsCheckbox = new MFXCheckBox("Select All");
    selectAllRolePermissionsCheckbox = new MFXCheckBox("Select All");
    selectAllUserManagementsCheckbox = new MFXCheckBox("Select All");
    selectAllSalesReturnsCheckbox = new MFXCheckBox("Select All");
    selectAllSettingsCheckbox = new MFXCheckBox("Select All");
    selectAllHRMCheckbox = new MFXCheckBox("Select All");
    selectAllSuppliersCheckbox = new MFXCheckBox("Select All");
    selectAllCustomersCheckbox = new MFXCheckBox("Select All");
    selectAllProductsCheckbox = new MFXCheckBox("Select All");
    selectAllReportsCheckbox = new MFXCheckBox("Select All");

    col1 = new ColumnConstraints();
    col1.setHgrow(Priority.ALWAYS);
    col2 = new ColumnConstraints();
    col2.setHgrow(Priority.ALWAYS);
  }

  public static RoleSettingsFormController getInstance() {
    if (Objects.equals(instance, null)) instance = new RoleSettingsFormController();
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    roleHolder.setSpacing(5);
    roleHolder.getStyleClass().addAll("spoty-role-form-header");
    roleHolder.getChildren().addAll(new Label("Create Role"), new Separator());

    emptyConstraint =
        Constraint.Builder.build()
            .setSeverity(Severity.ERROR)
            .setMessage("Field required")
            .setCondition(roleNameInputField.textProperty().length().greaterThan(0))
            .get();
    lengthConstraint =
        Constraint.Builder.build()
            .setSeverity(Severity.ERROR)
            .setMessage("Role name must be at least 4 Characters long")
            .setCondition(roleNameInputField.textProperty().length().greaterThan(3))
            .get();

    roleNameInputField.setFloatMode(FloatMode.BORDER);
    roleNameInputField.setFloatingText("Role name");
    roleNameInputField.setPrefWidth(400);
    roleNameInputField.requestFocus();
    roleNameInputField.getValidator().constraint(emptyConstraint).constraint(lengthConstraint);

    getRoleSettings();
  }

  protected VBox validationWrapper(MFXTextField node) {
    errorLabel = new Label();
    container = new VBox(5, node, errorLabel);
    container.setAlignment(Pos.CENTER);
    errorLabel.getStyleClass().add("input-validation-error");

    node.getValidator()
        .validProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                errorLabel.setVisible(false);
                roleNameInputField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
              }
            });
    node.delegateFocusedProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (oldValue && !newValue) {
                List<Constraint> constraints = roleNameInputField.validate();
                if (!constraints.isEmpty()) {
                  roleNameInputField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                  errorLabel.setText(constraints.get(0).getMessage());
                  errorLabel.setVisible(true);
                }
              }
            });

    roleSaveBtn.setOnAction(
        e -> {
          validator = node.getValidator();
          validate = validator.validate();
          if (!validate.isEmpty()) {
            errorLabel.setText(validate.get(0).getMessage());
            errorLabel.setVisible(true);
          }
        });
    return container;
  }

  private VBox getDashboardRoleSetting() {
    dashboardSetting.getStyleClass().add("card");
    dashboardSetting.setPadding(new Insets(10));
    dashboardCheckbox.setText("Access Dashboard");
    dashboardSetting.getChildren().addAll(title("DASHBOARD", null), dashboardCheckbox);
    return dashboardSetting;
  }

  private VBox getPOSSetting() {
    posSetting.getStyleClass().add("card");
    posSetting.setPadding(new Insets(10));
    accessPOSCheckbox.setText("Access Point Of Sale");
    posSetting.getChildren().addAll(title("POINT OF SALE", null), accessPOSCheckbox);
    return posSetting;
  }

  private VBox getUserMgtSetting() {
    // Controls CRUD operations on User Management.
    userMgtSetting.getStyleClass().add("card");
    userMgtSetting.setPadding(new Insets(10));

    viewUsersCheckbox.setText("View Users");
    editUsersCheckbox.setText("Edit Users");
    createUserCheckbox.setText("Create Users");
    deleteUserCheckbox.setText("Delete Users");

    userMgtSettingCheckboxes.getColumnConstraints().addAll(col1, col2);

    userMgtSettingCheckboxes.addColumn(0, viewUsersCheckbox, editUsersCheckbox);
    userMgtSettingCheckboxes.addColumn(1, createUserCheckbox, deleteUserCheckbox);

    userMgtSettingCheckboxes.setHgap(GRID_HGAP);
    userMgtSettingCheckboxes.setVgap(GRID_VGAP);

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
              if (!newVal) selectAllUserManagementsCheckbox.setSelected(false);
            });
    editUsersCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllUserManagementsCheckbox.setSelected(false);
            });
    createUserCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllUserManagementsCheckbox.setSelected(false);
            });
    deleteUserCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllUserManagementsCheckbox.setSelected(false);
            });

    userMgtSetting
        .getChildren()
        .addAll(
            title("USER MANAGEMENT", selectAllUserManagementsCheckbox), userMgtSettingCheckboxes);
    return userMgtSetting;
  }

  private VBox getUserPermissionsSetting() {
    // Controls CRUD operations on setting User permissions.
    userPermissionsSetting.getStyleClass().add("card");
    userPermissionsSetting.setPadding(new Insets(10));

    viewUserPermissionsCheckbox.setText("View Permissions");
    editUserPermissionsCheckbox.setText("Edit Permissions");
    createUserPermissionCheckbox.setText("Create Permissions");
    deleteUserPermissionCheckbox.setText("Delete Permissions");

    userPermissionsSettingCheckboxes.getColumnConstraints().addAll(col1, col2);

    userPermissionsSettingCheckboxes.addColumn(
        0, viewUserPermissionsCheckbox, editUserPermissionsCheckbox);
    userPermissionsSettingCheckboxes.addColumn(
        1, createUserPermissionCheckbox, deleteUserPermissionCheckbox);

    userPermissionsSettingCheckboxes.setHgap(GRID_HGAP);
    userPermissionsSettingCheckboxes.setVgap(GRID_VGAP);

    selectAllRolePermissionsCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (selectAllRolePermissionsCheckbox.isFocused()) {
                viewUserPermissionsCheckbox.setSelected(newVal);
                editUserPermissionsCheckbox.setSelected(newVal);
                createUserPermissionCheckbox.setSelected(newVal);
                deleteUserPermissionCheckbox.setSelected(newVal);
              }
            });

    viewUserPermissionsCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllRolePermissionsCheckbox.setSelected(false);
            });
    editUserPermissionsCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllRolePermissionsCheckbox.setSelected(false);
            });
    createUserPermissionCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllRolePermissionsCheckbox.setSelected(false);
            });
    deleteUserPermissionCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllRolePermissionsCheckbox.setSelected(false);
            });

    userPermissionsSetting
        .getChildren()
        .addAll(
            title("ROLE PERMISSION", selectAllRolePermissionsCheckbox),
            userPermissionsSettingCheckboxes);
    return userPermissionsSetting;
  }

  private VBox getProductsSetting() {
    // Controls CRUD operations on setting Products.
    productsSetting.getStyleClass().add("card");
    productsSetting.setPadding(new Insets(10));

    viewProductsCheckbox.setText("View Products");
    editProductsCheckbox.setText("Edit Products");
    productBarcodesCheckbox.setText("Edit Barcodes");
    productCategoriesCheckbox.setText("Access Product Categories");
    productUnitsCheckbox.setText("Edit Units of Measure");
    createProductCheckbox.setText("Create Products");
    deleteProductCheckbox.setText("Delete Products");
    productImportsCheckbox.setText("Import Products");
    productBrandsCheckbox.setText("Edit Brands");

    productsSettingCheckboxes.getColumnConstraints().addAll(col1, col2);

    productsSettingCheckboxes.addColumn(
        0,
        viewProductsCheckbox,
        editProductsCheckbox,
        productBarcodesCheckbox,
        productCategoriesCheckbox,
        productUnitsCheckbox);
    productsSettingCheckboxes.addColumn(
        1,
        createProductCheckbox,
        deleteProductCheckbox,
        productImportsCheckbox,
        productBrandsCheckbox);

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
              if (!newVal) selectAllProductsCheckbox.setSelected(false);
            });
    editProductsCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllProductsCheckbox.setSelected(false);
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
              if (!newVal) selectAllProductsCheckbox.setSelected(false);
            });
    productUnitsCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllProductsCheckbox.setSelected(false);
            });
    createProductCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllProductsCheckbox.setSelected(false);
            });
    deleteProductCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllProductsCheckbox.setSelected(false);
            });
    productImportsCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllProductsCheckbox.setSelected(false);
            });
    productBrandsCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllProductsCheckbox.setSelected(false);
            });

    productsSettingCheckboxes.setHgap(GRID_HGAP);
    productsSettingCheckboxes.setVgap(GRID_VGAP);

    productsSetting
        .getChildren()
        .addAll(title("PRODUCT", selectAllProductsCheckbox), productsSettingCheckboxes);
    return productsSetting;
  }

  private VBox getAdjustmentSetting() {
    // Controls CRUD operations on Adjustments.
    adjustmentSetting.getStyleClass().add("card");
    adjustmentSetting.setPadding(new Insets(10));

    viewAdjustmentsCheckbox.setText("View Adjustments");
    editAdjustmentsCheckbox.setText("Edit Adjustments");
    createAdjustmentCheckbox.setText("Create Adjustments");
    deleteAdjustmentCheckbox.setText("Delete Adjustments");

    adjustmentSettingCheckboxes.getColumnConstraints().addAll(col1, col2);

    adjustmentSettingCheckboxes.addColumn(0, viewAdjustmentsCheckbox, editAdjustmentsCheckbox);
    adjustmentSettingCheckboxes.addColumn(1, createAdjustmentCheckbox, deleteAdjustmentCheckbox);

    adjustmentSettingCheckboxes.setHgap(GRID_HGAP);
    adjustmentSettingCheckboxes.setVgap(GRID_VGAP);

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
              if (!newVal) selectAllAdjustmentsCheckbox.setSelected(false);
            });
    editAdjustmentsCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllAdjustmentsCheckbox.setSelected(false);
            });
    createAdjustmentCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllAdjustmentsCheckbox.setSelected(false);
            });
    deleteAdjustmentCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllAdjustmentsCheckbox.setSelected(false);
            });

    adjustmentSetting
        .getChildren()
        .addAll(title("ADJUSTMENT", selectAllAdjustmentsCheckbox), adjustmentSettingCheckboxes);
    return adjustmentSetting;
  }

  private VBox getTransferSetting() {
    // Controls CRUD operations on Transfer.
    transferSetting.getStyleClass().add("card");
    transferSetting.setPadding(new Insets(10));

    viewTransfersCheckbox.setText("View Transfers");
    editTransfersCheckbox.setText("Edit Transfers");
    createTransferCheckbox.setText("Create Transfers");
    deleteTransferCheckbox.setText("Delete Transfers");

    transferSettingCheckboxes.getColumnConstraints().addAll(col1, col2);

    transferSettingCheckboxes.addColumn(0, viewTransfersCheckbox, editTransfersCheckbox);
    transferSettingCheckboxes.addColumn(1, createTransferCheckbox, deleteTransferCheckbox);

    transferSettingCheckboxes.setHgap(GRID_HGAP);
    transferSettingCheckboxes.setVgap(GRID_VGAP);

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
              if (!newVal) selectAllTransfersCheckbox.setSelected(false);
            });
    editTransfersCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllTransfersCheckbox.setSelected(false);
            });
    createTransferCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllTransfersCheckbox.setSelected(false);
            });
    deleteTransferCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllTransfersCheckbox.setSelected(false);
            });

    transferSetting
        .getChildren()
        .addAll(title("TRANSFER", selectAllTransfersCheckbox), transferSettingCheckboxes);
    return transferSetting;
  }

  private VBox getExpenseSetting() {
    // Controls CRUD operations on Expense.
    expenseSetting.getStyleClass().add("card");
    expenseSetting.setPadding(new Insets(10));

    viewExpensesCheckbox.setText("View Expenses");
    editExpensesCheckbox.setText("Edit Expenses");
    createExpenseCheckbox.setText("Create Expenses");
    deleteExpenseCheckbox.setText("Delete Expenses");

    expenseSettingCheckboxes.getColumnConstraints().addAll(col1, col2);

    expenseSettingCheckboxes.addColumn(0, viewExpensesCheckbox, editExpensesCheckbox);
    expenseSettingCheckboxes.addColumn(1, createExpenseCheckbox, deleteExpenseCheckbox);

    expenseSettingCheckboxes.setHgap(GRID_HGAP);
    expenseSettingCheckboxes.setVgap(GRID_VGAP);

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
              if (!newVal) selectAllExpensesCheckbox.setSelected(false);
            });
    editExpensesCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllExpensesCheckbox.setSelected(false);
            });
    createExpenseCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllExpensesCheckbox.setSelected(false);
            });
    deleteExpenseCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllExpensesCheckbox.setSelected(false);
            });

    expenseSetting
        .getChildren()
        .addAll(title("EXPENSE", selectAllExpensesCheckbox), expenseSettingCheckboxes);
    return expenseSetting;
  }

  private VBox getSaleSetting() {
    // Controls CRUD operations on Sale.
    saleSetting.getStyleClass().add("card");
    saleSetting.setPadding(new Insets(10));

    viewSalesCheckbox.setText("View Sales");
    editSalesCheckbox.setText("Edit Sales");
    createSaleCheckbox.setText("Create Sales");
    deleteSaleCheckbox.setText("Delete Sales");

    saleSettingCheckboxes.getColumnConstraints().addAll(col1, col2);

    saleSettingCheckboxes.addColumn(0, viewSalesCheckbox, editSalesCheckbox);
    saleSettingCheckboxes.addColumn(1, createSaleCheckbox, deleteSaleCheckbox);

    saleSettingCheckboxes.setHgap(GRID_HGAP);
    saleSettingCheckboxes.setVgap(GRID_VGAP);

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
              if (!newVal) selectAllSalesCheckbox.setSelected(false);
            });
    editSalesCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllSalesCheckbox.setSelected(false);
            });
    createSaleCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllSalesCheckbox.setSelected(false);
            });
    deleteSaleCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllSalesCheckbox.setSelected(false);
            });

    saleSetting.getChildren().addAll(title("SALE", selectAllSalesCheckbox), saleSettingCheckboxes);
    return saleSetting;
  }

  private VBox getPurchaseSetting() {
    // Controls CRUD operations on Purchase.
    purchaseSetting.getStyleClass().add("card");
    purchaseSetting.setPadding(new Insets(10));

    viewPurchasesCheckbox.setText("View Purchases");
    editPurchasesCheckbox.setText("Edit Purchases");
    createPurchaseCheckbox.setText("Create Purchases");
    deletePurchaseCheckbox.setText("Delete Purchases");

    purchaseSettingCheckboxes.getColumnConstraints().addAll(col1, col2);

    purchaseSettingCheckboxes.addColumn(0, viewPurchasesCheckbox, editPurchasesCheckbox);
    purchaseSettingCheckboxes.addColumn(1, createPurchaseCheckbox, deletePurchaseCheckbox);

    purchaseSettingCheckboxes.setHgap(GRID_HGAP);
    purchaseSettingCheckboxes.setVgap(GRID_VGAP);

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
              if (!newVal) selectAllPurchasesCheckbox.setSelected(false);
            });
    editPurchasesCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllPurchasesCheckbox.setSelected(false);
            });
    createPurchaseCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllPurchasesCheckbox.setSelected(false);
            });
    deletePurchaseCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllPurchasesCheckbox.setSelected(false);
            });

    purchaseSetting
        .getChildren()
        .addAll(title("PURCHASE", selectAllPurchasesCheckbox), purchaseSettingCheckboxes);
    return purchaseSetting;
  }

  private VBox getRequisitionSetting() {
    // Controls CRUD operations on Requisition.
    requisitionSetting.getStyleClass().add("card");
    requisitionSetting.setPadding(new Insets(10));

    viewRequisitionCheckbox.setText("View Requisitions");
    editRequisitionCheckbox.setText("Edit Requisitions");
    createRequisitionCheckbox.setText("Create Requisitions");
    deleteRequisitionCheckbox.setText("Delete Requisitions");

    requisitionSettingCheckboxes.getColumnConstraints().addAll(col1, col2);

    requisitionSettingCheckboxes.addColumn(0, viewRequisitionCheckbox, editRequisitionCheckbox);
    requisitionSettingCheckboxes.addColumn(1, createRequisitionCheckbox, deleteRequisitionCheckbox);

    requisitionSettingCheckboxes.setHgap(GRID_HGAP);
    requisitionSettingCheckboxes.setVgap(GRID_VGAP);

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
              if (!newVal) selectAllRequisitionsCheckbox.setSelected(false);
            });
    editRequisitionCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllRequisitionsCheckbox.setSelected(false);
            });
    createRequisitionCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllRequisitionsCheckbox.setSelected(false);
            });
    deleteRequisitionCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllRequisitionsCheckbox.setSelected(false);
            });

    requisitionSetting
        .getChildren()
        .addAll(title("REQUISITION", selectAllRequisitionsCheckbox), requisitionSettingCheckboxes);
    return requisitionSetting;
  }

  private VBox getStockInSetting() {
    // Controls CRUD operations on StockIns.
    stockInSetting.getStyleClass().add("card");
    stockInSetting.setPadding(new Insets(10));

    viewStockInCheckbox.setText("View Stock Ins");
    editStockInCheckbox.setText("Edit Stock Ins");
    createStockInCheckbox.setText("Create Stock Ins");
    deleteStockInCheckbox.setText("Delete Stock Ins");

    stockInSettingCheckboxes.getColumnConstraints().addAll(col1, col2);

    stockInSettingCheckboxes.addColumn(0, viewStockInCheckbox, editStockInCheckbox);
    stockInSettingCheckboxes.addColumn(1, createStockInCheckbox, deleteStockInCheckbox);

    stockInSettingCheckboxes.setHgap(GRID_HGAP);
    stockInSettingCheckboxes.setVgap(GRID_VGAP);

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
              if (!newVal) selectAllStockInsCheckbox.setSelected(false);
            });
    editStockInCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllStockInsCheckbox.setSelected(false);
            });
    createStockInCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllStockInsCheckbox.setSelected(false);
            });
    deleteStockInCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllStockInsCheckbox.setSelected(false);
            });

    stockInSetting
        .getChildren()
        .addAll(title("STOCK IN", selectAllStockInsCheckbox), stockInSettingCheckboxes);
    return stockInSetting;
  }

  private VBox getQuotationSetting() {
    // Controls CRUD operations on Quotation.
    quotationSetting.getStyleClass().add("card");
    quotationSetting.setPadding(new Insets(10));

    viewQuotationsCheckbox.setText("View Quotations");
    editQuotationsCheckbox.setText("Edit Quotations");
    createQuotationCheckbox.setText("Create Quotations");
    deleteQuotationCheckbox.setText("Delete Quotations");

    quotationSettingCheckboxes.getColumnConstraints().addAll(col1, col2);

    quotationSettingCheckboxes.addColumn(0, viewQuotationsCheckbox, editQuotationsCheckbox);
    quotationSettingCheckboxes.addColumn(1, createQuotationCheckbox, deleteQuotationCheckbox);

    quotationSettingCheckboxes.setHgap(GRID_HGAP);
    quotationSettingCheckboxes.setVgap(GRID_VGAP);

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
              if (!newVal) selectAllQuotationsCheckbox.setSelected(false);
            });
    editQuotationsCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllQuotationsCheckbox.setSelected(false);
            });
    createQuotationCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllQuotationsCheckbox.setSelected(false);
            });
    deleteQuotationCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllQuotationsCheckbox.setSelected(false);
            });

    quotationSetting
        .getChildren()
        .addAll(title("QUOTATION", selectAllQuotationsCheckbox), quotationSettingCheckboxes);
    return quotationSetting;
  }

  private VBox getSaleReturnSetting() {
    // Controls CRUD operations on SaleReturn.
    saleReturnSetting.getStyleClass().add("card");
    saleReturnSetting.setPadding(new Insets(10));

    viewSaleReturnsCheckbox.setText("View Sales Returns");
    editSaleReturnsCheckbox.setText("Edit Sales Returns");
    createSaleReturnCheckbox.setText("Create Sales Returns");
    deleteSaleReturnCheckbox.setText("Delete Sales Returns");

    saleReturnSettingCheckboxes.getColumnConstraints().addAll(col1, col2);

    saleReturnSettingCheckboxes.addColumn(0, viewSaleReturnsCheckbox, editSaleReturnsCheckbox);
    saleReturnSettingCheckboxes.addColumn(1, createSaleReturnCheckbox, deleteSaleReturnCheckbox);

    saleReturnSettingCheckboxes.setHgap(GRID_HGAP);
    saleReturnSettingCheckboxes.setVgap(GRID_VGAP);

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
              if (!newVal) selectAllSalesReturnsCheckbox.setSelected(false);
            });
    editSaleReturnsCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllSalesReturnsCheckbox.setSelected(false);
            });
    createSaleReturnCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllSalesReturnsCheckbox.setSelected(false);
            });
    deleteSaleReturnCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllSalesReturnsCheckbox.setSelected(false);
            });

    saleReturnSetting
        .getChildren()
        .addAll(title("SALES RETURN", selectAllSalesReturnsCheckbox), saleReturnSettingCheckboxes);
    return saleReturnSetting;
  }

  private VBox getPurchaseReturnSetting() {
    // Controls CRUD operations on PurchaseReturn.
    purchaseReturnSetting.getStyleClass().add("card");
    purchaseReturnSetting.setPadding(new Insets(10));

    viewPurchaseReturnsCheckbox.setText("View Purchase Returns");
    editPurchaseReturnsCheckbox.setText("Edit Purchase Returns");
    createPurchaseReturnCheckbox.setText("Create Purchase Returns");
    deletePurchaseReturnCheckbox.setText("Delete Purchase Returns");

    purchaseReturnSettingCheckboxes.getColumnConstraints().addAll(col1, col2);

    purchaseReturnSettingCheckboxes.addColumn(
        0, viewPurchaseReturnsCheckbox, editPurchaseReturnsCheckbox);
    purchaseReturnSettingCheckboxes.addColumn(
        1, createPurchaseReturnCheckbox, deletePurchaseReturnCheckbox);

    purchaseReturnSettingCheckboxes.setHgap(GRID_HGAP);
    purchaseReturnSettingCheckboxes.setVgap(GRID_VGAP);

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
              if (!newVal) selectAllPurchasesReturnsCheckbox.setSelected(false);
            });
    editPurchaseReturnsCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllPurchasesReturnsCheckbox.setSelected(false);
            });
    createPurchaseReturnCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllPurchasesReturnsCheckbox.setSelected(false);
            });
    deletePurchaseReturnCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllPurchasesReturnsCheckbox.setSelected(false);
            });

    purchaseReturnSetting
        .getChildren()
        .addAll(
            title("PURCHASES RETURN", selectAllPurchasesReturnsCheckbox),
            purchaseReturnSettingCheckboxes);
    return purchaseReturnSetting;
  }

  private VBox getPaymentsSaleSetting() {
    // Controls CRUD operations on Payments Sale.
    paymentSaleSetting.getStyleClass().add("card");
    paymentSaleSetting.setPadding(new Insets(10));

    viewPaymentsSalesCheckbox.setText("View Payments Sales");
    editPaymentsSalesCheckbox.setText("Edit Payments Sales");
    createPaymentsSaleCheckbox.setText("Create Payments Sales");
    deletePaymentsSaleCheckbox.setText("Delete Payments Sales");

    paymentSaleSettingCheckboxes.getColumnConstraints().addAll(col1, col2);

    paymentSaleSettingCheckboxes.addColumn(0, viewPaymentsSalesCheckbox, editPaymentsSalesCheckbox);
    paymentSaleSettingCheckboxes.addColumn(
        1, createPaymentsSaleCheckbox, deletePaymentsSaleCheckbox);

    paymentSaleSettingCheckboxes.setHgap(GRID_HGAP);
    paymentSaleSettingCheckboxes.setVgap(GRID_VGAP);

    selectAllPaymentsSalesCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (selectAllPaymentsSalesCheckbox.isFocused()) {
                viewPaymentsSalesCheckbox.setSelected(newVal);
                editPaymentsSalesCheckbox.setSelected(newVal);
                createPaymentsSaleCheckbox.setSelected(newVal);
                deletePaymentsSaleCheckbox.setSelected(newVal);
              }
            });

    viewPaymentsSalesCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllPaymentsSalesCheckbox.setSelected(false);
            });
    editPaymentsSalesCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllPaymentsSalesCheckbox.setSelected(false);
            });
    createPaymentsSaleCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllPaymentsSalesCheckbox.setSelected(false);
            });
    deletePaymentsSaleCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllPaymentsSalesCheckbox.setSelected(false);
            });

    paymentSaleSetting
        .getChildren()
        .addAll(
            title("PAYMENTS SALE", selectAllPaymentsSalesCheckbox), paymentSaleSettingCheckboxes);
    return paymentSaleSetting;
  }

  private VBox getPaymentsPurchaseSetting() {
    // Controls CRUD operations on Payments Purchase.
    paymentPurchaseSetting.getStyleClass().add("card");
    paymentPurchaseSetting.setPadding(new Insets(10));

    viewPaymentsPurchasesCheckbox.setText("View Payments Purchases");
    editPaymentsPurchasesCheckbox.setText("Edit Payments Purchases");
    createPaymentsPurchaseCheckbox.setText("Create Payments Purchases");
    deletePaymentsPurchaseCheckbox.setText("Delete Payments Purchases");

    paymentPurchaseSettingCheckboxes.getColumnConstraints().addAll(col1, col2);

    paymentPurchaseSettingCheckboxes.addColumn(
        0, viewPaymentsPurchasesCheckbox, editPaymentsPurchasesCheckbox);
    paymentPurchaseSettingCheckboxes.addColumn(
        1, createPaymentsPurchaseCheckbox, deletePaymentsPurchaseCheckbox);

    paymentPurchaseSettingCheckboxes.setHgap(GRID_HGAP);
    paymentPurchaseSettingCheckboxes.setVgap(GRID_VGAP);

    selectAllPaymentsPurchasesCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (selectAllPaymentsPurchasesCheckbox.isFocused()) {
                viewPaymentsPurchasesCheckbox.setSelected(newVal);
                editPaymentsPurchasesCheckbox.setSelected(newVal);
                createPaymentsPurchaseCheckbox.setSelected(newVal);
                deletePaymentsPurchaseCheckbox.setSelected(newVal);
              }
            });

    viewPaymentsPurchasesCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllPaymentsPurchasesCheckbox.setSelected(false);
            });
    editPaymentsPurchasesCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllPaymentsPurchasesCheckbox.setSelected(false);
            });
    createPaymentsPurchaseCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllPaymentsPurchasesCheckbox.setSelected(false);
            });
    deletePaymentsPurchaseCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllPaymentsPurchasesCheckbox.setSelected(false);
            });

    paymentPurchaseSetting
        .getChildren()
        .addAll(
            title("PAYMENTS PURCHASE", selectAllPaymentsPurchasesCheckbox),
            paymentPurchaseSettingCheckboxes);
    return paymentPurchaseSetting;
  }

  private VBox getPaymentsReturnSetting() {
    // Controls CRUD operations on Payments Returns.
    paymentReturnSetting.getStyleClass().add("card");
    paymentReturnSetting.setPadding(new Insets(10));

    viewPaymentsReturnsCheckbox.setText("View Payments Returns");
    editPaymentsReturnsCheckbox.setText("Edit Payments Returns");
    createPaymentsReturnCheckbox.setText("Create Payments Returns");
    deletePaymentsReturnCheckbox.setText("Delete Payments Returns");

    paymentReturnSettingCheckboxes.getColumnConstraints().addAll(col1, col2);

    paymentReturnSettingCheckboxes.addColumn(
        0, viewPaymentsReturnsCheckbox, editPaymentsReturnsCheckbox);
    paymentReturnSettingCheckboxes.addColumn(
        1, createPaymentsReturnCheckbox, deletePaymentsReturnCheckbox);

    paymentReturnSettingCheckboxes.setHgap(GRID_HGAP);
    paymentReturnSettingCheckboxes.setVgap(GRID_VGAP);

    selectAllPaymentsReturnsCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (selectAllPaymentsReturnsCheckbox.isFocused()) {
                viewPaymentsReturnsCheckbox.setSelected(newVal);
                editPaymentsReturnsCheckbox.setSelected(newVal);
                createPaymentsReturnCheckbox.setSelected(newVal);
                deletePaymentsReturnCheckbox.setSelected(newVal);
              }
            });

    viewPaymentsReturnsCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllPaymentsReturnsCheckbox.setSelected(false);
            });
    editPaymentsReturnsCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllPaymentsReturnsCheckbox.setSelected(false);
            });
    createPaymentsReturnCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllPaymentsReturnsCheckbox.setSelected(false);
            });
    deletePaymentsReturnCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllPaymentsReturnsCheckbox.setSelected(false);
            });

    paymentReturnSetting
        .getChildren()
        .addAll(
            title("PAYMENTS RETURN", selectAllPaymentsReturnsCheckbox),
            paymentReturnSettingCheckboxes);
    return paymentReturnSetting;
  }

  private VBox getCustomerSetting() {
    // Controls CRUD operations on Customer.
    customerSetting.getStyleClass().add("card");
    customerSetting.setPadding(new Insets(10));

    viewCustomersCheckbox.setText("View Customers");
    editCustomersCheckbox.setText("Edit Customers");
    createCustomerCheckbox.setText("Create Customers");
    deleteCustomerCheckbox.setText("Delete Customers");
    importCustomersCheckbox.setText("Import Customers");
    payAllSellDueCheckbox.setText("Pay all sell due at a time");
    payAllSellReturnDueCheckbox.setText("Pay all sell return due at a time");

    customerSettingCheckboxes.getColumnConstraints().addAll(col1, col2);

    customerSettingCheckboxes.addRow(0, viewCustomersCheckbox, createCustomerCheckbox);
    customerSettingCheckboxes.addRow(1, editCustomersCheckbox, deleteCustomerCheckbox);
    customerSettingCheckboxes.addRow(2, importCustomersCheckbox);
    customerSettingCheckboxes.add(payAllSellDueCheckbox, 0, 3, 2, 1);
    customerSettingCheckboxes.add(payAllSellReturnDueCheckbox, 0, 4, 2, 1);

    customerSettingCheckboxes.setHgap(GRID_HGAP);
    customerSettingCheckboxes.setVgap(GRID_VGAP);

    selectAllCustomersCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (selectAllCustomersCheckbox.isFocused()) {
                viewCustomersCheckbox.setSelected(newVal);
                editCustomersCheckbox.setSelected(newVal);
                importCustomersCheckbox.setSelected(newVal);
                payAllSellDueCheckbox.setSelected(newVal);
                payAllSellReturnDueCheckbox.setSelected(newVal);
                createCustomerCheckbox.setSelected(newVal);
                deleteCustomerCheckbox.setSelected(newVal);
              }
            });

    viewCustomersCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllCustomersCheckbox.setSelected(false);
            });
    editCustomersCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllCustomersCheckbox.setSelected(false);
            });
    importCustomersCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllCustomersCheckbox.setSelected(false);
            });
    payAllSellDueCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllCustomersCheckbox.setSelected(false);
            });
    payAllSellReturnDueCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllCustomersCheckbox.setSelected(false);
            });
    createCustomerCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllCustomersCheckbox.setSelected(false);
            });
    deleteCustomerCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllCustomersCheckbox.setSelected(false);
            });

    customerSetting
        .getChildren()
        .addAll(title("CUSTOMER", selectAllCustomersCheckbox), customerSettingCheckboxes);
    return customerSetting;
  }

  private VBox getSupplierSetting() {
    // Controls CRUD operations on Suppliers.
    supplierSetting.getStyleClass().add("card");
    supplierSetting.setPadding(new Insets(10));

    viewSuppliersCheckbox.setText("View Suppliers");
    editSuppliersCheckbox.setText("Edit Suppliers");
    createSupplierCheckbox.setText("Create Suppliers");
    deleteSupplierCheckbox.setText("Delete Suppliers");
    importSuppliersCheckbox.setText("Import Suppliers");
    payAllPurchaseDueCheckbox.setText("Pay all purchase due at a time");
    payAllPurchaseReturnDueCheckbox.setText("Pay all purchase return due at a time");

    supplierSettingCheckboxes.getColumnConstraints().addAll(col1, col2);

    supplierSettingCheckboxes.addRow(0, viewSuppliersCheckbox, createSupplierCheckbox);
    supplierSettingCheckboxes.addRow(1, editSuppliersCheckbox, deleteSupplierCheckbox);
    supplierSettingCheckboxes.addRow(2, importSuppliersCheckbox);
    supplierSettingCheckboxes.add(payAllPurchaseDueCheckbox, 0, 3, 2, 1);
    supplierSettingCheckboxes.add(payAllPurchaseReturnDueCheckbox, 0, 4, 2, 1);

    supplierSettingCheckboxes.setHgap(GRID_HGAP);
    supplierSettingCheckboxes.setVgap(GRID_VGAP);

    selectAllSuppliersCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (selectAllSuppliersCheckbox.isFocused()) {
                viewSuppliersCheckbox.setSelected(newVal);
                editSuppliersCheckbox.setSelected(newVal);
                importSuppliersCheckbox.setSelected(newVal);
                payAllPurchaseDueCheckbox.setSelected(newVal);
                payAllPurchaseReturnDueCheckbox.setSelected(newVal);
                createSupplierCheckbox.setSelected(newVal);
                deleteSupplierCheckbox.setSelected(newVal);
              }
            });

    viewSuppliersCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllSuppliersCheckbox.setSelected(false);
            });
    editSuppliersCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllSuppliersCheckbox.setSelected(false);
            });
    importSuppliersCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllSuppliersCheckbox.setSelected(false);
            });
    payAllPurchaseDueCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllSuppliersCheckbox.setSelected(false);
            });
    payAllPurchaseReturnDueCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllSuppliersCheckbox.setSelected(false);
            });
    createSupplierCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllSuppliersCheckbox.setSelected(false);
            });
    deleteSupplierCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllSuppliersCheckbox.setSelected(false);
            });

    supplierSetting
        .getChildren()
        .addAll(title("SUPPLIER", selectAllSuppliersCheckbox), supplierSettingCheckboxes);
    return supplierSetting;
  }

  private VBox getReportSetting() {
    // Controls CRUD operations on Reports.
    reportSetting.getStyleClass().add("card");
    reportSetting.setPadding(new Insets(10));

    paymentSalesCheckbox.setText("Access Payments Sales Reports");
    paymentPurchasesCheckbox.setText("Access Payments Purchases Reports");
    saleReturnPaymentsCheckbox.setText("Access Sales Returns Payments Reports");
    purchaseReturnPaymentsCheckbox.setText("Access Purchase Returns Payments Reports");
    saleReportCheckbox.setText("Access Sales Reports");
    purchaseReportCheckbox.setText("Access Purchase Reports");
    customerReportCheckbox.setText("Access Customer Reports");
    supplierReportCheckbox.setText("Access Supplier Reports");
    profitAndLossCheckbox.setText("Access Profits and Losses Report");
    productQuantityAlertsCheckbox.setText("Access Product Quantity Reports");
    warehouseStockChartCheckbox.setText("Access Branch Stock Chart Reports");
    topSellingProductsCheckbox.setText("Access Top Selling Products Reports");
    bestCustomersCheckbox.setText("Access Customer Ranking Reports");
    usersReportCheckbox.setText("Access Users Reports");
    stockReportCheckbox.setText("Access Stock Reports");
    productReportCheckbox.setText("Access Products Reports");
    productSalesReportCheckbox.setText("Access Products Sales Reports");
    productPurchasesReportCheckbox.setText("Access Products Purchases Reports");

    reportSettingCheckboxes.getColumnConstraints().addAll(col1, col2);

    reportSettingCheckboxes.addColumn(
        0,
        paymentSalesCheckbox,
        paymentPurchasesCheckbox,
        saleReturnPaymentsCheckbox,
        purchaseReturnPaymentsCheckbox,
        saleReportCheckbox,
        purchaseReportCheckbox,
        customerReportCheckbox,
        supplierReportCheckbox,
        profitAndLossCheckbox);
    reportSettingCheckboxes.addColumn(
        1,
        productQuantityAlertsCheckbox,
        warehouseStockChartCheckbox,
        topSellingProductsCheckbox,
        bestCustomersCheckbox,
        usersReportCheckbox,
        stockReportCheckbox,
        productReportCheckbox,
        productSalesReportCheckbox,
        productPurchasesReportCheckbox);

    reportSettingCheckboxes.setHgap(GRID_HGAP);
    reportSettingCheckboxes.setVgap(GRID_VGAP);

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
                warehouseStockChartCheckbox.setSelected(newVal);
                topSellingProductsCheckbox.setSelected(newVal);
                bestCustomersCheckbox.setSelected(newVal);
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
              if (!newVal) selectAllReportsCheckbox.setSelected(false);
            });
    paymentPurchasesCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllReportsCheckbox.setSelected(false);
            });
    saleReturnPaymentsCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllReportsCheckbox.setSelected(false);
            });
    purchaseReturnPaymentsCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllReportsCheckbox.setSelected(false);
            });
    saleReportCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllReportsCheckbox.setSelected(false);
            });
    purchaseReportCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllReportsCheckbox.setSelected(false);
            });
    customerReportCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllReportsCheckbox.setSelected(false);
            });
    supplierReportCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllReportsCheckbox.setSelected(false);
            });
    profitAndLossCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllReportsCheckbox.setSelected(false);
            });
    productQuantityAlertsCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllReportsCheckbox.setSelected(false);
            });
    warehouseStockChartCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllReportsCheckbox.setSelected(false);
            });
    topSellingProductsCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllReportsCheckbox.setSelected(false);
            });
    bestCustomersCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllReportsCheckbox.setSelected(false);
            });
    usersReportCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllReportsCheckbox.setSelected(false);
            });
    stockReportCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllReportsCheckbox.setSelected(false);
            });
    productReportCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllReportsCheckbox.setSelected(false);
            });
    productSalesReportCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllReportsCheckbox.setSelected(false);
            });
    productPurchasesReportCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllReportsCheckbox.setSelected(false);
            });

    reportSetting
        .getChildren()
        .addAll(title("REPORT", selectAllReportsCheckbox), reportSettingCheckboxes);
    return reportSetting;
  }

  private VBox getHRMSetting() {
    // Controls CRUD operations on HRM.
    hrmSetting.getStyleClass().add("card");
    hrmSetting.setPadding(new Insets(10));

    viewEmployeeCheckbox.setText("View Employees");
    editEmployeeCheckbox.setText("Edit Employees");
    createEmployeeCheckbox.setText("Create Employees");
    deleteEmployeeCheckbox.setText("Delete Employees");
    CompanyCheckbox.setText("Access Company");
    departmentCheckbox.setText("Access Department");
    designationCheckbox.setText("Access Designation");
    officeShiftCheckbox.setText("Access Office Shift");
    attendanceCheckbox.setText("Access Attendance");
    leaveRequestCheckbox.setText("Access Leave Request");
    holidayCheckbox.setText("Access Holiday");

    hrmSettingCheckboxes.getColumnConstraints().addAll(col1, col2);

    hrmSettingCheckboxes.addColumn(
        0,
        viewEmployeeCheckbox,
        createEmployeeCheckbox,
        CompanyCheckbox,
        designationCheckbox,
        attendanceCheckbox,
        holidayCheckbox);
    hrmSettingCheckboxes.addColumn(
        1,
        editEmployeeCheckbox,
        deleteEmployeeCheckbox,
        departmentCheckbox,
        officeShiftCheckbox,
        leaveRequestCheckbox);

    hrmSettingCheckboxes.setHgap(20);

    selectAllHRMCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (selectAllHRMCheckbox.isFocused()) {
                viewEmployeeCheckbox.setSelected(newVal);
                createEmployeeCheckbox.setSelected(newVal);
                CompanyCheckbox.setSelected(newVal);
                designationCheckbox.setSelected(newVal);
                attendanceCheckbox.setSelected(newVal);
                holidayCheckbox.setSelected(newVal);
                editEmployeeCheckbox.setSelected(newVal);
                deleteEmployeeCheckbox.setSelected(newVal);
                departmentCheckbox.setSelected(newVal);
                officeShiftCheckbox.setSelected(newVal);
                leaveRequestCheckbox.setSelected(newVal);
              }
            });

    viewEmployeeCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllHRMCheckbox.setSelected(false);
            });
    createEmployeeCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllHRMCheckbox.setSelected(false);
            });
    CompanyCheckbox.selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllHRMCheckbox.setSelected(false);
            });
    designationCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllHRMCheckbox.setSelected(false);
            });
    attendanceCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllHRMCheckbox.setSelected(false);
            });
    holidayCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllHRMCheckbox.setSelected(false);
            });
    editEmployeeCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllHRMCheckbox.setSelected(false);
            });
    deleteEmployeeCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllHRMCheckbox.setSelected(false);
            });
    departmentCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllHRMCheckbox.setSelected(false);
            });
    officeShiftCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllHRMCheckbox.setSelected(false);
            });
    leaveRequestCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllHRMCheckbox.setSelected(false);
            });

    hrmSetting.getChildren().addAll(title("HRM", selectAllHRMCheckbox), hrmSettingCheckboxes);
    return hrmSetting;
  }

  private VBox getSettingSetting() {
    // Controls CRUD operations on Settings.
    settingSetting.getStyleClass().add("card");
    settingSetting.setPadding(new Insets(10));

    viewSystemSettingCheckbox.setText("View System Settings");
    viewPOSSettingsCheckbox.setText("View POS Settings");
    viewCurrencyCheckbox.setText("View Currency Settings");
    viewBranchCheckbox.setText("View Branch Settings");
    viewBackupCheckbox.setText("View Backup Settings");

    settingSettingCheckboxes.getColumnConstraints().addAll(col1, col2);
    settingSettingCheckboxes.addColumn(
        0, viewSystemSettingCheckbox, viewCurrencyCheckbox, viewBackupCheckbox);
    settingSettingCheckboxes.addColumn(1, viewPOSSettingsCheckbox, viewBranchCheckbox);

    settingSettingCheckboxes.setHgap(GRID_HGAP);
    settingSettingCheckboxes.setVgap(GRID_VGAP);

    selectAllSettingsCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (selectAllSettingsCheckbox.isFocused()) {
                viewSystemSettingCheckbox.setSelected(newVal);
                viewPOSSettingsCheckbox.setSelected(newVal);
                viewCurrencyCheckbox.setSelected(newVal);
                viewBranchCheckbox.setSelected(newVal);
                viewBackupCheckbox.setSelected(newVal);
              }
            });
    viewSystemSettingCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllSettingsCheckbox.setSelected(false);
            });
    viewPOSSettingsCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllSettingsCheckbox.setSelected(false);
            });
    viewCurrencyCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllSettingsCheckbox.setSelected(false);
            });
    viewBranchCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllSettingsCheckbox.setSelected(false);
            });
    viewBackupCheckbox
        .selectedProperty()
        .addListener(
            (observableVal, oldVal, newVal) -> {
              if (!newVal) selectAllSettingsCheckbox.setSelected(false);
            });

    settingSetting
        .getChildren()
        .addAll(title("SETTINGS", selectAllSettingsCheckbox), settingSettingCheckboxes);
    return settingSetting;
  }

  private void getRoleSettings() {
    roleSettings.getColumnConstraints().addAll(col1, col2);

    roleSettings.setPadding(new Insets(5));

    roleSettings.add(validationWrapper(roleNameInputField), 0, 0, 2, 1);
    roleSettings.addRow(1, getDashboardRoleSetting(), getPOSSetting());
    roleSettings.addRow(2, getRequisitionSetting(), getPurchaseSetting());
    roleSettings.addRow(3, getTransferSetting(), getStockInSetting());
    roleSettings.addRow(4, getQuotationSetting(), getSaleSetting());
    roleSettings.addRow(5, getAdjustmentSetting(), getPurchaseReturnSetting());
    roleSettings.addRow(6, getPaymentsPurchaseSetting(), getPaymentsSaleSetting());
    roleSettings.addRow(7, getExpenseSetting(), getPaymentsReturnSetting());
    roleSettings.addRow(8, getUserPermissionsSetting(), getUserMgtSetting());
    roleSettings.addRow(9, getSaleReturnSetting(), getSettingSetting());
    roleSettings.addRow(10, getSupplierSetting(), getCustomerSetting());
    roleSettings.addRow(11, getProductsSetting());
    roleSettings.add(getReportSetting(), 0, 12, 2, 1);

    roleSettings.setHgap(20);
    roleSettings.setVgap(40);
  }

  private VBox title(String labelText, Node selectAllNode) {
    VBox headerBox = new VBox();
    HBox titleHolder = new HBox();
    Separator separator = new Separator();
    Label lbl1 = new Label();
    Label lbl2 = new Label();

    lbl1.setText(labelText);
    lbl1.getStyleClass().add("title");
    lbl2.setText("Permissions");
    lbl2.getStyleClass().add("title-prefix");

    titleHolder.setSpacing(10);
    headerBox.setSpacing(5);

    if (!Objects.equals(selectAllNode, null)) {
      titleHolder.getChildren().addAll(lbl1, lbl2, new Spacer(), selectAllNode);
    } else {
      titleHolder.getChildren().addAll(lbl1, lbl2);
    }

    headerBox.getChildren().addAll(titleHolder, separator);
    return headerBox;
  }

  public void save() {}

  public void close() {
    Navigation.navigate(Pages.getRoleSettingsPane(), (StackPane) roleSettingsHolder.getParent());
  }
}
