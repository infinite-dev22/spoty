package inc.normad.spoty.accounting.views;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ContraVoucherController implements Initializable {
    public BorderPane contraVoucherContentPane;
    public MFXTextField contraVoucherSearchBar;
    public HBox contraVoucherActionsPane;
    public MFXButton contraVoucherImportBtn;
    public MFXButton contraVoucherCreateBtn;
    public MFXTableView contraVoucherMasterTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void contraVoucherCreateBtnClicked(MouseEvent mouseEvent) {
    }
}
