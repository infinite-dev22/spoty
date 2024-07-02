package inc.nomard.spoty.core.views.layout.navigation;

import inc.nomard.spoty.utils.navigation.*;
import io.github.palexdev.materialfx.controls.*;
import java.util.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.css.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import lombok.extern.java.*;
import org.kordamp.ikonli.javafx.*;

@Log
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
    public NavTree(Navigation navigation) {
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

        getStyleClass().addAll(EDGE_TO_EDGE, "nav-tree");
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
    public static void toggleVisibility(Node icon, boolean on) {
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
            root.setPadding(new Insets(0, 5, 0, 0));
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
                tagLabel.setText(null);
                titleLabel.setGraphic(null);
            } else {
                setGraphic(root);
                titleLabel.setGraphic(nav.graphic());
                titleLabel.setText(nav.title());
                tagLabel.setText(nav.tag());

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
        public static NavTreeItem root() {
            return new NavTreeItem(Nav.ROOT);
        }

        /**
         * Nested treeview.
         *
         * @param title display name on treeview item.
         * @param icon  display icon on treeview item.
         * @return NavTreeItem
         */
        public static NavTreeItem group(String title, String icon) {
            MFXIconWrapper wrapper = new MFXIconWrapper(icon, 24, 32);
            return new NavTreeItem(new Nav(title, null, wrapper, null, null));
        }

        /**
         * Nested treeview.
         *
         * @param title display name on treeview item.
         * @return NavTreeItem
         */
        public static NavTreeItem group(String title) {
            return new NavTreeItem(new Nav(title, null, null, null, null));
        }

        /**
         * Treeview item without child treeview items.
         *
         * @param title display name on treeview item.
         * @param icon  display icon on treeview item.
         * @param view  node to display on treeview item clicked.
         * @return NavTreeItem
         */
        public static NavTreeItem mainPage(String title, String icon, Pane view) {
            MFXIconWrapper wrapper = new MFXIconWrapper(icon, 24, 32);
            return new NavTreeItem(new Nav(title, null, wrapper, view, null));
        }

        /**
         * Child treeview item of nested treeview.
         *
         * @param title display name on treeview item.
         * @param view  node to display on treeview item clicked.
         * @return NavTreeItem
         */
        public static NavTreeItem page(String title, Pane view) {
            return new NavTreeItem(new Nav(title, null, null, view, null));
        }

        /**
         * Child treeview item of nested treeview that can be searched.
         *
         * @param title display name on treeview item.
         * @param view  node to display on treeview item clicked.
         * @return NavTreeItem
         */
        public static NavTreeItem page(String title, Pane view, String... searchKeywords) {
            return new NavTreeItem(new Nav(title, null, null, view, List.of(searchKeywords)));
        }

        public Pane view() {
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
