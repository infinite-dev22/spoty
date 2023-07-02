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
import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.values.numbers.Constants.GRID_HGAP;
import static org.infinite.spoty.values.numbers.Constants.GRID_VGAP;

import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXStepper;
import io.github.palexdev.materialfx.controls.MFXStepperToggle;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.enums.FloatMode;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.MFXValidator;
import io.github.palexdev.materialfx.validation.Severity;
import io.github.palexdev.mfxcomponents.controls.checkbox.MFXCheckBox;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.infinite.spoty.components.navigation.Navigation;
import org.infinite.spoty.components.navigation.Pages;

public class RoleSettingsFormController implements Initializable {
  private static RoleSettingsFormController instance;
  private final MFXTextField roleNameInputField;
  private final MFXTextField roleDescriptionInputField;
  private final MFXCheckBox dashboardCheckbox;
  private final MFXCheckBox accessPOSCheckbox;
  // Users
  private final MFXCheckBox viewUsersCheckbox;
  private final MFXCheckBox editUsersCheckbox;
  private final MFXCheckBox createUserCheckbox;
  private final MFXCheckBox deleteUserCheckbox;
  // User Permissions
  private final MFXCheckBox viewUserPermissionsCheckbox;
  private final MFXCheckBox editUserPermissionsCheckbox;
  private final MFXCheckBox createUserPermissionCheckbox;
  private final MFXCheckBox deleteUserPermissionCheckbox;
  // Products
  private final MFXCheckBox viewProductsCheckbox;
  private final MFXCheckBox editProductsCheckbox;
  private final MFXCheckBox createProductCheckbox;
  private final MFXCheckBox deleteProductCheckbox;
  private final MFXCheckBox productBarcodesCheckbox;
  private final MFXCheckBox productCategoriesCheckbox;
  private final MFXCheckBox productUnitsCheckbox;
  private final MFXCheckBox productImportsCheckbox;
  private final MFXCheckBox productBrandsCheckbox;
  // Adjustments
  private final MFXCheckBox viewAdjustmentsCheckbox;
  private final MFXCheckBox editAdjustmentsCheckbox;
  private final MFXCheckBox createAdjustmentCheckbox;
  private final MFXCheckBox deleteAdjustmentCheckbox;
  // Transfers
  private final MFXCheckBox viewTransfersCheckbox;
  private final MFXCheckBox editTransfersCheckbox;
  private final MFXCheckBox createTransferCheckbox;
  private final MFXCheckBox deleteTransferCheckbox;
  // Expenses
  private final MFXCheckBox viewExpensesCheckbox;
  private final MFXCheckBox editExpensesCheckbox;
  private final MFXCheckBox createExpenseCheckbox;
  private final MFXCheckBox deleteExpenseCheckbox;
  // Expenses
  private final MFXCheckBox viewSalesCheckbox;
  private final MFXCheckBox editSalesCheckbox;
  private final MFXCheckBox createSaleCheckbox;
  private final MFXCheckBox deleteSaleCheckbox;
  // Expenses
  private final MFXCheckBox viewPurchasesCheckbox;
  private final MFXCheckBox editPurchasesCheckbox;
  private final MFXCheckBox createPurchaseCheckbox;
  private final MFXCheckBox deletePurchaseCheckbox;
  // Quotations
  private final MFXCheckBox viewQuotationsCheckbox;
  private final MFXCheckBox editQuotationsCheckbox;
  private final MFXCheckBox createQuotationCheckbox;
  private final MFXCheckBox deleteQuotationCheckbox;
  // SaleMaster Returns
  private final MFXCheckBox viewSaleReturnsCheckbox;
  private final MFXCheckBox editSaleReturnsCheckbox;
  private final MFXCheckBox createSaleReturnCheckbox;
  private final MFXCheckBox deleteSaleReturnCheckbox;
  // PurchaseMaster Returns
  private final MFXCheckBox viewPurchaseReturnsCheckbox;
  private final MFXCheckBox editPurchaseReturnsCheckbox;
  private final MFXCheckBox createPurchaseReturnCheckbox;
  private final MFXCheckBox deletePurchaseReturnCheckbox;
  // Payment Sales
  private final MFXCheckBox viewPaymentSalesCheckbox;
  private final MFXCheckBox editPaymentSalesCheckbox;
  private final MFXCheckBox createPaymentSaleCheckbox;
  private final MFXCheckBox deletePaymentSaleCheckbox;
  // Payment Purchases
  private final MFXCheckBox viewPaymentPurchasesCheckbox;
  private final MFXCheckBox editPaymentPurchasesCheckbox;
  private final MFXCheckBox createPaymentPurchaseCheckbox;
  private final MFXCheckBox deletePaymentPurchaseCheckbox;
  // Payment Returns
  private final MFXCheckBox viewPaymentReturnsCheckbox;
  private final MFXCheckBox editPaymentReturnsCheckbox;
  private final MFXCheckBox createPaymentReturnCheckbox;
  private final MFXCheckBox deletePaymentReturnCheckbox;
  // Customers
  private final MFXCheckBox viewCustomersCheckbox;
  private final MFXCheckBox editCustomersCheckbox;
  private final MFXCheckBox createCustomerCheckbox;
  private final MFXCheckBox deleteCustomerCheckbox;
  private final MFXCheckBox importCustomersCheckbox;
  private final MFXCheckBox payAllSellDueCheckbox;
  private final MFXCheckBox payAllSellReturnDueCheckbox;
  // Suppliers
  private final MFXCheckBox viewSuppliersCheckbox;
  private final MFXCheckBox editSuppliersCheckbox;
  private final MFXCheckBox createSupplierCheckbox;
  private final MFXCheckBox deleteSupplierCheckbox;
  private final MFXCheckBox importSuppliersCheckbox;
  private final MFXCheckBox payAllPurchaseDueCheckbox;
  private final MFXCheckBox payAllPurchaseReturnDueCheckbox;
  // Reports
  private final MFXCheckBox paymentSalesCheckbox;
  private final MFXCheckBox paymentPurchasesCheckbox;
  private final MFXCheckBox saleReturnPaymentsCheckbox;
  private final MFXCheckBox purchaseReturnPaymentsCheckbox;
  private final MFXCheckBox saleReportCheckbox;
  private final MFXCheckBox purchaseReportCheckbox;
  private final MFXCheckBox customerReportCheckbox;
  private final MFXCheckBox supplierReportCheckbox;
  private final MFXCheckBox profitAndLossCheckbox;
  private final MFXCheckBox productQuantityAlertsCheckbox;
  private final MFXCheckBox warehouseStockChartCheckbox;
  private final MFXCheckBox topSellingProductsCheckbox;
  private final MFXCheckBox bestCustomersCheckbox;
  private final MFXCheckBox usersReportCheckbox;
  private final MFXCheckBox stockReportCheckbox;
  private final MFXCheckBox productReportCheckbox;
  private final MFXCheckBox productSalesReportCheckbox;
  private final MFXCheckBox productPurchasesReportCheckbox;
  // HRM
  private final MFXCheckBox viewEmployeeCheckbox;
  private final MFXCheckBox editEmployeeCheckbox;
  private final MFXCheckBox createEmployeeCheckbox;
  private final MFXCheckBox deleteEmployeeCheckbox;
  private final MFXCheckBox CompanyCheckbox;
  private final MFXCheckBox departmentCheckbox;
  private final MFXCheckBox designationCheckbox;
  private final MFXCheckBox officeShiftCheckbox;
  private final MFXCheckBox attendanceCheckbox;
  private final MFXCheckBox leaveRequestCheckbox;
  private final MFXCheckBox holidayCheckbox;
  // Settings
  private final MFXCheckBox viewSystemSettingCheckbox;
  private final MFXCheckBox viewPOSSettingsCheckbox;
  private final MFXCheckBox viewCurrencyCheckbox;
  private final MFXCheckBox viewBranchCheckbox;
  private final GridPane roleSettings = new GridPane();
  private final MFXScrollPane roleSettingsScrollPane = new MFXScrollPane();
  private final MFXCheckBox viewBackupCheckbox;
  @FXML private BorderPane roleSettingsHolder;
  @FXML private MFXStepper roleSettingsStepper;

