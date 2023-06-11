package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.Customer;
import org.infinite.spoty.database.models.SaleDetail;
import org.infinite.spoty.values.strings.Values;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.CustomerViewModel;
import org.infinite.spoty.viewModels.SaleDetailViewModel;
import org.infinite.spoty.viewModels.SaleMasterViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.dataShare.DataShare.getSaleProducts;

public class SaleMasterFormController implements Initializable {
    @FXML
    public Label saleFormTitle;
    @FXML
    public MFXDatePicker saleDate;
    @FXML
    public MFXFilterComboBox<Customer> saleCustomerId;
    @FXML
    public MFXFilterComboBox<Branch> saleBranchId;
    @FXML
    public MFXTableView<SaleDetail> saleProductsTable;
    @FXML
    public MFXTextField saleNote;
    @FXML
    public AnchorPane saleFormContentPane;
    @FXML
    public MFXFilterComboBox<String> saleStatus;
    @FXML
    public MFXFilterComboBox<String> salePaymentStatus;
    private Dialog<ButtonType> dialog;

    public SaleMasterFormController(Stage stage) {
        Platform.runLater(() -> {
            try {
                saleProductDialogPane(stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Add form input event listeners.
        saleDate.textProperty().addListener((observable, oldValue, newValue) -> saleDate.setTrailingIcon(null));
        saleCustomerId.textProperty().addListener((observable, oldValue, newValue) -> saleCustomerId.setTrailingIcon(null));
        saleBranchId.textProperty().addListener((observable, oldValue, newValue) -> saleBranchId.setTrailingIcon(null));
        saleStatus.textProperty().addListener((observable, oldValue, newValue) -> saleStatus.setTrailingIcon(null));
        // Set items to combo boxes and display custom text.
        saleCustomerId.setItems(CustomerViewModel.customersList);
        saleCustomerId.setConverter(new StringConverter<>() {
            @Override
            public String toString(Customer object) {
                if (object != null)
                    return object.getName();
                else
                    return null;
            }

            @Override
            public Customer fromString(String string) {
                return null;
            }
        });
        saleBranchId.setItems(BranchViewModel.branchesList);
        saleBranchId.setConverter(new StringConverter<>() {
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
        saleStatus.setItems(FXCollections.observableArrayList(Values.SALESTATUSES));
        salePaymentStatus.setItems(FXCollections.observableArrayList(Values.PAYMENTSTATUSES));
        // Bi~Directional Binding.
        saleDate.textProperty().bindBidirectional(SaleMasterViewModel.dateProperty());
        saleCustomerId.valueProperty().bindBidirectional(SaleMasterViewModel.customerProperty());
        saleBranchId.valueProperty().bindBidirectional(SaleMasterViewModel.branchProperty());
        saleStatus.textProperty().bindBidirectional(SaleMasterViewModel.statusProperty());
        setupTable();
    }

    private void saleProductDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/SaleProductsForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }

    public void saveBtnClicked() {
        MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

        if (saleDate.getText().length() == 0) {
            saleDate.setTrailingIcon(icon);
        }
        if (saleCustomerId.getText().length() == 0) {
            saleCustomerId.setTrailingIcon(icon);
        }
        if (saleBranchId.getText().length() == 0) {
            saleBranchId.setTrailingIcon(icon);
        }
        if (saleStatus.getText().length() == 0) {
            saleStatus.setTrailingIcon(icon);
        }
        if (saleProductsTable.getTableColumns().isEmpty()) {
            // Notify table can't be empty
            System.out.println("Table can't be empty");
        }
        if (saleDate.getText().length() > 0
                && saleCustomerId.getText().length() > 0
                && saleBranchId.getText().length() > 0
                && saleStatus.getText().length() > 0
                && !saleProductsTable.getTableColumns().isEmpty()) {
            SaleMasterViewModel.saveSaleMaster();
            SaleMasterViewModel.resetProperties();
            saleProductsTable.getTableColumns().clear();
            getSaleProducts().clear();
        }
    }

    public void cancelBtnClicked() {
        ((StackPane) saleFormContentPane.getParent().getParent()).getChildren().get(0).setVisible(true);
        ((StackPane) saleFormContentPane.getParent().getParent()).getChildren().remove(1);
        SaleMasterViewModel.resetProperties();
        saleProductsTable.getTableColumns().clear();
    }

    public void addBtnClicked() {
        dialog.showAndWait();
    }

    private void setupTable() {
        // Set table column titles.
        MFXTableColumn<SaleDetail> product = new MFXTableColumn<>("Product", false, Comparator.comparing(SaleDetail::getProductName));
        MFXTableColumn<SaleDetail> quantity = new MFXTableColumn<>("Quantity", false, Comparator.comparing(SaleDetail::getQuantity));
        MFXTableColumn<SaleDetail> tax = new MFXTableColumn<>("Tax", false, Comparator.comparing(SaleDetail::getNetTax));
        MFXTableColumn<SaleDetail> discount = new MFXTableColumn<>("Discount", false, Comparator.comparing(SaleDetail::getDiscount));
        // Set table column data.
        product.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(SaleDetail::getProductName));
        quantity.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(SaleDetail::getQuantity));
        tax.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(SaleDetail::getNetTax));
        discount.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(SaleDetail::getDiscount));
        //Set table column width.
        product.prefWidthProperty().bind(saleProductsTable.widthProperty().multiply(.25));
        quantity.prefWidthProperty().bind(saleProductsTable.widthProperty().multiply(.25));
        tax.prefWidthProperty().bind(saleProductsTable.widthProperty().multiply(.25));
        discount.prefWidthProperty().bind(saleProductsTable.widthProperty().multiply(.25));
        // Set table filter.
        saleProductsTable.getTableColumns().addAll(product, quantity, tax, discount);
        saleProductsTable.getFilters().addAll(
                new StringFilter<>("Product", SaleDetail::getProductName),
                new IntegerFilter<>("Quantity", SaleDetail::getQuantity),
                new DoubleFilter<>("Tax", SaleDetail::getNetTax),
                new DoubleFilter<>("Discount", SaleDetail::getDiscount)
        );
        styleTable();
        // Populate table.
        saleProductsTable.setItems(SaleDetailViewModel.saleDetailTempList);
    }

    private void styleTable() {
        saleProductsTable.setPrefSize(1000, 1000);
        saleProductsTable.features().enableBounceEffect();
        saleProductsTable.features().enableSmoothScrolling(0.5);
    }
}
