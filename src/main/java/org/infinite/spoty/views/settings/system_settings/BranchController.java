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

package org.infinite.spoty.views.settings.system_settings;

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
import org.infinite.spoty.data_source.dtos.Branch;
import org.infinite.spoty.forms.BranchFormController;
import org.infinite.spoty.utils.SpotyThreader;
import org.infinite.spoty.values.strings.Labels;
import org.infinite.spoty.viewModels.BranchViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotyResourceLoader.fxmlLoader;

@SuppressWarnings("unchecked")
public class BranchController implements Initializable {
    private static BranchController instance;
    @FXML
    public MFXTextField branchSearchBar;
    @FXML
    public HBox branchActionsPane;
    @FXML
    public MFXButton branchImportBtn;
    @FXML
    public MFXTableView<Branch> branchTable;
    @FXML
    public BorderPane branchContentPane;
    private MFXStageDialog dialog;

    private BranchController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        branchFormDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static BranchController getInstance(Stage stage) {
        if (instance == null) instance = new BranchController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    public void setupTable() {
        // TODO: Create ZipCode and Country Columns.
        MFXTableColumn<Branch> branchName =
                new MFXTableColumn<>("Name", false, Comparator.comparing(Branch::getName));
        MFXTableColumn<Branch> branchPhone =
                new MFXTableColumn<>("Phone", false, Comparator.comparing(Branch::getPhone));
        MFXTableColumn<Branch> branchCity =
                new MFXTableColumn<>("City", false, Comparator.comparing(Branch::getCity));
        MFXTableColumn<Branch> branchTown =
                new MFXTableColumn<>("Town", false, Comparator.comparing(Branch::getTown));
        MFXTableColumn<Branch> branchEmail =
                new MFXTableColumn<>("Email", false, Comparator.comparing(Branch::getEmail));

        branchName.setRowCellFactory(branch -> new MFXTableRowCell<>(Branch::getName));
        branchPhone.setRowCellFactory(branch -> new MFXTableRowCell<>(Branch::getPhone));
        branchCity.setRowCellFactory(branch -> new MFXTableRowCell<>(Branch::getCity));
        branchTown.setRowCellFactory(branch -> new MFXTableRowCell<>(Branch::getTown));
        branchEmail.setRowCellFactory(branch -> new MFXTableRowCell<>(Branch::getEmail));

        branchName.prefWidthProperty().bind(branchTable.widthProperty().multiply(.2));
        branchPhone.prefWidthProperty().bind(branchTable.widthProperty().multiply(.18));
        branchCity.prefWidthProperty().bind(branchTable.widthProperty().multiply(.18));
        branchTown.prefWidthProperty().bind(branchTable.widthProperty().multiply(.2));
        branchEmail.prefWidthProperty().bind(branchTable.widthProperty().multiply(.2));

        branchTable
                .getTableColumns()
                .addAll(branchName, branchPhone, branchCity, branchTown, branchEmail);

        branchTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", Branch::getName),
                        new StringFilter<>("Phone", Branch::getPhone),
                        new StringFilter<>("City", Branch::getCity),
                        new StringFilter<>("Town", Branch::getTown),
                        new StringFilter<>("Email", Branch::getEmail));

        getBranchTable();

        if (BranchViewModel.getBranches().isEmpty()) {
            BranchViewModel.getBranches()
                    .addListener(
                            (ListChangeListener<Branch>)
                                    c -> branchTable.setItems(BranchViewModel.getBranches()));
        } else {
            branchTable.itemsProperty().bindBidirectional(BranchViewModel.branchesProperty());
        }
    }

    private void getBranchTable() {
        branchTable.setPrefSize(1200, 1000);
        branchTable.features().enableBounceEffect();
        branchTable.features().enableSmoothScrolling(0.5);

        branchTable.setTableRowFactory(
                t -> {
                    MFXTableRow<Branch> row = new MFXTableRow<>(branchTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event ->
                                    showContextMenu((MFXTableRow<Branch>) event.getSource())
                                            .show(
                                                    branchContentPane.getScene().getWindow(),
                                                    event.getScreenX(),
                                                    event.getScreenY());
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<Branch> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(branchTable);

        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            BranchViewModel.deleteItem(obj.getData().getId());
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
                            BranchViewModel.getItem(obj.getData().getId());
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    branchCreateBtnClicked();
                    e.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    @FXML
    private void branchCreateBtnClicked() {
        BranchViewModel.setTitle(Labels.CREATE);
        dialog.showAndWait();
    }

    private void branchFormDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("forms/BranchForm.fxml");
        fxmlLoader.setControllerFactory(c -> BranchFormController.getInstance());

        MFXGenericDialog dialogContent = fxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(branchContentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }
}
