package inc.nomard.spoty.utils;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

public class UIUtils {
    public static void anchor(Node node, Double top, Double right, Double bottom, Double left) {
        if (Objects.nonNull(top)) {
            AnchorPane.setTopAnchor(node, top);
        }
        if (Objects.nonNull(bottom)) {
            AnchorPane.setBottomAnchor(node, bottom);
        }
        if (Objects.nonNull(right)) {
            AnchorPane.setRightAnchor(node, right);
        }
        if (Objects.nonNull(left)) {
            AnchorPane.setLeftAnchor(node, left);
        }
    }
}
