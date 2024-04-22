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

import inc.nomard.spoty.core.components.message.SpotyMessage;
import inc.nomard.spoty.core.components.message.SpotyMessageHolder;
import inc.nomard.spoty.core.components.message.enums.MessageDuration;
import inc.nomard.spoty.core.components.message.enums.MessageVariants;
import inc.nomard.spoty.core.viewModels.ExpenseCategoryViewModel;
import inc.nomard.spoty.core.viewModels.ExpensesViewModel;
import inc.nomard.spoty.network_bridge.dtos.ExpenseCategory;
import inc.nomard.spoty.utils.SpotyLogger;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static inc.nomard.spoty.core.GlobalActions.closeDialog;
import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

public class ExpenseFormController implements Initializable {
    private static ExpenseFormController instance;
    @FXML
    public MFXTextField amount,
            name;
    @FXML
    public TextArea description;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
    @FXML
    public MFXDatePicker date;
    @FXML
    public MFXComboBox<ExpenseCategory> category;
    @FXML
    public Label nameValidationLabel,
            dateValidationLabel,
            categoryValidationLabel,
            amountValidationLabel;
    private List<Constraint> nameConstraints,
            dateConstraints,
            categoryConstraints,
            amountConstraints;
    private ActionEvent actionEvent = null;

    public static ExpenseFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new ExpenseFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form Bindings.
        name.textProperty().bindBidirectional(ExpensesViewModel.nameProperty());
        date.textProperty().bindBidirectional(ExpensesViewModel.dateProperty());
        category.valueProperty().bindBidirectional(ExpensesViewModel.categoryProperty());
        amount.textProperty().bindBidirectional(ExpensesViewModel.amountProperty());
        description.textProperty().bindBidirectional(ExpensesViewModel.detailsProperty());

        // ComboBox Converters.
        StringConverter<ExpenseCategory> expenseCategoryConverter =
                FunctionalStringConverter.to(
                        expenseCategory -> (expenseCategory == null) ? "" : expenseCategory.getName());

        // Combo box properties.
        category.setItems(ExpenseCategoryViewModel.getCategories());
        category.setConverter(expenseCategoryConverter);

        // Input listeners.
        requiredValidator();

        dialogOnActions();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);
                    ExpensesViewModel.resetProperties();
                    category.clearSelection();

                    nameValidationLabel.setVisible(false);
                    dateValidationLabel.setVisible(false);
                    categoryValidationLabel.setVisible(false);
                    amountValidationLabel.setVisible(false);

                    nameValidationLabel.setManaged(false);
                    dateValidationLabel.setManaged(false);
                    categoryValidationLabel.setManaged(false);
                    amountValidationLabel.setManaged(false);

                    name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    date.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    category.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    amount.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    nameConstraints = name.validate();
                    dateConstraints = date.validate();
                    categoryConstraints = category.validate();
                    amountConstraints = amount.validate();
                    if (!nameConstraints.isEmpty()) {
                        nameValidationLabel.setManaged(true);
                        nameValidationLabel.setVisible(true);
                        nameValidationLabel.setText(nameConstraints.getFirst().getMessage());
                        name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) name.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!dateConstraints.isEmpty()) {
                        dateValidationLabel.setManaged(true);
                        dateValidationLabel.setVisible(true);
                        dateValidationLabel.setText(dateConstraints.getFirst().getMessage());
                        date.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) date.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!categoryConstraints.isEmpty()) {
                        categoryValidationLabel.setManaged(true);
                        categoryValidationLabel.setVisible(true);
                        categoryValidationLabel.setText(categoryConstraints.getFirst().getMessage());
                        category.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) category.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!amountConstraints.isEmpty()) {
                        amountValidationLabel.setManaged(true);
                        amountValidationLabel.setVisible(true);
                        amountValidationLabel.setText(amountConstraints.getFirst().getMessage());
                        amount.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) amount.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (nameConstraints.isEmpty()
                            && dateConstraints.isEmpty()
                            && categoryConstraints.isEmpty()
                            && amountConstraints.isEmpty()) {
                        if (ExpensesViewModel.getId() > 0) {
                            try {
                                ExpensesViewModel.updateItem(this::onAction, this::onUpdatedSuccess, this::onFailed);
                                actionEvent = event;
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                            return;
                        }
                        try {
                            ExpensesViewModel.saveExpense(this::onAction, this::onAddSuccess, this::onFailed);
                            actionEvent = event;
                        } catch (Exception e) {
                            SpotyLogger.writeToFile(e, this.getClass());
                        }
                    }
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
        category.clearSelection();

        closeDialog(actionEvent);
        ExpensesViewModel.resetProperties();
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
        category.clearSelection();

        closeDialog(actionEvent);
        ExpensesViewModel.resetProperties();
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

    public void requiredValidator() {
        // Name input validation.
        Constraint nameConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Name is required")
                        .setCondition(name.textProperty().length().greaterThan(0))
                        .get();
        name.getValidator().constraint(nameConstraint);
        Constraint dateConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Date is required")
                        .setCondition(date.textProperty().length().greaterThan(0))
                        .get();
        date.getValidator().constraint(dateConstraint);
        Constraint categoryConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Category is required")
                        .setCondition(category.textProperty().length().greaterThan(0))
                        .get();
        category.getValidator().constraint(categoryConstraint);
        Constraint amountConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Amount is required")
                        .setCondition(amount.textProperty().length().greaterThan(0))
                        .get();
        amount.getValidator().constraint(amountConstraint);
        // Display error.
        name
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                nameValidationLabel.setManaged(false);
                                nameValidationLabel.setVisible(false);
                                name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        date
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                dateValidationLabel.setManaged(false);
                                dateValidationLabel.setVisible(false);
                                date.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        category
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                categoryValidationLabel.setManaged(false);
                                categoryValidationLabel.setVisible(false);
                                category.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        amount
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                amountValidationLabel.setManaged(false);
                                amountValidationLabel.setVisible(false);
                                amount.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }
}
