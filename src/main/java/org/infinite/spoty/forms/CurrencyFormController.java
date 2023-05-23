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

public class CurrencyFormController implements Initializable {
    @FXML
    public Label currencyFormTitle;
    @FXML
    public MFXTextField currencyFormName;
    @FXML
    public MFXFilledButton currencyFormSaveBtn;
    @FXML
    public MFXOutlinedButton currencyFormCancelBtn;
    @FXML
    public MFXTextField currencyFormCode;
    @FXML
    public MFXTextField currencyFormSymbol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dialogOnActions();
    }

    private void dialogOnActions() {
        currencyFormCancelBtn.setOnAction((e) -> {
            final Node source = (Node) e.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
        currencyFormSaveBtn.setOnAction((e) -> {
        });
    }
}