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

package org.infinite.spoty.views.expenses;

import io.github.palexdev.materialfx.utils.others.loader.MFXLoader;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoaderBean;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.infinite.spoty.views.expenses.category.ExpenseCategoryController;
import org.infinite.spoty.views.expenses.expense.ExpenseController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.loadURL;
import static org.infinite.spoty.Utils.createToggle;

public class ExpensesController implements Initializable {
    private static ExpensesController instance;
    private final Stage stage;
    @FXML
    public VBox expensesNavbar;
    @FXML
    public StackPane contentPane;

    private ExpensesController(Stage stage) {
        this.stage = stage;
    }

    public static ExpensesController getInstance(Stage stage) {
        if (instance == null)
            instance = new ExpensesController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MFXLoader loader = new MFXLoader();
        loader.addView(MFXLoaderBean.of("CATEGORY", loadURL("fxml/expenses/category/Category.fxml")).setBeanToNodeMapper(() -> createToggle("fas-cubes-stacked", "Category")).setControllerFactory(c1 -> ExpenseCategoryController.getInstance(stage)).get());
        loader.addView(MFXLoaderBean.of("EXPENSE", loadURL("fxml/expenses/expense/Expense.fxml")).setBeanToNodeMapper(() -> createToggle("fas-money-check-dollar", "Expense")).setControllerFactory(c1 -> ExpenseController.getInstance(stage)).setDefaultRoot(true).get());
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
