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
import inc.nomard.spoty.core.viewModels.transfers.TransferDetailViewModel;
import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
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
import static inc.nomard.spoty.core.values.SharedResources.tempIdProperty;
import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

public class TransferDetailFormController implements Initializable {
    private static TransferDetailFormController instance;
    @FXML
    public MFXTextField quantity,
            description;
    @FXML
    public MFXFilterComboBox<Product> product;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
    @FXML
    public Label quantityValidationLabel,
            productValidationLabel;
    private List<Constraint> productConstraints,
            quantityConstraints;

    public static TransferDetailFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new TransferDetailFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form input binding.
        product.valueProperty().bindBidirectional(TransferDetailViewModel.productProperty());
        quantity.textProperty().bindBidirectional(TransferDetailViewModel.quantityProperty());
        description
                .textProperty()
                .bindBidirectional(TransferDetailViewModel.descriptionProperty());

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

        // Combo box properties.
        product.setItems(ProductViewModel.getProducts());
        product.setConverter(productVariantConverter);
        product.setFilterFunction(productVariantFilterFunction);

        // Input validators.
        requiredValidator();

        dialogOnActions();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);
                    TransferDetailViewModel.resetProperties();
                    productValidationLabel.setVisible(false);
                    quantityValidationLabel.setVisible(false);

                    productValidationLabel.setManaged(false);
                    quantityValidationLabel.setManaged(false);

                    product.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    quantity.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();

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
                            SpotyThreader.spotyThreadPool(() -> {
                                try {
                                    TransferDetailViewModel.updateTransferDetail();
                                } catch (Exception e) {
                                    SpotyLogger.writeToFile(e, this.getClass());
                                }
                            });

                            SpotyMessage notification =
                                    new SpotyMessage.MessageBuilder("Product changed successfully")
                                            .duration(MessageDuration.SHORT)
                                            .icon("fas-circle-check")
                                            .type(MessageVariants.SUCCESS)
                                            .build();
                            notificationHolder.addMessage(notification);

                            product.clearSelection();

                            closeDialog(event);
                            return;
                        }
                        TransferDetailViewModel.addTransferDetails();

                        SpotyMessage notification =
                                new SpotyMessage.MessageBuilder("Product added successfully")
                                        .duration(MessageDuration.SHORT)
                                        .icon("fas-circle-check")
                                        .type(MessageVariants.SUCCESS)
                                        .build();
                        notificationHolder.addMessage(notification);

                        product.clearSelection();

                        closeDialog(event);
                    }
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
}
