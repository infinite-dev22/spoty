package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.values.*;
import static inc.nomard.spoty.core.values.SharedResources.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.adjustments.*;
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
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class AdjustmentDetailForm extends MFXGenericDialog {
    @FXML
    public MFXTextField quantity;
    @FXML
    public MFXFilterComboBox<Product> product;
    @FXML
    public MFXButton saveBtn, cancelBtn;
    @FXML
    public MFXComboBox<String> type;
    @FXML
    public Label productValidationLabel, quantityValidationLabel, typeValidationLabel;

    public AdjustmentDetailForm() {
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


    private VBox buildName() {
        // Input.
        product = new MFXFilterComboBox<>();
        product.setFloatMode(FloatMode.BORDER);
        product.setFloatingText("Product");
        product.setPrefWidth(400d);
        product.valueProperty().bindBidirectional(AdjustmentDetailViewModel.productProperty());
        setupProductComboBox();
        // Validation.
        productValidationLabel = buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(product, productValidationLabel);
        return vbox;
    }

    private VBox buildNumber() {
        // Input.
        quantity = new MFXTextField();
        quantity.setFloatMode(FloatMode.BORDER);
        quantity.setFloatingText("Quantity");
        quantity.setPrefWidth(400d);
        quantity.textProperty().bindBidirectional(AdjustmentDetailViewModel.quantityProperty());
        // Validation.
        quantityValidationLabel = buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(quantity, quantityValidationLabel);
        return vbox;
    }

    private VBox buildBalance() {
        // Input.
        type = new MFXComboBox<>();
        type.setFloatMode(FloatMode.BORDER);
        type.setFloatingText("Adjustment Type");
        type.setPrefWidth(400d);
        type.valueProperty().bindBidirectional(AdjustmentDetailViewModel.adjustmentTypeProperty());
        setupTypeComboBox();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(type);
        return vbox;
    }

    private VBox buildCenter() {
        var vbox = new VBox();
        vbox.setSpacing(8d);
        vbox.setPadding(new Insets(10d));
        vbox.getChildren().addAll(buildName(), buildNumber(), buildBalance());
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

    private void setupProductComboBox() {
        StringConverter<Product> productConverter = FunctionalStringConverter.to(
                productDetail -> (productDetail == null) ? "" : productDetail.getName() + " - (" + productDetail.getQuantity() + " Pcs)");

        Function<String, Predicate<Product>> productFilterFunction = searchStr ->
                productDetail -> StringUtils.containsIgnoreCase(
                        productConverter.toString(productDetail), searchStr);

        product.setConverter(productConverter);
        product.setFilterFunction(productFilterFunction);

        ProductViewModel.getProducts().addListener((ListChangeListener<Product>) c ->
                product.setItems(ProductViewModel.getProducts())
        );
        if (!ProductViewModel.getProducts().isEmpty()) {
            product.itemsProperty().bindBidirectional(ProductViewModel.productsProperty());
        }
    }

    private void setupTypeComboBox() {
        type.setItems(FXCollections.observableArrayList(Values.ADJUSTMENT_TYPE));
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
                .setCondition(product.textProperty().length().greaterThan(0))
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
                .setCondition(type.textProperty().length().greaterThan(0))
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
        cancelBtn.setOnAction(this::resetForm);
        saveBtn.setOnAction(this::saveForm);
    }

    private void resetForm(ActionEvent event) {
        closeDialog(event);
        AdjustmentDetailViewModel.resetProperties();
        product.clearSelection();
        type.clearSelection();
        hideValidationLabels();
    }

    private void hideValidationLabels() {
        productValidationLabel.setVisible(false);
        productValidationLabel.setManaged(false);
        quantityValidationLabel.setVisible(false);
        quantityValidationLabel.setManaged(false);
        typeValidationLabel.setVisible(false);
        typeValidationLabel.setManaged(false);
    }

    private void saveForm(ActionEvent event) {
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
            AdjustmentDetailViewModel.updateAdjustmentDetail((long) SharedResources.getTempId());
            message = "Entry updated successfully";
        } else {
            AdjustmentDetailViewModel.addAdjustmentDetails();
            message = "Entry added successfully";
        }
        displayNotification(message, MessageVariants.SUCCESS, "fas-circle-check");

        product.clearSelection();
        type.clearSelection();
        AdjustmentDetailViewModel.resetProperties();
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
