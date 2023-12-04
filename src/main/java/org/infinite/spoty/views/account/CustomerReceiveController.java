package org.infinite.spoty.views.account;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerReceiveController implements Initializable {


    public BorderPane customerReceiveContentPane;
    public MFXTextField customerReceiveSearchBar;
    public HBox customerReceiveActionsPane;
    public MFXButton customerReceiveImportBtn;
    public MFXButton customerReceiveCreateBtn;
    public MFXTableView customerReceiveMasterTable;

    public void customerReceiveCreateBtnClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
