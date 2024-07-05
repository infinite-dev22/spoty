package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import static inc.nomard.spoty.core.values.SharedResources.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.requisitions.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.utils.*;
import io.github.palexdev.materialfx.utils.others.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.util.*;
import java.util.function.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class RequisitionDetailForm extends ModalPage {
    @FXML
    public MFXTextField quantity;
    @FXML
    public MFXFilterComboBox<Product> product;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
    @FXML
    public Label quantityValidationLabel,
            productValidationLabel,
            costValidationLabel;
    private List<Constraint> productConstraints,
            quantityConstraints;

    public RequisitionDetailForm() {
        init();
    }

    public void init() {
        buildDialogContent();
        requiredValidator();
        dialogOnActions();
    }

    // Validation label.
    private Label buildValidationLabel() {
        var label = new Label();
        label.setManaged(false);
        label.setVisible(false);
        label.setWrapText(true);
        label.getStyleClass().add("input-validation-error");
        label.setId("validationLabel");
        return label;
    }

    private VBox buildProduct() {
        // Input.
        product = new MFXFilterComboBox<>();
        product.setFloatMode(FloatMode.BORDER);
        product.setFloatingText("Product");
        product.setPrefWidth(400d);
        product.valueProperty().bindBidirectional(RequisitionDetailViewModel.productProperty());
        // Combo box Converter.
        StringConverter<Product> productVariantConverter =
                FunctionalStringConverter.to(
                        productDetail -> (productDetail == null) ? "" : productDetail.getName());

        // Combo box Filter Function.
        Function<String, Predicate<Product>> productVariantFilterFunction =
                searchStr ->
                        productDetail ->
                                StringUtils.containsIgnoreCase(
                                        productVariantConverter.toString(productDetail), searchStr);

        // ComboBox properties.
        product.setConverter(productVariantConverter);
        product.setFilterFunction(productVariantFilterFunction);
        if (ProductViewModel.getProducts().isEmpty()) {
            ProductViewModel.getProducts()
                    .addListener(
                            (ListChangeListener<Product>)
                                    c -> product.setItems(ProductViewModel.getProducts()));
        } else {
            product.itemsProperty().bindBidirectional(ProductViewModel.productsProperty());
        }
        // Validation.
        productValidationLabel = buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(product, productValidationLabel);
        return vbox;
    }

    private VBox buildQuantity() {
        // Input.
        quantity = new MFXTextField();
        quantity.setFloatMode(FloatMode.BORDER);
        quantity.setFloatingText("Quantity");
        quantity.setPrefWidth(400d);
        quantity.textProperty().bindBidirectional(RequisitionDetailViewModel.quantityProperty());
        // Validation.
        quantityValidationLabel = buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(quantity, quantityValidationLabel);
        return vbox;
    }

    private VBox buildCenter() {
        var vbox = new VBox();
        vbox.setSpacing(8d);
        vbox.setPadding(new Insets(10d));
        vbox.getChildren().addAll(buildProduct(), buildQuantity());
        return vbox;
    }

    private MFXButton buildSaveButton() {
        saveBtn = new MFXButton("Save");
        saveBtn.getStyleClass().add("filled");
        return saveBtn;
    }

    private MFXButton buildCancelButton() {
        cancelBtn = new MFXButton("Cancel");
        cancelBtn.getStyleClass().add("outlined");
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

    @Override
    public void dispose() {
        super.dispose();
        this.product = null;
        this.quantity = null;
        this.cancelBtn = null;
        this.saveBtn = null;
        this.quantityValidationLabel = null;
        this.productValidationLabel = null;
        this.costValidationLabel = null;
        this.productConstraints = null;
        this.quantityConstraints = null;
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);
                    RequisitionDetailViewModel.resetProperties();
                    product.clearSelection();
                    productValidationLabel.setVisible(false);
                    quantityValidationLabel.setVisible(false);
                    costValidationLabel.setVisible(false);

                    productValidationLabel.setManaged(false);
                    quantityValidationLabel.setManaged(false);
                    costValidationLabel.setManaged(false);

                    product.clearSelection();

                    product.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    quantity.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    dispose();
                });
        saveBtn.setOnAction(
                (event) -> {
                    productConstraints = product.validate();
                    quantityConstraints = quantity.validate();
                    if (!productConstraints.isEmpty()) {
                        productValidationLabel.setManaged(true);
                        productValidationLabel.setVisible(true);
                        productValidationLabel.setText(productConstraints.getFirst().getMessage());
                        product.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) product.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!quantityConstraints.isEmpty()) {
                        quantityValidationLabel.setManaged(true);
                        quantityValidationLabel.setVisible(true);
                        quantityValidationLabel.setText(quantityConstraints.getFirst().getMessage());
                        quantity.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) quantity.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (productConstraints.isEmpty()
                            && quantityConstraints.isEmpty()) {
                        if (tempIdProperty().get() > -1) {
                            RequisitionDetailViewModel.updateRequisitionDetail(RequisitionDetailViewModel.getId());
                            successMessage("Product changed successfully");
                            product.clearSelection();
                            closeDialog(event);
                            return;
                        }
                        RequisitionDetailViewModel.addRequisitionDetail();
                        successMessage("Product added successfully");
                        product.clearSelection();
                        closeDialog(event);
                    }
                    dispose();
                });
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint productConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Product is required")
                        .setCondition(product.textProperty().length().greaterThan(0))
                        .get();
        product.getValidator().constraint(productConstraint);
        Constraint quantityConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Quantity is required")
                        .setCondition(quantity.textProperty().length().greaterThan(0))
                        .get();
        quantity.getValidator().constraint(quantityConstraint);
        // Display error.
        product
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                productValidationLabel.setManaged(false);
                                productValidationLabel.setVisible(false);
                                product.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        quantity
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                quantityValidationLabel.setManaged(false);
                                quantityValidationLabel.setVisible(false);
                                quantity.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
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
}
