package inc.nomard.spoty.core.views.components;

import io.github.palexdev.materialfx.controls.*;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;

public class CustomButton extends HBox {
    private final Label label;
    private final MFXProgressSpinner progressSpinner;
    private final ObjectProperty<EventHandler<MouseEvent>> onAction;

    public CustomButton(String text) {
        this.onAction = new SimpleObjectProperty<>();
        this.label = new Label(text);
        this.progressSpinner = new MFXProgressSpinner();
        this.progressSpinner.setVisible(false);
        this.progressSpinner.setManaged(false);

        this.getStyleClass().add("button");

        this.getChildren().addAll(this.label, this.progressSpinner);

        this.progressSpinnerProperties();

        this.setMinHeight(40d);
        this.setPrefHeight(40d);
        this.setMaxHeight(40d);

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
        this.progressSpinner.setMinSize(30d, 30d);
        this.progressSpinner.setPrefSize(30d, 30d);
        this.progressSpinner.setMaxSize(30d, 30d);

        this.progressSpinner.setColor1(Color.WHITE);
        this.progressSpinner.setColor2(Color.WHITE);
        this.progressSpinner.setColor3(Color.WHITE);
        this.progressSpinner.setColor4(Color.WHITE);
    }

    private void setButtonEventProperty() {
        this.onMouseClickedProperty().bind(onAction);
        this.onMousePressedProperty().bind(onAction);
    }

    public void setOnAction(EventHandler<MouseEvent> actionEvent) {
        onAction.set(actionEvent);
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
