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

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.hrm.pay_roll.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll.*;
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
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class BeneficiaryBadgeFormController implements Initializable {
    private static BeneficiaryBadgeFormController instance;
    private final Stage stage;
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

    private BeneficiaryBadgeFormController(Stage stage) {
        this.stage = stage;
    }

    public static BeneficiaryBadgeFormController getInstance(Stage stage) {
        if (Objects.equals(instance, null)) {
            synchronized (BeneficiaryBadgeFormController.class) {
                instance = new BeneficiaryBadgeFormController(stage);
            }
        }
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input bindings.
        beneficiaryType.valueProperty().bindBidirectional(BeneficiaryBadgeViewModel.beneficiaryTypeProperty());
        name.textProperty().bindBidirectional(BeneficiaryBadgeViewModel.nameProperty());
        description.textProperty().bindBidirectional(BeneficiaryBadgeViewModel.descriptionProperty());
        colorPicker.textProperty().bindBidirectional(BeneficiaryBadgeViewModel.colorProperty());


        // ComboBox Converters.
        StringConverter<BeneficiaryType> beneficiaryTypeConverter =
                FunctionalStringConverter.to(
                        beneficiaryType -> (beneficiaryType == null) ? "" : beneficiaryType.getName());

        // ComboBox Filter Functions.
        Function<String, Predicate<BeneficiaryType>> beneficiaryTypeFilterFunction =
                searchStr ->
                        beneficiaryType ->
                                StringUtils.containsIgnoreCase(beneficiaryTypeConverter.toString(beneficiaryType), searchStr);

        // ProductType combo box properties.
        beneficiaryType.setConverter(beneficiaryTypeConverter);
        beneficiaryType.setFilterFunction(beneficiaryTypeFilterFunction);
        if (BeneficiaryTypeViewModel.getBeneficiaryTypes().isEmpty()) {
            BeneficiaryTypeViewModel.getBeneficiaryTypes()
                    .addListener(
                            (ListChangeListener<BeneficiaryType>)
                                    c -> beneficiaryType.setItems(BeneficiaryTypeViewModel.getBeneficiaryTypes()));
        } else {
            beneficiaryType.itemsProperty().bindBidirectional(BeneficiaryTypeViewModel.beneficiaryTypesProperty());
        }

        colorPicker.setItems(BeneficiaryBadgeViewModel.getColorsList());

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
                    beneficiaryTypeValidationLabel.setVisible(false);

                    nameValidationLabel.setManaged(false);
                    descriptionValidationLabel.setManaged(false);
                    colorPickerValidationLabel.setManaged(false);
                    beneficiaryTypeValidationLabel.setManaged(false);

                    beneficiaryType.clearSelection();
                    colorPicker.clearSelection();

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
                        MFXStageDialog dialog = (MFXStageDialog) name.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!colorConstraints.isEmpty()) {
                        colorPickerValidationLabel.setManaged(true);
                        colorPickerValidationLabel.setVisible(true);
                        colorPickerValidationLabel.setText(colorConstraints.getFirst().getMessage());
                        colorPicker.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) colorPicker.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!beneficiaryTypeConstraints.isEmpty()) {
                        beneficiaryTypeValidationLabel.setManaged(true);
                        beneficiaryTypeValidationLabel.setVisible(true);
                        beneficiaryTypeValidationLabel.setText(beneficiaryTypeConstraints.getFirst().getMessage());
                        beneficiaryType.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) beneficiaryType.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (nameConstraints.isEmpty()
                            && colorConstraints.isEmpty()
                            && beneficiaryTypeConstraints.isEmpty()) {
                        actionEvent = event;
                        if (BeneficiaryBadgeViewModel.getId() > 0) {
                            BeneficiaryBadgeViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                            return;
                        }
                        BeneficiaryBadgeViewModel.saveBeneficiaryBadge(this::onSuccess, this::successMessage, this::errorMessage);
                    }
                });
    }

    private void onSuccess() {
        beneficiaryType.clearSelection();
        colorPicker.clearSelection();
        closeDialog(actionEvent);
        BeneficiaryBadgeViewModel.resetProperties();
        BeneficiaryBadgeViewModel.getAllBeneficiaryBadges(null, null);
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

    private void successMessage(String message) {
        displayNotification(message, MessageVariants.SUCCESS, "fas-circle-check");
    }

    private void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, "fas-triangle-exclamation");
    }

    private void displayNotification(String message, MessageVariants type, String icon) {
        SpotyMessage notification = new SpotyMessage.MessageBuilder(message)
                .duration(MessageDuration.SHORT)
                .icon(icon)
                .type(type)
                .height(60)
                .build();
        AnchorPane.setTopAnchor(notification, 5.0);
        AnchorPane.setRightAnchor(notification, 5.0);

        var in = Animations.slideInDown(notification, Duration.millis(250));
        if (!BaseController.getInstance(stage).morphPane.getChildren().contains(notification)) {
            BaseController.getInstance(stage).morphPane.getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> SpotyMessage.delay(notification, stage));
        }
    }
}
