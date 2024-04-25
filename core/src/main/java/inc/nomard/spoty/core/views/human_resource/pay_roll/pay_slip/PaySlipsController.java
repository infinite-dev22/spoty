package inc.nomard.spoty.core.views.human_resource.pay_roll.pay_slip;

import inc.nomard.spoty.core.components.animations.SpotyAnimations;
import inc.nomard.spoty.core.viewModels.hrm.employee.EmploymentStatusViewModel;
import inc.nomard.spoty.utils.SpotyLogger;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.fxmlLoader;

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
