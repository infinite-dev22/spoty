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

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.enums.FloatMode;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.MFXValidator;
import io.github.palexdev.materialfx.validation.Severity;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

public class RoleSettingsFormController implements Initializable {
    private final MFXTextField roleNameInputField;
    private final MFXTextField roleDescriptionInputField;
    private final MFXCheckbox dashboardRoleCheckbox;
    // Users
    private final MFXCheckbox viewUsersCheckbox;
    private final MFXCheckbox editUsersCheckbox;
    private final MFXCheckbox createUserCheckbox;
    private final MFXCheckbox deleteUserCheckbox;
    private final MFXCheckbox viewAllUserRecordsCheckbox;
    // User Permissions
    private final MFXCheckbox viewUserPermissionsCheckbox;
    private final MFXCheckbox editUserPermissionsCheckbox;
    private final MFXCheckbox createUserPermissionCheckbox;
    private final MFXCheckbox deleteUserPermissionCheckbox;
    // Products
    private final MFXCheckbox viewProductsCheckbox;
    private final MFXCheckbox editProductsCheckbox;
    private final MFXCheckbox createProductCheckbox;
    private final MFXCheckbox deleteProductCheckbox;
    private final MFXCheckbox productBarcodesCheckbox;
    private final MFXCheckbox productCategorysCheckbox;
    private final MFXCheckbox productUnitsCheckbox;
    private final MFXCheckbox productImportsCheckbox;
    private final MFXCheckbox productBrandsCheckbox;
    // Adjustments
    private final MFXCheckbox viewAdjustmentsCheckbox;
    private final MFXCheckbox editAdjustmentsCheckbox;
    private final MFXCheckbox createAdjustmentCheckbox;
    private final MFXCheckbox deleteAdjustmentCheckbox;
    // Transfers
    private final MFXCheckbox viewTransfersCheckbox;
    private final MFXCheckbox editTransfersCheckbox;
    private final MFXCheckbox createTransferCheckbox;
    private final MFXCheckbox deleteTransferCheckbox;
    // Expenses
    private final MFXCheckbox viewExpensesCheckbox;
    private final MFXCheckbox editExpensesCheckbox;
    private final MFXCheckbox createExpenseCheckbox;
    private final MFXCheckbox deleteExpenseCheckbox;
    // Expenses
    private final MFXCheckbox viewSalesCheckbox;
    private final MFXCheckbox editSalesCheckbox;
    private final MFXCheckbox createSaleCheckbox;
    private final MFXCheckbox deleteSaleCheckbox;
    private final MFXCheckbox accessPOSCheckbox;
    // Expenses
    private final MFXCheckbox viewPurchasesCheckbox;
    private final MFXCheckbox editPurchasesCheckbox;
    private final MFXCheckbox createPurchaseCheckbox;
    private final MFXCheckbox deletePurchaseCheckbox;
    // Quotations
    private final MFXCheckbox viewQuotationsCheckbox;
    private final MFXCheckbox editQuotationsCheckbox;
    private final MFXCheckbox createQuotationCheckbox;
    private final MFXCheckbox deleteQuotationCheckbox;
    // SaleMaster Returns
    private final MFXCheckbox viewSaleReturnsCheckbox;
    private final MFXCheckbox editSaleReturnsCheckbox;
    private final MFXCheckbox createSaleReturnCheckbox;
    private final MFXCheckbox deleteSaleReturnCheckbox;
    // PurchaseMaster Returns
    private final MFXCheckbox viewPurchaseReturnsCheckbox;
    private final MFXCheckbox editPurchaseReturnsCheckbox;
    private final MFXCheckbox createPurchaseReturnCheckbox;
    private final MFXCheckbox deletePurchaseReturnCheckbox;
    // Payment Sales
    private final MFXCheckbox viewPaymentSalesCheckbox;
    private final MFXCheckbox editPaymentSalesCheckbox;
    private final MFXCheckbox createPaymentSaleCheckbox;
    private final MFXCheckbox deletePaymentSaleCheckbox;
    // Payment Purchases
    private final MFXCheckbox viewPaymentPurchasesCheckbox;
    private final MFXCheckbox editPaymentPurchasesCheckbox;
    private final MFXCheckbox createPaymentPurchaseCheckbox;
    private final MFXCheckbox deletePaymentPurchaseCheckbox;
    // Payment Returns
    private final MFXCheckbox viewPaymentReturnsCheckbox;
    private final MFXCheckbox editPaymentReturnsCheckbox;
    private final MFXCheckbox createPaymentReturnCheckbox;
    private final MFXCheckbox deletePaymentReturnCheckbox;
    // Customers
    private final MFXCheckbox viewCustomersCheckbox;
    private final MFXCheckbox editCustomersCheckbox;
    private final MFXCheckbox createCustomerCheckbox;
    private final MFXCheckbox deleteCustomerCheckbox;
    private final MFXCheckbox importCustomersCheckbox;
    private final MFXCheckbox payAllSellDueCheckbox;
    private final MFXCheckbox payAllSellReturnDueCheckbox;
    // Suppliers
    private final MFXCheckbox viewSuppliersCheckbox;
    private final MFXCheckbox editSuppliersCheckbox;
    private final MFXCheckbox createSupplierCheckbox;
    private final MFXCheckbox deleteSupplierCheckbox;
    private final MFXCheckbox importSuppliersCheckbox;
    private final MFXCheckbox payAllPurchaseDueCheckbox;
    private final MFXCheckbox payAllPurchaseReturnDueCheckbox;
    // Reports
    private final MFXCheckbox paymentSalesCheckbox;
    private final MFXCheckbox paymentPurchasesCheckbox;
    private final MFXCheckbox saleReturnPaymentsCheckbox;
    private final MFXCheckbox purchaseReturnPaymentsCheckbox;
    private final MFXCheckbox saleReportCheckbox;
    private final MFXCheckbox purchaseReportCheckbox;
    private final MFXCheckbox customerReportCheckbox;
    private final MFXCheckbox supplierReportCheckbox;
    private final MFXCheckbox profitAndLossCheckbox;
    private final MFXCheckbox productQuantityAlertsCheckbox;
    private final MFXCheckbox warehouseStockChartCheckbox;
    private final MFXCheckbox topSellingProductsCheckbox;
    private final MFXCheckbox bestCustomersCheckbox;
    private final MFXCheckbox usersReportCheckbox;
    private final MFXCheckbox stockReportCheckbox;
    private final MFXCheckbox productReportCheckbox;
    private final MFXCheckbox productSalesReportCheckbox;
    private final MFXCheckbox productPurchasesReportCheckbox;
    // HRM
    private final MFXCheckbox viewEmployeeCheckbox;
    private final MFXCheckbox editEmployeeCheckbox;
    private final MFXCheckbox createEmployeeCheckbox;
    private final MFXCheckbox deleteEmployeeCheckbox;
    private final MFXCheckbox CompanyCheckbox;
    private final MFXCheckbox departmentCheckbox;
    private final MFXCheckbox designationCheckbox;
    private final MFXCheckbox officeShiftCheckbox;
    private final MFXCheckbox attendanceCheckbox;
    private final MFXCheckbox leaveRequestCheckbox;
    private final MFXCheckbox holidayCheckbox;
    // Settings
    private final MFXCheckbox viewSystemSettingCheckbox;
    private final MFXCheckbox viewPOSSettingsCheckbox;
    private final MFXCheckbox viewCurrencyCheckbox;
    private final MFXCheckbox viewBranchCheckbox;
    private final MFXCheckbox viewBackupCheckbox;
    @FXML
    private BorderPane roleSettings;
    @FXML
    private MFXStepper roleSettingsStepper;

