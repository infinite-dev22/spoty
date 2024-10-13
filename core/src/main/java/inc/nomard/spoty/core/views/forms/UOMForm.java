package inc.nomard.spoty.core.views.forms;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.viewModels.UOMViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.validatables.ValidatableComboBox;
import inc.nomard.spoty.core.views.components.validatables.ValidatableNumberField;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextField;
import inc.nomard.spoty.core.views.util.FunctionalStringConverter;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.core.views.util.Validators;
import inc.nomard.spoty.network_bridge.dtos.UnitOfMeasure;
import inc.nomard.spoty.core.util.validation.Constraint;
import inc.nomard.spoty.core.util.validation.Severity;
import javafx.collections.ListChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

@Log4j2
public class UOMForm extends BorderPane {
    private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");
    private final ModalPane modalPane;
    public ValidatableTextField name,
            shortName;
    public ValidatableNumberField operatorValue;
    public CustomButton saveBtn;
    public Button cancelBtn;
    public ValidatableComboBox<UnitOfMeasure> baseUnit;
    public ValidatableComboBox<String> operator;
    public VBox formsHolder;
    public Label nameValidationLabel,
            operatorValidationLabel,
            operatorValueValidationLabel;
    private List<Constraint> nameConstraints,
            operatorConstraints,
            operatorValueConstraints;

    public UOMForm(ModalPane modalPane) {
        this.modalPane = modalPane;
        buildDialogContent();
        requiredValidator();
        setUomFormDialogOnActions();
    }

    private VBox buildName() {
        // Input.
        name = new ValidatableTextField();
        var label = new Label("Name");
        name.setPrefWidth(1000d);
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
        shortName.setPrefWidth(1000d);
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
        baseUnit.setPrefWidth(1000d);
        baseUnit.valueProperty().bindBidirectional(UOMViewModel.baseUnitProperty());
        // Input listeners.
        baseUnit
                .valueProperty()
                .addListener(
                        observable -> {
                            if (baseUnit.getValue() != null) {
                                formsHolder.setVisible(true);
                                formsHolder.setManaged(true);
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
                                uomConverter.toString(unitOfMeasure).toLowerCase().contains(searchStr);

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
        operator.setPrefWidth(1000d);
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
        operatorValue = new ValidatableNumberField();
        var label = new Label("Operator Value");
        operatorValue.setPrefWidth(1000d);
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
    }

    private void setUomFormDialogOnActions() {
        cancelBtn.setOnAction((event) -> this.dispose());
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
                    }
                    if (Objects.nonNull(baseUnit.getValue())) {
                        if (!operatorConstraints.isEmpty()) {
                            operatorValidationLabel.setManaged(true);
                            operatorValidationLabel.setVisible(true);
                            operatorValidationLabel.setText(operatorConstraints.getFirst().getMessage());
                            operator.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        }
                        if (!operatorValueConstraints.isEmpty()) {
                            operatorValueValidationLabel.setManaged(true);
                            operatorValueValidationLabel.setVisible(true);
                            operatorValueValidationLabel.setText(operatorValueConstraints.getFirst().getMessage());
                            operatorValue.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        }
                    }
                    if (nameConstraints.isEmpty()) {
                        if (Objects.nonNull(baseUnit.getValue())) {
                            if (!operatorConstraints.isEmpty()
                                    && !operatorValueConstraints.isEmpty()) {
                                return;
                            }
                        }
                        saveBtn.startLoading();
                        if (UOMViewModel.getId() > 0) {
                            UOMViewModel.updateItem(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
                        } else {
                            UOMViewModel.saveUOM(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
                        }
                    }
                });
    }

    private void onSuccess() {
        this.dispose();
        UOMViewModel.getAllUOMs(null, null, null, null);
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

    private void errorMessage(String message) {
        SpotyUtils.errorMessage(message);
        saveBtn.stopLoading();
    }

    public void dispose() {
        modalPane.hide(true);
        modalPane.setPersistent(false);
        UOMViewModel.resetUOMProperties();
        name = null;
        shortName = null;
        baseUnit = null;
        operator = null;
        operatorValue = null;
        saveBtn = null;
        cancelBtn = null;
        formsHolder = null;
        nameValidationLabel = null;
        operatorValidationLabel = null;
        operatorValueValidationLabel = null;
        nameConstraints = null;
        operatorConstraints = null;
        operatorValueConstraints = null;
    }
}
