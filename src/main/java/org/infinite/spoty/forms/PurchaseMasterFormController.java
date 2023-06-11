package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.database.dao.PurchaseDetailDao;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.PurchaseDetail;
import org.infinite.spoty.database.models.Supplier;
import org.infinite.spoty.values.strings.Values;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.PurchaseDetailViewModel;
import org.infinite.spoty.viewModels.PurchaseMasterViewModel;
import org.infinite.spoty.viewModels.SupplierViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.dataShare.DataShare.getPurchaseProducts;

@SuppressWarnings("unchecked")
public class PurchaseMasterFormController implements Initializable {
    public MFXTextField purchaseDetailID = new MFXTextField();
    public MFXTextField purchaseMasterID = new MFXTextField();
    @FXML
    public Label purchaseFormTitle;
    @FXML
    public MFXDatePicker purchaseDate;
    @FXML
    public MFXFilterComboBox<Supplier> purchaseSupplierId;
    @FXML
    public MFXFilterComboBox<Branch> purchaseBranchId;
    @FXML
    public MFXTableView<PurchaseDetail> purchaseDetailTable;
    @FXML
    public MFXTextField purchaseNote;
    @FXML
    public AnchorPane purchaseFormContentPane;
    @FXML
    public MFXFilterComboBox<String> purchaseStatus;
    private Dialog<ButtonType> dialog;

    public PurchaseMasterFormController(Stage stage) {
        Platform.runLater(() -> {
            try {
                createPurchaseProductDialog(stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Add event listeners on form inputs
//        purchaseDate.textProperty().addListener((observable, oldValue, newValue) -> purchaseDate.setTrailingIcon(null));
//        purchaseSupplierId.textProperty().addListener((observable, oldValue, newValue) -> purchaseSupplierId.setTrailingIcon(null));
//        purchaseBranchId.textProperty().addListener((observable, oldValue, newValue) -> purchaseBranchId.setTrailingIcon(null));
//        purchaseStatus.textProperty().addListener((observable, oldValue, newValue) -> purchaseStatus.setTrailingIcon(null));
        // Set items to combo boxes and display custom text.
        purchaseSupplierId.setItems(SupplierViewModel.suppliersList);
        purchaseSupplierId.setConverter(new StringConverter<>() {
            @Override
            public String toString(Supplier object) {
                if (object != null)
                    return object.getName();
                else
                    return null;
            }

            @Override
            public Supplier fromString(String string) {
                return null;
            }
        });
        purchaseBranchId.setItems(BranchViewModel.branchesList);
        purchaseBranchId.setConverter(new StringConverter<>() {
            @Override
            public String toString(Branch object) {
                if (object != null)
                    return object.getName();
                else
                    return null;
            }

            @Override
            public Branch fromString(String string) {
                return null;
            }
        });
        purchaseStatus.setItems(FXCollections.observableArrayList(Values.PURCHASESTATUSES));
        // Bi~Directional Binding.
        purchaseMasterID.textProperty().bindBidirectional(PurchaseMasterViewModel.idProperty(), new NumberStringConverter());
        purchaseDate.textProperty().bindBidirectional(PurchaseMasterViewModel.dateProperty());
        purchaseSupplierId.valueProperty().bindBidirectional(PurchaseMasterViewModel.supplierProperty());
        purchaseBranchId.valueProperty().bindBidirectional(PurchaseMasterViewModel.branchProperty());
        purchaseStatus.textProperty().bindBidirectional(PurchaseMasterViewModel.statusProperty());
        purchaseNote.textProperty().bindBidirectional(PurchaseMasterViewModel.noteProperty());

        setupTable();
    }

    private void createPurchaseProductDialog(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/PurchaseProductsForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }

    public void saveBtnClicked() {
        MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

        if (purchaseDate.getText().length() == 0) {
            purchaseDate.setTrailingIcon(icon);
        }
        if (purchaseSupplierId.getText().length() == 0) {
            purchaseSupplierId.setTrailingIcon(icon);
        }
        if (purchaseBranchId.getText().length() == 0) {
            purchaseBranchId.setTrailingIcon(icon);
        }
        if (purchaseStatus.getText().length() == 0) {
            purchaseStatus.setTrailingIcon(icon);
        }
        if (purchaseDetailTable.getTableColumns().isEmpty()) {
            // Notify table can't be empty
            System.out.println("Table can't be empty");
        }
        if (purchaseDate.getText().length() > 0
                && purchaseSupplierId.getText().length() > 0
                && purchaseBranchId.getText().length() > 0
                && purchaseStatus.getText().length() > 0
                && !purchaseDetailTable.getTableColumns().isEmpty()) {
            if (Integer.parseInt(purchaseMasterID.getText()) > 0) {
                PurchaseMasterViewModel.updateItem(Integer.parseInt(purchaseMasterID.getText()));
                cancelBtnClicked();
            } else
                PurchaseMasterViewModel.savePurchaseMaster();
            PurchaseMasterViewModel.resetProperties();
            getPurchaseProducts().clear();
            purchaseDetailTable.getTableColumns().clear();
        }
    }

    private void setupTable() {
        // Set table column titles.
        MFXTableColumn<PurchaseDetail> product = new MFXTableColumn<>("Product", false, Comparator.comparing(PurchaseDetail::getProductName));
        MFXTableColumn<PurchaseDetail> quantity = new MFXTableColumn<>("Quantity", false, Comparator.comparing(PurchaseDetail::getQuantity));
        MFXTableColumn<PurchaseDetail> tax = new MFXTableColumn<>("Tax", false, Comparator.comparing(PurchaseDetail::getNetTax));
        MFXTableColumn<PurchaseDetail> discount = new MFXTableColumn<>("Discount", false, Comparator.comparing(PurchaseDetail::getDiscount));
        // Set table column data.
        product.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(PurchaseDetail::getProductName));
        quantity.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(PurchaseDetail::getQuantity));
        tax.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(PurchaseDetail::getNetTax));
        discount.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(PurchaseDetail::getDiscount));
        //Set table column width.
        product.prefWidthProperty().bind(purchaseDetailTable.widthProperty().multiply(.25));
        quantity.prefWidthProperty().bind(purchaseDetailTable.widthProperty().multiply(.25));
        tax.prefWidthProperty().bind(purchaseDetailTable.widthProperty().multiply(.25));
        discount.prefWidthProperty().bind(purchaseDetailTable.widthProperty().multiply(.25));
        // Set table filter.
        purchaseDetailTable.getTableColumns().addAll(product, quantity, tax, discount);
        purchaseDetailTable.getFilters().addAll(
                new StringFilter<>("Product", PurchaseDetail::getProductName),
                new IntegerFilter<>("Quantity", PurchaseDetail::getQuantity),
                new DoubleFilter<>("Tax", PurchaseDetail::getNetTax),
                new DoubleFilter<>("Discount", PurchaseDetail::getDiscount)
        );
        styleTable();
        // Populate table.
        purchaseDetailTable.setItems(PurchaseDetailViewModel.purchaseDetailTempList);
    }

