package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.infinite.spoty.models.Customer;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;

public class CustomerFormController implements Initializable {
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
    public MFXTextField customerFormTown;
    @FXML
    public MFXTextField customerFormCity;
    @FXML
    public MFXTextField customerFormTaxNumber;
    @FXML
    public MFXTextField customerFormAddress;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customerFormName.textProperty().addListener((observable, oldValue, newValue) -> customerFormName.setTrailingIcon(null));
//        customerFormEmail.textProperty().addListener((observable, oldValue, newValue) -> customerFormEmail.setTrailingIcon(null));
//        customerFormPhone.textProperty().addListener((observable, oldValue, newValue) -> customerFormPhone.setTrailingIcon(null));
//        customerFormTown.textProperty().addListener((observable, oldValue, newValue) -> customerFormTown.setTrailingIcon(null));
//        customerFormCity.textProperty().addListener((observable, oldValue, newValue) -> customerFormCity.setTrailingIcon(null));
//        customerFormTaxNumber.textProperty().addListener((observable, oldValue, newValue) -> customerFormTaxNumber.setTrailingIcon(null));
//        customerFormAddress.textProperty().addListener((observable, oldValue, newValue) -> customerFormAddress.setTrailingIcon(null));

        dialogOnActions();
    }

    private void dialogOnActions() {
        customerFormCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            customerFormName.setText("");
            customerFormEmail.setText("");
            customerFormPhone.setText("");
            customerFormTown.setText("");
            customerFormCity.setText("");
            customerFormTaxNumber.setText("");
            customerFormAddress.setText("");

            customerFormName.setTrailingIcon(null);
            customerFormEmail.setTrailingIcon(null);
            customerFormPhone.setTrailingIcon(null);
            customerFormTown.setTrailingIcon(null);
            customerFormCity.setTrailingIcon(null);
            customerFormTaxNumber.setTrailingIcon(null);
            customerFormAddress.setTrailingIcon(null);
        });
        customerFormSaveBtn.setOnAction((e) -> {
            Customer brand = new Customer();
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (customerFormName.getText().length() == 0) {
                customerFormName.setTrailingIcon(icon);
            }
            if (customerFormEmail.getText().length() == 0) {
                customerFormEmail.setTrailingIcon(icon);
            }
            if (customerFormPhone.getText().length() == 0) {
                customerFormPhone.setTrailingIcon(icon);
            }
            if (customerFormTown.getText().length() == 0) {
                customerFormTown.setTrailingIcon(icon);
            }
            if (customerFormCity.getText().length() == 0) {
                customerFormCity.setTrailingIcon(icon);
            }
            if (customerFormTaxNumber.getText().length() == 0) {
                customerFormTaxNumber.setTrailingIcon(icon);
            }
            if (customerFormAddress.getText().length() == 0) {
                customerFormAddress.setTrailingIcon(icon);
            }
            if (customerFormName.getText().length() > 0
                    && customerFormEmail.getText().length() > 0
                    && customerFormPhone.getText().length() > 0
                    && customerFormTown.getText().length() > 0
                    && customerFormCity.getText().length() > 0
                    && customerFormTaxNumber.getText().length() > 0
                    && customerFormAddress.getText().length() > 0) {
                brand.setCustomerName(customerFormName.getText());
                brand.setCustomerEmail(customerFormEmail.getText());
                brand.setCustomerPhoneNumber(customerFormPhone.getText());
                brand.setCustomerTown(customerFormTown.getText());
                brand.setCustomerCity(customerFormCity.getText());
                brand.setCustomerTaxNumber(customerFormTaxNumber.getText());
                brand.setCustomerAddress(customerFormAddress.getText());

                closeDialog(e);

                customerFormName.setText("");
                customerFormEmail.setText("");
                customerFormPhone.setText("");
                customerFormTown.setText("");
                customerFormCity.setText("");
                customerFormTaxNumber.setText("");
                customerFormAddress.setText("");
            }
        });
    }
}
