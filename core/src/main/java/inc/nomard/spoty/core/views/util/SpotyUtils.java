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

import java.util.Optional;

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

    public static String getHTTPStatus(Integer statusCode, String msg) {
        var message = Optional.ofNullable(msg);
        return switch (statusCode) {
            // 1xx Informational
            case 100 -> message.orElse("Continue");
            case 101 -> message.orElse("Switching Protocols");
            case 102 -> message.orElse("Processing"); // WebDAV
            case 103 -> message.orElse("Early Hints");

            // 2xx Success
            case 200 -> message.orElse("OK");
            case 201 -> message.orElse("Created");
            case 202 -> message.orElse("Accepted");
            case 203 -> message.orElse("Non-Authoritative Information");
            case 204 -> message.orElse("No Content");
            case 205 -> message.orElse("Reset Content");
            case 206 -> message.orElse("Partial Content");
            case 207 -> message.orElse("Multi-Status"); // WebDAV
            case 208 -> message.orElse("Already Reported"); // WebDAV
            case 226 -> message.orElse("IM Used"); // HTTP Delta encoding

            // 3xx Redirection
            case 300 -> message.orElse("Multiple Choices");
            case 301 -> message.orElse("Moved Permanently");
            case 302 -> message.orElse("Found");
            case 303 -> message.orElse("See Other");
            case 304 -> message.orElse("Not Modified");
            case 305 -> message.orElse("Use Proxy"); // Deprecated
            case 307 -> message.orElse("Temporary Redirect");
            case 308 -> message.orElse("Permanent Redirect");

            // 4xx Client Error
            case 400 -> message.orElse("Bad Request");
            case 401 -> message.orElse("Unauthorized");
            case 402 -> message.orElse("Payment Required"); // Reserved for future use
            case 403 -> message.orElse("Forbidden");
            case 404 -> message.orElse("Not Found");
            case 405 -> message.orElse("Method Not Allowed");
            case 406 -> message.orElse("Not Acceptable");
            case 407 -> message.orElse("Proxy Authentication Required");
            case 408 -> message.orElse("Request Timeout");
            case 409 -> message.orElse("Conflict");
            case 410 -> message.orElse("Gone");
            case 411 -> message.orElse("Length Required");
            case 412 -> message.orElse("Precondition Failed");
            case 413 -> message.orElse("Payload Too Large");
            case 414 -> message.orElse("URI Too Long");
            case 415 -> message.orElse("Unsupported Media Type");
            case 416 -> message.orElse("Range Not Satisfiable");
            case 417 -> message.orElse("Expectation Failed");
            case 418 -> message.orElse("I'm a teapot"); // Easter egg in HTTP
            case 421 -> message.orElse("Misdirected Request");
            case 422 -> message.orElse("Unprocessable Entity"); // WebDAV
            case 423 -> message.orElse("Locked"); // WebDAV
            case 424 -> message.orElse("Failed Dependency"); // WebDAV
            case 425 -> message.orElse("Too Early");
            case 426 -> message.orElse("Upgrade Required");
            case 428 -> message.orElse("Precondition Required");
            case 429 -> message.orElse("Too Many Requests");
            case 431 -> message.orElse("Request Header Fields Too Large");
            case 451 -> message.orElse("Unavailable For Legal Reasons");

            // 5xx Server Error
            case 500 -> message.orElse("Internal Server Error");
            case 501 -> message.orElse("Not Implemented");
            case 502 -> message.orElse("Bad Gateway");
            case 503 -> message.orElse("Service Unavailable");
            case 504 -> message.orElse("Gateway Timeout");
            case 505 -> message.orElse("HTTP Version Not Supported");
            case 506 -> message.orElse("Variant Also Negotiates");
            case 507 -> message.orElse("Insufficient Storage"); // WebDAV
            case 508 -> message.orElse("Loop Detected"); // WebDAV
            case 510 -> message.orElse("Not Extended");
            case 511 -> message.orElse("Network Authentication Required");

            // Default for unknown status codes
            default -> message.orElse("Unknown status code");
        };
    }
}