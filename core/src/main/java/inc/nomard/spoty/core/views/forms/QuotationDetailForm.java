package inc.nomard.spoty.core.views.forms;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.GlobalActions;
import inc.nomard.spoty.core.viewModels.DiscountViewModel;
import inc.nomard.spoty.core.viewModels.ProductViewModel;
import inc.nomard.spoty.core.viewModels.TaxViewModel;
import inc.nomard.spoty.core.viewModels.quotations.QuotationDetailViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.validatables.ValidatableComboBox;
import inc.nomard.spoty.core.views.components.validatables.ValidatableNumberField;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.message.SpotyMessage;
import inc.nomard.spoty.core.views.layout.message.enums.MessageDuration;
import inc.nomard.spoty.core.views.layout.message.enums.MessageVariants;
import inc.nomard.spoty.core.views.util.Validators;
import inc.nomard.spoty.network_bridge.dtos.Discount;
import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.network_bridge.dtos.Tax;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.ListProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.StringConverter;
import lombok.extern.java.Log;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

@Log
public class QuotationDetailForm extends MFXGenericDialog {
    public ValidatableNumberField quantity;
    public ValidatableComboBox<Product> product;
    public CustomButton saveBtn;
    public Button cancelBtn;
    public Label productValidationLabel, quantityValidationLabel;
    public ValidatableComboBox<Discount> discount;
    public ValidatableComboBox<Tax> tax;

    public QuotationDetailForm() {
        init();
    }

    public void init() {
        buildDialogContent();
        setupComboBoxes();
        setupValidators();
        setupActions();
    }


