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
import org.infinite.spoty.viewModels.PurchaseDetailViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.Validators.requiredValidator;

public class PurchaseDetailFormController implements Initializable {
    public MFXTextField purchaseDetailID = new MFXTextField();
    @FXML
    public MFXTextField purchaseDetailQnty;
    @FXML
    public MFXFilterComboBox<ProductDetail> purchaseDetailPdct;
    @FXML
    public MFXTextField purchaseDetailOrderTax;
    @FXML
    public MFXTextField purchaseDetailDiscount;
    @FXML
    public MFXFilledButton purchaseDetailSaveBtn;
    @FXML
    public MFXOutlinedButton purchaseDetailCancelBtn;
    @FXML
    public Label purchaseDetailTitle;
    @FXML
    public Label purchaseDetailQntyValidationLabel;
    @FXML
    public Label purchaseDetailPdctValidationLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input bindings.
        purchaseDetailID.textProperty().bindBidirectional(PurchaseDetailViewModel.idProperty());
        purchaseDetailQnty.textProperty().bindBidirectional(PurchaseDetailViewModel.quantityProperty());
        purchaseDetailPdct.valueProperty().bindBidirectional(PurchaseDetailViewModel.productProperty());
        purchaseDetailOrderTax.textProperty().bindBidirectional(PurchaseDetailViewModel.netTaxProperty());
        purchaseDetailDiscount.textProperty().bindBidirectional(PurchaseDetailViewModel.discountProperty());
        purchaseDetailPdct.setItems(ProductDetailViewModel.getProductDetails());
        purchaseDetailPdct.setConverter(new StringConverter<>() {
            @Override
            public String toString(ProductDetail object) {
                return object != null ? object.getProduct().getName()
                        + " " + (object.getUnit() != null ? (object.getName().isEmpty() ? "" : object.getName())
                        + " " + object.getUnit().getName() : object.getName()) : null;
            }

            @Override
            public ProductDetail fromString(String string) {
                return null;
            }
        });
        // Input validators.
        requiredValidator(purchaseDetailPdct, "Product is required.", purchaseDetailPdctValidationLabel);
        requiredValidator(purchaseDetailQnty, "Quantity is required.", purchaseDetailQntyValidationLabel);
        dialogOnActions();
    }

    private void dialogOnActions() {
        purchaseDetailCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            PurchaseDetailViewModel.resetProperties();
            purchaseDetailPdctValidationLabel.setVisible(false);
            purchaseDetailQntyValidationLabel.setVisible(false);
        });
        purchaseDetailSaveBtn.setOnAction((e) -> {
            if (!purchaseDetailPdctValidationLabel.isVisible()
                    && !purchaseDetailQntyValidationLabel.isVisible()) {
                if (!purchaseDetailID.getText().isEmpty()) {
                    try {
                        if (Integer.parseInt(purchaseDetailID.getText()) > 0)
                            PurchaseDetailViewModel.updateItem(Integer.parseInt(purchaseDetailID.getText()));
                    } catch (NumberFormatException ignored) {
                        PurchaseDetailViewModel.updatePurchaseDetail(Integer.parseInt(purchaseDetailID.getText()
                                .substring(purchaseDetailID.getText().lastIndexOf(':') + 1,
                                        purchaseDetailID.getText().indexOf(';'))));
                    }
                } else PurchaseDetailViewModel.addPurchaseDetail();
                PurchaseDetailViewModel.resetProperties();
                closeDialog(e);
            }
        });
    }
}
