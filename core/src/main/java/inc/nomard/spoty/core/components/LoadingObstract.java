package inc.nomard.spoty.core.components;

import eu.iamgio.froxty.FrostyBox;
import eu.iamgio.froxty.FrostyEffect;
import inc.nomard.spoty.core.components.animations.ActivityIndicator;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LoadingObstract extends StackPane {
    private static final double BLUR_AMOUNT = .5;

    public LoadingObstract() {
        getChildren().addAll(glassMorphicPane(), loadingIndicator());
    }

    private FrostyBox glassMorphicPane() {
        var vbox = new VBox();
        vbox.setFillWidth(true);
        var effect = new FrostyEffect(BLUR_AMOUNT, 1);
        return new FrostyBox(effect, vbox);
    }

    private VBox loadingIndicator() {
        var indicator = new ActivityIndicator();

        var vbox = new VBox(indicator);
        vbox.setAlignment(Pos.CENTER);
        vbox.setFillWidth(true);
        return vbox;
    }
}
