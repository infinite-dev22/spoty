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

package inc.nomard.spoty.core.views.expenses.category;

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

@SuppressWarnings("unchecked")
public class ExpenseCategoryController implements Initializable {
    private static ExpenseCategoryController instance;
    @FXML
    public HBox categoryExpenseActionsPane;
    @FXML
    public BorderPane categoryContentPane;
    @FXML
    public MFXTableView<ExpenseCategory> categoryExpenseTable;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox refresh;
    private MFXStageDialog dialog;
    private RotateTransition transition;

    private ExpenseCategoryController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        expenseCategoryFormDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static ExpenseCategoryController getInstance(Stage stage) {
        if (instance == null) instance = new ExpenseCategoryController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setIcons();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<ExpenseCategory> categoryName =
                new MFXTableColumn<>("Name", true, Comparator.comparing(ExpenseCategory::getName));
        MFXTableColumn<ExpenseCategory> categoryDescription =
                new MFXTableColumn<>(
                        "Description", true, Comparator.comparing(ExpenseCategory::getDescription));

        categoryName.setRowCellFactory(category -> new MFXTableRowCell<>(ExpenseCategory::getName));
        categoryDescription.setRowCellFactory(
                category -> new MFXTableRowCell<>(ExpenseCategory::getDescription));

        categoryName.prefWidthProperty().bind(categoryExpenseTable.widthProperty().multiply(.5));
        categoryDescription.prefWidthProperty().bind(categoryExpenseTable.widthProperty().multiply(.5));

        categoryName.setColumnResizable(false);
        categoryDescription.setColumnResizable(false);

        categoryExpenseTable.getTableColumns().addAll(categoryName, categoryDescription);
        categoryExpenseTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", ExpenseCategory::getName),
                        new StringFilter<>("Description", ExpenseCategory::getDescription));

        styleExpenseCategoryTable();

        if (ExpenseCategoryViewModel.getCategories().isEmpty()) {
            ExpenseCategoryViewModel.getCategories().addListener(
                    (ListChangeListener<ExpenseCategory>)
                            c -> categoryExpenseTable.setItems(ExpenseCategoryViewModel.getCategories()));
        } else {
            categoryExpenseTable
                    .itemsProperty()
                    .bindBidirectional(ExpenseCategoryViewModel.categoriesProperty());
        }
    }

    private void styleExpenseCategoryTable() {
        categoryExpenseTable.setPrefSize(1200, 1000);
        categoryExpenseTable.features().enableBounceEffect();
        categoryExpenseTable.features().enableSmoothScrolling(0.5);

        categoryExpenseTable.setTableRowFactory(
                t -> {
                    MFXTableRow<ExpenseCategory> row = new MFXTableRow<>(categoryExpenseTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<ExpenseCategory>) event.getSource())
                                        .show(
                                                categoryExpenseTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<ExpenseCategory> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(categoryExpenseTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            ExpenseCategoryViewModel.deleteItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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
                            ExpenseCategoryViewModel.getItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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

    private void expenseCategoryFormDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/ExpenseCategoryForm.fxml");
        fxmlLoader.setControllerFactory(c -> ExpenseCategoryFormController.getInstance());

        MFXGenericDialog dialogContent = fxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);
        dialogContent.setShowClose(false);

        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(categoryContentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void categoryExpenseCreateBtnClicked() {
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

        refreshIcon.setOnMouseClicked(mouseEvent -> ExpenseCategoryViewModel.getAllCategories(this::onAction, this::onSuccess, this::onFailed));
    }
}
