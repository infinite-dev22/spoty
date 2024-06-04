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

import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.values.*;
import static inc.nomard.spoty.core.values.SharedResources.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.adjustments.*;
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
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class AdjustmentDetailFormController implements Initializable {
    private static AdjustmentDetailFormController instance;
    @FXML
    public MFXTextField quantity;
    @FXML
    public MFXFilterComboBox<Product> product;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
    @FXML
    public MFXComboBox<String> type;
    @FXML
    public Label productValidationLabel,
            quantityValidationLabel,
            typeValidationLabel;
    private List<Constraint> productConstraints,
            quantityConstraints,
            typeConstraints;

    public static AdjustmentDetailFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new AdjustmentDetailFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind form input value to property value.
        product
                .valueProperty()
                .bindBidirectional(AdjustmentDetailViewModel.productProperty());
        quantity
                .textProperty()
                .bindBidirectional(AdjustmentDetailViewModel.quantityProperty());
        type
                .textProperty()
                .bindBidirectional(AdjustmentDetailViewModel.adjustmentTypeProperty());
        // Combo box Converter.
        StringConverter<Product> productVariantConverter =
                FunctionalStringConverter.to(
                        productDetail -> (productDetail == null) ? "" : productDetail.getName() + " - (" + productDetail.getQuantity() + " Pcs)");
        // Combo box Filter Function.
        Function<String, Predicate<Product>> productVariantFilterFunction =
                searchStr ->
                        productDetail ->
                                StringUtils.containsIgnoreCase(
                                        productVariantConverter.toString(productDetail), searchStr);
        // AdjustmentType combo box properties.
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
        type.setItems(FXCollections.observableArrayList(Values.ADJUSTMENT_TYPE));
        // Input validators.
        requiredValidator();
        dialogOnActions();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);

                    AdjustmentDetailViewModel.resetProperties();
                    product.clearSelection();
                    type.clearSelection();

                    productValidationLabel.setVisible(false);
                    quantityValidationLabel.setVisible(false);
                    typeValidationLabel.setVisible(false);
                    // Manage
                    productValidationLabel.setManaged(false);
                    quantityValidationLabel.setManaged(false);
                    typeValidationLabel.setManaged(false);

                    productValidationLabel.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    quantityValidationLabel.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    typeValidationLabel.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();

                    productConstraints = product.validate();
                    quantityConstraints = quantity.validate();
                    typeConstraints = type.validate();
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
                    if (!typeConstraints.isEmpty()) {
                        typeValidationLabel.setManaged(true);
                        typeValidationLabel.setVisible(true);
                        typeValidationLabel.setText(typeConstraints.getFirst().getMessage());
                        type.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) type.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (productConstraints.isEmpty()
                            && quantityConstraints.isEmpty()
                            && typeConstraints.isEmpty()) {
                        if (tempIdProperty().get() > -1) {
                            AdjustmentDetailViewModel.updateAdjustmentDetail((long) SharedResources.getTempId());
                            SpotyMessage notification =
                                    new SpotyMessage.MessageBuilder("Entry updated successfully")
                                            .duration(MessageDuration.MEDIUM)
                                            .icon("fas-circle-check")
                                            .type(MessageVariants.SUCCESS)
                                            .build();
                            notificationHolder.addMessage(notification);
                            product.clearSelection();
                            type.clearSelection();
                            closeDialog(event);
                            return;
                        }
                        AdjustmentDetailViewModel.addAdjustmentDetails();
                        SpotyMessage notification =
                                new SpotyMessage.MessageBuilder("Entry added successfully")
                                        .duration(MessageDuration.SHORT)
                                        .icon("fas-circle-check")
                                        .type(MessageVariants.SUCCESS)
                                        .build();
                        notificationHolder.addMessage(notification);
                        AdjustmentDetailViewModel.resetProperties();
                        product.clearSelection();
                        type.clearSelection();
                        closeDialog(event);
                    }
                });
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint adjustmentProductVariantConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Product is required")
                        .setCondition(product.textProperty().length().greaterThan(0))
                        .get();
        product.getValidator().constraint(adjustmentProductVariantConstraint);
        Constraint adjustmentTypeConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Adjustment Type is required")
                        .setCondition(type.textProperty().length().greaterThan(0))
                        .get();
        type.getValidator().constraint(adjustmentTypeConstraint);
        Constraint adjustmentProductsQntyConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Quantity is required")
                        .setCondition(quantity.textProperty().length().greaterThan(0))
                        .get();
        quantity.getValidator().constraint(adjustmentProductsQntyConstraint);
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
        type
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                typeValidationLabel.setManaged(false);
                                typeValidationLabel.setVisible(false);
                                type.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
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
}
