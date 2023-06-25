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
import static org.infinite.spoty.viewModels.CurrencyViewModel.clearCurrencyData;
import static org.infinite.spoty.viewModels.CurrencyViewModel.saveCurrency;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.viewModels.CurrencyViewModel;

public class CurrencyFormController implements Initializable {
  private static CurrencyFormController instance;
  public MFXTextField currencyFormID = new MFXTextField();
  @FXML public Label currencyFormTitle;
  @FXML public MFXTextField currencyFormName;
  @FXML public MFXFilledButton currencyFormSaveBtn;
  @FXML public MFXOutlinedButton currencyFormCancelBtn;
  @FXML public MFXTextField currencyFormCode;
  @FXML public MFXTextField currencyFormSymbol;
  @FXML public Label currencyFormCodeValidationLabel;
  @FXML public Label currencyFormNameValidationLabel;

  public static CurrencyFormController getInstance() {
    if (Objects.equals(instance, null)) instance = new CurrencyFormController();
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Input listeners.
    // Input bindings.
    currencyFormID
        .textProperty()
        .bindBidirectional(CurrencyViewModel.idProperty(), new NumberStringConverter());
    currencyFormCode.textProperty().bindBidirectional(CurrencyViewModel.codeProperty());
    currencyFormName.textProperty().bindBidirectional(CurrencyViewModel.nameProperty());
    currencyFormSymbol.textProperty().bindBidirectional(CurrencyViewModel.symbolProperty());
    // Input listeners.
    requiredValidator(currencyFormName, "Name is required.", currencyFormNameValidationLabel);
    requiredValidator(currencyFormCode, "Code is required.", currencyFormCodeValidationLabel);
    dialogOnActions();
  }

  private void dialogOnActions() {
    currencyFormCancelBtn.setOnAction(
        (e) -> {
          clearCurrencyData();
          closeDialog(e);
          currencyFormNameValidationLabel.setVisible(false);
          currencyFormCodeValidationLabel.setVisible(false);
        });
    currencyFormSaveBtn.setOnAction(
        (e) -> {
          if (!currencyFormNameValidationLabel.isVisible()
              && !currencyFormCodeValidationLabel.isVisible()) {
            if (Integer.parseInt(currencyFormID.getText()) > 0)
              CurrencyViewModel.updateItem(Integer.parseInt(currencyFormID.getText()));
            else saveCurrency();
            clearCurrencyData();
            closeDialog(e);
          }
        });
  }
}
