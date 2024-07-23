package inc.nomard.spoty.utils;


import java.time.*;
import java.util.*;
import javafx.scene.*;
import javafx.scene.layout.*;

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
