/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package inc.nomard.spoty.core.views.dashboard;

import inc.nomard.spoty.core.data.SampleData;
import inc.nomard.spoty.core.models.QuickStats;
import inc.nomard.spoty.core.views.components.SmallCardController;
import inc.nomard.spoty.utils.SpotyLogger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.fxmlLoader;

public class DashboardController implements Initializable {

    @FXML
    public HBox graphStatsContainer1;

    @FXML
    public HBox graphStatsContainer2;
    @FXML
    public HBox graphStatsContainer0;

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
                SpotyLogger.writeToFile(e, this.getClass());
            }
        }

        try {
            AnchorPane graphCardPane = fxmlLoader("components/cards/GraphCard.fxml").load();

            graphStatsContainer0.getChildren().add(graphCardPane);

            AnchorPane barGraphCardPane = fxmlLoader("components/cards/BarGraphCard.fxml").load();
            AnchorPane barGraphCardPane0 = fxmlLoader("components/cards/BarGraphCard.fxml").load();

            graphStatsContainer1.getChildren().addAll(barGraphCardPane, barGraphCardPane0);

            AnchorPane graphCardPane1 = fxmlLoader("components/cards/PieChartCard.fxml").load();
            AnchorPane barGraphCardPane1 = fxmlLoader("components/cards/BarGraphCard.fxml").load();

            graphStatsContainer2.getChildren().addAll(graphCardPane1, barGraphCardPane1);
        } catch (IOException e) {
            SpotyLogger.writeToFile(e, this.getClass());
        }
    }
}
