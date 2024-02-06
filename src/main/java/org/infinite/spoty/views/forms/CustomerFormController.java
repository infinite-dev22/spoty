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

package org.infinite.spoty.views.forms;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.infinite.spoty.components.notification.SimpleNotification;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.components.notification.enums.NotificationDuration;
import org.infinite.spoty.components.notification.enums.NotificationVariants;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.utils.SpotyThreader;
import org.infinite.spoty.viewModels.CustomerViewModel;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.Validators.*;

public class CustomerFormController implements Initializable {
    private static CustomerFormController instance;
    @FXML
    public MFXButton customerFormSaveBtn;
    @FXML
    public MFXButton customerFormCancelBtn;
    @FXML
    public MFXTextField customerFormName;
    @FXML
    public MFXTextField customerFormEmail;
    @FXML
    public MFXTextField customerFormPhone;
    @FXML
    public MFXTextField customerFormCity;
    @FXML
    public MFXTextField customerFormCountry;
    @FXML
    public MFXTextField customerFormTaxNumber;
    @FXML
    public MFXTextField customerFormAddress;
    @FXML
    public Label validationLabel1, validationLabel2, validationLabel3;

    public static CustomerFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new CustomerFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form input listeners.
        // These don't work, not sure why.
        // TODO: Get a better way for input filtering.
        customerFormPhone
                .textProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (!newValue.matches("\\d*"))
                                customerFormPhone.setText(newValue.replaceAll("\\D", ""));
                        });
        customerFormPhone
                .focusedProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue != oldValue) customerFormPhone.setLeadingIcon(new Label("+"));
                            System.out.println("newValue oldValue");
                        });

        // Form input binding.
        customerFormName.textProperty().bindBidirectional(CustomerViewModel.nameProperty());
        customerFormEmail.textProperty().bindBidirectional(CustomerViewModel.emailProperty());
        customerFormPhone.textProperty().bindBidirectional(CustomerViewModel.phoneProperty());
        customerFormCity.textProperty().bindBidirectional(CustomerViewModel.cityProperty());
        customerFormCountry.textProperty().bindBidirectional(CustomerViewModel.countryProperty());
        customerFormTaxNumber.textProperty().bindBidirectional(CustomerViewModel.taxNumberProperty());
        customerFormAddress.textProperty().bindBidirectional(CustomerViewModel.addressProperty());

        // Name input validation.
        requiredValidator(
                customerFormName, "Name field is required.", validationLabel1, customerFormSaveBtn);

        // Email input validation.
        emailValidator(customerFormEmail, validationLabel2, customerFormSaveBtn);

        // Phone input validation.
        lengthValidator(customerFormPhone, 11, "Invalid length", validationLabel3, customerFormSaveBtn);
        dialogOnActions();
    }

    private void dialogOnActions() {
        customerFormCancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);

                    validationLabel1.setVisible(false);
                    validationLabel2.setVisible(false);
                    validationLabel3.setVisible(false);

                    CustomerViewModel.resetProperties();
                });
        customerFormSaveBtn.setOnAction(
                (event) -> {
                    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

                    if (!validationLabel1.isVisible()
                            && !validationLabel2.isVisible()
                            && !validationLabel3.isVisible()) {
                        if (CustomerViewModel.getId() > 0) {
                            SpotyThreader.spotyThreadPool(() -> {
                                try {
                                    CustomerViewModel.updateItem(this::onAction, this::onSuccess, this::onFailed);
                                } catch (Exception e) {
                                    SpotyLogger.writeToFile(e, this.getClass());
                                }
                            });

                            SimpleNotification notification =
                                    new SimpleNotification.NotificationBuilder("Customer updated successfully")
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
                                CustomerViewModel.saveCustomer(this::onAction, this::onSuccess, this::onFailed);
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                        });

                        SimpleNotification notification =
                                new SimpleNotification.NotificationBuilder("Customer saved successfully")
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
        System.out.println("Loading customers...");
    }

    private void onSuccess() {
        System.out.println("Loaded customers...");
    }

    private void onFailed() {
        System.out.println("failed loading customers...");
    }
}
