package org.infinite.spoty.views.requisition;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
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
import org.infinite.spoty.database.models.RequisitionMaster;
import org.infinite.spoty.forms.RequisitionMasterFormController;
import org.infinite.spoty.viewModels.RequisitionMasterViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

public class RequisitionController implements Initializable {
    private final Stage stage;
    @FXML
    public MFXTextField requisitionSearchBar;
    @FXML
    public HBox requisitionActionsPane;
    @FXML
    public MFXButton requisitionImportBtn;
    @FXML
    public MFXTableView<RequisitionMaster> requisitionsTable;
    @FXML
    public BorderPane requisitionContentPane;

    public RequisitionController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<RequisitionMaster> requisitionDate = new MFXTableColumn<>("Date", false, Comparator.comparing(RequisitionMaster::getDate));
        MFXTableColumn<RequisitionMaster> requisitionReference = new MFXTableColumn<>("Reference", false, Comparator.comparing(RequisitionMaster::getRef));
        MFXTableColumn<RequisitionMaster> requisitionBranch = new MFXTableColumn<>("Branch", false, Comparator.comparing(RequisitionMaster::getBranchName));
        MFXTableColumn<RequisitionMaster> requisitionSupplier = new MFXTableColumn<>("Supplier", false, Comparator.comparing(RequisitionMaster::getSupplierName));
        MFXTableColumn<RequisitionMaster> requisitionDelivery = new MFXTableColumn<>("Delivery Date", false, Comparator.comparing(RequisitionMaster::getDeliveryDate));
        MFXTableColumn<RequisitionMaster> requisitionStatus = new MFXTableColumn<>("Status", false, Comparator.comparing(RequisitionMaster::getStatus));
        MFXTableColumn<RequisitionMaster> requisitionShippingMethod = new MFXTableColumn<>("Shipping Method", false, Comparator.comparing(RequisitionMaster::getShipMethod));
//        MFXTableColumn<RequisitionMaster> requisitionTotalProducts = new MFXTableColumn<>("Total Products", false, Comparator.comparing(RequisitionMaster::getRequisitionMasterTotalProducts));

        requisitionDate.setRowCellFactory(requisition -> new MFXTableRowCell<>(RequisitionMaster::getDate));
        requisitionReference.setRowCellFactory(requisition -> new MFXTableRowCell<>(RequisitionMaster::getRef));
        requisitionBranch.setRowCellFactory(requisition -> new MFXTableRowCell<>(RequisitionMaster::getBranchName));
        requisitionSupplier.setRowCellFactory(requisition -> new MFXTableRowCell<>(RequisitionMaster::getSupplierName));
        requisitionDelivery.setRowCellFactory(requisition -> new MFXTableRowCell<>(RequisitionMaster::getDeliveryDateString));
        requisitionStatus.setRowCellFactory(requisition -> new MFXTableRowCell<>(RequisitionMaster::getStatus));
        requisitionShippingMethod.setRowCellFactory(requisition -> new MFXTableRowCell<>(RequisitionMaster::getShipMethod));

        requisitionDate.prefWidthProperty().bind(requisitionsTable.widthProperty().multiply(.15));
        requisitionReference.prefWidthProperty().bind(requisitionsTable.widthProperty().multiply(.15));
        requisitionBranch.prefWidthProperty().bind(requisitionsTable.widthProperty().multiply(.15));
        requisitionSupplier.prefWidthProperty().bind(requisitionsTable.widthProperty().multiply(.15));
        requisitionDelivery.prefWidthProperty().bind(requisitionsTable.widthProperty().multiply(.15));
        requisitionStatus.prefWidthProperty().bind(requisitionsTable.widthProperty().multiply(.15));
        requisitionShippingMethod.prefWidthProperty().bind(requisitionsTable.widthProperty().multiply(.15));
//        requisitionTotalProducts.setRowCellFactory(requisition -> new MFXTableRowCell<>(RequisitionMaster::getRequisitionMasterTotalProducts));

        requisitionsTable.getTableColumns().addAll(requisitionDate,
                requisitionReference,
                requisitionBranch,
                requisitionSupplier,
                requisitionDelivery,
                requisitionStatus,
                requisitionShippingMethod); //, requisitionTotalProducts);
        requisitionsTable.getFilters().addAll(
                new StringFilter<>("Reference", RequisitionMaster::getRef),
                new StringFilter<>("Branch", RequisitionMaster::getBranchName),
                new StringFilter<>("Supplier", RequisitionMaster::getSupplierName),
                new StringFilter<>("Delivery Date", RequisitionMaster::getDeliveryDateString),
                new StringFilter<>("Status", RequisitionMaster::getStatus),
                new StringFilter<>("Shipping Method", RequisitionMaster::getShipMethod)
//                new DoubleFilter<>("Total Products", RequisitionMaster::getRequisitionMasterTotalProducts)
        );
        getRequisitionMasterTable();
        requisitionsTable.setItems(RequisitionMasterViewModel.getRequisitionMasters());
    }

    private void getRequisitionMasterTable() {
        requisitionsTable.setPrefSize(1000, 1000);
        requisitionsTable.features().enableBounceEffect();
        requisitionsTable.features().enableSmoothScrolling(0.5);
    }

    public void requisitionCreateBtnClicked() {
        FXMLLoader loader = fxmlLoader("forms/RequisitionMasterForm.fxml");
        loader.setControllerFactory(c -> new RequisitionMasterFormController(stage));
        try {
            AnchorPane productFormPane = loader.load();
            ((StackPane) requisitionContentPane.getParent()).getChildren().add(productFormPane);
            ((StackPane) requisitionContentPane.getParent()).getChildren().get(0).setVisible(false);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
