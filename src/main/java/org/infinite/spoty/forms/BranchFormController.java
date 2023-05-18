package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.infinite.spoty.viewModels.BranchFormViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.viewModels.BranchFormViewModel.*;

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

        branchFormTitle.textProperty().bindBidirectional(BranchFormViewModel.titleProperty());
        branchFormName.textProperty().bindBidirectional(BranchFormViewModel.nameProperty());
        branchFormEmail.textProperty().bindBidirectional(BranchFormViewModel.emailProperty());
        branchFormPhone.textProperty().bindBidirectional(BranchFormViewModel.phoneProperty());
        branchFormTown.textProperty().bindBidirectional(BranchFormViewModel.townProperty());
        branchFormCity.textProperty().bindBidirectional(BranchFormViewModel.cityProperty());
        branchFormZipCode.textProperty().bindBidirectional(BranchFormViewModel.zipcodeProperty());
    }

    private void dialogOnActions() {
        branchFormCancelBtn.setOnAction((e) -> {
            clearBranchData();
            closeDialog(e);
        });
        branchFormSaveBtn.setOnAction((e) -> {
            saveBranch();
            closeDialog(e);
        });
    }
}
