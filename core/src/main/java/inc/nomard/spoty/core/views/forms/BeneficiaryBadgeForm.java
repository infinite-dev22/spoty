package inc.nomard.spoty.core.views.forms;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.viewModels.hrm.pay_roll.BeneficiaryBadgeViewModel;
import inc.nomard.spoty.core.viewModels.hrm.pay_roll.BeneficiaryTypeViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.validatables.ValidatableComboBox;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextArea;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextField;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.message.SpotyMessage;
import inc.nomard.spoty.core.views.layout.message.enums.MessageDuration;
import inc.nomard.spoty.core.views.layout.message.enums.MessageVariants;
import inc.nomard.spoty.core.views.util.Validators;
import inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll.BeneficiaryType;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.StringConverter;
import lombok.extern.java.Log;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static inc.nomard.spoty.core.GlobalActions.closeDialog;
import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

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
        displayNotification(message, MessageVariants.SUCCESS, FontAwesomeSolid.CHECK_CIRCLE);
    }

    private void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, FontAwesomeSolid.EXCLAMATION_TRIANGLE);
    }

    private void displayNotification(String message, MessageVariants type, Ikon icon) {
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
