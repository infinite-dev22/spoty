package inc.nomard.spoty.core.views.forms;

import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.viewModels.hrm.pay_roll.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.components.validatables.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.utils.*;
import io.github.palexdev.materialfx.utils.others.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import java.util.*;
import java.util.function.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class BeneficiaryBadgeForm extends MFXGenericDialog {
    public CustomButton saveBtn;
    public Button cancelBtn;
    public ValidatableComboBox<BeneficiaryType> beneficiaryType;
    public ValidatableTextField name;
    public ValidatableTextArea description;
    public Label colorPickerValidationLabel,
            nameValidationLabel,
            beneficiaryTypeValidationLabel;
    public ValidatableComboBox<String> colorPicker;
    private List<Constraint> nameConstraints,
            colorConstraints,
            beneficiaryTypeConstraints;
    private Event actionEvent = null;

    public BeneficiaryBadgeForm() {
        init();
    }

    public void init() {
        buildDialogContent();
        requiredValidator();
        dialogOnActions();
    }


    private VBox buildName() {
        // Input.
        name = new ValidatableTextField();
        var label = new Label("Name");
        name.setPrefWidth(400d);
        name.textProperty().bindBidirectional(BeneficiaryBadgeViewModel.nameProperty());
        // Validation.
        nameValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, name, nameValidationLabel);
        return vbox;
    }

    private VBox buildBeneficiaryType() {
        // Input.
        beneficiaryType = new ValidatableComboBox<>();
        var label = new Label("Beneficiary Type");
        beneficiaryType.setPrefWidth(400d);
        beneficiaryType.valueProperty().bindBidirectional(BeneficiaryBadgeViewModel.beneficiaryTypeProperty());
        // Converter
        StringConverter<BeneficiaryType> beneficiaryTypeConverter =
                FunctionalStringConverter.to(
                        beneficiaryType -> (beneficiaryType == null) ? "" : beneficiaryType.getName());
        // Filter function
        Function<String, Predicate<BeneficiaryType>> beneficiaryTypeFilterFunction =
                searchStr ->
                        beneficiaryType ->
                                StringUtils.containsIgnoreCase(beneficiaryTypeConverter.toString(beneficiaryType), searchStr);
        // Properties
        beneficiaryType.setConverter(beneficiaryTypeConverter);
        if (BeneficiaryTypeViewModel.getBeneficiaryTypes().isEmpty()) {
            BeneficiaryTypeViewModel.getBeneficiaryTypes()
                    .addListener(
                            (ListChangeListener<BeneficiaryType>)
                                    c -> beneficiaryType.setItems(BeneficiaryTypeViewModel.getBeneficiaryTypes()));
        } else {
            beneficiaryType.itemsProperty().bindBidirectional(BeneficiaryTypeViewModel.beneficiaryTypesProperty());
        }
        // Validation.
        beneficiaryTypeValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, beneficiaryType, beneficiaryTypeValidationLabel);
        return vbox;
    }

    private VBox buildColor() {
        // Input.
        colorPicker = new ValidatableComboBox<>();
        var label = new Label("Appearance Color");
        colorPicker.setPrefWidth(400d);
        colorPicker.valueProperty().bindBidirectional(BeneficiaryBadgeViewModel.colorProperty());
        colorPicker.setItems(BeneficiaryBadgeViewModel.getColorsList());
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, colorPicker);
        return vbox;
    }

    private VBox buildDescription() {
        // Input.
        description = new ValidatableTextArea();
        var label = new Label("Description");
        description.setPrefWidth(400d);
        description.textProperty().bindBidirectional(BeneficiaryBadgeViewModel.descriptionProperty());
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, description);
        return vbox;
    }

    private VBox buildCenter() {
        var vbox = new VBox();
        vbox.setSpacing(8d);
        vbox.setPadding(new Insets(10d));
        vbox.getChildren().addAll(buildName(), buildBeneficiaryType(), buildColor(), buildDescription());
        return vbox;
    }

    private CustomButton buildSaveButton() {
        saveBtn = new CustomButton("Save");
        saveBtn.getStyleClass().add(Styles.ACCENT);
        return saveBtn;
    }

    private Button buildCancelButton() {
        cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add(Styles.BUTTON_OUTLINED);
        return cancelBtn;
    }

    private HBox buildBottom() {
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setSpacing(20d);
        hbox.getChildren().addAll(buildSaveButton(), buildCancelButton());
        return hbox;
    }

    private void buildDialogContent() {
        this.setCenter(buildCenter());
        this.setBottom(buildBottom());
        this.setShowMinimize(false);
        this.setShowAlwaysOnTop(false);
        this.setShowClose(false);
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    BeneficiaryBadgeViewModel.resetProperties();
                    closeDialog(event);

                    nameValidationLabel.setVisible(false);
                    colorPickerValidationLabel.setVisible(false);
                    beneficiaryTypeValidationLabel.setVisible(false);

                    nameValidationLabel.setManaged(false);
                    colorPickerValidationLabel.setManaged(false);
                    beneficiaryTypeValidationLabel.setManaged(false);

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
        closeDialog(actionEvent);
        BeneficiaryBadgeViewModel.resetProperties();
        BeneficiaryBadgeViewModel.getAllBeneficiaryBadges(null, null, null, null);
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
                        .setCondition(beneficiaryType.valueProperty().isNotNull())
                        .get();
        beneficiaryType.getValidator().constraint(beneficiaryTypeConstraint);
        Constraint colorPickerConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Color is required")
                        .setCondition(colorPicker.valueProperty().isNotNull())
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
        if (!AppManager.getMorphPane().getChildren().contains(notification)) {
            AppManager.getMorphPane().getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> SpotyMessage.delay(notification));
        }
    }
}
