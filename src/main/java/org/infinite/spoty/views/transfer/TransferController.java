package org.infinite.spoty.views.transfer;

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
import org.infinite.spoty.database.models.TransferMaster;
import org.infinite.spoty.forms.TransferMasterFormController;
import org.infinite.spoty.viewModels.TransferMasterViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

public class TransferController implements Initializable {
    private final Stage stage;
    @FXML
    public MFXTextField transferSearchBar;
    @FXML
    public HBox transferActionsPane;
    @FXML
    public MFXButton transferImportBtn;
    @FXML
    public MFXTableView<TransferMaster> transferTable;
    @FXML
    public BorderPane transferContentPane;

    public TransferController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<TransferMaster> requisitionDate = new MFXTableColumn<>("Date", false, Comparator.comparing(TransferMaster::getDate));
        MFXTableColumn<TransferMaster> requisitionReference = new MFXTableColumn<>("Reference", false, Comparator.comparing(TransferMaster::getRef));
        MFXTableColumn<TransferMaster> requisitionFromBranch = new MFXTableColumn<>("Branch(From)", false, Comparator.comparing(TransferMaster::getFromBranchName));
        MFXTableColumn<TransferMaster> requisitionToBranch = new MFXTableColumn<>("Branch(To)", false, Comparator.comparing(TransferMaster::getToBranchName));
        MFXTableColumn<TransferMaster> requisitionStatus = new MFXTableColumn<>("Status", false, Comparator.comparing(TransferMaster::getStatus));
        MFXTableColumn<TransferMaster> requisitionTotalCost = new MFXTableColumn<>("Total Cost", false, Comparator.comparing(TransferMaster::getTotal));

        requisitionDate.setRowCellFactory(requisition -> new MFXTableRowCell<>(TransferMaster::getDate));
        requisitionReference.setRowCellFactory(requisition -> new MFXTableRowCell<>(TransferMaster::getRef));
        requisitionFromBranch.setRowCellFactory(requisition -> new MFXTableRowCell<>(TransferMaster::getFromBranchName));
        requisitionToBranch.setRowCellFactory(requisition -> new MFXTableRowCell<>(TransferMaster::getToBranchName));
        requisitionStatus.setRowCellFactory(requisition -> new MFXTableRowCell<>(TransferMaster::getStatus));
        requisitionTotalCost.setRowCellFactory(requisition -> new MFXTableRowCell<>(TransferMaster::getTotal));

        requisitionDate.prefWidthProperty().bind(transferTable.widthProperty().multiply(.17));
        requisitionReference.prefWidthProperty().bind(transferTable.widthProperty().multiply(.17));
        requisitionFromBranch.prefWidthProperty().bind(transferTable.widthProperty().multiply(.17));
        requisitionToBranch.prefWidthProperty().bind(transferTable.widthProperty().multiply(.17));
        requisitionStatus.prefWidthProperty().bind(transferTable.widthProperty().multiply(.17));
        requisitionTotalCost.prefWidthProperty().bind(transferTable.widthProperty().multiply(.17));

        transferTable.getTableColumns().addAll(requisitionDate,
                requisitionReference,
                requisitionFromBranch,
                requisitionToBranch,
                requisitionStatus,
                requisitionTotalCost);
        transferTable.getFilters().addAll(
                new StringFilter<>("Reference", TransferMaster::getRef),
                new StringFilter<>("Branch(From)", TransferMaster::getFromBranchName),
                new StringFilter<>("Branch(To)", TransferMaster::getToBranchName),
                new StringFilter<>("Status", TransferMaster::getStatus),
                new DoubleFilter<>("Total Cost", TransferMaster::getTotal)
        );
        getTransferMasterTable();
        transferTable.setItems(TransferMasterViewModel.getTransferMasters());
    }

    private void getTransferMasterTable() {
        transferTable.setPrefSize(1000, 1000);
        transferTable.features().enableBounceEffect();
        transferTable.features().enableSmoothScrolling(0.5);
    }

    @FXML
    private void transferCreateBtnClicked() {
        FXMLLoader loader = fxmlLoader("forms/TransferMasterForm.fxml");
        loader.setControllerFactory(c -> new TransferMasterFormController(stage));
        try {
            AnchorPane productFormPane = loader.load();
            ((StackPane) transferContentPane.getParent()).getChildren().add(productFormPane);
            ((StackPane) transferContentPane.getParent()).getChildren().get(0).setVisible(false);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
