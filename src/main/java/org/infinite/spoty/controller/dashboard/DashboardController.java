package org.infinite.spoty.controller.dashboard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.infinite.spoty.controller.components.SmallCardController;
import org.infinite.spoty.data.SampleData;
import org.infinite.spoty.model.QuickStats;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

public class DashboardController implements Initializable {

    @FXML
    public HBox graphStatsContainer1;

    @FXML
    public HBox graphStatsContainer2;

    @FXML
    private HBox quickStatsContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<QuickStats> quickStatsList = new ArrayList<>(SampleData.quickStatsSampleData());

        for (QuickStats quickStat : quickStatsList) {
            try {
                FXMLLoader cardLoader = fxmlLoader("components/cards/SmallCard.fxml");
                AnchorPane smallCardPane = cardLoader.load();

                SmallCardController smallCard = cardLoader.getController();
                smallCard.setData(quickStat);

                quickStatsContainer.getChildren().add(smallCardPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            AnchorPane graphCardPane = fxmlLoader("components/cards/GraphCard.fxml").load();

//            GraphCardController graphCard = graphCardLoader.getController();
//            smallCard.setData(quickStat);

            graphStatsContainer1.getChildren().add(graphCardPane);

            AnchorPane barGraphCardPane = fxmlLoader("components/cards/BarGraphCard.fxml").load();

//            GraphCardController graphCard = barGraphCardLoader.getController();
//            smallCard.setData(quickStat);

            graphStatsContainer1.getChildren().add(barGraphCardPane);

            AnchorPane graphCardPane1 = fxmlLoader("components/cards/PieChartCard.fxml").load();

            graphStatsContainer2.getChildren().add(graphCardPane1);

            AnchorPane barGraphCardPane1 = fxmlLoader("components/cards/BarGraphCard.fxml").load();

            graphStatsContainer2.getChildren().add(barGraphCardPane1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}