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

package org.infinite.spoty.views.suppliers;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.infinite.spoty.data_source.dtos.Supplier;
import org.infinite.spoty.forms.SupplierFormController;
import org.infinite.spoty.utils.SpotyThreader;
import org.infinite.spoty.viewModels.SupplierViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotyResourceLoader.fxmlLoader;

@SuppressWarnings("unchecked")
public class SupplierController implements Initializable {
    private static SupplierController instance;
    @FXML
    public MFXTextField supplierSearchBar;
    @FXML
    public HBox supplierActionsPane;
    @FXML
    public MFXButton supplierImportBtn;
    @FXML
    public MFXTableView<Supplier> suppliersTable;
    @FXML
    public BorderPane suppliersContentPane;
    private MFXStageDialog dialog;

    private SupplierController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        supplierFormDialogPane(stage);
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
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Supplier> supplierCode =
                new MFXTableColumn<>("Code", false, Comparator.comparing(Supplier::getCode));
        MFXTableColumn<Supplier> supplierName =
                new MFXTableColumn<>("Name", false, Comparator.comparing(Supplier::getName));
        MFXTableColumn<Supplier> supplierPhone =
                new MFXTableColumn<>("Phone", false, Comparator.comparing(Supplier::getPhone));
        MFXTableColumn<Supplier> supplierEmail =
                new MFXTableColumn<>("Email", false, Comparator.comparing(Supplier::getEmail));
        MFXTableColumn<Supplier> supplierTax =
                new MFXTableColumn<>("Tax No.", false, Comparator.comparing(Supplier::getTaxNumber));

        supplierCode.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getCode));
        supplierName.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getName));
        supplierPhone.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getPhone));
        supplierEmail.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getEmail));
        supplierTax.setRowCellFactory(supplier -> new MFXTableRowCell<>(Supplier::getTaxNumber));

        supplierCode.prefWidthProperty().bind(suppliersTable.widthProperty().multiply(.1));
        supplierName.prefWidthProperty().bind(suppliersTable.widthProperty().multiply(.3));
        supplierPhone.prefWidthProperty().bind(suppliersTable.widthProperty().multiply(.2));
        supplierEmail.prefWidthProperty().bind(suppliersTable.widthProperty().multiply(.2));
        supplierTax.prefWidthProperty().bind(suppliersTable.widthProperty().multiply(.2));

        suppliersTable
                .getTableColumns()
                .addAll(supplierCode, supplierName, supplierPhone, supplierEmail, supplierTax);
        suppliersTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Code", Supplier::getCode),
                        new StringFilter<>("Name", Supplier::getName),
                        new StringFilter<>("Phone", Supplier::getPhone),
                        new StringFilter<>("Email", Supplier::getEmail),
                        new StringFilter<>("Tax No.", Supplier::getTaxNumber));
        getTable();

        if (SupplierViewModel.getSuppliers().isEmpty()) {
            SupplierViewModel.getSuppliers()
                    .addListener(
                            (ListChangeListener<Supplier>)
                                    c -> suppliersTable.setItems(SupplierViewModel.getSuppliers()));
        } else {
            suppliersTable.itemsProperty().bindBidirectional(SupplierViewModel.suppliersProperty());
        }
    }

    private void getTable() {
        suppliersTable.setPrefSize(1000, 1000);
        suppliersTable.features().enableBounceEffect();
        suppliersTable.features().enableSmoothScrolling(0.5);

        suppliersTable.setTableRowFactory(
                t -> {
                    MFXTableRow<Supplier> row = new MFXTableRow<>(suppliersTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<Supplier>) event.getSource())
                                        .show(
                                                suppliersTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<Supplier> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(suppliersTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            SupplierViewModel.deleteItem(obj.getData().getId());
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
                            SupplierViewModel.getItem(obj.getData().getId());
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

    private void supplierFormDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/SupplierForm.fxml");
        fxmlLoader.setControllerFactory(c -> SupplierFormController.getInstance());

        MFXGenericDialog dialogContent = fxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(suppliersContentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void supplierCreateBtnClicked() {
        dialog.showAndWait();
    }
}
