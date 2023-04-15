package org.infinite.spoty.controller.inventory.category;

import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import org.infinite.spoty.model.Category;
import org.infinite.spoty.model.Device;

import java.net.URL;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {

    public VBox categoryContentPane;
//    public MFXPaginatedTableView categoryTable;

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
//        CategoryTable.setupTable();
    }
}