  public RoleSettingsFormController() {
    roleNameInputField = new MFXTextField();
    roleDescriptionInputField = new MFXTextField();

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

    viewPaymentSalesCheckbox = new MFXCheckBox();
    editPaymentSalesCheckbox = new MFXCheckBox();
    createPaymentSaleCheckbox = new MFXCheckBox();
    deletePaymentSaleCheckbox = new MFXCheckBox();

    viewPaymentPurchasesCheckbox = new MFXCheckBox();
    editPaymentPurchasesCheckbox = new MFXCheckBox();
    createPaymentPurchaseCheckbox = new MFXCheckBox();
    deletePaymentPurchaseCheckbox = new MFXCheckBox();

    viewPaymentReturnsCheckbox = new MFXCheckBox();
    editPaymentReturnsCheckbox = new MFXCheckBox();
    createPaymentReturnCheckbox = new MFXCheckBox();
    deletePaymentReturnCheckbox = new MFXCheckBox();

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
  }

  public static RoleSettingsFormController getInstance() {
    if (Objects.equals(instance, null)) instance = new RoleSettingsFormController();
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
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

    roleNameInputField.setFloatMode(FloatMode.BORDER);
    roleNameInputField.setFloatingText("Role name");
    roleNameInputField.setPrefWidth(400);
    roleNameInputField.requestFocus();
    roleNameInputField.getValidator().constraint(emptyConstraint).constraint(lengthConstraint);

    roleDescriptionInputField.setFloatMode(FloatMode.BORDER);
    roleDescriptionInputField.setFloatingText("Role description");
    roleDescriptionInputField.setPrefWidth(400);

    getRoleSettings();

    List<MFXStepperToggle> stepperToggle;
    try {
      stepperToggle = getSteps();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    roleSettingsStepper.getStepperToggles().addAll(stepperToggle);
    roleSettingsStepper.setAnimated(true);
    roleSettingsStepper.setOnLastNext(e -> roleSettingsStepper.setMouseTransparent(true));
  }

  protected VBox validationWrapper(MFXTextField node) {
    Label errorLabel = new Label();
    VBox container = new VBox(5, node, errorLabel);
    container.setAlignment(Pos.CENTER);
    errorLabel.getStyleClass().add("danger-validation-label");

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

    roleSettingsStepper.addEventHandler(
        MFXStepper.MFXStepperEvent.VALIDATION_FAILED_EVENT,
        event -> {
          MFXValidator validator = node.getValidator();
          List<Constraint> validate = validator.validate();
          if (!validate.isEmpty()) {
            errorLabel.setText(validate.get(0).getMessage());
            errorLabel.setVisible(true);
          }
        });
    roleSettingsStepper.addEventHandler(
        MFXStepper.MFXStepperEvent.NEXT_EVENT, event -> errorLabel.setVisible(false));
    return container;
  }

  private VBox getDashboardRoleSetting() {
    VBox dashboardSetting = new VBox();
    dashboardSetting.getStyleClass().add("card");
    dashboardSetting.setPadding(new Insets(10));
    dashboardCheckbox.setText("Access Dashboard");
    dashboardSetting.getChildren().addAll(title("Dashboard"), dashboardCheckbox);
    return dashboardSetting;
  }

  private VBox getPOSSetting() {
    VBox posSetting = new VBox();
    posSetting.getStyleClass().add("card");
    posSetting.setPadding(new Insets(10));
    accessPOSCheckbox.setText("Point Of SaleMaster");
    posSetting.getChildren().addAll(title("Dashboard"), accessPOSCheckbox);
    return posSetting;
  }

  private VBox getUserMgtSetting() {
    // Controls CRUD operations on User Management.
    VBox userMgtSetting = new VBox();
    userMgtSetting.getStyleClass().add("card");
    userMgtSetting.setPadding(new Insets(10));
    GridPane userMgtSettingCheckboxes = new GridPane();

    viewUsersCheckbox.setText("View");
    editUsersCheckbox.setText("Edit");
    createUserCheckbox.setText("Create");
    deleteUserCheckbox.setText("Delete");

    userMgtSettingCheckboxes.addColumn(0, viewUsersCheckbox, editUsersCheckbox);
    userMgtSettingCheckboxes.addColumn(1, createUserCheckbox, deleteUserCheckbox);

    userMgtSettingCheckboxes.setHgap(GRID_HGAP);
    userMgtSettingCheckboxes.setVgap(GRID_VGAP);

    userMgtSetting.getChildren().addAll(title("User Management"), userMgtSettingCheckboxes);
    return userMgtSetting;
  }

  private VBox getUserPermissionsSetting() {
    // Controls CRUD operations on setting User permissions.
    VBox userPermissionsSetting = new VBox();
    userPermissionsSetting.getStyleClass().add("card");
    userPermissionsSetting.setPadding(new Insets(10));
    GridPane userPermissionsSettingCheckboxes = new GridPane();

    viewUserPermissionsCheckbox.setText("View");
    editUserPermissionsCheckbox.setText("Edit");
    createUserPermissionCheckbox.setText("Create");
    deleteUserPermissionCheckbox.setText("Delete");

    userPermissionsSettingCheckboxes.addColumn(
        0, viewUserPermissionsCheckbox, editUserPermissionsCheckbox);
    userPermissionsSettingCheckboxes.addColumn(
        1, createUserPermissionCheckbox, deleteUserPermissionCheckbox);

    userPermissionsSettingCheckboxes.setHgap(GRID_HGAP);
    userPermissionsSettingCheckboxes.setVgap(GRID_VGAP);

    userPermissionsSetting
        .getChildren()
        .addAll(title("User Permissions"), userPermissionsSettingCheckboxes);
    return userPermissionsSetting;
  }

  private VBox getProductsSetting() {
    // Controls CRUD operations on setting Products.
    VBox productsSetting = new VBox();
    productsSetting.getStyleClass().add("card");
    productsSetting.setPadding(new Insets(10));
    GridPane productsSettingCheckboxes = new GridPane();

    viewProductsCheckbox.setText("View");
    editProductsCheckbox.setText("Edit");
    productBarcodesCheckbox.setText("Barcode");
    productCategoriesCheckbox.setText("Category");
    productUnitsCheckbox.setText("Unit");
    createProductCheckbox.setText("Create");
    deleteProductCheckbox.setText("Delete");
    productImportsCheckbox.setText("Import Products");
    productBrandsCheckbox.setText("Brand");

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

    productsSettingCheckboxes.setHgap(GRID_HGAP);
    productsSettingCheckboxes.setVgap(GRID_VGAP);

    productsSetting.getChildren().addAll(title("Products"), productsSettingCheckboxes);
    return productsSetting;
  }

  private VBox getAdjustmentSetting() {
    // Controls CRUD operations on Adjustments.
    VBox adjustmentSetting = new VBox();
    adjustmentSetting.getStyleClass().add("card");
    adjustmentSetting.setPadding(new Insets(10));
    GridPane adjustmentSettingCheckboxes = new GridPane();

    viewAdjustmentsCheckbox.setText("View");
    editAdjustmentsCheckbox.setText("Edit");
    createAdjustmentCheckbox.setText("Create");
    deleteAdjustmentCheckbox.setText("Delete");

    adjustmentSettingCheckboxes.addColumn(0, viewAdjustmentsCheckbox, editAdjustmentsCheckbox);
    adjustmentSettingCheckboxes.addColumn(1, createAdjustmentCheckbox, deleteAdjustmentCheckbox);

    adjustmentSettingCheckboxes.setHgap(GRID_HGAP);
    adjustmentSettingCheckboxes.setVgap(GRID_VGAP);

    adjustmentSetting.getChildren().addAll(title("Adjustment"), adjustmentSettingCheckboxes);
    return adjustmentSetting;
  }

  private VBox getTransferSetting() {
    // Controls CRUD operations on TransferMaster.
    VBox transferSetting = new VBox();
    transferSetting.getStyleClass().add("card");
    transferSetting.setPadding(new Insets(10));
    GridPane transferSettingCheckboxes = new GridPane();

    viewTransfersCheckbox.setText("View");
    editTransfersCheckbox.setText("Edit");
    createTransferCheckbox.setText("Create");
    deleteTransferCheckbox.setText("Delete");

    transferSettingCheckboxes.addColumn(0, viewTransfersCheckbox, editTransfersCheckbox);
    transferSettingCheckboxes.addColumn(1, createTransferCheckbox, deleteTransferCheckbox);

    transferSettingCheckboxes.setHgap(GRID_HGAP);
    transferSettingCheckboxes.setVgap(GRID_VGAP);

    transferSetting.getChildren().addAll(title("TransferMaster"), transferSettingCheckboxes);
    return transferSetting;
  }

  private VBox getExpenseSetting() {
    // Controls CRUD operations on Expense.
    VBox expenseSetting = new VBox();
    expenseSetting.getStyleClass().add("card");
    expenseSetting.setPadding(new Insets(10));
    GridPane expenseSettingCheckboxes = new GridPane();

    viewExpensesCheckbox.setText("View");
    editExpensesCheckbox.setText("Edit");
    createExpenseCheckbox.setText("Create");
    deleteExpenseCheckbox.setText("Delete");

    expenseSettingCheckboxes.addColumn(0, viewExpensesCheckbox, editExpensesCheckbox);
    expenseSettingCheckboxes.addColumn(1, createExpenseCheckbox, deleteExpenseCheckbox);

    expenseSettingCheckboxes.setHgap(GRID_HGAP);
    expenseSettingCheckboxes.setVgap(GRID_VGAP);

    expenseSetting.getChildren().addAll(title("Expenses"), expenseSettingCheckboxes);
    return expenseSetting;
  }

  private VBox getSaleSetting() {
    // Controls CRUD operations on SaleMaster.
    VBox saleSetting = new VBox();
    saleSetting.getStyleClass().add("card");
    saleSetting.setPadding(new Insets(10));
    GridPane saleSettingCheckboxes = new GridPane();

    viewSalesCheckbox.setText("View");
    editSalesCheckbox.setText("Edit");
    createSaleCheckbox.setText("Create");
    deleteSaleCheckbox.setText("Delete");

    saleSettingCheckboxes.addColumn(0, viewSalesCheckbox, editSalesCheckbox);
    saleSettingCheckboxes.addColumn(1, createSaleCheckbox, deleteSaleCheckbox);

    saleSettingCheckboxes.setHgap(GRID_HGAP);
    saleSettingCheckboxes.setVgap(GRID_VGAP);

    saleSetting.getChildren().addAll(title("Sales"), saleSettingCheckboxes);
    return saleSetting;
  }

  private VBox getPurchaseSetting() {
    // Controls CRUD operations on PurchaseMaster.
    VBox purchaseSetting = new VBox();
    purchaseSetting.getStyleClass().add("card");
    purchaseSetting.setPadding(new Insets(10));
    GridPane purchaseSettingCheckboxes = new GridPane();

    viewPurchasesCheckbox.setText("View");
    editPurchasesCheckbox.setText("Edit");
    createPurchaseCheckbox.setText("Create");
    deletePurchaseCheckbox.setText("Delete");

    purchaseSettingCheckboxes.addColumn(0, viewPurchasesCheckbox, editPurchasesCheckbox);
    purchaseSettingCheckboxes.addColumn(1, createPurchaseCheckbox, deletePurchaseCheckbox);

    purchaseSettingCheckboxes.setHgap(GRID_HGAP);
    purchaseSettingCheckboxes.setVgap(GRID_VGAP);

    purchaseSetting.getChildren().addAll(title("Purchases"), purchaseSettingCheckboxes);
    return purchaseSetting;
  }

  private VBox getQuotationSetting() {
    // Controls CRUD operations on Quotation.
    VBox quotationSetting = new VBox();
    quotationSetting.getStyleClass().add("card");
    quotationSetting.setPadding(new Insets(10));
    GridPane quotationSettingCheckboxes = new GridPane();

    viewQuotationsCheckbox.setText("View");
    editQuotationsCheckbox.setText("Edit");
    createQuotationCheckbox.setText("Create");
    deleteQuotationCheckbox.setText("Delete");

    quotationSettingCheckboxes.addColumn(0, viewQuotationsCheckbox, editQuotationsCheckbox);
    quotationSettingCheckboxes.addColumn(1, createQuotationCheckbox, deleteQuotationCheckbox);

    quotationSettingCheckboxes.setHgap(GRID_HGAP);
    quotationSettingCheckboxes.setVgap(GRID_VGAP);

    quotationSetting.getChildren().addAll(title("Quotations"), quotationSettingCheckboxes);
    return quotationSetting;
  }

  private VBox getSaleReturnSetting() {
    // Controls CRUD operations on SaleReturnMaster.
    VBox saleReturnSetting = new VBox();
    saleReturnSetting.getStyleClass().add("card");
    saleReturnSetting.setPadding(new Insets(10));
    GridPane saleReturnSettingCheckboxes = new GridPane();

    viewSaleReturnsCheckbox.setText("View");
    editSaleReturnsCheckbox.setText("Edit");
    createSaleReturnCheckbox.setText("Create");
    deleteSaleReturnCheckbox.setText("Delete");

    saleReturnSettingCheckboxes.addColumn(0, viewSaleReturnsCheckbox, editSaleReturnsCheckbox);
    saleReturnSettingCheckboxes.addColumn(1, createSaleReturnCheckbox, deleteSaleReturnCheckbox);

    saleReturnSettingCheckboxes.setHgap(GRID_HGAP);
    saleReturnSettingCheckboxes.setVgap(GRID_VGAP);

    saleReturnSetting
        .getChildren()
        .addAll(title("SaleMaster Returns"), saleReturnSettingCheckboxes);
    return saleReturnSetting;
  }

  private VBox getPurchaseReturnSetting() {
    // Controls CRUD operations on PurchaseReturnMaster.
    VBox purchaseReturnSetting = new VBox();
    purchaseReturnSetting.getStyleClass().add("card");
    purchaseReturnSetting.setPadding(new Insets(10));
    GridPane purchaseReturnSettingCheckboxes = new GridPane();

    viewPurchaseReturnsCheckbox.setText("View");
    editPurchaseReturnsCheckbox.setText("Edit");
    createPurchaseReturnCheckbox.setText("Create");
    deletePurchaseReturnCheckbox.setText("Delete");

    purchaseReturnSettingCheckboxes.addColumn(
        0, viewPurchaseReturnsCheckbox, editPurchaseReturnsCheckbox);
    purchaseReturnSettingCheckboxes.addColumn(
        1, createPurchaseReturnCheckbox, deletePurchaseReturnCheckbox);

    purchaseReturnSettingCheckboxes.setHgap(GRID_HGAP);
    purchaseReturnSettingCheckboxes.setVgap(GRID_VGAP);

    purchaseReturnSetting
        .getChildren()
        .addAll(title("PurchaseMaster Returns"), purchaseReturnSettingCheckboxes);
    return purchaseReturnSetting;
  }

  private VBox getPaymentSaleSetting() {
    // Controls CRUD operations on Payment SaleMaster.
    VBox paymentSaleSetting = new VBox();
    paymentSaleSetting.getStyleClass().add("card");
    paymentSaleSetting.setPadding(new Insets(10));
    GridPane paymentSaleSettingCheckboxes = new GridPane();

    viewPaymentSalesCheckbox.setText("View");
    editPaymentSalesCheckbox.setText("Edit");
    createPaymentSaleCheckbox.setText("Create");
    deletePaymentSaleCheckbox.setText("Delete");

    paymentSaleSettingCheckboxes.addColumn(0, viewPaymentSalesCheckbox, editPaymentSalesCheckbox);
    paymentSaleSettingCheckboxes.addColumn(1, createPaymentSaleCheckbox, deletePaymentSaleCheckbox);

    paymentSaleSettingCheckboxes.setHgap(GRID_HGAP);
    paymentSaleSettingCheckboxes.setVgap(GRID_VGAP);

    paymentSaleSetting.getChildren().addAll(title("Payment Sales"), paymentSaleSettingCheckboxes);
    return paymentSaleSetting;
  }

  private VBox getPaymentPurchaseSetting() {
    // Controls CRUD operations on Payment PurchaseMaster.
    VBox paymentPurchaseSetting = new VBox();
    paymentPurchaseSetting.getStyleClass().add("card");
    paymentPurchaseSetting.setPadding(new Insets(10));
    GridPane paymentPurchaseSettingCheckboxes = new GridPane();

    viewPaymentPurchasesCheckbox.setText("View");
    editPaymentPurchasesCheckbox.setText("Edit");
    createPaymentPurchaseCheckbox.setText("Create");
    deletePaymentPurchaseCheckbox.setText("Delete");

    paymentPurchaseSettingCheckboxes.addColumn(
        0, viewPaymentPurchasesCheckbox, editPaymentPurchasesCheckbox);
    paymentPurchaseSettingCheckboxes.addColumn(
        1, createPaymentPurchaseCheckbox, deletePaymentPurchaseCheckbox);

    paymentPurchaseSettingCheckboxes.setHgap(GRID_HGAP);
    paymentPurchaseSettingCheckboxes.setVgap(GRID_VGAP);

    paymentPurchaseSetting
        .getChildren()
        .addAll(title("Payment Purchases"), paymentPurchaseSettingCheckboxes);
    return paymentPurchaseSetting;
  }

  private VBox getPaymentReturnSetting() {
    // Controls CRUD operations on Payment PurchaseMaster.
    VBox paymentReturnSetting = new VBox();
    paymentReturnSetting.getStyleClass().add("card");
    paymentReturnSetting.setPadding(new Insets(10));
    GridPane paymentReturnSettingCheckboxes = new GridPane();

    viewPaymentReturnsCheckbox.setText("View");
    editPaymentReturnsCheckbox.setText("Edit");
    createPaymentReturnCheckbox.setText("Create");
    deletePaymentReturnCheckbox.setText("Delete");

    paymentReturnSettingCheckboxes.addColumn(
        0, viewPaymentReturnsCheckbox, editPaymentReturnsCheckbox);
    paymentReturnSettingCheckboxes.addColumn(
        1, createPaymentReturnCheckbox, deletePaymentReturnCheckbox);

    paymentReturnSettingCheckboxes.setHgap(GRID_HGAP);
    paymentReturnSettingCheckboxes.setVgap(GRID_VGAP);

    paymentReturnSetting
        .getChildren()
        .addAll(title("Payment Returns"), paymentReturnSettingCheckboxes);
    return paymentReturnSetting;
  }

  private VBox getCustomerSetting() {
    // Controls CRUD operations on Payment PurchaseMaster.
    VBox customerSetting = new VBox();
    customerSetting.getStyleClass().add("card");
    customerSetting.setPadding(new Insets(10));
    GridPane customerSettingCheckboxes = new GridPane();

    viewCustomersCheckbox.setText("View");
    editCustomersCheckbox.setText("Edit");
    createCustomerCheckbox.setText("Create");
    deleteCustomerCheckbox.setText("Delete");
    importCustomersCheckbox.setText("Import Customers");
    payAllSellDueCheckbox.setText("Pay all sell due at a time");
    payAllSellReturnDueCheckbox.setText("Pay all sell return due at a time");

    customerSettingCheckboxes.addColumn(
        0,
        viewCustomersCheckbox,
        editCustomersCheckbox,
        importCustomersCheckbox,
        payAllSellDueCheckbox,
        payAllSellReturnDueCheckbox);
    customerSettingCheckboxes.addColumn(1, createCustomerCheckbox, deleteCustomerCheckbox);

    customerSettingCheckboxes.setHgap(GRID_HGAP);
    customerSettingCheckboxes.setVgap(GRID_VGAP);

    customerSetting.getChildren().addAll(title("Customers"), customerSettingCheckboxes);
    return customerSetting;
  }

  private VBox getSupplierSetting() {
    // Controls CRUD operations on Suppliers.
    VBox supplierSetting = new VBox();
    supplierSetting.getStyleClass().add("card");
    supplierSetting.setPadding(new Insets(10));
    GridPane supplierSettingCheckboxes = new GridPane();

    viewSuppliersCheckbox.setText("View");
    editSuppliersCheckbox.setText("Edit");
    createSupplierCheckbox.setText("Create");
    deleteSupplierCheckbox.setText("Delete");
    importSuppliersCheckbox.setText("Import Suppliers");
    payAllPurchaseDueCheckbox.setText("Pay all purchase due at a time");
    payAllPurchaseReturnDueCheckbox.setText("Pay all purchase return due at a time");

    supplierSettingCheckboxes.addColumn(
        0,
        viewSuppliersCheckbox,
        editSuppliersCheckbox,
        importSuppliersCheckbox,
        payAllPurchaseDueCheckbox,
        payAllPurchaseReturnDueCheckbox);
    supplierSettingCheckboxes.addColumn(1, createSupplierCheckbox, deleteSupplierCheckbox);

    supplierSettingCheckboxes.setHgap(GRID_HGAP);
    supplierSettingCheckboxes.setVgap(GRID_VGAP);

    supplierSetting.getChildren().addAll(title("Suppliers"), supplierSettingCheckboxes);
    return supplierSetting;
  }

  private VBox getReportSetting() {
    // Controls CRUD operations on Suppliers.
    VBox reportSetting = new VBox();
    reportSetting.getStyleClass().add("card");
    reportSetting.setPadding(new Insets(10));
    GridPane reportSettingCheckboxes = new GridPane();

    paymentSalesCheckbox.setText("Reports payment sales");
    paymentPurchasesCheckbox.setText("Reports payment purchases");
    saleReturnPaymentsCheckbox.setText("Reports sale return payments");
    purchaseReturnPaymentsCheckbox.setText("Reports purchase return payments");
    saleReportCheckbox.setText("SaleMaster Report");
    purchaseReportCheckbox.setText("PurchaseMaster Report");
    customerReportCheckbox.setText("Customer Report");
    supplierReportCheckbox.setText("Supplier Report");
    profitAndLossCheckbox.setText("Profit and Loss Report");
    productQuantityAlertsCheckbox.setText("Product Quantity Alerts");
    warehouseStockChartCheckbox.setText("Warehouse Stock Chart");
    topSellingProductsCheckbox.setText("Top Selling Products");
    bestCustomersCheckbox.setText("Best Customers");
    usersReportCheckbox.setText("Users Report");
    stockReportCheckbox.setText("Stock Report");
    productReportCheckbox.setText("Product Report");
    productSalesReportCheckbox.setText("Product Sales Report");
    productPurchasesReportCheckbox.setText("Product PurchaseMaster Reports");

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

    reportSetting.getChildren().addAll(title("Reports"), reportSettingCheckboxes);
    return reportSetting;
  }

  private VBox getHRMSetting() {
    // Controls CRUD operations on HRM.
    VBox hrmSetting = new VBox();
    hrmSetting.getStyleClass().add("card");
    hrmSetting.setPadding(new Insets(10));
    GridPane hrmSettingCheckboxes = new GridPane();

    viewEmployeeCheckbox.setText("View Employee");
    editEmployeeCheckbox.setText("Edit Employee");
    createEmployeeCheckbox.setText("Create Employee");
    deleteEmployeeCheckbox.setText("Delete Employee");
    CompanyCheckbox.setText("Company");
    departmentCheckbox.setText("Department");
    designationCheckbox.setText("Designation");
    officeShiftCheckbox.setText("Office Shift");
    attendanceCheckbox.setText("Attendance");
    leaveRequestCheckbox.setText("Leave Request");
    holidayCheckbox.setText("Holiday");

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

    hrmSetting.getChildren().addAll(title("HRM"), hrmSettingCheckboxes);
    return hrmSetting;
  }

  private VBox getSettingSetting() {
    // Controls CRUD operations on Settings.
    VBox settingSetting = new VBox();
    settingSetting.getStyleClass().add("card");
    settingSetting.setPadding(new Insets(10));
    GridPane settingSettingCheckboxes = new GridPane();

    viewSystemSettingCheckbox.setText("View System Settings");
    viewPOSSettingsCheckbox.setText("View POS Settings");
    viewCurrencyCheckbox.setText("View Currency Settings");
    viewBranchCheckbox.setText("View Branch Settings");
    viewBackupCheckbox.setText("View Backup Settings");

    settingSettingCheckboxes.addColumn(
        0, viewSystemSettingCheckbox, viewCurrencyCheckbox, viewBackupCheckbox);
    settingSettingCheckboxes.addColumn(1, viewPOSSettingsCheckbox, viewBranchCheckbox);

    settingSettingCheckboxes.setHgap(GRID_HGAP);
    settingSettingCheckboxes.setVgap(GRID_VGAP);

    settingSetting.getChildren().addAll(title("Settings"), settingSettingCheckboxes);
    return settingSetting;
  }

  private void getRoleSettings() {
    ColumnConstraints col1 = new ColumnConstraints();
    col1.setHgrow(Priority.SOMETIMES);
    ColumnConstraints col2 = new ColumnConstraints();
    col2.setHgrow(Priority.SOMETIMES);

    roleSettings.setPadding(new Insets(10));
    roleSettings.prefHeight(roleSettingsScrollPane.getPrefWidth());

    roleSettingsScrollPane.setContent(roleSettings);

    roleSettings.addRow(0, getDashboardRoleSetting(), getPOSSetting());
    roleSettings.addRow(1, getAdjustmentSetting(), getTransferSetting());
    roleSettings.addRow(2, getQuotationSetting(), getSaleReturnSetting());
    roleSettings.addRow(3, getPaymentSaleSetting(), getPaymentPurchaseSetting());
    roleSettings.addRow(4, getPurchaseReturnSetting(), getPaymentReturnSetting());
    roleSettings.addRow(5, getExpenseSetting(), getUserPermissionsSetting());
    roleSettings.addRow(6, getPurchaseSetting(), getUserMgtSetting());
    roleSettings.addRow(7, getSaleSetting(), getSettingSetting());
    roleSettings.addRow(8, getSupplierSetting(), getCustomerSetting());
    roleSettings.addRow(9, getProductsSetting());
    roleSettings.add(getReportSetting(), 0, 10, 2, 1);

    roleSettings.setHgap(20);
    roleSettings.setVgap(40);
  }

  private List<MFXStepperToggle> getSteps() throws IOException {
    MFXStepperToggle step1 =
        new MFXStepperToggle("Role Wizard", new MFXFontIcon("fas-lock", 12, Color.web("#f1c40f")));
    AnchorPane roleInit = fxmlLoader("components/role_init/role_creator_intro.fxml").load();
    step1.setContent(roleInit);

    MFXStepperToggle step2 =
        new MFXStepperToggle("Role Details", new MFXFontIcon("fas-lock", 12, Color.web("#f1c40f")));
    VBox roleNamingStep =
        new VBox(20, validationWrapper(roleNameInputField), roleDescriptionInputField);
    roleNamingStep.setAlignment(Pos.CENTER);
    step2.setContent(roleNamingStep);
    step2.getValidator().dependsOn(roleNameInputField.getValidator());

    MFXStepperToggle step3 =
        new MFXStepperToggle("Permissions", new MFXFontIcon("fas-check", 12, Color.web("#f1c40f")));
    Platform.runLater(() -> step3.setContent(roleSettingsScrollPane));

    MFXStepperToggle step4 =
        new MFXStepperToggle("Confirming", new MFXFontIcon("fas-check", 12, Color.web("#f1c40f")));
    AnchorPane roleInit2 = fxmlLoader("components/role_init/role_creator_confirm.fxml").load();
    step4.setContent(roleInit2);

    return List.of(step1, step2, step3, step4);
  }

  private VBox title(String labelText) {
    VBox vbox = new VBox();
    Label label = new Label();
    label.setText(labelText);
    label.getStyleClass().add("title");
    Separator separator = new Separator();
    vbox.getChildren().addAll(label, separator);
    return vbox;
  }

  public void closeRoleStepper() {
    Navigation.navigate(Pages.getRoleSettingsPane(), (StackPane) roleSettingsHolder.getParent());
  }
}
