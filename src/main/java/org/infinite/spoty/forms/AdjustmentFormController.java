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
import org.infinite.spoty.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

public class AdjustmentFormController implements Initializable {
    @FXML
    public MFXFilterComboBox<Branch> adjustmentBranchId;
    @FXML
    public MFXDatePicker adjustmentDate;
    @FXML
    public MFXTableView<Product> adjustmentProductsTable;
    @FXML
    public MFXTextField adjustmentNote;
    @FXML
    public MFXFilledButton adjustmentSaveBtn;
    @FXML
    public MFXOutlinedButton adjustmentCancelBtn;
    @FXML
    public AnchorPane adjustmentFormContentPane;
    @FXML
    public Label adjustmentFormTitle;
    @FXML
    public MFXElevatedButton adjustmentProductAddBtn;
    private Dialog<ButtonType> dialog;

    public AdjustmentFormController(Stage stage) {
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
        adjustmentCancel();
        adjustmentAddProductBtnClicked();
    }

    private void adjustmentCancel() {
        adjustmentCancelBtn.setOnAction((e) -> {
            ((StackPane) adjustmentFormContentPane.getParent()).getChildren().get(0).setVisible(true);
            ((StackPane) adjustmentFormContentPane.getParent()).getChildren().remove(1);
        });
    }

    private void adjustmentAddProductBtnClicked() {
        adjustmentProductAddBtn.setOnAction(e -> dialog.showAndWait());
    }

    private void quotationProductDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/AdjustmentProductsForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }
}
