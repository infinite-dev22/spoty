package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import static inc.nomard.spoty.core.values.SharedResources.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.purchases.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.utils.*;
import io.github.palexdev.materialfx.utils.others.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.net.*;
import java.util.*;
import java.util.function.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class PurchaseDetailFormController implements Initializable {
    @FXML
    public MFXTextField quantity;
    @FXML
    public MFXFilterComboBox<Product> product;
    @FXML
    public MFXButton saveBtn, cancelBtn;
    @FXML
    public Label quantityValidationLabel, productValidationLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindProperties();
        setupProductComboBox();
        addValidators();
        setupDialogActions();
    }

    private void bindProperties() {
        quantity.textProperty().bindBidirectional(PurchaseDetailViewModel.quantityProperty());
        product.valueProperty().bindBidirectional(PurchaseDetailViewModel.productProperty());
    }

    private void setupProductComboBox() {
        StringConverter<Product> productConverter = FunctionalStringConverter.to(
                productDetail -> (productDetail == null) ? "" : productDetail.getName());

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

    private void addValidators() {
        addProductValidator();
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

    private void setupDialogActions() {
        cancelBtn.setOnAction(this::resetForm);
        saveBtn.setOnAction(this::saveForm);
    }

    private void resetForm(ActionEvent event) {
        closeDialog(event);
        PurchaseDetailViewModel.resetProperties();
        product.clearSelection();
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
        product.clearSelection();
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
