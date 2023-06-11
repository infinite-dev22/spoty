package org.infinite.spoty.views.inventory.brand;

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
import org.infinite.spoty.database.dao.BrandDao;
import org.infinite.spoty.database.models.Brand;
import org.infinite.spoty.viewModels.BrandViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.viewModels.BrandViewModel.getItems;

@SuppressWarnings("unchecked")
public class BrandController implements Initializable {
    @FXML
    public MFXTableView<Brand> brandTable;
    @FXML
    public MFXTextField brandSearchBar;
    @FXML
    public HBox brandActionsPane;
    @FXML
    public MFXButton brandImportBtn;
    private Dialog<ButtonType> dialog;

    public BrandController(Stage stage) {
        Platform.runLater(() -> {
            try {
                brandFormDialogPane(stage);
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
        MFXTableColumn<Brand> brandName = new MFXTableColumn<>("Name", false, Comparator.comparing(Brand::getName));
        MFXTableColumn<Brand> brandDescription = new MFXTableColumn<>("Description", false, Comparator.comparing(Brand::getDescription));

        brandName.setRowCellFactory(brand -> new MFXTableRowCell<>(Brand::getName));
        brandDescription.setRowCellFactory(brand -> new MFXTableRowCell<>(Brand::getDescription));

        brandName.prefWidthProperty().bind(brandTable.widthProperty().multiply(.5));
        brandDescription.prefWidthProperty().bind(brandTable.widthProperty().multiply(.5));

        brandTable.getTableColumns().addAll(brandName, brandDescription);
        brandTable.getFilters().addAll(
                new StringFilter<>("Name", Brand::getName),
                new StringFilter<>("Description", Brand::getDescription)
        );
        getBrandTable();
        brandTable.setItems(getItems());
    }

    private void getBrandTable() {
        brandTable.setPrefSize(1000, 1000);
        brandTable.features().enableBounceEffect();
        brandTable.features().enableSmoothScrolling(0.5);

        brandTable.setTableRowFactory(t -> {
            MFXTableRow<Brand> row = new MFXTableRow<>(brandTable, t);
            EventHandler<ContextMenuEvent> eventHandler = event -> {
                showContextMenu((MFXTableRow<Brand>) event.getSource())
                        .show(brandTable.getScene().getWindow(), event.getScreenX(), event.getScreenY());
                event.consume();
            };
            row.setOnContextMenuRequested(eventHandler);
            return row;
        });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<Brand> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(brandTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(e -> {
            BrandDao.deleteBrand(obj.getData().getId());
            BrandViewModel.getItems();
            e.consume();
        });
        // Edit
        edit.setOnAction(e -> {
            BrandViewModel.getItem(obj.getData().getId());
            dialog.showAndWait();
            e.consume();
        });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    private void brandFormDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/BrandForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }

    public void brandCreateBtnClicked() {
        dialog.showAndWait();
    }
}
