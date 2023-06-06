package org.infinite.spoty.views.people.suppliers;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.database.dao.SupplierDao;
import org.infinite.spoty.database.models.Supplier;
import org.infinite.spoty.viewModels.SupplierViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

public class SupplierController implements Initializable {
    @FXML
    public MFXTextField supplierSearchBar;
    @FXML
    public HBox supplierActionsPane;
    @FXML
    public MFXButton supplierImportBtn;
    @FXML
    public MFXTableView<Supplier> suppliersTable;
    @FXML
    public BorderPane suppliersContentPane;
    private Dialog<ButtonType> dialog;

    public SupplierController(Stage stage) {
        Platform.runLater(() -> {
            try {
                supplierFormDialogPane(stage);
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
        MFXTableColumn<Supplier> supplierCode = new MFXTableColumn<>("Code", false, Comparator.comparing(Supplier::getCode));
        MFXTableColumn<Supplier> supplierName = new MFXTableColumn<>("Name", false, Comparator.comparing(Supplier::getName));
        MFXTableColumn<Supplier> supplierPhone = new MFXTableColumn<>("Phone", false, Comparator.comparing(Supplier::getPhone));
        MFXTableColumn<Supplier> supplierEmail = new MFXTableColumn<>("Email", false, Comparator.comparing(Supplier::getEmail));
        MFXTableColumn<Supplier> supplierTax = new MFXTableColumn<>("Tax No.", false, Comparator.comparing(Supplier::getTaxNumber));

        supplierCode.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getCode));
        supplierName.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getName));
        supplierPhone.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getPhone));
        supplierEmail.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getEmail));
        supplierTax.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getTaxNumber));

        supplierCode.prefWidthProperty().bind(suppliersTable.widthProperty().multiply(.1));
        supplierName.prefWidthProperty().bind(suppliersTable.widthProperty().multiply(.3));
        supplierPhone.prefWidthProperty().bind(suppliersTable.widthProperty().multiply(.2));
        supplierEmail.prefWidthProperty().bind(suppliersTable.widthProperty().multiply(.2));
        supplierTax.prefWidthProperty().bind(suppliersTable.widthProperty().multiply(.2));

        suppliersTable.getTableColumns().addAll(supplierCode, supplierName, supplierPhone, supplierEmail, supplierTax);
        suppliersTable.getFilters().addAll(
                new StringFilter<>("Code", Supplier::getCode),
                new StringFilter<>("Name", Supplier::getName),
                new StringFilter<>("Phone", Supplier::getPhone),
                new StringFilter<>("Email", Supplier::getEmail),
                new StringFilter<>("Tax No.", Supplier::getTaxNumber)
        );
        getTable();
        suppliersTable.setItems(SupplierViewModel.getSuppliers());
    }

    private void getTable() {
        suppliersTable.setPrefSize(1000, 1000);
        suppliersTable.features().enableBounceEffect();
        suppliersTable.features().enableSmoothScrolling(0.5);

        suppliersTable.setTableRowFactory(t -> {
            MFXTableRow<Supplier> row = new MFXTableRow<>(suppliersTable, t);
            EventHandler<ContextMenuEvent> eventHandler = event -> {
                showContextMenu((MFXTableRow<Supplier>) event.getSource()).show(suppliersTable.getParent(), event.getScreenX(), event.getScreenY());
                event.consume();
            };
            row.setOnContextMenuRequested(eventHandler);
            return row;
        });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<Supplier> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(suppliersTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(e -> {
            SupplierDao.deleteSupplier(obj.getData().getId());
            SupplierViewModel.getSuppliers();
            e.consume();
        });
        // Edit
        edit.setOnAction(e -> {
            SupplierViewModel.getItem(obj.getData().getId());
            dialog.showAndWait();
            e.consume();
        });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    private void supplierFormDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/SupplierForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }

    public void supplierCreateBtnClicked() {
        dialog.showAndWait();
    }
}
