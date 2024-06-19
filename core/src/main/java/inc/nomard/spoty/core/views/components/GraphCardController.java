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

package inc.nomard.spoty.core.views.components;

import eu.hansolo.fx.charts.*;
import eu.hansolo.fx.charts.data.*;
import eu.hansolo.fx.charts.series.*;
import inc.nomard.spoty.core.components.*;
import inc.nomard.spoty.core.viewModels.dashboard.*;
import inc.nomard.spoty.network_bridge.dtos.dashboard.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class GraphCardController implements Initializable {
    private static final Random RND = new Random();
    private static final Double AXIS_WIDTH = 25d;
    private final Color expenseColor = Color.web("#00AEF5");
    private final Color incomeColor = Color.web("#4EE29B");
    private final Color strokeSymbolColor = Color.web("#293C47");
    @FXML
    public Circle incomeColorIndicator;
    @FXML
    public Circle expenseColorIndicator;
    @FXML
    public ViewAll viewAll;
    @FXML
    public Label cardTitle;
    @FXML
    public AnchorPane lineChartHolder;
    private Axis xAxisBottom, yAxisLeft;
    private XYChart lineChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cardTitle.setText("Financial Projections");

        expenseColorIndicator.setFill(expenseColor);
        incomeColorIndicator.setFill(incomeColor);

        createLeftAxes();
        createBottomAxes();
        buildLineChart();
        startCharting();
        createActions();
    }

    private void buildLineChart() {
        XYPane lineChartPane = new XYPane(expenseSeries(), incomeSeries());

        lineChart = new XYChart<>(lineChartPane, createGrids(), yAxisLeft, xAxisBottom);
        AnchorPane.setTopAnchor(lineChart, 0d);
        AnchorPane.setBottomAnchor(lineChart, 0d);
        AnchorPane.setLeftAnchor(lineChart, 0d);
        AnchorPane.setRightAnchor(lineChart, 0d);
    }

    private void createBottomAxes() {
        xAxisBottom = AxisBuilder.create(Orientation.HORIZONTAL, Position.BOTTOM)
                .type(AxisType.TEXT)
                .prefHeight(AXIS_WIDTH)
                .categories("", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
                .minValue(1)
                .maxValue(13)
                .axisColor(Color.TRANSPARENT)
                .tickLabelFontSize(13)
                .minorTickMarksVisible(false)
                .mediumTickMarksVisible(false)
                .majorTickMarksVisible(false)
                .build();
        AnchorPane.setBottomAnchor(xAxisBottom, 0d);
        AnchorPane.setLeftAnchor(xAxisBottom, AXIS_WIDTH);
        AnchorPane.setRightAnchor(xAxisBottom, AXIS_WIDTH);
    }

    private void createLeftAxes() {
        yAxisLeft = AxisBuilder.create(Orientation.VERTICAL, Position.LEFT)
                .type(AxisType.LINEAR)
                .prefWidth(AXIS_WIDTH)
                .minValue(0)
                .maxValue(800)
                .axisColor(Color.TRANSPARENT)
                .tickLabelFontSize(13)
                .minorTickMarksVisible(false)
                .mediumTickMarksVisible(false)
                .majorTickMarksVisible(false)
                .numberFormatter(new StringConverter<Number>() {
                    private final DecimalFormat df = new DecimalFormat("$#####0 K");

                    @Override
                    public String toString(Number object) {
                        if (object == null) {
                            return "";
                        }
                        return df.format(object);
                    }

                    @Override
                    public Number fromString(String string) {
                        try {
                            if (string == null) {
                                return null;
                            }
                            string = string.trim();
                            if (string.length() < 1) {
                                return null;
                            }
                            return df.parse(string).doubleValue();
                        } catch (ParseException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                })
                .build();

        // test the new numberFormatter as well
        yAxisLeft.setMinMax(RND.nextDouble() * 300, 1000 - RND.nextDouble() * 300);

        AnchorPane.setTopAnchor(yAxisLeft, 0d);
        AnchorPane.setBottomAnchor(yAxisLeft, AXIS_WIDTH);
        AnchorPane.setLeftAnchor(yAxisLeft, 0d);
    }

    private Grid createGrids() {
        return GridBuilder.create(xAxisBottom, yAxisLeft)
                .gridLinePaint(Color.web("#c4c4c4"))
                .minorHGridLinesVisible(false)
                .mediumHGridLinesVisible(false)
                .minorVGridLinesVisible(false)
                .mediumVGridLinesVisible(false)
                .majorVGridLinesVisible(false)
                .gridOpacity(.0)
                .gridLineDashes(6, 6)
                .build();
    }

    private XYSeries expenseSeries() {
        var plots = new LinkedList<XYItem>();
        var i = 1;
        for (LineChartModel data : DashboardViewModel.getExpenses()) {
            XYChartItem item = new XYChartItem(i, data.getTotalValue(), data.getPeriod(), data.getTotalValue() + " " + data.getPeriod());
            item.setY(RND.nextDouble() * 500 + 200);
            plots.add(item);
            i++;
        }

        XYSeries xySeries1 = XYSeriesBuilder.create()
                .items(plots)
                .chartType(ChartType.SMOOTH_AREA)
//                .fill(Color.web("#00AEF520"))
                .stroke(expenseColor)
                .symbolFill(expenseColor)
                .symbolStroke(strokeSymbolColor)
                .symbolSize(10)
                .strokeWidth(3)
                .symbolsVisible(true)
                .build();
        return xySeries1;
    }

    private XYSeries incomeSeries() {
        var plots = new LinkedList<XYItem>();
        var i = 1;
        for (LineChartModel data : DashboardViewModel.getExpenses()) {
            XYChartItem item = new XYChartItem(i, data.getTotalValue(), data.getPeriod(), data.getTotalValue() + " " + data.getPeriod());
            item.setY(RND.nextDouble() * 500 + 200);
            plots.add(item);
            i++;
        }

        XYSeries xySeries2 = XYSeriesBuilder.create()
                .items(plots)
                .chartType(ChartType.SMOOTH_AREA)
//                .fill(Color.web("#4EE29B20"))
                .stroke(incomeColor)
                .symbolFill(incomeColor)
                .symbolStroke(strokeSymbolColor)
                .symbolSize(10)
                .strokeWidth(3)
                .symbolsVisible(true)
                .build();
        return xySeries2;
    }

    public void startCharting() {
        lineChartHolder.getChildren().add(lineChart);
        lineChartHolder.setPadding(new Insets(10));
    }

    private void createActions() {
        viewAll.setOnMouseClicked(mouseEvent -> System.out.println("View All clicked"));
    }
}
