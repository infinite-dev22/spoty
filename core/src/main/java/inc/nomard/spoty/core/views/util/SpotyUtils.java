package inc.nomard.spoty.core.views.util;


import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.message.SpotyMessage;
import inc.nomard.spoty.core.views.layout.message.enums.MessageDuration;
import inc.nomard.spoty.core.views.layout.message.enums.MessageVariants;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

public final class SpotyUtils {
    private static void displayNotification(String message, MessageVariants type, Ikon icon) {
        SpotyMessage notification = new SpotyMessage.MessageBuilder(message)
                .duration(MessageDuration.SHORT)
                .icon(icon)
                .type(type)
                .height(60)
                .build();
        notification.setMinWidth(300);
        notification.setMinHeight(60);
        notification.setPrefWidth(400);
        notification.setPrefHeight(60);
        notification.setMaxWidth(500);
        notification.setMaxHeight(60);
        StackPane.setAlignment(notification, Pos.TOP_CENTER);

        var in = Animations.slideInDown(notification, Duration.millis(250));
        if (!AppManager.getParent().getChildren().contains(notification)) {
            AppManager.getParent().getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent1 -> {
                Duration delay = Duration.seconds(4);

                KeyFrame keyFrame = new KeyFrame(delay, event -> {
                    var out = Animations.slideOutUp(notification, Duration.millis(250));
                    out.playFromStart();
                    out.setOnFinished(actionEvent2 -> AppManager.getParent().getChildren().remove(notification));
                });

                Timeline timeline = new Timeline(keyFrame);
                timeline.play();
            });
        }
    }

    public static void successMessage(String message) {
        displayNotification(message, MessageVariants.SUCCESS, FontAwesomeSolid.CHECK_CIRCLE);
    }

    public static void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, FontAwesomeSolid.EXCLAMATION_TRIANGLE);
    }
}