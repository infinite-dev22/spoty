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
import org.infinite.spoty.database.models.SaleMaster;
import org.infinite.spoty.forms.SaleFormController;
import org.infinite.spoty.viewModels.SaleMasterViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

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
    private MFXTableView<SaleMaster> saleTable;

    public SalesController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<SaleMaster> saleDate = new MFXTableColumn<>("Date", false, Comparator.comparing(SaleMaster::getDate));
        MFXTableColumn<SaleMaster> saleReference = new MFXTableColumn<>("Reference", false, Comparator.comparing(SaleMaster::getRef));
        MFXTableColumn<SaleMaster> saleAddedBy = new MFXTableColumn<>("Added By", false, Comparator.comparing(SaleMaster::getAddedBy));
        MFXTableColumn<SaleMaster> saleCustomer = new MFXTableColumn<>("Customer", false, Comparator.comparing(SaleMaster::getCustomerName));
        MFXTableColumn<SaleMaster> saleBranch = new MFXTableColumn<>("Branch", false, Comparator.comparing(SaleMaster::getBranchName));
        MFXTableColumn<SaleMaster> saleStatus = new MFXTableColumn<>("Sale Status", false, Comparator.comparing(SaleMaster::getSaleStatus));
        MFXTableColumn<SaleMaster> saleGrandTotal = new MFXTableColumn<>("Total", false, Comparator.comparing(SaleMaster::getTotal));
        MFXTableColumn<SaleMaster> saleAmountPaid = new MFXTableColumn<>("Paid", false, Comparator.comparing(SaleMaster::getAmountPaid));
        MFXTableColumn<SaleMaster> saleAmountDue = new MFXTableColumn<>("Amount Due", false, Comparator.comparing(SaleMaster::getAmountDue));
        MFXTableColumn<SaleMaster> salePaymentStatus = new MFXTableColumn<>("Pay Status", false, Comparator.comparing(SaleMaster::getPaymentStatus));

        saleDate.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getDate));
        saleReference.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getRef));
        saleAddedBy.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getAddedBy));
        saleCustomer.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getCustomerName));
        saleBranch.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getBranchName));
        saleStatus.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getSaleStatus));
        saleGrandTotal.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getTotal));
        saleAmountPaid.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getAmountPaid));
        saleAmountDue.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getAmountDue));
        salePaymentStatus.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getPaymentStatus));

        saleDate.prefWidthProperty().bind(saleTable.widthProperty().multiply(.1));
        saleReference.prefWidthProperty().bind(saleTable.widthProperty().multiply(.1));
        saleAddedBy.prefWidthProperty().bind(saleTable.widthProperty().multiply(.1));
        saleCustomer.prefWidthProperty().bind(saleTable.widthProperty().multiply(.1));
        saleBranch.prefWidthProperty().bind(saleTable.widthProperty().multiply(.1));
        saleStatus.prefWidthProperty().bind(saleTable.widthProperty().multiply(.1));
        saleGrandTotal.prefWidthProperty().bind(saleTable.widthProperty().multiply(.1));
        saleAmountPaid.prefWidthProperty().bind(saleTable.widthProperty().multiply(.1));
        saleAmountDue.prefWidthProperty().bind(saleTable.widthProperty().multiply(.1));
        salePaymentStatus.prefWidthProperty().bind(saleTable.widthProperty().multiply(.1));

        saleTable.getTableColumns().addAll(saleDate, saleReference, saleAddedBy, saleCustomer, saleBranch, saleStatus, saleGrandTotal, saleAmountPaid, saleAmountDue, salePaymentStatus);
        saleTable.getFilters().addAll(
                new StringFilter<>("Reference", SaleMaster::getRef),
                new StringFilter<>("Added By", SaleMaster::getAddedBy),
                new StringFilter<>("Customer", SaleMaster::getCustomerName),
                new StringFilter<>("Branch", SaleMaster::getBranchName),
                new StringFilter<>("Sale Status", SaleMaster::getSaleStatus),
                new DoubleFilter<>("Grand Total", SaleMaster::getTotal),
                new DoubleFilter<>("Amount Paid", SaleMaster::getAmountPaid),
                new DoubleFilter<>("Amount Due", SaleMaster::getAmountDue),
                new StringFilter<>("Payment Status", SaleMaster::getPaymentStatus)
        );
        styleSaleMasterTable();
        saleTable.setItems(SaleMasterViewModel.getSaleMasters());
    }

    private void styleSaleMasterTable() {
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
