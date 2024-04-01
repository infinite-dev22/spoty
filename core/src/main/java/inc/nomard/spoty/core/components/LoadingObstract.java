package inc.nomard.spoty.core.components;

import eu.iamgio.froxty.FrostyBox;
import eu.iamgio.froxty.FrostyEffect;
import inc.nomard.spoty.core.components.animations.ActivityIndicator;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LoadingObstract extends StackPane {
    private static final double BLUR_AMOUNT = .5;

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
        return  vbox;
    }

    public LoadingObstract() {
        getChildren().addAll(glassMorphicPane(), loadingIndicator());
    }
}
