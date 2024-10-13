package inc.nomard.spoty.core.views.layout;

import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModalContentHolder extends VBox {
    private String name;

    public ModalContentHolder(double width, double height) {
        super();
        setSpacing(10);
        setMinSize(width, height);
        setMaxSize(width, height);
        getStyleClass().addAll("modal-dialog-side");
        setStyle("-fx-background-color: -color-bg-default;");
    }
}