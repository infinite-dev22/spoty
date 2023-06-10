package org.infinite.spoty.views.stock_in;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.infinite.spoty.database.dao.StockInMasterDao;
import org.infinite.spoty.database.models.StockInMaster;
import org.infinite.spoty.forms.StockInMasterFormController;
import org.infinite.spoty.viewModels.StockInMasterViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

@SuppressWarnings("unchecked")
public class StockInController implements Initializable {
    private final Stage stage;
    @FXML
    public MFXTextField stockInSearchBar;
    @FXML
    public HBox stockInActionsPane;
    @FXML
    public MFXButton stockInImportBtn;
    @FXML
    public MFXTableView<StockInMaster> stockInMasterTable;
    @FXML
    public BorderPane stockInContentPane;

    public StockInController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<StockInMaster> stockInDate = new MFXTableColumn<>("Date", false, Comparator.comparing(StockInMaster::getDate));
        MFXTableColumn<StockInMaster> stockInReference = new MFXTableColumn<>("Reference", false, Comparator.comparing(StockInMaster::getRef));
        MFXTableColumn<StockInMaster> stockInBranch = new MFXTableColumn<>("Branch", false, Comparator.comparing(StockInMaster::getBranchName));
        MFXTableColumn<StockInMaster> stockInStatus = new MFXTableColumn<>("Status", false, Comparator.comparing(StockInMaster::getStatus));
        MFXTableColumn<StockInMaster> stockInTotalCost = new MFXTableColumn<>("Total Cost", false, Comparator.comparing(StockInMaster::getTotal));

        stockInDate.setRowCellFactory(stockIn -> new MFXTableRowCell<>(StockInMaster::getDate));
        stockInReference.setRowCellFactory(stockIn -> new MFXTableRowCell<>(StockInMaster::getRef));
        stockInBranch.setRowCellFactory(stockIn -> new MFXTableRowCell<>(StockInMaster::getBranchName));
        stockInStatus.setRowCellFactory(stockIn -> new MFXTableRowCell<>(StockInMaster::getStatus));
        stockInTotalCost.setRowCellFactory(stockIn -> new MFXTableRowCell<>(StockInMaster::getTotal));

        stockInDate.prefWidthProperty().bind(stockInMasterTable.widthProperty().multiply(.2));
        stockInReference.prefWidthProperty().bind(stockInMasterTable.widthProperty().multiply(.2));
        stockInBranch.prefWidthProperty().bind(stockInMasterTable.widthProperty().multiply(.2));
        stockInStatus.prefWidthProperty().bind(stockInMasterTable.widthProperty().multiply(.2));
        stockInTotalCost.prefWidthProperty().bind(stockInMasterTable.widthProperty().multiply(.2));

        stockInMasterTable.getTableColumns().addAll(stockInDate,
                stockInReference,
                stockInBranch,
                stockInStatus,
                stockInTotalCost);
        stockInMasterTable.getFilters().addAll(
                new StringFilter<>("Reference", StockInMaster::getRef),
                new StringFilter<>("Branch", StockInMaster::getBranchName),
                new StringFilter<>("Status", StockInMaster::getStatus),
                new DoubleFilter<>("Total Cost", StockInMaster::getTotal)
        );
        getStockInMasterTable();
        stockInMasterTable.setItems(StockInMasterViewModel.getStockInMasters());
    }

    private void getStockInMasterTable() {
        stockInMasterTable.setPrefSize(1000, 1000);
        stockInMasterTable.features().enableBounceEffect();
        stockInMasterTable.features().enableSmoothScrolling(0.5);

        stockInMasterTable.setTableRowFactory(t -> {
            MFXTableRow<StockInMaster> row = new MFXTableRow<>(stockInMasterTable, t);
            EventHandler<ContextMenuEvent> eventHandler = event -> {
                showContextMenu((MFXTableRow<StockInMaster>) event.getSource()).show(stockInMasterTable.getParent(), event.getScreenX(), event.getScreenY());
                event.consume();
            };
            row.setOnContextMenuRequested(eventHandler);
            return row;
        });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<StockInMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(stockInMasterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(e -> {
            StockInMasterDao.deleteStockInMaster(obj.getData().getId());
            StockInMasterViewModel.getStockInMasters();
            e.consume();
        });
        // Edit
        edit.setOnAction(e -> {
            StockInMasterViewModel.getItem(obj.getData().getId());
            stockInCreateBtnClicked();
            e.consume();
        });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    @FXML
    private void stockInCreateBtnClicked() {
        FXMLLoader loader = fxmlLoader("forms/StockInMasterForm.fxml");
        loader.setControllerFactory(c -> new StockInMasterFormController(stage));
        try {
            AnchorPane productFormPane = loader.load();
            ((StackPane) stockInContentPane.getParent()).getChildren().add(productFormPane);
            ((StackPane) stockInContentPane.getParent()).getChildren().get(0).setVisible(false);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
