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
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.core.views.previews.*;
import inc.nomard.spoty.network_bridge.dtos.*;
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
public class SupplierController implements Initializable {
    private static SupplierController instance;
    private final Stage stage;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane,
            leftHeaderPane,
            customerActionsPane;
    @FXML
    public MFXTableView<Supplier> masterTable;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXButton createBtn;
    @FXML
    public MFXProgressSpinner progress;
    private MFXStageDialog dialog;
    private FXMLLoader viewFxmlLoader;
    private MFXStageDialog viewDialog;

    private SupplierController(Stage stage) {
        this.stage = stage;
        Platform.runLater(
                () -> {
                    try {
                        formDialogPane(stage);
                        viewDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static SupplierController getInstance(Stage stage) {
        if (instance == null) instance = new SupplierController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setIcons();
        setSearchBar();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Supplier> supplierName =
                new MFXTableColumn<>("Name", false, Comparator.comparing(Supplier::getName));
        MFXTableColumn<Supplier> supplierPhone =
                new MFXTableColumn<>("Phone", false, Comparator.comparing(Supplier::getPhone));
        MFXTableColumn<Supplier> supplierEmail =
                new MFXTableColumn<>("Email", false, Comparator.comparing(Supplier::getEmail));
        MFXTableColumn<Supplier> supplierTax =
                new MFXTableColumn<>("Tax No.", false, Comparator.comparing(Supplier::getTaxNumber));

        supplierName.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getName));
        supplierPhone.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getPhone));
        supplierEmail.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getEmail));
        supplierTax.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getTaxNumber));

        supplierName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.3));
        supplierPhone.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));
        supplierEmail.prefWidthProperty().bind(masterTable.widthProperty().multiply(.3));
        supplierTax.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));

        masterTable
                .getTableColumns()
                .addAll(supplierName, supplierPhone, supplierEmail, supplierTax);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", Supplier::getName),
                        new StringFilter<>("Phone", Supplier::getPhone),
                        new StringFilter<>("Email", Supplier::getEmail),
                        new StringFilter<>("Tax No.", Supplier::getTaxNumber));
        styleTable();

        if (SupplierViewModel.getSuppliers().isEmpty()) {
            SupplierViewModel.getSuppliers()
                    .addListener(
                            (ListChangeListener<Supplier>)
                                    c -> masterTable.setItems(SupplierViewModel.getSuppliers()));
        } else {
            masterTable.itemsProperty().bindBidirectional(SupplierViewModel.suppliersProperty());
        }
    }

    private void styleTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<Supplier> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<Supplier>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<Supplier> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        MFXContextMenuItem view = new MFXContextMenuItem("View");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            SupplierViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getData().getName(), stage, contentPane));
        // Edit
        edit.setOnAction(
                e -> {
                    SupplierViewModel.getItem(obj.getData().getId(), () -> dialog.showAndWait(), this::errorMessage);
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

    private void formDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/SupplierForm.fxml");
        fxmlLoader.setControllerFactory(c -> SupplierFormController.getInstance(stage));

        MFXGenericDialog dialogContent = fxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);
        dialogContent.setShowClose(false);

        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(contentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void createBtnClicked() {
        dialog.showAndWait();
    }

    private void onSuccess() {
        SupplierViewModel.getAllSuppliers(null, null);
    }

    private void viewDialogPane(Stage stage) throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/SupplierPreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new SupplierPreviewController());
        MFXGenericDialog genericDialog = viewFxmlLoader.load();
        genericDialog.setShowMinimize(false);
        genericDialog.setShowAlwaysOnTop(false);
        genericDialog.setHeaderText("Supplier Details Preview");

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

    public void viewShow(Supplier supplier) {
        SupplierPreviewController controller = viewFxmlLoader.getController();
        controller.init(supplier);
        viewDialog.showAndWait();
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
                SupplierViewModel.getAllSuppliers(null, null);
            }
            progress.setVisible(true);
            SupplierViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
            }, this::errorMessage);
        });
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
}
