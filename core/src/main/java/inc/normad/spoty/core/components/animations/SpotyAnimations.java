package inc.normad.spoty.core.components.animations;

import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class SpotyAnimations {
    public static void pulsate(Node node, Duration duration, double size, int cycleCount) {
        var transition = new ScaleTransition(duration);
        transition.setByX(size);
        transition.setByY(size);
        transition.setCycleCount(cycleCount);
        transition.setAutoReverse(true);
        transition.setNode(node);
        transition.playFromStart();
    }
}
