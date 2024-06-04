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

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
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
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class ExpenseCategoryController implements Initializable {
    private static ExpenseCategoryController instance;
    @FXML
    public HBox actionsPane;
    @FXML
    public BorderPane categoryContentPane;
    @FXML
    public MFXTableView<ExpenseCategory> categoryExpenseTable;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox refresh;
    @FXML
    public MFXProgressSpinner progress;
    @FXML
    public HBox leftHeaderPane;
    private MFXStageDialog dialog;

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
        setSearchBar();
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
                    ExpenseCategoryViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
                    e.consume();
                });
        // Edit
        edit.setOnAction(
                e -> {
                    ExpenseCategoryViewModel.getItem(obj.getData().getId(), () -> dialog.showAndWait(), this::errorMessage);
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

    public void createBtnClicked() {
        dialog.showAndWait();
    }

    private void onSuccess() {
        ExpenseCategoryViewModel.getAllCategories(null, null);
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
                ExpenseCategoryViewModel.getAllCategories(null, null);
            }
            progress.setVisible(true);
            ExpenseCategoryViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
            }, this::errorMessage);
        });
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
