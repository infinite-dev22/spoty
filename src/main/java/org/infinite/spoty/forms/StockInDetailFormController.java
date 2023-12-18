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

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import org.infinite.spoty.components.notification.SimpleNotification;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.components.notification.enums.NotificationDuration;
import org.infinite.spoty.components.notification.enums.NotificationVariants;
import org.infinite.spoty.data_source.dtos.Product;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.utils.SpotyThreader;
import org.infinite.spoty.viewModels.ProductViewModel;
import org.infinite.spoty.viewModels.StockInDetailViewModel;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.Validators.requiredValidator;
import static org.infinite.spoty.values.SharedResources.tempIdProperty;

public class StockInDetailFormController implements Initializable {
    private static StockInDetailFormController instance;
    @FXML
    public MFXTextField stockInDetailQnty;
    @FXML
    public MFXFilterComboBox<Product> stockInDetailPdct;
    @FXML
    public MFXButton stockInDetailSaveBtn;
    @FXML
    public MFXButton stockInDetailCancelBtn;
    @FXML
    public MFXTextField stockInDetailDescription;
    @FXML
    public Label stockInDetailPdctValidationLabel;
    @FXML
    public Label stockInDetailQntyValidationLabel;

    public static StockInDetailFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new StockInDetailFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form input binding.
        stockInDetailPdct.valueProperty().bindBidirectional(StockInDetailViewModel.productProperty());
        stockInDetailQnty.textProperty().bindBidirectional(StockInDetailViewModel.quantityProperty());
        stockInDetailDescription
                .textProperty()
                .bindBidirectional(StockInDetailViewModel.descriptionProperty());

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
        stockInDetailPdct.setItems(ProductViewModel.getProducts());
        stockInDetailPdct.setConverter(productVariantConverter);
        stockInDetailPdct.setFilterFunction(productVariantFilterFunction);

        // Input validators.
        requiredValidator(
                stockInDetailPdct,
                "Product is required.",
                stockInDetailPdctValidationLabel,
                stockInDetailSaveBtn);
        requiredValidator(
                stockInDetailQnty,
                "Quantity is required.",
                stockInDetailQntyValidationLabel,
                stockInDetailSaveBtn);

        dialogOnActions();
    }

    private void dialogOnActions() {
        stockInDetailCancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);
                    StockInDetailViewModel.resetProperties();
                    stockInDetailPdct.clearSelection();
                    stockInDetailPdctValidationLabel.setVisible(false);
                    stockInDetailQntyValidationLabel.setVisible(false);
                });
        stockInDetailSaveBtn.setOnAction(
                (event) -> {
                    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

                    if (!stockInDetailPdctValidationLabel.isVisible()
                            && !stockInDetailQntyValidationLabel.isVisible()) {
                        if (tempIdProperty().get() > -1) {
                            SpotyThreader.spotyThreadPool(
                                    () -> {
                                        try {
                                            StockInDetailViewModel.updateStockInDetail(
                                                    StockInDetailViewModel.getId());
                                        } catch (Exception e) {
                                            SpotyLogger.writeToFile(e, this.getClass());
                                        }
                                    });

                            SimpleNotification notification =
                                    new SimpleNotification.NotificationBuilder("Product changed successfully")
                                            .duration(NotificationDuration.SHORT)
                                            .icon("fas-circle-check")
                                            .type(NotificationVariants.SUCCESS)
                                            .build();
                            notificationHolder.addNotification(notification);

                            stockInDetailPdct.clearSelection();

                            closeDialog(event);
                            return;
                        }
                        StockInDetailViewModel.addStockInDetails();

                        SimpleNotification notification =
                                new SimpleNotification.NotificationBuilder("Product added successfully")
                                        .duration(NotificationDuration.SHORT)
                                        .icon("fas-circle-check")
                                        .type(NotificationVariants.SUCCESS)
                                        .build();
                        notificationHolder.addNotification(notification);

                        stockInDetailPdct.clearSelection();

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