    public RoleSettingsFormController() {
        roleNameInputField = new MFXTextField();
        roleDescriptionInputField = new MFXTextField();

        dashboardRoleCheckbox = new MFXCheckbox();

        viewUsersCheckbox = new MFXCheckbox();
        editUsersCheckbox = new MFXCheckbox();
        createUserCheckbox = new MFXCheckbox();
        deleteUserCheckbox = new MFXCheckbox();
        viewAllUserRecordsCheckbox = new MFXCheckbox();

        viewUserPermissionsCheckbox = new MFXCheckbox();
        editUserPermissionsCheckbox = new MFXCheckbox();
        createUserPermissionCheckbox = new MFXCheckbox();
        deleteUserPermissionCheckbox = new MFXCheckbox();

        viewProductsCheckbox = new MFXCheckbox();
        editProductsCheckbox = new MFXCheckbox();
        createProductCheckbox = new MFXCheckbox();
        deleteProductCheckbox = new MFXCheckbox();
        productBarcodesCheckbox = new MFXCheckbox();
        productCategorysCheckbox = new MFXCheckbox();
        productUnitsCheckbox = new MFXCheckbox();
        productImportsCheckbox = new MFXCheckbox();
        productBrandsCheckbox = new MFXCheckbox();

        viewAdjustmentsCheckbox = new MFXCheckbox();
        editAdjustmentsCheckbox = new MFXCheckbox();
        createAdjustmentCheckbox = new MFXCheckbox();
        deleteAdjustmentCheckbox = new MFXCheckbox();

        viewTransfersCheckbox = new MFXCheckbox();
        editTransfersCheckbox = new MFXCheckbox();
        createTransferCheckbox = new MFXCheckbox();
        deleteTransferCheckbox = new MFXCheckbox();

        viewExpensesCheckbox = new MFXCheckbox();
        editExpensesCheckbox = new MFXCheckbox();
        createExpenseCheckbox = new MFXCheckbox();
        deleteExpenseCheckbox = new MFXCheckbox();

        viewSalesCheckbox = new MFXCheckbox();
        editSalesCheckbox = new MFXCheckbox();
        createSaleCheckbox = new MFXCheckbox();
        deleteSaleCheckbox = new MFXCheckbox();
        accessPOSCheckbox = new MFXCheckbox();

        viewPurchasesCheckbox = new MFXCheckbox();
        editPurchasesCheckbox = new MFXCheckbox();
        createPurchaseCheckbox = new MFXCheckbox();
        deletePurchaseCheckbox = new MFXCheckbox();

        viewQuotationsCheckbox = new MFXCheckbox();
        editQuotationsCheckbox = new MFXCheckbox();
        createQuotationCheckbox = new MFXCheckbox();
        deleteQuotationCheckbox = new MFXCheckbox();

        viewSaleReturnsCheckbox = new MFXCheckbox();
        editSaleReturnsCheckbox = new MFXCheckbox();
        createSaleReturnCheckbox = new MFXCheckbox();
        deleteSaleReturnCheckbox = new MFXCheckbox();

        viewPurchaseReturnsCheckbox = new MFXCheckbox();
        editPurchaseReturnsCheckbox = new MFXCheckbox();
        createPurchaseReturnCheckbox = new MFXCheckbox();
        deletePurchaseReturnCheckbox = new MFXCheckbox();

        viewPaymentSalesCheckbox = new MFXCheckbox();
        editPaymentSalesCheckbox = new MFXCheckbox();
        createPaymentSaleCheckbox = new MFXCheckbox();
        deletePaymentSaleCheckbox = new MFXCheckbox();

        viewPaymentPurchasesCheckbox = new MFXCheckbox();
        editPaymentPurchasesCheckbox = new MFXCheckbox();
        createPaymentPurchaseCheckbox = new MFXCheckbox();
        deletePaymentPurchaseCheckbox = new MFXCheckbox();

        viewPaymentReturnsCheckbox = new MFXCheckbox();
        editPaymentReturnsCheckbox = new MFXCheckbox();
        createPaymentReturnCheckbox = new MFXCheckbox();
        deletePaymentReturnCheckbox = new MFXCheckbox();

        viewCustomersCheckbox = new MFXCheckbox();
        editCustomersCheckbox = new MFXCheckbox();
        createCustomerCheckbox = new MFXCheckbox();
        deleteCustomerCheckbox = new MFXCheckbox();
        importCustomersCheckbox = new MFXCheckbox();
        payAllSellDueCheckbox = new MFXCheckbox();
        payAllSellReturnDueCheckbox = new MFXCheckbox();

        viewSuppliersCheckbox = new MFXCheckbox();
        editSuppliersCheckbox = new MFXCheckbox();
        createSupplierCheckbox = new MFXCheckbox();
        deleteSupplierCheckbox = new MFXCheckbox();
        importSuppliersCheckbox = new MFXCheckbox();
        payAllPurchaseDueCheckbox = new MFXCheckbox();
        payAllPurchaseReturnDueCheckbox = new MFXCheckbox();

        paymentSalesCheckbox = new MFXCheckbox();
        paymentPurchasesCheckbox = new MFXCheckbox();
        saleReturnPaymentsCheckbox = new MFXCheckbox();
        purchaseReturnPaymentsCheckbox = new MFXCheckbox();
        saleReportCheckbox = new MFXCheckbox();
        purchaseReportCheckbox = new MFXCheckbox();
        customerReportCheckbox = new MFXCheckbox();
        supplierReportCheckbox = new MFXCheckbox();
        profitAndLossCheckbox = new MFXCheckbox();
        productQuantityAlertsCheckbox = new MFXCheckbox();
        warehouseStockChartCheckbox = new MFXCheckbox();
        topSellingProductsCheckbox = new MFXCheckbox();
        bestCustomersCheckbox = new MFXCheckbox();
        usersReportCheckbox = new MFXCheckbox();
        stockReportCheckbox = new MFXCheckbox();
        productReportCheckbox = new MFXCheckbox();
        productSalesReportCheckbox = new MFXCheckbox();
        productPurchasesReportCheckbox = new MFXCheckbox();

        viewEmployeeCheckbox = new MFXCheckbox();
        editEmployeeCheckbox = new MFXCheckbox();
        createEmployeeCheckbox = new MFXCheckbox();
        deleteEmployeeCheckbox = new MFXCheckbox();
        CompanyCheckbox = new MFXCheckbox();
        departmentCheckbox = new MFXCheckbox();
        designationCheckbox = new MFXCheckbox();
        officeShiftCheckbox = new MFXCheckbox();
        attendanceCheckbox = new MFXCheckbox();
        leaveRequestCheckbox = new MFXCheckbox();
        holidayCheckbox = new MFXCheckbox();

        viewSystemSettingCheckbox = new MFXCheckbox();
        viewPOSSettingsCheckbox = new MFXCheckbox();
        viewCurrencyCheckbox = new MFXCheckbox();
        viewBranchCheckbox = new MFXCheckbox();
        viewBackupCheckbox = new MFXCheckbox();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Constraint emptyConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Field required")
                .setCondition(roleNameInputField.textProperty().length().greaterThan(0))
                .get();
        Constraint lengthConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Role name must be at least 4 Characters long")
                .setCondition(roleNameInputField.textProperty().length().greaterThan(3))
                .get();

        roleNameInputField.setFloatMode(FloatMode.BORDER);
        roleNameInputField.setFloatingText("Role name");
        roleNameInputField.setPrefWidth(400);
        roleNameInputField.requestFocus();
        roleNameInputField.getValidator()
                .constraint(emptyConstraint)
                .constraint(lengthConstraint);

        roleDescriptionInputField.setFloatMode(FloatMode.BORDER);
        roleDescriptionInputField.setFloatingText("Role description");
        roleDescriptionInputField.setPrefWidth(400);

        List<MFXStepperToggle> stepperToggle = getSteps();
        roleSettingsStepper.getStepperToggles().addAll(stepperToggle);
        roleSettingsStepper.setOnLastNext(e -> roleSettingsStepper.setMouseTransparent(true));
    }