    private void styleTable() {
        purchaseDetailTable.setPrefSize(1000, 1000);
        purchaseDetailTable.features().enableBounceEffect();
        purchaseDetailTable.features().enableSmoothScrolling(0.5);

        purchaseDetailTable.setTableRowFactory(t -> {
            MFXTableRow<PurchaseDetail> row = new MFXTableRow<>(purchaseDetailTable, t);
            EventHandler<ContextMenuEvent> eventHandler = event -> {
                showContextMenu((MFXTableRow<PurchaseDetail>) event.getSource())
                        .show(purchaseDetailTable.getScene().getWindow(), event.getScreenX(), event.getScreenY());
                event.consume();
            };
            row.setOnContextMenuRequested(eventHandler);
            return row;
        });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<PurchaseDetail> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(purchaseDetailTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(e -> {
            PurchaseDetailViewModel.getItem(obj.getData(), PurchaseDetailViewModel.purchaseDetailTempList.indexOf(obj.getData()));
            try {
                if (Integer.parseInt(purchaseDetailID.getText()) > 0)
                    PurchaseDetailDao.deletePurchaseDetail(Integer.parseInt(purchaseDetailID.getText()));
            } catch (NumberFormatException ignored) {
                PurchaseDetailViewModel.removePurchaseDetail(PurchaseDetailViewModel.purchaseDetailTempList.indexOf(obj.getData()));
            }
            PurchaseDetailViewModel.getPurchaseDetails();
            e.consume();
        });
        // Edit
        edit.setOnAction(e -> {
            PurchaseDetailViewModel.getItem(obj.getData(), PurchaseDetailViewModel.purchaseDetailTempList.indexOf(obj.getData()));
            dialog.showAndWait();
            e.consume();
        });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    public void cancelBtnClicked() {
        ((StackPane) purchaseFormContentPane.getParent().getParent()).getChildren().get(0).setVisible(true);
        ((StackPane) purchaseFormContentPane.getParent().getParent()).getChildren().remove(1);
        purchaseDetailTable.getTableColumns().clear();
        PurchaseMasterViewModel.resetProperties();
    }

    public void addBtnClicked() {
        dialog.showAndWait();
    }
}
