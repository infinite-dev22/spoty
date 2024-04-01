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
import inc.nomard.spoty.core.viewModels.quotations.QuotationDetailViewModel;
import inc.nomard.spoty.network_bridge.dtos.Product;
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
import static inc.nomard.spoty.core.values.SharedResources.tempIdProperty;

public class QuotationDetailFormController implements Initializable {
    private static QuotationDetailFormController instance;
    @FXML
    public MFXTextField quotationProductQnty;
    @FXML
    public MFXFilterComboBox<Product> quotationProductPdct;
    @FXML
    public MFXTextField quotationProductsOrderTax;
    @FXML
    public MFXTextField quotationProductsDiscount;
    @FXML
    public MFXButton quotationProductsSaveBtn;
    @FXML
    public MFXButton quotationProductsCancelBtn;
    @FXML
    public Label quotationProductPdctValidationLabel;
    @FXML
    public Label quotationProductQntyValidationLabel;

    public static QuotationDetailFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new QuotationDetailFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind form input values.
        quotationProductQnty
                .textProperty()
                .bindBidirectional(QuotationDetailViewModel.quantityProperty());
        quotationProductPdct
                .valueProperty()
                .bindBidirectional(QuotationDetailViewModel.productProperty());
        quotationProductsOrderTax
                .textProperty()
                .bindBidirectional(QuotationDetailViewModel.taxProperty());
        quotationProductsDiscount
                .textProperty()
                .bindBidirectional(QuotationDetailViewModel.discountProperty());

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
        quotationProductPdct.setItems(ProductViewModel.getProducts());
        quotationProductPdct.setConverter(productVariantConverter);
        quotationProductPdct.setFilterFunction(productVariantFilterFunction);

        // Input validators.
        requiredValidator(
                quotationProductPdct,
                "Product is required.",
                quotationProductPdctValidationLabel,
                quotationProductsSaveBtn);
        requiredValidator(
                quotationProductQnty,
                "Quantity is required.",
                quotationProductQntyValidationLabel,
                quotationProductsSaveBtn);

        dialogOnActions();
    }

    private void dialogOnActions() {
        quotationProductsCancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);
                    QuotationDetailViewModel.resetProperties();
                    quotationProductPdct.clearSelection();
                    quotationProductPdctValidationLabel.setVisible(false);
                    quotationProductQntyValidationLabel.setVisible(false);
                });
        quotationProductsSaveBtn.setOnAction(
                (event) -> {
                    SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();

                    if (!quotationProductPdctValidationLabel.isVisible()
                            && !quotationProductQntyValidationLabel.isVisible()) {
                        if (tempIdProperty().get() > -1) {
                            QuotationDetailViewModel.updateQuotationDetail(
                                    QuotationDetailViewModel.getId());

                            SpotyMessage notification =
                                    new SpotyMessage.MessageBuilder("Product changed successfully")
                                            .duration(MessageDuration.SHORT)
                                            .icon("fas-circle-check")
                                            .type(MessageVariants.SUCCESS)
                                            .build();
                            notificationHolder.addMessage(notification);

                            quotationProductPdct.clearSelection();

                            closeDialog(event);
                            return;
                        }
                        QuotationDetailViewModel.addQuotationDetails();

                        SpotyMessage notification =
                                new SpotyMessage.MessageBuilder("Product added successfully")
                                        .duration(MessageDuration.SHORT)
                                        .icon("fas-circle-check")
                                        .type(MessageVariants.SUCCESS)
                                        .build();

                        notificationHolder.addMessage(notification);

                        quotationProductPdct.clearSelection();

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
