package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.network_bridge.dtos.stock_ins.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import java.net.*;
import java.util.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;

public class StockInPreviewController implements Initializable {
    static final ObservableList<StockInDetail> stockInDetailsList = FXCollections.observableArrayList();
    private static final ListProperty<StockInDetail> stockInDetails =
            new SimpleListProperty<>(stockInDetailsList);
    @FXML
    public Label stockInDate;
    @FXML
    public Label stockInRef;
    @FXML
    public MFXTableView<StockInDetail> itemsTable;
    @FXML
    public Label stockInNote;
    @FXML
    public Label doneBy;

    public static ObservableList<StockInDetail> getStockInDetails() {
        return stockInDetails.get();
    }

    public static ListProperty<StockInDetail> stockInDetailsProperty() {
        return stockInDetails;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        // Set table column titles.
        MFXTableColumn<StockInDetail> product =
                new MFXTableColumn<>("Name", false, Comparator.comparing(StockInDetail::getProductName));
        MFXTableColumn<StockInDetail> quantity =
                new MFXTableColumn<>("Qnty", false, Comparator.comparing(StockInDetail::getQuantity));

        // Set table column data.
        product.setRowCellFactory(stockInDetail -> {
            var cell = new MFXTableRowCell<>(StockInDetail::getProductName);
            cell.setAlignment(Pos.CENTER_LEFT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });
        quantity.setRowCellFactory(stockInDetail -> {
            var cell = new MFXTableRowCell<>(StockInDetail::getQuantity);
            cell.setAlignment(Pos.CENTER_RIGHT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });

        // Set table column width.
        product.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.5));
        quantity.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.5));

        // Set table filter.
        itemsTable
                .getTableColumns()
                .addAll(product, quantity);

        styleTable();

        // Populate table.
        if (getStockInDetails().isEmpty()) {
            getStockInDetails()
                    .addListener(
                            (ListChangeListener<StockInDetail>)
                                    change -> itemsTable.setItems(getStockInDetails()));
        } else {
            itemsTable.itemsProperty().bindBidirectional(stockInDetailsProperty());
        }
    }

    private void styleTable() {
        itemsTable.setPrefSize(1000, 1000);
        itemsTable.features().enableBounceEffect();
        itemsTable.features().enableSmoothScrolling(0.5);
        itemsTable.setFooterVisible(false);
    }

    public void init(StockInMaster stockIn) {
        stockInDetailsList.clear();
//        stockInDate.setText(stockIn.getLocaleDate());
        stockInRef.setText(stockIn.getRef());
        stockInDetailsList.addAll(stockIn.getStockInDetails());
        stockInNote.setText(stockIn.getNotes());
//        doneBy.setText(stockIn.doneBy());
    }
}
