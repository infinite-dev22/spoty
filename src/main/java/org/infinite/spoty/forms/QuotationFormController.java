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
import org.infinite.spoty.database.models.QuotationDetail;
import org.infinite.spoty.values.strings.Values;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.CustomerViewModel;
import org.infinite.spoty.viewModels.QuotationDetailViewModel;
import org.infinite.spoty.viewModels.QuotationMasterViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

public class QuotationFormController implements Initializable {
    @FXML
    public Label quotationFormTitle;
    @FXML
    public MFXDatePicker quotationDate;
    @FXML
    public MFXFilterComboBox<Customer> quotationCustomerId;
    @FXML
    public MFXFilterComboBox<Branch> quotationBranchId;
    @FXML
    public MFXTableView<QuotationDetail> quotationProductsTable;
    @FXML
    public MFXTextField quotationNote;
    @FXML
    public AnchorPane quotationFormContentPane;
    @FXML
    public MFXFilterComboBox<String> quotationStatus;
    private Dialog<ButtonType> dialog;

    public QuotationFormController(Stage stage) {
        Platform.runLater(() -> {
            try {
                quotationProductDialogPane(stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form input listeners.
        quotationDate.textProperty().addListener((observable, oldValue, newValue) -> quotationDate.setTrailingIcon(null));
        quotationCustomerId.textProperty().addListener((observable, oldValue, newValue) -> quotationCustomerId.setTrailingIcon(null));
        quotationBranchId.textProperty().addListener((observable, oldValue, newValue) -> quotationBranchId.setTrailingIcon(null));
        quotationStatus.textProperty().addListener((observable, oldValue, newValue) -> quotationStatus.setTrailingIcon(null));
        // Combo box properties.
        quotationCustomerId.setItems(CustomerViewModel.customersList);
        quotationCustomerId.setConverter(new StringConverter<>() {
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
        quotationBranchId.setItems(BranchViewModel.branchesList);
        quotationBranchId.setConverter(new StringConverter<>() {
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
        quotationStatus.setItems(FXCollections.observableArrayList(Values.QUOTATIONTYPE));
        // Form input binding.
        quotationDate.textProperty().bindBidirectional(QuotationMasterViewModel.dateProperty());
        quotationCustomerId.valueProperty().bindBidirectional(QuotationMasterViewModel.customerProperty());
        quotationBranchId.valueProperty().bindBidirectional(QuotationMasterViewModel.branchProperty());
        quotationStatus.textProperty().bindBidirectional(QuotationMasterViewModel.statusProperty());

        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<QuotationDetail> productName = new MFXTableColumn<>("Product", false, Comparator.comparing(QuotationDetail::getProductName));
        MFXTableColumn<QuotationDetail> productQuantity = new MFXTableColumn<>("Quantity", false, Comparator.comparing(QuotationDetail::getQuantity));
        MFXTableColumn<QuotationDetail> productDiscount = new MFXTableColumn<>("Discount", false, Comparator.comparing(QuotationDetail::getDiscount));
        MFXTableColumn<QuotationDetail> productTax = new MFXTableColumn<>("Tax", false, Comparator.comparing(QuotationDetail::getNetTax));

        productName.setRowCellFactory(product -> new MFXTableRowCell<>(QuotationDetail::getProductName));
        productQuantity.setRowCellFactory(product -> new MFXTableRowCell<>(QuotationDetail::getQuantity));
        productDiscount.setRowCellFactory(product -> new MFXTableRowCell<>(QuotationDetail::getDiscount));
        productTax.setRowCellFactory(product -> new MFXTableRowCell<>(QuotationDetail::getNetTax));

        productName.prefWidthProperty().bind(quotationProductsTable.widthProperty().multiply(.25));
        productQuantity.prefWidthProperty().bind(quotationProductsTable.widthProperty().multiply(.25));
        productDiscount.prefWidthProperty().bind(quotationProductsTable.widthProperty().multiply(.25));
        productTax.prefWidthProperty().bind(quotationProductsTable.widthProperty().multiply(.25));

        quotationProductsTable.getTableColumns().addAll(productName, productQuantity, productDiscount, productTax);
        quotationProductsTable.getFilters().addAll(
                new StringFilter<>("Product", QuotationDetail::getProductName),
                new IntegerFilter<>("Quantity", QuotationDetail::getQuantity),
                new DoubleFilter<>("Discount", QuotationDetail::getDiscount),
                new DoubleFilter<>("Tax", QuotationDetail::getNetTax)
        );
        getQuotationDetailTable();
        quotationProductsTable.setItems(QuotationDetailViewModel.quotationDetailsTempList);
    }

    private void getQuotationDetailTable() {
        quotationProductsTable.setPrefSize(1000, 1000);
        quotationProductsTable.features().enableBounceEffect();
        quotationProductsTable.autosizeColumnsOnInitialization();
        quotationProductsTable.features().enableSmoothScrolling(0.5);
    }

    private void quotationProductDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/QuotationProductsForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }

    public void saveBtnClicked() {
        MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

        if (quotationDate.getText().length() == 0) {
            quotationDate.setTrailingIcon(icon);
        }
        if (quotationCustomerId.getText().length() == 0) {
            quotationCustomerId.setTrailingIcon(icon);
        }
        if (quotationBranchId.getText().length() == 0) {
            quotationBranchId.setTrailingIcon(icon);
        }
        if (quotationStatus.getText().length() == 0) {
            quotationStatus.setTrailingIcon(icon);
        }
        if (quotationProductsTable.getTableColumns().isEmpty()) {
            // Notify table can't be empty
            System.out.println("Table can't be empty");
        }
        if (quotationDate.getText().length() > 0
                && quotationCustomerId.getText().length() > 0
                && quotationBranchId.getText().length() > 0
                && quotationStatus.getText().length() > 0
                && !quotationProductsTable.getTableColumns().isEmpty()) {
            QuotationMasterViewModel.saveQuotationMaster();
            QuotationMasterViewModel.resetProperties();
            quotationProductsTable.getTableColumns().clear();
        }
    }

    public void cancelBtnClicked() {
        QuotationMasterViewModel.resetProperties();
        quotationProductsTable.getTableColumns().clear();
        ((StackPane) quotationFormContentPane.getParent()).getChildren().get(0).setVisible(true);
        ((StackPane) quotationFormContentPane.getParent()).getChildren().remove(1);
    }

    public void addBtnClicked() {
        dialog.showAndWait();
    }
}
