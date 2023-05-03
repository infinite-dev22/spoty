package org.infinite.spoty.views.components;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

import java.net.URL;
import java.util.ResourceBundle;

public class PieChartCardController implements Initializable {

    @FXML
    private PieChart pieChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pieChartValues();
    }

    private void pieChartValues() {
        pieChart.getData().add(new PieChart.Data("Nov", 3));
        pieChart.getData().add(new PieChart.Data("Dec", 14));
        pieChart.getData().add(new PieChart.Data("Jan", 15));
        pieChart.getData().add(new PieChart.Data("Feb", 56));
        pieChart.getData().add(new PieChart.Data("Mar", 45));
        pieChart.getData().add(new PieChart.Data("Apr", 78));
    }
}
