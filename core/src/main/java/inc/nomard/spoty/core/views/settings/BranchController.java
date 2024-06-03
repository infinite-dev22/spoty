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

package inc.nomard.spoty.core.views.settings;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
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
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class BranchController implements Initializable {
    private static BranchController instance;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXTableView<Branch> masterTable;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXButton createBtn;
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

        branchName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));
        branchPhone.prefWidthProperty().bind(masterTable.widthProperty().multiply(.18));
        branchCity.prefWidthProperty().bind(masterTable.widthProperty().multiply(.18));
        branchTown.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));
        branchEmail.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));

        masterTable
                .getTableColumns()
                .addAll(branchName, branchPhone, branchCity, branchTown, branchEmail);

        masterTable
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
                                    c -> masterTable.setItems(BranchViewModel.getBranches()));
        } else {
            masterTable.itemsProperty().bindBidirectional(BranchViewModel.branchesProperty());
        }
    }

    private void getBranchTable() {
        masterTable.setPrefSize(1200, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<Branch> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event ->
                                    showContextMenu((MFXTableRow<Branch>) event.getSource())
                                            .show(
                                                    contentPane.getScene().getWindow(),
                                                    event.getScreenX(),
                                                    event.getScreenY());
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<Branch> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    BranchViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
                    e.consume();
                });
        // Edit
        edit.setOnAction(
                e -> {
                    BranchViewModel.getItem(obj.getData().getId(), this::createBtnClicked, this::errorMessage);
                    e.consume();
                });
        contextMenu.addItems(edit, delete);
        return contextMenu;
    }

    @FXML
    private void createBtnClicked() {
        BranchViewModel.setTitle(Labels.CREATE);
        dialog.showAndWait();
    }

    private void branchFormDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/BranchForm.fxml");
        fxmlLoader.setControllerFactory(c -> BranchFormController.getInstance());
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

    private void onSuccess() {
        BranchViewModel.getAllBranches(null, null);
    }

    private void successMessage(String message) {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        AnchorPane.setRightAnchor(notification, 40.0);
        AnchorPane.setTopAnchor(notification, 10.0);
    }

    private void errorMessage(String message) {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
        notificationHolder.addMessage(notification);
        AnchorPane.setRightAnchor(notification, 40.0);
        AnchorPane.setTopAnchor(notification, 10.0);
    }
}
