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
import inc.nomard.spoty.core.viewModels.BankViewModel;
import inc.nomard.spoty.utils.SpotyLogger;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static inc.nomard.spoty.core.GlobalActions.closeDialog;
import static inc.nomard.spoty.core.viewModels.BankViewModel.clearBankData;
import static inc.nomard.spoty.core.viewModels.BankViewModel.saveBank;
import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

public class BankFormController implements Initializable {
    private static BankFormController instance;
    @FXML
    public MFXTextField bankName,
            accountName,
            accountNumber,
            branch;
    @FXML
    public Label bankNameValidationLabel,
            accountNameValidationLabel,
            accountNumberValidationLabel,
            branchValidationLabel;
    @FXML
    public MFXButton logo,
            saveBtn,
            cancelBtn;
    private List<Constraint> bankNameConstraints,
            accountNameConstraints,
            accountNumberConstraints,
            branchConstraints;
    private ActionEvent actionEvent = null;

    public static BankFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new BankFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input bindings.
        bankName.textProperty().bindBidirectional(BankViewModel.nameProperty());
        accountName.textProperty().bindBidirectional(BankViewModel.emailProperty());
        accountNumber.textProperty().bindBidirectional(BankViewModel.phoneProperty());
        branch.textProperty().bindBidirectional(BankViewModel.townProperty());
        // Input listeners.
        requiredValidator();
        dialogOnActions();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    clearBankData();
                    closeDialog(event);

                    bankNameValidationLabel.setVisible(false);
                    accountNameValidationLabel.setVisible(false);
                    accountNumberValidationLabel.setVisible(false);
                    branchValidationLabel.setVisible(false);

                    bankNameValidationLabel.setManaged(false);
                    accountNameValidationLabel.setManaged(false);
                    accountNumberValidationLabel.setManaged(false);
                    branchValidationLabel.setManaged(false);

                    bankName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    accountName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    accountNumber.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    branch.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    bankNameConstraints = bankName.validate();
                    accountNameConstraints = accountName.validate();
                    accountNumberConstraints = accountNumber.validate();
                    branchConstraints = branch.validate();
                    if (!bankNameConstraints.isEmpty()) {
                        bankNameValidationLabel.setManaged(true);
                        bankNameValidationLabel.setVisible(true);
                        bankNameValidationLabel.setText(bankNameConstraints.getFirst().getMessage());
                        bankName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!accountNameConstraints.isEmpty()) {
                        accountNameValidationLabel.setManaged(true);
                        accountNameValidationLabel.setVisible(true);
                        accountNameValidationLabel.setText(accountNameConstraints.getFirst().getMessage());
                        accountName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!accountNumberConstraints.isEmpty()) {
                        accountNumberValidationLabel.setManaged(true);
                        accountNumberValidationLabel.setVisible(true);
                        accountNumberValidationLabel.setText(accountNumberConstraints.getFirst().getMessage());
                        accountNumber.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!branchConstraints.isEmpty()) {
                        branchValidationLabel.setManaged(true);
                        branchValidationLabel.setVisible(true);
                        branchValidationLabel.setText(branchConstraints.getFirst().getMessage());
                        branch.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (bankNameConstraints.isEmpty()
                            && accountNameConstraints.isEmpty()
                            && accountNumberConstraints.isEmpty()
                            && branchConstraints.isEmpty()) {
                        if (BankViewModel.getId() > 0) {
                            try {
                                BankViewModel.updateItem(this::onAction, this::onUpdatedSuccess, this::onFailed);
                                actionEvent = event;
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                            return;
                        }

                        try {
                            saveBank(this::onAction, this::onAddSuccess, this::onFailed);
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
                new SpotyMessage.MessageBuilder("Bank added successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        closeDialog(actionEvent);
        clearBankData();
        BankViewModel.getAllBanks(null, null, null);
    }

    private void onUpdatedSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Bank updated successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        closeDialog(actionEvent);
        clearBankData();
        BankViewModel.getAllBanks(null, null, null);
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

        BankViewModel.getAllBanks(null, null, null);
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint bankNameConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Bank name is required")
                        .setCondition(bankName.textProperty().length().greaterThan(0))
                        .get();
        bankName.getValidator().constraint(bankNameConstraint);
        Constraint accountNameConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Account name is required")
                        .setCondition(accountName.textProperty().length().greaterThan(0))
                        .get();
        accountName.getValidator().constraint(accountNameConstraint);
        Constraint accountNumberConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Account number is required")
                        .setCondition(accountNumber.textProperty().length().greaterThan(0))
                        .get();
        accountNumber.getValidator().constraint(accountNumberConstraint);
        Constraint branchConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Branch is required")
                        .setCondition(branch.textProperty().length().greaterThan(0))
                        .get();
        branch.getValidator().constraint(branchConstraint);
        // Display error.
        bankName
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                bankNameValidationLabel.setManaged(false);
                                bankNameValidationLabel.setVisible(false);
                                bankName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        accountName
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                accountNameValidationLabel.setManaged(false);
                                accountNameValidationLabel.setVisible(false);
                                accountName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        accountNumber
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                accountNumberValidationLabel.setManaged(false);
                                accountNumberValidationLabel.setVisible(false);
                                accountNumber.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        branch
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                branchValidationLabel.setManaged(false);
                                branchValidationLabel.setVisible(false);
                                branch.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }
}
