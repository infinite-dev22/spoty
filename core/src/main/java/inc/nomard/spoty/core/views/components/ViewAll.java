package inc.nomard.spoty.core.views.components;

import atlantafx.base.theme.Styles;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ViewAll extends VBox {
    public ViewAll() {
        Label label = new Label("View All");
        label.getStyleClass().add(Styles.ACCENT);
        getChildren().add(label);
        setCursor(Cursor.HAND);
    }
}
