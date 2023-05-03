package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXElevatedButton;
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
import org.infinite.spoty.models.AdjustmentProduct;
import org.infinite.spoty.models.Branch;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.dataShare.DataShare.getAdjustmentProducts;

public class AdjustmentFormController implements Initializable {
    @FXML
    public MFXFilterComboBox<Branch> adjustmentBranchId;
    @FXML
    public MFXDatePicker adjustmentDate;
    @FXML
    public MFXTableView<AdjustmentProduct> adjustmentProductsTable;
    @FXML
    public MFXTextField adjustmentNote;
    @FXML
    public AnchorPane adjustmentFormContentPane;
    @FXML
    public Label adjustmentFormTitle;
    @FXML
    public MFXElevatedButton adjustmentProductAddBtn;
    private Dialog<ButtonType> dialog;

    public AdjustmentFormController(Stage stage) {
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
        adjustmentBranchId.textProperty().addListener((observable, oldValue, newValue) -> adjustmentBranchId.setTrailingIcon(null));
        adjustmentDate.textProperty().addListener((observable, oldValue, newValue) -> adjustmentDate.setTrailingIcon(null));

        adjustmentAddProductBtnClicked();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<AdjustmentProduct> productName = new MFXTableColumn<>("Product", true, Comparator.comparing(AdjustmentProduct::product));
        MFXTableColumn<AdjustmentProduct> productQuantity = new MFXTableColumn<>("Quantity", true, Comparator.comparing(AdjustmentProduct::quantity));
        MFXTableColumn<AdjustmentProduct> adjustmentType = new MFXTableColumn<>("Adjustment Type", true, Comparator.comparing(AdjustmentProduct::adjustmentType));

        productName.setRowCellFactory(product -> new MFXTableRowCell<>(AdjustmentProduct::product));
        productQuantity.setRowCellFactory(product -> new MFXTableRowCell<>(AdjustmentProduct::quantity));
        adjustmentType.setRowCellFactory(product -> new MFXTableRowCell<>(AdjustmentProduct::adjustmentType));

        adjustmentProductsTable.getTableColumns().addAll(productName, productQuantity, adjustmentType);
        adjustmentProductsTable.getFilters().addAll(
                new StringFilter<>("Name", AdjustmentProduct::product),
                new DoubleFilter<>("Code", AdjustmentProduct::quantity),
                new StringFilter<>("Category", AdjustmentProduct::adjustmentType)
        );
        getAdjustmentProductTable();
        adjustmentProductsTable.setItems(getAdjustmentProducts());
    }

    private void getAdjustmentProductTable() {
        adjustmentProductsTable.setPrefSize(1000, 1000);
        adjustmentProductsTable.features().enableBounceEffect();
        adjustmentProductsTable.autosizeColumnsOnInitialization();
        adjustmentProductsTable.features().enableSmoothScrolling(0.5);
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

    public void adjustmentSaveBtnClicked() {
        MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

        if (adjustmentBranchId.getText().length() == 0) {
            adjustmentBranchId.setTrailingIcon(icon);
        }
        if (adjustmentDate.getText().length() == 0) {
            adjustmentDate.setTrailingIcon(icon);
        }
        if (adjustmentProductsTable.getTableColumns().isEmpty()) {
            // Notify table can't be empty
            System.out.println("Table can't be empty");
        }
        if (adjustmentBranchId.getText().length() > 0 && adjustmentDate.getText().length() > 0 && !adjustmentProductsTable.getTableColumns().isEmpty()) {
            adjustmentBranchId.getText();
            adjustmentDate.getText();
            adjustmentNote.getText();
//            getAdjustmentProducts();

            adjustmentBranchId.setText("");
            adjustmentDate.setText("");
            adjustmentNote.setText("");
            adjustmentProductsTable.getTableColumns().clear();
            getAdjustmentProducts().clear();
        }
    }

    public void adjustmentCancelBtnClicked() {
        ((StackPane) adjustmentFormContentPane.getParent()).getChildren().get(0).setVisible(true);
        ((StackPane) adjustmentFormContentPane.getParent()).getChildren().remove(1);
    }
}
