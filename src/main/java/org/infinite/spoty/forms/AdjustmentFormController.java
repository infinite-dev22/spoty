package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.IntegerFilter;
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
import javafx.util.StringConverter;
import org.infinite.spoty.database.models.AdjustmentDetail;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.viewModels.AdjustmentDetailViewModel;
import org.infinite.spoty.viewModels.AdjustmentMasterViewModel;
import org.infinite.spoty.viewModels.BranchViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

public class AdjustmentFormController implements Initializable {
    @FXML
    public MFXFilterComboBox<Branch> adjustmentBranchId;
    @FXML
    public MFXDatePicker adjustmentDate;
    @FXML
    public MFXTableView<AdjustmentDetail> adjustmentProductsTable;
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

        adjustmentBranchId.valueProperty().bindBidirectional(AdjustmentMasterViewModel.branchProperty());
        adjustmentBranchId.setItems(BranchViewModel.branchesList);
        adjustmentBranchId.setConverter(new StringConverter<>() {
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
        adjustmentDate.textProperty().bindBidirectional(AdjustmentMasterViewModel.dateProperty());
        adjustmentNote.textProperty().bindBidirectional(AdjustmentMasterViewModel.noteProperty());

        adjustmentAddProductBtnClicked();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<AdjustmentDetail> productName = new MFXTableColumn<>("Product", true, Comparator.comparing(AdjustmentDetail::getProductDetailName));
        MFXTableColumn<AdjustmentDetail> productQuantity = new MFXTableColumn<>("Quantity", true, Comparator.comparing(AdjustmentDetail::getQuantity));
        MFXTableColumn<AdjustmentDetail> adjustmentType = new MFXTableColumn<>("Adjustment Type", true, Comparator.comparing(AdjustmentDetail::getAdjustmentType));

        productName.setRowCellFactory(product -> new MFXTableRowCell<>(AdjustmentDetail::getProductDetailName));
        productQuantity.setRowCellFactory(product -> new MFXTableRowCell<>(AdjustmentDetail::getQuantity));
        adjustmentType.setRowCellFactory(product -> new MFXTableRowCell<>(AdjustmentDetail::getAdjustmentType));

        productName.prefWidthProperty().bind(adjustmentProductsTable.widthProperty().multiply(.4));
        productQuantity.prefWidthProperty().bind(adjustmentProductsTable.widthProperty().multiply(.4));
        adjustmentType.prefWidthProperty().bind(adjustmentProductsTable.widthProperty().multiply(.4));

        adjustmentProductsTable.getTableColumns().addAll(productName, productQuantity, adjustmentType);
        adjustmentProductsTable.getFilters().addAll(
                new StringFilter<>("Name", AdjustmentDetail::getProductDetailName),
                new IntegerFilter<>("Quantity", AdjustmentDetail::getQuantity),
                new StringFilter<>("Category", AdjustmentDetail::getAdjustmentType)
        );
        getAdjustmentDetailTable();
        adjustmentProductsTable.setItems(AdjustmentDetailViewModel.adjustmentDetailsTempList);
    }

    private void getAdjustmentDetailTable() {
        adjustmentProductsTable.setPrefSize(1000, 1000);
        adjustmentProductsTable.features().enableBounceEffect();
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
            AdjustmentMasterViewModel.saveAdjustmentMaster();
            AdjustmentMasterViewModel.resetProperties();
            AdjustmentDetailViewModel.adjustmentDetailsTempList.clear();
        }
    }

    public void adjustmentCancelBtnClicked() {
        AdjustmentMasterViewModel.resetProperties();
        AdjustmentDetailViewModel.adjustmentDetailsTempList.clear();
        ((StackPane) adjustmentFormContentPane.getParent()).getChildren().get(0).setVisible(true);
        ((StackPane) adjustmentFormContentPane.getParent()).getChildren().remove(1);
    }
}
