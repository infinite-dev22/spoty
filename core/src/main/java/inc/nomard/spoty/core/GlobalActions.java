package inc.nomard.spoty.core;

import javafx.event.*;
import javafx.scene.*;
import javafx.stage.*;
import lombok.extern.java.*;

@Log
public class GlobalActions {
    public static void closeDialog(ActionEvent e) {
        final var source = (Node) e.getSource();
        final var stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
