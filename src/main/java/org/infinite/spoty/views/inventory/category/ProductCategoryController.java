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
import org.infinite.spoty.database.models.ProductCategory;
import org.infinite.spoty.forms.ProductCategoryFormController;
import org.infinite.spoty.values.strings.Labels;
import org.infinite.spoty.viewModels.ProductCategoryFormViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

public class ProductCategoryController implements Initializable {
    @FXML
    public MFXTableView<ProductCategory> categoryTable;
    @FXML
    public MFXTextField categorySearchBar;
    @FXML
    public HBox categoryActionsPane;
    @FXML
    public MFXButton categoryImportBtn;
    private Dialog<ButtonType> dialog;

    public ProductCategoryController(Stage stage) {
        Platform.runLater(() -> {
            try {
                productProductCategoryDialogPane(stage);
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
        MFXTableColumn<ProductCategory> categoryCode = new MFXTableColumn<>("Code", true, Comparator.comparing(ProductCategory::getCode));
        MFXTableColumn<ProductCategory> categoryName = new MFXTableColumn<>("Name", true, Comparator.comparing(ProductCategory::getName));

        categoryCode.setRowCellFactory(category -> new MFXTableRowCell<>(ProductCategory::getCode));
        categoryName.setRowCellFactory(category -> new MFXTableRowCell<>(ProductCategory::getName));

        categoryCode.prefWidthProperty().bind(categoryTable.widthProperty().multiply(.5));
        categoryName.prefWidthProperty().bind(categoryTable.widthProperty().multiply(.5));

        categoryTable.getTableColumns().addAll(categoryCode, categoryName);
        categoryTable.getFilters().addAll(
                new StringFilter<>("Code", ProductCategory::getCode),
                new StringFilter<>("Name", ProductCategory::getName)
        );
        getProductCategoryTable();
        categoryTable.setItems(ProductCategoryFormViewModel.getItems());
    }

    private void getProductCategoryTable() {
        categoryTable.setPrefSize(1000, 1000);
        categoryTable.features().enableBounceEffect();
        categoryTable.features().enableSmoothScrolling(0.5);
    }

    private void productProductCategoryDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/ProductCategoryForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
        // To update dialog title dynamically, create a bind in the viewModel with the formTile.
        // ProductCategoryFormController.formTitle.setText(Labels.CREATE);
    }

    public void categoryCreateBtnClicked() {
        dialog.showAndWait();
    }
}
