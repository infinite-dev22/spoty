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

public class SupplierFormController implements Initializable {
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
    public MFXTextField supplierFormTown;
    @FXML
    public MFXTextField supplierFormCity;
    @FXML
    public MFXTextField supplierFormTaxNumber;
    @FXML
    public MFXTextField supplierFormAddress;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dialogOnActions();
        supplierFormName.textProperty().addListener((observable, oldValue, newValue) -> supplierFormName.setTrailingIcon(null));
//        supplierFormEmail.textProperty().addListener((observable, oldValue, newValue) -> supplierFormEmail.setTrailingIcon(null));
//        supplierFormPhone.textProperty().addListener((observable, oldValue, newValue) -> supplierFormPhone.setTrailingIcon(null));
//        supplierFormTown.textProperty().addListener((observable, oldValue, newValue) -> supplierFormTown.setTrailingIcon(null));
//        supplierFormCity.textProperty().addListener((observable, oldValue, newValue) -> supplierFormCity.setTrailingIcon(null));
//        supplierFormTaxNumber.textProperty().addListener((observable, oldValue, newValue) -> supplierFormTaxNumber.setTrailingIcon(null));
//        supplierFormAddress.textProperty().addListener((observable, oldValue, newValue) -> supplierFormAddress.setTrailingIcon(null));

        dialogOnActions();
    }

    private void dialogOnActions() {
        supplierFormCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            supplierFormName.setText("");
            supplierFormEmail.setText("");
            supplierFormPhone.setText("");
            supplierFormTown.setText("");
            supplierFormCity.setText("");
            supplierFormTaxNumber.setText("");
            supplierFormAddress.setText("");

            supplierFormName.setTrailingIcon(null);
            supplierFormEmail.setTrailingIcon(null);
            supplierFormPhone.setTrailingIcon(null);
            supplierFormTown.setTrailingIcon(null);
            supplierFormCity.setTrailingIcon(null);
            supplierFormTaxNumber.setTrailingIcon(null);
            supplierFormAddress.setTrailingIcon(null);
        });
        supplierFormSaveBtn.setOnAction((e) -> {
            Customer brand = new Customer();
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (supplierFormName.getText().length() == 0) {
                supplierFormName.setTrailingIcon(icon);
            }
            if (supplierFormEmail.getText().length() == 0) {
                supplierFormEmail.setTrailingIcon(icon);
            }
            if (supplierFormPhone.getText().length() == 0) {
                supplierFormPhone.setTrailingIcon(icon);
            }
            if (supplierFormTown.getText().length() == 0) {
                supplierFormTown.setTrailingIcon(icon);
            }
            if (supplierFormCity.getText().length() == 0) {
                supplierFormCity.setTrailingIcon(icon);
            }
            if (supplierFormTaxNumber.getText().length() == 0) {
                supplierFormTaxNumber.setTrailingIcon(icon);
            }
            if (supplierFormAddress.getText().length() == 0) {
                supplierFormAddress.setTrailingIcon(icon);
            }
            if (supplierFormName.getText().length() > 0
                    && supplierFormEmail.getText().length() > 0
                    && supplierFormPhone.getText().length() > 0
                    && supplierFormTown.getText().length() > 0
                    && supplierFormCity.getText().length() > 0
                    && supplierFormTaxNumber.getText().length() > 0
                    && supplierFormAddress.getText().length() > 0) {
                brand.setCustomerName(supplierFormName.getText());
                brand.setCustomerEmail(supplierFormEmail.getText());
                brand.setCustomerPhoneNumber(supplierFormPhone.getText());
                brand.setCustomerTown(supplierFormTown.getText());
                brand.setCustomerCity(supplierFormCity.getText());
                brand.setCustomerTaxNumber(supplierFormTaxNumber.getText());
                brand.setCustomerAddress(supplierFormAddress.getText());

                closeDialog(e);

                supplierFormName.setText("");
                supplierFormEmail.setText("");
                supplierFormPhone.setText("");
                supplierFormTown.setText("");
                supplierFormCity.setText("");
                supplierFormTaxNumber.setText("");
                supplierFormAddress.setText("");
            }
        });
    }
}
