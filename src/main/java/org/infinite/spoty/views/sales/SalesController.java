package org.infinite.spoty.views.sales;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.infinite.spoty.database.dao.SaleMasterDao;
import org.infinite.spoty.database.models.SaleMaster;
import org.infinite.spoty.forms.SaleMasterFormController;
import org.infinite.spoty.viewModels.SaleMasterViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

@SuppressWarnings("unchecked")
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
    private MFXTableView<SaleMaster> saleMasterTable;

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
//        MFXTableColumn<SaleMaster> saleAddedBy = new MFXTableColumn<>("Added By", false, Comparator.comparing(SaleMaster::getAddedBy));
        MFXTableColumn<SaleMaster> saleCustomer = new MFXTableColumn<>("Customer", false, Comparator.comparing(SaleMaster::getCustomerName));
        MFXTableColumn<SaleMaster> saleBranch = new MFXTableColumn<>("Branch", false, Comparator.comparing(SaleMaster::getBranchName));
        MFXTableColumn<SaleMaster> saleStatus = new MFXTableColumn<>("Sale Status", false, Comparator.comparing(SaleMaster::getSaleStatus));
        MFXTableColumn<SaleMaster> saleGrandTotal = new MFXTableColumn<>("Total", false, Comparator.comparing(SaleMaster::getTotal));
        MFXTableColumn<SaleMaster> saleAmountPaid = new MFXTableColumn<>("Paid", false, Comparator.comparing(SaleMaster::getAmountPaid));
        MFXTableColumn<SaleMaster> saleAmountDue = new MFXTableColumn<>("Amount Due", false, Comparator.comparing(SaleMaster::getAmountDue));
        MFXTableColumn<SaleMaster> salePaymentStatus = new MFXTableColumn<>("Pay Status", false, Comparator.comparing(SaleMaster::getPaymentStatus));

        saleDate.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getLocaleDate));
        saleReference.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getRef));
//        saleAddedBy.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getAddedBy));
        saleCustomer.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getCustomerName));
        saleBranch.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getBranchName));
        saleStatus.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getSaleStatus));
        saleGrandTotal.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getTotal));
        saleAmountPaid.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getAmountPaid));
        saleAmountDue.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getAmountDue));
        salePaymentStatus.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getPaymentStatus));

        saleDate.prefWidthProperty().bind(saleMasterTable.widthProperty().multiply(.1));
        saleReference.prefWidthProperty().bind(saleMasterTable.widthProperty().multiply(.1));
//        saleAddedBy.prefWidthProperty().bind(saleTable.widthProperty().multiply(.1));
        saleCustomer.prefWidthProperty().bind(saleMasterTable.widthProperty().multiply(.1));
        saleBranch.prefWidthProperty().bind(saleMasterTable.widthProperty().multiply(.1));
        saleStatus.prefWidthProperty().bind(saleMasterTable.widthProperty().multiply(.1));
        saleGrandTotal.prefWidthProperty().bind(saleMasterTable.widthProperty().multiply(.1));
        saleAmountPaid.prefWidthProperty().bind(saleMasterTable.widthProperty().multiply(.1));
        saleAmountDue.prefWidthProperty().bind(saleMasterTable.widthProperty().multiply(.1));
        salePaymentStatus.prefWidthProperty().bind(saleMasterTable.widthProperty().multiply(.1));

        saleMasterTable.getTableColumns().addAll(saleDate, saleReference, saleCustomer, saleBranch, saleStatus, saleGrandTotal, saleAmountPaid, saleAmountDue, salePaymentStatus);
        saleMasterTable.getFilters().addAll(
                new StringFilter<>("Reference", SaleMaster::getRef),
//                new StringFilter<>("Added By", SaleMaster::getAddedBy),
                new StringFilter<>("Customer", SaleMaster::getCustomerName),
                new StringFilter<>("Branch", SaleMaster::getBranchName),
                new StringFilter<>("Sale Status", SaleMaster::getSaleStatus),
                new DoubleFilter<>("Grand Total", SaleMaster::getTotal),
                new DoubleFilter<>("Amount Paid", SaleMaster::getAmountPaid),
                new DoubleFilter<>("Amount Due", SaleMaster::getAmountDue),
                new StringFilter<>("Payment Status", SaleMaster::getPaymentStatus)
        );
        styleSaleMasterTable();
        saleMasterTable.setItems(SaleMasterViewModel.getSaleMasters());
    }

    private void styleSaleMasterTable() {
        saleMasterTable.setPrefSize(1200, 1000);
        saleMasterTable.features().enableBounceEffect();
        saleMasterTable.features().enableSmoothScrolling(0.5);

        saleMasterTable.setTableRowFactory(t -> {
            MFXTableRow<SaleMaster> row = new MFXTableRow<>(saleMasterTable, t);
            EventHandler<ContextMenuEvent> eventHandler = event -> {
                showContextMenu((MFXTableRow<SaleMaster>) event.getSource())
                        .show(saleMasterTable.getScene().getWindow(), event.getScreenX(), event.getScreenY());
                event.consume();
            };
            row.setOnContextMenuRequested(eventHandler);
            return row;
        });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<SaleMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(saleMasterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(e -> {
            SaleMasterDao.deleteSaleMaster(obj.getData().getId());
            SaleMasterViewModel.getSaleMasters();
            e.consume();
        });
        // Edit
        edit.setOnAction(e -> {
            SaleMasterViewModel.getItem(obj.getData().getId());
            saleCreateBtnClicked();
            e.consume();
        });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    public void saleCreateBtnClicked() {
        FXMLLoader loader = fxmlLoader("forms/SaleMasterForm.fxml");
        loader.setControllerFactory(c -> new SaleMasterFormController(stage));
        try {
            BorderPane productFormPane = loader.load();
            ((StackPane) saleContentPane.getParent()).getChildren().add(productFormPane);
            ((StackPane) saleContentPane.getParent()).getChildren().get(0).setVisible(false);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
