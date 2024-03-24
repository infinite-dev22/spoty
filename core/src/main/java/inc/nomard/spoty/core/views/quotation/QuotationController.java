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

package inc.nomard.spoty.core.views.quotation;

import inc.nomard.spoty.core.components.animations.SpotyAnimations;
import inc.nomard.spoty.core.components.navigation.Pages;
import inc.nomard.spoty.core.viewModels.quotations.QuotationMasterViewModel;
import inc.nomard.spoty.core.views.BaseController;
import inc.nomard.spoty.core.views.previews.QuotationPreviewController;
import inc.nomard.spoty.network_bridge.dtos.quotations.QuotationMaster;
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
public class QuotationController implements Initializable {
    private static QuotationController instance;
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
    private MFXTableView<QuotationMaster> masterTable;
    private RotateTransition transition;
    private FXMLLoader viewFxmlLoader;
    private MFXStageDialog viewDialog;

    public static QuotationController getInstance(Stage stage) {
        if (instance == null) instance = new QuotationController(stage);
        return instance;
    }

    public QuotationController(Stage stage) {
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
        MFXTableColumn<QuotationMaster> quotationCustomer =
                new MFXTableColumn<>(
                        "Customer", false, Comparator.comparing(QuotationMaster::getCustomerName));
        MFXTableColumn<QuotationMaster> quotationStatus =
                new MFXTableColumn<>("Status", false, Comparator.comparing(QuotationMaster::getStatus));
        MFXTableColumn<QuotationMaster> quotationDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(QuotationMaster::getDate));
        MFXTableColumn<QuotationMaster> quotationTotalAmount =
                new MFXTableColumn<>(
                        "Total Amount", false, Comparator.comparing(QuotationMaster::getTotal));

        quotationCustomer.setRowCellFactory(
                quotation -> new MFXTableRowCell<>(QuotationMaster::getCustomerName));
        quotationStatus.setRowCellFactory(
                quotation -> new MFXTableRowCell<>(QuotationMaster::getStatus));
        quotationDate.setRowCellFactory(
                quotation -> new MFXTableRowCell<>(QuotationMaster::getLocaleDate));
        quotationTotalAmount.setRowCellFactory(
                quotation -> new MFXTableRowCell<>(QuotationMaster::getTotal));

        quotationCustomer.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        quotationStatus.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        quotationDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        quotationTotalAmount.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));

        masterTable
                .getTableColumns()
                .addAll(
                        quotationCustomer,
                        quotationStatus,
                        quotationDate,
                        quotationTotalAmount);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Reference", QuotationMaster::getRef),
                        new StringFilter<>("Customer", QuotationMaster::getCustomerName),
                        new StringFilter<>("Status", QuotationMaster::getStatus),
                        new DoubleFilter<>("Grand Total", QuotationMaster::getTotal));
        getQuotationMasterTable();

        if (QuotationMasterViewModel.getQuotations().isEmpty()) {
            QuotationMasterViewModel.getQuotations()
                    .addListener(
                            (ListChangeListener<QuotationMaster>)
                                    c -> masterTable.setItems(QuotationMasterViewModel.getQuotations()));
        } else {
            masterTable
                    .itemsProperty()
                    .bindBidirectional(QuotationMasterViewModel.quotationsProperty());
        }
    }

    private void getQuotationMasterTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<QuotationMaster> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<QuotationMaster>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<QuotationMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem view = new MFXContextMenuItem("View");
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            QuotationMasterViewModel.deleteItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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
                            QuotationMasterViewModel.getQuotationMaster(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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
        BaseController.navigation.navigate(Pages.getQuotationMasterFormPane());
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

        refreshIcon.setOnMouseClicked(mouseEvent -> QuotationMasterViewModel.getAllQuotationMasters(this::onAction, this::onSuccess, this::onFailed));
    }

    private void viewDialogPane(Stage stage) throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/QuotationPreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new QuotationPreviewController());
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

    public void viewShow(QuotationMaster quotationMaster) {
        QuotationPreviewController controller = viewFxmlLoader.getController();
        controller.init(quotationMaster);
        viewDialog.showAndWait();
    }
}
