package org.infinite.spoty.views.expenses.expense;

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
import org.infinite.spoty.database.models.Expense;
import org.infinite.spoty.viewModels.ExpenseViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

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
            try {
                expenseFormDialogPane(stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Expense> expenseDate = new MFXTableColumn<>("Date", true, Comparator.comparing(Expense::getDate));
        MFXTableColumn<Expense> expenseRef = new MFXTableColumn<>("Reference No.", true, Comparator.comparing(Expense::getRef));
        MFXTableColumn<Expense> expenseName = new MFXTableColumn<>("Name", true, Comparator.comparing(Expense::getBranchName));
        MFXTableColumn<Expense> expenseAmount = new MFXTableColumn<>("Amount", true, Comparator.comparing(Expense::getAmount));
        MFXTableColumn<Expense> expenseCategory = new MFXTableColumn<>("Category", true, Comparator.comparing(Expense::getExpenseCategoryName));
        MFXTableColumn<Expense> expenseBranch = new MFXTableColumn<>("Branch", true, Comparator.comparing(Expense::getBranchName));

        expenseDate.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getDate));
        expenseRef.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getRef));
        expenseName.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getBranchName));
        expenseAmount.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getAmount));
        expenseCategory.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getExpenseCategoryName));
        expenseBranch.setRowCellFactory(expense -> new MFXTableRowCell<>(Expense::getBranchName));

        expenseDate.prefWidthProperty().bind(expenseTable.widthProperty().multiply(.1));
        expenseRef.prefWidthProperty().bind(expenseTable.widthProperty().multiply(.16));
        expenseName.prefWidthProperty().bind(expenseTable.widthProperty().multiply(.2));
        expenseAmount.prefWidthProperty().bind(expenseTable.widthProperty().multiply(.2));
        expenseCategory.prefWidthProperty().bind(expenseTable.widthProperty().multiply(.2));
        expenseBranch.prefWidthProperty().bind(expenseTable.widthProperty().multiply(.2));

        expenseTable.getTableColumns().addAll(expenseDate, expenseRef, expenseName, expenseAmount, expenseCategory, expenseBranch);
        expenseTable.getFilters().addAll(
                new StringFilter<>("Reference No.", Expense::getRef),
                new StringFilter<>("Name", Expense::getBranchName),
                new DoubleFilter<>("Amount", Expense::getAmount),
                new StringFilter<>("Category", Expense::getExpenseCategoryName),
                new StringFilter<>("Branch", Expense::getBranchName)
        );
        styleExpenseTable();
        expenseTable.setItems(ExpenseViewModel.getExpenses());
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
