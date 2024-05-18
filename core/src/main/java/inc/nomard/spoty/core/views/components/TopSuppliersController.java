package inc.nomard.spoty.core.views.components;

import inc.nomard.spoty.core.components.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.filter.*;
import java.net.*;
import java.util.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;

public class TopSuppliersController implements Initializable {
    public Label cardTitle;
    public ViewAll viewAll;
    public MFXTableView<Supplier> suppliers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cardTitle.setText(Labels.TOP_SUPPLIERS);
        suppliers.setFooterVisible(false);
        suppliers.setBorder(null);
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Supplier> supplierName =
                new MFXTableColumn<>("Name", false, Comparator.comparing(Supplier::getName));
        MFXTableColumn<Supplier> supplierPhone =
                new MFXTableColumn<>("Phone", false, Comparator.comparing(Supplier::getPhone));
        MFXTableColumn<Supplier> supplierEmail =
                new MFXTableColumn<>("Email", false, Comparator.comparing(Supplier::getEmail));

        supplierName.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getName));
        supplierPhone.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getPhone));
        supplierEmail.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getEmail));

        supplierName.prefWidthProperty().bind(suppliers.widthProperty().multiply(.4));
        supplierPhone.prefWidthProperty().bind(suppliers.widthProperty().multiply(.4));
        supplierEmail.prefWidthProperty().bind(suppliers.widthProperty().multiply(.4));

        suppliers
                .getTableColumns()
                .addAll(supplierName, supplierPhone, supplierEmail);
        suppliers
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", Supplier::getName),
                        new StringFilter<>("Phone", Supplier::getPhone),
                        new StringFilter<>("Email", Supplier::getEmail));
        styleTable();

        if (SupplierViewModel.getSuppliers().isEmpty()) {
            SupplierViewModel.getSuppliers()
                    .addListener(
                            (ListChangeListener<Supplier>)
                                    c -> suppliers.setItems(SupplierViewModel.getSuppliers()));
        } else {
            suppliers.itemsProperty().bindBidirectional(SupplierViewModel.suppliersProperty());
        }
    }

    private void styleTable() {
        suppliers.setPrefSize(1000, 1000);
        suppliers.features().enableBounceEffect();
        suppliers.features().enableSmoothScrolling(0.5);

        suppliers.setTableRowFactory(
                t -> {
                    MFXTableRow<Supplier> row = new MFXTableRow<>(suppliers, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
//                                showContextMenu((MFXTableRow<Supplier>) event.getSource())
//                                        .show(
//                                                suppliers.getScene().getWindow(),
//                                                event.getScreenX(),
//                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }
}
