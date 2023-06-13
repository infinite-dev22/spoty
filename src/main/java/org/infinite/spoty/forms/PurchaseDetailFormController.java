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
import org.infinite.spoty.viewModels.PurchaseDetailViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;

public class PurchaseDetailFormController implements Initializable {
    public MFXTextField purchaseDetailID = new MFXTextField();
    @FXML
    public MFXTextField purchaseDetailQnty;
    @FXML
    public MFXFilterComboBox<ProductDetail> purchaseDetail;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        purchaseDetailQnty.textProperty().addListener((observable, oldValue, newValue) -> purchaseDetailQnty.setTrailingIcon(null));
//        purchaseDetail.textProperty().addListener((observable, oldValue, newValue) -> purchaseDetail.setLeadingIcon(null));
//        purchaseDetailOrderTax.textProperty().addListener((observable, oldValue, newValue) -> purchaseDetailOrderTax.setLeadingIcon(null));
//        purchaseDetailDiscount.textProperty().addListener((observable, oldValue, newValue) -> purchaseDetailDiscount.setLeadingIcon(null));

        purchaseDetailID.textProperty().bindBidirectional(PurchaseDetailViewModel.idProperty());
        purchaseDetailQnty.textProperty().bindBidirectional(PurchaseDetailViewModel.quantityProperty());
        purchaseDetail.valueProperty().bindBidirectional(PurchaseDetailViewModel.productProperty());
        purchaseDetailOrderTax.textProperty().bindBidirectional(PurchaseDetailViewModel.netTaxProperty());
        purchaseDetailDiscount.textProperty().bindBidirectional(PurchaseDetailViewModel.discountProperty());
        purchaseDetail.setItems(ProductDetailViewModel.purchaseDetailsList);
        purchaseDetail.setConverter(new StringConverter<>() {
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
        purchaseDetailCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            PurchaseDetailViewModel.resetProperties();
            purchaseDetailQnty.setTrailingIcon(null);
            purchaseDetail.setLeadingIcon(null);
            purchaseDetailOrderTax.setLeadingIcon(null);
            purchaseDetailDiscount.setLeadingIcon(null);
        });
        purchaseDetailSaveBtn.setOnAction((e) -> {
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (purchaseDetailQnty.getText().length() == 0) {
                purchaseDetailQnty.setTrailingIcon(icon);
            }
            if (purchaseDetail.getText().length() == 0) {
                purchaseDetail.setLeadingIcon(icon);
            }
            if (purchaseDetailOrderTax.getText().length() == 0) {
                purchaseDetailOrderTax.setLeadingIcon(icon);
            }
            if (purchaseDetailDiscount.getText().length() == 0) {
                purchaseDetailDiscount.setLeadingIcon(icon);
            }
            if (purchaseDetailQnty.getText().length() > 0
                    && purchaseDetail.getText().length() > 0
                    && purchaseDetailOrderTax.getText().length() > 0
                    && purchaseDetailDiscount.getText().length() > 0) {
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
