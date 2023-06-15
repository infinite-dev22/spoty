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
import org.infinite.spoty.viewModels.AdjustmentDetailViewModel;
import org.infinite.spoty.viewModels.ProductDetailViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;

public class ProductDetailFormController implements Initializable {
    public MFXTextField adjustmentDetailID = new MFXTextField();
    @FXML
    public MFXTextField adjustmentProductsQnty;
    @FXML
    public MFXFilterComboBox<ProductDetail> adjustmentProductsPdct;
    @FXML
    public MFXFilledButton adjustmentProductsSaveBtn;
    @FXML
    public MFXOutlinedButton adjustmentProductsCancelBtn;
    @FXML
    public Label adjustmentProductsTitle;
    @FXML
    public MFXComboBox<String> adjustmentType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Add form input listeners.
        adjustmentProductsPdct.textProperty().addListener((observable, oldValue, newValue) -> adjustmentProductsPdct.setLeadingIcon(null));
        adjustmentProductsQnty.textProperty().addListener((observable, oldValue, newValue) -> adjustmentProductsQnty.setTrailingIcon(null));
        adjustmentType.textProperty().addListener((observable, oldValue, newValue) -> adjustmentType.setLeadingIcon(null));
        // Bind form input value to property value.
        adjustmentDetailID.textProperty().bindBidirectional(AdjustmentDetailViewModel.idProperty());
        adjustmentProductsPdct.valueProperty().bindBidirectional(AdjustmentDetailViewModel.productProperty());
        adjustmentProductsQnty.textProperty().bindBidirectional(AdjustmentDetailViewModel.quantityProperty());
        adjustmentType.textProperty().bindBidirectional(AdjustmentDetailViewModel.adjustmentTypeProperty());
        // AdjustmentType combo box properties.
        adjustmentProductsPdct.setItems(ProductDetailViewModel.productDetailsList);
        adjustmentProductsPdct.setConverter(new StringConverter<>() {
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
        adjustmentType.setItems(FXCollections.observableArrayList(Values.ADJUSTMENTTYPE));
        dialogOnActions();
    }

    private void dialogOnActions() {
        adjustmentProductsCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            AdjustmentDetailViewModel.resetProperties();
            adjustmentProductsPdct.setLeadingIcon(null);
            adjustmentProductsQnty.setTrailingIcon(null);
            adjustmentType.setLeadingIcon(null);
        });
        adjustmentProductsSaveBtn.setOnAction((e) -> {
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);
            if (adjustmentProductsPdct.getText().length() == 0) {
                adjustmentProductsPdct.setLeadingIcon(icon);
            }
            if (adjustmentProductsQnty.getText().length() == 0) {
                adjustmentProductsQnty.setTrailingIcon(icon);
            }
            if (adjustmentType.getText().length() == 0) {
                adjustmentType.setLeadingIcon(icon);
            }
            if (adjustmentProductsQnty.getText().length() > 0
                    && adjustmentProductsPdct.getText().length() > 0
                    && adjustmentType.getText().length() > 0) {
                if (!adjustmentDetailID.getText().isEmpty()) {
                    try {
                        if (Integer.parseInt(adjustmentDetailID.getText()) > 0)
                            AdjustmentDetailViewModel.updateItem(Integer.parseInt(adjustmentDetailID.getText()));
                    } catch (NumberFormatException ignored) {
                        AdjustmentDetailViewModel.updateAdjustmentDetail(Integer.parseInt(adjustmentDetailID.getText()
                                .substring(adjustmentDetailID.getText().lastIndexOf(':') + 1,
                                        adjustmentDetailID.getText().indexOf(';'))));
                    }
                } else AdjustmentDetailViewModel.addAdjustmentDetails();
                AdjustmentDetailViewModel.resetProperties();
                closeDialog(e);
            }
        });
    }
}
