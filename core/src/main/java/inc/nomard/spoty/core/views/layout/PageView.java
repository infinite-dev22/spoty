package inc.nomard.spoty.core.views.layout;

import inc.nomard.spoty.utils.*;
import java.util.*;
import java.util.function.*;
import javafx.scene.*;
import javafx.scene.layout.*;

public class PageView extends StackPane {
    private void init() {
        this.setPrefHeight(604d);
        this.setPrefWidth(1053d);
        UIUtils.anchor(this, 55d, 5d, 5d, 5d);
    }

    protected void add(Node node) {
        this.getChildren().add(node);
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
