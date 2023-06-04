package org.infinite.spoty.views.inventory.category;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.database.dao.ProductCategoryDao;
import org.infinite.spoty.database.models.ProductCategory;
import org.infinite.spoty.viewModels.ProductCategoryViewModel;

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
        MFXTableColumn<ProductCategory> categoryCode = new MFXTableColumn<>("Code", false, Comparator.comparing(ProductCategory::getCode));
        MFXTableColumn<ProductCategory> categoryName = new MFXTableColumn<>("Name", false, Comparator.comparing(ProductCategory::getName));

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
        categoryTable.setItems(ProductCategoryViewModel.getItems());
    }

    private void getProductCategoryTable() {
        categoryTable.setPrefSize(1000, 1000);
        categoryTable.features().enableBounceEffect();
        categoryTable.features().enableSmoothScrolling(0.5);

        categoryTable.setTableRowFactory(t -> {
            MFXTableRow<ProductCategory> row = new MFXTableRow<>(categoryTable, t);
            EventHandler<ContextMenuEvent> eventHandler = event -> {
                showContextMenu(event.getSource()).show(categoryTable.getParent(), event.getScreenX(), event.getScreenY());
                event.consume();
            };
            row.setOnContextMenuRequested(eventHandler);
            return row;
        });
    }

    private MFXContextMenu showContextMenu(Object obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(categoryTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(e -> {
            MFXTableRow<ProductCategory> onj = (MFXTableRow) obj;
            ProductCategoryDao.deleteProductCategory(onj.getData().getId());
            ProductCategoryViewModel.getItems();
            e.consume();
        });
        // Edit
        edit.setOnAction(e -> {
            MFXTableRow<ProductCategory> onj = (MFXTableRow) obj;
            ProductCategoryViewModel.getItem(onj.getData().getId());
            dialog.showAndWait();
            e.consume();
        });

        contextMenu.addItems(edit, delete);

        return contextMenu;
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
