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
import inc.nomard.spoty.core.viewModels.SupplierViewModel;
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

public class SupplierFormController implements Initializable {
    private static SupplierFormController instance;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
    @FXML
    public MFXTextField supplierFormName,
            supplierFormEmail,
            supplierFormPhone,
            supplierFormCity,
            supplierFormCountry,
            supplierFormTaxNumber,
            supplierFormAddress;
    @FXML
    public Label supplierFormNameValidationLabel,
            supplierFormEmailValidationLabel,
            supplierFormPhoneValidationLabel;
    private List<Constraint> nameConstraints,
            emailConstraints,
            phoneConstraints;
    private ActionEvent actionEvent = null;

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
        requiredValidator();

        // Email input validation.
        emailValidator(supplierFormEmail, supplierFormEmailValidationLabel, saveBtn);

        // Phone input validation.
        lengthValidator(
                supplierFormPhone,
                11,
                "Invalid length",
                supplierFormPhoneValidationLabel,
                saveBtn);
        dialogOnActions();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);
                    SupplierViewModel.resetProperties();
                    supplierFormNameValidationLabel.setVisible(false);
                    supplierFormEmailValidationLabel.setVisible(false);
                    supplierFormPhoneValidationLabel.setVisible(false);

                    supplierFormNameValidationLabel.setManaged(false);
                    supplierFormEmailValidationLabel.setManaged(false);
                    supplierFormPhoneValidationLabel.setManaged(false);

                    supplierFormName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    supplierFormEmail.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    supplierFormPhone.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    nameConstraints = supplierFormName.validate();
                    emailConstraints = supplierFormEmail.validate();
                    phoneConstraints = supplierFormPhone.validate();
                    if (!nameConstraints.isEmpty()) {
                        supplierFormNameValidationLabel.setManaged(true);
                        supplierFormNameValidationLabel.setVisible(true);
                        supplierFormNameValidationLabel.setText(nameConstraints.getFirst().getMessage());
                        supplierFormName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!emailConstraints.isEmpty()) {
                        supplierFormEmailValidationLabel.setManaged(true);
                        supplierFormEmailValidationLabel.setVisible(true);
                        supplierFormEmailValidationLabel.setText(emailConstraints.getFirst().getMessage());
                        supplierFormEmail.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!phoneConstraints.isEmpty()) {
                        supplierFormPhoneValidationLabel.setManaged(true);
                        supplierFormPhoneValidationLabel.setVisible(true);
                        supplierFormPhoneValidationLabel.setText(phoneConstraints.getFirst().getMessage());
                        supplierFormPhone.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (nameConstraints.isEmpty()
                            && emailConstraints.isEmpty()
                            && phoneConstraints.isEmpty()) {
                        if (SupplierViewModel.getId() > 0) {
                            try {
                                SupplierViewModel.updateItem(this::onAction, this::onUpdatedSuccess, this::onFailed);
                                actionEvent=event;
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                            return;
                        }
                        try {
                            SupplierViewModel.saveSupplier(this::onAction, this::onAddSuccess, this::onFailed);
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
                new SpotyMessage.MessageBuilder("Supplier added successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        closeDialog(actionEvent);
        SupplierViewModel.resetProperties();
        SupplierViewModel.getAllSuppliers(null, null, null);
    }

    private void onUpdatedSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Supplier updated successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        closeDialog(actionEvent);
        SupplierViewModel.resetProperties();
        SupplierViewModel.getAllSuppliers(null, null, null);
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

        SupplierViewModel.getAllSuppliers(null, null, null);
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint name =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Name is required")
                        .setCondition(supplierFormName.textProperty().length().greaterThan(0))
                        .get();
        supplierFormName.getValidator().constraint(name);
        Constraint email =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Email is required")
                        .setCondition(supplierFormEmail.textProperty().length().greaterThan(0))
                        .get();
        supplierFormEmail.getValidator().constraint(email);
        Constraint phone =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Phone is required")
                        .setCondition(supplierFormPhone.textProperty().length().greaterThan(0))
                        .get();
        supplierFormPhone.getValidator().constraint(phone);
        // Display error.
        supplierFormName
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                supplierFormNameValidationLabel.setManaged(false);
                                supplierFormNameValidationLabel.setVisible(false);
                                supplierFormName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        supplierFormEmail
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                supplierFormEmailValidationLabel.setManaged(false);
                                supplierFormEmailValidationLabel.setVisible(false);
                                supplierFormEmail.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        supplierFormPhone
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                supplierFormPhoneValidationLabel.setManaged(false);
                                supplierFormPhoneValidationLabel.setVisible(false);
                                supplierFormPhone.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }
}
