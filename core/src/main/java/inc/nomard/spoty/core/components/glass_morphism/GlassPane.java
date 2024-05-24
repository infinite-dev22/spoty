package inc.nomard.spoty.core.components.glass_morphism;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;

public class GlassPane extends AnchorPane {
    private boolean isGlassMorphismEnabled; // Flag to control effect

    public GlassPane() {
        isGlassMorphismEnabled = false;
    }

    public GlassPane(Node... nodes) {
        isGlassMorphismEnabled = false;
    }

    public void morph(boolean enableGlassMorphism) { // Accepts a boolean parameter
        this.isGlassMorphismEnabled = enableGlassMorphism;
        if (isGlassMorphismEnabled) {
            var color = Color.WHITE;
            var paint = Color.rgb(
                    (int) color.getRed() * 255,
                    (int) color.getGreen() * 255,
                    (int) color.getBlue() * 255, .5);

            this.setEffect(new GaussianBlur(10));
            this.setBackground(new Background(new BackgroundFill(paint, CornerRadii.EMPTY, Insets.EMPTY)));
            this.setDisable(true);
        } else {
            // Disable effect when flag is false
            this.setEffect(null); // Remove blur effect
            this.setBackground(null); // Remove background paint
            this.setDisable(false);
        }
    }
}