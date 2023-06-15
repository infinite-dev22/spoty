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
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.values.strings.Values;
import org.infinite.spoty.viewModels.ProductDetailViewModel;
import org.infinite.spoty.viewModels.ProductDetailViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;

public class ProductDetailFormController implements Initializable {
    public MFXTextField productDetailID = new MFXTextField();
    @FXML
    public MFXTextField productProductsQnty;
    @FXML
    public MFXFilterComboBox<ProductDetail> productProductsPdct;
    @FXML
    public MFXFilledButton productProductsSaveBtn;
    @FXML
    public MFXOutlinedButton productProductsCancelBtn;
    @FXML
    public Label productProductsTitle;
    @FXML
    public MFXComboBox<String> productType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Add form input listeners.
        productProductsPdct.textProperty().addListener((observable, oldValue, newValue) -> productProductsPdct.setLeadingIcon(null));
        productProductsQnty.textProperty().addListener((observable, oldValue, newValue) -> productProductsQnty.setTrailingIcon(null));
        productType.textProperty().addListener((observable, oldValue, newValue) -> productType.setLeadingIcon(null));
        // Bind form input value to property value.
        productDetailID.textProperty().bindBidirectional(ProductDetailViewModel.idProperty());
        productProductsPdct.valueProperty().bindBidirectional(ProductDetailViewModel.productProperty());
//        productProductsQnty.textProperty().bindBidirectional(ProductDetailViewModel.quantityProperty());/
//        productType.textProperty().bindBidirectional(ProductDetailViewModel.productTypeProperty());
        // ProductType combo box properties.
        productProductsPdct.setItems(ProductDetailViewModel.productDetailsList);
        productProductsPdct.setConverter(new StringConverter<>() {
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
        productType.setItems(FXCollections.observableArrayList(Values.ADJUSTMENTTYPE));
        dialogOnActions();
    }

    private void dialogOnActions() {
        productProductsCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            ProductDetailViewModel.resetProperties();
            productProductsPdct.setLeadingIcon(null);
            productProductsQnty.setTrailingIcon(null);
            productType.setLeadingIcon(null);
        });
        productProductsSaveBtn.setOnAction((e) -> {
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);
            if (productProductsPdct.getText().length() == 0) {
                productProductsPdct.setLeadingIcon(icon);
            }
            if (productProductsQnty.getText().length() == 0) {
                productProductsQnty.setTrailingIcon(icon);
            }
            if (productType.getText().length() == 0) {
                productType.setLeadingIcon(icon);
            }
            if (productProductsQnty.getText().length() > 0
                    && productProductsPdct.getText().length() > 0
                    && productType.getText().length() > 0) {
                if (!productDetailID.getText().isEmpty()) {
                    try {
                        if (Integer.parseInt(productDetailID.getText()) > 0)
                            ProductDetailViewModel.updateItem(Integer.parseInt(productDetailID.getText()));
                    } catch (NumberFormatException ignored) {
                        ProductDetailViewModel.updateProductDetail(Integer.parseInt(productDetailID.getText()
                                .substring(productDetailID.getText().lastIndexOf(':') + 1,
                                        productDetailID.getText().indexOf(';'))));
                    }
                } else ProductDetailViewModel.addProductDetail();
                ProductDetailViewModel.resetProperties();
                closeDialog(e);
            }
        });
    }
}
