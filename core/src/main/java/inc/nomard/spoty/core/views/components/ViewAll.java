package inc.nomard.spoty.core.views.components;

import atlantafx.base.theme.Styles;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ViewAll extends VBox {
    public ViewAll() {
        Label label = new Label("View All");
        label.getStyleClass().add(Styles.ACCENT);
        getChildren().add(label);
        setCursor(Cursor.HAND);
    }
}
