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
import java.util.List;
import java.util.Objects;
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
import org.jetbrains.annotations.Nullable;
import org.kordamp.ikonli.javafx.FontIcon;

public class NavTree extends TreeView<Nav> {
  public static final double SIDEBAR_WIDTH = 194.0;
  /** Removes or hides dropdown arrow nav. */
  public static final String NO_ARROW = "no-arrow";

  /** Removes external control borders. */
  public static final String EDGE_TO_EDGE = "edge-to-edge";

  /** Removes control header. */
  public static final String NO_HEADER = "no-header";

  /** Alignment. */
  public static final String ALIGN_LEFT = "align-left";

  public static final String ALIGN_CENTER = "align-center";
  public static final String ALIGN_RIGHT = "align-right";
  /** Forces a control to use alternative icon, if available. */
  public static final String ALT_ICON = "alt-icon";

  static ChangeListener<Boolean> expandedListener;

  public NavTree(Navigation navigation) {
    super();
    setPrefHeight(600);

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

              if (navTreeItem.isGroup()) {
                navTreeItem.expandedProperty().addListener(expandedListener);
              }

              if (navTreeItem.isMainPage()) {
                getSelectionModel()
                    .selectedItemProperty()
                    .addListener(
                        changeListener -> {
                          ReadOnlyProperty<?> expandedProperty = (ReadOnlyProperty<?>) obs;
                          Object itemThatWasJustExpanded = expandedProperty.getBean();
                          for (TreeItem<Nav> item : getRoot().getChildren()) {
                            if (item != itemThatWasJustExpanded) {
                              item.setExpanded(false);
                            }
                          }
                        });
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
              }
            }
          }
        };

    getStyleClass().addAll(EDGE_TO_EDGE);
    setShowRoot(false);
    rootProperty().bind(navigation.navTreeProperty());
    setCellFactory(p -> new NavTreeCell());
  }

  public static void toggleVisibility(Node node, boolean on) {
    node.setVisible(on);
    node.setManaged(on);
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

    public NavTreeItem(Nav nav) {
      this.nav = Objects.requireNonNull(nav, "nav");
      setValue(nav);
    }

    public static NavTreeItem root() {
      return new NavTreeItem(Nav.ROOT);
    }

    public static NavTreeItem group(String title, String icon) {
      MFXIconWrapper wrapper = new MFXIconWrapper(icon, 24, 32);
      return new NavTreeItem(new Nav(title, wrapper, null, null));
    }

    public static NavTreeItem mainPage(String title, String icon, BorderPane view) {
      MFXIconWrapper wrapper = new MFXIconWrapper(icon, 24, 32);
      return new NavTreeItem(new Nav(title, wrapper, view, null));
    }

    public static NavTreeItem page(String title, BorderPane view) {
      return new NavTreeItem(new Nav(title, null, view, null));
    }

    public static NavTreeItem page(String title, BorderPane view, String... searchKeywords) {
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
