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
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.TransferDetail;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.TransferDetailViewModel;
import org.infinite.spoty.viewModels.TransferMasterViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

public class TransferMasterFormController implements Initializable {
    @FXML
    public MFXFilterComboBox<Branch> transferMasterFromBranchId;
    public MFXFilterComboBox<Branch> transferMasterToBranchId;
    @FXML
    public MFXDatePicker transferMasterDate;
    @FXML
    public MFXTableView<TransferDetail> transferMasterProductsTable;
    @FXML
    public MFXTextField transferMasterNote;
    @FXML
    public AnchorPane transferMasterFormContentPane;
    @FXML
    public Label transferMasterFormTitle;
    @FXML
    public MFXElevatedButton transferMasterProductAddBtn;
    private Dialog<ButtonType> dialog;

    public TransferMasterFormController(Stage stage) {
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
        transferMasterFromBranchId.textProperty().addListener((observable, oldValue, newValue) -> transferMasterFromBranchId.setTrailingIcon(null));
        transferMasterToBranchId.textProperty().addListener((observable, oldValue, newValue) -> transferMasterToBranchId.setTrailingIcon(null));
        transferMasterDate.textProperty().addListener((observable, oldValue, newValue) -> transferMasterDate.setTrailingIcon(null));

        transferMasterFromBranchId.valueProperty().bindBidirectional(TransferMasterViewModel.fromBranchProperty());
        transferMasterFromBranchId.setItems(BranchViewModel.branchesList);
        transferMasterFromBranchId.setConverter(new StringConverter<>() {
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
        transferMasterToBranchId.valueProperty().bindBidirectional(TransferMasterViewModel.toBranchProperty());
        transferMasterToBranchId.setItems(BranchViewModel.branchesList);
        transferMasterToBranchId.setConverter(new StringConverter<>() {
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
        transferMasterDate.textProperty().bindBidirectional(TransferMasterViewModel.dateProperty());
        transferMasterNote.textProperty().bindBidirectional(TransferMasterViewModel.noteProperty());

        transferMasterAddProductBtnClicked();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<TransferDetail> productName = new MFXTableColumn<>("Product", false, Comparator.comparing(TransferDetail::getProductDetailName));
        MFXTableColumn<TransferDetail> productQuantity = new MFXTableColumn<>("Quantity", false, Comparator.comparing(TransferDetail::getQuantity));
//        MFXTableColumn<TransferDetail> transferMasterType = new MFXTableColumn<>("Transfer Type", false, Comparator.comparing(TransferDetail::getTransferType));

        productName.setRowCellFactory(product -> new MFXTableRowCell<>(TransferDetail::getProductDetailName));
        productQuantity.setRowCellFactory(product -> new MFXTableRowCell<>(TransferDetail::getQuantity));
//        transferMasterType.setRowCellFactory(product -> new MFXTableRowCell<>(TransferDetail::getTransferType));

        productName.prefWidthProperty().bind(transferMasterProductsTable.widthProperty().multiply(.4));
        productQuantity.prefWidthProperty().bind(transferMasterProductsTable.widthProperty().multiply(.4));
//        transferMasterType.prefWidthProperty().bind(transferMasterProductsTable.widthProperty().multiply(.4));

        transferMasterProductsTable.getTableColumns().addAll(productName, productQuantity); // , transferMasterType);
        transferMasterProductsTable.getFilters().addAll(
                new StringFilter<>("Name", TransferDetail::getProductDetailName),
                new IntegerFilter<>("Quantity", TransferDetail::getQuantity)
//                new StringFilter<>("Category", TransferDetail::getTransferType)
        );
        getTransferDetailTable();
        transferMasterProductsTable.setItems(TransferDetailViewModel.transferDetailsTempList);
    }

    private void getTransferDetailTable() {
        transferMasterProductsTable.setPrefSize(1000, 1000);
        transferMasterProductsTable.features().enableBounceEffect();
        transferMasterProductsTable.features().enableSmoothScrolling(0.5);
    }

    private void transferMasterAddProductBtnClicked() {
        transferMasterProductAddBtn.setOnAction(e -> dialog.showAndWait());
    }

    private void quotationProductDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/TransferDetailForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }

    public void transferMasterSaveBtnClicked() {
        MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

        if (transferMasterFromBranchId.getText().length() == 0) {
            transferMasterFromBranchId.setTrailingIcon(icon);
        }
        if (transferMasterToBranchId.getText().length() == 0) {
            transferMasterToBranchId.setTrailingIcon(icon);
        }
        if (transferMasterDate.getText().length() == 0) {
            transferMasterDate.setTrailingIcon(icon);
        }
        if (transferMasterProductsTable.getTableColumns().isEmpty()) {
            // Notify table can't be empty
            System.out.println("Table can't be empty");
        }
        if (transferMasterFromBranchId.getText().length() > 0
                && transferMasterToBranchId.getText().length() > 0
                && transferMasterDate.getText().length() > 0
                && !transferMasterProductsTable.getTableColumns().isEmpty()) {
            TransferMasterViewModel.saveTransferMaster();
            TransferMasterViewModel.resetProperties();
            TransferDetailViewModel.transferDetailsTempList.clear();
        }
    }

    public void transferMasterCancelBtnClicked() {
        TransferMasterViewModel.resetProperties();
        TransferDetailViewModel.transferDetailsTempList.clear();
        ((StackPane) transferMasterFormContentPane.getParent()).getChildren().get(0).setVisible(true);
        ((StackPane) transferMasterFormContentPane.getParent()).getChildren().remove(1);
    }
}
