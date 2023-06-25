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

package org.infinite.spoty.views.components;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

public class PieChartCardController implements Initializable {

  @FXML private PieChart pieChart;

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
