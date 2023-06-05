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
import org.infinite.spoty.viewModels.CustomerViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form input listener.
        customerFormName.textProperty().addListener((observable, oldValue, newValue) -> customerFormName.setStyle("-fx-border-color: red:"));
        // Form input binding.
        customerID.textProperty().bindBidirectional(CustomerViewModel.idProperty(), new NumberStringConverter());
        customerFormName.textProperty().bindBidirectional(CustomerViewModel.nameProperty());
        customerFormEmail.textProperty().bindBidirectional(CustomerViewModel.emailProperty());
        customerFormPhone.textProperty().bindBidirectional(CustomerViewModel.phoneProperty());
        customerFormCity.textProperty().bindBidirectional(CustomerViewModel.cityProperty());
        customerFormCountry.textProperty().bindBidirectional(CustomerViewModel.countryProperty());
        customerFormTaxNumber.textProperty().bindBidirectional(CustomerViewModel.taxNumberProperty());
        customerFormAddress.textProperty().bindBidirectional(CustomerViewModel.addressProperty());

        dialogOnActions();
    }

    private void dialogOnActions() {
        customerFormCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            CustomerViewModel.resetProperties();

            customerFormName.setStyle("-fx-border-color: red:");
        });
        customerFormSaveBtn.setOnAction((e) -> {
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (customerFormName.getText().length() == 0) {
                customerFormName.setStyle("-fx-border-color: red:");
            }
            if (customerFormName.getText().length() > 0) {
                if (Integer.parseInt(customerID.getText()) > 0)
                    CustomerViewModel.updateItem(Integer.parseInt(customerID.getText()));
                else
                    CustomerViewModel.saveCustomer();
                CustomerViewModel.resetProperties();
                closeDialog(e);
            }
        });
    }
}
