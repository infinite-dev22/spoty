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
import inc.nomard.spoty.core.viewModels.returns.sales.*;
import inc.nomard.spoty.core.views.previews.*;
import inc.nomard.spoty.network_bridge.dtos.returns.sale_returns.*;
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
    public MFXProgressSpinner progress;
    @FXML
    public HBox leftHeaderPane;
    @FXML
    private MFXTableView<SaleReturnMaster> masterTable;
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
        setSearchBar();
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

    private void onSuccess() {
        SaleReturnMasterViewModel.getSaleReturnMasters(null, null);
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
                    SaleReturnMasterViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
                    e.consume();
                });
        // Edit
        edit.setOnAction(
                e -> {
                    SaleReturnMasterViewModel.getItem(obj.getData().getId(), this::createBtnClicked, this::errorMessage);
                    createBtnClicked();
                    e.consume();
                });
        // View
        view.setOnAction(
                event -> {
                    viewShow(obj.getData());
                    event.consume();
                });

        contextMenu.addItems(view, edit, delete);

        return contextMenu;
    }

    private void viewDialogPane(Stage stage) throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/SaleReturnsPreview.fxml");
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
                SaleReturnMasterViewModel.getSaleReturnMasters(null, null);
            }
            progress.setVisible(true);
            SaleReturnMasterViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
            }, this::errorMessage);
        });
    }
}
