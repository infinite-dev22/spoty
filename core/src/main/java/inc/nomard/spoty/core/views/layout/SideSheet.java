package inc.nomard.spoty.core.views.layout;

import inc.nomard.spoty.utils.navigation.Spacer;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXIconButton;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

public class SideSheet extends StackPane {
    private final Pane owner;
    private final Pane dialog;
    private final StackPane overlay;
    @Getter
    private final VBox content;
    @Setter
    private Color backgroundColor = Color.WHITE;
    private CornerRadii cornerRadius = CornerRadii.EMPTY;
    private Insets insets = Insets.EMPTY;
    @Setter
    private int durationMillis = 300;
    @Setter
    private String title = "";

    public SideSheet(Pane owner) {
        this.owner = owner;
        content = new VBox();
        owner.getChildren().add(this);
        // Initialize dialog and overlay
        dialog = createModalSheet();
        overlay = createOverlay();
        HBox.setHgrow(content, Priority.ALWAYS);
        VBox.setVgrow(content, Priority.ALWAYS);

        setWidth(owner.getWidth());
        setAlignment(Pos.CENTER_LEFT);

        // Add dialog and overlay to the StackPane
        getChildren().addAll(overlay, createModalPane(dialog));
    }

    private VBox createModalSheet() {
        var titlePane = new Label(title);
        titlePane.getStyleClass().add("h1");
        var closeBtn = new MFXIconButton(new MFXFontIcon("fas-xmark"));
        closeBtn.setOnAction(event -> hide());
        var topBar = new HBox(titlePane, new Spacer(), closeBtn);
        topBar.setPadding(new Insets(0d, 2d, 0d, 2d));
        topBar.setAlignment(Pos.CENTER);
        VBox.setVgrow(topBar, Priority.NEVER);
        var vbox = new VBox(topBar, content);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setBackground(new Background(new BackgroundFill(backgroundColor, cornerRadius, insets)));
        HBox.setHgrow(vbox, Priority.NEVER);
        vbox.setPadding(new Insets(18d));

        // Bind the dialog width and height to be 30% of the parent width and 100% height
        vbox.prefWidthProperty().bind(owner.widthProperty().multiply(0.3));
        vbox.prefHeightProperty().bind(owner.heightProperty());

        // Set initial position outside the right edge of the owner
        vbox.translateXProperty().bind(owner.widthProperty());

        vbox.setVisible(false);
        vbox.setManaged(false);
        return vbox;
    }

    private Pane createModalPane(Pane dialog) {
        return new Pane(dialog);
    }

    private StackPane createOverlay() {
        var pane = new StackPane();
        pane.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, .5), CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setVisible(false);
        pane.setManaged(false);
        return pane;
    }

    public void showAndWait() {
        // Unbind the translateXProperty before the animation
        dialog.translateXProperty().unbind();

        // Show the overlay with fade-in effect
        dialog.setVisible(true);
        dialog.setManaged(true);
        overlay.setVisible(true);
        overlay.setManaged(true);
        this.toFront();
        animateFade(overlay, 0, 1);

        // Calculate target X position for sliding animation
        double targetX = owner.getWidth() - dialog.getPrefWidth();
        if (targetX < 0) {
            targetX = 0;
        }

        // Create and play the sliding animation
        animateTranslateX(dialog, targetX);
    }

    public void hide() {
        // Unbind the translateXProperty before the animation
        dialog.translateXProperty().unbind();

        // Create and play the sliding out animation
        animateTranslateX(dialog, owner.getWidth()).setOnFinished(event -> {
            // Rebind the translateXProperty after the animation
            dialog.translateXProperty().bind(owner.widthProperty());

            // Hide the overlay with fade-out effect
            animateFade(overlay, 1, 0).setOnFinished(e -> {
                overlay.setVisible(false);
                overlay.setManaged(false);
                this.toBack();
            });
        });
    }

    private FadeTransition animateFade(Pane node, double fromValue, double toValue) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(durationMillis), node);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
        fadeTransition.play();
        return fadeTransition;
    }

    private Timeline animateTranslateX(Pane node, double toValue) {
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(node.translateXProperty(), toValue);
        KeyFrame kf = new KeyFrame(Duration.millis(durationMillis), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
        return timeline;
    }

    public void setCornerRadius(Double radius) {
        cornerRadius = new CornerRadii(radius);
    }

    public void setCornerRadius(Double topLeft, Double topRight, Double bottomRight, Double bottomLeft) {
        cornerRadius = new CornerRadii(topLeft, topRight, bottomRight, bottomLeft, true);
    }

    public void setInsets(Double insets) {
        this.insets = new Insets(insets);
    }

    public void setInsets(Double top, Double right, Double bottom, Double left) {
        this.insets = new Insets(top, right, bottom, left);
    }

    public void setChild(Node child) {
        content.getChildren().add(child);
    }
}
