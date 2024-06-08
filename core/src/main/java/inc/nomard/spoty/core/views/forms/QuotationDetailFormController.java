/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import inc.nomard.spoty.core.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.quotations.*;
import inc.nomard.spoty.core.views.*;
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
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class QuotationDetailFormController implements Initializable {
    private static QuotationDetailFormController instance;
    private final Stage stage;
    @FXML
    public MFXTextField quantity;
    @FXML
    public MFXFilterComboBox<Product> product;
    @FXML
    public MFXButton saveBtn, cancelBtn;
    @FXML
    public Label productValidationLabel, quantityValidationLabel;
    @FXML
    public MFXComboBox<Discount> discount;
    @FXML
    public MFXComboBox<Tax> tax;

    public QuotationDetailFormController(Stage stage) {
        this.stage = stage;
    }

    public static synchronized QuotationDetailFormController getInstance(Stage stage) {
        if (instance == null) {
            synchronized (QuotationDetailFormController.class) {
                instance = new QuotationDetailFormController(stage);
            }
        }
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindFormInputs();
        setupComboBoxes();
        setupValidators();
        setupActions();
    }

    private void bindFormInputs() {
        quantity.textProperty().bindBidirectional(QuotationDetailViewModel.quantityProperty());
        product.valueProperty().bindBidirectional(QuotationDetailViewModel.productProperty());
        discount.valueProperty().bindBidirectional(QuotationDetailViewModel.discountProperty());
        tax.valueProperty().bindBidirectional(QuotationDetailViewModel.taxProperty());
    }

    private void setupComboBoxes() {
        product.setConverter(createStringConverter(Product::getName));
        product.setFilterFunction(createFilterFunction(Product::getName));
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

    private <T> void bindComboBoxItems(MFXComboBox<T> comboBox, ObservableList<T> items, ListProperty<T> itemsProperty) {
        if (items.isEmpty()) {
            items.addListener((ListChangeListener<T>) c -> comboBox.setItems(items));
        } else {
            comboBox.itemsProperty().bindBidirectional(itemsProperty);
        }
    }

    private void setupValidators() {
        createAndAddConstraint(product, "Product is required", product.textProperty().length().greaterThan(0));
        createAndAddConstraint(quantity, "Quantity is required", quantity.textProperty().length().greaterThan(0));

        product.getValidator().validProperty().addListener((obs, oldVal, newVal) -> updateValidationState(newVal, productValidationLabel, product));
        quantity.getValidator().validProperty().addListener((obs, oldVal, newVal) -> updateValidationState(newVal, quantityValidationLabel, quantity));
    }

    private void createAndAddConstraint(MFXTextField control, String message, BooleanExpression condition) {
        Constraint constraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage(message)
                .setCondition(condition)
                .get();
        control.getValidator().constraint(constraint);
    }

    private void updateValidationState(boolean isValid, Label validationLabel, MFXTextField control) {
        validationLabel.setManaged(!isValid);
        validationLabel.setVisible(!isValid);
        control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, !isValid);
    }

    private void setupActions() {
        cancelBtn.setOnAction(this::resetForm);
        saveBtn.setOnAction(this::saveForm);
    }

    private void resetForm(ActionEvent event) {
        GlobalActions.closeDialog(event);
        QuotationDetailViewModel.resetProperties();
        clearSelections();
        hideValidationLabels();
    }

    private void clearSelections() {
        product.clearSelection();
        tax.clearSelection();
        discount.clearSelection();
    }

    private void hideValidationLabels() {
        productValidationLabel.setVisible(false);
        quantityValidationLabel.setVisible(false);
        productValidationLabel.setManaged(false);
        quantityValidationLabel.setManaged(false);
        product.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        quantity.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
    }

    private void saveForm(ActionEvent event) {

        List<Constraint> productConstraints = product.validate();
        List<Constraint> quantityConstraints = quantity.validate();

        if (validateConstraints(productConstraints, productValidationLabel, product)
                && validateConstraints(quantityConstraints, quantityValidationLabel, quantity)) {

            if (QuotationDetailViewModel.tempIdProperty().get() > -1) {
                QuotationDetailViewModel.updateQuotationDetail(QuotationDetailViewModel.getId());
                successMessage("Product changed successfully");
            } else {
                QuotationDetailViewModel.addQuotationDetails();
                successMessage("Product added successfully");
            }

            resetForm(event);
        }
    }

    private boolean validateConstraints(List<Constraint> constraints, Label validationLabel, MFXTextField control) {
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

    private void resizeDialog(MFXTextField control) {
        MFXStageDialog dialog = (MFXStageDialog) control.getScene().getWindow();
        dialog.sizeToScene();
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
        if (!BaseController.getInstance(stage).morphPane.getChildren().contains(notification)) {
            BaseController.getInstance(stage).morphPane.getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> SpotyMessage.delay(notification, stage));
        }
    }
}
