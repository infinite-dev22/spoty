package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.infinite.spoty.viewModels.BranchViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.viewModels.BranchViewModel.*;

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

        branchFormTitle.textProperty().bindBidirectional(BranchViewModel.titleProperty());
        branchFormName.textProperty().bindBidirectional(BranchViewModel.nameProperty());
        branchFormEmail.textProperty().bindBidirectional(BranchViewModel.emailProperty());
        branchFormPhone.textProperty().bindBidirectional(BranchViewModel.phoneProperty());
        branchFormTown.textProperty().bindBidirectional(BranchViewModel.townProperty());
        branchFormCity.textProperty().bindBidirectional(BranchViewModel.cityProperty());
        branchFormZipCode.textProperty().bindBidirectional(BranchViewModel.zipcodeProperty());
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
