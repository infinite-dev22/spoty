package inc.nomard.spoty.core.views.layout.navigation;

import inc.nomard.spoty.core.views.util.Page;
import inc.nomard.spoty.utils.navigation.Spacer;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import lombok.extern.java.Log;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;
import java.util.Objects;

@Log
public class NavTree extends TreeView<Nav> {
    public static final double SIDEBAR_WIDTH = 250.0;
    public static final String EDGE_TO_EDGE = "edge-to-edge";

    static ChangeListener<Boolean> expandedListener;

    public NavTree(Navigation navigation) {
        super();

        log.info("Initializing NavTree with navigation: " + navigation);

        getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, old, val) -> {
                    if (!(val instanceof NavTreeItem navTreeItem)) {
                        log.warning("Selected item is not of type NavTreeItem: " + (val != null ? val.getClass() : "null"));
                        return;
                    }

                    if (!navTreeItem.isGroup()) {
                        log.info("Navigating to view: " + navTreeItem.view().getName());
                        navigation.navigate(navTreeItem.view());
                    } else {
                        log.info("Selected item is a group: " + navTreeItem.nav.title());
                    }

                    // Collapse an already expanded node when another is clicked
                    if (navTreeItem.isGroup() && !navTreeItem.isInnerGroup()) {
                        navTreeItem.expandedProperty().removeListener(expandedListener);
                        navTreeItem.expandedProperty().addListener(expandedListener);
                    }
                });

        expandedListener = (obs, wasExpanded, isNowExpanded) -> {
            if (isNowExpanded) {
                ReadOnlyProperty<?> expandedProperty = (ReadOnlyProperty<?>) obs;
                Object itemThatWasJustExpanded = expandedProperty.getBean();

                if (getRoot() != null && getRoot().getChildren() != null) {
                    for (TreeItem<Nav> item : getRoot().getChildren()) {
                        if (item != itemThatWasJustExpanded) {
                            log.info("Collapsing item: " + item.getValue().title());
                            item.setExpanded(false);
                            if (item.getChildren() != null && !item.getChildren().isEmpty()) {
                                item.getChildren().forEach(child -> {
                                    if (child != null && child.isExpanded()) {
                                        log.info("Collapsing child item: " + child.getValue().title());
                                        child.setExpanded(false);
                                    }
                                });
                            }
                        }
                    }
                }
            }
        };

        getStyleClass().addAll(EDGE_TO_EDGE, "nav-tree");
        setShowRoot(false);
        rootProperty().bind(navigation.navTreeProperty());
        setCellFactory(p -> new NavTreeCell());
    }

    public static void toggleVisibility(Node icon, boolean on) {
        icon.setVisible(on);
        icon.setManaged(on);
    }

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

            root.setOnMouseClicked(e -> {
                if (getTreeItem() == null) {
                    log.warning("TreeItem is null.");
                    return;
                }

                if (!(getTreeItem() instanceof NavTreeItem navTreeItem)) {
                    log.warning("TreeItem is not of type NavTreeItem: " + getTreeItem().getClass());
                    return;
                }

                if (navTreeItem.isGroup() && e.getButton() == MouseButton.PRIMARY) {
                    navTreeItem.setExpanded(!navTreeItem.isExpanded());

                    // Get the row index for scrolling
                    int rowIndex = getTreeView().getRow(navTreeItem);
                    log.info("Row index for navTreeItem: " + rowIndex);
                    if (rowIndex >= 0) { // Ensure row index is valid
                        log.info("Scrolling to row index: " + (rowIndex - 10));
                        getTreeView().scrollTo(rowIndex - 10);
                    }
                }
            });

            treeItemProperty()
                    .addListener((obs, oldTreeItem, newTreeItem) -> {
                        pseudoClassStateChanged(SUB_ITEM, newTreeItem != null && newTreeItem.getParent() != getTreeView().getRoot());
                        log.info("TreeItem changed from " + oldTreeItem + " to " + newTreeItem);
                    });

            getStyleClass().add("nav-tree-cell");
        }

        @Override
        protected void updateItem(Nav nav, boolean empty) {
            super.updateItem(nav, empty);
            setDisclosureNode(null);

            if (nav == null || empty) {
                log.info("Updating item: cell is empty");
                setText("");
                setGraphic(null);
                titleLabel.setText(null);
                tagLabel.setText(null);
                titleLabel.setGraphic(null);
            } else {
                log.info("Updating item: " + nav.title());
                setGraphic(root);
                titleLabel.setGraphic(nav.graphic());
                titleLabel.setText(nav.title());
                tagLabel.setText(nav.tag());

                pseudoClassStateChanged(GROUP, nav.isGroup());
                toggleVisibility(arrowIcon, nav.isGroup());
            }
        }
    }

    public static final class NavTreeItem extends TreeItem<Nav> {
        private final Nav nav;

        public NavTreeItem(Nav nav) {
            this.nav = Objects.requireNonNull(nav, "nav");
            setValue(nav);
            log.info("Created NavTreeItem: " + nav.title());
        }

        public static NavTreeItem root() {
            return new NavTreeItem(Nav.ROOT);
        }

        public static NavTreeItem group(String title, Ikon icon) {
            FontIcon node = new FontIcon(icon);
            log.info("Creating group NavTreeItem: " + title);
            return new NavTreeItem(new Nav(title, null, node, null, null));
        }

        public static NavTreeItem group(String title) {
            log.info("Creating group NavTreeItem: " + title);
            return new NavTreeItem(new Nav(title, null, null, null, null));
        }

        public static NavTreeItem mainPage(String title, Ikon icon, Class<? extends Page> view) {
            FontIcon node = new FontIcon(icon);
            log.info("Creating main page NavTreeItem: " + title);
            return new NavTreeItem(new Nav(title, null, node, view, null));
        }

        public static NavTreeItem page(String title, Class<? extends Page> view) {
            log.info("Creating page NavTreeItem: " + title);
            return new NavTreeItem(new Nav(title, null, null, view, null));
        }

        public static NavTreeItem page(String title, Class<? extends Page> view, String... searchKeywords) {
            log.info("Creating searchable page NavTreeItem: " + title);
            return new NavTreeItem(new Nav(title, null, null, view, List.of(searchKeywords)));
        }

        public Class<? extends Page> view() {
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
