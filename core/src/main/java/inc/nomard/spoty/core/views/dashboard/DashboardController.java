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
import inc.nomard.spoty.utils.responsiveness.layouts.BootstrapColumn;
import inc.nomard.spoty.utils.responsiveness.layouts.BootstrapPane;
import inc.nomard.spoty.utils.responsiveness.layouts.BootstrapRow;
import inc.nomard.spoty.utils.responsiveness.layouts.Breakpoint;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.fxmlLoader;

public class DashboardController implements Initializable {
    @FXML
    public MFXScrollPane scrollPane;

    @FXML
    private BootstrapPane quickStatsContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaX() != 0) {
                event.consume();
            }
        });
        scrollPane.widthProperty().addListener((obs, oV, nV) -> {
            quickStatsContainer.setMaxWidth(nV.doubleValue() - 10);
            quickStatsContainer.setPrefWidth(nV.doubleValue() - 10);
            quickStatsContainer.setMinWidth(nV.doubleValue() - 10);
        });

        List<QuickStats> quickStatsList = new ArrayList<>(SampleData.quickStatsSampleData());
        var row1 = new BootstrapRow();
        var row2 = new BootstrapRow();
        var row3 = new BootstrapRow();
        quickStatsContainer.setAlignment(Pos.CENTER_LEFT);
        quickStatsContainer.setPadding(new Insets(10));

        for (QuickStats quickStat : quickStatsList) {
            try {
                FXMLLoader cardLoader = fxmlLoader("components/cards/SmallCard.fxml");
                AnchorPane smallCardPane = cardLoader.load();

                SmallCardController smallCard = cardLoader.getController();
                smallCard.setData(quickStat);

                var column = new BootstrapColumn(smallCardPane);
                column.setBreakpointColumnWidth(Breakpoint.LARGE, 3);
                column.setBreakpointColumnWidth(Breakpoint.SMALL, 6);
                column.setBreakpointColumnWidth(Breakpoint.XSMALL, 12);

                row1.addColumn(column);
            } catch (IOException e) {
                SpotyLogger.writeToFile(e, this.getClass());
            }
        }

        try {
            AnchorPane financialProjectionGraph = fxmlLoader("components/cards/GraphCard.fxml").load();

            var column1 = new BootstrapColumn(financialProjectionGraph);
            column1.setBreakpointColumnWidth(Breakpoint.LARGE, 12);
            column1.setBreakpointColumnWidth(Breakpoint.SMALL, 12);
            column1.setBreakpointColumnWidth(Breakpoint.XSMALL, 12);
            row1.addColumn(column1);

            AnchorPane totalRevenueCardPane = fxmlLoader("components/cards/TotalRevenueCard.fxml").load();
            AnchorPane topProductsCardPane = fxmlLoader("components/cards/TopProductsCard.fxml").load();

            var column2 = new BootstrapColumn(totalRevenueCardPane);
            column2.setBreakpointColumnWidth(Breakpoint.LARGE, 6);
            column2.setBreakpointColumnWidth(Breakpoint.SMALL, 6);
            column2.setBreakpointColumnWidth(Breakpoint.XSMALL, 12);
            var column3 = new BootstrapColumn(topProductsCardPane);
            column3.setBreakpointColumnWidth(Breakpoint.LARGE, 6);
            column3.setBreakpointColumnWidth(Breakpoint.SMALL, 6);
            column3.setBreakpointColumnWidth(Breakpoint.XSMALL, 12);
            row2.addColumn(column2);
            row2.addColumn(column3);

            AnchorPane topCustomersCardPane = fxmlLoader("components/cards/TopCustomersCard.fxml").load();
            AnchorPane topSuppliersCardPane = fxmlLoader("components/cards/TopSuppliersCard.fxml").load();

            var column4 = new BootstrapColumn(topCustomersCardPane);
            column4.setBreakpointColumnWidth(Breakpoint.LARGE, 6);
            column4.setBreakpointColumnWidth(Breakpoint.SMALL, 6);
            column4.setBreakpointColumnWidth(Breakpoint.XSMALL, 12);
            var column5 = new BootstrapColumn(topSuppliersCardPane);
            column5.setBreakpointColumnWidth(Breakpoint.LARGE, 6);
            column5.setBreakpointColumnWidth(Breakpoint.SMALL, 6);
            column5.setBreakpointColumnWidth(Breakpoint.XSMALL, 12);
            row2.addColumn(column4);
            row2.addColumn(column5);

            AnchorPane ordersCardPane = fxmlLoader("components/cards/OrdersCard.fxml").load();

            var column6 = new BootstrapColumn(ordersCardPane);
            column6.setBreakpointColumnWidth(Breakpoint.LARGE, 6);
            column6.setBreakpointColumnWidth(Breakpoint.SMALL, 6);
            column6.setBreakpointColumnWidth(Breakpoint.XSMALL, 12);
            row2.addColumn(column6);
        } catch (IOException e) {
            SpotyLogger.writeToFile(e, this.getClass());
        }
        quickStatsContainer.addRow(row1);
        quickStatsContainer.addRow(row2);
        quickStatsContainer.addRow(row3);
    }
}
