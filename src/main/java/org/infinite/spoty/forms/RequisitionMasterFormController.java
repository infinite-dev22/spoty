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
import org.infinite.spoty.database.models.RequisitionDetail;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.RequisitionDetailViewModel;
import org.infinite.spoty.viewModels.RequisitionMasterViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

public class RequisitionMasterFormController implements Initializable {
    @FXML
    public MFXFilterComboBox<Branch> requisitionMasterBranchId;
    @FXML
    public MFXDatePicker requisitionMasterDate;
    @FXML
    public MFXTableView<RequisitionDetail> requisitionMasterProductsTable;
    @FXML
    public MFXTextField requisitionMasterNote;
    @FXML
    public AnchorPane requisitionMasterFormContentPane;
    @FXML
    public Label requisitionMasterFormTitle;
    @FXML
    public MFXElevatedButton requisitionMasterProductAddBtn;
    private Dialog<ButtonType> dialog;

    public RequisitionMasterFormController(Stage stage) {
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
        requisitionMasterBranchId.textProperty().addListener((observable, oldValue, newValue) -> requisitionMasterBranchId.setTrailingIcon(null));
        requisitionMasterDate.textProperty().addListener((observable, oldValue, newValue) -> requisitionMasterDate.setTrailingIcon(null));

        requisitionMasterBranchId.valueProperty().bindBidirectional(RequisitionMasterViewModel.branchProperty());
        requisitionMasterBranchId.setItems(BranchViewModel.branchesList);
        requisitionMasterBranchId.setConverter(new StringConverter<>() {
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
        requisitionMasterDate.textProperty().bindBidirectional(RequisitionMasterViewModel.dateProperty());
        requisitionMasterNote.textProperty().bindBidirectional(RequisitionMasterViewModel.noteProperty());

        requisitionMasterAddProductBtnClicked();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<RequisitionDetail> productName = new MFXTableColumn<>("Product", false, Comparator.comparing(RequisitionDetail::getProductDetailName));
        MFXTableColumn<RequisitionDetail> productQuantity = new MFXTableColumn<>("Quantity", false, Comparator.comparing(RequisitionDetail::getQuantity));
//        MFXTableColumn<RequisitionDetail> requisitionMasterType = new MFXTableColumn<>("Requisition Type", false, Comparator.comparing(RequisitionDetail::getRequisitionType));

        productName.setRowCellFactory(product -> new MFXTableRowCell<>(RequisitionDetail::getProductDetailName));
        productQuantity.setRowCellFactory(product -> new MFXTableRowCell<>(RequisitionDetail::getQuantity));
//        requisitionMasterType.setRowCellFactory(product -> new MFXTableRowCell<>(RequisitionDetail::getRequisitionType));

        productName.prefWidthProperty().bind(requisitionMasterProductsTable.widthProperty().multiply(.4));
        productQuantity.prefWidthProperty().bind(requisitionMasterProductsTable.widthProperty().multiply(.4));
//        requisitionMasterType.prefWidthProperty().bind(requisitionMasterProductsTable.widthProperty().multiply(.4));

        requisitionMasterProductsTable.getTableColumns().addAll(productName, productQuantity); // , requisitionMasterType);
        requisitionMasterProductsTable.getFilters().addAll(
                new StringFilter<>("Name", RequisitionDetail::getProductDetailName),
                new IntegerFilter<>("Quantity", RequisitionDetail::getQuantity)
//                new StringFilter<>("Category", RequisitionDetail::getRequisitionType)
        );
        getRequisitionDetailTable();
        requisitionMasterProductsTable.setItems(RequisitionDetailViewModel.requisitionDetailsTempList);
    }

    private void getRequisitionDetailTable() {
        requisitionMasterProductsTable.setPrefSize(1000, 1000);
        requisitionMasterProductsTable.features().enableBounceEffect();
        requisitionMasterProductsTable.features().enableSmoothScrolling(0.5);
    }

    private void requisitionMasterAddProductBtnClicked() {
        requisitionMasterProductAddBtn.setOnAction(e -> dialog.showAndWait());
    }

    private void quotationProductDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/RequisitionDetailForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }

    public void requisitionMasterSaveBtnClicked() {
        MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

        if (requisitionMasterBranchId.getText().length() == 0) {
            requisitionMasterBranchId.setTrailingIcon(icon);
        }
        if (requisitionMasterDate.getText().length() == 0) {
            requisitionMasterDate.setTrailingIcon(icon);
        }
        if (requisitionMasterProductsTable.getTableColumns().isEmpty()) {
            // Notify table can't be empty
            System.out.println("Table can't be empty");
        }
        if (requisitionMasterBranchId.getText().length() > 0 && requisitionMasterDate.getText().length() > 0 && !requisitionMasterProductsTable.getTableColumns().isEmpty()) {
            RequisitionMasterViewModel.saveRequisitionMaster();
            RequisitionMasterViewModel.resetProperties();
            RequisitionDetailViewModel.requisitionDetailsTempList.clear();
        }
    }

    public void requisitionMasterCancelBtnClicked() {
        RequisitionMasterViewModel.resetProperties();
        RequisitionDetailViewModel.requisitionDetailsTempList.clear();
        ((StackPane) requisitionMasterFormContentPane.getParent()).getChildren().get(0).setVisible(true);
        ((StackPane) requisitionMasterFormContentPane.getParent()).getChildren().remove(1);
    }
}
