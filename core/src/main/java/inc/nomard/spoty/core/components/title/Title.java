package inc.nomard.spoty.core.components.title;

import inc.nomard.spoty.core.SpotyCoreResourceLoader;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
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

    public Title(String label) {
        this.label.setText(label);
        FXMLLoader fxmlLoader = SpotyCoreResourceLoader.fxmlLoader("components/title/Title.fxml");
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            log.error(ex.getLocalizedMessage(), ex);
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
