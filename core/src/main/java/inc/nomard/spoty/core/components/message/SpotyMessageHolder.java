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

import inc.nomard.spoty.core.SpotyCoreResourceLoader;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SpotyMessageHolder {
    private static FlowPane root;
    private static Stage stage;
    private static Duration notificationAnimationTime;
    private static ObservableList<Pane> popups;

    private static SpotyMessageHolder instance;

    private SpotyMessageHolder() {
        initGraphics();
    }

    /**
     * Sets the SpotyMessage's owner stage so that when the owner stage is closed SpotyMessages will
     * be shut down as well.<br>
     * This is only needed if <code>setPopupLocation</code> is called <u>without</u> a stage
     * reference.
     *
     * @param parentStage owner of the window.
     */
    public static void setMessageOwner(Stage parentStage) {
        stage = new Stage();
        stage.initOwner(parentStage);
        stage.initStyle(StageStyle.TRANSPARENT);
        // Calculate position relative to the parent stage
        stage.setX(parentStage.getWidth() - SpotyMessage.getLayoutWidth());
        stage.setY(parentStage.getHeight() - (parentStage.getHeight() - 5));
        stage.initOwner(parentStage);
        stage.setHeight(138);
        stage.setAlwaysOnTop(true);
    }

    public static SpotyMessageHolder getInstance() {
        if (Objects.equals(instance, null))
            instance = new SpotyMessageHolder();
        return instance;
    }

    public static void initGraphics() {
        notificationAnimationTime = Duration.millis(500);
        popups = FXCollections.observableArrayList();

        root = new FlowPane(Orientation.HORIZONTAL);
        root.setPickOnBounds(false);
        root.setPrefWrapLength(70);
        root.setVgap(10);
        root.getStyleClass().add("message-holder");
        root.getStylesheets().addAll(SpotyCoreResourceLoader.load("styles/base.css"),
                SpotyCoreResourceLoader.load("styles/theming/Default.css"));

        StackPane.setAlignment(root, Pos.TOP_RIGHT);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
    }

    public void hide() {
        if (stage.isShowing()) stage.close();
    }

    public void show() {
        if (!stage.isShowing()) stage.show();
    }

    public void addMessage(SpotyMessage notification) {
        Pane POPUP = new Pane();
        POPUP.getChildren().addAll(notification);

        root.getChildren().add(0, POPUP);
        popups.add(POPUP);

        POPUP.setVisible(true);

        this.show();

        // popup fade in - first height animation from 0, next opacity from 0
        final double orgHeight = 62;
        notification.setPrefHeight(0.0);
        notification.setOpacity(0.0);
        final Timeline tlFadeIn = new Timeline();
        final KeyValue kvFadeIn = new KeyValue(notification.prefHeightProperty(), orgHeight);
        final KeyFrame kfFadeIn = new KeyFrame(notificationAnimationTime.divide(2), kvFadeIn);
        tlFadeIn.getKeyFrames().addAll(kfFadeIn);
        tlFadeIn.setOnFinished(
                fadeInAction -> {
                    final Timeline tlOpacity = new Timeline();
                    final KeyValue kvOpacity = new KeyValue(notification.opacityProperty(), 1.0);
                    final KeyFrame kfOpacity = new KeyFrame(notificationAnimationTime.divide(2), kvOpacity);

                    tlOpacity.getKeyFrames().addAll(kfOpacity);
                    tlOpacity.play();
                });
        tlFadeIn.play();
        removeMessage(notification, POPUP);
    }

    public void removeMessage(@NotNull SpotyMessage notification, Pane POPUP) {
        // popup fade out - first opacity animation to 0, next height to 0
        final Timeline tlFadeOut = new Timeline();
        final KeyValue kvOpacity = new KeyValue(POPUP.opacityProperty(), 0.0);
        final KeyFrame kfOpacity = new KeyFrame(notificationAnimationTime.divide(2), kvOpacity);

        tlFadeOut.getKeyFrames().addAll(kfOpacity);
        tlFadeOut.setDelay(notification.getMessageDuration());
        tlFadeOut.setOnFinished(
                actionEvent -> {
                    // Remove content of POPUP because of animation height performance problems
                    notification.setPrefHeight(notification.getHeight());
                    POPUP.getChildren().removeAll();
                    final KeyValue kvHeight = new KeyValue(notification.prefHeightProperty(), 0.0);
                    final KeyFrame kfHeight = new KeyFrame(notificationAnimationTime.divide(2), kvHeight);
                    Timeline tlHeight = new Timeline();
                    tlHeight.getKeyFrames().addAll(kfHeight);
                    tlHeight.setOnFinished(
                            tlHeightAction -> {
                                root.getChildren().remove(POPUP);
                                popups.remove(POPUP);
                                // Close notifications stage if no more popups are visible
                                if (popups.isEmpty()) {
                                    this.hide();
                                }
                            });
                    tlHeight.play();
                });
        tlFadeOut.play();
    }
}
