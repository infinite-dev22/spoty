package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
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
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.models.QuotationProduct;
import org.infinite.spoty.database.models.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.dataShare.DataShare.getQuotationProducts;

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
    public MFXTableView<QuotationProduct> quotationProductsTable;
    @FXML
    public MFXTextField quotationNote;
    @FXML
    public AnchorPane quotationFormContentPane;
    @FXML
    public MFXFilterComboBox<?> quotationStatus;
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
        quotationDate.textProperty().addListener((observable, oldValue, newValue) -> quotationDate.setTrailingIcon(null));
        quotationCustomerId.textProperty().addListener((observable, oldValue, newValue) -> quotationCustomerId.setTrailingIcon(null));
        quotationBranchId.textProperty().addListener((observable, oldValue, newValue) -> quotationBranchId.setTrailingIcon(null));
        quotationStatus.textProperty().addListener((observable, oldValue, newValue) -> quotationStatus.setTrailingIcon(null));

        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<QuotationProduct> productName = new MFXTableColumn<>("Product", true, Comparator.comparing(QuotationProduct::product));
        MFXTableColumn<QuotationProduct> productQuantity = new MFXTableColumn<>("Quantity", true, Comparator.comparing(QuotationProduct::quantity));
        MFXTableColumn<QuotationProduct> productDiscount = new MFXTableColumn<>("Discount", true, Comparator.comparing(QuotationProduct::discount));
        MFXTableColumn<QuotationProduct> productTax = new MFXTableColumn<>("Tax", true, Comparator.comparing(QuotationProduct::tax));

        productName.setRowCellFactory(product -> new MFXTableRowCell<>(QuotationProduct::product));
        productQuantity.setRowCellFactory(product -> new MFXTableRowCell<>(QuotationProduct::quantity));
        productDiscount.setRowCellFactory(product -> new MFXTableRowCell<>(QuotationProduct::discount));
        productTax.setRowCellFactory(product -> new MFXTableRowCell<>(QuotationProduct::tax));

        quotationProductsTable.getTableColumns().addAll(productName, productQuantity, productDiscount, productTax);
        quotationProductsTable.getFilters().addAll(
                new StringFilter<>("Product", QuotationProduct::product),
                new DoubleFilter<>("Quantity", QuotationProduct::quantity),
                new DoubleFilter<>("Discount", QuotationProduct::discount),
                new DoubleFilter<>("Tax", QuotationProduct::tax)
        );
        getQuotationProductTable();
        quotationProductsTable.setItems(getQuotationProducts());
    }

    private void getQuotationProductTable() {
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
            quotationDate.getText();
            quotationCustomerId.getText();
            quotationBranchId.getText();
            quotationStatus.getText();
            quotationNote.getText();

            quotationDate.setText("");
            quotationCustomerId.setText("");
            quotationBranchId.setText("");
            quotationStatus.setText("");
            quotationNote.setText("");
            quotationProductsTable.getTableColumns().clear();
            getQuotationProducts().clear();
        }
    }

    public void cancelBtnClicked() {
        ((StackPane) quotationFormContentPane.getParent()).getChildren().get(0).setVisible(true);
        ((StackPane) quotationFormContentPane.getParent()).getChildren().remove(1);
    }

    public void addBtnClicked() {
        dialog.showAndWait();
    }
}
