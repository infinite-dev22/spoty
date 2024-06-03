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

import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.utils.*;
import io.github.palexdev.materialfx.utils.others.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.net.*;
import java.util.*;
import java.util.function.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class UOMFormController implements Initializable {
    /**
     * =>When editing a row, the extra fields won't display even though the row clearly has a BaseUnit
     * filled in its combo. =>The dialog should animate to expand and contract when a BaseUnit is
     * present i.e. not just have a scroll view.
     */
    private static UOMFormController instance;
    @FXML
    public MFXTextField name,
            shortName,
            operatorValue;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
    @FXML
    public MFXFilterComboBox<UnitOfMeasure> baseUnit;
    @FXML
    public MFXComboBox<String> operator;
    @FXML
    public VBox formsHolder;
    @FXML
    public Label nameValidationLabel,
            operatorValidationLabel,
            operatorValueValidationLabel;
    @FXML
    public VBox inputsHolder;
    private List<Constraint> nameConstraints,
            operatorConstraints,
            operatorValueConstraints;
    private ActionEvent actionEvent = null;

    public static UOMFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new UOMFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form input binding.
        name.textProperty().bindBidirectional(UOMViewModel.nameProperty());
        shortName.textProperty().bindBidirectional(UOMViewModel.shortNameProperty());
        baseUnit.valueProperty().bindBidirectional(UOMViewModel.baseUnitProperty());
        operator.textProperty().bindBidirectional(UOMViewModel.operatorProperty());
        operatorValue.textProperty().bindBidirectional(UOMViewModel.operatorValueProperty());

        // Input listeners.
        baseUnit
                .valueProperty()
                .addListener(
                        observable -> {
                            if (baseUnit.getSelectedItem() != null) {
                                formsHolder.setVisible(true);
                                formsHolder.setManaged(true);
                                MFXStageDialog dialog = (MFXStageDialog) baseUnit.getScene().getWindow();
                                dialog.sizeToScene();
                            } else {
                                formsHolder.setManaged(false);
                                formsHolder.setVisible(false);
                                operatorValidationLabel.setVisible(false);
                                operatorValueValidationLabel.setVisible(false);
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
        operator.setItems(UOMViewModel.operatorList);
        baseUnit.setConverter(uomConverter);
        baseUnit.setFilterFunction(uomFilterFunction);
        if (UOMViewModel.getUnitsOfMeasure().isEmpty()) {
            UOMViewModel.getUnitsOfMeasure()
                    .addListener(
                            (ListChangeListener<UnitOfMeasure>)
                                    c -> baseUnit.setItems(UOMViewModel.getUnitsOfMeasure()));
        } else {
            baseUnit.itemsProperty().bindBidirectional(UOMViewModel.unitsOfMeasureProperty());
        }

        // Input validators.
        requiredValidator();

        setUomFormDialogOnActions();
    }

    private void setUomFormDialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);
                    UOMViewModel.resetUOMProperties();
                    baseUnit.clearSelection();
                    nameValidationLabel.setVisible(false);
                    operatorValidationLabel.setVisible(false);
                    operatorValueValidationLabel.setVisible(false);
                    formsHolder.setVisible(false);

                    nameValidationLabel.setManaged(false);
                    operatorValidationLabel.setManaged(false);
                    operatorValueValidationLabel.setManaged(false);
                    formsHolder.setManaged(false);

                    name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    operator.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    operatorValue.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    nameConstraints = name.validate();
                    if (Objects.nonNull(baseUnit.getSelectedItem())) {
                        operatorConstraints = operator.validate();
                        operatorValueConstraints = operatorValue.validate();
                    }
                    if (!nameConstraints.isEmpty()) {
                        nameValidationLabel.setManaged(true);
                        nameValidationLabel.setVisible(true);
                        nameValidationLabel.setText(nameConstraints.getFirst().getMessage());
                        name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) name.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (Objects.nonNull(baseUnit.getSelectedItem())) {
                        if (!operatorConstraints.isEmpty()) {
                            operatorValidationLabel.setManaged(true);
                            operatorValidationLabel.setVisible(true);
                            operatorValidationLabel.setText(operatorConstraints.getFirst().getMessage());
                            operator.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                            MFXStageDialog dialog = (MFXStageDialog) operator.getScene().getWindow();
                            dialog.sizeToScene();
                        }
                        if (!operatorValueConstraints.isEmpty()) {
                            operatorValueValidationLabel.setManaged(true);
                            operatorValueValidationLabel.setVisible(true);
                            operatorValueValidationLabel.setText(operatorValueConstraints.getFirst().getMessage());
                            operatorValue.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                            MFXStageDialog dialog = (MFXStageDialog) operator.getScene().getWindow();
                            dialog.sizeToScene();
                        }
                    }
                    if (nameConstraints.isEmpty()) {
                        if (Objects.nonNull(baseUnit.getSelectedItem())) {
                            if (!operatorConstraints.isEmpty()
                                    && !operatorValueConstraints.isEmpty()) {
                                return;
                            }
                        }
                        if (UOMViewModel.getId() > 0) {
                            UOMViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                            actionEvent = event;
                            return;
                        }
                        UOMViewModel.saveUOM(this::onSuccess, this::successMessage, this::errorMessage);
                        actionEvent = event;
                    }
                });
    }

    private void onSuccess() {
        closeDialog(actionEvent);
        UOMViewModel.resetUOMProperties();
        UOMViewModel.getAllUOMs(null, null);
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
        Constraint operatorValueConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Operator is required")
                        .setCondition(operatorValue.textProperty().length().greaterThan(0))
                        .get();
        operatorValue.getValidator().constraint(operatorValueConstraint);
        Constraint operatorConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Operator Value is required")
                        .setCondition(operator.textProperty().length().greaterThan(0))
                        .get();
        operator.getValidator().constraint(operatorConstraint);
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

        operatorValue
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                operatorValueValidationLabel.setManaged(false);
                                operatorValueValidationLabel.setVisible(false);
                                operatorValue.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        operator
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                operatorValidationLabel.setManaged(false);
                                operatorValidationLabel.setVisible(false);
                                operator.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }

    private void successMessage(String message) {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        AnchorPane.setRightAnchor(notification, 40.0);
        AnchorPane.setTopAnchor(notification, 10.0);
    }

    private void errorMessage(String message) {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
        notificationHolder.addMessage(notification);
        AnchorPane.setRightAnchor(notification, 40.0);
        AnchorPane.setTopAnchor(notification, 10.0);
    }
}
