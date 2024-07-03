package inc.nomard.spoty.core.views.layout.message;

import atlantafx.base.util.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import io.github.palexdev.mfxresources.fonts.*;
import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.*;
import lombok.extern.java.*;

@Setter
@Getter

@Log
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
        messageLayout.setMinHeight(layoutHeight);
        messageLayout.setMinWidth(layoutWidth);
        messageLayout.getChildren().addAll(icon, message);

        getStyleClass().add("message");
        getChildren().addAll(messageLayout);

        StackPane.setAlignment(this, Pos.TOP_RIGHT);
        StackPane.setMargin(this, new Insets(10, 10, 0, 0));
    }

    public static void delay(SpotyMessage message) {
        Duration delay = Duration.seconds(3);

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
        private MFXFontIcon icon;
        private Duration messageDuration;
        private String styleClass;

        public MessageBuilder(String message) {
            this.message = message;
        }

        public MessageBuilder icon(String icon) {
            this.icon = new MFXFontIcon(icon);
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
