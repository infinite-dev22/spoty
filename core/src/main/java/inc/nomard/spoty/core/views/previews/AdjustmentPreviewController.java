package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.network_bridge.dtos.adjustments.*;
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
import lombok.extern.java.*;

@Log
public class AdjustmentPreviewController implements Initializable {
    static final ObservableList<AdjustmentDetail> adjustmentDetailsList = FXCollections.observableArrayList();
    private static final ListProperty<AdjustmentDetail> adjustmentDetails =
            new SimpleListProperty<>(adjustmentDetailsList);
    @FXML
    public Label adjustmentDate;
    @FXML
    public Label adjustmentRef;
    @FXML
    public MFXTableView<AdjustmentDetail> itemsTable;
    @FXML
    public Label doneBy;
    @FXML
    public Label adjustmentNote;

    public static ObservableList<AdjustmentDetail> getAdjustmentDetails() {
        return adjustmentDetails.get();
    }

    public static ListProperty<AdjustmentDetail> adjustmentDetailsProperty() {
        return adjustmentDetails;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        // Set table column titles.
        MFXTableColumn<AdjustmentDetail> product =
                new MFXTableColumn<>("Name", false, Comparator.comparing(AdjustmentDetail::getProductName));
        MFXTableColumn<AdjustmentDetail> quantity =
                new MFXTableColumn<>("Quantity", false, Comparator.comparing(AdjustmentDetail::getQuantity));

        // Set table column data.
        product.setRowCellFactory(saleDetail -> {
            var cell = new MFXTableRowCell<>(AdjustmentDetail::getProductName);
            cell.setAlignment(Pos.CENTER_LEFT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });
        quantity.setRowCellFactory(saleDetail -> {
            var cell = new MFXTableRowCell<>(AdjustmentDetail::getQuantity);
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
        if (getAdjustmentDetails().isEmpty()) {
            getAdjustmentDetails()
                    .addListener(
                            (ListChangeListener<AdjustmentDetail>)
                                    change -> itemsTable.setItems(getAdjustmentDetails()));
        } else {
            itemsTable.itemsProperty().bindBidirectional(adjustmentDetailsProperty());
        }
    }

    private void styleTable() {
        itemsTable.setPrefSize(1000, 1000);
        itemsTable.features().enableBounceEffect();
        itemsTable.features().enableSmoothScrolling(0.5);
        itemsTable.setFooterVisible(false);
    }

    public void init(AdjustmentMaster adjustment) {
        adjustmentDetailsList.clear();
        adjustmentRef.setText(adjustment.getRef());
        adjustmentNote.setText(adjustment.getNotes());
//        doneBy.setText(adjustment.getCreatedBy().getName());
        adjustmentDetailsList.addAll(adjustment.getAdjustmentDetails());
    }
}
