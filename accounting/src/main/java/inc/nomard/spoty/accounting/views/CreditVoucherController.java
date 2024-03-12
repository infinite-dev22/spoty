package inc.nomard.spoty.accounting.views;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class CreditVoucherController implements Initializable {
    public BorderPane creditVoucherContentPane;
    public MFXTextField creditVoucherSearchBar;
    public HBox creditVoucherActionsPane;
    public MFXButton creditVoucherImportBtn;
    public MFXButton creditVoucherCreateBtn;
    public MFXTableView creditVoucherMasterTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void creditVoucherCreateBtnClicked(MouseEvent mouseEvent) {
    }
}
