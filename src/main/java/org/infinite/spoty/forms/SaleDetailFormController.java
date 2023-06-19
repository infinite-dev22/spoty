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
import org.infinite.spoty.viewModels.SaleDetailViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.Validators.requiredValidator;

public class SaleDetailFormController implements Initializable {
    public MFXTextField saleDetailID = new MFXTextField();
    @FXML
    public MFXTextField saleDetailQnty;
    @FXML
    public MFXFilterComboBox<ProductDetail> saleDetailPdct;
    @FXML
    public MFXTextField saleDeatilOrderTax;
    @FXML
    public MFXTextField saleDetailDiscount;
    @FXML
    public MFXFilledButton saleProductsSaveBtn;
    @FXML
    public MFXOutlinedButton saleProductsCancelBtn;
    @FXML
    public Label saleProductsTitle;
    @FXML
    public Label saleDetailPdctValidationLabel;
    @FXML
    public Label saleDetailQntyValidationLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set combo box options.
        saleDetailPdct.setItems(ProductDetailViewModel.productDetailsList);
        saleDetailPdct.setConverter(new StringConverter<>() {
            @Override
            public String toString(ProductDetail object) {
                if (object != null)
                    return object.getProduct().getName() + " " + object.getName();
                else
                    return "Empty, Add products";
            }

            @Override
            public ProductDetail fromString(String string) {
                return null;
            }
        });
        // Property binding.
        saleDetailID.textProperty().bindBidirectional(SaleDetailViewModel.idProperty());
        saleDetailQnty.textProperty().bindBidirectional(SaleDetailViewModel.quantityProperty());
        saleDetailPdct.valueProperty().bindBidirectional(SaleDetailViewModel.productProperty());
        saleDeatilOrderTax.textProperty().bindBidirectional(SaleDetailViewModel.netTaxProperty());
        saleDetailDiscount.textProperty().bindBidirectional(SaleDetailViewModel.discountProperty());
        // Input validators.
        requiredValidator(saleDetailPdct, "Product is required.", saleDetailPdctValidationLabel);
        requiredValidator(saleDetailQnty, "Quantity is required.", saleDetailQntyValidationLabel);
        dialogOnActions();
    }

    private void dialogOnActions() {
        saleProductsCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            SaleDetailViewModel.resetProperties();
            saleDetailPdctValidationLabel.setVisible(false);
            saleDetailQntyValidationLabel.setVisible(false);
        });
        saleProductsSaveBtn.setOnAction((e) -> {
            if (!saleDetailPdctValidationLabel.isVisible()
                    && !saleDetailQntyValidationLabel.isVisible()) {
                if (!saleDetailID.getText().isEmpty()) {
                    try {
                        if (Integer.parseInt(saleDetailID.getText()) > 0)
                            SaleDetailViewModel.updateItem(Integer.parseInt(saleDetailID.getText()));
                    } catch (NumberFormatException ignored) {
                        SaleDetailViewModel.updateSaleDetail(Integer.parseInt(saleDetailID.getText()
                                .substring(saleDetailID.getText().lastIndexOf(':') + 1,
                                        saleDetailID.getText().indexOf(';'))));
                    }
                } else
                    SaleDetailViewModel.addSaleDetail();
                SaleDetailViewModel.resetProperties();
                closeDialog(e);
            }
        });
    }
}
