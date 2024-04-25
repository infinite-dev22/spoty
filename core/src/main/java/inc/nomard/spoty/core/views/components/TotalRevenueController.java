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

import io.github.palexdev.mfxcomponents.controls.buttons.MFXSegmentedButton;
import io.github.palexdev.mfxcomponents.skins.MFXSegmentedButtonSkin;
import io.github.palexdev.mfxcore.controls.Label;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class TotalRevenueController implements Initializable {
    private static final Random RND = new Random();
    @FXML
    public HBox buttonHolder;
    @FXML
    public Label cardTitle;
    @FXML
    private BarChart<String, Number> barChart;
    private MFXSegmentedButton graphViewSegment;
    private MFXSegmentedButtonSkin.MFXSegment monthlyViewSegment,
            weeklyViewSegment;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cardTitle.setText("Total Revenue");

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
        buttonHolder.getChildren().addAll(graphViewSegment);

        barChartValues();
    }

    private void barChartValues() {
        if (graphViewSegment.getSegments().getFirst().isSelected()) {
            var t1 = RND.nextDouble() * 500 + 200;
            var t2 = RND.nextDouble() * 500 + 200;
            var t3 = RND.nextDouble() * 500 + 200;
            var t4 = RND.nextDouble() * 500 + 200;
            var t5 = RND.nextDouble() * 500 + 200;
            var t6 = RND.nextDouble() * 500 + 200;
            var t7 = RND.nextDouble() * 500 + 200;

            XYChart.Series<String, Number> weeklySeries1 = new XYChart.Series<>();
            weeklySeries1.getData().add(new XYChart.Data<>("Mon", t1));
            weeklySeries1.getData().add(new XYChart.Data<>("Tue", t2));
            weeklySeries1.getData().add(new XYChart.Data<>("Wed", t3));
            weeklySeries1.getData().add(new XYChart.Data<>("Thur", t4));
            weeklySeries1.getData().add(new XYChart.Data<>("Fri", t5));
            weeklySeries1.getData().add(new XYChart.Data<>("Sat", t6));
            weeklySeries1.getData().add(new XYChart.Data<>("Sun", t7));
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
            for (final XYChart.Series<String, Number> series : barChart.getData()) {
                for (final XYChart.Data<String, Number> data : series.getData()) {
                    Tooltip tooltip = new Tooltip();
                    tooltip.setText(data.getXValue() + " " +
                            data.getYValue().toString());
                    Tooltip.install(data.getNode(), tooltip);
                }
            }

            Platform.runLater(() -> {
                barChart.getData().clear();
                barChart.getData().addAll(weeklySeries1);
                barChart.setCategoryGap(70);
            });
        }

        if (graphViewSegment.getSegments().getLast().isSelected()) {
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

            XYChart.Series<String, Number> monthlySeries1 = new XYChart.Series<>();
            monthlySeries1.getData().add(new XYChart.Data<>("Jan", t1));
            monthlySeries1.getData().add(new XYChart.Data<>("Feb", t2));
            monthlySeries1.getData().add(new XYChart.Data<>("Mar", t3));
            monthlySeries1.getData().add(new XYChart.Data<>("Apr", t4));
            monthlySeries1.getData().add(new XYChart.Data<>("May", t5));
            monthlySeries1.getData().add(new XYChart.Data<>("Jun", t6));
            monthlySeries1.getData().add(new XYChart.Data<>("Jul", t7));
            monthlySeries1.getData().add(new XYChart.Data<>("Aug", t8));
            monthlySeries1.getData().add(new XYChart.Data<>("Sep", t9));
            monthlySeries1.getData().add(new XYChart.Data<>("Oct", t10));
            monthlySeries1.getData().add(new XYChart.Data<>("Nov", t11));
            monthlySeries1.getData().add(new XYChart.Data<>("Dec", t12));
            monthlySeries1.setName("Sales");

            for (final XYChart.Series<String, Number> series : barChart.getData()) {
                for (final XYChart.Data<String, Number> data : series.getData()) {
                    Tooltip tooltip = new Tooltip();
                    tooltip.setText(data.getXValue() + " " +
                            data.getYValue().toString());
                    Tooltip.install(data.getNode(), tooltip);
                }
            }

            Platform.runLater(() -> {
                barChart.getData().clear();
                barChart.getData().addAll(monthlySeries1);
                barChart.setCategoryGap(30);
            });
        }

        weeklyViewSegment.setOnAction(actionEvent -> {
            var t1 = RND.nextDouble() * 500 + 200;
            var t2 = RND.nextDouble() * 500 + 200;
            var t3 = RND.nextDouble() * 500 + 200;
            var t4 = RND.nextDouble() * 500 + 200;
            var t5 = RND.nextDouble() * 500 + 200;
            var t6 = RND.nextDouble() * 500 + 200;
            var t7 = RND.nextDouble() * 500 + 200;

            XYChart.Series<String, Number> weeklySeries1 = new XYChart.Series<>();
            weeklySeries1.getData().add(new XYChart.Data<>("Mon", t1));
            weeklySeries1.getData().add(new XYChart.Data<>("Tue", t2));
            weeklySeries1.getData().add(new XYChart.Data<>("Wed", t3));
            weeklySeries1.getData().add(new XYChart.Data<>("Thur", t4));
            weeklySeries1.getData().add(new XYChart.Data<>("Fri", t5));
            weeklySeries1.getData().add(new XYChart.Data<>("Sat", t6));
            weeklySeries1.getData().add(new XYChart.Data<>("Sun", t7));
            weeklySeries1.setName("Sales");

            for (final XYChart.Series<String, Number> series : barChart.getData()) {
                for (final XYChart.Data<String, Number> data : series.getData()) {
                    Tooltip tooltip = new Tooltip();
                    tooltip.setText(data.getXValue() + " " +
                            data.getYValue().toString());
                    Tooltip.install(data.getNode(), tooltip);
                }
            }

            Platform.runLater(() -> {
                barChart.getData().clear();
                barChart.getData().addAll(weeklySeries1);
                barChart.setCategoryGap(70);
            });
        });

        monthlyViewSegment.setOnAction(actionEvent -> {
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

            XYChart.Series<String, Number> monthlySeries1 = new XYChart.Series<>();
            monthlySeries1.getData().add(new XYChart.Data<>("Jan", t1));
            monthlySeries1.getData().add(new XYChart.Data<>("Feb", t2));
            monthlySeries1.getData().add(new XYChart.Data<>("Mar", t3));
            monthlySeries1.getData().add(new XYChart.Data<>("Apr", t4));
            monthlySeries1.getData().add(new XYChart.Data<>("May", t5));
            monthlySeries1.getData().add(new XYChart.Data<>("Jun", t6));
            monthlySeries1.getData().add(new XYChart.Data<>("Jul", t7));
            monthlySeries1.getData().add(new XYChart.Data<>("Aug", t8));
            monthlySeries1.getData().add(new XYChart.Data<>("Sep", t9));
            monthlySeries1.getData().add(new XYChart.Data<>("Oct", t10));
            monthlySeries1.getData().add(new XYChart.Data<>("Nov", t11));
            monthlySeries1.getData().add(new XYChart.Data<>("Dec", t12));
            monthlySeries1.setName("Sales");

            for (final XYChart.Series<String, Number> series : barChart.getData()) {
                for (final XYChart.Data<String, Number> data : series.getData()) {
                    Tooltip tooltip = new Tooltip();
                    tooltip.setText(data.getXValue() + " " +
                            data.getYValue().toString());
                    Tooltip.install(data.getNode(), tooltip);
                }
            }

            Platform.runLater(() -> {
                barChart.getData().clear();
                barChart.getData().addAll(monthlySeries1);
                barChart.setCategoryGap(30);
            });
        });

        barChart.setLegendVisible(false);
        barChart.setAnimated(false);
    }
}
