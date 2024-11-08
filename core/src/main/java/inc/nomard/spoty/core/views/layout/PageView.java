package inc.nomard.spoty.core.views.layout;

import inc.nomard.spoty.utils.UIUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.util.Optional;
import java.util.function.Predicate;

public class PageView extends StackPane {
    public PageView() {
        init();
    }

    private void init() {
        this.setPrefHeight(604d);
        this.setPrefWidth(1053d);
        this.getStyleClass().addAll("content-view");
        this.setPadding(new Insets(5d));
        UIUtils.anchor(this, 45d, 0d, 0d, 0d);
    }

    protected void add(Node node) {
        if (!this.getChildren().contains(node)) {
            this.getChildren().add(node);
        }
    }

    protected void remove(Node node) {
        this.getChildren().remove(node);
    }

    protected Node get(Integer index) {
        return this.getChildren().get(index);
    }

    protected Boolean isEmpty() {
        return this.getChildren().isEmpty();
    }

    protected Optional<Node> filterChildren(Predicate<? super Node> predicate) {
        return this.getChildren().stream().findAny().filter(predicate);
    }
}
