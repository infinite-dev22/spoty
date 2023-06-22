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
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.viewModels.ProductDetailViewModel;
import org.infinite.spoty.viewModels.QuotationDetailViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.Validators.requiredValidator;
import static org.infinite.spoty.values.SharedResources.tempIdProperty;

public class QuotationDetailFormController implements Initializable {
    @FXML
    public MFXTextField quotationProductQnty;
    @FXML
    public MFXFilterComboBox<ProductDetail> quotationProductPdct;
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
    @FXML
    public Label quotationProductPdctValidationLabel;
    @FXML
    public Label quotationProductQntyValidationLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind form input values.
        quotationProductQnty.textProperty().bindBidirectional(QuotationDetailViewModel.quantityProperty());
        quotationProductPdct.valueProperty().bindBidirectional(QuotationDetailViewModel.productProperty());
        quotationProductsOrderTax.textProperty().bindBidirectional(QuotationDetailViewModel.taxProperty());
        quotationProductsDiscount.textProperty().bindBidirectional(QuotationDetailViewModel.discountProperty());
        // Combo box properties.
        quotationProductPdct.setItems(ProductDetailViewModel.getProductDetails());
        quotationProductPdct.setConverter(new StringConverter<>() {
            @Override
            public String toString(ProductDetail object) {
                return object != null ? object.getProduct().getName()
                        + " " + (object.getUnit() != null ? (object.getName().isEmpty() ? "" : object.getName())
                        + " " + object.getUnit().getName() : object.getName()) : "No products";
            }

            @Override
            public ProductDetail fromString(String string) {
                return null;
            }
        });
        // Input validators.
        requiredValidator(quotationProductPdct, "Product is required.", quotationProductPdctValidationLabel);
        requiredValidator(quotationProductQnty, "Quantity is required.", quotationProductQntyValidationLabel);
        dialogOnActions();
    }

    private void dialogOnActions() {
        quotationProductsCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            QuotationDetailViewModel.resetProperties();
            quotationProductPdctValidationLabel.setVisible(false);
            quotationProductQntyValidationLabel.setVisible(false);
        });
        quotationProductsSaveBtn.setOnAction((e) -> {
            if (!quotationProductPdctValidationLabel.isVisible()
                    && !quotationProductQntyValidationLabel.isVisible()) {
                if (tempIdProperty().get() > -1)
                    QuotationDetailViewModel.updateQuotationDetail(QuotationDetailViewModel.getId());
                else
                    QuotationDetailViewModel.addQuotationDetails();
                QuotationDetailViewModel.resetProperties();
                closeDialog(e);
            }
        });
    }
}
