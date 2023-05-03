package org.infinite.spoty.views.components;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.infinite.spoty.models.QuickStats;

import java.net.URL;
import java.util.ResourceBundle;

public class SmallCardController implements Initializable {

    @FXML
    private Label subtitle;

    @FXML
    private Label title;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setData(QuickStats quickStats) {
        this.title.setText(quickStats.getTitle());
        this.subtitle.setText(quickStats.getSubtitle());
    }
}
