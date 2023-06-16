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

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.viewModels.CustomerViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.Validators.*;

public class CustomerFormController implements Initializable {
    public MFXTextField customerID = new MFXTextField();
    @FXML
    public MFXFilledButton customerFormSaveBtn;
    @FXML
    public MFXOutlinedButton customerFormCancelBtn;
    @FXML
    public Label customerFormTitle;
    @FXML
    public MFXTextField customerFormName;
    @FXML
    public MFXTextField customerFormEmail;
    @FXML
    public MFXTextField customerFormPhone;
    @FXML
    public MFXTextField customerFormCity;
    @FXML
    public MFXTextField customerFormCountry;
    @FXML
    public MFXTextField customerFormTaxNumber;
    @FXML
    public MFXTextField customerFormAddress;
    @FXML
    public Label validationLabel1, validationLabel2, validationLabel3;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form input listeners.
        // These don't work, not sure why.
        // TODO: Get a better way for input filtering.
        customerFormPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                customerFormPhone.setText(newValue.replaceAll("\\D", ""));
        });
        customerFormPhone.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != oldValue) customerFormPhone.setLeadingIcon(new Label("+"));
            System.out.println("newValue oldValue");
        });
        // Form input binding.
        customerID.textProperty().bindBidirectional(CustomerViewModel.idProperty(), new NumberStringConverter());
        customerFormName.textProperty().bindBidirectional(CustomerViewModel.nameProperty());
        customerFormEmail.textProperty().bindBidirectional(CustomerViewModel.emailProperty());
        customerFormPhone.textProperty().bindBidirectional(CustomerViewModel.phoneProperty());
        customerFormCity.textProperty().bindBidirectional(CustomerViewModel.cityProperty());
        customerFormCountry.textProperty().bindBidirectional(CustomerViewModel.countryProperty());
        customerFormTaxNumber.textProperty().bindBidirectional(CustomerViewModel.taxNumberProperty());
        customerFormAddress.textProperty().bindBidirectional(CustomerViewModel.addressProperty());
        // Name input validation.
        requiredValidator(customerFormName, "Name field is required.", validationLabel1);
        // Email input validation.
        emailValidator(customerFormEmail, validationLabel2);
        // Phone input validation.
        lengthValidator(customerFormPhone, 11, "Invalid length", validationLabel3);
        dialogOnActions();
    }

    private void dialogOnActions() {
        customerFormCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            validationLabel1.setVisible(false);
            validationLabel2.setVisible(false);
            validationLabel3.setVisible(false);

            CustomerViewModel.resetProperties();
        });
        customerFormSaveBtn.setOnAction((e) -> {
            if (!validationLabel1.isVisible() && !validationLabel2.isVisible() && !validationLabel3.isVisible()) {
                if (Integer.parseInt(customerID.getText()) > 0)
                    CustomerViewModel.updateItem(Integer.parseInt(customerID.getText()));
                else
                    CustomerViewModel.saveCustomer();
                CustomerViewModel.resetProperties();
                closeDialog(e);
                validationLabel1.setVisible(false);
                validationLabel2.setVisible(false);
                validationLabel3.setVisible(false);
            }
        });
    }
}
