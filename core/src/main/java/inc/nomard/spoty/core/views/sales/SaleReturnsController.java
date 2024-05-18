/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package inc.nomard.spoty.core.views.sales;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.animations.*;
import inc.nomard.spoty.core.viewModels.returns.sales.*;
import inc.nomard.spoty.core.views.previews.sales.*;
import inc.nomard.spoty.network_bridge.dtos.returns.sale_returns.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxresources.fonts.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javafx.animation.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;

@SuppressWarnings("unchecked")
public class SaleReturnsController implements Initializable {
    private static SaleReturnsController instance;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXButton createBtn;
    @FXML
    public HBox refresh;
    @FXML
    private MFXTableView<SaleReturnMaster> masterTable;
    private RotateTransition transition;
    private FXMLLoader viewFxmlLoader;
    private MFXStageDialog viewDialog;

    private SaleReturnsController(Stage stage) {
        Platform.runLater(() ->
        {
            try {
                viewDialogPane(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static SaleReturnsController getInstance(Stage stage) {
        if (instance == null) instance = new SaleReturnsController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setIcons();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<SaleReturnMaster> saleReturnCustomer =
                new MFXTableColumn<>("Customer", false, Comparator.comparing(SaleReturnMaster::getCustomerName));
        MFXTableColumn<SaleReturnMaster> saleReturnStatus =
                new MFXTableColumn<>("Order Status", false, Comparator.comparing(SaleReturnMaster::getSaleStatus));
        MFXTableColumn<SaleReturnMaster> saleReturnPaymentStatus =
                new MFXTableColumn<>(
                        "Pay Status", false, Comparator.comparing(SaleReturnMaster::getPaymentStatus));
        MFXTableColumn<SaleReturnMaster> saleReturnDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(SaleReturnMaster::getDate));
        MFXTableColumn<SaleReturnMaster> saleReturnGrandTotal =
                new MFXTableColumn<>("Total Amount", false, Comparator.comparing(SaleReturnMaster::getTotal));
        MFXTableColumn<SaleReturnMaster> saleReturnAmountPaid =
                new MFXTableColumn<>("Paid Amount", false, Comparator.comparing(SaleReturnMaster::getAmountPaid));
        MFXTableColumn<SaleReturnMaster> saleReturnAmountDue =
                new MFXTableColumn<>("Amount Due", false, Comparator.comparing(SaleReturnMaster::getAmountDue));

        saleReturnCustomer.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getCustomerName));
        saleReturnStatus.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getSaleStatus));
        saleReturnPaymentStatus.setRowCellFactory(
                saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getPaymentStatus));
        saleReturnDate.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getLocaleDate));
        saleReturnGrandTotal.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getTotal));
        saleReturnAmountPaid.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getAmountPaid));
        saleReturnAmountDue.setRowCellFactory(saleReturn -> new MFXTableRowCell<>(SaleReturnMaster::getAmountDue));

        saleReturnPaymentStatus.setTooltip(new Tooltip("SaleMaster Return Payment Status"));
        saleReturnStatus.setTooltip(new Tooltip("SaleMaster Return Status"));

        saleReturnDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.143));
        saleReturnCustomer.prefWidthProperty().bind(masterTable.widthProperty().multiply(.143));
        saleReturnStatus.prefWidthProperty().bind(masterTable.widthProperty().multiply(.143));
        saleReturnGrandTotal.prefWidthProperty().bind(masterTable.widthProperty().multiply(.143));
        saleReturnAmountPaid.prefWidthProperty().bind(masterTable.widthProperty().multiply(.143));
        saleReturnPaymentStatus
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.143));

        masterTable
                .getTableColumns()
                .addAll(
                        saleReturnCustomer,
                        saleReturnStatus,
                        saleReturnPaymentStatus,
                        saleReturnDate,
                        saleReturnGrandTotal,
                        saleReturnAmountPaid);

        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Ref No.", SaleReturnMaster::getRef),
                        new StringFilter<>("Customer", SaleReturnMaster::getCustomerName),
                        new StringFilter<>("Sale Status", SaleReturnMaster::getSaleStatus),
                        new StringFilter<>("Payment Status", SaleReturnMaster::getPaymentStatus),
                        new DoubleFilter<>("Grand Total", SaleReturnMaster::getTotal),
                        new DoubleFilter<>("Amount Paid", SaleReturnMaster::getAmountPaid),
                        new DoubleFilter<>("Amount Due", SaleReturnMaster::getAmountDue));

        getSaleReturnMasterTable();

        if (SaleReturnMasterViewModel.getSaleReturns().isEmpty()) {
            SaleReturnMasterViewModel.getSaleReturns()
                    .addListener(
                            (ListChangeListener<SaleReturnMaster>)
                                    c -> masterTable.setItems(SaleReturnMasterViewModel.getSaleReturns()));
        } else {
            masterTable
                    .itemsProperty()
                    .bindBidirectional(SaleReturnMasterViewModel.saleReturnsProperty());
        }
    }

    private void getSaleReturnMasterTable() {
        masterTable.setPrefSize(1200, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<SaleReturnMaster> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<SaleReturnMaster>) event.getSource())
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

    public void createBtnClicked() {
    }

    private void onAction() {
        transition.playFromStart();
        transition.setOnFinished((ActionEvent event) -> transition.playFromStart());
    }

    private void onSuccess() {
        transition.setOnFinished(null);
    }

    private void onFailed() {
        transition.setOnFinished(null);
    }

    private void setIcons() {
        var refreshIcon = new MFXFontIcon("fas-arrows-rotate", 24);
        refresh.getChildren().addFirst(refreshIcon);

        transition = SpotyAnimations.rotateTransition(refreshIcon, Duration.millis(1000), 360);

        refreshIcon.setOnMouseClicked(mouseEvent -> SaleReturnMasterViewModel.getSaleReturnMasters(this::onAction, this::onSuccess, this::onFailed));
    }

    private MFXContextMenu showContextMenu(MFXTableRow<SaleReturnMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        MFXContextMenuItem view = new MFXContextMenuItem("View");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    SaleReturnMasterViewModel.deleteItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }
                            });

                    e.consume();
                });
        // Edit
        edit.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    SaleReturnMasterViewModel.getItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }
                            });

                    createBtnClicked();
                    e.consume();
                });
        // View
        view.setOnAction(
                event -> {
                    try {
                        viewShow(obj.getData());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    event.consume();
                });

        contextMenu.addItems(view, edit, delete);

        return contextMenu;
    }

    private void viewDialogPane(Stage stage) throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/sales/SaleReturnsPreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new SaleReturnsPreviewController());
        MFXGenericDialog genericDialog = viewFxmlLoader.load();
        genericDialog.setShowMinimize(false);
        genericDialog.setShowAlwaysOnTop(false);

        genericDialog.setPrefHeight(screenHeight * .98);
        genericDialog.setPrefWidth(700);

        viewDialog =
                MFXGenericDialogBuilder.build(genericDialog)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(contentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .setCenterInOwnerNode(false)
                        .setOverlayClose(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(viewDialog.getScene());
    }

    public void viewShow(SaleReturnMaster saleReturnsMaster) {
        SaleReturnsPreviewController controller = viewFxmlLoader.getController();
        controller.init(saleReturnsMaster);
        viewDialog.showAndWait();
    }
}
