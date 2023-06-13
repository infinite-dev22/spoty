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

package org.infinite.spoty.views;

import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.utils.ScrollUtils;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoader;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoaderBean;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.infinite.spoty.values.strings.Labels;
import org.infinite.spoty.views.expenses.ExpensesController;
import org.infinite.spoty.views.inventory.InventoryController;
import org.infinite.spoty.views.people.PeopleController;
import org.infinite.spoty.views.purchases.PurchasesController;
import org.infinite.spoty.views.requisition.RequisitionController;
import org.infinite.spoty.views.sales.SalesController;
import org.infinite.spoty.views.settings.SettingsController;
import org.infinite.spoty.views.stock_in.StockInController;
import org.infinite.spoty.views.transfer.TransferController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.loadURL;
import static org.infinite.spoty.Utils.createToggle;

public class BaseController implements Initializable {
    public final Stage stage;
    @FXML
    public MFXFontIcon closeIcon;
    @FXML
    public MFXFontIcon maximizeIcon;
    @FXML
    public MFXFontIcon minimizeIcon;
    @FXML
    public Pane contentPane;
    @FXML
    public VBox navBar;
    @FXML
    public HBox windowHeader;
    @FXML
    public MFXScrollPane scrollPane;
    @FXML
    public AnchorPane rootPane;
    @FXML
    public VBox settingsHolder;
    @FXML
    public Label appNameLabel;
    private double xOffset;
    private double yOffset;

    public BaseController(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void closeIconClicked() {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void maximizeIconClicked() {
        ((Stage) rootPane.getScene().getWindow()).setMaximized(!((Stage) rootPane.getScene().getWindow()).isMaximized());
    }

    @FXML
    void minimizeIconClicked() {
        ((Stage) rootPane.getScene().getWindow()).setIconified(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        appNameLabel.setText(Labels.APP_NAME);
        appNameLabel.setFont(new Font(48));
        windowHeader.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
        windowHeader.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });

        initializeLoader();

        ScrollUtils.addSmoothScrolling(scrollPane);
    }

    public void initializeLoader() {
        MFXLoader loader = new MFXLoader();
        loader.addView(MFXLoaderBean.of("DASHBOARD", loadURL("fxml/dashboard/Dashboard.fxml")).setBeanToNodeMapper(() -> createToggle("fas-chart-simple", "Dashboard")).setDefaultRoot(true).get());
        loader.addView(MFXLoaderBean.of("INVENTORY", loadURL("fxml/inventory/Inventory.fxml")).setBeanToNodeMapper(() -> createToggle("fas-cubes", "Inventory")).setControllerFactory(c -> new InventoryController(stage)).get());
        loader.addView(MFXLoaderBean.of("REQUISITIONS", loadURL("fxml/requisition/Requisition.fxml")).setBeanToNodeMapper(() -> createToggle("fas-hand-holding", "Requisitions")).setControllerFactory(c -> new RequisitionController(stage)).get());
        loader.addView(MFXLoaderBean.of("PURCHASES", loadURL("fxml/purchases/Purchases.fxml")).setBeanToNodeMapper(() -> createToggle("fas-cart-plus", "Purchases")).setControllerFactory(c -> new PurchasesController(stage)).get());
        loader.addView(MFXLoaderBean.of("TRANSFERS", loadURL("fxml/transfer/Transfer.fxml")).setBeanToNodeMapper(() -> createToggle("fas-arrow-right-arrow-left", "Transfers")).setControllerFactory(c -> new TransferController(stage)).get());
        loader.addView(MFXLoaderBean.of("STOCK IN", loadURL("fxml/stock_in/StockIn.fxml")).setBeanToNodeMapper(() -> createToggle("fas-cart-flatbed", "Stock In")).setControllerFactory(c -> new StockInController(stage)).get());
        loader.addView(MFXLoaderBean.of("SALES", loadURL("fxml/sales/Sales.fxml")).setBeanToNodeMapper(() -> createToggle("fas-cash-register", "Sales")).setControllerFactory(c -> new SalesController(stage)).get());
        loader.addView(MFXLoaderBean.of("RETURNS", loadURL("fxml/returns/Returns.fxml")).setBeanToNodeMapper(() -> createToggle("fas-retweet", "Returns")).get());
        loader.addView(MFXLoaderBean.of("EXPENSES", loadURL("fxml/expenses/Expenses.fxml")).setBeanToNodeMapper(() -> createToggle("fas-wallet", "Expenses")).setControllerFactory(c -> new ExpensesController(stage)).get());
        loader.addView(MFXLoaderBean.of("PEOPLE", loadURL("fxml/people/People.fxml")).setBeanToNodeMapper(() -> createToggle("fas-users", "People")).setControllerFactory(c -> new PeopleController(stage)).get());
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
            navBar.getChildren().setAll(nodes);
        });
        loader.start();

        MFXLoader settingsLoader = new MFXLoader();
        settingsLoader.addView(MFXLoaderBean.of("SETTINGS", loadURL("fxml/settings/Settings.fxml")).setBeanToNodeMapper(() -> createToggle("fas-gears", "Settings")).setControllerFactory(c -> new SettingsController(stage)).get());
        settingsLoader.setOnLoadedAction(beans -> {
            List<ToggleButton> node = beans.stream().map(bean -> {
                ToggleButton toggle = (ToggleButton) bean.getBeanToNodeMapper().get();
                toggle.setOnAction(event -> contentPane.getChildren().setAll(bean.getRoot()));
                return toggle;
            }).toList();
            settingsHolder.getChildren().setAll(node);
        });
        settingsLoader.start();
    }
}
