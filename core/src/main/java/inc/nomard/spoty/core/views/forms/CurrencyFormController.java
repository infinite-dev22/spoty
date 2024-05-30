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
import lombok.extern.java.Log;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static inc.nomard.spoty.core.GlobalActions.closeDialog;
import static inc.nomard.spoty.core.viewModels.CurrencyViewModel.clearCurrencyData;
import static inc.nomard.spoty.core.viewModels.CurrencyViewModel.saveCurrency;
import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

@Log
public class CurrencyFormController implements Initializable {
    private static CurrencyFormController instance;
    @FXML
    public MFXTextField name,
            code,
            symbol;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
    @FXML
    public Label codeValidationLabel,
            nameValidationLabel,
            symbolValidationLabel;
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
        code.textProperty().bindBidirectional(CurrencyViewModel.codeProperty());
        name.textProperty().bindBidirectional(CurrencyViewModel.nameProperty());
        symbol.textProperty().bindBidirectional(CurrencyViewModel.symbolProperty());
        // Input listeners.
        requiredValidator();
        dialogOnActions();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    clearCurrencyData();
                    closeDialog(event);

                    nameValidationLabel.setVisible(false);
                    codeValidationLabel.setVisible(false);
                    symbolValidationLabel.setVisible(false);

                    nameValidationLabel.setManaged(false);
                    codeValidationLabel.setManaged(false);
                    symbolValidationLabel.setManaged(false);

                    name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    code.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    symbol.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    currencyFormNameConstraints = name.validate();
                    colorConstraints = symbol.validate();
                    currencyFormCodeConstraints = code.validate();
                    if (!currencyFormNameConstraints.isEmpty()) {
                        nameValidationLabel.setManaged(true);
                        nameValidationLabel.setVisible(true);
                        nameValidationLabel.setText(currencyFormNameConstraints.getFirst().getMessage());
                        name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!colorConstraints.isEmpty()) {
                        symbolValidationLabel.setManaged(true);
                        symbolValidationLabel.setVisible(true);
                        symbolValidationLabel.setText(colorConstraints.getFirst().getMessage());
                        symbol.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!currencyFormCodeConstraints.isEmpty()) {
                        codeValidationLabel.setManaged(true);
                        codeValidationLabel.setVisible(true);
                        codeValidationLabel.setText(currencyFormCodeConstraints.getFirst().getMessage());
                        code.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
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
                        .setCondition(name.textProperty().length().greaterThan(0))
                        .get();
        name.getValidator().constraint(currencyFormNameConstraint);
        Constraint currencyFormCodeConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Code is required")
                        .setCondition(code.textProperty().length().greaterThan(0))
                        .get();
        code.getValidator().constraint(currencyFormCodeConstraint);
        Constraint currencyFormSymbolConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Symbol is required")
                        .setCondition(symbol.textProperty().length().greaterThan(0))
                        .get();
        symbol.getValidator().constraint(currencyFormSymbolConstraint);
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
        code
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                codeValidationLabel.setManaged(false);
                                codeValidationLabel.setVisible(false);
                                code.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        symbol
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                symbolValidationLabel.setManaged(false);
                                symbolValidationLabel.setVisible(false);
                                symbol.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }
}
