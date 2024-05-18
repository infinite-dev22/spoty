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
        XYPane lineChartPane = new XYPane(createSeries1(), createSeries2());

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

    private XYSeries createSeries1() {
        var t1 = RND.nextDouble() * 500 + 200;
        var t2 = RND.nextDouble() * 500 + 200;
        var t3 = RND.nextDouble() * 500 + 200;
        var t4 = RND.nextDouble() * 500 + 200;
        var t5 = RND.nextDouble() * 500 + 200;
        var t6 = RND.nextDouble() * 500 + 200;
        var t7 = RND.nextDouble() * 500 + 200;
        var t8 = RND.nextDouble() * 500 + 200;
        var t9 = RND.nextDouble() * 500 + 200;
        var t10 = RND.nextDouble() * 500 + 200;
        var t11 = RND.nextDouble() * 500 + 200;
        var t12 = RND.nextDouble() * 500 + 200;

        XYChartItem p1 = new XYChartItem(1, t1, "Jan", t1 + "\tJanuary");
        XYChartItem p2 = new XYChartItem(2, t2, "Feb", t2 + "\tFebruary");
        XYChartItem p3 = new XYChartItem(3, t3, "Mar", t3 + "\tMarch");
        XYChartItem p4 = new XYChartItem(4, t4, "Apr", t4 + "\tApril");
        XYChartItem p5 = new XYChartItem(5, t5, "May", t5 + "\tMay");
        XYChartItem p6 = new XYChartItem(6, t6, "Jun", t6 + "\tJune");
        XYChartItem p7 = new XYChartItem(7, t7, "Jul", t7 + "\tJuly");
        XYChartItem p8 = new XYChartItem(8, t8, "Aug", t8 + "\tAugust");
        XYChartItem p9 = new XYChartItem(9, t9, "Sep", t9 + "\tSeptember");
        XYChartItem p10 = new XYChartItem(10, t10, "Oct", t10 + "\tOctober");
        XYChartItem p11 = new XYChartItem(11, t11, "Nov", t11 + "\tNovember");
        XYChartItem p12 = new XYChartItem(12, t12, "Dec", t12 + "\tDecember");


        p1.setY(RND.nextDouble() * 500 + 200);
        p2.setY(RND.nextDouble() * 500 + 200);
        p3.setY(RND.nextDouble() * 500 + 200);
        p4.setY(RND.nextDouble() * 500 + 200);
        p5.setY(RND.nextDouble() * 500 + 200);
        p6.setY(RND.nextDouble() * 500 + 200);
        p7.setY(RND.nextDouble() * 500 + 200);
        p8.setY(RND.nextDouble() * 500 + 200);
        p9.setY(RND.nextDouble() * 500 + 200);
        p10.setY(RND.nextDouble() * 500 + 200);
        p11.setY(RND.nextDouble() * 500 + 200);
        p12.setY(RND.nextDouble() * 500 + 200);

        XYSeries xySeries1 = XYSeriesBuilder.create()
                .items(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12)
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

    private XYSeries createSeries2() {
        var t1 = RND.nextDouble() * 500 + 200;
        var t2 = RND.nextDouble() * 500 + 200;
        var t3 = RND.nextDouble() * 500 + 200;
        var t4 = RND.nextDouble() * 500 + 200;
        var t5 = RND.nextDouble() * 500 + 200;
        var t6 = RND.nextDouble() * 500 + 200;
        var t7 = RND.nextDouble() * 500 + 200;
        var t8 = RND.nextDouble() * 500 + 200;
        var t9 = RND.nextDouble() * 500 + 200;
        var t10 = RND.nextDouble() * 500 + 200;
        var t11 = RND.nextDouble() * 500 + 200;
        var t12 = RND.nextDouble() * 500 + 200;

        XYSeries xySeries2 = XYSeriesBuilder.create()
                .items(new XYChartItem(1, t1, "Jan", t1 + "\tJanuary"),
                        new XYChartItem(2, t2, "Feb", t2 + "\tFebruary"),
                        new XYChartItem(3, t3, "Mar", t3 + "\tMarch"),
                        new XYChartItem(4, t4, "Apr", t4 + "\tApril"),
                        new XYChartItem(5, t5, "May", t5 + "\tMay"),
                        new XYChartItem(6, t6, "Jun", t6 + "\tJune"),
                        new XYChartItem(7, t7, "Jul", t7 + "\tJuly"),
                        new XYChartItem(8, t8, "Aug", t8 + "\tAugust"),
                        new XYChartItem(9, t9, "Sep", t9 + "\tSeptember"),
                        new XYChartItem(10, t10, "Oct", t10 + "\tOctober"),
                        new XYChartItem(11, t11, "Nov", t11 + "\tNovember"),
                        new XYChartItem(12, t12, "Dec", t12 + "\tDecember"))
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
