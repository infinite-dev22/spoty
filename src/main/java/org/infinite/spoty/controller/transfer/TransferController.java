package org.infinite.spoty.controller.transfer;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TransferController implements Initializable {
    @FXML
    public MFXTextField transferSearchBar;
    @FXML
    public HBox transferActionsPane;
    @FXML
    public MFXButton transferImportBtn;
    @FXML
    public MFXTableView<?> transferTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void transferCreateBtnClicked() {
        // TODO Auto-generated method stub
    }
}
