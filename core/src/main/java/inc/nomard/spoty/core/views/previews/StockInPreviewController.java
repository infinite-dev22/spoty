package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.network_bridge.dtos.stock_ins.*;
import io.github.palexdev.materialfx.controls.cell.*;
import java.net.*;
import java.util.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import lombok.extern.java.*;

@Log
public class StockInPreviewController implements Initializable {
    static final ObservableList<StockInDetail> stockInDetailsList = FXCollections.observableArrayList();
    private static final ListProperty<StockInDetail> stockInDetails =
            new SimpleListProperty<>(stockInDetailsList);
    @FXML
    public Label stockInDate;
    @FXML
    public Label stockInRef;
    @FXML
    public TableView<StockInDetail> itemsTable;
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
        TableColumn<StockInDetail, String> product = new TableColumn<>("Name");
        TableColumn<StockInDetail, String> quantity = new TableColumn<>("Qnty");

        // Set table column width.
        product.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.5));
        quantity.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.5));

        // Set table filter.
        itemsTable
                .getColumns()
                .addAll(product, quantity);

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

    public void init(StockInMaster stockIn) {
        stockInDetailsList.clear();
//        stockInDate.setText(stockIn.getLocaleDate());
        stockInRef.setText(stockIn.getRef());
        stockInDetailsList.addAll(stockIn.getStockInDetails());
        stockInNote.setText(stockIn.getNotes());
//        doneBy.setText(stockIn.doneBy());
    }
}
