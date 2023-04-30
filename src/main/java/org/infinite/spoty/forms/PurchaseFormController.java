package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXElevatedButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.SpotResourceLoader;
import org.infinite.spoty.model.Branch;
import org.infinite.spoty.model.Customer;
import org.infinite.spoty.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

public class PurchaseFormController implements Initializable {
    @FXML
    public Label purchaseFormTitle;
    @FXML
    public MFXDatePicker purchaseDate;
    @FXML
    public MFXFilterComboBox<Customer> purchaseCustomerId;
    @FXML
    public MFXFilterComboBox<Branch> purchaseBranchId;
    @FXML
    public MFXTableView<Product> purchaseProductsTable;
    @FXML
    public MFXTextField purchaseNote;
    @FXML
    public MFXFilledButton purchaseSaveBtn;
    @FXML
    public MFXOutlinedButton purchaseCancelBtn;
    @FXML
    public AnchorPane purchaseFormContentPane;
    @FXML
    public MFXFilterComboBox<?> purchaseStatus;
    @FXML
    public MFXElevatedButton purchaseAddBtn;
    private Dialog<ButtonType> dialog;

    public PurchaseFormController(Stage stage) {
        Platform.runLater(() -> {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            try {
                purchaseProductDialogPane(stage);
            } catch (IOException ex) {
                logger.error(ex.getLocalizedMessage());
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        purchaseAddProductBtnClicked();
        purchaseCancel();
    }

    private void purchaseCancel() {
        purchaseCancelBtn.setOnAction((e) -> {
            ((StackPane) purchaseFormContentPane.getParent().getParent()).getChildren().get(0).setVisible(true);
            ((StackPane) purchaseFormContentPane.getParent().getParent()).getChildren().remove(1);
        });
    }

    private void purchaseAddProductBtnClicked() {
        purchaseAddBtn.setOnAction(e -> dialog.showAndWait());
    }

    private void purchaseProductDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/PurchaseProductsForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }
}
