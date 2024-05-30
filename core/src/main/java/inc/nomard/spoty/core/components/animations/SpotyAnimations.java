package inc.nomard.spoty.core.components.animations;

import javafx.animation.*;
import javafx.scene.*;
import javafx.util.*;

import lombok.extern.java.Log;

@Log
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
