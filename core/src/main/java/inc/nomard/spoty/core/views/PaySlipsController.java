package inc.nomard.spoty.core.views;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.hrm.pay_roll.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxresources.fonts.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import lombok.extern.java.*;

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
    public HBox leftHeaderPane;
    @FXML
    public MFXProgressSpinner progress;

    public void createBtnClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setIcons();
        setSearchBar();
        scrollPane.setNodeOrientation(NodeOrientation.INHERIT);
        try {
            FXMLLoader paySlipItemLoader = fxmlLoader("views/PaySlipItem.fxml");

            HBox paySlipItemBox = paySlipItemLoader.load();

            PaySlipItemController paySlipItem = paySlipItemLoader.getController();
//            paySlipItem.setData(quickStat);

            paySlipItemHolder.getChildren().addAll(paySlipItemBox);
        } catch (IOException e) {
            SpotyLogger.writeToFile(e, this.getClass());
        }
    }

    private void setIcons() {
        searchBar.setTrailingIcon(new MFXFontIcon("fas-magnifying-glass"));
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                PaySlipViewModel.getAllPaySlips(null, null);
            }
            progress.setVisible(true);
            PaySlipViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
            }, this::errorMessage);
        });
    }

    private void onSuccess() {
        PaySlipViewModel.getAllPaySlips(null, null);
    }

    private void errorMessage(String message) {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
        notificationHolder.addMessage(notification);
        AnchorPane.setRightAnchor(notification, 40.0);
        AnchorPane.setTopAnchor(notification, 10.0);
    }
}
