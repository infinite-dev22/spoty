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

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.Validators.requiredValidator;
import static org.infinite.spoty.values.SharedResources.tempIdProperty;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import org.infinite.spoty.database.models.UnitOfMeasure;
import org.infinite.spoty.viewModels.ProductDetailViewModel;
import org.infinite.spoty.viewModels.UOMViewModel;

public class ProductDetailFormController implements Initializable {
  @FXML public MFXButton productProductsSaveBtn;
  @FXML public MFXButton productProductsCancelBtn;
  @FXML public Label productProductsTitle;
  @FXML public MFXTextField productVariantSerial;
  @FXML public MFXTextField productVariantName;
  @FXML public Label productVariantNameValidationLabel;
  @FXML public MFXFilterComboBox<UnitOfMeasure> productVariantUOM;
  @FXML public Label productVariantUOMValidationLabel;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Bind form input value to property value.
    productVariantUOM.valueProperty().bindBidirectional(ProductDetailViewModel.unitProperty());
    productVariantSerial.textProperty().bindBidirectional(ProductDetailViewModel.serialProperty());
    productVariantName.textProperty().bindBidirectional(ProductDetailViewModel.nameProperty());
    // ProductType combo box properties.
    productVariantUOM.setItems(UOMViewModel.uomComboList);
    productVariantUOM.setResetOnPopupHidden(true);
    productVariantUOM.setAnimated(true);
    productVariantUOM.setConverter(
        new StringConverter<>() {
          @Override
          public String toString(UnitOfMeasure object) {
            return (object != null) ? object.getName() : "--Unit Of Measure--";
          }

          @Override
          public UnitOfMeasure fromString(String string) {
            return null;
          }
        });
    // Input validators.
    requiredValidator(
        productVariantUOM, "Unit of measure is required.", productVariantUOMValidationLabel);
    requiredValidator(productVariantName, "Name is required.", productVariantNameValidationLabel);
    dialogOnActions();
  }

  private void dialogOnActions() {
    productProductsCancelBtn.setOnAction(
        (e) -> {
          closeDialog(e);
          ProductDetailViewModel.resetProperties();
          productVariantUOMValidationLabel.setVisible(false);
          productVariantNameValidationLabel.setVisible(false);
        });
    productProductsSaveBtn.setOnAction(
        (e) -> {
          // TODO: Find better logic for this.
          // If one of productVariantUOM or productVariantName is provide can save the product.
          if (productVariantName.getText().isEmpty() && !productVariantUOM.getText().isEmpty())
            productVariantNameValidationLabel.setVisible(false);
          if (productVariantUOM.getText().isEmpty() && !productVariantName.getText().isEmpty())
            productVariantUOMValidationLabel.setVisible(false);
          if (!productVariantUOMValidationLabel.isVisible()
              && !productVariantNameValidationLabel.isVisible()) {
            if (tempIdProperty().get() > -1)
              ProductDetailViewModel.updateProductDetail(ProductDetailViewModel.getId());
            else ProductDetailViewModel.addProductDetail();
            ProductDetailViewModel.resetProperties();
            closeDialog(e);
          }
        });
  }
}
