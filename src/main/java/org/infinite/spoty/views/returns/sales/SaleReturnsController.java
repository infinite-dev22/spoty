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
import org.infinite.spoty.database.models.SaleReturnMaster;
import org.infinite.spoty.viewModels.SaleReturnMasterViewModel;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

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
    private MFXTableView<SaleReturnMaster> saleReturnTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<SaleReturnMaster> saleReturnDate = new MFXTableColumn<>("Date", false, Comparator.comparing(SaleReturnMaster::getDate));
        MFXTableColumn<SaleReturnMaster> saleReturnReference = new MFXTableColumn<>("Ref No.", false, Comparator.comparing(SaleReturnMaster::getRef));
        MFXTableColumn<SaleReturnMaster> saleReturnCustomer = new MFXTableColumn<>("Customer", false, Comparator.comparing(SaleReturnMaster::getCustomerName));
        MFXTableColumn<SaleReturnMaster> saleReturnBranch = new MFXTableColumn<>("Branch", false, Comparator.comparing(SaleReturnMaster::getBranchName));
        MFXTableColumn<SaleReturnMaster> saleReturnStatus = new MFXTableColumn<>("Status", false, Comparator.comparing(SaleReturnMaster::getStatus));
        MFXTableColumn<SaleReturnMaster> saleReturnGrandTotal = new MFXTableColumn<>("Total", false, Comparator.comparing(SaleReturnMaster::getTotal));
        MFXTableColumn<SaleReturnMaster> saleReturnAmountPaid = new MFXTableColumn<>("Paid", false, Comparator.comparing(SaleReturnMaster::getPaid));
        MFXTableColumn<SaleReturnMaster> saleReturnPaymentStatus = new MFXTableColumn<>("Pay Status", false, Comparator.comparing(SaleReturnMaster::getPaymentStatus));

        saleReturnReference.setTooltip(new Tooltip("PurchaseMaster Return Reference Number"));
        saleReturnPaymentStatus.setTooltip(new Tooltip("PurchaseMaster Return Payment Status"));
        saleReturnStatus.setTooltip(new Tooltip("PurchaseMaster Return Status"));
        saleReturnBranch.setTooltip(new Tooltip("Branch, store or warehouse"));

        saleReturnDate.prefWidthProperty().bind(saleReturnTable.widthProperty().multiply(.13));
        saleReturnReference.prefWidthProperty().bind(saleReturnTable.widthProperty().multiply(.13));
        saleReturnCustomer.prefWidthProperty().bind(saleReturnTable.widthProperty().multiply(.13));
        saleReturnBranch.prefWidthProperty().bind(saleReturnTable.widthProperty().multiply(.13));
        saleReturnStatus.prefWidthProperty().bind(saleReturnTable.widthProperty().multiply(.13));
        saleReturnGrandTotal.prefWidthProperty().bind(saleReturnTable.widthProperty().multiply(.13));
        saleReturnAmountPaid.prefWidthProperty().bind(saleReturnTable.widthProperty().multiply(.13));
        saleReturnPaymentStatus.prefWidthProperty().bind(saleReturnTable.widthProperty().multiply(.13));

        saleReturnDate.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getDate));
        saleReturnReference.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getRef));
        saleReturnCustomer.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getCustomerName));
        saleReturnBranch.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getBranchName));
        saleReturnStatus.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getStatus));
        saleReturnGrandTotal.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getTotal));
        saleReturnAmountPaid.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getPaid));
        saleReturnPaymentStatus.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getPaymentStatus));

        saleReturnTable.getTableColumns().addAll(saleReturnDate, saleReturnReference, saleReturnCustomer, saleReturnBranch, saleReturnStatus, saleReturnGrandTotal, saleReturnAmountPaid, saleReturnPaymentStatus);
        saleReturnTable.getFilters().addAll(
                new StringFilter<>("Reference", SaleReturnMaster::getRef),
                new StringFilter<>("Customer", SaleReturnMaster::getCustomerName),
                new StringFilter<>("Branch", SaleReturnMaster::getBranchName),
                new StringFilter<>("Status", SaleReturnMaster::getStatus),
                new DoubleFilter<>("Total", SaleReturnMaster::getTotal),
                new DoubleFilter<>("Paid", SaleReturnMaster::getPaid),
                new StringFilter<>("Pay Status", SaleReturnMaster::getPaymentStatus)
        );
        getSaleReturnMasterTable();
        saleReturnTable.setItems(SaleReturnMasterViewModel.getSaleReturnMasters());
    }

    private void getSaleReturnMasterTable() {
        saleReturnTable.setPrefSize(1200, 1000);
        saleReturnTable.features().enableBounceEffect();
        saleReturnTable.autosizeColumnsOnInitialization();
        saleReturnTable.features().enableSmoothScrolling(0.5);
    }
}
