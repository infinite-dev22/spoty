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

package org.infinite.spoty.views.expenses.category;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.StringFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.database.dao.ExpenseCategoryDao;
import org.infinite.spoty.database.models.ExpenseCategory;
import org.infinite.spoty.viewModels.ExpenseCategoryViewModel;

@SuppressWarnings("unchecked")
public class ExpenseCategoryController implements Initializable {
  private static ExpenseCategoryController instance;
  @FXML public MFXTextField categoryExpenseSearchBar;
  @FXML public HBox categoryExpenseActionsPane;
  @FXML public MFXButton categoryExpenseImportBtn;
  @FXML public BorderPane categoryContentPane;
  @FXML private MFXTableView<ExpenseCategory> categoryExpenseTable;
  private Dialog<ButtonType> dialog;

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

    categoryExpenseTable.getTableColumns().addAll(categoryName, categoryDescription);
    categoryExpenseTable
        .getFilters()
        .addAll(
            new StringFilter<>("Name", ExpenseCategory::getName),
            new StringFilter<>("Description", ExpenseCategory::getDescription));

    styleExpenseCategoryTable();
    categoryExpenseTable.setItems(ExpenseCategoryViewModel.getCategories());
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
          ExpenseCategoryDao.deleteExpenseCategory(obj.getData().getId());
          ExpenseCategoryViewModel.getCategories();
          e.consume();
        });
    // Edit
    edit.setOnAction(
        e -> {
          ExpenseCategoryViewModel.getItem(obj.getData().getId());
          dialog.showAndWait();
          e.consume();
        });

    contextMenu.addItems(edit, delete);

    return contextMenu;
  }

  private void expenseCategoryFormDialogPane(Stage stage) throws IOException {
    DialogPane dialogPane = fxmlLoader("forms/ExpenseCategoryForm.fxml").load();

    dialog = new Dialog<>();
    dialog.setDialogPane(dialogPane);
    dialog.initOwner(stage);
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initStyle(StageStyle.UNDECORATED);
  }

  public void categoryExpenseCreateBtnClicked() {
    dialog.showAndWait();
  }
}
