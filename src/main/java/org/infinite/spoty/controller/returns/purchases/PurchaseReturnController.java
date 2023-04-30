package org.infinite.spoty.controller.returns.purchases;

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
import org.infinite.spoty.model.PurchaseReturn;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.data.SampleData.purchaseReturnSampleData;

public class PurchaseReturnController implements Initializable {
    @FXML
    public MFXTableView<PurchaseReturn> purchaseReturnTable;
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
        MFXTableColumn<PurchaseReturn> purchaseReturnDate = new MFXTableColumn<>("Date", false, Comparator.comparing(PurchaseReturn::getPurchaseReturnDate));
        MFXTableColumn<PurchaseReturn> purchaseReturnReference = new MFXTableColumn<>("Ref No.", false, Comparator.comparing(PurchaseReturn::getPurchaseReturnReference));
        MFXTableColumn<PurchaseReturn> purchaseReturnSupplier = new MFXTableColumn<>("Supplier", false, Comparator.comparing(PurchaseReturn::getPurchaseReturnSupplier));
        MFXTableColumn<PurchaseReturn> purchaseReturnBranch = new MFXTableColumn<>("Branch", false, Comparator.comparing(PurchaseReturn::getPurchaseReturnBranch));
        MFXTableColumn<PurchaseReturn> purchaseReturnReturnRef = new MFXTableColumn<>("Purch Ref", false, Comparator.comparing(PurchaseReturn::getPurchaseReturnRef));
        MFXTableColumn<PurchaseReturn> purchaseReturnStatus = new MFXTableColumn<>("Status", false, Comparator.comparing(PurchaseReturn::getPurchaseReturnStatus));
        MFXTableColumn<PurchaseReturn> purchaseReturnGrandTotal = new MFXTableColumn<>("Total", false, Comparator.comparing(PurchaseReturn::getPurchaseReturnGrandTotal));
        MFXTableColumn<PurchaseReturn> purchaseReturnAmountPaid = new MFXTableColumn<>("Paid", false, Comparator.comparing(PurchaseReturn::getPurchaseReturnAmountPaid));
        MFXTableColumn<PurchaseReturn> purchaseReturnAmountDue = new MFXTableColumn<>("Due", false, Comparator.comparing(PurchaseReturn::getPurchaseReturnAmountDue));
        MFXTableColumn<PurchaseReturn> purchaseReturnPaymentStatus = new MFXTableColumn<>("Payment Status", false, Comparator.comparing(PurchaseReturn::getPurchaseReturnPaymentStatus));

        purchaseReturnReturnRef.setTooltip(new Tooltip("Purchase Reference Number"));
        purchaseReturnReference.setTooltip(new Tooltip("Purchase Return Reference Number"));
        purchaseReturnPaymentStatus.setTooltip(new Tooltip("Purchase Return Payment Status"));
        purchaseReturnStatus.setTooltip(new Tooltip("Purchase Return Status"));
        purchaseReturnBranch.setTooltip(new Tooltip("Branch, store or warehouse"));
//        purchaseReturnPaymentStatus.setPrefWidth(100);
//        purchaseReturnAmountDue.setPrefWidth(100);
//        purchaseReturnSupplier.setPrefWidth(100);

        purchaseReturnDate.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturn::getPurchaseReturnDate));
        purchaseReturnReference.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturn::getPurchaseReturnReference));
        purchaseReturnSupplier.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturn::getPurchaseReturnSupplier));
        purchaseReturnBranch.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturn::getPurchaseReturnBranch));
        purchaseReturnReturnRef.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturn::getPurchaseReturnRef));
        purchaseReturnStatus.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturn::getPurchaseReturnStatus));
        purchaseReturnGrandTotal.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturn::getPurchaseReturnGrandTotal));
        purchaseReturnAmountPaid.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturn::getPurchaseReturnAmountPaid));
        purchaseReturnAmountDue.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturn::getPurchaseReturnAmountDue));
        purchaseReturnPaymentStatus.setRowCellFactory(purchaseReturn -> new MFXTableRowCell<>(PurchaseReturn::getPurchaseReturnPaymentStatus));

        purchaseReturnTable.getTableColumns().addAll(purchaseReturnDate, purchaseReturnReference, purchaseReturnSupplier, purchaseReturnBranch, purchaseReturnReturnRef, purchaseReturnStatus, purchaseReturnGrandTotal, purchaseReturnAmountPaid, purchaseReturnAmountDue, purchaseReturnPaymentStatus);
        purchaseReturnTable.getFilters().addAll(new StringFilter<>("Ref No.", PurchaseReturn::getPurchaseReturnReference), new StringFilter<>("Supplier", PurchaseReturn::getPurchaseReturnSupplier), new StringFilter<>("Branch", PurchaseReturn::getPurchaseReturnBranch), new StringFilter<>("Purchase Ref", PurchaseReturn::getPurchaseReturnRef), new StringFilter<>("Status", PurchaseReturn::getPurchaseReturnStatus), new DoubleFilter<>("Total", PurchaseReturn::getPurchaseReturnGrandTotal), new DoubleFilter<>("Paid", PurchaseReturn::getPurchaseReturnAmountPaid), new DoubleFilter<>("Due", PurchaseReturn::getPurchaseReturnAmountDue), new StringFilter<>("Payment Status", PurchaseReturn::getPurchaseReturnPaymentStatus));

        stylePurchaseReturnTable();
        purchaseReturnTable.setItems(purchaseReturnSampleData());
    }

    private void stylePurchaseReturnTable() {
        purchaseReturnTable.setPrefSize(1200, 1000);
        purchaseReturnTable.features().enableBounceEffect();
        purchaseReturnTable.autosizeColumnsOnInitialization();
        purchaseReturnTable.features().enableSmoothScrolling(0.5);
    }
}
