package inc.nomard.spoty.core.views.layout;

import atlantafx.base.controls.ModalPane;
import inc.nomard.spoty.core.views.layout.navigation.Navigation;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public class AppManager {
    @Getter
    @Setter
    private static Stage primaryStage;
    @Getter
    @Setter
    private static AnchorPane morphPane;
    @Getter
    @Setter
    private static Navigation navigation;
    @Getter
    @Setter
    private static Pane parent;
    @Getter
    @Setter
    private static Scene scene;
    @Getter
    @Setter
    private static ModalPane globalModalPane;
}
