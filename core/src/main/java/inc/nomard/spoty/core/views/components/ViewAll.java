package inc.nomard.spoty.core.views.components;

import atlantafx.base.theme.*;
import io.github.palexdev.mfxcore.controls.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import lombok.extern.java.*;

@Log
public class ViewAll extends VBox {
    public ViewAll() {
        Label label = new Label("View All");
        label.getStyleClass().add(Styles.ACCENT);
        getChildren().add(label);
        setCursor(Cursor.HAND);
    }
}
