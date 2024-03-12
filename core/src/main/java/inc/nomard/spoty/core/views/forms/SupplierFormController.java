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
import inc.nomard.spoty.core.viewModels.SupplierViewModel;
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
import static inc.nomard.spoty.core.Validators.*;

public class SupplierFormController implements Initializable {
    private static SupplierFormController instance;
    @FXML
    public MFXButton supplierFormSaveBtn;
    @FXML
    public MFXButton supplierFormCancelBtn;
    @FXML
    public MFXTextField supplierFormName;
    @FXML
    public MFXTextField supplierFormEmail;
    @FXML
    public MFXTextField supplierFormPhone;
    @FXML
    public MFXTextField supplierFormCity;
    @FXML
    public MFXTextField supplierFormCountry;
    @FXML
    public MFXTextField supplierFormTaxNumber;
    @FXML
    public MFXTextField supplierFormAddress;
    @FXML
    public Label supplierFormNameValidationLabel;
    @FXML
    public Label supplierFormEmailValidationLabel;
    @FXML
    public Label supplierFormPhoneValidationLabel;

    public static SupplierFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new SupplierFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form input binding.
        supplierFormName.textProperty().bindBidirectional(SupplierViewModel.nameProperty());
        supplierFormEmail.textProperty().bindBidirectional(SupplierViewModel.emailProperty());
        supplierFormPhone.textProperty().bindBidirectional(SupplierViewModel.phoneProperty());
        supplierFormCity.textProperty().bindBidirectional(SupplierViewModel.cityProperty());
        supplierFormCountry.textProperty().bindBidirectional(SupplierViewModel.countryProperty());
        supplierFormTaxNumber.textProperty().bindBidirectional(SupplierViewModel.taxNumberProperty());
        supplierFormAddress.textProperty().bindBidirectional(SupplierViewModel.addressProperty());

        // Input listeners.
        supplierFormPhone
                .textProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (!newValue.matches("\\d*"))
                                supplierFormPhone.setText(newValue.replaceAll("\\D", ""));
                        });
        supplierFormPhone
                .focusedProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue != oldValue) supplierFormPhone.setLeadingIcon(new Label("+"));
                        });

        // Input validations.
        // Name input validation.
        requiredValidator(
                supplierFormName,
                "Name field is required.",
                supplierFormNameValidationLabel,
                supplierFormSaveBtn);

        // Email input validation.
        emailValidator(supplierFormEmail, supplierFormEmailValidationLabel, supplierFormSaveBtn);

        // Phone input validation.
        lengthValidator(
                supplierFormPhone,
                11,
                "Invalid length",
                supplierFormPhoneValidationLabel,
                supplierFormSaveBtn);
        dialogOnActions();
    }

    private void dialogOnActions() {
        supplierFormCancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);
                    SupplierViewModel.resetProperties();
                    supplierFormNameValidationLabel.setVisible(false);
                    supplierFormEmailValidationLabel.setVisible(false);
                    supplierFormPhoneValidationLabel.setVisible(false);
                });
        supplierFormSaveBtn.setOnAction(
                (event) -> {
                    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();
                    if (!supplierFormNameValidationLabel.isVisible()
                            && !supplierFormEmailValidationLabel.isVisible()
                            && !supplierFormPhoneValidationLabel.isVisible()) {
                        if (SupplierViewModel.getId() > 0) {
                            SpotyThreader.spotyThreadPool(() -> {
                                try {
                                    SupplierViewModel.updateItem(this::onAction, this::onSuccess, this::onFailed);
                                } catch (Exception e) {
                                    SpotyLogger.writeToFile(e, this.getClass());
                                }
                            });

                            SimpleNotification notification =
                                    new SimpleNotification.NotificationBuilder("Supplier updated successfully")
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
                                SupplierViewModel.saveSupplier(this::onAction, this::onSuccess, this::onFailed);
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                        });

                        SimpleNotification notification =
                                new SimpleNotification.NotificationBuilder("Supplier saved successfully")
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
        System.out.println("Loading suppliers...");
    }

    private void onSuccess() {
        System.out.println("Loaded suppliers...");
    }

    private void onFailed() {
        System.out.println("failed loading suppliers...");
    }
}