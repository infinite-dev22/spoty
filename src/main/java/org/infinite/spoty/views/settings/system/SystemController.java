package org.infinite.spoty.views.settings.system;

import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import org.infinite.spoty.models.Branch;
import org.infinite.spoty.models.Currency;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SystemController implements Initializable {
    @FXML
    public MFXComboBox<Currency> defaultCurrency;
    @FXML
    public MFXComboBox<?> defaultLanguage;
    @FXML
    public MFXTextField defaultEmail;
    @FXML
    public MFXTextField companyName;
    @FXML
    public MFXTextField companyPhone;
    @FXML
    public MFXComboBox<Branch> defaultBranch;
    @FXML
    public MFXTextField branchAddress;
    @FXML
    public MFXCheckbox invoiceFooter;
    @FXML
    public GridPane systemSettings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpeg", "*.gif", "*.png", "*.jpg"));
        File res = fileChooser.showOpenDialog(systemSettings.getScene().getWindow());
        System.out.println(res);
    }
}
