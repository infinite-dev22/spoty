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

import atlantafx.base.util.*;
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
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class ExpenseController implements Initializable {
    private static ExpenseController instance;
    private final Stage stage;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXTableView<Expense> expenseTable;
    @FXML
    public BorderPane expenseContentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox refresh;
    @FXML
    public HBox leftHeaderPane;
    @FXML
    public MFXProgressSpinner progress;
    private MFXStageDialog dialog;

    private ExpenseController(Stage stage) {
        this.stage = stage;
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
        setIcons();
        setSearchBar();
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
        expenseDate.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getLocaleDate));
        expenseName.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getName));
        expenseAmount.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getAmount));
        expenseCategory.setRowCellFactory(
                expense -> new MFXTableRowCell<>(Expense::getExpenseCategoryName));
        expenseDate.prefWidthProperty().bind(expenseTable.widthProperty().multiply(.25));
        expenseName.prefWidthProperty().bind(expenseTable.widthProperty().multiply(.25));
        expenseAmount.prefWidthProperty().bind(expenseTable.widthProperty().multiply(.25));
        expenseCategory.prefWidthProperty().bind(expenseTable.widthProperty().multiply(.25));
        expenseTable
                .getTableColumns()
                .addAll(expenseName, expenseCategory, expenseDate, expenseAmount);
        expenseTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Reference No.", Expense::getRef),
                        new StringFilter<>("Name", Expense::getName),
                        new DoubleFilter<>("Amount", Expense::getAmount),
                        new StringFilter<>("Category", Expense::getExpenseCategoryName));
        styleExpenseTable();
        if (ExpensesViewModel.getExpenses().isEmpty()) {
            ExpensesViewModel.getExpenses()
                    .addListener(
                            (ListChangeListener<Expense>)
                                    c -> expenseTable.setItems(ExpensesViewModel.getExpenses()));
        } else {
            expenseTable.itemsProperty().bindBidirectional(ExpensesViewModel.expensesProperty());
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
                    ExpensesViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
                    e.consume();
                });
        // Edit
        edit.setOnAction(
                e -> {
                    ExpensesViewModel.getItem(obj.getData().getId(), () -> dialog.showAndWait(), this::errorMessage);
                    e.consume();
                });
        contextMenu.addItems(edit, delete);
        return contextMenu;
    }

    private void expenseFormDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/ExpenseForm.fxml");
        fxmlLoader.setControllerFactory(c -> ExpenseFormController.getInstance(stage));
        MFXGenericDialog dialogContent = fxmlLoader.load();
        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);
        dialogContent.setShowClose(false);
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

    public void createBtnClicked() {
        dialog.showAndWait();
    }

    private void onSuccess() {
        ExpensesViewModel.getAllExpenses(null, null);
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
                ExpensesViewModel.getAllExpenses(null, null);
            }
            progress.setVisible(true);
            ExpensesViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
            }, this::errorMessage);
        });
    }

    private void successMessage(String message) {
        displayNotification(message, MessageVariants.SUCCESS, "fas-circle-check");
    }

    private void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, "fas-triangle-exclamation");
    }

    private void displayNotification(String message, MessageVariants type, String icon) {
        SpotyMessage notification = new SpotyMessage.MessageBuilder(message)
                .duration(MessageDuration.SHORT)
                .icon(icon)
                .type(type)
                .height(60)
                .build();
        AnchorPane.setTopAnchor(notification, 5.0);
        AnchorPane.setRightAnchor(notification, 5.0);

        var in = Animations.slideInDown(notification, Duration.millis(250));
        if (!BaseController.getInstance(stage).morphPane.getChildren().contains(notification)) {
            BaseController.getInstance(stage).morphPane.getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> SpotyMessage.delay(notification, stage));
        }
    }
}
