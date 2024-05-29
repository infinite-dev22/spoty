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

package inc.nomard.spoty.core.views.inventory.unit_of_measure;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.animations.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxresources.fonts.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javafx.animation.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.slf4j .*;

@SuppressWarnings("unchecked")
@Slf4j
public class UnitOfMeasureController implements Initializable {
    private static UnitOfMeasureController instance;
    @FXML
    public MFXTableView<UnitOfMeasure> masterTable;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXButton createBtn;
    @FXML
    public HBox refresh;
    private MFXStageDialog dialog;
    private RotateTransition transition;

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
        setIcons();
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

        uomName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        uomShortName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        uomBaseUnit.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        uomOperator.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));
        uomOperationValue.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));

        masterTable
                .getTableColumns()
                .addAll(uomName, uomShortName, uomBaseUnit, uomOperator, uomOperationValue);
        masterTable
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
                                    c -> masterTable.setItems(UOMViewModel.getUnitsOfMeasure()));
        } else {
            masterTable.itemsProperty().bindBidirectional(UOMViewModel.unitsOfMeasureProperty());
        }
    }

    private void getUnitOfMeasureTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<UnitOfMeasure> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<UnitOfMeasure>) event.getSource())
                                        .show(masterTable.getScene().getWindow(), event.getScreenX(), event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<UnitOfMeasure> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
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
                            UOMViewModel.getItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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

        refreshIcon.setOnMouseClicked(mouseEvent -> UOMViewModel.getAllUOMs(this::onAction, this::onSuccess, this::onFailed));
    }
}
