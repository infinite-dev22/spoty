package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.network_bridge.dtos.transfers.TransferDetail;
import inc.nomard.spoty.network_bridge.dtos.transfers.TransferMaster;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.extern.java.Log;

import java.net.URL;
import java.util.ResourceBundle;

@Log
public class TransferPreviewController implements Initializable {
    static final ObservableList<TransferDetail> transferDetailsList = FXCollections.observableArrayList();
    private static final ListProperty<TransferDetail> transferDetails =
            new SimpleListProperty<>(transferDetailsList);
    private static TransferPreviewController instance;
    public Label transferDate;
    public Label transferRef;
    public Label fromBranch;
    public Label toBranch;
    public TableView<TransferDetail> itemsTable;
    public Label doneBy;
    public Label transferNote;

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
        TableColumn<TransferDetail, String> product = new TableColumn<>("Name");
        TableColumn<TransferDetail, String> quantity = new TableColumn<>("Quantity");

        // Set table column width.
        product.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.5));
        quantity.prefWidthProperty().bind(itemsTable.widthProperty().multiply(.5));
        // Set table filter.
        itemsTable
                .getColumns()
                .addAll(product, quantity);

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
