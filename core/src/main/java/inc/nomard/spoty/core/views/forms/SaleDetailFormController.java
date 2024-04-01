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
import inc.nomard.spoty.core.components.message.enums.MessageDuration;
import inc.nomard.spoty.core.components.message.enums.MessageVariants;
import inc.nomard.spoty.core.viewModels.ProductViewModel;
import inc.nomard.spoty.core.viewModels.sales.SaleDetailViewModel;
import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

import static inc.nomard.spoty.core.GlobalActions.closeDialog;
import static inc.nomard.spoty.core.Validators.requiredValidator;
import static inc.nomard.spoty.core.values.SharedResources.getTempId;
import static inc.nomard.spoty.core.values.SharedResources.tempIdProperty;

public class SaleDetailFormController implements Initializable {
    private static SaleDetailFormController instance;
    @FXML
    public MFXTextField saleDetailQnty;
    @FXML
    public MFXFilterComboBox<Product> saleDetailPdct;
    @FXML
    public MFXTextField saleDetailOrderTax;
    @FXML
    public MFXTextField saleDetailDiscount;
    @FXML
    public MFXButton saleProductsSaveBtn;
    @FXML
    public MFXButton saleProductsCancelBtn;
    @FXML
    public Label saleDetailPdctValidationLabel;
    @FXML
    public Label saleDetailQntyValidationLabel;

    public static SaleDetailFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new SaleDetailFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Property binding.
        saleDetailQnty.textProperty().bindBidirectional(SaleDetailViewModel.quantityProperty());
        saleDetailPdct.valueProperty().bindBidirectional(SaleDetailViewModel.productProperty());
        saleDetailOrderTax.textProperty().bindBidirectional(SaleDetailViewModel.netTaxProperty());
        saleDetailDiscount.textProperty().bindBidirectional(SaleDetailViewModel.discountProperty());

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

        // Set combo box options.
        saleDetailPdct.setItems(ProductViewModel.getProducts());
        saleDetailPdct.setConverter(productVariantConverter);
        saleDetailPdct.setFilterFunction(productVariantFilterFunction);

        // Input validators.
        requiredValidator(
                saleDetailPdct, "Product is required.", saleDetailPdctValidationLabel, saleProductsSaveBtn);
        requiredValidator(
                saleDetailQnty,
                "Quantity is required.",
                saleDetailQntyValidationLabel,
                saleProductsSaveBtn);

        dialogOnActions();
    }

    private void dialogOnActions() {
        saleProductsCancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);
                    SaleDetailViewModel.resetProperties();
                    saleDetailPdct.clearSelection();
                    saleDetailPdctValidationLabel.setVisible(false);
                    saleDetailQntyValidationLabel.setVisible(false);
                });
        saleProductsSaveBtn.setOnAction(
                (event) -> {
                    SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();

                    if (!saleDetailPdctValidationLabel.isVisible()
                            && !saleDetailQntyValidationLabel.isVisible()) {
                        if (tempIdProperty().get() > -1) {
                            SpotyThreader.spotyThreadPool(
                                    () -> {
                                        try {
                                            SaleDetailViewModel.updateSaleDetail((long) getTempId());
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

                            saleDetailPdct.clearSelection();

                            closeDialog(event);
                            return;
                        }
                        SaleDetailViewModel.addSaleDetail();

                        SpotyMessage notification =
                                new SpotyMessage.MessageBuilder("Product added successfully")
                                        .duration(MessageDuration.SHORT)
                                        .icon("fas-circle-check")
                                        .type(MessageVariants.SUCCESS)
                                        .build();
                        notificationHolder.addMessage(notification);

                        saleDetailPdct.clearSelection();

                        closeDialog(event);
                        return;
                    }
                    SpotyMessage notification =
                            new SpotyMessage.MessageBuilder("Required fields missing")
                                    .duration(MessageDuration.SHORT)
                                    .icon("fas-triangle-exclamation")
                                    .type(MessageVariants.ERROR)
                                    .build();
                    notificationHolder.addMessage(notification);
                });
    }
}
