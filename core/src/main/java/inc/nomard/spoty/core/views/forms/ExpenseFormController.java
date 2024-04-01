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

package inc.nomard.spoty.core.views.forms;

import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.MessageDuration;
import inc.nomard.spoty.core.components.message.enums.MessageVariants;
import inc.nomard.spoty.core.viewModels.BranchViewModel;
import inc.nomard.spoty.core.viewModels.ExpenseCategoryViewModel;
import inc.nomard.spoty.core.viewModels.ExpensesViewModel;
import inc.nomard.spoty.network_bridge.dtos.Branch;
import inc.nomard.spoty.network_bridge.dtos.ExpenseCategory;
import inc.nomard.spoty.utils.SpotyLogger;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static inc.nomard.spoty.core.GlobalActions.closeDialog;
import static inc.nomard.spoty.core.Validators.requiredValidator;

public class ExpenseFormController implements Initializable {
    private static ExpenseFormController instance;
    @FXML
    public MFXTextField expenseFormAmount;
    @FXML
    public MFXTextField expenseFormDetails;
    @FXML
    public MFXButton saveBtn;
    @FXML
    public MFXButton cancelBtn;
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
                expenseFormName, "Name is required.", expenseFormNameValidationLabel, saveBtn);
        requiredValidator(
                expenseFormDate, "Date is required.", expenseFormDateValidationLabel, saveBtn);
        requiredValidator(
                expenseFormBranch,
                "Branch is required.",
                expenseFormBranchValidationLabel,
                saveBtn);
        requiredValidator(
                expenseFormCategory,
                "Category is required.",
                expenseFormCategoryValidationLabel,
                saveBtn);
        requiredValidator(
                expenseFormAmount,
                "Amount can't be empty.",
                expenseFormAmountValidationLabel,
                saveBtn);

        dialogOnActions();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
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
        saveBtn.setOnAction(
                (event) -> {
                    if (!expenseFormNameValidationLabel.isVisible()
                            && !expenseFormDateValidationLabel.isVisible()
                            && !expenseFormBranchValidationLabel.isVisible()
                            && !expenseFormCategoryValidationLabel.isVisible()
                            && !expenseFormAmountValidationLabel.isVisible()) {
                        if (ExpensesViewModel.getId() > 0) {
                            try {
                                ExpensesViewModel.updateItem(this::onAction, this::onUpdatedSuccess, this::onFailed);
                                closeDialog(event);
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                            return;
                        }
                        try {
                            ExpensesViewModel.saveExpense(this::onAction, this::onAddSuccess, this::onFailed);
                            closeDialog(event);
                        } catch (Exception e) {
                            SpotyLogger.writeToFile(e, this.getClass());
                        }
                        return;
                    }
                    onRequiredFieldsMissing();
                });
    }

    private void onAction() {
        cancelBtn.setDisable(true);
        saveBtn.setDisable(true);
//        cancelBtn.setManaged(true);
//        saveBtn.setManaged(true);
    }

    private void onAddSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Expense added successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);
        expenseFormBranch.clearSelection();
        expenseFormCategory.clearSelection();

        ExpensesViewModel.getAllExpenses(null, null, null);
    }

    private void onUpdatedSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Expense updated successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);
        expenseFormBranch.clearSelection();
        expenseFormCategory.clearSelection();

        ExpensesViewModel.getAllExpenses(null, null, null);
    }

    private void onFailed() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("An error occurred")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        ExpensesViewModel.getAllExpenses(null, null, null);
    }

    private void onRequiredFieldsMissing() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Required fields can't be null")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        ExpensesViewModel.getAllExpenses(null, null, null);
    }
}
