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

public class ExpenseCategoryFormController implements Initializable {
    @FXML
    public MFXTextField categoryExpenseFormName;
    @FXML
    public MFXTextField categoryExpenseFormDescription;
    @FXML
    public MFXFilledButton categoryExpenseFormSaveBtn;
    @FXML
    public MFXOutlinedButton categoryExpenseFormCancelBtn;
    @FXML
    public Label categoryExpenseFormTitle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dialogOnActions();
    }

    private void dialogOnActions() {
        categoryExpenseFormCancelBtn.setOnAction((e) -> {
            final Node source = (Node) e.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
        categoryExpenseFormSaveBtn.setOnAction((e) -> {});
    }
}
