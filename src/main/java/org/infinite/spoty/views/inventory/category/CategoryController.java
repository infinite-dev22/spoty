package org.infinite.spoty.views.inventory.category;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.models.Category;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.data.SampleData.categorySampleData;

public class CategoryController implements Initializable {
    @FXML
    public MFXTableView<Category> categoryTable;
    @FXML
    public MFXTextField categorySearchBar;
    @FXML
    public HBox categoryActionsPane;
    @FXML
    public MFXButton categoryImportBtn;
    private Dialog<ButtonType> dialog;

    public CategoryController(Stage stage) {
        Platform.runLater(() -> {
            try {
                productCategoryDialogPane(stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
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
        getCategoryTable();
        categoryTable.setItems(categorySampleData());
    }

    private void getCategoryTable() {
        categoryTable = new MFXTableView<>();
        categoryTable.setPrefSize(1000, 1000);
        categoryTable.features().enableBounceEffect();
        categoryTable.features().enableSmoothScrolling(0.5);
        categoryTable.autosizeColumnsOnInitialization();
    }

    private void productCategoryDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/ProductCategoryForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }

    public void categoryCreateBtnClicked() {
        dialog.showAndWait();
    }
}
