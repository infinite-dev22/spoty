package org.infinite.spoty.views.stock_in;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.infinite.spoty.database.models.StockInMaster;
import org.infinite.spoty.forms.StockInMasterFormController;
import org.infinite.spoty.viewModels.StockInMasterViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

public class StockInController implements Initializable {
    private final Stage stage;
    @FXML
    public MFXTextField stockInSearchBar;
    @FXML
    public HBox stockInActionsPane;
    @FXML
    public MFXButton stockInImportBtn;
    @FXML
    public MFXTableView<StockInMaster> stockInTable;
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
        MFXTableColumn<StockInMaster> requisitionDate = new MFXTableColumn<>("Date", false, Comparator.comparing(StockInMaster::getDate));
        MFXTableColumn<StockInMaster> requisitionReference = new MFXTableColumn<>("Reference", false, Comparator.comparing(StockInMaster::getRef));
        MFXTableColumn<StockInMaster> requisitionBranch = new MFXTableColumn<>("Branch", false, Comparator.comparing(StockInMaster::getBranchName));
        MFXTableColumn<StockInMaster> requisitionStatus = new MFXTableColumn<>("Status", false, Comparator.comparing(StockInMaster::getStatus));
        MFXTableColumn<StockInMaster> requisitionTotalCost = new MFXTableColumn<>("Total Cost", false, Comparator.comparing(StockInMaster::getTotal));

        requisitionDate.setRowCellFactory(requisition -> new MFXTableRowCell<>(StockInMaster::getDate));
        requisitionReference.setRowCellFactory(requisition -> new MFXTableRowCell<>(StockInMaster::getRef));
        requisitionBranch.setRowCellFactory(requisition -> new MFXTableRowCell<>(StockInMaster::getBranchName));
        requisitionStatus.setRowCellFactory(requisition -> new MFXTableRowCell<>(StockInMaster::getStatus));
        requisitionTotalCost.setRowCellFactory(requisition -> new MFXTableRowCell<>(StockInMaster::getTotal));

        requisitionDate.prefWidthProperty().bind(stockInTable.widthProperty().multiply(.2));
        requisitionReference.prefWidthProperty().bind(stockInTable.widthProperty().multiply(.2));
        requisitionBranch.prefWidthProperty().bind(stockInTable.widthProperty().multiply(.2));
        requisitionStatus.prefWidthProperty().bind(stockInTable.widthProperty().multiply(.2));
        requisitionTotalCost.prefWidthProperty().bind(stockInTable.widthProperty().multiply(.2));

        stockInTable.getTableColumns().addAll(requisitionDate,
                requisitionReference,
                requisitionBranch,
                requisitionStatus,
                requisitionTotalCost);
        stockInTable.getFilters().addAll(
                new StringFilter<>("Reference", StockInMaster::getRef),
                new StringFilter<>("Branch", StockInMaster::getBranchName),
                new StringFilter<>("Status", StockInMaster::getStatus),
                new DoubleFilter<>("Total Cost", StockInMaster::getTotal)
        );
        getStockInMasterTable();
        stockInTable.setItems(StockInMasterViewModel.getStockInMasters());
    }

    private void getStockInMasterTable() {
        stockInTable.setPrefSize(1000, 1000);
        stockInTable.features().enableBounceEffect();
        stockInTable.features().enableSmoothScrolling(0.5);
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
