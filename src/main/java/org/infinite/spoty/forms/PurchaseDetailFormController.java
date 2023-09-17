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

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.Validators.requiredValidator;
import static org.infinite.spoty.values.SharedResources.tempIdProperty;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;

import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import org.infinite.spoty.GlobalActions;
import org.infinite.spoty.components.notification.SimpleNotification;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.components.notification.enums.NotificationDuration;
import org.infinite.spoty.components.notification.enums.NotificationVariants;
import org.infinite.spoty.database.models.Product;
import org.infinite.spoty.viewModels.ProductViewModel;
import org.infinite.spoty.viewModels.PurchaseDetailViewModel;

public class PurchaseDetailFormController implements Initializable {
    private static PurchaseDetailFormController instance;
    @FXML
    public MFXTextField purchaseDetailQnty;
    @FXML
    public MFXFilterComboBox<Product> purchaseDetailPdct;
    @FXML
    public MFXTextField purchaseDetailCost;
    @FXML
    public MFXButton purchaseDetailSaveBtn;
    @FXML
    public MFXButton purchaseDetailCancelBtn;
    @FXML
    public Label purchaseDetailQntyValidationLabel;
    @FXML
    public Label purchaseDetailPdctValidationLabel;
    @FXML
    public Label purchaseDetailCostValidationLabel;

    public static PurchaseDetailFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new PurchaseDetailFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input bindings.
        purchaseDetailQnty.textProperty().bindBidirectional(PurchaseDetailViewModel.quantityProperty());
        purchaseDetailPdct.valueProperty().bindBidirectional(PurchaseDetailViewModel.productProperty());
        purchaseDetailCost.textProperty().bindBidirectional(PurchaseDetailViewModel.costProperty());

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
        purchaseDetailPdct.setItems(ProductViewModel.getProducts());
        purchaseDetailPdct.setConverter(productVariantConverter);
        purchaseDetailPdct.setFilterFunction(productVariantFilterFunction);

        // Input validators.
        requiredValidator(
                purchaseDetailPdct,
                "Product is required.",
                purchaseDetailPdctValidationLabel,
                purchaseDetailSaveBtn);
        requiredValidator(
                purchaseDetailQnty,
                "Quantity is required.",
                purchaseDetailQntyValidationLabel,
                purchaseDetailSaveBtn);
        requiredValidator(
                purchaseDetailCost,
                "Cost is required.",
                purchaseDetailCostValidationLabel,
                purchaseDetailSaveBtn);
        dialogOnActions();
    }

    private void dialogOnActions() {
        purchaseDetailCancelBtn.setOnAction(
                (e) -> {
                    closeDialog(e);
                    PurchaseDetailViewModel.resetProperties();
                    purchaseDetailPdct.clearSelection();
                    purchaseDetailPdctValidationLabel.setVisible(false);
                    purchaseDetailQntyValidationLabel.setVisible(false);
                    purchaseDetailCostValidationLabel.setVisible(false);
                });
        purchaseDetailSaveBtn.setOnAction(
                (e) -> {
                    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

                    if (!purchaseDetailPdctValidationLabel.isVisible()
                            && !purchaseDetailQntyValidationLabel.isVisible()
                            && !purchaseDetailCostValidationLabel.isVisible()) {
                        if (tempIdProperty().get() > -1) {
                            GlobalActions.spotyThreadPool().execute(() -> {
                                try {
                                    PurchaseDetailViewModel.updatePurchaseDetail(PurchaseDetailViewModel.getId());
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }
                            });

                            SimpleNotification notification =
                                    new SimpleNotification.NotificationBuilder("Product changed successfully")
                                            .duration(NotificationDuration.SHORT)
                                            .icon("fas-circle-check")
                                            .type(NotificationVariants.SUCCESS)
                                            .build();
                            notificationHolder.addNotification(notification);

                            purchaseDetailPdct.clearSelection();

                            closeDialog(e);
                            return;
                        }
                        PurchaseDetailViewModel.addPurchaseDetail();

                        SimpleNotification notification =
                                new SimpleNotification.NotificationBuilder("Product added successfully")
                                        .duration(NotificationDuration.SHORT)
                                        .icon("fas-circle-check")
                                        .type(NotificationVariants.SUCCESS)
                                        .build();
                        notificationHolder.addNotification(notification);

                        closeDialog(e);
                        purchaseDetailPdct.clearSelection();
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
