package org.infinite.spoty.controller.requisition;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import org.infinite.spoty.model.Requisition;

import java.net.URL;
import java.util.ResourceBundle;

public class RequisitionController implements Initializable {
    @FXML
    public MFXTextField requisitionSearchBar;
    @FXML
    public HBox requisitionActionsPane;
    @FXML
    public MFXButton requisitionImportBtn;
    @FXML
    public MFXTableView<Requisition> requisitionsTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void requisitionCreateBtnClicked() {
        // TODO Auto-generated method stub
    }
}
