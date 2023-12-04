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

public class SupplierPaymentController implements Initializable {

    public BorderPane contentPane;
    public MFXTextField searchBar;
    public HBox actionsPane;
    public MFXButton importBtn;
    public MFXButton createBtn;
    public MFXTableView masterTable;

    public void createBtnClicked(MouseEvent mouseEvent) {
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
