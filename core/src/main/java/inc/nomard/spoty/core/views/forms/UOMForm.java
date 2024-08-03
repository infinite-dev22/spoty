package inc.nomard.spoty.core.views.forms;

import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.components.validatables.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.*;
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
public class UOMForm extends ModalPage {
    /**
     * =>When editing a row, the extra fields won't display even though the row clearly has a BaseUnit
     * filled in its combo. =>The dialog should animate to expand and contract when a BaseUnit is
     * present i.e. not just have a scroll view.
     */
    public ValidatableTextField name,
            shortName,
            operatorValue;
    public Button saveBtn,
            cancelBtn;
    public ValidatableComboBox<UnitOfMeasure> baseUnit;
    public ValidatableComboBox<String> operator;
    public VBox formsHolder;
    public Label nameValidationLabel,
            operatorValidationLabel,
            operatorValueValidationLabel;
    private List<Constraint> nameConstraints,
            operatorConstraints,
            operatorValueConstraints;
    private ActionEvent actionEvent = null;

    public UOMForm() {
        init();
    }

    public void init() {
        buildDialogContent();
        requiredValidator();
        setUomFormDialogOnActions();
    }

    private VBox buildName() {
        // Input.
        name = new ValidatableTextField();
        var label = new Label("Name");
        name.setPrefWidth(400d);
        name.textProperty().bindBidirectional(UOMViewModel.nameProperty());
        // Validation.
        nameValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, name, nameValidationLabel);
        return vbox;
    }

    private VBox buildShortName() {
        // Input.
        shortName = new ValidatableTextField();
        var label = new Label("Short Name");
        shortName.setPrefWidth(400d);
        shortName.textProperty().bindBidirectional(UOMViewModel.shortNameProperty());
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, shortName);
        return vbox;
    }

    private VBox buildBaseUnit() {
        // Input.
        baseUnit = new ValidatableComboBox<>();
        var label = new Label("Base Unit");
        baseUnit.setPrefWidth(400d);
        baseUnit.valueProperty().bindBidirectional(UOMViewModel.baseUnitProperty());
        // Input listeners.
        baseUnit
                .valueProperty()
                .addListener(
                        observable -> {
                            if (baseUnit.getValue() != null) {
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
        baseUnit.setConverter(uomConverter);
        if (UOMViewModel.getUnitsOfMeasure().isEmpty()) {
            UOMViewModel.getUnitsOfMeasure()
                    .addListener(
                            (ListChangeListener<UnitOfMeasure>)
                                    c -> baseUnit.setItems(UOMViewModel.getUnitsOfMeasure()));
        } else {
            baseUnit.itemsProperty().bindBidirectional(UOMViewModel.unitsOfMeasureProperty());
        }
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, baseUnit);
        return vbox;
    }

    private VBox buildOperator() {
        // Input.
        operator = new ValidatableComboBox<>();
        var label = new Label("Operator");
        operator.setPrefWidth(400d);
        operator.valueProperty().bindBidirectional(UOMViewModel.operatorProperty());
        operator.setItems(UOMViewModel.operatorList);
        // Validation.
        operatorValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, operator, operatorValidationLabel);
        return vbox;
    }

    private VBox buildOperatorValue() {
        // Input.
        operatorValue = new ValidatableTextField();
        var label = new Label("Operator Value");
        operatorValue.setPrefWidth(400d);
        operatorValue.textProperty().bindBidirectional(UOMViewModel.operatorValueProperty());
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 0d, 0d));
        vbox.getChildren().addAll(label, operatorValue);
        return vbox;
    }

    private VBox buildFormsHolder() {
        formsHolder = new VBox();
        formsHolder.setVisible(false);
        formsHolder.setManaged(false);
        formsHolder.setSpacing(10d);
        formsHolder.getChildren().addAll(buildOperator(), buildOperatorValue());
        return formsHolder;
    }

    private VBox buildCenter() {
        var vbox = new VBox();
        vbox.setSpacing(8d);
        vbox.setPadding(new Insets(10d));
        vbox.getChildren().addAll(buildName(), buildShortName(), buildBaseUnit(), buildFormsHolder());
        return vbox;
    }

    private Button buildSaveButton() {
        saveBtn = new Button("Save");
        saveBtn.setDefaultButton(true);
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

    private void setUomFormDialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);
                    UOMViewModel.resetUOMProperties();
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
                    this.dispose();
                });
        saveBtn.setOnAction(
                (event) -> {
                    nameConstraints = name.validate();
                    if (Objects.nonNull(baseUnit.getValue())) {
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
                    if (Objects.nonNull(baseUnit.getValue())) {
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
                        if (Objects.nonNull(baseUnit.getValue())) {
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
        formsHolder.setManaged(false);
        formsHolder.setVisible(false);
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
                        .setCondition(operator.valueProperty().isNotNull())
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
        this.dispose();
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

    @Override
    public void dispose() {
        super.dispose();
        this.name = null;
        this.shortName = null;
        this.baseUnit = null;
        this.operator = null;
        this.operatorValue = null;
        this.saveBtn = null;
        this.cancelBtn = null;
        this.formsHolder = null;
        this.nameValidationLabel = null;
        this.operatorValidationLabel = null;
        this.operatorValueValidationLabel = null;
        this.nameConstraints = null;
        this.operatorConstraints = null;
        this.operatorValueConstraints = null;
        this.actionEvent = null;
    }
}
