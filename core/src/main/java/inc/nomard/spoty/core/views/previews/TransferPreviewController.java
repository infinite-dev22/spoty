package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.network_bridge.dtos.transfers.TransferDetail;
import inc.nomard.spoty.network_bridge.dtos.transfers.TransferMaster;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class TransferPreviewController implements Initializable {
    private static TransferPreviewController instance;
    public Label transferDate;
    public Label transferRef;
    public Label fromBranch;
    public Label toBranch;
    public MFXTableView<TransferDetail> itemsTable;
    public Label doneBy;
    public Label transferNote;
    static final ObservableList<TransferDetail> transferDetailsList = FXCollections.observableArrayList();
    private static final ListProperty<TransferDetail> transferDetails =
            new SimpleListProperty<>(transferDetailsList);

    public static ObservableList<TransferDetail> getTransferDetails() {
        return transferDetails.get();
    }

    public static ListProperty<TransferDetail> transferDetailsProperty() {
        return transferDetails;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        // Set table column titles.
        MFXTableColumn<TransferDetail> product =
                new MFXTableColumn<>("Name", false, Comparator.comparing(TransferDetail::getProductName));
        MFXTableColumn<TransferDetail> quantity =
                new MFXTableColumn<>("Quantity", false, Comparator.comparing(TransferDetail::getQuantity));

        // Set table column data.
        product.setRowCellFactory(saleDetail -> {
            var cell = new MFXTableRowCell<>(TransferDetail::getProductName);
            cell.setAlignment(Pos.CENTER_LEFT);
            cell.getStyleClass().add("table-cell-border");
            return cell;
        });
        quantity.setRowCellFactory(saleDetail -> {
            var cell = new MFXTableRowCell<>(TransferDetail::getQuantity);
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
        if (getTransferDetails().isEmpty()) {
            getTransferDetails()
                    .addListener(
                            (ListChangeListener<TransferDetail>)
                                    change -> itemsTable.setItems(getTransferDetails()));
        } else {
            itemsTable.itemsProperty().bindBidirectional(transferDetailsProperty());
        }
    }

    private void styleTable() {
        itemsTable.setPrefSize(1000, 1000);
        itemsTable.features().enableBounceEffect();
        itemsTable.features().enableSmoothScrolling(0.5);
        itemsTable.setFooterVisible(false);
    }

    public void init(TransferMaster transfer) {
        transferDetailsList.clear();
        transferDate.setText(transfer.getLocaleDate());
        transferRef.setText(transfer.getRef());
        fromBranch.setText(transfer.getFromBranchName());
        toBranch.setText(transfer.getToBranchName());
        transferNote.setText(transfer.getNotes());
//        doneBy.setText(transfer.getCreatedBy().getName());
        transferDetailsList.addAll(transfer.getTransferDetails());
    }
}