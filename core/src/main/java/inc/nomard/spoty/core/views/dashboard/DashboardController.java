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

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.viewModels.dashboard.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.network_bridge.dtos.dashboard.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.responsiveness.layouts.*;
import io.github.palexdev.materialfx.controls.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import lombok.extern.java.*;

@Log
public class DashboardController implements Initializable {
    @FXML
    public MFXScrollPane scrollPane;

    @FXML
    public BootstrapPane kpisContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaX() != 0) {
                event.consume();
            }
        });
        scrollPane.widthProperty().addListener((obs, oV, nV) -> {
            kpisContainer.setMaxWidth(nV.doubleValue() - 10);
            kpisContainer.setPrefWidth(nV.doubleValue() - 10);
            kpisContainer.setMinWidth(nV.doubleValue() - 10);
        });

        List<DashboardKPIModel> kpisList = new ArrayList<>(DashboardViewModel.getKPIs());
        var row1 = new BootstrapRow();
        var row2 = new BootstrapRow();
        var row3 = new BootstrapRow();
        kpisContainer.setAlignment(Pos.CENTER_LEFT);
        kpisContainer.setPadding(new Insets(10));

        for (DashboardKPIModel kpi : kpisList) {
            try {
                FXMLLoader cardLoader = fxmlLoader("components/cards/SmallCard.fxml");
                AnchorPane smallCardPane = cardLoader.load();

                SmallCardController smallCard = cardLoader.getController();
                smallCard.setData(kpi);

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
        kpisContainer.addRow(row1);
        kpisContainer.addRow(row2);
        kpisContainer.addRow(row3);
    }
}
