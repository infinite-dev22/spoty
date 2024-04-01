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

import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.MessageDuration;
import inc.nomard.spoty.core.components.message.enums.MessageVariants;
import inc.nomard.spoty.core.viewModels.CustomerViewModel;
import inc.nomard.spoty.utils.SpotyLogger;
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

public class CustomerFormController implements Initializable {
    private static CustomerFormController instance;
    @FXML
    public MFXButton saveBtn;
    @FXML
    public MFXButton cancelBtn;
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
                customerFormName, "Name field is required.", validationLabel1, saveBtn);

        // Email input validation.
        emailValidator(customerFormEmail, validationLabel2, saveBtn);

        // Phone input validation.
        lengthValidator(customerFormPhone, 11, "Invalid length", validationLabel3, saveBtn);
        dialogOnActions();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);

                    validationLabel1.setVisible(false);
                    validationLabel2.setVisible(false);
                    validationLabel3.setVisible(false);

                    CustomerViewModel.resetProperties();
                });
        saveBtn.setOnAction(
                (event) -> {
                    if (!validationLabel1.isVisible()
                            && !validationLabel2.isVisible()
                            && !validationLabel3.isVisible()) {
                        if (CustomerViewModel.getId() > 0) {
                                try {
                                    CustomerViewModel.updateItem(this::onAction, this::onUpdatedSuccess, this::onFailed);
                                    closeDialog(event);
                                } catch (Exception e) {
                                    SpotyLogger.writeToFile(e, this.getClass());
                                }
                            return;
                        }
                            try {
                                CustomerViewModel.saveCustomer(this::onAction, this::onAddSuccess, this::onFailed);
                                closeDialog(event);
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                        return;
                    }
                    onRequiredFieldsMissing();
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
                new SpotyMessage.MessageBuilder("Customer added successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        CustomerViewModel.getAllCustomers(null, null, null);
    }

    private void onUpdatedSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Customer updated successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        CustomerViewModel.getAllCustomers(null, null, null);
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

        CustomerViewModel.getAllCustomers(null, null, null);
    }

    private void onRequiredFieldsMissing() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Required fields can't be null")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        CustomerViewModel.getAllCustomers(null, null, null);
    }
}
