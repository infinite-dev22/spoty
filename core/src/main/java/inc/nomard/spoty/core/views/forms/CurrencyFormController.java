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

import inc.nomard.spoty.core.components.notification.SimpleNotification;
import inc.nomard.spoty.core.components.notification.SimpleNotificationHolder;
import inc.nomard.spoty.core.components.notification.enums.NotificationDuration;
import inc.nomard.spoty.core.components.notification.enums.NotificationVariants;
import inc.nomard.spoty.core.viewModels.CurrencyViewModel;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static inc.nomard.spoty.core.GlobalActions.closeDialog;
import static inc.nomard.spoty.core.Validators.requiredValidator;
import static inc.nomard.spoty.core.viewModels.CurrencyViewModel.clearCurrencyData;
import static inc.nomard.spoty.core.viewModels.CurrencyViewModel.saveCurrency;

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
                            SpotyThreader.spotyThreadPool(() -> {
                                try {
                                    CurrencyViewModel.updateItem(this::onAction, this::onSuccess, this::onFailed);
                                } catch (Exception e) {
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
                        SpotyThreader.spotyThreadPool(() -> {
                            try {
                                saveCurrency(this::onAction, this::onSuccess, this::onFailed);
                            } catch (Exception e) {
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

    private void onAction() {
        System.out.println("Loading currency...");
    }

    private void onSuccess() {
        System.out.println("Loaded currency...");
    }

    private void onFailed() {
        System.out.println("failed loading currency...");
    }
}