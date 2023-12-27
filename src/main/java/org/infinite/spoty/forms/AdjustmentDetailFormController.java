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

package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import org.infinite.spoty.components.notification.SimpleNotification;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.components.notification.enums.NotificationDuration;
import org.infinite.spoty.components.notification.enums.NotificationVariants;
import org.infinite.spoty.data_source.daos.Product;
import org.infinite.spoty.values.SharedResources;
import org.infinite.spoty.values.strings.Values;
import org.infinite.spoty.viewModels.adjustments.AdjustmentDetailViewModel;
import org.infinite.spoty.viewModels.ProductViewModel;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.Validators.requiredValidator;
import static org.infinite.spoty.values.SharedResources.tempIdProperty;

public class AdjustmentDetailFormController implements Initializable {
    private static AdjustmentDetailFormController instance;
    @FXML
    public MFXTextField adjustmentProductsQnty;
    @FXML
    public MFXFilterComboBox<Product> adjustmentProductVariant;
    @FXML
    public MFXButton adjustmentProductsSaveBtn;
    @FXML
    public MFXButton adjustmentProductsCancelBtn;
    @FXML
    public MFXComboBox<String> adjustmentType;
    @FXML
    public Label adjustmentProductVariantValidationLabel;
    @FXML
    public Label adjustmentProductsQntyValidationLabel;
    @FXML
    public Label adjustmentTypeValidationLabel;

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
        requiredValidator(
                adjustmentProductVariant,
                "Product is required.",
                adjustmentProductVariantValidationLabel,
                adjustmentProductsSaveBtn);
        requiredValidator(
                adjustmentProductsQnty,
                "Quantity is required.",
                adjustmentProductsQntyValidationLabel,
                adjustmentProductsSaveBtn);
        requiredValidator(
                adjustmentType,
                "Type is required.",
                adjustmentTypeValidationLabel,
                adjustmentProductsSaveBtn);

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
                });
        adjustmentProductsSaveBtn.setOnAction(
                (event) -> {
                    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

                    if (!adjustmentProductVariantValidationLabel.isVisible()
                            && !adjustmentProductsQntyValidationLabel.isVisible()
                            && !adjustmentTypeValidationLabel.isVisible()) {
                        if (tempIdProperty().get() > -1) {
                            AdjustmentDetailViewModel.updateAdjustmentDetail(
                                    SharedResources.getTempId());

                            SimpleNotification notification =
                                    new SimpleNotification.NotificationBuilder("Entry updated successfully")
                                            .duration(NotificationDuration.MEDIUM)
                                            .icon("fas-circle-check")
                                            .type(NotificationVariants.SUCCESS)
                                            .build();
                            notificationHolder.addNotification(notification);

                            adjustmentProductVariant.clearSelection();
                            adjustmentType.clearSelection();

                            closeDialog(event);
                            return;
                        }
                        AdjustmentDetailViewModel.addAdjustmentDetails();

                        SimpleNotification notification =
                                new SimpleNotification.NotificationBuilder("Entry added successfully")
                                        .duration(NotificationDuration.SHORT)
                                        .icon("fas-circle-check")
                                        .type(NotificationVariants.SUCCESS)
                                        .build();

                        notificationHolder.addNotification(notification);
                        AdjustmentDetailViewModel.resetProperties();

                        adjustmentProductVariant.clearSelection();
                        adjustmentType.clearSelection();

                        closeDialog(event);
                        return;
                    }
                    SimpleNotification notification =
                            new SimpleNotification.NotificationBuilder("Required fields missing")
                                    .duration(NotificationDuration.SHORT)
                                    .icon("fas-triangle-exclamation")
                                    .type(NotificationVariants.ERROR)
                                    .build();
                    notificationHolder.addNotification(notification);
                });
    }
}
