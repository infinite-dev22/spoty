package inc.nomard.spoty.core.views.layout;

import inc.nomard.spoty.utils.UIUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.ArrayList;

public class WindowHeader extends HBox {
    private HBox left;
    private HBox center;
    private HBox right;

    public WindowHeader() {
        init();
    }

    private void init() {
        HBox.setHgrow(this, Priority.ALWAYS);
        this.setSpacing(10d);
        UIUtils.anchor(this, 0d, 0d, null, 0d);
        this.getStyleClass().add("appBar");
        this.getChildren().addAll(buildLeft(), buildCenter(), buildRight());
    }

    private HBox buildLeft() {
        left = new HBox();
        left.setSpacing(10d);
        HBox.setHgrow(left, Priority.ALWAYS);
        left.setAlignment(Pos.CENTER_LEFT);
        left.setPadding(new Insets(5d));
        return left;
    }

    private HBox buildCenter() {
        center = new HBox();
        center.setSpacing(10d);
        HBox.setHgrow(center, Priority.ALWAYS);
        center.setAlignment(Pos.CENTER);
        center.setPadding(new Insets(5d));
        return center;
    }

    private HBox buildRight() {
        right = new HBox();
        right.setSpacing(10d);
        HBox.setHgrow(right, Priority.ALWAYS);
        right.setAlignment(Pos.CENTER_RIGHT);
        right.setPadding(new Insets(5d));
        return right;
    }

    public void addLeftNode(Node node) {
        left.getChildren().add(node);
    }

    public void addCenterNode(Node node) {
        center.getChildren().add(node);
    }

    public void addRightNode(Node node) {
        right.getChildren().add(node);
    }

    public void addLeftNodes(Node... nodes) {
        left.getChildren().addAll(nodes);
    }

    public void addCenterNodes(Node... nodes) {
        center.getChildren().addAll(nodes);
    }

    public void addRightNodes(Node... nodes) {
        right.getChildren().addAll(nodes);
    }

    public void addLeftNodes(ArrayList<Node> nodes) {
        left.getChildren().addAll(nodes);
    }

    public void addCenterNodes(ArrayList<Node> nodes) {
        center.getChildren().addAll(nodes);
    }

    public void addRightNodes(ArrayList<Node> nodes) {
        right.getChildren().addAll(nodes);
    }
}
