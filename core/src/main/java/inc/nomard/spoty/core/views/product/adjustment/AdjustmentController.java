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

package inc.nomard.spoty.core.views.product.adjustment;

import inc.nomard.spoty.core.components.animations.SpotyAnimations;
import inc.nomard.spoty.core.components.navigation.Pages;
import inc.nomard.spoty.core.viewModels.adjustments.AdjustmentMasterViewModel;
import inc.nomard.spoty.core.views.BaseController;
import inc.nomard.spoty.network_bridge.dtos.adjustments.AdjustmentMaster;
import inc.nomard.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

@SuppressWarnings("unchecked")
public class AdjustmentController implements Initializable {
    private static AdjustmentController instance;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXButton importBtn;
    @FXML
    public MFXButton createBtn;
    @FXML
    public HBox refresh;
    @FXML
    private MFXTableView<AdjustmentMaster> masterTable;
    private RotateTransition transition;

    public static AdjustmentController getInstance() {
        if (instance == null) instance = new AdjustmentController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setIcons();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<AdjustmentMaster> adjustmentDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(AdjustmentMaster::getDate));
        MFXTableColumn<AdjustmentMaster> adjustmentStatus =
                new MFXTableColumn<>("Status", false, Comparator.comparing(AdjustmentMaster::getStatus));
        MFXTableColumn<AdjustmentMaster> adjustmentTotalAmount =
                new MFXTableColumn<>(
                        "Total Amount", false, Comparator.comparing(AdjustmentMaster::getTotal));

        adjustmentDate.setRowCellFactory(
                adjustment -> new MFXTableRowCell<>(AdjustmentMaster::getLocaleDate));
        adjustmentStatus.setRowCellFactory(
                adjustment -> new MFXTableRowCell<>(AdjustmentMaster::getStatus));
        adjustmentTotalAmount.setRowCellFactory(
                adjustment -> new MFXTableRowCell<>(AdjustmentMaster::getTotal));

        adjustmentDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.5));
        adjustmentStatus.prefWidthProperty().bind(masterTable.widthProperty().multiply(.5));
        adjustmentTotalAmount
                .prefWidthProperty()
                .bind(masterTable.widthProperty().multiply(.5));

        masterTable
                .getTableColumns()
                .addAll(adjustmentDate, adjustmentStatus, adjustmentTotalAmount);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Reference", AdjustmentMaster::getRef),
                        new StringFilter<>("Status", AdjustmentMaster::getStatus),
                        new DoubleFilter<>("Total Amount", AdjustmentMaster::getTotal));
        getAdjustmentMasterTable();

        if (AdjustmentMasterViewModel.getAdjustmentMasters().isEmpty()) {
            AdjustmentMasterViewModel.getAdjustmentMasters()
                    .addListener(
                            (ListChangeListener<AdjustmentMaster>)
                                    c ->
                                            masterTable.setItems(
                                                    AdjustmentMasterViewModel.getAdjustmentMasters()));
        } else {
            masterTable
                    .itemsProperty()
                    .bindBidirectional(AdjustmentMasterViewModel.adjustmentMastersProperty());
        }
    }

    private void getAdjustmentMasterTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<AdjustmentMaster> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<AdjustmentMaster>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<AdjustmentMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    AdjustmentMasterViewModel.deleteItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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
                                    AdjustmentMasterViewModel.getAdjustmentMaster(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }
                            });

                    createBtnClicked();
                    e.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    public void createBtnClicked() {
        BaseController.navigation.navigate(Pages.getAdjustmentMasterFormPane());
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

        refreshIcon.setOnMouseClicked(mouseEvent -> AdjustmentMasterViewModel.getAllAdjustmentMasters(this::onAction, this::onSuccess, this::onFailed));
    }
}