    protected VBox validationWrapper(MFXTextField node) {
        Label errorLabel = new Label();
        VBox container = new VBox(5, node, errorLabel);
        container.setAlignment(Pos.CENTER);
        errorLabel.getStyleClass().add("danger-validation-label");

        node.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                errorLabel.setVisible(false);
                roleNameInputField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
            }
        });
        node.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                List<Constraint> constraints = roleNameInputField.validate();
                if (!constraints.isEmpty()) {
                    roleNameInputField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    errorLabel.setText(constraints.get(0).getMessage());
                    errorLabel.setVisible(true);
                }
            }
        });

        roleSettingsStepper.addEventHandler(MFXStepper.MFXStepperEvent.VALIDATION_FAILED_EVENT, event -> {
            MFXValidator validator = node.getValidator();
            List<Constraint> validate = validator.validate();
            if (!validate.isEmpty()) {
                errorLabel.setText(validate.get(0).getMessage());
                errorLabel.setVisible(true);
            }
        });
        roleSettingsStepper.addEventHandler(MFXStepper.MFXStepperEvent.NEXT_EVENT, event -> errorLabel.setVisible(false));
        return container;
    }

    private VBox getDashboardRoleSetting() {
        VBox dashboardRoleSetting = new VBox();
        dashboardRoleSetting.getStyleClass().add("card");
        dashboardRoleSetting.setPadding(new Insets(10));
        dashboardRoleCheckbox.setText("Access Dashboard");
        dashboardRoleSetting.getChildren().addAll(title("Dashboard"), dashboardRoleCheckbox);
        return dashboardRoleSetting;
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
        viewAllUserRecordsCheckbox.setText("View all user records");

        userMgtSettingCheckboxes.addColumn(0, viewUsersCheckbox, editUsersCheckbox, viewAllUserRecordsCheckbox);
        userMgtSettingCheckboxes.addColumn(1, createUserCheckbox, deleteUserCheckbox);

        userMgtSettingCheckboxes.setHgap(20);

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

        userPermissionsSettingCheckboxes.addColumn(0, viewUserPermissionsCheckbox, editUserPermissionsCheckbox);
        userPermissionsSettingCheckboxes.addColumn(1, createUserPermissionCheckbox, deleteUserPermissionCheckbox);

        userPermissionsSettingCheckboxes.setHgap(20);

        userPermissionsSetting.getChildren().addAll(title("User Permissions"), userPermissionsSettingCheckboxes);
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
        productCategorysCheckbox.setText("Category");
        productUnitsCheckbox.setText("Unit");
        createProductCheckbox.setText("Create");
        deleteProductCheckbox.setText("Delete");
        productImportsCheckbox.setText("Import Products");
        productBrandsCheckbox.setText("Brand");

        productsSettingCheckboxes.addColumn(0, viewProductsCheckbox, editProductsCheckbox,
                productBarcodesCheckbox, productCategorysCheckbox, productUnitsCheckbox);
        productsSettingCheckboxes.addColumn(1, createProductCheckbox, deleteProductCheckbox,
                productImportsCheckbox, productBrandsCheckbox);

        productsSettingCheckboxes.setHgap(20);

        productsSetting.getChildren().addAll(title("Products"), productsSettingCheckboxes);
        return productsSetting;
    }

    private VBox getAdjustmentSetting() {
        // Controls CRUD operations on Adjustment.
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

        adjustmentSettingCheckboxes.setHgap(20);

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

        transferSettingCheckboxes.setHgap(20);

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

        expenseSettingCheckboxes.setHgap(20);

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
        accessPOSCheckbox.setText("Point Of SaleMaster");

        saleSettingCheckboxes.addColumn(0, viewSalesCheckbox, editSalesCheckbox, accessPOSCheckbox);
        saleSettingCheckboxes.addColumn(1, createSaleCheckbox, deleteSaleCheckbox);

        saleSettingCheckboxes.setHgap(20);

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

        purchaseSettingCheckboxes.setHgap(20);

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

        quotationSettingCheckboxes.setHgap(20);

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

        saleReturnSettingCheckboxes.setHgap(20);

        saleReturnSetting.getChildren().addAll(title("SaleMaster Returns"), saleReturnSettingCheckboxes);
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

        purchaseReturnSettingCheckboxes.addColumn(0, viewPurchaseReturnsCheckbox, editPurchaseReturnsCheckbox);
        purchaseReturnSettingCheckboxes.addColumn(1, createPurchaseReturnCheckbox, deletePurchaseReturnCheckbox);

        purchaseReturnSettingCheckboxes.setHgap(20);

        purchaseReturnSetting.getChildren().addAll(title("PurchaseMaster Returns"), purchaseReturnSettingCheckboxes);
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

        paymentSaleSettingCheckboxes.setHgap(20);

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

        paymentPurchaseSettingCheckboxes.addColumn(0, viewPaymentPurchasesCheckbox, editPaymentPurchasesCheckbox);
        paymentPurchaseSettingCheckboxes.addColumn(1, createPaymentPurchaseCheckbox, deletePaymentPurchaseCheckbox);

        paymentPurchaseSettingCheckboxes.setHgap(20);

        paymentPurchaseSetting.getChildren().addAll(title("Payment Purchases"), paymentPurchaseSettingCheckboxes);
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

        paymentReturnSettingCheckboxes.addColumn(0, viewPaymentReturnsCheckbox, editPaymentReturnsCheckbox);
        paymentReturnSettingCheckboxes.addColumn(1, createPaymentReturnCheckbox, deletePaymentReturnCheckbox);

        paymentReturnSettingCheckboxes.setHgap(20);

        paymentReturnSetting.getChildren().addAll(title("Payment Returns"), paymentReturnSettingCheckboxes);
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

        customerSettingCheckboxes.addColumn(0, viewCustomersCheckbox, editCustomersCheckbox,
                importCustomersCheckbox, payAllSellDueCheckbox, payAllSellReturnDueCheckbox);
        customerSettingCheckboxes.addColumn(1, createCustomerCheckbox, deleteCustomerCheckbox);

        customerSettingCheckboxes.setHgap(20);

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

        supplierSettingCheckboxes.addColumn(0, viewSuppliersCheckbox, editSuppliersCheckbox,
                importSuppliersCheckbox, payAllPurchaseDueCheckbox, payAllPurchaseReturnDueCheckbox);
        supplierSettingCheckboxes.addColumn(1, createSupplierCheckbox, deleteSupplierCheckbox);

        supplierSettingCheckboxes.setHgap(20);

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

        reportSettingCheckboxes.addColumn(0, paymentSalesCheckbox, paymentPurchasesCheckbox,
                saleReturnPaymentsCheckbox, purchaseReturnPaymentsCheckbox, saleReportCheckbox,
                purchaseReportCheckbox, customerReportCheckbox, supplierReportCheckbox,
                profitAndLossCheckbox);
        reportSettingCheckboxes.addColumn(1, productQuantityAlertsCheckbox
                , warehouseStockChartCheckbox, topSellingProductsCheckbox, bestCustomersCheckbox,
                usersReportCheckbox, stockReportCheckbox, productReportCheckbox,
                productSalesReportCheckbox, productPurchasesReportCheckbox);

        reportSettingCheckboxes.setHgap(20);

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

        hrmSettingCheckboxes.addColumn(0, viewEmployeeCheckbox, createEmployeeCheckbox,
                CompanyCheckbox, designationCheckbox, attendanceCheckbox, holidayCheckbox);
        hrmSettingCheckboxes.addColumn(1, editEmployeeCheckbox, deleteEmployeeCheckbox,
                departmentCheckbox, officeShiftCheckbox, leaveRequestCheckbox);

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

        settingSettingCheckboxes.addColumn(0, viewSystemSettingCheckbox, viewCurrencyCheckbox,
                viewBackupCheckbox);
        settingSettingCheckboxes.addColumn(1, viewPOSSettingsCheckbox, viewBranchCheckbox);

        settingSettingCheckboxes.setHgap(20);

        settingSetting.getChildren().addAll(title("Settings"), settingSettingCheckboxes);
        return settingSetting;
    }

    private MFXScrollPane getRoleSettings() {
        MFXScrollPane roleSettingsScrollPane = new MFXScrollPane();
        GridPane roleSettings = new GridPane();
        roleSettings.setPadding(new Insets(10));
        roleSettingsScrollPane.setContent(roleSettings);

        roleSettings.addRow(0, getDashboardRoleSetting(), getUserMgtSetting());
        roleSettings.addRow(1, getUserPermissionsSetting(), getProductsSetting());
        roleSettings.addRow(2, getAdjustmentSetting(), getTransferSetting());
        roleSettings.addRow(3, getExpenseSetting(), getSaleSetting());
        roleSettings.addRow(4, getPurchaseSetting(), getQuotationSetting());
        roleSettings.addRow(5, getSaleReturnSetting(), getPurchaseReturnSetting());
        roleSettings.addRow(6, getPaymentSaleSetting(), getPaymentPurchaseSetting());
        roleSettings.addRow(7, getPaymentReturnSetting(), getCustomerSetting());
        roleSettings.addRow(8, getSupplierSetting(), getReportSetting());
        roleSettings.addRow(9, getHRMSetting(), getSettingSetting());

        roleSettings.setHgap(20);
        roleSettings.setVgap(40);

        return roleSettingsScrollPane;
    }

    private List<MFXStepperToggle> getSteps() {
        MFXStepperToggle step1 = new MFXStepperToggle("Role", new MFXFontIcon("fas-lock", 16, Color.web("#f1c40f")));
        VBox roleNamingStep = new VBox(20, validationWrapper(roleNameInputField), roleDescriptionInputField);
        roleNamingStep.setAlignment(Pos.CENTER);
        step1.setContent(roleNamingStep);
        step1.getValidator().dependsOn(roleNameInputField.getValidator());

        MFXStepperToggle step2 = new MFXStepperToggle("Permissions", new MFXFontIcon("fas-check", 16, Color.web("#f1c40f")));
        Platform.runLater(() -> step2.setContent(getRoleSettings()));

        return List.of(step1, step2);
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
        ((StackPane) roleSettings.getParent()).getChildren().get(0).setVisible(true);
        ((StackPane) roleSettings.getParent()).getChildren().remove(1);
    }
}
