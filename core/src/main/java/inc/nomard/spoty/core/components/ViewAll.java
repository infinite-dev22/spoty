package inc.nomard.spoty.core.components;

import io.github.palexdev.mfxcore.controls.*;
import javafx.scene.*;
import javafx.scene.layout.*;

import lombok.extern.slf4j.*;

@Slf4j
public class ViewAll extends VBox {
    public ViewAll() {
        Label label = new Label("View All");
        label.getStyleClass().add("view-all");
        getChildren().add(label);
        setCursor(Cursor.HAND);
    }
}