    private VBox buildProduct() {
        // Input.
        product = new ValidatableComboBox<>();
        var label = new Label("Product");
        product.setPrefWidth(400d);
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
        quantity.setPrefWidth(400d);
        quantity.textProperty().bindBidirectional(QuotationDetailViewModel.quantityProperty());
        // Validation.
        quantityValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, quantity, quantityValidationLabel);
        return vbox;
    }

    private VBox buildTax() {
        // Input.
        tax = new ValidatableComboBox<>();
        var label = new Label("Tax");
        tax.setPrefWidth(400d);
        tax.valueProperty().bindBidirectional(QuotationDetailViewModel.taxProperty());
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, tax);
        return vbox;
    }

    private VBox buildDiscount() {
        // Input.
        discount = new ValidatableComboBox<>();
        var label = new Label("Discount");
        discount.setPrefWidth(400d);
        discount.valueProperty().bindBidirectional(QuotationDetailViewModel.discountProperty());
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, discount);
        return vbox;
    }

    private VBox buildCenter() {
        var vbox = new VBox();
        vbox.setSpacing(8d);
        vbox.setPadding(new Insets(10d));
        vbox.getChildren().addAll(buildProduct(), buildQuantity(), buildTax(), buildDiscount());
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

    private void setupComboBoxes() {
        product.setConverter(createStringConverter(Product::getName));
//        product.selectedItemProperty().addListener((obs, o, n) -> {
//            if (Objects.nonNull(n) && !Objects.equals(n, o)) {
//                var salePrice = n.getSalePrice();
//                if (Objects.nonNull(n.getTax())) {
//                    QuotationDetailViewModel.setTax(n.getTax());
//                    salePrice += salePrice * (n.getTax().getPercentage() / 100);
//                } else {
//                    QuotationDetailViewModel.setTax(null);
//                }
//                if (Objects.nonNull(n.getDiscount())) {
//                    QuotationDetailViewModel.setDiscount(n.getDiscount());
//                    salePrice += salePrice * (n.getDiscount().getPercentage() / 100);
//                } else {
//                    QuotationDetailViewModel.setDiscount(null);
//                }
//                QuotationDetailViewModel.setSubTotal(salePrice);
//            }
//        });
        bindComboBoxItems(product, ProductViewModel.getProducts(), ProductViewModel.productsProperty());

        discount.setConverter(createStringConverter(d -> d.getName() + "(" + d.getPercentage() + ")"));
        bindComboBoxItems(discount, DiscountViewModel.getDiscounts(), DiscountViewModel.discountsProperty());

        tax.setConverter(createStringConverter(t -> t.getName() + "(" + t.getPercentage() + ")"));
        bindComboBoxItems(tax, TaxViewModel.getTaxes(), TaxViewModel.taxesProperty());
    }

    private <T> StringConverter<T> createStringConverter(Function<T, String> toStringFunction) {
        return FunctionalStringConverter.to(obj -> (obj == null) ? "" : toStringFunction.apply(obj));
    }

    private <T> Function<String, Predicate<T>> createFilterFunction(Function<T, String> toStringFunction) {
        return searchStr -> obj -> StringUtils.containsIgnoreCase(toStringFunction.apply(obj), searchStr);
    }

    private <T> void bindComboBoxItems(ValidatableComboBox<T> comboBox, ObservableList<T> items, ListProperty<T> itemsProperty) {
        if (items.isEmpty()) {
            items.addListener((ListChangeListener<T>) c -> comboBox.setItems(items));
        } else {
            comboBox.itemsProperty().bindBidirectional(itemsProperty);
        }
    }

    private void setupValidators() {
        createAndAddConstraint(product, "Product is required", product.valueProperty().isNotNull());
        createAndAddConstraint(quantity, "Quantity is required", quantity.textProperty().length().greaterThan(0));

        product.getValidator().validProperty().addListener((obs, oldVal, newVal) -> updateValidationState(newVal, productValidationLabel, product));
        quantity.getValidator().validProperty().addListener((obs, oldVal, newVal) -> updateValidationState(newVal, quantityValidationLabel, quantity));
    }

    private void createAndAddConstraint(ValidatableNumberField control, String message, BooleanExpression condition) {
        Constraint constraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage(message)
                .setCondition(condition)
                .get();
        control.getValidator().constraint(constraint);
    }

    private <T> void createAndAddConstraint(ValidatableComboBox<T> control, String message, BooleanExpression condition) {
        Constraint constraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage(message)
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
        cancelBtn.setOnAction(this::resetForm);
        saveBtn.setOnAction(this::saveForm);
    }

    private void resetForm(Event event) {
        GlobalActions.closeDialog(event);
        QuotationDetailViewModel.resetProperties();
        hideValidationLabels();
    }

    private void hideValidationLabels() {
        productValidationLabel.setVisible(false);
        quantityValidationLabel.setVisible(false);
        productValidationLabel.setManaged(false);
        quantityValidationLabel.setManaged(false);
        product.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        quantity.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
    }

    private void saveForm(Event event) {

        List<Constraint> productConstraints = product.validate();
        List<Constraint> quantityConstraints = quantity.validate();

        if (validateConstraints(productConstraints, productValidationLabel, product)
                && validateConstraints(quantityConstraints, quantityValidationLabel, quantity)) {

            if (QuotationDetailViewModel.tempIdProperty().get() > -1) {
                QuotationDetailViewModel.updateQuotationDetail();
                successMessage("Product changed successfully");
            } else {
                QuotationDetailViewModel.addQuotationDetails();
                successMessage("Product added successfully");
            }

            resetForm(event);
        }
    }

    private boolean validateConstraints(List<Constraint> constraints, Label validationLabel, ValidatableNumberField control) {
        if (!constraints.isEmpty()) {
            validationLabel.setText(constraints.getFirst().getMessage());
            validationLabel.setManaged(true);
            validationLabel.setVisible(true);
            control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
            resizeDialog(control);
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
            resizeDialog(control);
            return false;
        }
        return true;
    }

    private <T> void resizeDialog(Control control) {
        MFXStageDialog dialog = (MFXStageDialog) control.getScene().getWindow();
        dialog.sizeToScene();
    }

    private void successMessage(String message) {
        displayNotification(message, MessageVariants.SUCCESS, FontAwesomeSolid.CHECK_CIRCLE);
    }

    private void displayNotification(String message, MessageVariants type, Ikon icon) {
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
