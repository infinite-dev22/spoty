package inc.nomard.spoty.core.components;

import io.github.palexdev.mfxcore.controls.Label;
import javafx.scene.Cursor;
import javafx.scene.layout.VBox;

public class ViewAll extends VBox {
    public ViewAll() {
        Label label = new Label("View All");
        label.getStyleClass().add("view-all");
        getChildren().add(label);
        setCursor(Cursor.HAND);
    }
}
