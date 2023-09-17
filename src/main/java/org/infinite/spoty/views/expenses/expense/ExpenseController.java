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

package org.infinite.spoty.views.expenses.expense;

import static org.infinite.spoty.SpotyResourceLoader.fxmlLoader;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.ResourceBundle;

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
import org.infinite.spoty.GlobalActions;
import org.infinite.spoty.database.models.Expense;
import org.infinite.spoty.forms.ExpenseFormController;
import org.infinite.spoty.viewModels.ExpenseViewModel;

@SuppressWarnings("unchecked")
public class ExpenseController implements Initializable {
    private static ExpenseController instance;
    @FXML
    public MFXTextField expenseSearchBar;
    @FXML
    public HBox expenseActionsPane;
    @FXML
    public MFXButton expenseImportBtn;
    @FXML
    public MFXTableView<Expense> expenseTable;
    @FXML
    public BorderPane expenseContentPane;
    private MFXStageDialog dialog;

    private ExpenseController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        expenseFormDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static ExpenseController getInstance(Stage stage) {
        if (instance == null) instance = new ExpenseController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Expense> expenseDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(Expense::getDate));
        MFXTableColumn<Expense> expenseName =
                new MFXTableColumn<>("Name", false, Comparator.comparing(Expense::getName));
        MFXTableColumn<Expense> expenseAmount =
                new MFXTableColumn<>("Amount", false, Comparator.comparing(Expense::getAmount));
        MFXTableColumn<Expense> expenseCategory =
                new MFXTableColumn<>(
                        "Category", false, Comparator.comparing(Expense::getExpenseCategoryName));
        MFXTableColumn<Expense> expenseBranch =
                new MFXTableColumn<>("Branch", false, Comparator.comparing(Expense::getBranchName));

        expenseDate.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getLocaleDate));
        expenseName.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getName));
        expenseAmount.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getAmount));
        expenseCategory.setRowCellFactory(
                expense -> new MFXTableRowCell<>(Expense::getExpenseCategoryName));
        expenseBranch.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getBranchName));

        expenseDate.prefWidthProperty().bind(expenseTable.widthProperty().multiply(.25));
        expenseName.prefWidthProperty().bind(expenseTable.widthProperty().multiply(.25));
        expenseAmount.prefWidthProperty().bind(expenseTable.widthProperty().multiply(.25));
        expenseCategory.prefWidthProperty().bind(expenseTable.widthProperty().multiply(.25));
        expenseBranch.prefWidthProperty().bind(expenseTable.widthProperty().multiply(.25));

        expenseTable
                .getTableColumns()
                .addAll(expenseName, expenseCategory, expenseBranch, expenseDate, expenseAmount);
        expenseTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Reference No.", Expense::getRef),
                        new StringFilter<>("Name", Expense::getName),
                        new DoubleFilter<>("Amount", Expense::getAmount),
                        new StringFilter<>("Category", Expense::getExpenseCategoryName),
                        new StringFilter<>("Branch", Expense::getBranchName));
        styleExpenseTable();

        if (ExpenseViewModel.getExpenses().isEmpty()) {
            ExpenseViewModel.getExpenses()
                    .addListener(
                            (ListChangeListener<Expense>)
                                    c -> expenseTable.setItems(ExpenseViewModel.getExpenses()));
        } else {
            expenseTable.itemsProperty().bindBidirectional(ExpenseViewModel.expensesProperty());
        }
    }

    private void styleExpenseTable() {
        expenseTable.setPrefSize(1200, 1000);
        expenseTable.features().enableBounceEffect();
        expenseTable.features().enableSmoothScrolling(0.5);

        expenseTable.setTableRowFactory(
                t -> {
                    MFXTableRow<Expense> row = new MFXTableRow<>(expenseTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<Expense>) event.getSource())
                                        .show(
                                                expenseTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<Expense> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(expenseTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    GlobalActions.spotyThreadPool().execute(() -> {
                        try {
                            ExpenseViewModel.deleteItem(obj.getData().getId());
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    e.consume();
                });
        // Edit
        edit.setOnAction(
                e -> {
                    GlobalActions.spotyThreadPool().execute(() -> {
                        try {
                            ExpenseViewModel.getItem(obj.getData().getId());
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    dialog.showAndWait();
                    e.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    private void expenseFormDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("forms/ExpenseForm.fxml");
        fxmlLoader.setControllerFactory(c -> ExpenseFormController.getInstance());

        MFXGenericDialog dialogContent = fxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(expenseContentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void expenseCreateBtnClicked() {
        dialog.showAndWait();
    }
}
