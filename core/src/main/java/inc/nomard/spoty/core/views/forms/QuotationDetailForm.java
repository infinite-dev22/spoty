package inc.nomard.spoty.core.views.forms;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.util.validation.Constraint;
import inc.nomard.spoty.core.util.validation.Severity;
import inc.nomard.spoty.core.viewModels.ProductViewModel;
import inc.nomard.spoty.core.viewModels.quotations.QuotationDetailViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.validatables.ValidatableComboBox;
import inc.nomard.spoty.core.views.components.validatables.ValidatableNumberField;
import inc.nomard.spoty.core.views.util.FunctionalStringConverter;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.core.views.util.Validators;
import inc.nomard.spoty.network_bridge.dtos.Product;
import javafx.beans.binding.BooleanExpression;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import lombok.extern.java.Log;

import java.util.List;
import java.util.function.Function;

import static inc.nomard.spoty.core.util.validation.Validated.INVALID_PSEUDO_CLASS;

@Log
public class QuotationDetailForm extends BorderPane {
    private final ModalPane modalPane;
    public ValidatableNumberField quantity;
    public ValidatableComboBox<Product> product;
    public CustomButton saveBtn;
    public Button cancelBtn;
    public Label productValidationLabel, quantityValidationLabel;

    public QuotationDetailForm(ModalPane modalPane) {
        this.modalPane = modalPane;
        buildDialogContent();
        setupComboBoxes();
        setupValidators();
        setupActions();
    }


    private VBox buildProduct() {
        // Input.
        product = new ValidatableComboBox<>();
        var label = new Label("Product");
        product.setPrefWidth(1000d);
        product.valueProperty().bindBidirectional(QuotationDetailViewModel.productProperty());
        // Validation.
        productValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, product, productValidationLabel);
        return vbox;
    }

    private VBox buildQuantity() {
        // Input.
        quantity = new ValidatableNumberField();
        var label = new Label("Quantity");
        quantity.setPrefWidth(1000d);
        quantity.textProperty().bindBidirectional(QuotationDetailViewModel.quantityProperty());
        // Validation.
        quantityValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, quantity, quantityValidationLabel);
        return vbox;
    }

    private VBox buildCenter() {
        var vbox = new VBox();
        vbox.setSpacing(8d);
        vbox.setPadding(new Insets(10d));
        vbox.getChildren().addAll(buildProduct(), buildQuantity());
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
        hbox.setPadding(new Insets(10d));
        hbox.getChildren().addAll(buildSaveButton(), buildCancelButton());
        return hbox;
    }

    private void buildDialogContent() {
        this.setCenter(buildCenter());
        this.setBottom(buildBottom());
    }

    private void setupComboBoxes() {
        product.setConverter(createStringConverter(Product::getName));
        product.setItems(ProductViewModel.getProducts());
    }

    private <T> StringConverter<T> createStringConverter(Function<T, String> toStringFunction) {
        return FunctionalStringConverter.to(obj -> (obj == null) ? "" : toStringFunction.apply(obj));
    }

    private void setupValidators() {
        createAndAddConstraint(product, product.valueProperty().isNotNull());
        createAndAddConstraint(quantity, quantity.textProperty().length().greaterThan(0));

        product.getValidator().validProperty().addListener((obs, oldVal, newVal) -> updateValidationState(newVal, productValidationLabel, product));
        quantity.getValidator().validProperty().addListener((obs, oldVal, newVal) -> updateValidationState(newVal, quantityValidationLabel, quantity));
    }

    private void createAndAddConstraint(ValidatableNumberField control, BooleanExpression condition) {
        Constraint constraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Quantity is required")
                .setCondition(condition)
                .get();
        control.getValidator().constraint(constraint);
    }

    private <T> void createAndAddConstraint(ValidatableComboBox<T> control, BooleanExpression condition) {
        Constraint constraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Product is required")
                .setCondition(condition)
                .get();
        control.getValidator().constraint(constraint);
    }

    private void updateValidationState(boolean isValid, Label validationLabel, ValidatableNumberField control) {
        validationLabel.setManaged(!isValid);
        validationLabel.setVisible(!isValid);
        control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, !isValid);
    }

    private <T> void updateValidationState(boolean isValid, Label validationLabel, ValidatableComboBox<T> control) {
        validationLabel.setManaged(!isValid);
        validationLabel.setVisible(!isValid);
        control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, !isValid);
    }

    private void setupActions() {
        cancelBtn.setOnAction(event -> this.dispose());
        saveBtn.setOnAction(this::saveForm);
    }

    private void saveForm(Event event) {
        List<Constraint> productConstraints = product.validate();
        List<Constraint> quantityConstraints = quantity.validate();

        if (validateConstraints(productConstraints, productValidationLabel, product)
                && validateConstraints(quantityConstraints, quantityValidationLabel, quantity)) {
            String message;
            if (QuotationDetailViewModel.tempIdProperty().get() > -1) {
                QuotationDetailViewModel.updateQuotationDetail();
                message = "Product changed successfully";
            } else {
                QuotationDetailViewModel.addQuotationDetails();
                message = "Product added successfully";
            }
            SpotyUtils.successMessage(message);
            this.dispose();
        }
    }

    private boolean validateConstraints(List<Constraint> constraints, Label validationLabel, ValidatableNumberField control) {
        if (!constraints.isEmpty()) {
            validationLabel.setText(constraints.getFirst().getMessage());
            validationLabel.setManaged(true);
            validationLabel.setVisible(true);
            control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
            return false;
        }
        return true;
    }

    private <T> boolean validateConstraints(List<Constraint> constraints, Label validationLabel, ValidatableComboBox<T> control) {
        if (!constraints.isEmpty()) {
            validationLabel.setText(constraints.getFirst().getMessage());
            validationLabel.setManaged(true);
            validationLabel.setVisible(true);
            control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
            return false;
        }
        return true;
    }

    public void dispose() {
        modalPane.hide(true);
        modalPane.setPersistent(false);
        QuotationDetailViewModel.resetProperties();
        this.product = null;
        this.quantity = null;
        this.cancelBtn = null;
        this.saveBtn = null;
        this.quantityValidationLabel = null;
        this.productValidationLabel = null;
    }
}
