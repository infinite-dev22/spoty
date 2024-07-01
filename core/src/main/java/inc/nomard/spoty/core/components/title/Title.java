package inc.nomard.spoty.core.components.title;

import inc.nomard.spoty.core.*;
import java.io.*;
import javafx.beans.property.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import lombok.extern.java.*;

@Log
public class Title extends HBox {
    @FXML
    private Label label;

    public Title() {
        FXMLLoader fxmlLoader = SpotyCoreResourceLoader.fxmlLoader("components/title/Title.fxml");
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getText() {
        return textProperty().get();
    }

    public void setText(String value) {
        textProperty().set(value);
    }

    public StringProperty textProperty() {
        return label.textProperty();
    }
}
