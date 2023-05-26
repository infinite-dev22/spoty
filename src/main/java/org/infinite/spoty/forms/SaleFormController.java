package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.enums.ButtonType;
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
import org.infinite.spoty.database.models.Customer;
import org.infinite.spoty.models.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.dataShare.DataShare.getSaleProducts;

public class SaleFormController implements Initializable {
    @FXML
    public Label saleFormTitle;
    @FXML
    public MFXDatePicker saleDate;
    @FXML
    public MFXFilterComboBox<Customer> saleSupplierId;
    @FXML
    public MFXFilterComboBox<Branch> saleBranchId;
    @FXML
    public MFXTableView<Product> saleProductsTable;
    @FXML
    public MFXTextField saleNote;
    @FXML
    public AnchorPane saleFormContentPane;
    @FXML
    public MFXFilterComboBox<?> saleStatus;
    @FXML
    public MFXFilterComboBox<?> salePaymentStatus;
    private Dialog<ButtonType> dialog;

    public SaleFormController(Stage stage) {
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
        saleDate.textProperty().addListener((observable, oldValue, newValue) -> saleDate.setTrailingIcon(null));
        saleSupplierId.textProperty().addListener((observable, oldValue, newValue) -> saleSupplierId.setTrailingIcon(null));
        saleBranchId.textProperty().addListener((observable, oldValue, newValue) -> saleBranchId.setTrailingIcon(null));
        saleStatus.textProperty().addListener((observable, oldValue, newValue) -> saleStatus.setTrailingIcon(null));
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
        if (saleSupplierId.getText().length() == 0) {
            saleSupplierId.setTrailingIcon(icon);
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
                && saleSupplierId.getText().length() > 0
                && saleBranchId.getText().length() > 0
                && saleStatus.getText().length() > 0
                && !saleProductsTable.getTableColumns().isEmpty()) {
            saleDate.getText();
            saleSupplierId.getText();
            saleBranchId.getText();
            saleStatus.getText();
            saleNote.getText();

            saleDate.setText("");
            saleSupplierId.setText("");
            saleBranchId.setText("");
            saleStatus.setText("");
            saleNote.setText("");
            saleProductsTable.getTableColumns().clear();
            getSaleProducts().clear();
        }
    }

    public void cancelBtnClicked() {
        ((StackPane) saleFormContentPane.getParent().getParent()).getChildren().get(0).setVisible(true);
        ((StackPane) saleFormContentPane.getParent().getParent()).getChildren().remove(1);
    }

    public void addBtnClicked() {
        dialog.showAndWait();
    }
}
