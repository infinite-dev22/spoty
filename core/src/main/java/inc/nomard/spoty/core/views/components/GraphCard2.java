package inc.nomard.spoty.core.views.components;

import eu.hansolo.fx.charts.*;
import eu.hansolo.fx.charts.data.XYChartItem;
import eu.hansolo.fx.charts.data.XYItem;
import eu.hansolo.fx.charts.series.XYSeries;
import eu.hansolo.fx.charts.series.XYSeriesBuilder;
import inc.nomard.spoty.core.viewModels.dashboard.DashboardViewModel;
import inc.nomard.spoty.network_bridge.dtos.dashboard.LineChartModel;
import inc.nomard.spoty.utils.UIUtils;
import inc.nomard.spoty.utils.navigation.Spacer;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.StringConverter;
import lombok.extern.log4j.Log4j2;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.LinkedList;

@Log4j2
public class GraphCard2 extends AnchorPane {
    private static final Double AXIS_WIDTH = 25d;
    private static final Double AXIS_HEIGHT = 25d;
    private final Color expenseColor = Color.web("#00AEF5");
    private final Color incomeColor = Color.web("#4EE29B");
    private final Color strokeSymbolColor = Color.web("#293C47");
    private Axis xAxisBottom, yAxisLeft;

    public GraphCard2() {
        this.setMinHeight(351d);
        this.setPrefHeight(551d);
        this.setMaxHeight(751d);
        this.getStyleClass().add("card-flat");
        this.getChildren().addAll(buildTop(), buildBottom());
    }

    private HBox buildTop() {
        var hbox = new HBox();
        hbox.setSpacing(20);
        hbox.setPadding(new Insets(5d));
        hbox.getChildren().addAll(
                buildTitle(),
                buildIndicator(incomeColor, "Incomes"),
                buildIndicator(expenseColor, "Expenses"),
                new Spacer()
        );
        UIUtils.anchor(hbox, 0d, 0d, null, 0d);
        return hbox;
    }

    private Label buildTitle() {
        var label = new Label("Financial Projections");
        label.getStyleClass().add("card-title");
        UIUtils.anchor(label, 0d, null, 0d, 0d);
        return label;
    }

    private HBox buildIndicator(Paint color, String txt) {
        var hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
        var circle = new Circle();
        circle.setRadius(8);
        circle.setFill(color);
        var label = new Label(txt);
        label.getStyleClass().add("card-sub-title");
        hbox.getChildren().addAll(circle, label);
        return hbox;
    }

    private AnchorPane buildBottom() {
        var pane = new AnchorPane();
        pane.getChildren().add(buildLineChart());
        pane.setPadding(new Insets(10));
        UIUtils.anchor(pane, 32d, 0d, 0d, 0d);
        return pane;
    }

    private XYChart<XYChartItem> buildLineChart() {
        var lineChartPane = new XYPane<>(expenseSeries(), incomeSeries());
        createBottomAxes();
        createLeftAxes();
        var lineChart = new XYChart<>(lineChartPane, createGrids(), yAxisLeft, xAxisBottom);
        UIUtils.anchor(lineChart, 0d, 0d, 0d, 0d);
        return lineChart;
    }

    private void createBottomAxes() {
        xAxisBottom = AxisBuilder.create(Orientation.HORIZONTAL, Position.BOTTOM)
                .type(AxisType.TEXT)
                .prefHeight(AXIS_HEIGHT)
                .categories("", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
                .minValue(1)
                .maxValue(13)
//                .autoScale(true)
                .axisColor(Color.TRANSPARENT)
                .minorTickMarksVisible(false)
                .mediumTickMarksVisible(true)
                .majorTickMarksVisible(true)
                .build();
        AnchorPane.setBottomAnchor(xAxisBottom, 0d);
        AnchorPane.setLeftAnchor(xAxisBottom, AXIS_WIDTH);
        AnchorPane.setRightAnchor(xAxisBottom, AXIS_WIDTH);
        UIUtils.anchor(xAxisBottom, null, AXIS_WIDTH, 0d, AXIS_WIDTH);
    }

    private void createLeftAxes() {
        yAxisLeft = AxisBuilder.create(Orientation.VERTICAL, Position.LEFT)
                .type(AxisType.LINEAR)
                .prefWidth(AXIS_WIDTH)
                .autoScale(true)
                .axisColor(Color.TRANSPARENT)
                .minorTickMarksVisible(false)
                .mediumTickMarksVisible(false)
                .majorTickMarksVisible(false)
                .numberFormatter(new StringConverter<Number>() {
                    private final DecimalFormat df = new DecimalFormat("$##,###,###,###,###,###,###,###,###,###,###");

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

        AnchorPane.setTopAnchor(yAxisLeft, 0d);
        AnchorPane.setBottomAnchor(yAxisLeft, AXIS_WIDTH);
        AnchorPane.setLeftAnchor(yAxisLeft, 0d);
        UIUtils.anchor(yAxisLeft, 0d, null, AXIS_HEIGHT, 0d);
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

    private XYSeries<XYChartItem> expenseSeries() {
        var plots = new LinkedList<XYItem>();
        var i = 1;
        for (LineChartModel data : DashboardViewModel.getExpenses()) {
            var item = new XYChartItem(i, data.getTotalValue(), data.getPeriod(), data.getTotalValue() + " " + data.getPeriod());
            plots.add(item);
            i++;
        }

        var xySeries1 = XYSeriesBuilder.create()
                .items(plots)
                .chartType(ChartType.SMOOTH_AREA)
                .stroke(expenseColor)
                .symbolFill(expenseColor)
                .symbolStroke(strokeSymbolColor)
                .symbolSize(10)
                .strokeWidth(3)
                .symbolsVisible(true)
                .build();
        return xySeries1;
    }

    private XYSeries<XYChartItem> incomeSeries() {
        var plots = new LinkedList<XYItem>();
        var i = 1;
        for (LineChartModel data : DashboardViewModel.getIncomes()) {
            var item = new XYChartItem(i, data.getTotalValue(), data.getPeriod(), data.getTotalValue() + " " + data.getPeriod());
            plots.add(item);
            i++;
        }

        XYSeries<XYChartItem> xySeries2 = XYSeriesBuilder.create()
                .items(plots)
                .chartType(ChartType.SMOOTH_AREA)
                .stroke(incomeColor)
                .symbolFill(incomeColor)
                .symbolStroke(strokeSymbolColor)
                .symbolSize(10)
                .strokeWidth(3)
                .symbolsVisible(true)
                .build();
        return xySeries2;
    }
}
