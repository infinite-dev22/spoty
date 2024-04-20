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
import inc.nomard.spoty.core.viewModels.hrm.pay_roll.BeneficiaryBadgeViewModel;
import inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll.BeneficiaryType;
import inc.nomard.spoty.utils.SpotyLogger;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static inc.nomard.spoty.core.GlobalActions.closeDialog;
import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

public class BeneficiaryBadgeFormController implements Initializable {
    private static BeneficiaryBadgeFormController instance;
    @FXML
    public MFXButton saveBtn, cancelBtn;
    @FXML
    public MFXFilterComboBox<BeneficiaryType> beneficiaryType;
    @FXML
    public MFXTextField name;
    public TextArea description;
    @FXML
    public Label descriptionValidationLabel,
            colorPickerValidationLabel,
            nameValidationLabel,
            beneficiaryTypeValidationLabel;
    @FXML
    public MFXComboBox<String> colorPicker;
    private List<Constraint> nameConstraints,
            colorConstraints,
            beneficiaryTypeConstraints;
    private ActionEvent actionEvent = null;

    public static BeneficiaryBadgeFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new BeneficiaryBadgeFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input bindings.
        beneficiaryType.valueProperty().bindBidirectional(BeneficiaryBadgeViewModel.beneficiaryTypeProperty());
        name.textProperty().bindBidirectional(BeneficiaryBadgeViewModel.nameProperty());
        description.textProperty().bindBidirectional(BeneficiaryBadgeViewModel.descriptionProperty());
        colorPicker.textProperty().bindBidirectional(BeneficiaryBadgeViewModel.colorProperty());
        // Input listeners.
        requiredValidator();
        dialogOnActions();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    BeneficiaryBadgeViewModel.resetProperties();
                    closeDialog(event);

                    nameValidationLabel.setVisible(false);
                    descriptionValidationLabel.setVisible(false);
                    colorPickerValidationLabel.setVisible(false);

                    nameValidationLabel.setManaged(false);
                    descriptionValidationLabel.setManaged(false);
                    colorPickerValidationLabel.setManaged(false);

                    name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    colorPicker.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    beneficiaryType.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    nameConstraints = name.validate();
                    colorConstraints = colorPicker.validate();
                    beneficiaryTypeConstraints = beneficiaryType.validate();
                    if (!nameConstraints.isEmpty()) {
                        nameValidationLabel.setManaged(true);
                        nameValidationLabel.setVisible(true);
                        nameValidationLabel.setText(nameConstraints.getFirst().getMessage());
                        name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!colorConstraints.isEmpty()) {
                        colorPickerValidationLabel.setManaged(true);
                        colorPickerValidationLabel.setVisible(true);
                        colorPickerValidationLabel.setText(colorConstraints.getFirst().getMessage());
                        colorPicker.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!beneficiaryTypeConstraints.isEmpty()) {
                        beneficiaryTypeValidationLabel.setManaged(true);
                        beneficiaryTypeValidationLabel.setVisible(true);
                        beneficiaryTypeValidationLabel.setText(beneficiaryTypeConstraints.getFirst().getMessage());
                        beneficiaryType.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (nameConstraints.isEmpty()
                            && colorConstraints.isEmpty()
                            && beneficiaryTypeConstraints.isEmpty()) {
                        if (BeneficiaryBadgeViewModel.getId() > 0) {
                            try {
                                BeneficiaryBadgeViewModel.updateItem(this::onAction, this::onUpdatedSuccess, this::onFailed);
                                actionEvent = event;
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                            return;
                        }

                        try {
                            BeneficiaryBadgeViewModel.saveBeneficiaryBadge(this::onAction, this::onAddSuccess, this::onFailed);
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
                new SpotyMessage.MessageBuilder("Beneficiary badge added successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        closeDialog(actionEvent);
        BeneficiaryBadgeViewModel.resetProperties();
        BeneficiaryBadgeViewModel.getAllBeneficiaryBadges(null, null, null);
    }

    private void onUpdatedSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Beneficiary badge updated successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        closeDialog(actionEvent);
        BeneficiaryBadgeViewModel.resetProperties();
        BeneficiaryBadgeViewModel.getAllBeneficiaryBadges(null, null, null);
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

        BeneficiaryBadgeViewModel.getAllBeneficiaryBadges(null, null, null);
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint nameConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Name is required")
                        .setCondition(name.textProperty().length().greaterThan(0))
                        .get();
        name.getValidator().constraint(nameConstraint);
        Constraint beneficiaryTypeConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Beneficiary Type is required")
                        .setCondition(beneficiaryType.textProperty().length().greaterThan(0))
                        .get();
        beneficiaryType.getValidator().constraint(beneficiaryTypeConstraint);
        Constraint colorPickerConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Color is required")
                        .setCondition(colorPicker.textProperty().length().greaterThan(0))
                        .get();
        colorPicker.getValidator().constraint(colorPickerConstraint);
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
        beneficiaryType
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                beneficiaryTypeValidationLabel.setManaged(false);
                                beneficiaryTypeValidationLabel.setVisible(false);
                                beneficiaryType.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        colorPicker
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                colorPickerValidationLabel.setManaged(false);
                                colorPickerValidationLabel.setVisible(false);
                                colorPicker.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }
}
