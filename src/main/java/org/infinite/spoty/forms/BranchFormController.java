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

public class BranchFormController implements Initializable {
    @FXML
    public MFXFilledButton branchFormSaveBtn;
    @FXML
    public MFXOutlinedButton branchFormCancelBtn;
    @FXML
    public Label branchFormTitle;
    @FXML
    public MFXTextField branchFormName;
    @FXML
    public MFXTextField branchFormEmail;
    @FXML
    public MFXTextField branchFormPhone;
    @FXML
    public MFXTextField branchFormTown;
    @FXML
    public MFXTextField branchFormCity;
    @FXML
    public MFXTextField branchFormZipCode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dialogOnActions();
    }

    private void dialogOnActions() {
        branchFormCancelBtn.setOnAction((e) -> {
            final Node source = (Node) e.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
        branchFormSaveBtn.setOnAction((e) -> {
        });
    }
}
