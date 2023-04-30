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
    public MFXTableView<Product> quotationProductsTable;
    @FXML
    public MFXTextField quotationNote;
    @FXML
    public MFXFilledButton quotationSaveBtn;
    @FXML
    public MFXOutlinedButton quotationCancelBtn;
    @FXML
    public AnchorPane quotationFormContentPane;
    @FXML
    public MFXFilterComboBox<?> quotationStatus;
    @FXML
    public MFXElevatedButton quotationAddBtn;
    private Dialog<ButtonType> dialog;

    public QuotationFormController(Stage stage) {
        Platform.runLater(() -> {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            try {
                quotationProductDialogPane(stage);
            } catch (IOException ex) {
                logger.error(ex.getLocalizedMessage());
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        quotationAddProductBtnClicked();
        quotationCancel();
    }

    private void quotationCancel() {
        quotationCancelBtn.setOnAction((e) -> {
            ((StackPane) quotationFormContentPane.getParent()).getChildren().get(0).setVisible(true);
            ((StackPane) quotationFormContentPane.getParent()).getChildren().remove(1);
        });
    }

    private void quotationAddProductBtnClicked() {
        quotationAddBtn.setOnAction(e -> {
            dialog.showAndWait();
        });
    }

    private void quotationProductDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/QuotationProductsForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }
}
