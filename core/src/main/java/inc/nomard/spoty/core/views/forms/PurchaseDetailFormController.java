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

import inc.nomard.spoty.core.components.message.SpotyMessage;
import inc.nomard.spoty.core.components.message.SpotyMessageHolder;
import inc.nomard.spoty.core.components.message.enums.MessageDuration;
import inc.nomard.spoty.core.components.message.enums.MessageVariants;
import inc.nomard.spoty.core.viewModels.ProductViewModel;
import inc.nomard.spoty.core.viewModels.purchases.PurchaseDetailViewModel;
import inc.nomard.spoty.network_bridge.dtos.Product;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import lombok.extern.java.Log;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

import static inc.nomard.spoty.core.GlobalActions.closeDialog;
import static inc.nomard.spoty.core.values.SharedResources.tempIdProperty;
import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

@Log
public class PurchaseDetailFormController implements Initializable {
    private static volatile PurchaseDetailFormController instance;
    @FXML
    public MFXTextField quantity;
    @FXML
    public MFXFilterComboBox<Product> product;
    @FXML
    public MFXButton saveBtn, cancelBtn;
    @FXML
    public Label quantityValidationLabel, productValidationLabel;

    public static PurchaseDetailFormController getInstance() {
        if (instance == null) {
            synchronized (PurchaseDetailFormController.class) {
                if (instance == null) {
                    instance = new PurchaseDetailFormController();
                }
            }
        }
        return instance;
    }

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
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        String message;

        if (tempIdProperty().get() > -1) {
            PurchaseDetailViewModel.updatePurchaseDetail();
            message = "Product changed successfully";
        } else {
            PurchaseDetailViewModel.addPurchaseDetail();
            message = "Product added successfully";
        }

        SpotyMessage notification = new SpotyMessage.MessageBuilder(message)
                .duration(MessageDuration.SHORT)
                .icon("fas-circle-check")
                .type(MessageVariants.SUCCESS)
                .build();
        notificationHolder.addMessage(notification);

        product.clearSelection();
        PurchaseDetailViewModel.resetProperties();
        closeDialog(event);
    }
}
