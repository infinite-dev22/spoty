package inc.nomard.spoty.core;

import inc.nomard.spoty.utils.*;
import java.text.*;
import java.util.*;
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

    public static Date fineDate(String dateString) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            SpotyLogger.writeToFile(e, GlobalActions.class);
        }
        return null;
    }
}
