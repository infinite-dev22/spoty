package inc.nomard.spoty.core.views.components;

import atlantafx.base.theme.Styles;
import io.github.palexdev.mfxcore.controls.Label;
import javafx.scene.Cursor;
import javafx.scene.layout.VBox;
import lombok.extern.java.Log;

@Log
public class ViewAll extends VBox {
    public ViewAll() {
        Label label = new Label("View All");
        label.getStyleClass().add(Styles.ACCENT);
        getChildren().add(label);
        setCursor(Cursor.HAND);
    }
}
