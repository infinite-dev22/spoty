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

package org.infinite.spoty.components.navigation;

import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;
import java.util.Objects;

public class NavTree extends TreeView<Nav> {
    public static final double SIDEBAR_WIDTH = 250.0;

    /**
     * Removes external control borders.
     */
    public static final String EDGE_TO_EDGE = "edge-to-edge";

    static ChangeListener<Boolean> expandedListener;

    /**
     * Creates a side navigation.
     *
     * @param navigation navigation
     */
    public NavTree(@NotNull Navigation navigation) {
        super();

        getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (obs, old, val) -> {
                            if (!(val instanceof NavTreeItem navTreeItem)) {
                                return;
                            }

                            if (!navTreeItem.isGroup()) {
                                navigation.navigate(navTreeItem.view());
                            }
                            // Collapse an already expanded node when another is clicked
                            if (navTreeItem.isGroup() && !navTreeItem.isInnerGroup()) {
                                navTreeItem.expandedProperty().addListener(expandedListener);
                            }
                        });

        expandedListener =
                (obs, wasExpanded, isNowExpanded) -> {
                    if (isNowExpanded) {
                        ReadOnlyProperty<?> expandedProperty = (ReadOnlyProperty<?>) obs;
                        Object itemThatWasJustExpanded = expandedProperty.getBean();
                        for (TreeItem<Nav> item : getRoot().getChildren()) {
                            if (item != itemThatWasJustExpanded) {
                                item.setExpanded(false);
                                item.getChildren().forEach(child -> {
                                    if (child.isExpanded()) {
                                        child.setExpanded(false);
                                    }
                                });
                            }
                        }
                    }
                };

        getStyleClass().addAll(EDGE_TO_EDGE);
        setShowRoot(false);
        rootProperty().bind(navigation.navTreeProperty());
        setCellFactory(p -> new NavTreeCell());
    }

    /**
     * Controls side navigation dropdown arrow visibility.
     *
     * @param icon dropdown icon.
     * @param on   show icon(boolean).
     */
    public static void toggleVisibility(@NotNull Node icon, boolean on) {
        icon.setVisible(on);
        icon.setManaged(on);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final class NavTreeCell extends TreeCell<Nav> {
        private static final PseudoClass GROUP = PseudoClass.getPseudoClass("group");
        private static final PseudoClass SUB_ITEM = PseudoClass.getPseudoClass("sub-tree-item");

        private final HBox root;
        private final Label titleLabel;
        private final Node arrowIcon;
        private final Label tagLabel;

        public NavTreeCell() {
            super();

            titleLabel = new Label();
            titleLabel.getStyleClass().add("title");

            arrowIcon = new FontIcon();
            arrowIcon.getStyleClass().add("arrow");

            tagLabel = new Label("");
            tagLabel.getStyleClass().add("tag");

            root = new HBox();
            root.setAlignment(Pos.CENTER_LEFT);
            root.getChildren().setAll(titleLabel, new Spacer(), tagLabel, arrowIcon);
            root.setCursor(Cursor.HAND);
            root.getStyleClass().add("container");
            root.setMaxWidth(SIDEBAR_WIDTH - 10);
            setPrefWidth(root.getPrefWidth());
            root.setPrefHeight(getPrefHeight());

            // Expand dropdown on single click.
            root.setOnMouseClicked(
                    e -> {
                        if (!(getTreeItem() instanceof NavTreeItem navTreeItem)) {
                            return;
                        }

                        if (navTreeItem.isGroup() && e.getButton() == MouseButton.PRIMARY) {
                            navTreeItem.setExpanded(!navTreeItem.isExpanded());
                            // scroll slightly above the target
                            getTreeView().scrollTo(getTreeView().getRow(navTreeItem) - 10);
                        }
                    });

            treeItemProperty()
                    .addListener(
                            (obs, oldTreeItem, newTreeItem) ->
                                    pseudoClassStateChanged(
                                            SUB_ITEM,
                                            newTreeItem != null && newTreeItem.getParent() != getTreeView().getRoot()));

            getStyleClass().add("nav-tree-cell");
        }

        /**
         * @param nav   The new item for the cell.
         * @param empty whether this cell represents data from the list. If it
         *              is empty, then it does not represent any domain data, but is a cell
         *              being used to render an "empty" row.
         */
        @Override
        protected void updateItem(Nav nav, boolean empty) {
            super.updateItem(nav, empty);
            setDisclosureNode(null);

            if (nav == null || empty) {
                setText("");
                setGraphic(null);
                titleLabel.setText(null);
                titleLabel.setGraphic(null);
            } else {
                setGraphic(root);
                titleLabel.setGraphic(nav.graphic());
                titleLabel.setText(nav.title());

                pseudoClassStateChanged(GROUP, nav.isGroup());
                toggleVisibility(arrowIcon, nav.isGroup());
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final class NavTreeItem extends TreeItem<Nav> {
        private final Nav nav;

        /**
         * <code>NavTreeItem</code> class constructor.
         *
         * @param nav nav
         */
        public NavTreeItem(Nav nav) {
            this.nav = Objects.requireNonNull(nav, "nav");
            setValue(nav);
        }

        /**
         * Treeview root item.
         *
         * @return NavTreeItem
         */
        @Contract(" -> new")
        public static @NotNull NavTreeItem root() {
            return new NavTreeItem(Nav.ROOT);
        }

        /**
         * Nested treeview.
         *
         * @param title display name on treeview item.
         * @param icon  display icon on treeview item.
         * @return NavTreeItem
         */
        public static @NotNull NavTreeItem group(String title, String icon) {
            MFXIconWrapper wrapper = new MFXIconWrapper(icon, 24, 32);
            return new NavTreeItem(new Nav(title, wrapper, null, null));
        }

        /**
         * Nested treeview.
         *
         * @param title display name on treeview item.
         * @return NavTreeItem
         */
        public static @NotNull NavTreeItem group(String title) {
            return new NavTreeItem(new Nav(title, null, null, null));
        }

        /**
         * Treeview item without child treeview items.
         *
         * @param title display name on treeview item.
         * @param icon  display icon on treeview item.
         * @param view  node to display on treeview item clicked.
         * @return NavTreeItem
         */
        public static @NotNull NavTreeItem mainPage(String title, String icon, BorderPane view) {
            MFXIconWrapper wrapper = new MFXIconWrapper(icon, 24, 32);
            return new NavTreeItem(new Nav(title, wrapper, view, null));
        }

        /**
         * Child treeview item of nested treeview.
         *
         * @param title display name on treeview item.
         * @param view  node to display on treeview item clicked.
         * @return NavTreeItem
         */
        @Contract("_, _ -> new")
        public static @NotNull NavTreeItem page(String title, BorderPane view) {
            return new NavTreeItem(new Nav(title, null, view, null));
        }

        /**
         * Child treeview item of nested treeview that can be searched.
         *
         * @param title display name on treeview item.
         * @param view  node to display on treeview item clicked.
         * @return NavTreeItem
         */
        @Contract("_, _, _ -> new")
        public static @NotNull NavTreeItem page(String title, BorderPane view, String... searchKeywords) {
            return new NavTreeItem(new Nav(title, null, view, List.of(searchKeywords)));
        }

        public @Nullable BorderPane view() {
            return nav.view();
        }

        public boolean isGroup() {
            return nav.isGroup();
        }

        public boolean isInnerGroup() {
            return nav.isInnerGroup();
        }

        public boolean isMainPage() {
            return nav.isMainPage();
        }
    }
}
