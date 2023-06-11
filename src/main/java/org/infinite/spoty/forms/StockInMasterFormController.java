package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXElevatedButton;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.database.dao.StocKInDetailDao;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.StockInDetail;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.StockInDetailViewModel;
import org.infinite.spoty.viewModels.StockInMasterViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

@SuppressWarnings("unchecked")
public class StockInMasterFormController implements Initializable {
    public MFXTextField stockInDetailID = new MFXTextField();
    public MFXTextField stockInMasterID = new MFXTextField();
    @FXML
    public MFXFilterComboBox<Branch> stockInMasterBranchId;
    @FXML
    public MFXDatePicker stockInMasterDate;
    @FXML
    public MFXTableView<StockInDetail> stockInDetailTable;
    @FXML
    public MFXTextField stockInMasterNote;
    @FXML
    public AnchorPane stockInMasterFormContentPane;
    @FXML
    public Label stockInMasterFormTitle;
    @FXML
    public MFXElevatedButton stockInMasterProductAddBtn;
    private Dialog<ButtonType> dialog;

    public StockInMasterFormController(Stage stage) {
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
        // Input listeners.
        stockInMasterBranchId.textProperty().addListener((observable, oldValue, newValue) -> stockInMasterBranchId.setTrailingIcon(null));
        stockInMasterDate.textProperty().addListener((observable, oldValue, newValue) -> stockInMasterDate.setTrailingIcon(null));
        // Input binding.
        stockInMasterID.textProperty().bindBidirectional(StockInMasterViewModel.idProperty(), new NumberStringConverter());
        stockInMasterBranchId.valueProperty().bindBidirectional(StockInMasterViewModel.branchProperty());
        stockInMasterBranchId.setItems(BranchViewModel.branchesList);
        stockInMasterBranchId.setConverter(new StringConverter<>() {
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
        stockInMasterDate.textProperty().bindBidirectional(StockInMasterViewModel.dateProperty());
        stockInMasterNote.textProperty().bindBidirectional(StockInMasterViewModel.noteProperty());
        stockInMasterAddProductBtnClicked();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<StockInDetail> productName = new MFXTableColumn<>("Product", false, Comparator.comparing(StockInDetail::getProductDetailName));
        MFXTableColumn<StockInDetail> productQuantity = new MFXTableColumn<>("Quantity", false, Comparator.comparing(StockInDetail::getQuantity));
//        MFXTableColumn<StockInDetail> stockInMasterType = new MFXTableColumn<>("StockIn Type", false, Comparator.comparing(StockInDetail::getStockInType));

        productName.setRowCellFactory(product -> new MFXTableRowCell<>(StockInDetail::getProductDetailName));
        productQuantity.setRowCellFactory(product -> new MFXTableRowCell<>(StockInDetail::getQuantity));
//        stockInMasterType.setRowCellFactory(product -> new MFXTableRowCell<>(StockInDetail::getStockInType));

        productName.prefWidthProperty().bind(stockInDetailTable.widthProperty().multiply(.4));
        productQuantity.prefWidthProperty().bind(stockInDetailTable.widthProperty().multiply(.4));
//        stockInMasterType.prefWidthProperty().bind(stockInMasterProductsTable.widthProperty().multiply(.4));

        stockInDetailTable.getTableColumns().addAll(productName, productQuantity); // , stockInMasterType);
        stockInDetailTable.getFilters().addAll(
                new StringFilter<>("Name", StockInDetail::getProductDetailName),
                new IntegerFilter<>("Quantity", StockInDetail::getQuantity)
//                new StringFilter<>("Category", StockInDetail::getStockInType)
        );
        getStockInDetailTable();
        stockInDetailTable.setItems(StockInDetailViewModel.stockInDetailsTempList);
    }

    private void getStockInDetailTable() {
        stockInDetailTable.setPrefSize(1000, 1000);
        stockInDetailTable.features().enableBounceEffect();
        stockInDetailTable.features().enableSmoothScrolling(0.5);

        stockInDetailTable.setTableRowFactory(t -> {
            MFXTableRow<StockInDetail> row = new MFXTableRow<>(stockInDetailTable, t);
            EventHandler<ContextMenuEvent> eventHandler = event -> {
                showContextMenu((MFXTableRow<StockInDetail>) event.getSource())
                        .show(stockInDetailTable.getScene().getWindow(), event.getScreenX(), event.getScreenY());
                event.consume();
            };
            row.setOnContextMenuRequested(eventHandler);
            return row;
        });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<StockInDetail> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(stockInDetailTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(e -> {
            StockInDetailViewModel.getItem(obj.getData(), StockInDetailViewModel.stockInDetailsTempList.indexOf(obj.getData()));
            try {
                if (Integer.parseInt(stockInDetailID.getText()) > 0)
                    StocKInDetailDao.deleteStockInDetail(Integer.parseInt(stockInDetailID.getText()));
            } catch (NumberFormatException ignored) {
                StockInDetailViewModel.removeStockInDetail(StockInDetailViewModel.stockInDetailsTempList.indexOf(obj.getData()));
            }
            StockInDetailViewModel.getStockInDetails();
            e.consume();
        });
        // Edit
        edit.setOnAction(e -> {
            StockInDetailViewModel.getItem(obj.getData(), StockInDetailViewModel.stockInDetailsTempList.indexOf(obj.getData()));
            dialog.showAndWait();
            e.consume();
        });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    private void stockInMasterAddProductBtnClicked() {
        stockInMasterProductAddBtn.setOnAction(e -> dialog.showAndWait());
    }

    private void quotationProductDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/StockInDetailForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }

    public void stockInMasterSaveBtnClicked() {
        MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

        if (stockInMasterBranchId.getText().length() == 0) {
            stockInMasterBranchId.setTrailingIcon(icon);
        }
        if (stockInMasterDate.getText().length() == 0) {
            stockInMasterDate.setTrailingIcon(icon);
        }
        if (stockInDetailTable.getTableColumns().isEmpty()) {
            // Notify table can't be empty
            System.out.println("Table can't be empty");
        }
        if (stockInMasterBranchId.getText().length() > 0
                && stockInMasterDate.getText().length() > 0
                && !stockInDetailTable.getTableColumns().isEmpty()) {
            if (Integer.parseInt(stockInMasterID.getText()) > 0) {
                StockInMasterViewModel.updateItem(Integer.parseInt(stockInMasterID.getText()));
                stockInMasterCancelBtnClicked();
            } else
                StockInMasterViewModel.saveStockInMaster();
            StockInMasterViewModel.resetProperties();
            StockInDetailViewModel.stockInDetailsTempList.clear();
        }
    }

    public void stockInMasterCancelBtnClicked() {
        StockInMasterViewModel.resetProperties();
        StockInDetailViewModel.stockInDetailsTempList.clear();
        ((StackPane) stockInMasterFormContentPane.getParent()).getChildren().get(0).setVisible(true);
        ((StackPane) stockInMasterFormContentPane.getParent()).getChildren().remove(1);
    }
}
