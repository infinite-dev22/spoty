package org.infinite.spoty.views.sales;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.infinite.spoty.forms.SaleFormController;
import org.infinite.spoty.models.Sale;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.data.SampleData.saleSampleData;

public class SalesController implements Initializable {
    private final Stage stage;
    @FXML
    public MFXTextField saleSearchBar;
    @FXML
    public HBox saleActionsPane;
    @FXML
    public MFXButton saleImportBtn;
    @FXML
    public MFXButton saleCreateBtn;
    @FXML
    public BorderPane saleContentPane;
    @FXML
    private MFXTableView<Sale> saleTable;

    public SalesController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Sale> saleDate = new MFXTableColumn<>("Date", false, Comparator.comparing(Sale::getSaleDate));
        MFXTableColumn<Sale> saleReference = new MFXTableColumn<>("Reference", false, Comparator.comparing(Sale::getSaleReference));
        MFXTableColumn<Sale> saleAddedBy = new MFXTableColumn<>("Added By", false, Comparator.comparing(Sale::getSaleAddedBy));
        MFXTableColumn<Sale> saleCustomer = new MFXTableColumn<>("Customer", false, Comparator.comparing(Sale::getSaleCustomer));
        MFXTableColumn<Sale> saleBranch = new MFXTableColumn<>("Branch", false, Comparator.comparing(Sale::getSaleBranch));
        MFXTableColumn<Sale> saleStatus = new MFXTableColumn<>("Sale Status", false, Comparator.comparing(Sale::getSaleStatus));
        MFXTableColumn<Sale> saleGrandTotal = new MFXTableColumn<>("Total", false, Comparator.comparing(Sale::getSaleGrandTotal));
        MFXTableColumn<Sale> saleAmountPaid = new MFXTableColumn<>("Paid", false, Comparator.comparing(Sale::getSaleAmountPaid));
        MFXTableColumn<Sale> saleAmountDue = new MFXTableColumn<>("Amount Due", false, Comparator.comparing(Sale::getSaleAmountDue));
        MFXTableColumn<Sale> salePaymentStatus = new MFXTableColumn<>("Pay Status", false, Comparator.comparing(Sale::getSalePaymentStatus));

        saleDate.setRowCellFactory(sale -> new MFXTableRowCell<>(Sale::getSaleDate));
        saleReference.setRowCellFactory(sale -> new MFXTableRowCell<>(Sale::getSaleReference));
        saleAddedBy.setRowCellFactory(sale -> new MFXTableRowCell<>(Sale::getSaleAddedBy));
        saleCustomer.setRowCellFactory(sale -> new MFXTableRowCell<>(Sale::getSaleCustomer));
        saleBranch.setRowCellFactory(sale -> new MFXTableRowCell<>(Sale::getSaleBranch));
        saleStatus.setRowCellFactory(sale -> new MFXTableRowCell<>(Sale::getSaleStatus));
        saleGrandTotal.setRowCellFactory(sale -> new MFXTableRowCell<>(Sale::getSaleGrandTotal));
        saleAmountPaid.setRowCellFactory(sale -> new MFXTableRowCell<>(Sale::getSaleAmountPaid));
        saleAmountDue.setRowCellFactory(sale -> new MFXTableRowCell<>(Sale::getSaleAmountDue));
        salePaymentStatus.setRowCellFactory(sale -> new MFXTableRowCell<>(Sale::getSalePaymentStatus));

        saleTable.getTableColumns().addAll(saleDate, saleReference, saleAddedBy, saleCustomer, saleBranch, saleStatus, saleGrandTotal, saleAmountPaid, saleAmountDue, salePaymentStatus);
        saleTable.getFilters().addAll(
                new StringFilter<>("Reference", Sale::getSaleReference),
                new StringFilter<>("Added By", Sale::getSaleAddedBy),
                new StringFilter<>("Customer", Sale::getSaleCustomer),
                new StringFilter<>("Branch", Sale::getSaleBranch),
                new StringFilter<>("Sale Status", Sale::getSaleStatus),
                new DoubleFilter<>("Grand Total", Sale::getSaleGrandTotal),
                new DoubleFilter<>("Amount Paid", Sale::getSaleAmountPaid),
                new DoubleFilter<>("Amount Due", Sale::getSaleAmountDue),
                new StringFilter<>("Payment Status", Sale::getSalePaymentStatus)
        );
        styleSaleTable();
        saleTable.setItems(saleSampleData());
    }

    private void styleSaleTable() {
        saleTable.setPrefSize(1200, 1000);
        saleTable.features().enableBounceEffect();
        saleTable.autosizeColumnsOnInitialization();
        saleTable.features().enableSmoothScrolling(0.5);
    }

    public void saleCreateBtnClicked() {
        FXMLLoader loader = fxmlLoader("forms/SaleForm.fxml");
        loader.setControllerFactory(c -> new SaleFormController(stage));
        try {
            BorderPane productFormPane = loader.load();
            ((StackPane) saleContentPane.getParent()).getChildren().add(productFormPane);
            ((StackPane) saleContentPane.getParent()).getChildren().get(0).setVisible(false);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
