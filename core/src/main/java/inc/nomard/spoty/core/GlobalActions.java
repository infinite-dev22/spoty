package inc.nomard.spoty.core;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GlobalActions {
    public static void closeDialog(Event e) {
        final var source = (Node) e.getSource();
        final var stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
