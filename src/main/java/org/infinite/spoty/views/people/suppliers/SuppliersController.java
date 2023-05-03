package org.infinite.spoty.views.people.suppliers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.models.Supplier;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.data.SampleData.supplierSampleData;

public class SuppliersController implements Initializable {
    @FXML
    public MFXTextField supplierSearchBar;
    @FXML
    public HBox supplierActionsPane;
    @FXML
    public MFXButton supplierImportBtn;
    @FXML
    public MFXTableView<Supplier> supplierTable;
    @FXML
    public BorderPane suppliersContentPane;
    private Dialog<ButtonType> dialog;

    public SuppliersController(Stage stage) {
        Platform.runLater(() -> {
            try {
                supplierFormDialogPane(stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Supplier> supplierCode = new MFXTableColumn<>("Code", true, Comparator.comparing(Supplier::getSupplierCode));
        MFXTableColumn<Supplier> supplierName = new MFXTableColumn<>("Name", true, Comparator.comparing(Supplier::getSupplierName));
        MFXTableColumn<Supplier> supplierPhone = new MFXTableColumn<>("Phone", true, Comparator.comparing(Supplier::getSupplierPhoneNumber));
        MFXTableColumn<Supplier> supplierEmail = new MFXTableColumn<>("Email", true, Comparator.comparing(Supplier::getSupplierEmail));
        MFXTableColumn<Supplier> supplierTax = new MFXTableColumn<>("Tax No.", true, Comparator.comparing(Supplier::getSupplierTaxNumber));
        MFXTableColumn<Supplier> supplierPurchasesDue = new MFXTableColumn<>("Purchases Due", true, Comparator.comparing(Supplier::getSupplierTotalPurchaseDue));
        MFXTableColumn<Supplier> supplierPurchaseReturnDue = new MFXTableColumn<>("Returns Due", true, Comparator.comparing(Supplier::getSupplierTotalPurchaseReturnDue));

        supplierCode.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getSupplierCode));
        supplierName.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getSupplierName));
        supplierPhone.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getSupplierPhoneNumber));
        supplierEmail.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getSupplierEmail));
        supplierTax.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getSupplierTaxNumber));
        supplierPurchasesDue.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getSupplierTotalPurchaseDue));
        supplierPurchaseReturnDue.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getSupplierTotalPurchaseReturnDue));

        supplierTable.getTableColumns().addAll(supplierCode, supplierName, supplierPhone, supplierEmail, supplierTax, supplierPurchasesDue, supplierPurchaseReturnDue);
        supplierTable.getFilters().addAll(
                new IntegerFilter<>("Code", Supplier::getSupplierCode),
                new StringFilter<>("Name", Supplier::getSupplierName),
                new StringFilter<>("Phone", Supplier::getSupplierPhoneNumber),
                new StringFilter<>("Email", Supplier::getSupplierEmail),
                new StringFilter<>("Tax No.", Supplier::getSupplierTaxNumber),
                new DoubleFilter<>("Purchases Due", Supplier::getSupplierTotalPurchaseDue),
                new DoubleFilter<>("Returns Due", Supplier::getSupplierTotalPurchaseReturnDue)
        );
        getSupplierTable();
        supplierTable.setItems(supplierSampleData());
    }

    private void getSupplierTable() {
        supplierTable.setPrefSize(1000, 1000);
        supplierTable.autosizeColumnsOnInitialization();
        supplierTable.features().enableBounceEffect();
        supplierTable.features().enableSmoothScrolling(0.5);
    }

    private void supplierFormDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/SupplierForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }

    public void supplierCreateBtnClicked() {
        dialog.showAndWait();
    }
}
