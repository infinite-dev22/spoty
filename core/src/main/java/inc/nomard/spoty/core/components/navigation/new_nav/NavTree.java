package inc.nomard.spoty.core.components.navigation.new_nav;

import inc.nomard.spoty.core.components.navigation.Navigation;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTreeItem;
import io.github.palexdev.materialfx.controls.MFXTreeView;
import io.github.palexdev.materialfx.controls.base.AbstractMFXTreeCell;
import io.github.palexdev.materialfx.controls.base.AbstractMFXTreeItem;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.base.Theme;
import io.github.palexdev.materialfx.utils.NodeUtils;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class NavTree extends MFXTreeView<Nav> {
    public static final double SIDEBAR_WIDTH = 250.0;
    public static final String EDGE_TO_EDGE = "edge-to-edge";
    static ChangeListener<Boolean> expandedListener;
    static Navigation navigation;

    public NavTree(Navigation navigation) {
        super();
        expandedListener =
                (obs, wasExpanded, isNowExpanded) -> {
                    if (isNowExpanded) {
                        ReadOnlyProperty<?> expandedProperty = (ReadOnlyProperty<?>) obs;
                        Object itemThatWasJustExpanded = expandedProperty.getBean();
                        for (AbstractMFXTreeItem<Nav> item : getRoot().getItems()) {
                            if (item != itemThatWasJustExpanded) {
                                item.setStartExpanded(false);
                            }
                        }
                    }
                };

        NavTree.navigation = navigation;

        getStyleClass().addAll(EDGE_TO_EDGE);
        setShowRoot(false);
//        rootProperty().bind(navigation.navTreeProperty());
    }

    public static final class NavTreeCell<T> extends AbstractMFXTreeCell<T> {
        private static final PseudoClass GROUP = PseudoClass.getPseudoClass("group");
        private final AbstractMFXTreeItem<T> item;

        public NavTreeCell(AbstractMFXTreeItem<T> item) {
            super(item);
            this.initialize();
            this.item = item;
            getStyleClass().add("nav-tree-cell");
        }

        private void initialize() {
            this.getStyleClass().add("mfx-tree-cell");
            this.defaultDisclosureNode();
            this.getChildren().add(1, new Spacer());
            this.getChildren().add(2, this.getDisclosureNode());
            this.disclosureNode.addListener((observable, oldValue, newValue) -> {
                if (!newValue.equals(oldValue)) {
                    this.getChildren().set(1, new Spacer());
                    this.getChildren().set(2, (Node) newValue);
                }

            });
            this.sceneBuilderIntegration();
        }

        @Override
        protected void defaultDisclosureNode() {
            MFXIconWrapper disclosureNode = (new MFXIconWrapper()).defaultRippleGeneratorBehavior();
            disclosureNode.getStyleClass().setAll("disclosure-node");
            disclosureNode.setSize(22.0);
            NodeUtils.makeRegionCircular(disclosureNode, 9.5);
            this.setDisclosureNode(disclosureNode);
        }

        @Override
        public Node getDisclosureNode() {
            return (MFXIconWrapper) this.disclosureNode.get();
        }

        @Override
        public <N extends Node> void setDisclosureNode(N node) {
            this.disclosureNode.set(node);
        }

        @Override
        protected void render(T data) {

            Label titleLabel = new Label();
            titleLabel.getStyleClass().add("title");

            Label tagLabel = new Label("");
            tagLabel.getStyleClass().add("tag");

            HBox root = new HBox();
            root.setAlignment(Pos.CENTER_LEFT);
            root.setPadding(new Insets(20));
            root.getChildren().setAll(new Spacer(), titleLabel, new Spacer(), tagLabel, new Spacer());
//            root.setCursor(Cursor.HAND);
            root.getStyleClass().add("container");
            root.setMaxWidth(SIDEBAR_WIDTH - 10);
            setPrefWidth(root.getPrefWidth());


            root.setPrefHeight(getPrefHeight());

            setOnMouseClicked(
                    e -> {
                        if (!(item instanceof NavTreeItem navTreeItem)) {
                            return;
                        }

                        if (navTreeItem.isGroup() && e.getButton() == MouseButton.PRIMARY) {
                            navTreeItem.setExpanded(!navTreeItem.isExpanded());
                        }
                        if (!navTreeItem.isGroup()) {
                            navigation.navigate(navTreeItem.view());
                        }
                        if (navTreeItem.isGroup()) {
                            navTreeItem.expandedProperty().addListener(expandedListener);
                        }
                    });
            setCursor(Cursor.HAND);

            if (data instanceof Node) {
                this.getChildren().add((Node) data);
            } else {
                var nav = (Nav) data;
                titleLabel.setGraphic(nav.graphic());
                titleLabel.setText(nav.title());
                titleLabel.getStyleClass().add("data-label");
                this.getChildren().add(root);
                pseudoClassStateChanged(GROUP, nav.isGroup());
            }
        }

        @Override
        public void updateCell(MFXTreeItem<T> item) {
            MFXIconWrapper disclosureNode = (MFXIconWrapper) this.getDisclosureNode();
            if (!item.getItems().isEmpty()) {
                MFXFontIcon icon = new MFXFontIcon();
                icon.getStyleClass().add("disclosure-icon");
                disclosureNode.setIcon(icon);
            } else {
                disclosureNode.removeIcon();
            }

            if (item.isStartExpanded()) {
                disclosureNode.setRotate(90.0);
            }
        }

        @Override
        public Theme getTheme() {
            return MaterialFXStylesheets.TREE_CELL;
        }
    }

    public static final class NavTreeItem extends MFXTreeItem<Nav> {
        private final Nav nav;

        public NavTreeItem(Nav nav) {
            super(nav);
            this.nav = Objects.requireNonNull(nav, "nav");
            setCellFactory(p -> new NavTreeCell<>(this));
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

        public boolean isMainPage() {
            return nav.isMainPage();
        }
    }
}
