package inc.nomard.spoty.core.components.animations;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;
import lombok.extern.log4j.Log4j2;

@Log4j2
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

    public static void rotate(Node node, Duration duration, double angle) {
        var transition = new RotateTransition(duration);
        transition.setByAngle(angle);
        transition.setNode(node);
        transition.setCycleCount(500);
        transition.playFromStart();
    }

    public static RotateTransition rotateTransition(Node node, Duration duration, double angle) {
        var transition = new RotateTransition(duration);
        transition.setByAngle(angle);
        transition.setNode(node);
//        transition.setCycleCount(500);
        transition.setCycleCount(2);
        transition.setInterpolator(Interpolator.LINEAR);
        return transition;
    }
}
