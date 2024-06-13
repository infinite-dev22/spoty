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

package inc.nomard.spoty.core.views;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.returns.purchases.*;
import inc.nomard.spoty.core.views.previews.*;
import inc.nomard.spoty.network_bridge.dtos.returns.purchase_returns.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxresources.fonts.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class PurchaseReturnController implements Initializable {
    private static PurchaseReturnController instance;
    @FXML
    public MFXTableView<PurchaseReturnMaster> masterTable;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXButton createBtn;
    @FXML
    public MFXProgressSpinner progress;
    @FXML
    public HBox leftHeaderPane;
    private FXMLLoader viewFxmlLoader;
    private MFXStageDialog viewDialog;

    private PurchaseReturnController(Stage stage) {
        Platform.runLater(() ->
        {
            try {
                viewDialogPane(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static PurchaseReturnController getInstance(Stage stage) {
        if (instance == null) instance = new PurchaseReturnController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setIcons();
        setSearchBar();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnSupplier =
                new MFXTableColumn<>(
                        "Supplier", false, Comparator.comparing(PurchaseReturnMaster::getSupplierName));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnStatus =
                new MFXTableColumn<>("Status", false, Comparator.comparing(PurchaseReturnMaster::getStatus));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnPaymentStatus =
                new MFXTableColumn<>(
                        "Pay Status", false, Comparator.comparing(PurchaseReturnMaster::getPaymentStatus));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(PurchaseReturnMaster::getDate));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnGrandTotal =
                new MFXTableColumn<>("Total Amount", false, Comparator.comparing(PurchaseReturnMaster::getTotal));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnAmountPaid =
                new MFXTableColumn<>("Paid Amount", false, Comparator.comparing(PurchaseReturnMaster::getAmountPaid));
        MFXTableColumn<PurchaseReturnMaster> purchaseReturnAmountDue =
                new MFXTableColumn<>("Due Amount", false, Comparator.comparing(PurchaseReturnMaster::getAmountDue));

        purchaseReturnSupplier.setRowCellFactory(
                purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getSupplierName));
        purchaseReturnStatus.setRowCellFactory(
                purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getStatus));
        purchaseReturnPaymentStatus.setRowCellFactory(
                purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getPaymentStatus));
        purchaseReturnDate.setRowCellFactory(
                purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getLocaleDate));
        purchaseReturnGrandTotal.setRowCellFactory(
                purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getTotal));
        purchaseReturnAmountPaid.setRowCellFactory(
                purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getAmountPaid));
        purchaseReturnAmountDue.setRowCellFactory(
                purchaseReturn -> new MFXTableRowCell<>(PurchaseReturnMaster::getAmountDue));
        purchaseReturnPaymentStatus.setTooltip(new Tooltip("PurchaseReturnMaster Return Payment Status"));
        purchaseReturnStatus.setTooltip(new Tooltip("PurchaseReturnMaster Return Status"));

        purchaseReturnDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.13));
        purchaseReturnSupplier
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        purchaseReturnStatus
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        purchaseReturnGrandTotal
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        purchaseReturnAmountPaid
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        purchaseReturnPaymentStatus
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));

        masterTable
                .getTableColumns()
                .addAll(
                        purchaseReturnSupplier,
                        purchaseReturnStatus,
                        purchaseReturnPaymentStatus,
                        purchaseReturnDate,
                        purchaseReturnGrandTotal,
                        purchaseReturnAmountPaid);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Reference", PurchaseReturnMaster::getRef),
                        new StringFilter<>("Supplier", PurchaseReturnMaster::getSupplierName),
                        new StringFilter<>("Status", PurchaseReturnMaster::getStatus),
                        new StringFilter<>("Pay Status", PurchaseReturnMaster::getPaymentStatus),
                        new DoubleFilter<>("Total", PurchaseReturnMaster::getTotal),
                        new DoubleFilter<>("Paid", PurchaseReturnMaster::getAmountPaid),
                        new DoubleFilter<>("Due", PurchaseReturnMaster::getAmountDue));

        stylePurchaseReturnMasterTable();

        if (PurchaseReturnMasterViewModel.getPurchaseReturns().isEmpty()) {
            PurchaseReturnMasterViewModel.getPurchaseReturns()
                    .addListener(
                            (ListChangeListener<PurchaseReturnMaster>)
                                    c ->
                                            masterTable.setItems(
                                                    PurchaseReturnMasterViewModel.getPurchaseReturns()));
        } else {
            masterTable
                    .itemsProperty()
                    .bindBidirectional(PurchaseReturnMasterViewModel.purchaseReturnsProperty());
        }
    }

    private void stylePurchaseReturnMasterTable() {
        masterTable.setPrefSize(1200, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<PurchaseReturnMaster> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<PurchaseReturnMaster>) event.getSource())
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

    private void onSuccess() {
        PurchaseReturnMasterViewModel.getPurchaseReturnMasters(null, null);
    }

    private MFXContextMenu showContextMenu(MFXTableRow<PurchaseReturnMaster> obj) {
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
                                    PurchaseReturnMasterViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
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
                                    PurchaseReturnMasterViewModel.getItem(obj.getData().getId(), this::createBtnClicked, this::errorMessage);
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }
                            });
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
        viewFxmlLoader = fxmlLoader("views/previews/PurchaseReturnsPreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new PurchaseReturnsPreviewController());
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

    public void viewShow(PurchaseReturnMaster purchaseReturn) {
        PurchaseReturnsPreviewController controller = viewFxmlLoader.getController();
        controller.init(purchaseReturn);
        viewDialog.showAndWait();
    }

    private void successMessage(String message) {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        AnchorPane.setRightAnchor(notification, 40.0);
        AnchorPane.setTopAnchor(notification, 10.0);
    }

    private void errorMessage(String message) {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
        notificationHolder.addMessage(notification);
        AnchorPane.setRightAnchor(notification, 40.0);
        AnchorPane.setTopAnchor(notification, 10.0);
    }

    private void setIcons() {
        searchBar.setTrailingIcon(new MFXFontIcon("fas-magnifying-glass"));
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                PurchaseReturnMasterViewModel.getPurchaseReturnMasters(null, null);
            }
            progress.setVisible(true);
            PurchaseReturnMasterViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
            }, this::errorMessage);
        });
    }
}