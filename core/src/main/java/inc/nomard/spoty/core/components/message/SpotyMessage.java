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

package inc.nomard.spoty.core.components.message;

import inc.nomard.spoty.core.components.message.enums.*;
import io.github.palexdev.mfxresources.fonts.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.*;

@Setter
@Getter
public class SpotyMessage extends StackPane {
    @Getter
    private static int layoutHeight;
    @Getter
    private static int layoutWidth;
    private Duration messageDuration;

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
