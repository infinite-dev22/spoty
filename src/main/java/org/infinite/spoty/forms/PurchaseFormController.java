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
import org.infinite.spoty.models.Product;
import org.infinite.spoty.models.Supplier;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.dataShare.DataShare.getPurchaseProducts;

public class PurchaseFormController implements Initializable {
    @FXML
    public Label purchaseFormTitle;
    @FXML
    public MFXDatePicker purchaseDate;
    @FXML
    public MFXFilterComboBox<Supplier> purchaseSupplierId;
    @FXML
    public MFXFilterComboBox<Branch> purchaseBranchId;
    @FXML
    public MFXTableView<Product> purchaseProductsTable;
    @FXML
    public MFXTextField purchaseNote;
    @FXML
    public AnchorPane purchaseFormContentPane;
    @FXML
    public MFXFilterComboBox<?> purchaseStatus;
    private Dialog<ButtonType> dialog;

    public PurchaseFormController(Stage stage) {
        Platform.runLater(() -> {
            try {
                purchaseProductDialogPane(stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        purchaseDate.textProperty().addListener((observable, oldValue, newValue) -> purchaseDate.setTrailingIcon(null));
        purchaseSupplierId.textProperty().addListener((observable, oldValue, newValue) -> purchaseSupplierId.setTrailingIcon(null));
        purchaseBranchId.textProperty().addListener((observable, oldValue, newValue) -> purchaseBranchId.setTrailingIcon(null));
        purchaseStatus.textProperty().addListener((observable, oldValue, newValue) -> purchaseStatus.setTrailingIcon(null));
    }

    private void purchaseProductDialogPane(Stage stage) throws IOException {
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
        if (purchaseProductsTable.getTableColumns().isEmpty()) {
            // Notify table can't be empty
            System.out.println("Table can't be empty");
        }
        if (purchaseDate.getText().length() > 0
                && purchaseSupplierId.getText().length() > 0
                && purchaseBranchId.getText().length() > 0
                && purchaseStatus.getText().length() > 0
                && !purchaseProductsTable.getTableColumns().isEmpty()) {
            purchaseDate.getText();
            purchaseSupplierId.getText();
            purchaseBranchId.getText();
            purchaseStatus.getText();
            purchaseNote.getText();

            purchaseDate.setText("");
            purchaseSupplierId.setText("");
            purchaseBranchId.setText("");
            purchaseStatus.setText("");
            purchaseNote.setText("");
            purchaseProductsTable.getTableColumns().clear();
            getPurchaseProducts().clear();
        }
    }

    public void cancelBtnClicked() {
        ((StackPane) purchaseFormContentPane.getParent().getParent()).getChildren().get(0).setVisible(true);
        ((StackPane) purchaseFormContentPane.getParent().getParent()).getChildren().remove(1);
    }

    public void addBtnClicked() {
        dialog.showAndWait();
    }
}
