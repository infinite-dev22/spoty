package inc.nomard.spoty.core.views.components;

import inc.nomard.spoty.core.viewModels.dashboard.DashboardViewModel;
import inc.nomard.spoty.core.views.layout.navigation.Spacer;
import inc.nomard.spoty.network_bridge.dtos.dashboard.LineChartModel;
import inc.nomard.spoty.utils.UIUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GraphCard extends AnchorPane {
    private static final Double AXIS_WIDTH = 25d;
    private static final Double AXIS_HEIGHT = 25d;
    private final Color expenseColor = Color.web("#00AEF5");
    private final Color incomeColor = Color.web("#4EE29B");
    private final Color strokeSymbolColor = Color.web("#293C47");
    private final CategoryAxis xAxis = new CategoryAxis();
    private final NumberAxis yAxis = new NumberAxis();

    public GraphCard() {
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

    private LineChart<String, Number> buildLineChart() {
        var chart = new LineChart<>(xAxis, yAxis);
        chart.setData(getChartData());
        chart.setLegendVisible(false);
        chart.setAnimated(true);
        UIUtils.anchor(chart, 0d, 0d, 0d, 0d);
        for (XYChart.Series<String, Number> s : chart.getData()) {
            for (XYChart.Data<String, Number> d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip(
                        d.getXValue() + "\n" + d.getYValue()));
                d.getNode().setOnMouseEntered(event -> d.getNode().setCursor(Cursor.HAND));
                d.getNode().setOnMouseExited(event -> d.getNode().setCursor(Cursor.HAND));
            }
        }
        return chart;
    }

    private ObservableList<XYChart.Series<String, Number>> getChartData() {
        ObservableList<XYChart.Series<String, Number>> series = FXCollections
                .observableArrayList();
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        for (LineChartModel data : DashboardViewModel.getExpenses()) {
            expenseSeries.getData().add(new XYChart.Data<>(data.getPeriod(), data.getTotalValue()));
        }
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        for (LineChartModel data : DashboardViewModel.getIncomes()) {
            incomeSeries.getData().add(new XYChart.Data<>(data.getPeriod(), data.getTotalValue()));
        }
        series.addAll(expenseSeries, incomeSeries);
        return series;
    }
}
