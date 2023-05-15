package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.models.RoleMaster;
import org.infinite.spoty.models.User;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;

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
        userFormFirstname.textProperty().addListener((observable, oldValue, newValue) -> userFormFirstname.setTrailingIcon(null));
        userFormLastname.textProperty().addListener((observable, oldValue, newValue) -> userFormLastname.setTrailingIcon(null));
        userFormLastname.textProperty().addListener((observable, oldValue, newValue) -> userFormUsername.setTrailingIcon(null));
//        userFormEmail.textProperty().addListener((observable, oldValue, newValue) -> userFormEmail.setTrailingIcon(null));
//        userFormPhone.textProperty().addListener((observable, oldValue, newValue) -> userFormPhone.setTrailingIcon(null));
//        userFormTown.textProperty().addListener((observable, oldValue, newValue) -> userFormTown.setTrailingIcon(null));
//        userFormCity.textProperty().addListener((observable, oldValue, newValue) -> userFormCity.setTrailingIcon(null));
//        userFormTaxNumber.textProperty().addListener((observable, oldValue, newValue) -> userFormTaxNumber.setTrailingIcon(null));
//        userFormAddress.textProperty().addListener((observable, oldValue, newValue) -> userFormAddress.setTrailingIcon(null));

        dialogOnActions();
    }

    private void dialogOnActions() {
        userFormCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            userFormFirstname.setText("");
            userFormLastname.setText("");
            userFormUsername.setText("");
            userFormEmail.setText("");
            userFormPhone.setText("");
            userFormPassword.setText("");
            userFormRole.setText("");
            userFormBranches.setText("");
            userFormActive.setSelected(true);

            userFormFirstname.setTrailingIcon(null);
            userFormLastname.setTrailingIcon(null);
            userFormUsername.setTrailingIcon(null);
            userFormEmail.setTrailingIcon(null);
            userFormPhone.setTrailingIcon(null);
            userFormPassword.setLeadingIcon(null);
            userFormRole.setLeadingIcon(null);
            userFormBranches.setLeadingIcon(null);
        });
        userFormSaveBtn.setOnAction((e) -> {
            User brand = new User();
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (userFormFirstname.getText().length() == 0) {
                userFormFirstname.setTrailingIcon(icon);
            }
            if (userFormLastname.getText().length() == 0) {
                userFormLastname.setTrailingIcon(icon);
            }
            if (userFormUsername.getText().length() == 0) {
                userFormUsername.setTrailingIcon(icon);
            }
//            if (userFormEmail.getText().length() == 0) {
//                userFormEmail.setTrailingIcon(icon);
//            }
//            if (userFormPhone.getText().length() == 0) {
//                userFormPhone.setTrailingIcon(icon);
//            }
            // Should be set by user i.e.
            // User is created by admin, User tries to log in and is asked to set a password.
            if (userFormPassword.getText().length() == 0) {
                userFormPassword.setLeadingIcon(icon);
            }
            if (userFormRole.getText().length() == 0) {
                userFormRole.setLeadingIcon(icon);
            }
            if (userFormBranches.getText().length() == 0) {
                userFormBranches.setLeadingIcon(icon);
            }
            if (userFormFirstname.getText().length() > 0
                    && userFormLastname.getText().length() > 0
                    && userFormUsername.getText().length() > 0
                    && userFormEmail.getText().length() > 0
                    && userFormPhone.getText().length() > 0
                    && userFormPassword.getText().length() > 0
                    && userFormRole.getText().length() > 0
                    && userFormBranches.getText().length() > 0) {
                brand.setUserName(userFormFirstname.getText());
                brand.setUserName(userFormLastname.getText());
                brand.setUserName(userFormUsername.getText());
                brand.setUserEmail(userFormEmail.getText());
                brand.setUserPhoneNumber(userFormPhone.getText());
//                To be set when database included.
//                brand.setUser(userFormPassword.getText());
//                brand.setUserCity(userFormRole.getText());
//                brand.setUserTaxNumber(userFormBranches.getText());

                closeDialog(e);

                userFormFirstname.setText("");
                userFormLastname.setText("");
                userFormUsername.setText("");
                userFormEmail.setText("");
                userFormPhone.setText("");
                userFormPassword.setText("");
                userFormRole.setText("");
                userFormBranches.setText("");
                userFormActive.setSelected(true);
            }
        });
    }
}
