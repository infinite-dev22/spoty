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

import inc.normad.spoty.core.components.notification.SimpleNotification;
import inc.normad.spoty.core.components.notification.SimpleNotificationHolder;
import inc.normad.spoty.core.components.notification.enums.NotificationDuration;
import inc.normad.spoty.core.components.notification.enums.NotificationVariants;
import inc.normad.spoty.core.viewModels.BankViewModel;
import inc.normad.spoty.utils.SpotyLogger;
import inc.normad.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static inc.normad.spoty.core.GlobalActions.closeDialog;
import static inc.normad.spoty.core.Validators.requiredValidator;
import static inc.normad.spoty.core.viewModels.BankViewModel.clearBankData;
import static inc.normad.spoty.core.viewModels.BankViewModel.saveBank;

public class BankFormController implements Initializable {
    private static BankFormController instance;
    @FXML
    public MFXTextField bankName;
    @FXML
    public Label bankNameValidationLabel;
    @FXML
    public MFXTextField accountName;
    @FXML
    public Label accountNameValidationLabel;
    @FXML
    public MFXTextField accountNumber;
    @FXML
    public Label accountNumberValidationLabel;
    @FXML
    public MFXTextField branch;
    @FXML
    public Label branchValidationLabel;
    @FXML
    public MFXButton logo;
    @FXML
    public MFXButton bankSaveBtn;
    @FXML
    public MFXButton bankCancelBtn;

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
        requiredValidator(
                bankName, "Name is required.", bankNameValidationLabel, bankSaveBtn);
        requiredValidator(
                accountName, "Email is required.", accountNameValidationLabel, bankSaveBtn);
        requiredValidator(
                accountNumber, "Phone is required.", accountNumberValidationLabel, bankSaveBtn);
        requiredValidator(
                branch, "Town is required.", branchValidationLabel, bankSaveBtn);
        dialogOnActions();
    }

    private void dialogOnActions() {
        bankCancelBtn.setOnAction(
                (event) -> {
                    clearBankData();

                    closeDialog(event);

                    bankNameValidationLabel.setVisible(false);
                    accountNameValidationLabel.setVisible(false);
                    accountNumberValidationLabel.setVisible(false);
                    branchValidationLabel.setVisible(false);
                });
        bankSaveBtn.setOnAction(
                (event) -> {
                    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

                    if (!bankNameValidationLabel.isVisible()
                            && !accountNameValidationLabel.isVisible()
                            && !accountNumberValidationLabel.isVisible()
                            && !branchValidationLabel.isVisible()) {
                        if (BankViewModel.getId() > 0) {
                            SpotyThreader.spotyThreadPool(() -> {
                                try {
                                    BankViewModel.updateItem(this::onAction, this::onSuccess, this::onFailed);
                                } catch (Exception e) {
                                    SpotyLogger.writeToFile(e, this.getClass());
                                }
                            });

                            SimpleNotification notification =
                                    new SimpleNotification.NotificationBuilder("Bank updated successfully")
                                            .duration(NotificationDuration.SHORT)
                                            .icon("fas-circle-check")
                                            .type(NotificationVariants.SUCCESS)
                                            .build();
                            notificationHolder.addNotification(notification);

                            closeDialog(event);
                            return;
                        }

//                        try {
//                            saveBank();
//                        } catch (Exception e) {
//                            SpotyLogger.writeToFile(e, this.getClass());
//                        }

//                        SpotyThreader.spotyThreadPool(() -> {
                        try {
                            saveBank(this::onAction, this::onSuccess, this::onFailed);
                        } catch (Exception e) {
                            SpotyLogger.writeToFile(e, this.getClass());
                        }
//                        });

                        SimpleNotification notification =
                                new SimpleNotification.NotificationBuilder("Bank saved successfully")
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
        System.out.println("Loading bank...");
    }

    private void onSuccess() {
        System.out.println("Loaded bank...");
    }

    private void onFailed() {
        System.out.println("failed loading bank...");
    }
}
