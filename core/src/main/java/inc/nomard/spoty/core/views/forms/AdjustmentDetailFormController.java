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

import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.SpotyMessageHolder;
import inc.nomard.spoty.core.components.message.enums.MessageDuration;
import inc.nomard.spoty.core.components.message.enums.MessageVariants;
import inc.nomard.spoty.core.values.SharedResources;
import inc.nomard.spoty.core.values.strings.Values;
import inc.nomard.spoty.core.viewModels.ProductViewModel;
import inc.nomard.spoty.core.viewModels.adjustments.AdjustmentDetailViewModel;
import inc.nomard.spoty.network_bridge.dtos.Product;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

import static inc.nomard.spoty.core.GlobalActions.closeDialog;
import static inc.nomard.spoty.core.Validators.requiredValidator;
import static inc.nomard.spoty.core.values.SharedResources.tempIdProperty;
import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

public class AdjustmentDetailFormController implements Initializable {
    private static AdjustmentDetailFormController instance;
    @FXML
    public MFXTextField adjustmentProductsQnty;
    @FXML
    public MFXFilterComboBox<Product> adjustmentProductVariant;
    @FXML
    public MFXButton adjustmentProductsSaveBtn,
            adjustmentProductsCancelBtn;
    @FXML
    public MFXComboBox<String> adjustmentType;
    @FXML
    public Label adjustmentProductVariantValidationLabel,
            adjustmentProductsQntyValidationLabel,
            adjustmentTypeValidationLabel;
    private List<Constraint> adjustmentProductVariantConstraints,
            adjustmentProductsQntyConstraints,
            adjustmentTypeConstraints;

    public static AdjustmentDetailFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new AdjustmentDetailFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind form input value to property value.
        adjustmentProductVariant
                .valueProperty()
                .bindBidirectional(AdjustmentDetailViewModel.productProperty());
        adjustmentProductsQnty
                .textProperty()
                .bindBidirectional(AdjustmentDetailViewModel.quantityProperty());
        adjustmentType
                .textProperty()
                .bindBidirectional(AdjustmentDetailViewModel.adjustmentTypeProperty());

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

        // AdjustmentType combo box properties.
        adjustmentProductVariant.setItems(ProductViewModel.getProducts());
        adjustmentProductVariant.setConverter(productVariantConverter);
        adjustmentProductVariant.setFilterFunction(productVariantFilterFunction);

        adjustmentType.setItems(FXCollections.observableArrayList(Values.ADJUSTMENT_TYPE));

        // Input validators.
        requiredValidator();

        dialogOnActions();
    }

    private void dialogOnActions() {
        adjustmentProductsCancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);

                    AdjustmentDetailViewModel.resetProperties();
                    adjustmentProductVariant.clearSelection();
                    adjustmentType.clearSelection();

                    adjustmentProductVariantValidationLabel.setVisible(false);
                    adjustmentProductsQntyValidationLabel.setVisible(false);
                    adjustmentTypeValidationLabel.setVisible(false);
                    // Manage
                    adjustmentProductVariantValidationLabel.setManaged(false);
                    adjustmentProductsQntyValidationLabel.setManaged(false);
                    adjustmentTypeValidationLabel.setManaged(false);

                    adjustmentProductVariantValidationLabel.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    adjustmentProductsQntyValidationLabel.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    adjustmentTypeValidationLabel.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        adjustmentProductsSaveBtn.setOnAction(
                (event) -> {
                    SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();

                    adjustmentProductVariantConstraints = adjustmentProductVariant.validate();
                    adjustmentProductsQntyConstraints = adjustmentProductsQnty.validate();
                    adjustmentTypeConstraints = adjustmentType.validate();
                    if (!adjustmentProductVariantConstraints.isEmpty()) {
                        adjustmentProductVariantValidationLabel.setManaged(true);
                        adjustmentProductVariantValidationLabel.setVisible(true);
                        adjustmentProductVariantValidationLabel.setText(adjustmentProductVariantConstraints.getFirst().getMessage());
                        adjustmentProductVariant.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!adjustmentProductsQntyConstraints.isEmpty()) {
                        adjustmentProductsQntyValidationLabel.setManaged(true);
                        adjustmentProductsQntyValidationLabel.setVisible(true);
                        adjustmentProductsQntyValidationLabel.setText(adjustmentProductsQntyConstraints.getFirst().getMessage());
                        adjustmentProductsQnty.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!adjustmentTypeConstraints.isEmpty()) {
                        adjustmentTypeValidationLabel.setManaged(true);
                        adjustmentTypeValidationLabel.setVisible(true);
                        adjustmentTypeValidationLabel.setText(adjustmentTypeConstraints.getFirst().getMessage());
                        adjustmentType.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (adjustmentProductVariantConstraints.isEmpty()
                            && adjustmentProductsQntyConstraints.isEmpty()
                            && adjustmentTypeConstraints.isEmpty()) {
                        if (tempIdProperty().get() > -1) {
                            AdjustmentDetailViewModel.updateAdjustmentDetail(
                                    SharedResources.getTempId());

                            SpotyMessage notification =
                                    new SpotyMessage.MessageBuilder("Entry updated successfully")
                                            .duration(MessageDuration.MEDIUM)
                                            .icon("fas-circle-check")
                                            .type(MessageVariants.SUCCESS)
                                            .build();
                            notificationHolder.addMessage(notification);

                            adjustmentProductVariant.clearSelection();
                            adjustmentType.clearSelection();

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

                        adjustmentProductVariant.clearSelection();
                        adjustmentType.clearSelection();

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
                        .setCondition(adjustmentProductVariant.textProperty().length().greaterThan(0))
                        .get();
        adjustmentProductVariant.getValidator().constraint(adjustmentProductVariantConstraint);
        Constraint adjustmentTypeConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Adjustment Type is required")
                        .setCondition(adjustmentType.textProperty().length().greaterThan(0))
                        .get();
        adjustmentType.getValidator().constraint(adjustmentTypeConstraint);
        Constraint adjustmentProductsQntyConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Quantity is required")
                        .setCondition(adjustmentProductsQnty.textProperty().length().greaterThan(0))
                        .get();
        adjustmentProductsQnty.getValidator().constraint(adjustmentProductsQntyConstraint);
        // Display error.
        adjustmentProductVariant
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                adjustmentProductVariantValidationLabel.setManaged(false);
                                adjustmentProductVariantValidationLabel.setVisible(false);
                                adjustmentProductVariant.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        adjustmentType
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                adjustmentTypeValidationLabel.setManaged(false);
                                adjustmentTypeValidationLabel.setVisible(false);
                                adjustmentType.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        adjustmentProductsQnty
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                adjustmentProductsQntyValidationLabel.setManaged(false);
                                adjustmentProductsQntyValidationLabel.setVisible(false);
                                adjustmentProductsQnty.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }
}
