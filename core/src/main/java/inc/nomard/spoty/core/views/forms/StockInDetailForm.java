package inc.nomard.spoty.core.views.forms;

import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import static inc.nomard.spoty.core.values.SharedResources.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.stock_ins.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.components.validatables.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.utils.others.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import java.util.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class StockInDetailForm extends ModalPage {
    public CustomButton saveBtn;
    public Button cancelBtn;
    private ValidatableNumberField quantity;
    private ValidatableComboBox<Product> product;
    private Label productValidationLabel, quantityValidationLabel;
    private List<Constraint> productConstraints, quantityConstraints;

    public StockInDetailForm() {
        init();
    }

    public void init() {
        buildDialogContent();
        setupDialogActions();
        requiredValidator();
    }

    private VBox buildProduct() {
        // Input.
        product = new ValidatableComboBox<>();
        var label = new Label("Product");
        product.setPrefWidth(400d);
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
        quantity.setPrefWidth(400d);
        quantity.textProperty().bindBidirectional(StockInDetailViewModel.quantityProperty());
        // Validation.
        quantityValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, quantity, quantityValidationLabel);
        return vbox;
    }

    private VBox buildDescription() {
        // Input.
        var description = new ValidatableTextArea();
        var label = new Label("Description");
        description.setPrefWidth(400d);
        description.textProperty().bindBidirectional(StockInDetailViewModel.descriptionProperty());
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
        vbox.getChildren().addAll(buildProduct(), buildQuantity(), buildDescription());
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

    private void setupDialogActions() {
        cancelBtn.setOnAction(this::resetForm);
        saveBtn.setOnAction(this::handleSaveAction);
    }

    private void resetForm(Event event) {
        closeDialog(event);
        StockInDetailViewModel.resetProperties();
        clearSelections();
        hideValidationLabels();
        dispose();
    }

    private void clearSelections() {
        product.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        quantity.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
    }

    private void hideValidationLabels() {
        productValidationLabel.setVisible(false);
        quantityValidationLabel.setVisible(false);
        productValidationLabel.setManaged(false);
        quantityValidationLabel.setManaged(false);
    }

    private void handleSaveAction(Event event) {
        validateInputs();
        if (productConstraints.isEmpty() && quantityConstraints.isEmpty()) {
            if (tempIdProperty().get() > -1) {
                StockInDetailViewModel.updateStockInDetail();
                successMessage("Product changed successfully");
            } else {
                StockInDetailViewModel.addStockInDetails();
                successMessage("Product added successfully");
            }
            resetForm(event);
            closeDialog(event);
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
        setupValidator(product, "Product is required");
        setupValidator(quantity, "Quantity is required");
    }

    private void setupValidator(ValidatableNumberField control, String message) {
        Constraint constraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage(message)
                .setCondition(control.textProperty().length().greaterThan(0))
                .get();
        control.getValidator().constraint(constraint);

        control.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                hideValidationLabel(control);
            }
        });
    }

    private <T> void setupValidator(ValidatableComboBox<T> control, String message) {
        Constraint constraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage(message)
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

    private void successMessage(String message) {
        displayNotification(message, MessageVariants.SUCCESS, "fas-circle-check");
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
