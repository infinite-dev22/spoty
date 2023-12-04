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

package org.infinite.spoty.components.navigation.new_nav;

import io.github.palexdev.materialfx.controls.MFXTreeView;
import javafx.animation.FadeTransition;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.infinite.spoty.components.navigation.Pages;
import org.infinite.spoty.utils.SpotyLogger;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Navigation {
    static final int PAGE_TRANSITION_DURATION = 300;
    private static final Map<String, NavTree.NavTreeItem> NAV_TREE = createNavItems();
    private static StackPane viewWindow;
    private static Navigation instance;
    private final ReadOnlyObjectWrapper<NavTree.NavTreeItem> navTree =
            new ReadOnlyObjectWrapper<>(createTree());

    private Navigation(StackPane viewWindow) {
        Navigation.viewWindow = viewWindow;
    }

    public static Navigation getInstance(StackPane viewWindow) {
        if (instance == null) {
            instance = new Navigation(viewWindow);
            instance.loadView(Pages.getDashboardPane());
        }
        return instance;
    }

    public static @NotNull Map<String, NavTree.NavTreeItem> createNavItems() {
        var map = new HashMap<String, NavTree.NavTreeItem>();

        map.put("CATEGORY", NavTree.NavTreeItem.page("Category", Pages.getProductCategoryPane()));
        map.put("BRAND", NavTree.NavTreeItem.page("Brand", Pages.getBrandPane()));
        map.put("UNIT", NavTree.NavTreeItem.page("Unit", Pages.getUnitPane()));
        map.put("PRODUCTS", NavTree.NavTreeItem.page("Products", Pages.getProductPane()));
        map.put("ADJUSTMENTS", NavTree.NavTreeItem.page("Adjustments", Pages.getAdjustmentPane()));
        map.put("QUOTATIONS", NavTree.NavTreeItem.page("Quotations", Pages.getQuotationPane()));

        map.put("SALE RETURN", NavTree.NavTreeItem.page("Sales", Pages.getSaleReturnPane()));
        map.put("PURCHASE RETURN", NavTree.NavTreeItem.page("Purchases", Pages.getPurchaseReturnPane()));

        map.put(
                "EXPENSE CATEGORY", NavTree.NavTreeItem.page("Category", Pages.getExpenseCategoryPane()));
        map.put("EXPENSE", NavTree.NavTreeItem.page("Expenses", Pages.getExpensePane()));

        map.put("CUSTOMER", NavTree.NavTreeItem.page("Customers", Pages.getCustomerPane()));
        map.put("SUPPLIER", NavTree.NavTreeItem.page("Suppliers", Pages.getSupplierPane()));
        map.put("USER", NavTree.NavTreeItem.page("Users", Pages.getUserPane()));

        return map;
    }

    public static void navigate(Pane view, Pane viewWindow) {
        loadIntoView(view, viewWindow);
    }

    private static void loadIntoView(Pane view, Pane viewWindow) {
        try {
            final Pane prevWindow =
                    (Pane)
                            viewWindow.getChildren().stream()
                                    .filter(c -> c instanceof Pane)
                                    .findFirst()
                                    .orElse(null);
            final Pane nextWindow = view;
            Pane existingNextChild =
                    (Pane)
                            viewWindow.getChildren().stream().findAny().filter(c -> c.equals(view)).orElse(null);

            if (Objects.equals(prevWindow, nextWindow)) return;

            if (viewWindow.getChildren().isEmpty()) {
                viewWindow.getChildren().add(nextWindow);
                return;
            } else if (!Objects.equals(existingNextChild, null)) {
                var transition =
                        new FadeTransition(Duration.millis(PAGE_TRANSITION_DURATION), existingNextChild);
                transition.setFromValue(0.0);
                transition.setToValue(1.0);
                transition.setOnFinished(
                        event -> viewWindow.getChildren().get((int) existingNextChild.getViewOrder()).toFront());
                transition.play();
                return;
            }

            Objects.requireNonNull(prevWindow);

            viewWindow.getChildren().add(nextWindow);
            viewWindow.getChildren().remove(prevWindow);
            var transition = new FadeTransition(Duration.millis(PAGE_TRANSITION_DURATION), nextWindow);
            transition.setFromValue(0.0);
            transition.setToValue(1.0);
            transition.setOnFinished(event -> nextWindow.toFront());
            transition.play();
        } catch (RuntimeException e) {
            SpotyLogger.writeToFile(e, Navigation.class);
        }
    }

    public ReadOnlyObjectProperty<NavTree.NavTreeItem> navTreeProperty() {
        return navTree.getReadOnlyProperty();
    }

    public MFXTreeView<Nav> createNavigation() {
        return new NavTree(this);
    }

    private NavTree.@NotNull NavTreeItem createTree() {
        var dashboard =
                NavTree.NavTreeItem.mainPage("Dashboard", "fas-chart-simple", Pages.getDashboardPane());

        var inventory = NavTree.NavTreeItem.group("Inventory", "fas-cubes");
        inventory
                .getItems()
                .setAll(
                        NAV_TREE.get("CATEGORY"),
                        NAV_TREE.get("BRAND"),
                        NAV_TREE.get("UNIT"),
                        NAV_TREE.get("PRODUCTS"),
                        NAV_TREE.get("ADJUSTMENTS"),
                        NAV_TREE.get("QUOTATIONS"));

        var requisition =
                NavTree.NavTreeItem.mainPage(
                        "Requisitions", "fas-hand-holding", Pages.getRequisitionPane());
        var purchase =
                NavTree.NavTreeItem.mainPage("Purchases", "fas-cart-plus", Pages.getPurchasePane());
        var transfer =
                NavTree.NavTreeItem.mainPage(
                        "Transfers", "fas-arrow-right-arrow-left", Pages.getTransferPane());
        var stockIn =
                NavTree.NavTreeItem.mainPage("Stock In", "fas-cart-flatbed", Pages.getStockInPane());
        var pos = NavTree.NavTreeItem.mainPage("POS", "fas-cart-flatbed", Pages.getPosPane());
        var sale = NavTree.NavTreeItem.mainPage("Sales", "fas-cash-register", Pages.getSalePane());
        var returns = NavTree.NavTreeItem.group("Returns", "fas-retweet");
        returns.getItems().setAll(NAV_TREE.get("SALE RETURN"), NAV_TREE.get("PURCHASE RETURN"));

        var expense = NavTree.NavTreeItem.group("Expenses", "fas-wallet");
        expense.getItems().setAll(NAV_TREE.get("EXPENSE CATEGORY"), NAV_TREE.get("EXPENSE"));

        var people = NavTree.NavTreeItem.group("People", "fas-users");
        people
                .getItems()
                .setAll(NAV_TREE.get("CUSTOMER"), NAV_TREE.get("SUPPLIER"), NAV_TREE.get("USER"));

        var root = NavTree.NavTreeItem.root();
        root.getItems()
                .addAll(
                        dashboard,
                        inventory,
                        requisition,
                        purchase,
                        transfer,
                        stockIn,
                        pos,
                        sale,
                        returns,
                        expense,
                        people);
        return root;
    }

    public void navigate(BorderPane view) {
        loadView(view);
    }

    private void loadView(BorderPane view) {
        try {
            final BorderPane prevWindow =
                    (BorderPane)
                            viewWindow.getChildren().stream()
                                    .filter(c -> c instanceof BorderPane)
                                    .findFirst()
                                    .orElse(null);
            final BorderPane nextWindow = view;
            BorderPane existingNextChild =
                    (BorderPane)
                            viewWindow.getChildren().stream().findAny().filter(c -> c.equals(view)).orElse(null);

            if (Objects.equals(prevWindow, nextWindow)) return;

            if (viewWindow.getChildren().isEmpty()) {
                viewWindow.getChildren().add(nextWindow);
                return;
            } else if (!Objects.equals(existingNextChild, null)) {
                var transition =
                        new FadeTransition(Duration.millis(PAGE_TRANSITION_DURATION), existingNextChild);
                transition.setFromValue(0.0);
                transition.setToValue(1.0);
                transition.setOnFinished(
                        t -> viewWindow.getChildren().get((int) existingNextChild.getViewOrder()).toFront());
                transition.play();
                return;
            }

            Objects.requireNonNull(prevWindow);

            viewWindow.getChildren().add(nextWindow);
            viewWindow.getChildren().remove(prevWindow);
            var transition = new FadeTransition(Duration.millis(PAGE_TRANSITION_DURATION), nextWindow);
            transition.setFromValue(0.0);
            transition.setToValue(1.0);
            transition.setOnFinished(t -> nextWindow.toFront());
            transition.play();
        } catch (RuntimeException e) {
            SpotyLogger.writeToFile(e, Navigation.class);
        }
    }
}
