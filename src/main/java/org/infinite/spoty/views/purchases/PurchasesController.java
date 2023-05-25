package org.infinite.spoty.views.purchases;

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
import org.infinite.spoty.forms.PurchaseFormController;
import org.infinite.spoty.database.models.PurchaseMaster;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.viewModels.PurchaseMasterViewModel.getPurchaseMasters;

public class PurchasesController implements Initializable {
    private final Stage stage;
    @FXML
    public MFXTextField purchaseSearchBar;
    @FXML
    public HBox purchaseActionsPane;
    @FXML
    public MFXButton purchaseImportBtn;
    @FXML
    public BorderPane purchaseContentPane;
    @FXML
    private MFXTableView<PurchaseMaster> purchaseTable;

    public PurchasesController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<PurchaseMaster> purchaseMasterDate = new MFXTableColumn<>("Date", false, Comparator.comparing(PurchaseMaster::getDate));
        MFXTableColumn<PurchaseMaster> purchaseMasterReference = new MFXTableColumn<>("Reference", false, Comparator.comparing(PurchaseMaster::getRef));
        MFXTableColumn<PurchaseMaster> purchaseMasterSupplier = new MFXTableColumn<>("Supplier", false, Comparator.comparing(PurchaseMaster::getSupplierName));
        MFXTableColumn<PurchaseMaster> purchaseMasterBranch = new MFXTableColumn<>("Branch", false, Comparator.comparing(PurchaseMaster::getBranchName));
        MFXTableColumn<PurchaseMaster> purchaseMasterStatus = new MFXTableColumn<>("Status", false, Comparator.comparing(PurchaseMaster::getStatus));
        MFXTableColumn<PurchaseMaster> purchaseMasterGrandTotal = new MFXTableColumn<>("Total", false, Comparator.comparing(PurchaseMaster::getTotal));
        MFXTableColumn<PurchaseMaster> purchaseMasterAmountPaid = new MFXTableColumn<>("Paid", false, Comparator.comparing(PurchaseMaster::getPaid));
        MFXTableColumn<PurchaseMaster> purchaseMasterAmountDue = new MFXTableColumn<>("Due", false, Comparator.comparing(PurchaseMaster::getDue));
        MFXTableColumn<PurchaseMaster> purchaseMasterPaymentStatus = new MFXTableColumn<>("Pay Status", false, Comparator.comparing(PurchaseMaster::getPaymentStatus));

        purchaseMasterDate.setRowCellFactory(purchaseMaster -> new MFXTableRowCell<>(PurchaseMaster::getDate));
        purchaseMasterReference.setRowCellFactory(purchaseMaster -> new MFXTableRowCell<>(PurchaseMaster::getRef));
        purchaseMasterSupplier.setRowCellFactory(purchaseMaster -> new MFXTableRowCell<>(PurchaseMaster::getSupplierName));
        purchaseMasterBranch.setRowCellFactory(purchaseMaster -> new MFXTableRowCell<>(PurchaseMaster::getBranchName));
        purchaseMasterStatus.setRowCellFactory(purchaseMaster -> new MFXTableRowCell<>(PurchaseMaster::getStatus));
        purchaseMasterGrandTotal.setRowCellFactory(purchaseMaster -> new MFXTableRowCell<>(PurchaseMaster::getTotal));
        purchaseMasterAmountPaid.setRowCellFactory(purchaseMaster -> new MFXTableRowCell<>(PurchaseMaster::getPaid));
        purchaseMasterAmountDue.setRowCellFactory(purchaseMaster -> new MFXTableRowCell<>(PurchaseMaster::getDue));
        purchaseMasterPaymentStatus.setRowCellFactory(purchaseMaster -> new MFXTableRowCell<>(PurchaseMaster::getPaymentStatus));

        purchaseMasterDate.prefWidthProperty().bind(purchaseTable.widthProperty().multiply(.1));
        purchaseMasterReference.prefWidthProperty().bind(purchaseTable.widthProperty().multiply(.1));
        purchaseMasterSupplier.prefWidthProperty().bind(purchaseTable.widthProperty().multiply(.15));
        purchaseMasterBranch.prefWidthProperty().bind(purchaseTable.widthProperty().multiply(.15));
        purchaseMasterStatus.prefWidthProperty().bind(purchaseTable.widthProperty().multiply(.1));
        purchaseMasterGrandTotal.prefWidthProperty().bind(purchaseTable.widthProperty().multiply(.1));
        purchaseMasterAmountPaid.prefWidthProperty().bind(purchaseTable.widthProperty().multiply(.1));
        purchaseMasterAmountDue.prefWidthProperty().bind(purchaseTable.widthProperty().multiply(.1));
        purchaseMasterPaymentStatus.prefWidthProperty().bind(purchaseTable.widthProperty().multiply(.1));

        purchaseTable.getTableColumns().addAll(purchaseMasterDate, purchaseMasterReference, purchaseMasterSupplier, purchaseMasterBranch, purchaseMasterStatus, purchaseMasterGrandTotal, purchaseMasterAmountPaid, purchaseMasterAmountDue, purchaseMasterPaymentStatus);
        purchaseTable.getFilters().addAll(
                new StringFilter<>("Reference", PurchaseMaster::getRef),
                new StringFilter<>("Supplier", PurchaseMaster::getSupplierName),
                new StringFilter<>("Branch", PurchaseMaster::getBranchName),
                new StringFilter<>("Status", PurchaseMaster::getStatus),
                new DoubleFilter<>("Total", PurchaseMaster::getTotal),
                new DoubleFilter<>("Paid", PurchaseMaster::getPaid),
                new DoubleFilter<>("Due", PurchaseMaster::getDue),
                new StringFilter<>("Pay Status", PurchaseMaster::getPaymentStatus)
        );
        getTable();
        purchaseTable.setItems(getPurchaseMasters());
    }

    private void getTable() {
        purchaseTable.setPrefSize(1200, 1000);
        purchaseTable.features().enableBounceEffect();
        purchaseTable.autosizeColumnsOnInitialization();
        purchaseTable.features().enableSmoothScrolling(0.5);
    }

    public void purchaseCreateBtnClicked() {
        FXMLLoader loader = fxmlLoader("forms/PurchaseForm.fxml");
        loader.setControllerFactory(c -> new PurchaseFormController(stage));
        try {
            BorderPane productFormPane = loader.load();
            ((StackPane) purchaseContentPane.getParent()).getChildren().add(productFormPane);
            ((StackPane) purchaseContentPane.getParent()).getChildren().get(0).setVisible(false);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
