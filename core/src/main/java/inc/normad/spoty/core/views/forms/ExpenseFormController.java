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

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import inc.normad.spoty.core.components.notification.SimpleNotification;
import inc.normad.spoty.core.components.notification.SimpleNotificationHolder;
import inc.normad.spoty.core.components.notification.enums.NotificationDuration;
import inc.normad.spoty.core.components.notification.enums.NotificationVariants;
import inc.normad.spoty.network_bridge.dtos.Branch;
import inc.normad.spoty.network_bridge.dtos.ExpenseCategory;
import inc.normad.spoty.utils.SpotyLogger;
import inc.normad.spoty.utils.SpotyThreader;
import inc.normad.spoty.core.viewModels.BranchViewModel;
import inc.normad.spoty.core.viewModels.ExpenseCategoryViewModel;
import inc.normad.spoty.core.viewModels.ExpensesViewModel;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static inc.normad.spoty.core.GlobalActions.closeDialog;
import static inc.normad.spoty.core.Validators.requiredValidator;

public class ExpenseFormController implements Initializable {
    private static ExpenseFormController instance;
    @FXML
    public MFXTextField expenseFormAmount;
    @FXML
    public MFXTextField expenseFormDetails;
    @FXML
    public MFXButton expenseFormSaveBtn;
    @FXML
    public MFXButton expenseFormCancelBtn;
    @FXML
    public MFXDatePicker expenseFormDate;
    @FXML
    public MFXComboBox<Branch> expenseFormBranch;
    @FXML
    public MFXComboBox<ExpenseCategory> expenseFormCategory;
    @FXML
    public MFXTextField expenseFormName;
    @FXML
    public Label expenseFormNameValidationLabel;
    @FXML
    public Label expenseFormDateValidationLabel;
    @FXML
    public Label expenseFormBranchValidationLabel;
    @FXML
    public Label expenseFormCategoryValidationLabel;
    @FXML
    public Label expenseFormAmountValidationLabel;

    public static ExpenseFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new ExpenseFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form Bindings.
        expenseFormName.textProperty().bindBidirectional(ExpensesViewModel.nameProperty());
        expenseFormDate.textProperty().bindBidirectional(ExpensesViewModel.dateProperty());
        expenseFormBranch.valueProperty().bindBidirectional(ExpensesViewModel.branchProperty());
        expenseFormCategory.valueProperty().bindBidirectional(ExpensesViewModel.categoryProperty());
        expenseFormAmount.textProperty().bindBidirectional(ExpensesViewModel.amountProperty());
        expenseFormDetails.textProperty().bindBidirectional(ExpensesViewModel.detailsProperty());

        // ComboBox Converters.
        StringConverter<Branch> branchConverter =
                FunctionalStringConverter.to(branch -> (branch == null) ? "" : branch.getName());

        StringConverter<ExpenseCategory> expenseCategoryConverter =
                FunctionalStringConverter.to(
                        expenseCategory -> (expenseCategory == null) ? "" : expenseCategory.getName());

        // Combo box properties.
        expenseFormBranch.setItems(BranchViewModel.getBranches());
        expenseFormBranch.setConverter(branchConverter);

        expenseFormCategory.setItems(ExpenseCategoryViewModel.getCategories());
        expenseFormCategory.setConverter(expenseCategoryConverter);

        // Input listeners.
        requiredValidator(
                expenseFormName, "Name is required.", expenseFormNameValidationLabel, expenseFormSaveBtn);
        requiredValidator(
                expenseFormDate, "Date is required.", expenseFormDateValidationLabel, expenseFormSaveBtn);
        requiredValidator(
                expenseFormBranch,
                "Branch is required.",
                expenseFormBranchValidationLabel,
                expenseFormSaveBtn);
        requiredValidator(
                expenseFormCategory,
                "Category is required.",
                expenseFormCategoryValidationLabel,
                expenseFormSaveBtn);
        requiredValidator(
                expenseFormAmount,
                "Amount can't be empty.",
                expenseFormAmountValidationLabel,
                expenseFormSaveBtn);

        dialogOnActions();
    }

    private void dialogOnActions() {
        expenseFormCancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);
                    ExpensesViewModel.resetProperties();
                    expenseFormBranch.clearSelection();
                    expenseFormCategory.clearSelection();

                    expenseFormNameValidationLabel.setVisible(false);
                    expenseFormDateValidationLabel.setVisible(false);
                    expenseFormBranchValidationLabel.setVisible(false);
                    expenseFormCategoryValidationLabel.setVisible(false);
                    expenseFormAmountValidationLabel.setVisible(false);
                });
        expenseFormSaveBtn.setOnAction(
                (event) -> {
                    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();
                    if (!expenseFormNameValidationLabel.isVisible()
                            && !expenseFormDateValidationLabel.isVisible()
                            && !expenseFormBranchValidationLabel.isVisible()
                            && !expenseFormCategoryValidationLabel.isVisible()
                            && !expenseFormAmountValidationLabel.isVisible()) {
                        if (ExpensesViewModel.getId() > 0) {
                            SpotyThreader.spotyThreadPool(() -> {
                                try {
                                    ExpensesViewModel.updateItem(this::onAction, this::onSuccess, this::onFailed);
                                } catch (Exception e) {
                                    SpotyLogger.writeToFile(e, this.getClass());
                                }
                            });

                            SimpleNotification notification =
                                    new SimpleNotification.NotificationBuilder("Expense updated successfully")
                                            .duration(NotificationDuration.SHORT)
                                            .icon("fas-circle-check")
                                            .type(NotificationVariants.SUCCESS)
                                            .build();
                            notificationHolder.addNotification(notification);

                            expenseFormBranch.clearSelection();
                            expenseFormCategory.clearSelection();

                            closeDialog(event);
                            return;
                        }
//                        SpotyThreader.spotyThreadPool(() -> {
                            try {
                                ExpensesViewModel.saveExpense(this::onAction, this::onSuccess, this::onFailed);
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
//                        });

                        SimpleNotification notification =
                                new SimpleNotification.NotificationBuilder("Expense saved successfully")
                                        .duration(NotificationDuration.SHORT)
                                        .icon("fas-circle-check")
                                        .type(NotificationVariants.SUCCESS)
                                        .build();
                        notificationHolder.addNotification(notification);

                        expenseFormBranch.clearSelection();
                        expenseFormCategory.clearSelection();

                        closeDialog(event);
                        return;
                    }
                    SimpleNotification notification =
                            new SimpleNotification.NotificationBuilder("Required fields missing")
                                    .duration(NotificationDuration.SHORT)
                                    .icon("fas-triangle-exclamation")
                                    .type(NotificationVariants.ERROR)
                                    .build();
                    notificationHolder.addNotification(notification);
                });
    }

    private void onAction() {
        System.out.println("Loading expenses...");
    }

    private void onSuccess() {
        System.out.println("Loaded expenses...");
    }

    private void onFailed() {
        System.out.println("failed loading expenses...");
    }
}
