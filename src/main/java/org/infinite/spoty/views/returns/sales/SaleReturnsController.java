package org.infinite.spoty.views.returns.sales;

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
import org.infinite.spoty.models.SaleReturn;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.data.SampleData.saleReturnSampleData;

public class SaleReturnsController implements Initializable {
    @FXML
    public BorderPane saleReturnContentPane;
    @FXML
    public MFXTextField saleReturnSearchBar;
    @FXML
    public HBox saleReturnActionsPane;
    @FXML
    public MFXButton saleReturnImportBtn;
    @FXML
    private MFXTableView<SaleReturn> saleReturnTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<SaleReturn> saleReturnDate = new MFXTableColumn<>("Date", false, Comparator.comparing(SaleReturn::getSaleReturnDate));
        MFXTableColumn<SaleReturn> saleReturnReference = new MFXTableColumn<>("Ref No.", false, Comparator.comparing(SaleReturn::getSaleReturnReference));
        MFXTableColumn<SaleReturn> saleReturnCustomer = new MFXTableColumn<>("Customer", false, Comparator.comparing(SaleReturn::getSaleReturnCustomer));
        MFXTableColumn<SaleReturn> saleReturnBranch = new MFXTableColumn<>("Branch", false, Comparator.comparing(SaleReturn::getSaleReturnBranch));
        MFXTableColumn<SaleReturn> saleReturnRef = new MFXTableColumn<>("Sale Ref", false, Comparator.comparing(SaleReturn::getSaleReturnStatus));
        MFXTableColumn<SaleReturn> saleReturnStatus = new MFXTableColumn<>("Status", false, Comparator.comparing(SaleReturn::getSaleReturnStatus));
        MFXTableColumn<SaleReturn> saleReturnGrandTotal = new MFXTableColumn<>("Total", false, Comparator.comparing(SaleReturn::getSaleReturnGrandTotal));
        MFXTableColumn<SaleReturn> saleReturnAmountPaid = new MFXTableColumn<>("Paid", false, Comparator.comparing(SaleReturn::getSaleReturnAmountPaid));
        MFXTableColumn<SaleReturn> saleReturnAmountDue = new MFXTableColumn<>("Due", false, Comparator.comparing(SaleReturn::getSaleReturnAmountDue));
        MFXTableColumn<SaleReturn> saleReturnPaymentStatus = new MFXTableColumn<>("Payment Status", false, Comparator.comparing(SaleReturn::getSaleReturnPaymentStatus));

        saleReturnRef.setTooltip(new Tooltip("Purchase Reference Number"));
        saleReturnReference.setTooltip(new Tooltip("Purchase Return Reference Number"));
        saleReturnPaymentStatus.setTooltip(new Tooltip("Purchase Return Payment Status"));
        saleReturnStatus.setTooltip(new Tooltip("Purchase Return Status"));
        saleReturnBranch.setTooltip(new Tooltip("Branch, store or warehouse"));
//        saleReturnPaymentStatus.setPrefWidth(100);
//        saleReturnAmountDue.setPrefWidth(100);
//        saleReturnStatus.setPrefWidth(100);
//        saleReturnCustomer.setPrefWidth(100);

        saleReturnDate.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturn::getSaleReturnDate));
        saleReturnReference.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturn::getSaleReturnReference));
        saleReturnCustomer.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturn::getSaleReturnCustomer));
        saleReturnBranch.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturn::getSaleReturnBranch));
        saleReturnRef.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturn::getSaleReturnStatus));
        saleReturnStatus.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturn::getSaleReturnStatus));
        saleReturnGrandTotal.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturn::getSaleReturnGrandTotal));
        saleReturnAmountPaid.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturn::getSaleReturnAmountPaid));
        saleReturnAmountDue.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturn::getSaleReturnAmountDue));
        saleReturnPaymentStatus.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturn::getSaleReturnPaymentStatus));

        saleReturnTable.getTableColumns().addAll(saleReturnDate, saleReturnReference, saleReturnCustomer, saleReturnBranch, saleReturnRef, saleReturnStatus, saleReturnGrandTotal, saleReturnAmountPaid, saleReturnAmountDue, saleReturnPaymentStatus);
        saleReturnTable.getFilters().addAll(
                new StringFilter<>("Reference", SaleReturn::getSaleReturnReference),
                new StringFilter<>("Customer", SaleReturn::getSaleReturnCustomer),
                new StringFilter<>("Branch", SaleReturn::getSaleReturnBranch),
                new StringFilter<>("Sale Ref", SaleReturn::getSaleReturnStatus),
                new StringFilter<>("Status", SaleReturn::getSaleReturnStatus),
                new DoubleFilter<>("Total", SaleReturn::getSaleReturnGrandTotal),
                new DoubleFilter<>("Paid", SaleReturn::getSaleReturnAmountPaid),
                new DoubleFilter<>("Due", SaleReturn::getSaleReturnAmountDue),
                new StringFilter<>("Payment Status", SaleReturn::getSaleReturnPaymentStatus)
        );
        getSaleReturnTable();
        saleReturnTable.setItems(saleReturnSampleData());
    }

    private void getSaleReturnTable() {
        saleReturnTable.setPrefSize(1200, 1000);
        saleReturnTable.features().enableBounceEffect();
        saleReturnTable.autosizeColumnsOnInitialization();
        saleReturnTable.features().enableSmoothScrolling(0.5);
    }
}
