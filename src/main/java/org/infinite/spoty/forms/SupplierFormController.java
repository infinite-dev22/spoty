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

import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.viewModels.SupplierViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;

public class SupplierFormController implements Initializable {
    public MFXTextField supplierID = new MFXTextField();
    @FXML
    public MFXFilledButton supplierFormSaveBtn;
    @FXML
    public MFXOutlinedButton supplierFormCancelBtn;
    @FXML
    public Label supplierFormTitle;
    @FXML
    public MFXTextField supplierFormName;
    @FXML
    public MFXTextField supplierFormEmail;
    @FXML
    public MFXTextField supplierFormPhone;
    @FXML
    public MFXTextField supplierFormCity;
    @FXML
    public MFXTextField supplierFormCountry;
    @FXML
    public MFXTextField supplierFormTaxNumber;
    @FXML
    public MFXTextField supplierFormAddress;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form input binding.
        supplierID.textProperty().bindBidirectional(SupplierViewModel.idProperty(), new NumberStringConverter());
        supplierFormName.textProperty().bindBidirectional(SupplierViewModel.nameProperty());
        supplierFormEmail.textProperty().bindBidirectional(SupplierViewModel.emailProperty());
        supplierFormPhone.textProperty().bindBidirectional(SupplierViewModel.phoneProperty());
        supplierFormCity.textProperty().bindBidirectional(SupplierViewModel.cityProperty());
        supplierFormCountry.textProperty().bindBidirectional(SupplierViewModel.countryProperty());
        supplierFormTaxNumber.textProperty().bindBidirectional(SupplierViewModel.taxNumberProperty());
        supplierFormAddress.textProperty().bindBidirectional(SupplierViewModel.addressProperty());

        dialogOnActions();
    }

    private void dialogOnActions() {
        supplierFormCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            SupplierViewModel.resetProperties();
            supplierFormName.setStyle("-fx-border-color: red;");
        });
        supplierFormSaveBtn.setOnAction((e) -> {
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (supplierFormName.getText().length() == 0) {
                supplierFormName.setStyle("-fx-border-color: red;");
            }
            if (supplierFormName.getText().length() > 0) {
                if (Integer.parseInt(supplierID.getText()) > 0)
                    SupplierViewModel.updateItem(Integer.parseInt(supplierID.getText()));
                else
                    SupplierViewModel.saveSupplier();
                SupplierViewModel.resetProperties();
                closeDialog(e);
            }
        });
    }
}
