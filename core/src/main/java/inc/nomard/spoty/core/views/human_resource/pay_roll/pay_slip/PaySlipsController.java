package inc.nomard.spoty.core.views.human_resource.pay_roll.pay_slip;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
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

    public void createBtnClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

    private void onSuccess() {
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
