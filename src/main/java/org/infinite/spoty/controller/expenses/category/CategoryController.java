package org.infinite.spoty.controller.expenses.category;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.infinite.spoty.model.ExpenseCategory;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.data.SampleData.expenseCategorySampleData;

public class CategoryController implements Initializable {

    private MFXTableView<ExpenseCategory> categoriesTable;

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
            categoryContentPane.setCenter(getExpenseCategoryTable());
            setupTable();
        })).start();
    }

    private void setupTable() {
        MFXTableColumn<ExpenseCategory> categoryName = new MFXTableColumn<>("Name", true, Comparator.comparing(ExpenseCategory::getCategoryName));
        MFXTableColumn<ExpenseCategory> categoryDescription = new MFXTableColumn<>("Description", true, Comparator.comparing(ExpenseCategory::getCategoryDescription));

        categoryName.setRowCellFactory(category -> new MFXTableRowCell<>(ExpenseCategory::getCategoryName));
        categoryDescription.setRowCellFactory(category -> new MFXTableRowCell<>(ExpenseCategory::getCategoryDescription));

        categoriesTable.getTableColumns().addAll(categoryName, categoryDescription);
        categoriesTable.getFilters().addAll(
                new StringFilter<>("Name", ExpenseCategory::getCategoryName),
                new StringFilter<>("Description", ExpenseCategory::getCategoryDescription)
        );

        categoriesTable.setItems(expenseCategorySampleData());
    }

    private MFXTableView<ExpenseCategory> getExpenseCategoryTable() {
        categoriesTable = new MFXTableView<>();
        categoriesTable.setPrefSize(1200, 1000);
        categoriesTable.features().enableBounceEffect();
        categoriesTable.autosizeColumnsOnInitialization();
        categoriesTable.features().enableSmoothScrolling(0.5);
        return categoriesTable;
    }
}
