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
import org.infinite.spoty.database.models.ExpenseCategory;
import org.infinite.spoty.viewModels.ExpenseCategoryViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

public class ExpenseCategoryController implements Initializable {
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

    public ExpenseCategoryController(Stage stage) {
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
        MFXTableColumn<ExpenseCategory> categoryName = new MFXTableColumn<>("Name", true, Comparator.comparing(ExpenseCategory::getName));
        MFXTableColumn<ExpenseCategory> categoryDescription = new MFXTableColumn<>("Description", true, Comparator.comparing(ExpenseCategory::getDescription));

        categoryName.setRowCellFactory(category -> new MFXTableRowCell<>(ExpenseCategory::getName));
        categoryDescription.setRowCellFactory(category -> new MFXTableRowCell<>(ExpenseCategory::getDescription));

        categoryName.prefWidthProperty().bind(categoryExpenseTable.widthProperty().multiply(.5));
        categoryDescription.prefWidthProperty().bind(categoryExpenseTable.widthProperty().multiply(.5));

        categoryExpenseTable.getTableColumns().addAll(categoryName, categoryDescription);
        categoryExpenseTable.getFilters().addAll(
                new StringFilter<>("Name", ExpenseCategory::getName),
                new StringFilter<>("Description", ExpenseCategory::getDescription)
        );

        styleExpenseCategoryTable();
        categoryExpenseTable.setItems(ExpenseCategoryViewModel.getCategories());
    }

    private void styleExpenseCategoryTable() {
        categoryExpenseTable.setPrefSize(1200, 1000);
        categoryExpenseTable.features().enableBounceEffect();
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
