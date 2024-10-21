package inc.nomard.spoty.core.views.layout.message;

import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.message.enums.MessageDuration;
import inc.nomard.spoty.core.views.layout.message.enums.MessageVariants;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

@Setter
@Getter
@Slf4j
public class SpotyMessage extends StackPane {
    @Getter
    private static int layoutHeight;
    @Getter
    private static int layoutWidth;
    private final Duration messageDuration;

    public SpotyMessage(MessageBuilder messageBuilder) {
        this.messageDuration = messageBuilder.messageDuration;
        layoutHeight = messageBuilder.height;
        layoutWidth = messageBuilder.width;

        StackPane icon = new StackPane(messageBuilder.icon);
        icon.setAlignment(Pos.CENTER);
        icon.getStyleClass().add("message-icon");

        Label message = new Label(messageBuilder.message);
        message.getStyleClass().add("message-message");

        HBox messageLayout = new HBox();
        messageLayout.getStyleClass().addAll("message-layout", messageBuilder.styleClass);
        messageLayout.setSpacing(10);
        messageLayout.setAlignment(Pos.CENTER_LEFT);
        messageLayout.setPadding(new Insets(10, 10, 10, 10));
        messageLayout.setMinHeight(getMinHeight());
        messageLayout.setMinWidth(getMinWidth());
        messageLayout.setPrefHeight(getPrefHeight());
        messageLayout.setPrefWidth(getPrefWidth());
        messageLayout.setMaxHeight(getMaxHeight());
        messageLayout.setMaxWidth(getMaxWidth());
        messageLayout.getChildren().addAll(icon, message);

        getStyleClass().add("message");
        getChildren().addAll(messageLayout);

        StackPane.setAlignment(this, Pos.TOP_RIGHT);
        StackPane.setMargin(this, new Insets(10, 0, 0, 0));
    }

    public static void delay(SpotyMessage message) {
        Duration delay = Duration.seconds(4);

        KeyFrame keyFrame = new KeyFrame(delay, event -> {
            var out = Animations.slideOutUp(message, Duration.millis(250));
            out.playFromStart();
            out.setOnFinished(actionEvent -> AppManager.getMorphPane().getChildren().remove(message));
        });

        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }

    public void setLayoutHeight(int layoutHeight) {
        SpotyMessage.layoutHeight = layoutHeight;
    }

    public void setLayoutWidth(int layoutWidth) {
        SpotyMessage.layoutWidth = layoutWidth;
    }

    public static class MessageBuilder {
        private final String message;
        private int height = 0;
        private int width = 300;
        private FontIcon icon;
        private Duration messageDuration;
        private String styleClass;

        public MessageBuilder(String message) {
            this.message = message;
        }

        public MessageBuilder icon(Ikon icon) {
            this.icon = new FontIcon(icon);
            return this;
        }

        public MessageBuilder width(int width) {
            this.width = width;
            return this;
        }

        public MessageBuilder height(int height) {
            this.height = height;
            return this;
        }

        public MessageBuilder duration(MessageDuration duration) {
            this.messageDuration = new Duration(duration.getDuration());
            return this;
        }

        public MessageBuilder type(MessageVariants variant) {
            this.styleClass = variant.getStyleClass();
            return this;
        }

        public SpotyMessage build() {
            return new SpotyMessage(this);
        }
    }
}
