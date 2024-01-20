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

package org.infinite.spoty.forms;

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
import org.infinite.spoty.components.notification.SimpleNotification;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.components.notification.enums.NotificationDuration;
import org.infinite.spoty.components.notification.enums.NotificationVariants;
import org.infinite.spoty.data_source.dtos.UnitOfMeasure;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.utils.SpotyThreader;
import org.infinite.spoty.viewModels.UOMViewModel;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.Validators.requiredValidator;

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
    public MFXButton uomFormSaveBtn;
    @FXML
    public MFXButton uomFormCancelBtn;
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
        requiredValidator(uomFormName, "Name is required.", uomFormNameValidationLabel, uomFormSaveBtn);
        requiredValidator(
                uomFormShortName,
                "Short name field is required.",
                uomFormShortNameValidationLabel,
                uomFormSaveBtn);
        requiredValidator(
                uomFormOperator, "Operator is required.", uomFormOperatorValidationLabel, uomFormSaveBtn);
        requiredValidator(
                uomFormOperatorValue,
                "Operator value is required.",
                uomFormOperatorValueValidationLabel,
                uomFormSaveBtn);

        setUomFormDialogOnActions();
    }

    private void setUomFormDialogOnActions() {
        uomFormCancelBtn.setOnAction(
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
        uomFormSaveBtn.setOnAction(
                (event) -> {
                    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

                    if (!uomFormNameValidationLabel.isVisible()
                            && !uomFormShortNameValidationLabel.isVisible()
                            && !uomFormOperatorValidationLabel.isVisible()
                            && !uomFormOperatorValueValidationLabel.isVisible()) {
                        if (UOMViewModel.getId() > 0) {
                            SpotyThreader.spotyThreadPool(() -> {
                                try {
                                    UOMViewModel.updateItem();
                                } catch (Exception e) {
                                    SpotyLogger.writeToFile(e, this.getClass());
                                }
                            });

                            SimpleNotification notification =
                                    new SimpleNotification.NotificationBuilder("Unit of measure updated successfully")
                                            .duration(NotificationDuration.SHORT)
                                            .icon("fas-circle-check")
                                            .type(NotificationVariants.SUCCESS)
                                            .build();
                            notificationHolder.addNotification(notification);

                            uomFormBaseUnit.clearSelection();

                            closeDialog(event);
                            return;
                        }
                        SpotyThreader.spotyThreadPool(() -> {
                            try {
                                UOMViewModel.saveUOM();
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                        });

                        SimpleNotification notification =
                                new SimpleNotification.NotificationBuilder("Unit of measure saved successfully")
                                        .duration(NotificationDuration.SHORT)
                                        .icon("fas-circle-check")
                                        .type(NotificationVariants.SUCCESS)
                                        .build();
                        notificationHolder.addNotification(notification);

                        uomFormBaseUnit.clearSelection();

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
}
