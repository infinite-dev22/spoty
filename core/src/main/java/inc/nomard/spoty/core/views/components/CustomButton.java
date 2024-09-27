package inc.nomard.spoty.core.views.components;

import atlantafx.base.controls.RingProgressIndicator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class CustomButton extends HBox {
    private final Label label;
    private final RingProgressIndicator progressSpinner;
    private final ObjectProperty<EventHandler<MouseEvent>> onAction;

    public CustomButton(String text) {
        this.onAction = new SimpleObjectProperty<>();
        this.label = new Label(text);
        this.progressSpinner = new RingProgressIndicator();
        this.progressSpinner.setVisible(false);
        this.progressSpinner.setManaged(false);

        this.getStyleClass().add("button");
        label.setStyle("-fx-text-fill: white;");

        this.getChildren().addAll(this.label, this.progressSpinner);

        this.progressSpinnerProperties();

//        this.setMinHeight(40d);
//        this.setPrefHeight(40d);
//        this.setMaxHeight(40d);

        HBox.setHgrow(this, Priority.NEVER);
        VBox.setVgrow(this, Priority.NEVER);
        setButtonEventProperty();
    }

    public void stopLoading() {
        this.label.setVisible(true);
        this.label.setManaged(true);
        this.progressSpinner.setVisible(false);
        this.progressSpinner.setManaged(false);
    }

    public void startLoading() {
        this.label.setVisible(false);
        this.label.setManaged(false);
        this.progressSpinner.setVisible(true);
        this.progressSpinner.setManaged(true);
    }

    private void progressSpinnerProperties() {
        this.progressSpinner.setMinSize(22d, 22d);
        this.progressSpinner.setPrefSize(22d, 22d);
        this.progressSpinner.setMaxSize(22d, 22d);
        this.progressSpinner.getStyleClass().add("button_busy");
    }

    public final StringProperty textProperty() {
        return this.label.textProperty();
    }

    public final String getText() {
        return this.textProperty().getValue();
    }

    public final void setText(String var1) {
        this.textProperty().setValue(var1);
    }

    private void setButtonEventProperty() {
        this.onMouseClickedProperty().bind(onAction);
    }

    public void setOnAction(EventHandler<MouseEvent> actionEvent) {
        if (!this.progressSpinner.isVisible() && !this.progressSpinner.isManaged()) {
            onAction.set(actionEvent);
        }
    }

    public void fire() {
        if (!this.isDisabled()) {// Manually fire the MouseEvent
            MouseEvent mouseClickedEvent = new MouseEvent(
                    MouseEvent.MOUSE_CLICKED,  // event type
                    0, 0, 0, 0,                // position and screen coordinates
                    MouseButton.PRIMARY,        // primary button (left mouse button)
                    1,                         // click count
                    false, false, false, false, // modifier keys (shift, ctrl, alt, meta)
                    true,                      // primary button down
                    false, false, false, false, false, null // other parameters
            );
            Event.fireEvent(this, mouseClickedEvent);
        }
    }
}
