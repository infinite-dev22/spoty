package inc.normad.spoty.core.components.animations;

import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ActivityIndicator extends HBox {
    private Color color;

    public ActivityIndicator() {
        var circle1 = new Circle(175, 100, 10);
        var circle2 = new Circle(175, 100, 15);
        var circle3 = new Circle(175, 100, 10);

        var pane1 = new BorderPane(circle1);
        var pane2 = new BorderPane(circle2);
        var pane3 = new BorderPane(circle3);

        setAlignment(Pos.CENTER);
        getChildren().addAll(pane1, pane2, pane3);
        setMaxHeight(USE_COMPUTED_SIZE);
        setMaxWidth(USE_COMPUTED_SIZE);
        setSpacing(10);
//        setStyle("-fx-background-color: red;");

        circle1.setFill(Color.PURPLE);
        circle2.setFill(Color.PURPLE);
        circle3.setFill(Color.PURPLE);

        pane1.setMaxSize(40, 40);
        pane2.setMaxSize(40, 40);
        pane3.setMaxSize(40, 40);

        SpotyAnimations.pulsate(circle1, Duration.millis(500), .4, Timeline.INDEFINITE);
        SpotyAnimations.pulsate(circle2, Duration.millis(500), -.4, Timeline.INDEFINITE);
        SpotyAnimations.pulsate(circle3, Duration.millis(500), .4, Timeline.INDEFINITE);
    }

    public ActivityIndicator(@Nullable Color color) {
        var circle1 = new Circle(175, 100, 10);
        var circle2 = new Circle(175, 100, 15);
        var circle3 = new Circle(175, 100, 10);

        var pane1 = new BorderPane(circle1);
        var pane2 = new BorderPane(circle2);
        var pane3 = new BorderPane(circle3);

        setAlignment(Pos.CENTER);
        getChildren().addAll(pane1, pane2, pane3);
        setMaxHeight(USE_COMPUTED_SIZE);
        setMaxWidth(USE_COMPUTED_SIZE);

        if (Objects.isNull(color)) {
            color = Color.WHITE;
        }

        circle1.setFill(color);
        circle2.setFill(color);
        circle3.setFill(color);

        pane1.setMaxSize(35, 35);
        pane2.setMaxSize(35, 35);
        pane3.setMaxSize(35, 35);

        SpotyAnimations.pulsate(circle1, Duration.millis(500), .4, Timeline.INDEFINITE);
        SpotyAnimations.pulsate(circle2, Duration.millis(500), -.4, Timeline.INDEFINITE);
        SpotyAnimations.pulsate(circle3, Duration.millis(500), .4, Timeline.INDEFINITE);
    }

    public ActivityIndicator(double radius) {
        var circle1 = new Circle(175, 100, radius);
        var circle2 = new Circle(175, 100, radius + 5);
        var circle3 = new Circle(175, 100, radius);

        var pane1 = new BorderPane(circle1);
        var pane2 = new BorderPane(circle2);
        var pane3 = new BorderPane(circle3);

        setAlignment(Pos.CENTER);
        getChildren().addAll(pane1, pane2, pane3);
        setMaxHeight(USE_COMPUTED_SIZE);
        setMaxWidth(USE_COMPUTED_SIZE);

        if (Objects.isNull(color)) {
            color = Color.WHITE;
        }

        circle1.setFill(color);
        circle2.setFill(color);
        circle3.setFill(color);

        pane1.setMaxSize(35, 35);
        pane2.setMaxSize(35, 35);
        pane3.setMaxSize(35, 35);

        SpotyAnimations.pulsate(circle1, Duration.millis(500), .4, Timeline.INDEFINITE);
        SpotyAnimations.pulsate(circle2, Duration.millis(500), -.4, Timeline.INDEFINITE);
        SpotyAnimations.pulsate(circle3, Duration.millis(500), .4, Timeline.INDEFINITE);
    }

    public ActivityIndicator(@Nullable Color color, double radius) {
        var circle1 = new Circle(175, 100, radius);
        var circle2 = new Circle(175, 100, radius + 5);
        var circle3 = new Circle(175, 100, radius);

        var pane1 = new BorderPane(circle1);
        var pane2 = new BorderPane(circle2);
        var pane3 = new BorderPane(circle3);

        setAlignment(Pos.CENTER);
        getChildren().addAll(pane1, pane2, pane3);
        setMaxHeight(USE_COMPUTED_SIZE);
        setMaxWidth(USE_COMPUTED_SIZE);

        if (Objects.isNull(color)) {
            color = Color.WHITE;
        }

        circle1.setFill(color);
        circle2.setFill(color);
        circle3.setFill(color);

        pane1.setMaxSize(35, 35);
        pane2.setMaxSize(35, 35);
        pane3.setMaxSize(35, 35);

        SpotyAnimations.pulsate(circle1, Duration.millis(500), .4, Timeline.INDEFINITE);
        SpotyAnimations.pulsate(circle2, Duration.millis(500), -.4, Timeline.INDEFINITE);
        SpotyAnimations.pulsate(circle3, Duration.millis(500), .4, Timeline.INDEFINITE);
    }

    public void setActivityColor(Color color) {
        this.color = color;
    }
}
