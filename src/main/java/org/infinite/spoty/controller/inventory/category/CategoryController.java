package org.infinite.spoty.controller.inventory.category;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import org.infinite.spoty.model.Category;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.data.SampleData.categorySampleData;

public class CategoryController implements Initializable {


    public MFXTableView<Category> categoryTable;

    @FXML
    public BorderPane categoryContentPane;


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

        new Thread(() -> Platform.runLater(() -> {
            categoryContentPane.setCenter(getCategoryTable());
            setupTable();

//            When.onChanged(categoryTable.currentPageProperty())
//                    .then((oldValue, newValue) -> categoryTable.autosizeColumns())
//                    .listen();
        })).start();
    }

    private void setupTable() {
        MFXTableColumn<Category> categoryCode = new MFXTableColumn<>("Category Code", true, Comparator.comparing(Category::getCategoryCode));
        MFXTableColumn<Category> categoryName = new MFXTableColumn<>("Category Name", true, Comparator.comparing(Category::getCategoryName));

        categoryCode.setRowCellFactory(category -> new MFXTableRowCell<>(Category::getCategoryCode));
        categoryName.setRowCellFactory(category -> new MFXTableRowCell<>(Category::getCategoryName));

        categoryTable.getTableColumns().addAll(categoryCode, categoryName);
        categoryTable.getFilters().addAll(
                new StringFilter<>("Category Code", Category::getCategoryCode),
                new StringFilter<>("Category Name", Category::getCategoryName)
        );

        categoryTable.setItems(categorySampleData());
    }

    private MFXTableView<Category> getCategoryTable() {
        categoryTable = new MFXTableView<>();
        categoryTable.setPrefSize(1000, 1000);
        categoryTable.features().enableBounceEffect();
        categoryTable.features().enableSmoothScrolling(0.5);
        categoryTable.autosizeColumnsOnInitialization();
        return categoryTable;
    }
}
