package inc.nomard.spoty.core.views.human_resource.pay_roll.pay_slip;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;

import inc.nomard.spoty.core.components.animations.*;
import inc.nomard.spoty.core.viewModels.hrm.employee.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxresources.fonts.*;

import java.io.*;
import java.net.*;
import java.util.*;

import javafx.animation.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.util.*;

import lombok.extern.java.Log;

@Log
public class PaySlipsController implements Initializable {
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXButton createBtn;
    @FXML
    public VBox paySlipItemHolder;
    @FXML
    public MFXScrollPane scrollPane;
    @FXML
    public HBox refresh;
    private RotateTransition transition;

    public void createBtnClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setIcons();
        scrollPane.setNodeOrientation(NodeOrientation.INHERIT);
        try {
            FXMLLoader paySlipItemLoader = fxmlLoader("views/human_resource/pay_roll/pay_slip/PaySlipItem.fxml");

            HBox paySlipItemBox = paySlipItemLoader.load();

            PaySlipItemController paySlipItem = paySlipItemLoader.getController();
//            paySlipItem.setData(quickStat);

            paySlipItemHolder.getChildren().addAll(paySlipItemBox);
        } catch (IOException e) {
            SpotyLogger.writeToFile(e, this.getClass());
        }
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

        refreshIcon.setOnMouseClicked(mouseEvent -> EmploymentStatusViewModel.getAllEmploymentStatuses(this::onAction, this::onSuccess, this::onFailed));
    }
}
