package inc.nomard.spoty.core.views.layout;

import javafx.scene.layout.*;
import javafx.stage.*;
import lombok.*;

public class AppManager {
    @Getter
    @Setter
    private static Stage primaryStage;
    @Getter
    @Setter
    private static GlassPane morphPane;
}
