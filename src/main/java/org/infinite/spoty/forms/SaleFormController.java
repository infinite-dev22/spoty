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
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.model.Branch;
import org.infinite.spoty.model.Customer;
import org.infinite.spoty.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

public class SaleFormController implements Initializable {
    @FXML
    public Label saleFormTitle;
    @FXML
    public MFXDatePicker saleDate;
    @FXML
    public MFXFilterComboBox<Customer> saleCustomerId;
    @FXML
    public MFXFilterComboBox<Branch> saleBranchId;
    @FXML
    public MFXTableView<Product> saleProductsTable;
    @FXML
    public MFXTextField saleNote;
    @FXML
    public MFXFilledButton saleSaveBtn;
    @FXML
    public MFXOutlinedButton saleCancelBtn;
    @FXML
    public AnchorPane saleFormContentPane;
    @FXML
    public MFXFilterComboBox<?> saleStatus;
    @FXML
    public MFXElevatedButton saleAddBtn;
    @FXML
    public MFXFilterComboBox<?> salePaymentStatus;
    private Dialog<ButtonType> dialog;

    public SaleFormController(Stage stage) {
        Platform.runLater(() -> {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            try {
                saleProductDialogPane(stage);
            } catch (IOException ex) {
                logger.error(ex.getLocalizedMessage());
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saleAddProductBtnClicked();
        saleCancel();
    }

    private void saleCancel() {
        saleCancelBtn.setOnAction((e) -> {
            ((StackPane) saleFormContentPane.getParent().getParent()).getChildren().get(0).setVisible(true);
            ((StackPane) saleFormContentPane.getParent().getParent()).getChildren().remove(1);
        });
    }

    private void saleAddProductBtnClicked() {
        saleAddBtn.setOnAction(e -> dialog.showAndWait());
    }

    private void saleProductDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/SaleProductsForm.fxml").load();

        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }
}
