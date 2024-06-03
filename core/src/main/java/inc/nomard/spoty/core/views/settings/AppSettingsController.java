package inc.nomard.spoty.core.views.settings;

import io.github.palexdev.materialfx.controls.*;
import java.net.*;
import java.util.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import lombok.extern.java.*;

@Log
public class AppSettingsController implements Initializable {
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public TabPane tabbedPane;
    @FXML
    public AnchorPane emailTabContent;

    public void createBtnClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTabbedPane();
    }

    private void initTabbedPane() {
        tabbedPane.setMinWidth(450);
    }
}
