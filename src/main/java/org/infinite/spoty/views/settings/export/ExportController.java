package org.infinite.spoty.views.settings.export;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ExportController implements Initializable {
    @FXML
    public MFXTextField exportSearchBar;
    @FXML
    public HBox exportActionsPane;
    @FXML
    public MFXButton exportBtn;
    @FXML
    public MFXTableView<?> exportTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
