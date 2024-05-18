package inc.nomard.spoty.core.views.tax;

import inc.nomard.spoty.core.components.animations.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxresources.fonts.*;
import java.net.*;
import java.util.*;
import javafx.animation.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.util.*;

public class TaxSettingsController implements Initializable {
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXButton createBtn;
    @FXML
    public MFXTableView masterTable;
    @FXML
    public HBox refresh;
    private RotateTransition transition;

    public void createBtnClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setIcons();
    }

    private void onAction() {
        transition.playFromStart();
        transition.setOnFinished((ActionEvent event) -> transition.playFromStart());
    }

    private void onSuccess() {
        transition.setOnFinished(null);
    }

    private void onFailed() {
        transition.setOnFinished(null);
    }

    private void setIcons() {
        var refreshIcon = new MFXFontIcon("fas-arrows-rotate", 24);
        refresh.getChildren().addFirst(refreshIcon);

        transition = SpotyAnimations.rotateTransition(refreshIcon, Duration.millis(1000), 360);

//        refreshIcon.setOnMouseClicked(mouseEvent -> TaxSettingViewModel.getAllTaxSettings(this::onAction, this::onSuccess, this::onFailed));
    }
}
