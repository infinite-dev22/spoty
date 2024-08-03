package inc.nomard.spoty.core.views.forms;

import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import static inc.nomard.spoty.core.values.SharedResources.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.purchases.*;
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
public class PurchaseDetailForm extends MFXGenericDialog {
    public ValidatableNumberField quantity;
    public ValidatableComboBox<Product> product;
    public Button saveBtn, cancelBtn;
    public Label quantityValidationLabel, productValidationLabel;

    public PurchaseDetailForm() {
        init();
    }

    public void init() {
        buildDialogContent();
        requiredValidator();
        dialogOnActions();
    }


    private VBox buildProduct() {
        // Input.
        product = new ValidatableComboBox<>();
        var label = new Label("Product");
        product.setPrefWidth(400d);
        product.valueProperty().bindBidirectional(PurchaseDetailViewModel.productProperty());
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
        quantity = new ValidatableNumberField();
        var label = new Label("Quantity");
        quantity.setPrefWidth(400d);
        quantity.textProperty().bindBidirectional(PurchaseDetailViewModel.quantityProperty());
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

    private void setupProductComboBox() {
        StringConverter<Product> productConverter = FunctionalStringConverter.to(
                productDetail -> (productDetail == null) ? "" : productDetail.getName());

        product.setConverter(productConverter);

        ProductViewModel.getProducts().addListener((ListChangeListener<Product>) c ->
                product.setItems(ProductViewModel.getProducts())
        );
        if (!ProductViewModel.getProducts().isEmpty()) {
            product.itemsProperty().bindBidirectional(ProductViewModel.productsProperty());
        }
    }

    private void requiredValidator() {
        addProductValidator();
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
        cancelBtn.setOnAction(this::resetForm);
        saveBtn.setOnAction(this::saveForm);
    }

    private void resetForm(ActionEvent event) {
        closeDialog(event);
        PurchaseDetailViewModel.resetProperties();
        hideValidationLabels();
    }

    private void hideValidationLabels() {
        productValidationLabel.setVisible(false);
        productValidationLabel.setManaged(false);
        quantityValidationLabel.setVisible(false);
        quantityValidationLabel.setManaged(false);
    }

    private void saveForm(ActionEvent event) {
        List<Constraint> productConstraints = product.validate();
        List<Constraint> quantityConstraints = quantity.validate();

        if (!productConstraints.isEmpty()) {
            showValidationError(productValidationLabel, productConstraints.getFirst().getMessage(), product);
        }
        if (!quantityConstraints.isEmpty()) {
            showValidationError(quantityValidationLabel, quantityConstraints.getFirst().getMessage(), quantity);
        }

        if (productConstraints.isEmpty() && quantityConstraints.isEmpty()) {
            processSave(event);
        }
    }

    private void showValidationError(Label validationLabel, String message, Node control) {
        validationLabel.setManaged(true);
        validationLabel.setVisible(true);
        validationLabel.setText(message);
        control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        MFXStageDialog dialog = (MFXStageDialog) control.getScene().getWindow();
        dialog.sizeToScene();
    }

    private void processSave(ActionEvent event) {
        String message;

        if (tempIdProperty().get() > -1) {
            PurchaseDetailViewModel.updatePurchaseDetail();
            message = "Product changed successfully";
        } else {
            PurchaseDetailViewModel.addPurchaseDetail();
            message = "Product added successfully";
        }
        displayNotification(message, MessageVariants.SUCCESS, "fas-circle-check");
        PurchaseDetailViewModel.resetProperties();
        closeDialog(event);
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
