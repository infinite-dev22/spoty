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
import static org.infinite.spoty.Validators.*;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.viewModels.SupplierViewModel;

public class SupplierFormController implements Initializable {
  public MFXTextField supplierID = new MFXTextField();
  @FXML public MFXFilledButton supplierFormSaveBtn;
  @FXML public MFXOutlinedButton supplierFormCancelBtn;
  @FXML public Label supplierFormTitle;
  @FXML public MFXTextField supplierFormName;
  @FXML public MFXTextField supplierFormEmail;
  @FXML public MFXTextField supplierFormPhone;
  @FXML public MFXTextField supplierFormCity;
  @FXML public MFXTextField supplierFormCountry;
  @FXML public MFXTextField supplierFormTaxNumber;
  @FXML public MFXTextField supplierFormAddress;
  @FXML public Label supplierFormNameValidationLabel;
  @FXML public Label supplierFormEmailValidationLabel;
  @FXML public Label supplierFormPhoneValidationLabel;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Input listeners.
    supplierFormPhone
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (!newValue.matches("\\d*"))
                supplierFormPhone.setText(newValue.replaceAll("\\D", ""));
            });
    supplierFormPhone
        .focusedProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue != oldValue) supplierFormPhone.setLeadingIcon(new Label("+"));
              System.out.println("newValue oldValue");
            });
    // Form input binding.
    supplierID
        .textProperty()
        .bindBidirectional(SupplierViewModel.idProperty(), new NumberStringConverter());
    supplierFormName.textProperty().bindBidirectional(SupplierViewModel.nameProperty());
    supplierFormEmail.textProperty().bindBidirectional(SupplierViewModel.emailProperty());
    supplierFormPhone.textProperty().bindBidirectional(SupplierViewModel.phoneProperty());
    supplierFormCity.textProperty().bindBidirectional(SupplierViewModel.cityProperty());
    supplierFormCountry.textProperty().bindBidirectional(SupplierViewModel.countryProperty());
    supplierFormTaxNumber.textProperty().bindBidirectional(SupplierViewModel.taxNumberProperty());
    supplierFormAddress.textProperty().bindBidirectional(SupplierViewModel.addressProperty());
    // Input validations.
    // Name input validation.
    requiredValidator(supplierFormName, "Name field is required.", supplierFormNameValidationLabel);
    // Email input validation.
    emailValidator(supplierFormEmail, supplierFormEmailValidationLabel);
    // Phone input validation.
    lengthValidator(supplierFormPhone, 11, "Invalid length", supplierFormPhoneValidationLabel);
    dialogOnActions();
  }

  private void dialogOnActions() {
    supplierFormCancelBtn.setOnAction(
        (e) -> {
          closeDialog(e);
          SupplierViewModel.resetProperties();
          supplierFormNameValidationLabel.setVisible(false);
          supplierFormEmailValidationLabel.setVisible(false);
          supplierFormPhoneValidationLabel.setVisible(false);
        });
    supplierFormSaveBtn.setOnAction(
        (e) -> {
          if (!supplierFormNameValidationLabel.isVisible()
              && !supplierFormEmailValidationLabel.isVisible()
              && !supplierFormPhoneValidationLabel.isVisible()) {
            if (Integer.parseInt(supplierID.getText()) > 0)
              SupplierViewModel.updateItem(Integer.parseInt(supplierID.getText()));
            else SupplierViewModel.saveSupplier();
            SupplierViewModel.resetProperties();
            closeDialog(e);
          }
        });
  }
}
