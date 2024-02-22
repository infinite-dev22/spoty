package org.infinite.spoty.views.human_resource.pay_roll.pay_slip;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.infinite.spoty.utils.SpotyLogger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotyResourceLoader.fxmlLoader;

public class PaySlipsController implements Initializable {

    public BorderPane contentPane;
    public MFXTextField searchBar;
    public HBox actionsPane;
    public MFXButton importBtn;
    public MFXButton createBtn;
    public VBox paySlipItemHolder;
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
}
