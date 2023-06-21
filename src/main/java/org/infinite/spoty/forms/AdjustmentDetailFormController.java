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
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.values.strings.Values;
import org.infinite.spoty.viewModels.AdjustmentDetailViewModel;
import org.infinite.spoty.viewModels.ProductDetailViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.Validators.requiredValidator;
import static org.infinite.spoty.values.SharedResources.tempIdProperty;

public class AdjustmentDetailFormController implements Initializable {
    public MFXTextField adjustmentDetailID = new MFXTextField();
    @FXML
    public MFXTextField adjustmentProductsQnty;
    @FXML
    public MFXFilterComboBox<ProductDetail> adjustmentProductVariant;
    @FXML
    public MFXFilledButton adjustmentProductsSaveBtn;
    @FXML
    public MFXOutlinedButton adjustmentProductsCancelBtn;
    @FXML
    public Label adjustmentProductsTitle;
    @FXML
    public MFXComboBox<String> adjustmentType;
    @FXML
    public Label adjustmentProductVariantValidationLabel;
    @FXML
    public Label adjustmentProductsQntyValidationLabel;
    @FXML
    public Label adjustmentTypeValidationLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind form input value to property value.
        adjustmentDetailID.textProperty()
                .bindBidirectional(AdjustmentDetailViewModel.idProperty(), new NumberStringConverter());
        adjustmentProductVariant.valueProperty().bindBidirectional(AdjustmentDetailViewModel.productProperty());
        adjustmentProductsQnty.textProperty().bindBidirectional(AdjustmentDetailViewModel.quantityProperty());
        adjustmentType.textProperty().bindBidirectional(AdjustmentDetailViewModel.adjustmentTypeProperty());
        // AdjustmentType combo box properties.
        adjustmentProductVariant.setItems(ProductDetailViewModel.getProductDetails());
        adjustmentProductVariant.setConverter(new StringConverter<>() {
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
        adjustmentType.setItems(FXCollections.observableArrayList(Values.ADJUSTMENTTYPE));
        // Input validators.
        requiredValidator(adjustmentProductVariant, "Product is required.", adjustmentProductVariantValidationLabel);
        requiredValidator(adjustmentProductsQnty, "Quantity is required.", adjustmentProductsQntyValidationLabel);
        requiredValidator(adjustmentType, "Type is required.", adjustmentTypeValidationLabel);
        dialogOnActions();
    }

    private void dialogOnActions() {
        adjustmentProductsCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            AdjustmentDetailViewModel.resetProperties();
            adjustmentProductVariantValidationLabel.setVisible(false);
            adjustmentProductsQntyValidationLabel.setVisible(false);
            adjustmentTypeValidationLabel.setVisible(false);
        });
        adjustmentProductsSaveBtn.setOnAction((e) -> {
            if (!adjustmentProductVariantValidationLabel.isVisible()
                    && !adjustmentProductsQntyValidationLabel.isVisible()
                    && !adjustmentTypeValidationLabel.isVisible()) {
                if (tempIdProperty().get() > -1)
                    AdjustmentDetailViewModel.updateAdjustmentDetail(AdjustmentDetailViewModel.getId());
                else
                    AdjustmentDetailViewModel.addAdjustmentDetails();
                AdjustmentDetailViewModel.resetProperties();
                closeDialog(e);
            }
        });
    }
}
