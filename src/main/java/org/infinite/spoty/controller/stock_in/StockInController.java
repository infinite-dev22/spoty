package org.infinite.spoty.controller.stock_in;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class StockInController implements Initializable {
    @FXML
    public MFXTextField stockInSearchBar;
    @FXML
    public HBox stockInActionsPane;
    @FXML
    public MFXButton stockInImportBtn;
    @FXML
    public MFXTableView<?> stockInTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void stockInCreateBtnClicked() {
        // TODO Auto-generated method stub
    }
}
