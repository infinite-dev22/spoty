package inc.nomard.spoty.core.views.forms;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.util.validation.Constraint;
import inc.nomard.spoty.core.util.validation.Severity;
import inc.nomard.spoty.core.values.SharedResources;
import inc.nomard.spoty.core.values.strings.Values;
import inc.nomard.spoty.core.viewModels.ProductViewModel;
import inc.nomard.spoty.core.viewModels.adjustments.AdjustmentDetailViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.validatables.ValidatableComboBox;
import inc.nomard.spoty.core.views.components.validatables.ValidatableNumberField;
import inc.nomard.spoty.core.views.util.FunctionalStringConverter;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.core.views.util.Validators;
import inc.nomard.spoty.network_bridge.dtos.Product;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static inc.nomard.spoty.core.util.validation.Validated.INVALID_PSEUDO_CLASS;
import static inc.nomard.spoty.core.values.SharedResources.tempIdProperty;

@Slf4j
public class AdjustmentDetailForm extends BorderPane {
    private final ModalPane modalPane;
    public ValidatableNumberField quantity;
    public ValidatableComboBox<Product> product;
    public CustomButton saveBtn;
    public Button cancelBtn;
    public ValidatableComboBox<String> type;
    public Label productValidationLabel, quantityValidationLabel, typeValidationLabel;

    public AdjustmentDetailForm(ModalPane modalPane) {
        this.modalPane = modalPane;
        buildDialogContent();
        requiredValidator();
        dialogOnActions();
    }


    private VBox buildName() {
        // Input.
        var label = new Label("Product");
        product = new ValidatableComboBox<>();
        product.setPrefWidth(1000d);
        product.valueProperty().bindBidirectional(AdjustmentDetailViewModel.productProperty());
        setupProductComboBox();
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
        var label = new Label("Quantity");
        quantity = new ValidatableNumberField();
        quantity.setPrefWidth(1000d);
        quantity.textProperty().bindBidirectional(AdjustmentDetailViewModel.quantityProperty());
        // Validation.
        quantityValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, quantity, quantityValidationLabel);
        return vbox;
    }

    private VBox buildAdjustmentType() {
        // Input.
        var label = new Label("Adjustment Type");
        type = new ValidatableComboBox<>();
        type.setPrefWidth(1000d);
        type.valueProperty().bindBidirectional(AdjustmentDetailViewModel.adjustmentTypeProperty());
        typeValidationLabel = Validators.buildValidationLabel();
        setupTypeComboBox();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, type, typeValidationLabel);
        return vbox;
    }

    private VBox buildCenter() {
        var vbox = new VBox();
        vbox.setSpacing(8d);
        vbox.setPadding(new Insets(10d));
        vbox.getChildren().addAll(buildName(), buildQuantity(), buildAdjustmentType());
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

    private void setupProductComboBox() {
        StringConverter<Product> productConverter = FunctionalStringConverter.to(
                productDetail -> (productDetail == null) ? "" : productDetail.getName() + " - (" + productDetail.getQuantity() + " Pcs)");

        product.setConverter(productConverter);
        product.setItems(ProductViewModel.getProducts());
    }

    private void setupTypeComboBox() {
        type.setItems(Values.ADJUSTMENT_TYPE);
    }

    private void requiredValidator() {
        addProductValidator();
        addTypeValidator();
        addQuantityValidator();
    }

    private void addProductValidator() {
        Constraint productConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Product is required")
                .setCondition(product.valueProperty().isNotNull())
                .get();
        product.getValidator().constraint(productConstraint);

        product.getValidator().validProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue) {
                        productValidationLabel.setManaged(false);
                        productValidationLabel.setVisible(false);
                        product.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    }
                }
        );
    }

    private void addTypeValidator() {
        Constraint typeConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Adjustment Type is required")
                .setCondition(type.valueProperty().isNotNull())
                .get();
        type.getValidator().constraint(typeConstraint);

        type.getValidator().validProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue) {
                        typeValidationLabel.setManaged(false);
                        typeValidationLabel.setVisible(false);
                        type.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    }
                }
        );
    }

    private void addQuantityValidator() {
        Constraint quantityConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Quantity is required")
                .setCondition(quantity.textProperty().length().greaterThan(0))
                .get();
        quantity.getValidator().constraint(quantityConstraint);

        quantity.getValidator().validProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue) {
                        quantityValidationLabel.setManaged(false);
                        quantityValidationLabel.setVisible(false);
                        quantity.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    }
                }
        );
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(event -> this.dispose());
        saveBtn.setOnAction(event -> this.saveForm());
    }

    private void saveForm() {
        List<Constraint> productConstraints = product.validate();
        List<Constraint> quantityConstraints = quantity.validate();
        List<Constraint> typeConstraints = type.validate();

        if (!productConstraints.isEmpty()) {
            showValidationError(productValidationLabel, productConstraints.getFirst().getMessage(), product);
        }
        if (!quantityConstraints.isEmpty()) {
            showValidationError(quantityValidationLabel, quantityConstraints.getFirst().getMessage(), quantity);
        }
        if (!typeConstraints.isEmpty()) {
            showValidationError(typeValidationLabel, typeConstraints.getFirst().getMessage(), type);
        }

        if (productConstraints.isEmpty() && quantityConstraints.isEmpty() && typeConstraints.isEmpty()) {
            processSave();
        }
    }

    private void showValidationError(Label validationLabel, String message, Node control) {
        validationLabel.setManaged(true);
        validationLabel.setVisible(true);
        validationLabel.setText(message);
        control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
    }

    private void processSave() {
        String message;
        if (tempIdProperty().get() > -1) {
            AdjustmentDetailViewModel.updateAdjustmentDetail((long) SharedResources.getTempId());
            message = "Entry updated successfully";
        } else {
            AdjustmentDetailViewModel.addAdjustmentDetails();
            message = "Entry added successfully";
        }
        SpotyUtils.successMessage(message);
        dispose();
    }

    public void dispose() {
        modalPane.hide(true);
        modalPane.setPersistent(false);
        AdjustmentDetailViewModel.resetProperties();
        this.product = null;
        this.quantity = null;
        this.type = null;
        this.cancelBtn = null;
        this.saveBtn = null;
        this.quantityValidationLabel = null;
        this.productValidationLabel = null;
        this.typeValidationLabel = null;
    }
}
