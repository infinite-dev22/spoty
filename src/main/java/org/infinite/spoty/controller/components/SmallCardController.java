package org.infinite.spoty.controller.components;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.infinite.spoty.model.QuickStats;

import java.net.URL;
import java.util.ResourceBundle;

public class SmallCardController implements Initializable {

    @FXML
    private Label subtitle;

    @FXML
    private Label title;

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

    }

    public void setData(QuickStats quickStats){
        this.title.setText(quickStats.getTitle());
        this.subtitle.setText(quickStats.getSubtitle());
    }
}
