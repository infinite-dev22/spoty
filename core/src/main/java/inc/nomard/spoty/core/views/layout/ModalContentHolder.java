package inc.nomard.spoty.core.views.layout;

import javafx.scene.layout.*;
import lombok.*;

@Setter
@Getter
public class ModalContentHolder extends VBox {
    private String name;
        public ModalContentHolder(int width, int height) {
            super();
            setSpacing(10);
            setMinSize(width, height);
            setMaxSize(width, height);
            getStyleClass().addAll("modal-dialog-side");
            setStyle("-fx-background-color: -color-bg-default;");
        }
    }