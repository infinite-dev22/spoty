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
import inc.nomard.spoty.core.viewModels.CurrencyViewModel;
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
import static inc.nomard.spoty.core.viewModels.CurrencyViewModel.clearCurrencyData;
import static inc.nomard.spoty.core.viewModels.CurrencyViewModel.saveCurrency;
import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

public class CurrencyFormController implements Initializable {
    private static CurrencyFormController instance;
    @FXML
    public MFXTextField currencyFormName,
            currencyFormCode,
            currencyFormSymbol;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
    @FXML
    public Label currencyFormCodeValidationLabel,
            currencyFormNameValidationLabel,
            currencyFormSymbolValidationLabel;
    private List<Constraint> currencyFormNameConstraints,
            colorConstraints,
            currencyFormCodeConstraints;
    private ActionEvent actionEvent = null;

    public static CurrencyFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new CurrencyFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input bindings.
        currencyFormCode.textProperty().bindBidirectional(CurrencyViewModel.codeProperty());
        currencyFormName.textProperty().bindBidirectional(CurrencyViewModel.nameProperty());
        currencyFormSymbol.textProperty().bindBidirectional(CurrencyViewModel.symbolProperty());
        // Input listeners.
        requiredValidator();
        dialogOnActions();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    clearCurrencyData();
                    closeDialog(event);

                    currencyFormNameValidationLabel.setVisible(false);
                    currencyFormCodeValidationLabel.setVisible(false);
                    currencyFormSymbolValidationLabel.setVisible(false);

                    currencyFormNameValidationLabel.setManaged(false);
                    currencyFormCodeValidationLabel.setManaged(false);
                    currencyFormSymbolValidationLabel.setManaged(false);

                    currencyFormName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    currencyFormCode.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    currencyFormSymbol.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    currencyFormNameConstraints = currencyFormName.validate();
                    colorConstraints = currencyFormSymbol.validate();
                    currencyFormCodeConstraints = currencyFormCode.validate();
                    if (!currencyFormNameConstraints.isEmpty()) {
                        currencyFormNameValidationLabel.setManaged(true);
                        currencyFormNameValidationLabel.setVisible(true);
                        currencyFormNameValidationLabel.setText(currencyFormNameConstraints.getFirst().getMessage());
                        currencyFormName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!colorConstraints.isEmpty()) {
                        currencyFormSymbolValidationLabel.setManaged(true);
                        currencyFormSymbolValidationLabel.setVisible(true);
                        currencyFormSymbolValidationLabel.setText(colorConstraints.getFirst().getMessage());
                        currencyFormSymbol.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!currencyFormCodeConstraints.isEmpty()) {
                        currencyFormCodeValidationLabel.setManaged(true);
                        currencyFormCodeValidationLabel.setVisible(true);
                        currencyFormCodeValidationLabel.setText(currencyFormCodeConstraints.getFirst().getMessage());
                        currencyFormCode.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (currencyFormNameConstraints.isEmpty()
                            && colorConstraints.isEmpty()
                            && currencyFormCodeConstraints.isEmpty()) {
                        if (CurrencyViewModel.getId() > 0) {
                            try {
                                CurrencyViewModel.updateItem(this::onAction, this::onUpdatedSuccess, this::onFailed);
                                actionEvent = event;
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e.getCause(), this.getClass());
                            }
                            return;
                        }
                        try {
                            saveCurrency(this::onAction, this::onAddSuccess, this::onFailed);
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
                new SpotyMessage.MessageBuilder("Currency added successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        closeDialog(actionEvent);
        clearCurrencyData();
        CurrencyViewModel.getAllCurrencies(null, null, null);
    }

    private void onUpdatedSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Currency updated successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        closeDialog(actionEvent);
        clearCurrencyData();
        CurrencyViewModel.getAllCurrencies(null, null, null);
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

        CurrencyViewModel.getAllCurrencies(null, null, null);
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint currencyFormNameConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Name is required")
                        .setCondition(currencyFormName.textProperty().length().greaterThan(0))
                        .get();
        currencyFormName.getValidator().constraint(currencyFormNameConstraint);
        Constraint currencyFormCodeConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Code is required")
                        .setCondition(currencyFormCode.textProperty().length().greaterThan(0))
                        .get();
        currencyFormCode.getValidator().constraint(currencyFormCodeConstraint);
        Constraint currencyFormSymbolConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Symbol is required")
                        .setCondition(currencyFormSymbol.textProperty().length().greaterThan(0))
                        .get();
        currencyFormSymbol.getValidator().constraint(currencyFormSymbolConstraint);
        // Display error.
        currencyFormName
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                currencyFormNameValidationLabel.setManaged(false);
                                currencyFormNameValidationLabel.setVisible(false);
                                currencyFormName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        currencyFormCode
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                currencyFormCodeValidationLabel.setManaged(false);
                                currencyFormCodeValidationLabel.setVisible(false);
                                currencyFormCode.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        currencyFormSymbol
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                currencyFormSymbolValidationLabel.setManaged(false);
                                currencyFormSymbolValidationLabel.setVisible(false);
                                currencyFormSymbol.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }
}
