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

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.infinite.spoty.GlobalActions;
import org.infinite.spoty.components.notification.SimpleNotification;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.components.notification.enums.NotificationDuration;
import org.infinite.spoty.components.notification.enums.NotificationVariants;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.viewModels.CurrencyViewModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.Validators.requiredValidator;
import static org.infinite.spoty.viewModels.CurrencyViewModel.clearCurrencyData;
import static org.infinite.spoty.viewModels.CurrencyViewModel.saveCurrency;

public class CurrencyFormController implements Initializable {
    private static CurrencyFormController instance;
    @FXML
    public MFXTextField currencyFormName;
    @FXML
    public MFXButton currencyFormSaveBtn;
    @FXML
    public MFXButton currencyFormCancelBtn;
    @FXML
    public MFXTextField currencyFormCode;
    @FXML
    public MFXTextField currencyFormSymbol;
    @FXML
    public Label currencyFormCodeValidationLabel;
    @FXML
    public Label currencyFormNameValidationLabel;

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
        requiredValidator(
                currencyFormName,
                "Name is required.",
                currencyFormNameValidationLabel,
                currencyFormSaveBtn);
        requiredValidator(
                currencyFormCode,
                "Code is required.",
                currencyFormCodeValidationLabel,
                currencyFormSaveBtn);
        dialogOnActions();
    }

    private void dialogOnActions() {
        currencyFormCancelBtn.setOnAction(
                (event) -> {
                    clearCurrencyData();

                    closeDialog(event);

                    currencyFormNameValidationLabel.setVisible(false);
                    currencyFormCodeValidationLabel.setVisible(false);
                });
        currencyFormSaveBtn.setOnAction(
                (event) -> {
                    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

                    if (!currencyFormNameValidationLabel.isVisible()
                            && !currencyFormCodeValidationLabel.isVisible()) {
                        if (CurrencyViewModel.getId() > 0) {
                            GlobalActions.spotyThreadPool().execute(() -> {
                                try {
                                    CurrencyViewModel.updateItem(CurrencyViewModel.getId());
                                } catch (SQLException e) {
                                    SpotyLogger.writeToFile(e.getCause(), this.getClass());
                                }
                            });

                            SimpleNotification notification =
                                    new SimpleNotification.NotificationBuilder("Currency updated successfully")
                                            .duration(NotificationDuration.SHORT)
                                            .icon("fas-circle-check")
                                            .type(NotificationVariants.SUCCESS)
                                            .build();
                            notificationHolder.addNotification(notification);

                            closeDialog(event);
                            return;
                        }
                        GlobalActions.spotyThreadPool().execute(() -> {
                            try {
                                saveCurrency();
                            } catch (SQLException e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                        });

                        SimpleNotification notification =
                                new SimpleNotification.NotificationBuilder("Currency saved successfully")
                                        .duration(NotificationDuration.SHORT)
                                        .icon("fas-circle-check")
                                        .type(NotificationVariants.SUCCESS)
                                        .build();
                        notificationHolder.addNotification(notification);

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
}
