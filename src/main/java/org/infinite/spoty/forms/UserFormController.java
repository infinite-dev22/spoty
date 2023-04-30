package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.infinite.spoty.model.Branch;
import org.infinite.spoty.model.RoleMaster;

import java.net.URL;
import java.util.ResourceBundle;

public class UserFormController implements Initializable {
    @FXML
    public MFXFilledButton userFormSaveBtn;
    @FXML
    public MFXOutlinedButton userFormCancelBtn;
    @FXML
    public Label userFormTitle;
    @FXML
    public MFXTextField userFormEmail;
    @FXML
    public MFXTextField userFormPhone;
    @FXML
    public MFXTextField userFormFirstname;
    @FXML
    public MFXTextField userFormLastname;
    @FXML
    public MFXTextField userFormUsername;
    @FXML
    public MFXPasswordField userFormPassword;
    @FXML
    public MFXFilterComboBox<RoleMaster> userFormRole;
    @FXML
    public MFXFilterComboBox<Branch> userFormBranches;
    @FXML
    public MFXToggleButton userFormActive;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dialogOnActions();
    }

    private void dialogOnActions() {
        userFormCancelBtn.setOnAction((e) -> {
            final Node source = (Node) e.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
        userFormSaveBtn.setOnAction((e) -> {
        });
    }
}
