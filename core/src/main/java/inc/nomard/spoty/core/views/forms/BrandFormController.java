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
import inc.nomard.spoty.core.viewModels.BrandViewModel;
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
import static inc.nomard.spoty.core.viewModels.BrandViewModel.clearBrandData;
import static inc.nomard.spoty.core.viewModels.BrandViewModel.saveBrand;
import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

public class BrandFormController implements Initializable {
    private static BrandFormController instance;
    @FXML
    public MFXTextField brandFormName,
            brandFormDescription;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
    @FXML
    public Label brandFormNameValidationLabel;
    private List<Constraint> nameConstraints;
    private ActionEvent actionEvent = null;

    public static BrandFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new BrandFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input bindings.
        brandFormName.textProperty().bindBidirectional(BrandViewModel.nameProperty());
        brandFormDescription.textProperty().bindBidirectional(BrandViewModel.descriptionProperty());
        // Input listeners.
        requiredValidator();
        dialogOnActions();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);
                    clearBrandData();

                    brandFormNameValidationLabel.setVisible(false);
                    brandFormNameValidationLabel.setManaged(false);
                    brandFormName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    nameConstraints = brandFormName.validate();
                    if (!nameConstraints.isEmpty()) {
                        brandFormNameValidationLabel.setManaged(true);
                        brandFormNameValidationLabel.setVisible(true);
                        brandFormNameValidationLabel.setText(nameConstraints.getFirst().getMessage());
                        brandFormName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (nameConstraints.isEmpty()) {
                        if (BrandViewModel.getId() > 0) {
                            try {
                                BrandViewModel.updateItem(this::onAction, this::onUpdatedSuccess, this::onFailed);
                                actionEvent = event;
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                            return;
                        }
                        try {
                            saveBrand(this::onAction, this::onAddSuccess, this::onFailed);
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
                new SpotyMessage.MessageBuilder("Brand added successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        closeDialog(actionEvent);
        clearBrandData();
        BrandViewModel.getAllBrands(null, null, null);
    }

    private void onUpdatedSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Brand updated successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        closeDialog(actionEvent);
        clearBrandData();
        BrandViewModel.getAllBrands(null, null, null);
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

        BrandViewModel.getAllBrands(null, null, null);
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint nameConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Name is required")
                        .setCondition(brandFormName.textProperty().length().greaterThan(0))
                        .get();
        brandFormName.getValidator().constraint(nameConstraint);
        // Display error.
        brandFormName
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                brandFormNameValidationLabel.setManaged(false);
                                brandFormNameValidationLabel.setVisible(false);
                                brandFormName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }
}
