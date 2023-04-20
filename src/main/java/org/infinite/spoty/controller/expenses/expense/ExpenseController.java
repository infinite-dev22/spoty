package org.infinite.spoty.controller.expenses.expense;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.infinite.spoty.model.Expense;
import org.infinite.spoty.model.Expense;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.data.SampleData.expenseSampleData;
import static org.infinite.spoty.data.SampleData.expenseSampleData;

public class ExpenseController implements Initializable {
    private MFXTableView<Expense> expensesTable;
    @FXML
    public BorderPane expenseContentPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> Platform.runLater(() -> {
            expenseContentPane.setCenter(getExpenseTable());
            setupTable();
        })).start();
    }

    private void setupTable() {
        MFXTableColumn<Expense> expenseDate = new MFXTableColumn<>("Date", true, Comparator.comparing(Expense::getExpenseDate));
        MFXTableColumn<Expense> expenseReference = new MFXTableColumn<>("Ref No.", true, Comparator.comparing(Expense::getExpenseReference));
        MFXTableColumn<Expense> expenseName = new MFXTableColumn<>("Name", true, Comparator.comparing(Expense::getExpenseName));
        MFXTableColumn<Expense> expenseAmount = new MFXTableColumn<>("Amount", true, Comparator.comparing(Expense::getExpenseAmount));
        MFXTableColumn<Expense> expenseCategory = new MFXTableColumn<>("Category", true, Comparator.comparing(Expense::getExpenseCategory));
        MFXTableColumn<Expense> expenseWarehouse = new MFXTableColumn<>("Warehouse", true, Comparator.comparing(Expense::getExpenseWarehouse));
        
        expenseDate.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getExpenseDate));
        expenseReference.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getExpenseReference));
        expenseName.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getExpenseName));
        expenseAmount.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getExpenseAmount));
        expenseCategory.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getExpenseCategory));
        expenseWarehouse.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getExpenseWarehouse));
        
        expensesTable.getTableColumns().addAll(expenseDate, expenseReference, expenseName, expenseAmount, expenseCategory, expenseWarehouse);
        expensesTable.getFilters().addAll(
                new StringFilter<>("Reference", Expense::getExpenseReference),
                new StringFilter<>("Name", Expense::getExpenseName),
                new DoubleFilter<>("Amount", Expense::getExpenseAmount),
                new StringFilter<>("Category", Expense::getExpenseCategory),
                new StringFilter<>("Warehouse", Expense::getExpenseWarehouse)
        );

        expensesTable.setItems(expenseSampleData());
    }

    private MFXTableView<Expense> getExpenseTable() {
        expensesTable = new MFXTableView<>();
        expensesTable.setPrefSize(1200, 1000);
        expensesTable.features().enableBounceEffect();
        expensesTable.autosizeColumnsOnInitialization();
        expensesTable.features().enableSmoothScrolling(0.5);
        return expensesTable;
    }
}
