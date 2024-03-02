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
import inc.nomard.spoty.core.viewModels.ServiceViewModel;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
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
import static inc.nomard.spoty.core.viewModels.ServiceViewModel.clearServiceData;
import static inc.nomard.spoty.core.viewModels.ServiceViewModel.saveService;

public class ServiceInvoiceFormController implements Initializable {
    private static ServiceInvoiceFormController instance;
    @FXML
    public MFXButton bankSaveBtn;
    @FXML
    public MFXButton bankCancelBtn;
    public MFXTextField serviceName;
    public Label serviceNameValidationLabel;
    public MFXTextField serviceQuantity;
    public Label serviceQuantityValidationLabel;
    public MFXTextField serviceCharge;
    public Label serviceChargeValidationLabel;
    public MFXTextField serviceDiscountCharge;
    public Label serviceDiscountChargeValidationLabel;
    public MFXTextField serviceVat;
    public Label serviceVatValidationLabel;
    public MFXTextField shippingCost;
    public Label ShippingCostValidationLabel;
    public MFXFilterComboBox<String> paymentType; // TODO: Create table for payment types
    public MFXTextField paidAmount;
    public Label paidAmountValidationLabel;
    public Label paymentTypeValidationLabel;

    public static ServiceInvoiceFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new ServiceInvoiceFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input bindings.
        serviceName.textProperty().bindBidirectional(ServiceViewModel.nameProperty());
        serviceCharge.textProperty().bindBidirectional(ServiceViewModel.emailProperty());
        serviceVat.textProperty().bindBidirectional(ServiceViewModel.phoneProperty());
        serviceQuantity.textProperty().bindBidirectional(ServiceViewModel.townProperty());
        shippingCost.textProperty().bindBidirectional(ServiceViewModel.townProperty());
        paymentType.textProperty().bindBidirectional(ServiceViewModel.townProperty());
        paidAmount.textProperty().bindBidirectional(ServiceViewModel.townProperty());
        // Input listeners.
        requiredValidator(
                serviceName, "Field is required.", serviceNameValidationLabel, bankSaveBtn);
        requiredValidator(
                serviceCharge, "Field is required.", serviceChargeValidationLabel, bankSaveBtn);
        requiredValidator(
                serviceVat, "Field is required.", serviceVatValidationLabel, bankSaveBtn);
        requiredValidator(
                serviceQuantity, "Field is required.", serviceQuantityValidationLabel, bankSaveBtn);
        requiredValidator(
                shippingCost, "Field is required.", ShippingCostValidationLabel, bankSaveBtn);
        requiredValidator(
                paymentType, "Field is required.", paymentTypeValidationLabel, bankSaveBtn);
        dialogOnActions();
    }

    private void dialogOnActions() {
        bankCancelBtn.setOnAction(
                (event) -> {
                    clearServiceData();

                    closeDialog(event);

                    serviceNameValidationLabel.setVisible(false);
                    serviceChargeValidationLabel.setVisible(false);
                    serviceVatValidationLabel.setVisible(false);
                    serviceQuantityValidationLabel.setVisible(false);
                    ShippingCostValidationLabel.setVisible(false);
                    paymentTypeValidationLabel.setVisible(false);
                });
        bankSaveBtn.setOnAction(
                (event) -> {
                    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

                    if (!serviceNameValidationLabel.isVisible()
                            && !serviceChargeValidationLabel.isVisible()
                            && !serviceVatValidationLabel.isVisible()
                            && !serviceQuantityValidationLabel.isVisible()
                            && !ShippingCostValidationLabel.isVisible()
                            && !paymentTypeValidationLabel.isVisible()) {
                        if (ServiceViewModel.getId() > 0) {
                            SpotyThreader.spotyThreadPool(() -> {
                                try {
                                    ServiceViewModel.updateItem(this::onAction, this::onSuccess, this::onFailed);
                                } catch (Exception e) {
                                    SpotyLogger.writeToFile(e, this.getClass());
                                }
                            });

                            SimpleNotification notification =
                                    new SimpleNotification.NotificationBuilder("Service updated successfully")
                                            .duration(NotificationDuration.SHORT)
                                            .icon("fas-circle-check")
                                            .type(NotificationVariants.SUCCESS)
                                            .build();
                            notificationHolder.addNotification(notification);

                            closeDialog(event);
                            return;
                        }

//                        try {
//                            saveService();
//                        } catch (Exception e) {
//                            SpotyLogger.writeToFile(e, this.getClass());
//                        }

//                        SpotyThreader.spotyThreadPool(() -> {
                        try {
                            saveService(this::onAction, this::onSuccess, this::onFailed);
                        } catch (Exception e) {
                            SpotyLogger.writeToFile(e, this.getClass());
                        }
//                        });

                        SimpleNotification notification =
                                new SimpleNotification.NotificationBuilder("Service saved successfully")
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
        System.out.println("Loading service...");
    }

    private void onSuccess() {
        System.out.println("Loaded service...");
    }

    private void onFailed() {
        System.out.println("failed loading service...");
    }
}
