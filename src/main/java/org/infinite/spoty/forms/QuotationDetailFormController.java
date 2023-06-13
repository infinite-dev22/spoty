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
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.viewModels.ProductDetailViewModel;
import org.infinite.spoty.viewModels.QuotationDetailViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;

public class QuotationDetailFormController implements Initializable {
    public MFXTextField quotationDetailID = new MFXTextField();
    @FXML
    public MFXTextField quotationProductsQnty;
    @FXML
    public MFXFilterComboBox<ProductDetail> quotationProductsPdct;
    @FXML
    public MFXTextField quotationProductsOrderTax;
    @FXML
    public MFXTextField quotationProductsDiscount;
    @FXML
    public MFXFilledButton quotationProductsSaveBtn;
    @FXML
    public MFXOutlinedButton quotationProductsCancelBtn;
    @FXML
    public Label quotationProductsTitle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Add form input listeners.
        quotationProductsQnty.textProperty().addListener((observable, oldValue, newValue) -> quotationProductsQnty.setTrailingIcon(null));
        quotationProductsPdct.textProperty().addListener((observable, oldValue, newValue) -> quotationProductsPdct.setLeadingIcon(null));
        quotationProductsOrderTax.textProperty().addListener((observable, oldValue, newValue) -> quotationProductsOrderTax.setLeadingIcon(null));
        quotationProductsDiscount.textProperty().addListener((observable, oldValue, newValue) -> quotationProductsDiscount.setLeadingIcon(null));
        // Bind form input values.
        quotationDetailID.textProperty().bindBidirectional(QuotationDetailViewModel.idProperty());
        quotationProductsQnty.textProperty().bindBidirectional(QuotationDetailViewModel.quantityProperty());
        quotationProductsPdct.valueProperty().bindBidirectional(QuotationDetailViewModel.productProperty());
        quotationProductsOrderTax.textProperty().bindBidirectional(QuotationDetailViewModel.taxProperty());
        quotationProductsDiscount.textProperty().bindBidirectional(QuotationDetailViewModel.discountProperty());
        // Combo box properties.
        quotationProductsPdct.setItems(ProductDetailViewModel.purchaseDetailsList);
        quotationProductsPdct.setConverter(new StringConverter<>() {
            @Override
            public String toString(ProductDetail object) {
                if (object != null)
                    return object.getProduct().getName() + " " + object.getName();
                else
                    return null;
            }

            @Override
            public ProductDetail fromString(String string) {
                return null;
            }
        });
        dialogOnActions();
    }

    private void dialogOnActions() {
        quotationProductsCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            QuotationDetailViewModel.resetProperties();
            quotationProductsQnty.setTrailingIcon(null);
            quotationProductsPdct.setLeadingIcon(null);
            quotationProductsOrderTax.setLeadingIcon(null);
            quotationProductsDiscount.setLeadingIcon(null);
        });
        quotationProductsSaveBtn.setOnAction((e) -> {
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (quotationProductsQnty.getText().length() == 0) {
                quotationProductsQnty.setTrailingIcon(icon);
            }
//            if (quotationProductsPdct.getText().length() == 0) {
//                quotationProductsPdct.setLeadingIcon(icon);
//            }
            if (quotationProductsOrderTax.getText().length() == 0) {
                quotationProductsOrderTax.setLeadingIcon(icon);
            }
            if (quotationProductsDiscount.getText().length() == 0) {
                quotationProductsDiscount.setLeadingIcon(icon);
            }
            if (quotationProductsQnty.getText().length() > 0
//                    && quotationProductsPdct.getText().length() > 0
                    && quotationProductsOrderTax.getText().length() > 0
                    && quotationProductsDiscount.getText().length() > 0) {
                if (!quotationDetailID.getText().isEmpty() && Integer.parseInt(quotationDetailID.getText()) > 0)
                    QuotationDetailViewModel.updateQuotationDetail();
                else
                    QuotationDetailViewModel.addQuotationDetails();
                QuotationDetailViewModel.resetProperties();
                closeDialog(e);
            }
        });
    }
}
