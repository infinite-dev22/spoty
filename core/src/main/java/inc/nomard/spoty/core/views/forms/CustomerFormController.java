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
import inc.nomard.spoty.core.viewModels.CustomerViewModel;
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
import static inc.nomard.spoty.core.Validators.emailValidator;
import static inc.nomard.spoty.core.Validators.lengthValidator;
import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

public class CustomerFormController implements Initializable {
    private static CustomerFormController instance;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
    @FXML
    public MFXTextField customerFormName,
            customerFormEmail,
            customerFormPhone,
            customerFormCity,
            customerFormCountry,
            customerFormTaxNumber,
            customerFormAddress;
    @FXML
    public Label customerFormNameValidationLabel,
            customerFormEmailValidationLabel,
            customerFormPhoneValidationLabel;
    private List<Constraint> nameConstraints,
            emailConstraints,
            phoneConstraints;
    private ActionEvent actionEvent = null;

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
        requiredValidator();

        // Email input validation.
        emailValidator(customerFormEmail, customerFormEmailValidationLabel, saveBtn);

        // Phone input validation.
        lengthValidator(customerFormPhone, 11, "Invalid length", customerFormPhoneValidationLabel, saveBtn);
        dialogOnActions();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);
                    CustomerViewModel.resetProperties();
                    customerFormNameValidationLabel.setVisible(false);
                    customerFormEmailValidationLabel.setVisible(false);
                    customerFormPhoneValidationLabel.setVisible(false);

                    customerFormNameValidationLabel.setManaged(false);
                    customerFormEmailValidationLabel.setManaged(false);
                    customerFormPhoneValidationLabel.setManaged(false);

                    customerFormName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    customerFormEmail.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    customerFormPhone.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);

                });
        saveBtn.setOnAction(
                (event) -> {
                    nameConstraints = customerFormName.validate();
                    emailConstraints = customerFormEmail.validate();
                    phoneConstraints = customerFormPhone.validate();
                    if (!nameConstraints.isEmpty()) {
                        customerFormNameValidationLabel.setManaged(true);
                        customerFormNameValidationLabel.setVisible(true);
                        customerFormNameValidationLabel.setText(nameConstraints.getFirst().getMessage());
                        customerFormName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!emailConstraints.isEmpty()) {
                        customerFormEmailValidationLabel.setManaged(true);
                        customerFormEmailValidationLabel.setVisible(true);
                        customerFormEmailValidationLabel.setText(emailConstraints.getFirst().getMessage());
                        customerFormEmail.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!phoneConstraints.isEmpty()) {
                        customerFormPhoneValidationLabel.setManaged(true);
                        customerFormPhoneValidationLabel.setVisible(true);
                        customerFormPhoneValidationLabel.setText(phoneConstraints.getFirst().getMessage());
                        customerFormPhone.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (nameConstraints.isEmpty()
                            && emailConstraints.isEmpty()
                            && phoneConstraints.isEmpty()) {
                        if (CustomerViewModel.getId() > 0) {
                            try {
                                CustomerViewModel.updateItem(this::onAction, this::onUpdatedSuccess, this::onFailed);
                                actionEvent=event;
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                            return;
                        }
                        try {
                            CustomerViewModel.saveCustomer(this::onAction, this::onAddSuccess, this::onFailed);
                            actionEvent=event;
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
                new SpotyMessage.MessageBuilder("Customer added successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        closeDialog(actionEvent);
        CustomerViewModel.resetProperties();
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

        closeDialog(actionEvent);
        CustomerViewModel.resetProperties();
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

    public void requiredValidator() {
        // Name input validation.
        Constraint name =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Name is required")
                        .setCondition(customerFormName.textProperty().length().greaterThan(0))
                        .get();
        customerFormName.getValidator().constraint(name);
        Constraint email =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Email is required")
                        .setCondition(customerFormEmail.textProperty().length().greaterThan(0))
                        .get();
        customerFormEmail.getValidator().constraint(email);
        Constraint phone =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Phone is required")
                        .setCondition(customerFormPhone.textProperty().length().greaterThan(0))
                        .get();
        customerFormPhone.getValidator().constraint(phone);
        // Display error.
        customerFormName
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                customerFormNameValidationLabel.setManaged(false);
                                customerFormNameValidationLabel.setVisible(false);
                                customerFormName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        customerFormEmail
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                customerFormEmailValidationLabel.setManaged(false);
                                customerFormEmailValidationLabel.setVisible(false);
                                customerFormEmail.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        customerFormPhone
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                customerFormPhoneValidationLabel.setManaged(false);
                                customerFormPhoneValidationLabel.setVisible(false);
                                customerFormPhone.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }
}
