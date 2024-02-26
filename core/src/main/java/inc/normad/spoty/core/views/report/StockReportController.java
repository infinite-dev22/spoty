package inc.normad.spoty.core.views.report;

import inc.normad.spoty.network_bridge.dtos.StockReport;
import inc.normad.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class StockReportController implements Initializable {
    public MFXTextField searchBar;
    public HBox actionsPane;
    public MFXButton importBtn;
    public MFXButton createBtn;
    public MFXTableView<StockReport> masterTable;
    public BorderPane contentPane;
    private MFXStageDialog dialog;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(this::setupTable);
    }

    public void createBtnClicked(MouseEvent mouseEvent) {
    }

    private void setupTable() {
        MFXTableColumn<StockReport> productName =
                new MFXTableColumn<>("Product Name", true, Comparator.comparing(StockReport::getProductName));
        MFXTableColumn<StockReport> salePrice =
                new MFXTableColumn<>(
                        "Sale Price", true, Comparator.comparing(StockReport::getSalePrice));
        MFXTableColumn<StockReport> purchasePrice =
                new MFXTableColumn<>(
                        "Purchase Price", true, Comparator.comparing(StockReport::getPurchasePrice));
        MFXTableColumn<StockReport> purchasedQuantity =
                new MFXTableColumn<>(
                        "Purchased Qnty", true, Comparator.comparing(StockReport::getPurchasedQuantity));
        MFXTableColumn<StockReport> soldQuantity =
                new MFXTableColumn<>(
                        "Sold Qnty", true, Comparator.comparing(StockReport::getSoldQuantity));
        MFXTableColumn<StockReport> availableStock =
                new MFXTableColumn<>(
                        "Available Stock", true, Comparator.comparing(StockReport::getAvailableStock));
        MFXTableColumn<StockReport> stockSaleValue =
                new MFXTableColumn<>(
                        "Stock Sale Value", true, Comparator.comparing(StockReport::getSaleStockValue));
        MFXTableColumn<StockReport> stockPurchaseValue =
                new MFXTableColumn<>(
                        "Stock Purchase Value", true, Comparator.comparing(StockReport::getPurchaseStockValue));

        productName.setRowCellFactory(category -> new MFXTableRowCell<>(StockReport::getProductName));
        salePrice.setRowCellFactory(
                category -> new MFXTableRowCell<>(StockReport::getSalePrice));
        purchasePrice.setRowCellFactory(
                category -> new MFXTableRowCell<>(StockReport::getPurchasePrice));
        purchasedQuantity.setRowCellFactory(
                category -> new MFXTableRowCell<>(StockReport::getPurchasedQuantity));
        soldQuantity.setRowCellFactory(
                category -> new MFXTableRowCell<>(StockReport::getSoldQuantity));
        availableStock.setRowCellFactory(
                category -> new MFXTableRowCell<>(StockReport::getAvailableStock));
        stockSaleValue.setRowCellFactory(
                category -> new MFXTableRowCell<>(StockReport::getSaleStockValue));
        stockPurchaseValue.setRowCellFactory(
                category -> new MFXTableRowCell<>(StockReport::getPurchaseStockValue));

        productName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.24));
        salePrice.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        purchasePrice.prefWidthProperty().bind(masterTable.widthProperty().multiply(.14));
        purchasedQuantity.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        soldQuantity.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        availableStock.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        stockSaleValue.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        stockPurchaseValue.prefWidthProperty().bind(masterTable.widthProperty().multiply(.18));

        productName.setColumnResizable(false);
        salePrice.setColumnResizable(false);
        purchasePrice.setColumnResizable(false);
        purchasedQuantity.setColumnResizable(false);
        soldQuantity.setColumnResizable(false);
        availableStock.setColumnResizable(false);
        stockSaleValue.setColumnResizable(false);
        stockPurchaseValue.setColumnResizable(false);

        masterTable.getTableColumns().addAll(productName,
                salePrice,
                purchasePrice,
                purchasedQuantity,
                soldQuantity,
                availableStock,
                stockSaleValue,
                stockPurchaseValue);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Product Name", StockReport::getProductName));

        styleStockReportTable();

//        if (StockReportViewModel.getCategories().isEmpty()) {
//            StockReportViewModel.getCategories().addListener(
//                    (ListChangeListener<StockReport>)
//                            c -> masterTable.setItems(StockReportViewModel.getCategories()));
//        } else {
//            masterTable
//                    .itemsProperty()
//                    .bindBidirectional(StockReportViewModel.categoriesProperty());
//        }
    }

    private void styleStockReportTable() {
        masterTable.setPrefSize(1200, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<StockReport> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<StockReport>) event.getSource())
                                        .show(
                                                masterTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<StockReport> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
//                            StockReportViewModel.deleteItem(obj.getData().getId());
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    e.consume();
                });
        // Edit
        edit.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
//                            StockReportViewModel.getItem(obj.getData().getId());
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    dialog.showAndWait();
                    e.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    private void saleTermFormDialogPane(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = fxmlLoader("views/forms/StockReportForm.fxml");
//        fxmlLoader.setControllerFactory(c -> StockReportFormController.getInstance());
//
//        MFXGenericDialog dialogContent = fxmlLoader.load();
//
//        dialogContent.setShowMinimize(false);
//        dialogContent.setShowAlwaysOnTop(false);
//
//        dialog =
//                MFXGenericDialogBuilder.build(dialogContent)
//                        .toStageDialogBuilder()
//                        .initOwner(stage)
//                        .initModality(Modality.WINDOW_MODAL)
//                        .setOwnerNode(saleTermContentPane)
//                        .setScrimPriority(ScrimPriority.WINDOW)
//                        .setScrimOwner(true)
//                        .get();
//
//        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void categoryExpenseCreateBtnClicked() {
        dialog.showAndWait();
    }
}
