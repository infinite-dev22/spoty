package org.infinite.spoty.controller.expenses;

import io.github.palexdev.materialfx.utils.others.loader.MFXLoader;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoaderBean;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.infinite.spoty.controller.expenses.category.CategoryController;
import org.infinite.spoty.controller.expenses.expense.ExpenseController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.loadURL;
import static org.infinite.spoty.Utils.createToggle;

public class ExpensesController implements Initializable {
    private final Stage stage;
    @FXML
    public VBox expensesNavbar;

    @FXML
    public StackPane contentPane;

    public ExpensesController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MFXLoader loader = new MFXLoader();
        loader.addView(MFXLoaderBean.of("CATEGORY", loadURL("fxml/expenses/category/Category.fxml")).setBeanToNodeMapper(() -> createToggle("fas-gauge-high", "Category")).setControllerFactory(c1 -> new CategoryController(stage)).get());
        loader.addView(MFXLoaderBean.of("EXPENSE", loadURL("fxml/expenses/expense/Expense.fxml")).setBeanToNodeMapper(() -> createToggle("fas-toggle-on", "Expense")).setControllerFactory(c1 -> new ExpenseController(stage)).setDefaultRoot(true).get());
        loader.setOnLoadedAction(beans -> {
            List<ToggleButton> nodes = beans.stream()
                    .map(bean -> {
                        ToggleButton toggle = (ToggleButton) bean.getBeanToNodeMapper().get();
                        toggle.setOnAction(event -> contentPane.getChildren().setAll(bean.getRoot()));
                        if (bean.isDefaultView()) {
                            contentPane.getChildren().setAll(bean.getRoot());
                            toggle.setSelected(true);
                        }
                        return toggle;
                    })
                    .toList();
            expensesNavbar.getChildren().setAll(nodes);
        });
        Platform.runLater(loader::start);
    }
}
