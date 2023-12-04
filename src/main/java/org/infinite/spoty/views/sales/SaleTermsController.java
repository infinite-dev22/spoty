package org.infinite.spoty.views.sales;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SaleTermsController implements Initializable {
    public BorderPane saleTermContentPane;
    public MFXTextField saleTermSearchBar;
    public HBox saleTermActionsPane;
    public MFXButton saleTermImportBtn;
    public MFXButton saleTermCreateBtn;
    public MFXTableView saleTermMasterTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void saleTermCreateBtnClicked(MouseEvent mouseEvent) {
    }
}
