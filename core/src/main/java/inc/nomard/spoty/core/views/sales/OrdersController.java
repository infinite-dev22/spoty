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
import inc.nomard.spoty.core.components.navigation.*;
import inc.nomard.spoty.core.viewModels.sales.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.core.views.previews.sales.*;
import inc.nomard.spoty.network_bridge.dtos.sales.*;
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
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;

@SuppressWarnings("unchecked")
public class OrdersController implements Initializable {
    private static OrdersController instance;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXButton createBtn;
    @FXML
    public BorderPane contentPane;
    @FXML
    public HBox refresh;
    @FXML
    private MFXTableView<SaleMaster> masterTable;
    private RotateTransition transition;
    private FXMLLoader viewFxmlLoader;
    private MFXStageDialog viewDialog;

    public OrdersController(Stage stage) {
        Platform.runLater(() ->
        {
            try {
                viewDialogPane(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static OrdersController getInstance(Stage stage) {
        if (instance == null) instance = new OrdersController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setIcons();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<SaleMaster> saleCustomer =
                new MFXTableColumn<>("Customer", false, Comparator.comparing(SaleMaster::getCustomerName));
        MFXTableColumn<SaleMaster> saleStatus =
                new MFXTableColumn<>("Order Status", false, Comparator.comparing(SaleMaster::getSaleStatus));
        MFXTableColumn<SaleMaster> salePaymentStatus =
                new MFXTableColumn<>(
                        "Pay Status", false, Comparator.comparing(SaleMaster::getPaymentStatus));
        MFXTableColumn<SaleMaster> saleDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(SaleMaster::getDate));
        MFXTableColumn<SaleMaster> saleGrandTotal =
                new MFXTableColumn<>("Total Amount", false, Comparator.comparing(SaleMaster::getTotal));
        MFXTableColumn<SaleMaster> saleAmountPaid =
                new MFXTableColumn<>("Paid Amount", false, Comparator.comparing(SaleMaster::getAmountPaid));
        MFXTableColumn<SaleMaster> saleAmountDue =
                new MFXTableColumn<>("Amount Due", false, Comparator.comparing(SaleMaster::getAmountDue));

        saleCustomer.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getCustomerName));
        saleStatus.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getSaleStatus));
        salePaymentStatus.setRowCellFactory(
                sale -> new MFXTableRowCell<>(SaleMaster::getPaymentStatus));
        saleDate.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getLocaleDate));
        saleGrandTotal.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getTotal));
        saleAmountPaid.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getAmountPaid));
        saleAmountDue.setRowCellFactory(sale -> new MFXTableRowCell<>(SaleMaster::getAmountDue));

        saleCustomer.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        saleStatus.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        salePaymentStatus.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        saleDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        saleGrandTotal.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        saleAmountPaid.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        saleAmountDue.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));

        masterTable
                .getTableColumns()
                .addAll(
                        saleCustomer,
                        saleStatus,
                        salePaymentStatus,
                        saleDate,
                        saleGrandTotal,
                        saleAmountPaid,
                        saleAmountDue);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Ref No.", SaleMaster::getRef),
                        new StringFilter<>("Customer", SaleMaster::getCustomerName),
                        new StringFilter<>("Order Status", SaleMaster::getSaleStatus),
                        new StringFilter<>("Payment Status", SaleMaster::getPaymentStatus),
                        new DoubleFilter<>("Grand Total", SaleMaster::getTotal),
                        new DoubleFilter<>("Amount Paid", SaleMaster::getAmountPaid),
                        new DoubleFilter<>("Amount Due", SaleMaster::getAmountDue));
        styleSaleMasterTable();

        if (SaleMasterViewModel.getSales().isEmpty()) {
            SaleMasterViewModel.getSales()
                    .addListener(
                            (ListChangeListener<SaleMaster>)
                                    c -> masterTable.setItems(SaleMasterViewModel.getSales()));
        } else {
            masterTable.itemsProperty().bindBidirectional(SaleMasterViewModel.salesProperty());
        }
    }

    private void styleSaleMasterTable() {
        masterTable.setPrefSize(1200, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<SaleMaster> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<SaleMaster>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<SaleMaster> obj) {
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
                                    SaleMasterViewModel.deleteItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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
                                    SaleMasterViewModel.getItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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

    public void createBtnClicked() {
        BaseController.navigation.navigate(Pages.getPosPane());
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

        refreshIcon.setOnMouseClicked(mouseEvent -> SaleMasterViewModel.getAllSaleMasters(this::onAction, this::onSuccess, this::onFailed));
    }

    private void viewDialogPane(Stage stage) throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/sales/OrderPreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new SalePreviewController());
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

    public void viewShow(SaleMaster saleMaster) {
        SalePreviewController controller = viewFxmlLoader.getController();
        controller.init(saleMaster);
        viewDialog.showAndWait();
    }
}
