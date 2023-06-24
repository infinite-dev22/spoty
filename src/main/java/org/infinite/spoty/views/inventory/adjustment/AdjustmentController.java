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

package org.infinite.spoty.views.inventory.adjustment;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.infinite.spoty.database.dao.AdjustmentMasterDao;
import org.infinite.spoty.database.models.AdjustmentMaster;
import org.infinite.spoty.forms.AdjustmentMasterFormController;
import org.infinite.spoty.viewModels.AdjustmentMasterViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

@SuppressWarnings("unchecked")
public class AdjustmentController implements Initializable {
    private static AdjustmentController instance;
    private final Stage stage;
    @FXML
    public BorderPane adjustmentContentPane;
    @FXML
    public MFXTextField adjustmentSearchBar;
    @FXML
    public HBox adjustmentActionsPane;
    @FXML
    public MFXButton adjustmentImportBtn;
    @FXML
    private MFXTableView<AdjustmentMaster> adjustmentMasterTable;

    private AdjustmentController(Stage stage) {
        this.stage = stage;
    }

    public static AdjustmentController getInstance(Stage stage) {
        if (instance == null)
            instance = new AdjustmentController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<AdjustmentMaster> adjustmentDate = new MFXTableColumn<>("Date", false, Comparator.comparing(AdjustmentMaster::getDate));
        MFXTableColumn<AdjustmentMaster> adjustmentReference = new MFXTableColumn<>("Reference", false, Comparator.comparing(AdjustmentMaster::getRef));
        MFXTableColumn<AdjustmentMaster> adjustmentBranch = new MFXTableColumn<>("Branch", false, Comparator.comparing(AdjustmentMaster::getBranchName));

        adjustmentDate.setRowCellFactory(adjustment -> new MFXTableRowCell<>(AdjustmentMaster::getLocaleDate));
        adjustmentReference.setRowCellFactory(adjustment -> new MFXTableRowCell<>(AdjustmentMaster::getRef));
        adjustmentBranch.setRowCellFactory(adjustment -> new MFXTableRowCell<>(AdjustmentMaster::getBranchName));

        adjustmentDate.prefWidthProperty().bind(adjustmentMasterTable.widthProperty().multiply(.4));
        adjustmentReference.prefWidthProperty().bind(adjustmentMasterTable.widthProperty().multiply(.4));
        adjustmentBranch.prefWidthProperty().bind(adjustmentMasterTable.widthProperty().multiply(.4));

        adjustmentMasterTable.getTableColumns().addAll(adjustmentDate, adjustmentReference, adjustmentBranch);
        adjustmentMasterTable.getFilters().addAll(
                new StringFilter<>("Reference", AdjustmentMaster::getRef),
                new StringFilter<>("Branch", AdjustmentMaster::getBranchName)
        );
        getAdjustmentMasterTable();
        adjustmentMasterTable.setItems(AdjustmentMasterViewModel.getAdjustmentMasters());
    }

    private void getAdjustmentMasterTable() {
        adjustmentMasterTable.setPrefSize(1000, 1000);
        adjustmentMasterTable.features().enableBounceEffect();
        adjustmentMasterTable.features().enableSmoothScrolling(0.5);

        adjustmentMasterTable.setTableRowFactory(t -> {
            MFXTableRow<AdjustmentMaster> row = new MFXTableRow<>(adjustmentMasterTable, t);
            EventHandler<ContextMenuEvent> eventHandler = event -> {
                showContextMenu((MFXTableRow<AdjustmentMaster>) event.getSource())
                        .show(adjustmentMasterTable.getScene().getWindow(), event.getScreenX(), event.getScreenY());
                event.consume();
            };
            row.setOnContextMenuRequested(eventHandler);
            return row;
        });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<AdjustmentMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(adjustmentMasterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(e -> {
            AdjustmentMasterDao.deleteAdjustmentMaster(obj.getData().getId());
            AdjustmentMasterViewModel.getAdjustmentMasters();
            e.consume();
        });
        // Edit
        edit.setOnAction(e -> {
            AdjustmentMasterViewModel.getItem(obj.getData().getId());
            adjustmentCreateBtnClicked();
            e.consume();
        });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    public void adjustmentCreateBtnClicked() {
        FXMLLoader loader = fxmlLoader("forms/AdjustmentMasterForm.fxml");
        loader.setControllerFactory(c -> new AdjustmentMasterFormController(stage));

        try {
            BorderPane productFormPane = loader.load();
            ((StackPane) adjustmentContentPane.getParent()).getChildren().add(productFormPane);
            ((StackPane) adjustmentContentPane.getParent()).getChildren().get(0).setVisible(false);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
