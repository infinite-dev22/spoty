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

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.infinite.spoty.SpotyResourceLoader;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SimpleNotificationHolder {
    private static FlowPane root;
    private static Stage stage;
    private static Duration notificationAnimationTime;
    private static ObservableList<Pane> popups;

    private static SimpleNotificationHolder instance;

    private SimpleNotificationHolder() {
        initGraphics();
    }

    /**
     * Sets the Notification's owner stage so that when the owner stage is closed Notifications will
     * be shut down as well.<br>
     * This is only needed if <code>setPopupLocation</code> is called <u>without</u> a stage
     * reference.
     *
     * @param windowOwner owner of the window.
     */
    public static void setNotificationOwner(Stage windowOwner) {
        stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setX(windowOwner.getWidth() - SimpleNotification.getLayoutWidth());
        stage.setY(windowOwner.getHeight() - (windowOwner.getHeight() - 5));
        stage.setAlwaysOnTop(true);
        stage.setHeight(138);
        stage.initOwner(windowOwner);
    }

    public static SimpleNotificationHolder getInstance() {
        if (Objects.equals(instance, null)) instance = new SimpleNotificationHolder();
        return instance;
    }

    public static void initGraphics() {
        notificationAnimationTime = Duration.millis(500);
        popups = FXCollections.observableArrayList();

        root = new FlowPane(Orientation.HORIZONTAL);
        root.setPickOnBounds(false);
        root.setPrefWrapLength(70);
        root.setVgap(10);
        root.getStyleClass().add("notification-holder");
        root.getStylesheets().add(SpotyResourceLoader.load("styles/base.css"));

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

    public void addNotification(SimpleNotification notification) {
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
        removeNotification(notification, POPUP);
    }

    public void removeNotification(@NotNull SimpleNotification notification, Pane POPUP) {
        // popup fade out - first opacity animation to 0, next height to 0
        final Timeline tlFadeOut = new Timeline();
        final KeyValue kvOpacity = new KeyValue(POPUP.opacityProperty(), 0.0);
        final KeyFrame kfOpacity = new KeyFrame(notificationAnimationTime.divide(2), kvOpacity);

        tlFadeOut.getKeyFrames().addAll(kfOpacity);
        tlFadeOut.setDelay(notification.getNotificationDuration());
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
