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

public class ChartOfAccountController implements Initializable {
    public BorderPane chartOfAccountContentPane;
    public MFXTextField chartOfAccountSearchBar;
    public HBox chartOfAccountActionsPane;
    public MFXButton chartOfAccountImportBtn;
    public MFXButton chartOfAccountCreateBtn;
    public MFXTableView chartOfAccountMasterTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void chartOfAccountCreateBtnClicked(MouseEvent mouseEvent) {
    }
}
