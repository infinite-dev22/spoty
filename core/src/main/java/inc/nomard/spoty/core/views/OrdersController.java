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

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.sales.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.previews.*;
import inc.nomard.spoty.network_bridge.dtos.sales.*;
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
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class OrdersController implements Initializable {
    private static OrdersController instance;
    private final Stage stage;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXButton createBtn;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXProgressSpinner progress;
    @FXML
    public HBox leftHeaderPane;
    @FXML
    private MFXTableView<SaleMaster> masterTable;
    private FXMLLoader viewFxmlLoader;
    private MFXStageDialog viewDialog;

    public OrdersController(Stage stage) {
        this.stage = stage;
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
        setSearchBar();
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
                new MFXTableColumn<>("Date", false, Comparator.comparing(SaleMaster::getCreatedAt));
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
        MFXContextMenuItem view = new MFXContextMenuItem("View");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            SaleMasterViewModel.deleteSaleMaster(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getData().getCustomerName() + "'s order", stage, contentPane));
        // View
        view.setOnAction(
                event -> {
                    viewShow(obj.getData());
                    event.consume();
                });

        contextMenu.addItems(view, delete);

        return contextMenu;
    }

    public void createBtnClicked() {
//        BaseController.navigation.navigate(Pages.getPosPane());

        SaleMasterViewModel.getAllSaleMasters(null, null);
    }

    private void onSuccess() {
        SaleMasterViewModel.getAllSaleMasters(null, null);
    }

    private void viewDialogPane(Stage stage) throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/OrderPreview.fxml");
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

    private void successMessage(String message) {
        displayNotification(message, MessageVariants.SUCCESS, "fas-circle-check");
    }

    private void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, "fas-triangle-exclamation");
    }

    private void displayNotification(String message, MessageVariants type, String icon) {
        SpotyMessage notification = new SpotyMessage.MessageBuilder(message)
                .duration(MessageDuration.SHORT)
                .icon(icon)
                .type(type)
                .height(60)
                .build();
        AnchorPane.setTopAnchor(notification, 5.0);
        AnchorPane.setRightAnchor(notification, 5.0);

        var in = Animations.slideInDown(notification, Duration.millis(250));
        if (!BaseController.getInstance(stage).morphPane.getChildren().contains(notification)) {
            BaseController.getInstance(stage).morphPane.getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> SpotyMessage.delay(notification, stage));
        }
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
                SaleMasterViewModel.getAllSaleMasters(null, null);
            }
            progress.setVisible(true);
            SaleMasterViewModel.searchSaleMaster(nv, () -> {
                progress.setVisible(false);
            }, this::errorMessage);
        });
    }
}
