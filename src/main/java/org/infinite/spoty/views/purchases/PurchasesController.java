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
import org.infinite.spoty.models.Purchase;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.data.SampleData.purchaseSampleData;

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
    private MFXTableView<Purchase> purchaseTable;

    public PurchasesController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Purchase> purchaseDate = new MFXTableColumn<>("Date", true, Comparator.comparing(Purchase::getPurchaseDate));
        MFXTableColumn<Purchase> purchaseReference = new MFXTableColumn<>("Reference", true, Comparator.comparing(Purchase::getPurchaseReference));
        MFXTableColumn<Purchase> purchaseSupplier = new MFXTableColumn<>("Supplier", true, Comparator.comparing(Purchase::getPurchaseSupplier));
        MFXTableColumn<Purchase> purchaseBranch = new MFXTableColumn<>("Branch", true, Comparator.comparing(Purchase::getPurchaseBranch));
        MFXTableColumn<Purchase> purchaseStatus = new MFXTableColumn<>("PurchaseMaster Status", true, Comparator.comparing(Purchase::getPurchaseStatus));
        MFXTableColumn<Purchase> purchaseGrandTotal = new MFXTableColumn<>("Grand Total", true, Comparator.comparing(Purchase::getPurchaseGrandTotal));
        MFXTableColumn<Purchase> purchaseAmountPaid = new MFXTableColumn<>("Amount Paid", true, Comparator.comparing(Purchase::getPurchaseAmountPaid));
        MFXTableColumn<Purchase> purchaseAmountDue = new MFXTableColumn<>("Amount Due", true, Comparator.comparing(Purchase::getPurchaseAmountDue));
        MFXTableColumn<Purchase> purchasePaymentStatus = new MFXTableColumn<>("Payment Status", true, Comparator.comparing(Purchase::getPurchasePaymentStatus));

        purchaseDate.setRowCellFactory(purchase -> new MFXTableRowCell<>(Purchase::getPurchaseDate));
        purchaseReference.setRowCellFactory(purchase -> new MFXTableRowCell<>(Purchase::getPurchaseReference));
        purchaseSupplier.setRowCellFactory(purchase -> new MFXTableRowCell<>(Purchase::getPurchaseSupplier));
        purchaseBranch.setRowCellFactory(purchase -> new MFXTableRowCell<>(Purchase::getPurchaseBranch));
        purchaseStatus.setRowCellFactory(purchase -> new MFXTableRowCell<>(Purchase::getPurchaseStatus));
        purchaseGrandTotal.setRowCellFactory(purchase -> new MFXTableRowCell<>(Purchase::getPurchaseGrandTotal));
        purchaseAmountPaid.setRowCellFactory(purchase -> new MFXTableRowCell<>(Purchase::getPurchaseAmountPaid));
        purchaseAmountDue.setRowCellFactory(purchase -> new MFXTableRowCell<>(Purchase::getPurchaseAmountDue));
        purchasePaymentStatus.setRowCellFactory(purchase -> new MFXTableRowCell<>(Purchase::getPurchasePaymentStatus));

        purchaseTable.getTableColumns().addAll(purchaseDate, purchaseReference, purchaseSupplier, purchaseBranch, purchaseStatus, purchaseGrandTotal, purchaseAmountPaid, purchaseAmountDue, purchasePaymentStatus);
        purchaseTable.getFilters().addAll(
                new StringFilter<>("Reference", Purchase::getPurchaseReference),
                new StringFilter<>("Supplier", Purchase::getPurchaseSupplier),
                new StringFilter<>("Branch", Purchase::getPurchaseBranch),
                new StringFilter<>("PurchaseMaster Status", Purchase::getPurchaseStatus),
                new DoubleFilter<>("Grand Total", Purchase::getPurchaseGrandTotal),
                new DoubleFilter<>("Amount Paid", Purchase::getPurchaseAmountPaid),
                new DoubleFilter<>("Amount Due", Purchase::getPurchaseAmountDue),
                new StringFilter<>("Payment Status", Purchase::getPurchasePaymentStatus)
        );
        getPurchaseTable();
        purchaseTable.setItems(purchaseSampleData());
    }

    private void getPurchaseTable() {
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
