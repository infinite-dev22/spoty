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
import inc.nomard.spoty.core.viewModels.UOMViewModel;
import inc.nomard.spoty.network_bridge.dtos.UnitOfMeasure;
import inc.nomard.spoty.utils.SpotyLogger;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

import static inc.nomard.spoty.core.GlobalActions.closeDialog;
import static inc.nomard.spoty.core.Validators.requiredValidator;

public class UOMFormController implements Initializable {
    /**
     * =>When editing a row, the extra fields won't display even though the row clearly has a BaseUnit
     * filled in its combo. =>The dialog should animate to expand and contract when a BaseUnit is
     * present i.e. not just have a scroll view.
     */
    private static UOMFormController instance;
    @FXML
    public MFXTextField uomFormName;
    @FXML
    public MFXTextField uomFormShortName;
    @FXML
    public MFXButton saveBtn;
    @FXML
    public MFXButton cancelBtn;
    @FXML
    public MFXFilterComboBox<UnitOfMeasure> uomFormBaseUnit;
    @FXML
    public MFXTextField uomFormOperator;
    @FXML
    public MFXTextField uomFormOperatorValue;
    @FXML
    public VBox formsHolder;
    @FXML
    public Label uomFormNameValidationLabel;
    @FXML
    public Label uomFormShortNameValidationLabel;
    @FXML
    public Label uomFormOperatorValidationLabel;
    @FXML
    public Label uomFormOperatorValueValidationLabel;
    @FXML
    public VBox inputsHolder;

    public static UOMFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new UOMFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form input binding.
        uomFormName.textProperty().bindBidirectional(UOMViewModel.nameProperty());
        uomFormShortName.textProperty().bindBidirectional(UOMViewModel.shortNameProperty());
        uomFormBaseUnit.valueProperty().bindBidirectional(UOMViewModel.baseUnitProperty());
        uomFormOperator.textProperty().bindBidirectional(UOMViewModel.operatorProperty());
        uomFormOperatorValue.textProperty().bindBidirectional(UOMViewModel.operatorValueProperty());

        // Input listeners.
        uomFormBaseUnit
                .valueProperty()
                .addListener(
                        observable -> {
                            if (uomFormBaseUnit.getSelectedItem() != null) {
                                formsHolder.setVisible(true);
                                formsHolder.setManaged(true);
                            } else {
                                formsHolder.setManaged(false);
                                formsHolder.setVisible(false);
                                uomFormOperatorValidationLabel.setVisible(false);
                                uomFormOperatorValueValidationLabel.setVisible(false);
                            }
                        });

        // ComboBox Converters.
        StringConverter<UnitOfMeasure> uomConverter =
                FunctionalStringConverter.to(
                        unitOfMeasure -> (unitOfMeasure == null) ? "" : unitOfMeasure.getName());

        // ComboBox Filter Functions.
        Function<String, Predicate<UnitOfMeasure>> uomFilterFunction =
                searchStr ->
                        unitOfMeasure ->
                                StringUtils.containsIgnoreCase(uomConverter.toString(unitOfMeasure), searchStr);

        // ComboBox properties.
        uomFormBaseUnit.setItems(UOMViewModel.getUnitsOfMeasure());
        uomFormBaseUnit.setConverter(uomConverter);
        uomFormBaseUnit.setFilterFunction(uomFilterFunction);

        // Input validators.
        requiredValidator(uomFormName, "Name is required.", uomFormNameValidationLabel, saveBtn);
        requiredValidator(
                uomFormShortName,
                "Short name field is required.",
                uomFormShortNameValidationLabel,
                saveBtn);
        requiredValidator(
                uomFormOperator, "Operator is required.", uomFormOperatorValidationLabel, saveBtn);
        requiredValidator(
                uomFormOperatorValue,
                "Operator value is required.",
                uomFormOperatorValueValidationLabel,
                saveBtn);

        setUomFormDialogOnActions();
    }

    private void setUomFormDialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);
                    UOMViewModel.resetUOMProperties();
                    uomFormBaseUnit.clearSelection();
                    uomFormNameValidationLabel.setVisible(false);
                    uomFormShortNameValidationLabel.setVisible(false);
                    uomFormOperatorValidationLabel.setVisible(false);
                    uomFormOperatorValueValidationLabel.setVisible(false);
                    formsHolder.setVisible(false);
                    formsHolder.setManaged(false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    if (!uomFormNameValidationLabel.isVisible()
                            && !uomFormShortNameValidationLabel.isVisible()
                            && !uomFormOperatorValidationLabel.isVisible()
                            && !uomFormOperatorValueValidationLabel.isVisible()) {
                        if (UOMViewModel.getId() > 0) {
                            try {
                                UOMViewModel.updateItem(this::onAction, this::onUpdatedSuccess, this::onFailed);
                                closeDialog(event);
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                            return;
                        }
                        try {
                            UOMViewModel.saveUOM(this::onAction, this::onAddSuccess, this::onFailed);
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
                new SpotyMessage.MessageBuilder("Unit Of Measure added successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        UOMViewModel.getAllUOMs(null, null, null);
    }

    private void onUpdatedSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Unit Of Measure updated successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        UOMViewModel.getAllUOMs(null, null, null);
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

        UOMViewModel.getAllUOMs(null, null, null);
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

        UOMViewModel.getAllUOMs(null, null, null);
    }
}
