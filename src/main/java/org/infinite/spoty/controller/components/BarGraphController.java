package org.infinite.spoty.controller.components;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ResourceBundle;

public class BarGraphController implements Initializable {

    @FXML
    private BarChart<String, Number> barChart;
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
        barChartValues();
    }

    private void barChartValues(){
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();

        series1.getData().add(new XYChart.Data<>("Nov", 3));
        series1.getData().add(new XYChart.Data<>("Dec", 14));
        series1.getData().add(new XYChart.Data<>("Jan", 15));
        series1.getData().add(new XYChart.Data<>("Feb", 56));
        series1.getData().add(new XYChart.Data<>("Mar", 45));
        series1.getData().add(new XYChart.Data<>("Apr", 78));
        series1.setName("Incomes");

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();

        series2.getData().add(new XYChart.Data<>("Nov", 10));
        series2.getData().add(new XYChart.Data<>("Dec", 29));
        series2.getData().add(new XYChart.Data<>("Jan", 21));
        series2.getData().add(new XYChart.Data<>("Feb", 37));
        series2.getData().add(new XYChart.Data<>("Mar", 39));
        series2.getData().add(new XYChart.Data<>("Apr", 62));
        series2.setName("Expenses");

        barChart.getData().addAll(series1, series2);
    }
}
