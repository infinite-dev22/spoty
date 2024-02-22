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

public class BankReconciliationController implements Initializable {
    public BorderPane bankReconciliationContentPane;
    public MFXTextField bankReconciliationSearchBar;
    public HBox bankReconciliationActionsPane;
    public MFXButton bankReconciliationImportBtn;
    public MFXButton bankReconciliationCreateBtn;
    public MFXTableView bankReconciliationMasterTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void bankReconciliationCreateBtnClicked(MouseEvent mouseEvent) {
    }
}
