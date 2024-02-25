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

package inc.normad.spoty.core.views.product.unit_of_measure;

import inc.normad.spoty.core.viewModels.UOMViewModel;
import inc.normad.spoty.core.views.forms.UOMFormController;
import inc.normad.spoty.network_bridge.dtos.UnitOfMeasure;
import inc.normad.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.DoubleFilter;
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

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static inc.normad.spoty.core.SpotyCoreResourceLoader.fxmlLoader;

@SuppressWarnings("unchecked")
public class UnitOfMeasureController implements Initializable {
    private static UnitOfMeasureController instance;
    @FXML
    public MFXTableView<UnitOfMeasure> uomTable;
    @FXML
    public MFXTextField uomSearchBar;
    @FXML
    public HBox uomActionsPane;
    @FXML
    public MFXButton uomImportBtn;
    @FXML
    public BorderPane uomContentPane;
    private MFXStageDialog dialog;

    public UnitOfMeasureController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        uomFormDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static UnitOfMeasureController getInstance(Stage stage) {
        if (instance == null) instance = new UnitOfMeasureController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<UnitOfMeasure> uomName =
                new MFXTableColumn<>("Name", false, Comparator.comparing(UnitOfMeasure::getName));
        MFXTableColumn<UnitOfMeasure> uomShortName =
                new MFXTableColumn<>(
                        "Short Name", false, Comparator.comparing(UnitOfMeasure::getShortName));
        MFXTableColumn<UnitOfMeasure> uomBaseUnit =
                new MFXTableColumn<>(
                        "Base Unit", false, Comparator.comparing(UnitOfMeasure::getBaseUnitName));
        MFXTableColumn<UnitOfMeasure> uomOperator =
                new MFXTableColumn<>("Operator", false, Comparator.comparing(UnitOfMeasure::getOperator));
        MFXTableColumn<UnitOfMeasure> uomOperationValue =
                new MFXTableColumn<>(
                        "Operation Value", false, Comparator.comparing(UnitOfMeasure::getOperatorValue));

        uomName.setRowCellFactory(brand -> new MFXTableRowCell<>(UnitOfMeasure::getName));
        uomShortName.setRowCellFactory(brand -> new MFXTableRowCell<>(UnitOfMeasure::getShortName));
        uomBaseUnit.setRowCellFactory(brand -> new MFXTableRowCell<>(UnitOfMeasure::getBaseUnitName));
        uomOperator.setRowCellFactory(brand -> new MFXTableRowCell<>(UnitOfMeasure::getOperator));
        uomOperationValue.setRowCellFactory(
                brand -> new MFXTableRowCell<>(UnitOfMeasure::getOperatorValue));

        uomName.prefWidthProperty().bind(uomTable.widthProperty().multiply(.25));
        uomShortName.prefWidthProperty().bind(uomTable.widthProperty().multiply(.25));
        uomBaseUnit.prefWidthProperty().bind(uomTable.widthProperty().multiply(.25));
        uomOperator.prefWidthProperty().bind(uomTable.widthProperty().multiply(.2));
        uomOperationValue.prefWidthProperty().bind(uomTable.widthProperty().multiply(.15));

        uomTable
                .getTableColumns()
                .addAll(uomName, uomShortName, uomBaseUnit, uomOperator, uomOperationValue);
        uomTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", UnitOfMeasure::getName),
                        new StringFilter<>("Short Name", UnitOfMeasure::getShortName),
                        new StringFilter<>("Base Unit", UnitOfMeasure::getBaseUnitName),
                        new StringFilter<>("Operator", UnitOfMeasure::getOperator),
                        new DoubleFilter<>("Operation Value", UnitOfMeasure::getOperatorValue));
        getUnitOfMeasureTable();

        if (UOMViewModel.getUnitsOfMeasure().isEmpty()) {
            UOMViewModel.getUnitsOfMeasure()
                    .addListener(
                            (ListChangeListener<UnitOfMeasure>)
                                    c -> uomTable.setItems(UOMViewModel.getUnitsOfMeasure()));
        } else {
            uomTable.itemsProperty().bindBidirectional(UOMViewModel.unitsOfMeasureProperty());
        }
    }

    private void getUnitOfMeasureTable() {
        uomTable.setPrefSize(1000, 1000);
        uomTable.features().enableBounceEffect();
        uomTable.features().enableSmoothScrolling(0.5);

        uomTable.setTableRowFactory(
                t -> {
                    MFXTableRow<UnitOfMeasure> row = new MFXTableRow<>(uomTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<UnitOfMeasure>) event.getSource())
                                        .show(uomTable.getScene().getWindow(), event.getScreenX(), event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<UnitOfMeasure> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(uomTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            UOMViewModel.deleteItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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
                            UOMViewModel.getItem(obj.getData().getId(), this::onAction, this::onFailed);
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

    private void uomFormDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/UOMForm.fxml");
        fxmlLoader.setControllerFactory(c -> UOMFormController.getInstance());

        MFXGenericDialog dialogContent = fxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(uomContentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void uomCreateBtnClicked() {
        dialog.showAndWait();
    }

    private void onAction() {
        System.out.println("Loading unit of measure...");
    }

    private void onSuccess() {
        System.out.println("Loaded unit of measure...");
    }

    private void onFailed() {
        System.out.println("failed loading unit of measure...");
    }
}
