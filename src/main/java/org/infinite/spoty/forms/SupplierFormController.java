package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

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
    }

    private void dialogOnActions() {
        supplierFormCancelBtn.setOnAction((e) -> {
            final Node source = (Node) e.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
        supplierFormSaveBtn.setOnAction((e) -> {
        });
    }
}
