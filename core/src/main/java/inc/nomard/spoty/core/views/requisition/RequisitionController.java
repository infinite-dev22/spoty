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

package inc.nomard.spoty.core.views.requisition;

import inc.nomard.spoty.core.components.animations.SpotyAnimations;
import inc.nomard.spoty.core.components.navigation.Pages;
import inc.nomard.spoty.core.viewModels.requisitions.RequisitionMasterViewModel;
import inc.nomard.spoty.core.views.BaseController;
import inc.nomard.spoty.core.views.previews.RequisitionPreviewController;
import inc.nomard.spoty.network_bridge.dtos.requisitions.RequisitionMaster;
import inc.nomard.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.fxmlLoader;

@SuppressWarnings("unchecked")
public class RequisitionController implements Initializable {
    private static RequisitionController instance;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXButton importBtn;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXButton createBtn;
    @FXML
    public HBox refresh;
    @FXML
    private MFXTableView<RequisitionMaster> masterTable;
    private RotateTransition transition;
    private FXMLLoader viewFxmlLoader;
    private MFXStageDialog viewDialog;

    public static RequisitionController getInstance(Stage stage) {
        if (instance == null) instance = new RequisitionController(stage);
        return instance;
    }

    public RequisitionController(Stage stage) {
        Platform.runLater(() ->
        {
            try {
                viewDialogPane(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setIcons();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<RequisitionMaster> masterSupplier =
                new MFXTableColumn<>(
                        "Supplier", false, Comparator.comparing(RequisitionMaster::getSupplierName));
        MFXTableColumn<RequisitionMaster> masterStatus =
                new MFXTableColumn<>("Status", false, Comparator.comparing(RequisitionMaster::getStatus));
        MFXTableColumn<RequisitionMaster> masterPaymentStatus =
                new MFXTableColumn<>(
                        "Pay Status", false, Comparator.comparing(RequisitionMaster::getPaymentStatus));
        MFXTableColumn<RequisitionMaster> masterDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(RequisitionMaster::getDate));
        MFXTableColumn<RequisitionMaster> masterGrandTotal =
                new MFXTableColumn<>("Total Amount", false, Comparator.comparing(RequisitionMaster::getTotal));
        MFXTableColumn<RequisitionMaster> masterAmountPaid =
                new MFXTableColumn<>("Paid Amount", false, Comparator.comparing(RequisitionMaster::getAmountPaid));
        MFXTableColumn<RequisitionMaster> masterAmountDue =
                new MFXTableColumn<>("Due Amount", false, Comparator.comparing(RequisitionMaster::getAmountDue));

        masterSupplier.setRowCellFactory(
                master -> new MFXTableRowCell<>(RequisitionMaster::getSupplierName));
        masterStatus.setRowCellFactory(
                master -> new MFXTableRowCell<>(RequisitionMaster::getStatus));
        masterPaymentStatus.setRowCellFactory(
                master -> new MFXTableRowCell<>(RequisitionMaster::getPaymentStatus));
        masterDate.setRowCellFactory(
                master -> new MFXTableRowCell<>(RequisitionMaster::getLocaleDate));
        masterGrandTotal.setRowCellFactory(
                master -> new MFXTableRowCell<>(RequisitionMaster::getTotal));
        masterAmountPaid.setRowCellFactory(
                master -> new MFXTableRowCell<>(RequisitionMaster::getAmountPaid));
        masterAmountDue.setRowCellFactory(
                master -> new MFXTableRowCell<>(RequisitionMaster::getAmountDue));

        masterSupplier
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        masterStatus
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        masterPaymentStatus
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        masterDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        masterGrandTotal
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        masterAmountPaid
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));
        masterAmountDue
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.25));

        masterTable
                .getTableColumns()
                .addAll(
                        masterSupplier,
                        masterStatus,
                        masterPaymentStatus,
                        masterDate,
                        masterGrandTotal,
                        masterAmountPaid,
                        masterAmountDue);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Reference", RequisitionMaster::getRef),
                        new StringFilter<>("Supplier", RequisitionMaster::getSupplierName),
                        new StringFilter<>("Status", RequisitionMaster::getStatus),
                        new StringFilter<>("Pay Status", RequisitionMaster::getPaymentStatus),
                        new DoubleFilter<>("Total", RequisitionMaster::getTotal),
                        new DoubleFilter<>("Paid", RequisitionMaster::getAmountPaid),
                        new DoubleFilter<>("Due", RequisitionMaster::getAmountDue));
        getTable();

        if (RequisitionMasterViewModel.getRequisitions().isEmpty()) {
            RequisitionMasterViewModel.getRequisitions()
                    .addListener(
                            (ListChangeListener<RequisitionMaster>)
                                    c -> masterTable.setItems(RequisitionMasterViewModel.getRequisitions()));
        } else {
            masterTable
                    .itemsProperty()
                    .bindBidirectional(RequisitionMasterViewModel.requisitionsProperty());
        }
    }

    private void getTable() {
        masterTable.setPrefSize(1200, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<RequisitionMaster> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<RequisitionMaster>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<RequisitionMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        MFXContextMenuItem view = new MFXContextMenuItem("View");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            RequisitionMasterViewModel.deleteItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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
                            RequisitionMasterViewModel.getRequisitionMaster(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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
        BaseController.navigation.navigate(Pages.getRequisitionMasterFormPane());
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

        refreshIcon.setOnMouseClicked(mouseEvent -> RequisitionMasterViewModel.getRequisitionMasters(this::onAction, this::onSuccess, this::onFailed));
    }

    private void viewDialogPane(Stage stage) throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/RequisitionPreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new RequisitionPreviewController());
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

    public void viewShow(RequisitionMaster requisitionMaster) {
        RequisitionPreviewController controller = viewFxmlLoader.getController();
        controller.init(requisitionMaster);
        viewDialog.showAndWait();
    }
}
