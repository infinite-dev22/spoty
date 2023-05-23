package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.infinite.spoty.viewModels.SupplierVewModel;

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
    public MFXTextField supplierFormCity;
    @FXML
    public MFXTextField supplierFormCountry;
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

        supplierFormName.textProperty().bindBidirectional(SupplierVewModel.nameProperty());
        supplierFormEmail.textProperty().bindBidirectional(SupplierVewModel.emailProperty());
        supplierFormPhone.textProperty().bindBidirectional(SupplierVewModel.phoneProperty());
        supplierFormCity.textProperty().bindBidirectional(SupplierVewModel.cityProperty());
        supplierFormCountry.textProperty().bindBidirectional(SupplierVewModel.countryProperty());
        supplierFormTaxNumber.textProperty().bindBidirectional(SupplierVewModel.taxNumberProperty());
        supplierFormAddress.textProperty().bindBidirectional(SupplierVewModel.addressProperty());

        dialogOnActions();
    }

    private void dialogOnActions() {
        supplierFormCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            SupplierVewModel.resetProperties();

            supplierFormName.setTrailingIcon(null);
            supplierFormEmail.setTrailingIcon(null);
            supplierFormPhone.setTrailingIcon(null);
            supplierFormCity.setTrailingIcon(null);
            supplierFormCountry.setTrailingIcon(null);
            supplierFormTaxNumber.setTrailingIcon(null);
            supplierFormAddress.setTrailingIcon(null);
        });
        supplierFormSaveBtn.setOnAction((e) -> {
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
            if (supplierFormCity.getText().length() == 0) {
                supplierFormCity.setTrailingIcon(icon);
            }
            if (supplierFormCountry.getText().length() == 0) {
                supplierFormCountry.setTrailingIcon(icon);
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
                    && supplierFormCity.getText().length() > 0
                    && supplierFormCountry.getText().length() > 0
                    && supplierFormTaxNumber.getText().length() > 0
                    && supplierFormAddress.getText().length() > 0) {
                SupplierVewModel.saveSupplier();
                SupplierVewModel.resetProperties();
                closeDialog(e);
            }
        });
    }
}
