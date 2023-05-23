package org.infinite.spoty.views.people.suppliers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
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
import org.infinite.spoty.database.models.Supplier;
import org.infinite.spoty.viewModels.SupplierVewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

public class SuppliersController implements Initializable {
    @FXML
    public MFXTextField supplierSearchBar;
    @FXML
    public HBox supplierActionsPane;
    @FXML
    public MFXButton supplierImportBtn;
    @FXML
    public MFXTableView<Supplier> suppliersTable;
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
        MFXTableColumn<Supplier> supplierCode = new MFXTableColumn<>("Code", true, Comparator.comparing(Supplier::getCode));
        MFXTableColumn<Supplier> supplierName = new MFXTableColumn<>("Name", true, Comparator.comparing(Supplier::getName));
        MFXTableColumn<Supplier> supplierPhone = new MFXTableColumn<>("Phone", true, Comparator.comparing(Supplier::getPhone));
        MFXTableColumn<Supplier> supplierEmail = new MFXTableColumn<>("Email", true, Comparator.comparing(Supplier::getEmail));
        MFXTableColumn<Supplier> supplierTax = new MFXTableColumn<>("Tax No.", true, Comparator.comparing(Supplier::getTaxNumber));
//        MFXTableColumn<Supplier> supplierPurchasesDue = new MFXTableColumn<>("Purchases Due", true, Comparator.comparing(Supplier::getTotalPurchaseDue));
//        MFXTableColumn<Supplier> supplierPurchaseReturnDue = new MFXTableColumn<>("Returns Due", true, Comparator.comparing(Supplier::getTotalPurchaseReturnDue));

        supplierCode.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getCode));
        supplierName.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getName));
        supplierPhone.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getPhone));
        supplierEmail.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getEmail));
        supplierTax.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getTaxNumber));
//        supplierPurchasesDue.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getTotalPurchaseDue));
//        supplierPurchaseReturnDue.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getTotalPurchaseReturnDue));

        supplierCode.prefWidthProperty().bind(suppliersTable.widthProperty().multiply(.1));
        supplierName.prefWidthProperty().bind(suppliersTable.widthProperty().multiply(.3));
        supplierPhone.prefWidthProperty().bind(suppliersTable.widthProperty().multiply(.2));
        supplierEmail.prefWidthProperty().bind(suppliersTable.widthProperty().multiply(.2));
        supplierTax.prefWidthProperty().bind(suppliersTable.widthProperty().multiply(.2));

        suppliersTable.getTableColumns().addAll(supplierCode, supplierName, supplierPhone, supplierEmail, supplierTax);
        suppliersTable.getFilters().addAll(
                new StringFilter<>("Code", Supplier::getCode),
                new StringFilter<>("Name", Supplier::getName),
                new StringFilter<>("Phone", Supplier::getPhone),
                new StringFilter<>("Email", Supplier::getEmail),
                new StringFilter<>("Tax No.", Supplier::getTaxNumber)
        );
        getTable();
        suppliersTable.setItems(SupplierVewModel.getSuppliers());
    }

    private void getTable() {
        suppliersTable.setPrefSize(1000, 1000);
        suppliersTable.features().enableBounceEffect();
        suppliersTable.features().enableSmoothScrolling(0.5);
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
