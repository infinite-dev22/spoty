package org.infinite.spoty.controller.dashboard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.infinite.spoty.SpotResourceLoader;
import org.infinite.spoty.controller.components.SmallCardController;
import org.infinite.spoty.data.SampleData;
import org.infinite.spoty.model.QuickStats;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    public HBox graphStatsContainer1;

    @FXML
    public HBox graphStatsContainer2;

    @FXML
    private HBox quickStatsContainer;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<QuickStats> quickStatsList = new ArrayList<>(SampleData.quickStatsSampleData());

        for (QuickStats quickStat : quickStatsList) {
            try {
                FXMLLoader cardLoader = new FXMLLoader(SpotResourceLoader.loadURL("components/cards/SmallCard.fxml"));
                AnchorPane smallCardPane = cardLoader.load();

                SmallCardController smallCard = cardLoader.getController();
                smallCard.setData(quickStat);

                quickStatsContainer.getChildren().add(smallCardPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            FXMLLoader graphCardLoader = new FXMLLoader(SpotResourceLoader.loadURL("components/cards/GraphCard.fxml"));
            AnchorPane graphCardPane = graphCardLoader.load();

//            GraphCardController graphCard = graphCardLoader.getController();
//            smallCard.setData(quickStat);

            graphStatsContainer1.getChildren().add(graphCardPane);

            FXMLLoader barGraphCardLoader = new FXMLLoader(SpotResourceLoader.loadURL("components/cards/BarGraphCard.fxml"));
            AnchorPane barGraphCardPane = barGraphCardLoader.load();

//            GraphCardController graphCard = barGraphCardLoader.getController();
//            smallCard.setData(quickStat);

            graphStatsContainer1.getChildren().add(barGraphCardPane);



            FXMLLoader graphCardLoader1 = new FXMLLoader(SpotResourceLoader.loadURL("components/cards/PieChartCard.fxml"));
            AnchorPane graphCardPane1 = graphCardLoader1.load();

            graphStatsContainer2.getChildren().add(graphCardPane1);

            FXMLLoader barGraphCardLoader1 = new FXMLLoader(SpotResourceLoader.loadURL("components/cards/BarGraphCard.fxml"));
            AnchorPane barGraphCardPane1 = barGraphCardLoader1.load();

            graphStatsContainer2.getChildren().add(barGraphCardPane1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}