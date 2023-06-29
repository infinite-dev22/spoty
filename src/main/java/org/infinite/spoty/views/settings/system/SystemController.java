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

package org.infinite.spoty.views.settings.system;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.checkbox.MFXCheckBox;
import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.Currency;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.CurrencyViewModel;

public class SystemController implements Initializable {
  private static SystemController instance;
  @FXML public MFXComboBox<Currency> defaultCurrency;
  @FXML public MFXComboBox<?> defaultLanguage;
  @FXML public MFXTextField defaultEmail;
  @FXML public MFXTextField companyName;
  @FXML public MFXTextField companyPhone;
  @FXML public MFXComboBox<Branch> defaultBranch;
  @FXML public MFXTextField branchAddress;
  @FXML public MFXCheckBox invoiceFooter;
  @FXML public GridPane systemSettings;

  public static SystemController getInstance() {
    if (Objects.equals(instance, null)) instance = new SystemController();
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Input listeners.
    // Input bindings.
    // Combo box properties.
    defaultCurrency.setItems(CurrencyViewModel.currenciesList);
    defaultCurrency.setConverter(
        new StringConverter<>() {
          @Override
          public String toString(Currency object) {
            if (object != null) return object.getName() + " " + object.getSymbol();
            else return null;
          }

          @Override
          public Currency fromString(String string) {
            return null;
          }
        });
    defaultBranch.setItems(BranchViewModel.branchesList);
    defaultBranch.setConverter(
        new StringConverter<>() {
          @Override
          public String toString(Branch object) {
            if (object != null) return object.getName();
            else return null;
          }

          @Override
          public Branch fromString(String string) {
            return null;
          }
        });
  }

  @FXML
  private void saveClicked() {
    defaultCurrency.getItems();
    defaultLanguage.getItems();
    defaultBranch.getItems();
    defaultEmail.getText();
    companyName.getText();
    companyPhone.getText();
    branchAddress.getText();
    invoiceFooter.isSelected();

    // Reset the input forms.
    defaultCurrency.setItems(null);
    defaultLanguage.setItems(null);
    defaultBranch.setItems(null);
    defaultEmail.setText("");
    companyName.setText("");
    companyPhone.setText("");
    branchAddress.setText("");
    invoiceFooter.setSelected(true);
  }

  @FXML
  private void companyLogoClicked() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose Company Logo");
    fileChooser
        .getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Images", "*.jpeg", "*.gif", "*.png", "*.jpg"));
    File res = fileChooser.showOpenDialog(systemSettings.getScene().getWindow());
    System.out.println(res);
  }
}
