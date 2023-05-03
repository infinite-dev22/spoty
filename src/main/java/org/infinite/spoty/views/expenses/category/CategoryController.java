package org.infinite.spoty.views.expenses.category;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
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
import org.infinite.spoty.models.ExpenseCategory;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.data.SampleData.expenseCategorySampleData;

public class CategoryController implements Initializable {
    @FXML
    public MFXTextField categoryExpenseSearchBar;
    @FXML
    public HBox categoryExpenseActionsPane;
    @FXML
    public MFXButton categoryExpenseImportBtn;
    @FXML
    public BorderPane categoryContentPane;
    @FXML
    private MFXTableView<ExpenseCategory> categoryExpenseTable;
    private Dialog<ButtonType> dialog;

    public CategoryController(Stage stage) {
        Platform.runLater(() -> {
            try {
                expenseCategoryFormDialogPane(stage);
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
        MFXTableColumn<ExpenseCategory> categoryName = new MFXTableColumn<>("Name", true, Comparator.comparing(ExpenseCategory::getCategoryName));
        MFXTableColumn<ExpenseCategory> categoryDescription = new MFXTableColumn<>("Description", true, Comparator.comparing(ExpenseCategory::getCategoryDescription));

        categoryName.setRowCellFactory(category -> new MFXTableRowCell<>(ExpenseCategory::getCategoryName));
        categoryDescription.setRowCellFactory(category -> new MFXTableRowCell<>(ExpenseCategory::getCategoryDescription));

        categoryExpenseTable.getTableColumns().addAll(categoryName, categoryDescription);
        categoryExpenseTable.getFilters().addAll(
                new StringFilter<>("Name", ExpenseCategory::getCategoryName),
                new StringFilter<>("Description", ExpenseCategory::getCategoryDescription)
        );

        styleExpenseCategoryTable();
        categoryExpenseTable.setItems(expenseCategorySampleData());
    }

    private void styleExpenseCategoryTable() {
        categoryExpenseTable.setPrefSize(1200, 1000);
        categoryExpenseTable.features().enableBounceEffect();
        categoryExpenseTable.autosizeColumnsOnInitialization();
        categoryExpenseTable.features().enableSmoothScrolling(0.5);
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
