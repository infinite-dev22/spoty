/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package org.infinite.spoty.components.notification;

import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.infinite.spoty.components.notification.enums.NotificationDuration;
import org.infinite.spoty.components.notification.enums.NotificationVariants;
import org.jetbrains.annotations.NotNull;

public class SimpleNotification extends StackPane {
    private static int layoutHeight;
    private static int layoutWidth;
    private Duration notificationDuration;

    public SimpleNotification(@NotNull NotificationBuilder notificationBuilder) {
        this.notificationDuration = notificationBuilder.notificationDuration;
        layoutHeight = notificationBuilder.height;
        layoutWidth = notificationBuilder.width;

        StackPane icon = new StackPane(notificationBuilder.icon);
        icon.setAlignment(Pos.CENTER);
        icon.getStyleClass().add("notification-icon");

        Label message = new Label(notificationBuilder.message);
        message.getStyleClass().add("notification-message");

        HBox notificationLayout = new HBox();
        notificationLayout.getStyleClass().addAll("notification-layout", notificationBuilder.styleClass);
        notificationLayout.setSpacing(10);
        notificationLayout.setAlignment(Pos.CENTER_LEFT);
        notificationLayout.setPadding(new Insets(10, 10, 10, 10));
        notificationLayout.setMinHeight(layoutHeight);
        notificationLayout.setMinWidth(layoutWidth);
        notificationLayout.getChildren().addAll(icon, message);

        getStyleClass().add("notification");
        getChildren().addAll(notificationLayout);
    }

    public static int getLayoutHeight() {
        return layoutHeight;
    }

    public void setLayoutHeight(int layoutHeight) {
        SimpleNotification.layoutHeight = layoutHeight;
    }

    public static int getLayoutWidth() {
        return layoutWidth;
    }

    public void setLayoutWidth(int layoutWidth) {
        SimpleNotification.layoutWidth = layoutWidth;
    }

    public Duration getNotificationDuration() {
        return notificationDuration;
    }

    public void setNotificationDuration(Duration notificationDuration) {
        this.notificationDuration = notificationDuration;
    }

    public static class NotificationBuilder {
        private final String message;
        private int height = 0;
        private int width = 300;
        private MFXFontIcon icon;
        private Duration notificationDuration;
        private String styleClass;

        public NotificationBuilder(String message) {
            this.message = message;
        }

        public NotificationBuilder icon(String icon) {
            this.icon = new MFXFontIcon(icon);
            return this;
        }

        public NotificationBuilder width(int width) {
            this.width = width;
            return this;
        }

        public NotificationBuilder height(int height) {
            this.height = height;
            return this;
        }

        public NotificationBuilder duration(NotificationDuration duration) {
            this.notificationDuration = new Duration(duration.getDuration());
            return this;
        }

        public NotificationBuilder type(NotificationVariants variant) {
            this.styleClass = variant.getStyleClass();
            return this;
        }

        public SimpleNotification build() {
            return new SimpleNotification(this);
        }
    }
}
