package inc.nomard.spoty.core.views.components;

import inc.nomard.spoty.core.viewModels.dashboard.*;
import inc.nomard.spoty.network_bridge.dtos.dashboard.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.navigation.*;
import io.github.palexdev.mfxcomponents.controls.buttons.*;
import io.github.palexdev.mfxcomponents.skins.*;
import io.github.palexdev.mfxcore.controls.Label;
import java.util.*;
import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import lombok.extern.java.*;

@Log
public class TotalRevenue extends AnchorPane {
    private static final Random RND = new Random();
    private BarChart<String, Number> chart;
    private MFXSegmentedButton graphViewSegment;
    private MFXSegmentedButtonSkin.MFXSegment monthlyViewSegment,
            weeklyViewSegment;

    public TotalRevenue() {
        this.setMinHeight(351d);
        this.setPrefHeight(551d);
        this.setMaxHeight(751d);
        this.getStyleClass().add("card-flat");
        this.getChildren().addAll(buildTop(), buildBottom());
    }

    private HBox buildTop() {
        var hbox = new HBox();
        hbox.setSpacing(20d);
        hbox.setPadding(new Insets(5d));
        hbox.getChildren().addAll(
                buildTitle(),
                new Spacer(),
                buildTimeFilter()
        );
        UIUtils.anchor(hbox, 0d, 0d, null, 0d);
        return hbox;
    }

    private Label buildTitle() {
        var label = new Label("Total Revenue");
        label.getStyleClass().add("card-title");
        UIUtils.anchor(label, 0d, null, 0d, 0d);
        return label;
    }

    private HBox buildTimeFilter() {
        var hbox = new HBox();
        hbox.setSpacing(20d);

//        var toggleGroup = new ToggleGroup();
//        var monthlyViewBtn = new MFXRectangleToggleNode("Monthly");
//        var weeklyViewBtn = new MFXRectangleToggleNode("Weekly");
//        monthlyViewBtn.setToggleGroup(toggleGroup);
//        weeklyViewBtn.setToggleGroup(toggleGroup);
//        buttonHolder.getChildren().addAll(monthlyViewBtn, weeklyViewBtn);

        monthlyViewSegment = new MFXSegmentedButtonSkin.MFXSegment("Monthly");
        weeklyViewSegment = new MFXSegmentedButtonSkin.MFXSegment("Weekly");
        graphViewSegment = new MFXSegmentedButton(weeklyViewSegment, monthlyViewSegment);
        graphViewSegment.setMinHeight(30);
        graphViewSegment.setPrefHeight(30);
        graphViewSegment.setMaxHeight(30);
        hbox.getChildren().addAll(graphViewSegment);

        return hbox;
    }

    private BarChart<String, Number> buildBottom() {
        var xAxis = new CategoryAxis();
        xAxis.setSide(Side.BOTTOM);
        var yAxis = new NumberAxis();
        yAxis.setSide(Side.LEFT);
        chart = new BarChart<>(xAxis, yAxis);
        UIUtils.anchor(chart, 32d, 0d, 0d, 0d);
        barChartValues();
        return chart;
    }

    private void barChartValues() {
        if (graphViewSegment.getSegments().getFirst().isSelected()) {
            DashboardViewModel.getWeeklyRevenue(null, null);
            XYChart.Series<String, Number> weeklySeries1 = new XYChart.Series<>();
            for (LineChartModel data : DashboardViewModel.getRevenues()) {
                weeklySeries1.getData().add(new XYChart.Data<>(data.getPeriod(), data.getTotalValue()));
            }
            weeklySeries1.setName("Sales");

            /*for (XYChart.Series<String, Number> series : barChart.getData()) {
                for (XYChart.Data<String, Number> item : series.getData()) {
                    item.getNode().setOnMousePressed(
                            (MouseEvent event) -> System.out.println("you clicked " + item + series)
                    );
                    Tooltip.install(item.getNode(), new Tooltip(item.getXValue() + ":\n" + item.getYValue()));
                    new MFXTooltip(item.getNode()).install(new Label(item.getXValue() + ":\n" + item.getYValue()));
                }
            }*/
            for (final XYChart.Series<String, Number> series : chart.getData()) {
                for (final XYChart.Data<String, Number> data : series.getData()) {
                    Tooltip tooltip = new Tooltip();
                    tooltip.setText(data.getXValue() + " " +
                            data.getYValue().toString());
                    Tooltip.install(data.getNode(), tooltip);
                }
            }

            Platform.runLater(() -> {
                chart.getData().clear();
                chart.getData().addAll(weeklySeries1);
                chart.setCategoryGap(70);
            });
        }

        if (graphViewSegment.getSegments().getLast().isSelected()) {
            DashboardViewModel.getMonthlyRevenue(null, null);
            XYChart.Series<String, Number> monthlySeries1 = new XYChart.Series<>();
            for (LineChartModel data : DashboardViewModel.getRevenues()) {
                monthlySeries1.getData().add(new XYChart.Data<>(data.getPeriod(), data.getTotalValue()));
            }
            monthlySeries1.setName("Sales");

            for (final XYChart.Series<String, Number> series : chart.getData()) {
                for (final XYChart.Data<String, Number> data : series.getData()) {
                    Tooltip tooltip = new Tooltip();
                    tooltip.setText(data.getXValue() + " " +
                            data.getYValue().toString());
                    Tooltip.install(data.getNode(), tooltip);
                }
            }

            Platform.runLater(() -> {
                chart.getData().clear();
                chart.getData().addAll(monthlySeries1);
                chart.setCategoryGap(30);
            });
        }

        weeklyViewSegment.setOnAction(actionEvent -> DashboardViewModel.getWeeklyRevenue(() -> {
            XYChart.Series<String, Number> weeklySeries1 = new XYChart.Series<>();
            for (LineChartModel data : DashboardViewModel.getRevenues()) {
                weeklySeries1.getData().add(new XYChart.Data<>(data.getPeriod(), data.getTotalValue()));
            }
            weeklySeries1.setName("Sales");

            for (final XYChart.Series<String, Number> series : chart.getData()) {
                for (final XYChart.Data<String, Number> data : series.getData()) {
                    Tooltip tooltip = new Tooltip();
                    tooltip.setText(data.getXValue() + " " +
                            data.getYValue().toString());
                    Tooltip.install(data.getNode(), tooltip);
                }
            }

            Platform.runLater(() -> {
                chart.getData().clear();
                chart.getData().addAll(weeklySeries1);
                chart.setCategoryGap(70);
            });
        }, null));

        monthlyViewSegment.setOnAction(actionEvent -> DashboardViewModel.getMonthlyRevenue(() -> {
            XYChart.Series<String, Number> monthlySeries1 = new XYChart.Series<>();
            for (LineChartModel data : DashboardViewModel.getRevenues()) {
                monthlySeries1.getData().add(new XYChart.Data<>(data.getPeriod(), data.getTotalValue()));
            }
            monthlySeries1.setName("Sales");

            for (final XYChart.Series<String, Number> series : chart.getData()) {
                for (final XYChart.Data<String, Number> data : series.getData()) {
                    Tooltip tooltip = new Tooltip();
                    tooltip.setText(data.getXValue() + " " +
                            data.getYValue().toString());
                    Tooltip.install(data.getNode(), tooltip);
                }
            }

            Platform.runLater(() -> {
                chart.getData().clear();
                chart.getData().addAll(monthlySeries1);
                chart.setCategoryGap(30);
            });
        }, null));

        chart.setLegendVisible(false);
        chart.setAnimated(false);
    }
}
