package inc.nomard.spoty.core.views.components;

import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.viewModels.dashboard.DashboardViewModel;
import inc.nomard.spoty.network_bridge.dtos.dashboard.LineChartModel;
import inc.nomard.spoty.utils.UIUtils;
import inc.nomard.spoty.utils.navigation.Spacer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class TotalRevenue extends AnchorPane {
    private final ToggleButton monthlyViewBtn;
    private final ToggleButton weeklyViewBtn;
    private BarChart<String, Number> chart;

    public TotalRevenue() {
        monthlyViewBtn = new ToggleButton("Monthly");
        weeklyViewBtn = new ToggleButton("Weekly");
        monthlyViewBtn.getStyleClass().add(Styles.RIGHT_PILL);
        weeklyViewBtn.getStyleClass().add(Styles.LEFT_PILL);
        weeklyViewBtn.setSelected(true);
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
        var toggleGroup = new ToggleGroup();
        monthlyViewBtn.setToggleGroup(toggleGroup);
        weeklyViewBtn.setToggleGroup(toggleGroup);

        var hbox = new HBox(weeklyViewBtn, monthlyViewBtn);
        hbox.setAlignment(Pos.CENTER_LEFT);

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
        if (weeklyViewBtn.isSelected()) {
            DashboardViewModel.getWeeklyRevenue(null, null);
            if (!DashboardViewModel.getRevenues().isEmpty()) {
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
            }
        }

        if (monthlyViewBtn.isSelected()) {
            DashboardViewModel.getMonthlyRevenue(null, null);
            if (!DashboardViewModel.getRevenues().isEmpty()) {
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
        }

        weeklyViewBtn.setOnAction(actionEvent -> DashboardViewModel.getWeeklyRevenue(() -> {
            if (!DashboardViewModel.getRevenues().isEmpty()) {
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
            }
        }, null));

        monthlyViewBtn.setOnAction(actionEvent -> DashboardViewModel.getMonthlyRevenue(() -> {
            if (!DashboardViewModel.getRevenues().isEmpty()) {
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
        }, null));

        chart.setLegendVisible(false);
        chart.setAnimated(false);
    }
}
