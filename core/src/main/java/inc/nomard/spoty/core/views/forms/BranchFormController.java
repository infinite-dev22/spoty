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
import inc.nomard.spoty.core.viewModels.BranchViewModel;
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
import static inc.nomard.spoty.core.viewModels.BranchViewModel.clearBranchData;
import static inc.nomard.spoty.core.viewModels.BranchViewModel.saveBranch;
import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

public class BranchFormController implements Initializable {
    private static BranchFormController instance;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
    @FXML
    public MFXTextField branchFormName,
            branchFormEmail,
            branchFormPhone,
            branchFormTown,
            branchFormCity,
            branchFormZipCode;
    @FXML
    public Label branchFormEmailValidationLabel,
            branchFormCityValidationLabel,
            branchFormTownValidationLabel,
            branchFormPhoneValidationLabel,
            branchFormNameValidationLabel;
    private List<Constraint> nameConstraints,
            townConstraints,
            cityConstraints;
    private ActionEvent actionEvent = null;

    public static BranchFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new BranchFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input bindings.
        branchFormName.textProperty().bindBidirectional(BranchViewModel.nameProperty());
        branchFormEmail.textProperty().bindBidirectional(BranchViewModel.emailProperty());
        branchFormPhone.textProperty().bindBidirectional(BranchViewModel.phoneProperty());
        branchFormTown.textProperty().bindBidirectional(BranchViewModel.townProperty());
        branchFormCity.textProperty().bindBidirectional(BranchViewModel.cityProperty());
        // Input listeners.
        requiredValidator();
        dialogOnActions();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    clearBranchData();
                    closeDialog(event);

                    branchFormNameValidationLabel.setVisible(false);
                    branchFormEmailValidationLabel.setVisible(false);
                    branchFormPhoneValidationLabel.setVisible(false);
                    branchFormTownValidationLabel.setVisible(false);
                    branchFormCityValidationLabel.setVisible(false);

                    branchFormNameValidationLabel.setManaged(false);
                    branchFormEmailValidationLabel.setManaged(false);
                    branchFormPhoneValidationLabel.setManaged(false);
                    branchFormTownValidationLabel.setManaged(false);
                    branchFormCityValidationLabel.setManaged(false);

                    branchFormName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    branchFormTown.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    branchFormCity.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    nameConstraints = branchFormName.validate();
                    townConstraints = branchFormTown.validate();
                    cityConstraints = branchFormCity.validate();
                    if (!nameConstraints.isEmpty()) {
                        branchFormNameValidationLabel.setManaged(true);
                        branchFormNameValidationLabel.setVisible(true);
                        branchFormNameValidationLabel.setText(nameConstraints.getFirst().getMessage());
                        branchFormName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!townConstraints.isEmpty()) {
                        branchFormTownValidationLabel.setManaged(true);
                        branchFormTownValidationLabel.setVisible(true);
                        branchFormTownValidationLabel.setText(townConstraints.getFirst().getMessage());
                        branchFormTown.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!cityConstraints.isEmpty()) {
                        branchFormCityValidationLabel.setManaged(true);
                        branchFormCityValidationLabel.setVisible(true);
                        branchFormCityValidationLabel.setText(cityConstraints.getFirst().getMessage());
                        branchFormCity.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (nameConstraints.isEmpty()
                            && townConstraints.isEmpty()
                            && cityConstraints.isEmpty()) {
                        if (BranchViewModel.getId() > 0) {
                            try {
                                BranchViewModel.updateItem(this::onAction, this::onUpdatedSuccess, this::onFailed);
                                actionEvent = event;
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                            return;
                        }

                        try {
                            saveBranch(this::onAction, this::onAddSuccess, this::onFailed);
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
                new SpotyMessage.MessageBuilder("Branch added successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        closeDialog(actionEvent);
        clearBranchData();
        BranchViewModel.getAllBranches(null, null, null);
    }

    private void onUpdatedSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Branch updated successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        closeDialog(actionEvent);
        clearBranchData();
        BranchViewModel.getAllBranches(null, null, null);
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

        BranchViewModel.getAllBranches(null, null, null);
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint branchFormNameConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Name is required")
                        .setCondition(branchFormName.textProperty().length().greaterThan(0))
                        .get();
        branchFormName.getValidator().constraint(branchFormNameConstraint);
        Constraint branchFormCityConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Beneficiary Type is required")
                        .setCondition(branchFormCity.textProperty().length().greaterThan(0))
                        .get();
        branchFormCity.getValidator().constraint(branchFormCityConstraint);
        Constraint branchFormTownConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Color is required")
                        .setCondition(branchFormTown.textProperty().length().greaterThan(0))
                        .get();
        branchFormTown.getValidator().constraint(branchFormTownConstraint);
        // Display error.
        branchFormName
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                branchFormNameValidationLabel.setManaged(false);
                                branchFormNameValidationLabel.setVisible(false);
                                branchFormName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        branchFormCity
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                branchFormCityValidationLabel.setManaged(false);
                                branchFormCityValidationLabel.setVisible(false);
                                branchFormCity.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        branchFormTown
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                branchFormTownValidationLabel.setManaged(false);
                                branchFormTownValidationLabel.setVisible(false);
                                branchFormTown.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }
}
