package org.infinite.spoty.views.returns.purchases;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.infinite.spoty.database.models.PurchaseReturnMaster;
import org.infinite.spoty.viewModels.PurchaseReturnMasterViewModel;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class PurchaseReturnController implements Initializable {
    @FXML
    public MFXTableView<PurchaseReturnMaster> purchaseReturnTable;
    @FXML
    public BorderPane purchaseReturnContentPane;
    @FXML
    public MFXTextField purchaseReturnSearchBar;
    @FXML
    public HBox purchaseReturnActionsPane;
    @FXML
    public MFXButton purchaseReturnImportBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnDate = new MFXTableColumn<>("Date", false, Comparator.comparing(PurchaseReturnMaster::getDate));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnReference = new MFXTableColumn<>("Ref No.", false, Comparator.comparing(PurchaseReturnMaster::getRef));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnSupplier = new MFXTableColumn<>("Supplier", false, Comparator.comparing(PurchaseReturnMaster::getSupplierName));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnBranch = new MFXTableColumn<>("Branch", false, Comparator.comparing(PurchaseReturnMaster::getBranchName));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnStatus = new MFXTableColumn<>("Status", false, Comparator.comparing(PurchaseReturnMaster::getStatus));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnGrandTotal = new MFXTableColumn<>("Total", false, Comparator.comparing(PurchaseReturnMaster::getTotal));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnAmountPaid = new MFXTableColumn<>("Paid", false, Comparator.comparing(PurchaseReturnMaster::getPaid));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnPaymentStatus = new MFXTableColumn<>("Pay Status", false, Comparator.comparing(PurchaseReturnMaster::getPaymentStatus));

        purchaseReturnReference.setTooltip(new Tooltip("PurchaseMaster Return Reference Number"));
        purchaseReturnPaymentStatus.setTooltip(new Tooltip("PurchaseMaster Return Payment Status"));
        purchaseReturnStatus.setTooltip(new Tooltip("PurchaseMaster Return Status"));
        purchaseReturnBranch.setTooltip(new Tooltip("Branch, store or warehouse"));
//        purchaseReturnPaymentStatus.setPrefWidth(100);
//        purchaseReturnAmountDue.setPrefWidth(100);
//        purchaseReturnSupplier.setPrefWidth(100);

        purchaseReturnDate.prefWidthProperty().bind(purchaseReturnTable.widthProperty().multiply(.13));
        purchaseReturnReference.prefWidthProperty().bind(purchaseReturnTable.widthProperty().multiply(.13));
        purchaseReturnSupplier.prefWidthProperty().bind(purchaseReturnTable.widthProperty().multiply(.13));
        purchaseReturnBranch.prefWidthProperty().bind(purchaseReturnTable.widthProperty().multiply(.13));
        purchaseReturnStatus.prefWidthProperty().bind(purchaseReturnTable.widthProperty().multiply(.13));
        purchaseReturnGrandTotal.prefWidthProperty().bind(purchaseReturnTable.widthProperty().multiply(.13));
        purchaseReturnAmountPaid.prefWidthProperty().bind(purchaseReturnTable.widthProperty().multiply(.13));
        purchaseReturnPaymentStatus.prefWidthProperty().bind(purchaseReturnTable.widthProperty().multiply(.13));

        purchaseReturnDate.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getDate));
        purchaseReturnReference.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getRef));
        purchaseReturnSupplier.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getSupplierName));
        purchaseReturnBranch.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getBranchName));
        purchaseReturnStatus.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getStatus));
        purchaseReturnGrandTotal.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getTotal));
        purchaseReturnAmountPaid.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getPaid));
        purchaseReturnPaymentStatus.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getPaymentStatus));

        purchaseReturnTable.getTableColumns().addAll(purchaseReturnDate, purchaseReturnReference, purchaseReturnSupplier, purchaseReturnBranch, purchaseReturnStatus, purchaseReturnGrandTotal, purchaseReturnAmountPaid, purchaseReturnPaymentStatus);
        purchaseReturnTable.getFilters().addAll(new StringFilter<>("Ref No.", PurchaseReturnMaster::getRef),
                new StringFilter<>("Supplier", PurchaseReturnMaster::getSupplierName),
                new StringFilter<>("Branch", PurchaseReturnMaster::getBranchName),
                new StringFilter<>("PurchaseMaster Ref", PurchaseReturnMaster::getRef),
                new StringFilter<>("Status", PurchaseReturnMaster::getStatus),
                new DoubleFilter<>("Total", PurchaseReturnMaster::getTotal),
                new DoubleFilter<>("Paid", PurchaseReturnMaster::getPaid),
                new StringFilter<>("Pay Status", PurchaseReturnMaster::getPaymentStatus));

        stylePurchaseReturnMasterTable();
        purchaseReturnTable.setItems(PurchaseReturnMasterViewModel.getPurchaseReturnMasters());
    }

    private void stylePurchaseReturnMasterTable() {
        purchaseReturnTable.setPrefSize(1200, 1000);
        purchaseReturnTable.features().enableBounceEffect();
        purchaseReturnTable.features().enableSmoothScrolling(0.5);
    }
}
