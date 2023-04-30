package org.infinite.spoty.controller.expenses.expense;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.model.Expense;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.data.SampleData.expenseSampleData;

public class ExpenseController implements Initializable {
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
    private Dialog<ButtonType> dialog;

    public ExpenseController(Stage stage) {
        Platform.runLater(() -> {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            try {
                expenseFormDialogPane(stage);
            } catch (IOException ex) {
                logger.error(ex.getLocalizedMessage());
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Expense> expenseDate = new MFXTableColumn<>("Date", true, Comparator.comparing(Expense::getExpenseDate));
        MFXTableColumn<Expense> expenseReference = new MFXTableColumn<>("Ref No.", true, Comparator.comparing(Expense::getExpenseReference));
        MFXTableColumn<Expense> expenseName = new MFXTableColumn<>("Name", true, Comparator.comparing(Expense::getExpenseName));
        MFXTableColumn<Expense> expenseAmount = new MFXTableColumn<>("Amount", true, Comparator.comparing(Expense::getExpenseAmount));
        MFXTableColumn<Expense> expenseCategory = new MFXTableColumn<>("Category", true, Comparator.comparing(Expense::getExpenseCategory));
        MFXTableColumn<Expense> expenseBranch = new MFXTableColumn<>("Branch", true, Comparator.comparing(Expense::getExpenseBranch));

        expenseDate.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getExpenseDate));
        expenseReference.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getExpenseReference));
        expenseName.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getExpenseName));
        expenseAmount.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getExpenseAmount));
        expenseCategory.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getExpenseCategory));
        expenseBranch.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getExpenseBranch));

        expenseTable.getTableColumns().addAll(expenseDate, expenseReference, expenseName, expenseAmount, expenseCategory, expenseBranch);
        expenseTable.getFilters().addAll(
                new StringFilter<>("Reference", Expense::getExpenseReference),
                new StringFilter<>("Name", Expense::getExpenseName),
                new DoubleFilter<>("Amount", Expense::getExpenseAmount),
                new StringFilter<>("Category", Expense::getExpenseCategory),
                new StringFilter<>("Branch", Expense::getExpenseBranch)
        );
        styleExpenseTable();
        expenseTable.setItems(expenseSampleData());
    }

    private void styleExpenseTable() {
        expenseTable.setPrefSize(1200, 1000);
        expenseTable.features().enableBounceEffect();
        expenseTable.autosizeColumnsOnInitialization();
        expenseTable.features().enableSmoothScrolling(0.5);
    }

    private void expenseFormDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/ExpenseForm.fxml").load();

        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }

    public void expenseCreateBtnClicked() {
        dialog.showAndWait();
    }
}
