package inc.nomard.spoty.core.views.report;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.net.*;
import java.util.*;
import javafx.fxml.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import lombok.extern.slf4j.*;

@Slf4j
public class DueReportController implements Initializable {

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
