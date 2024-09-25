package inc.nomard.spoty.core.views.forms;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.viewModels.ProductViewModel;
import inc.nomard.spoty.core.viewModels.stock_ins.StockInDetailViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.validatables.ValidatableComboBox;
import inc.nomard.spoty.core.views.components.validatables.ValidatableNumberField;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextArea;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.core.views.util.Validators;
import inc.nomard.spoty.network_bridge.dtos.Product;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import lombok.extern.java.Log;

import java.util.List;

import static inc.nomard.spoty.core.values.SharedResources.tempIdProperty;
import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

@Log
public class StockInDetailForm extends BorderPane {
    private final ModalPane modalPane;
    public CustomButton saveBtn;
    public Button cancelBtn;
    private ValidatableNumberField quantity;
    private ValidatableComboBox<Product> product;
    private Label productValidationLabel, quantityValidationLabel;
    private List<Constraint> productConstraints, quantityConstraints;

    public StockInDetailForm(ModalPane modalPane) {
        this.modalPane = modalPane;
        buildDialogContent();
        setupDialogActions();
        requiredValidator();
    }

    private VBox buildProduct() {
        // Input.
        product = new ValidatableComboBox<>();
        var label = new Label("Product");
        product.setPrefWidth(1000d);
        product.valueProperty().bindBidirectional(StockInDetailViewModel.productProperty());

        StringConverter<Product> productConverter = FunctionalStringConverter.to(
                productDetail -> productDetail == null ? "" : productDetail.getName());
        product.setConverter(productConverter);

        if (ProductViewModel.getProducts().isEmpty()) {
            ProductViewModel.getProducts().addListener((ListChangeListener<Product>) c -> product.setItems(ProductViewModel.getProducts()));
        } else {
            product.itemsProperty().bindBidirectional(ProductViewModel.productsProperty());
        }
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
        quantity.textProperty().bindBidirectional(StockInDetailViewModel.quantityProperty());
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
        hbox.getChildren().addAll(buildSaveButton(), buildCancelButton());
        return hbox;
    }

    private void buildDialogContent() {
        this.setCenter(buildCenter());
        this.setBottom(buildBottom());
    }

    private void setupDialogActions() {
        cancelBtn.setOnAction(event -> this.dispose());
        saveBtn.setOnAction(this::handleSaveAction);
    }

    private void handleSaveAction(Event event) {
        validateInputs();
        if (productConstraints.isEmpty() && quantityConstraints.isEmpty()) {
            if (tempIdProperty().get() > -1) {
                StockInDetailViewModel.updateStockInDetail();
                SpotyUtils.successMessage("Product changed successfully");
            } else {
                StockInDetailViewModel.addStockInDetails();
                SpotyUtils.successMessage("Product added successfully");
            }
            this.dispose();
        }
    }

    private void validateInputs() {
        productConstraints = product.validate();
        quantityConstraints = quantity.validate();

        if (!productConstraints.isEmpty()) {
            showValidationLabel(productValidationLabel, productConstraints.getFirst().getMessage(), product);
        }

        if (!quantityConstraints.isEmpty()) {
            showValidationLabel(quantityValidationLabel, quantityConstraints.getFirst().getMessage(), quantity);
        }
    }

    private void showValidationLabel(Label label, String message, ValidatableNumberField control) {
        label.setManaged(true);
        label.setVisible(true);
        label.setText(message);
        control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        MFXStageDialog dialog = (MFXStageDialog) control.getScene().getWindow();
        dialog.sizeToScene();
    }

    private <T> void showValidationLabel(Label label, String message, ValidatableComboBox<T> control) {
        label.setManaged(true);
        label.setVisible(true);
        label.setText(message);
        control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        MFXStageDialog dialog = (MFXStageDialog) control.getScene().getWindow();
        dialog.sizeToScene();
    }

    public void requiredValidator() {
        setupValidator(product);
        setupValidator(quantity);
    }

    private void setupValidator(ValidatableNumberField control) {
        Constraint constraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Quantity is required")
                .setCondition(control.textProperty().length().greaterThan(0))
                .get();
        control.getValidator().constraint(constraint);

        control.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                hideValidationLabel(control);
            }
        });
    }

    private <T> void setupValidator(ValidatableComboBox<T> control) {
        Constraint constraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Product is required")
                .setCondition(control.valueProperty().isNotNull())
                .get();
        control.getValidator().constraint(constraint);

        control.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                hideValidationLabel(control);
            }
        });
    }

    private void hideValidationLabel(Node control) {
        if (control.equals(product)) {
            productValidationLabel.setManaged(false);
            productValidationLabel.setVisible(false);
        } else if (control.equals(quantity)) {
            quantityValidationLabel.setManaged(false);
            quantityValidationLabel.setVisible(false);
        }
        control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
    }

    public void dispose() {
        modalPane.hide(true);
        modalPane.setPersistent(false);
        StockInDetailViewModel.resetProperties();
        this.product = null;
        this.quantity = null;
        this.cancelBtn = null;
        this.saveBtn = null;
        this.quantityValidationLabel = null;
        this.productValidationLabel = null;
        this.productConstraints = null;
        this.quantityConstraints = null;
    }
}
