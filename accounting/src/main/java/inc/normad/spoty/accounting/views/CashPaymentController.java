package inc.normad.spoty.accounting.views;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class CashPaymentController implements Initializable {
    @FXML
    public BorderPane cashPaymentContentPane;
    @FXML
    public MFXTextField cashPaymentSearchBar;
    @FXML
    public HBox cashPaymentActionsPane;
    @FXML
    public MFXButton cashPaymentImportBtn;
    @FXML
    public MFXButton cashPaymentCreateBtn;
    @FXML
    public MFXTableView cashPaymentMasterTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void cashPaymentCreateBtnClicked(MouseEvent mouseEvent) {
    }
}
