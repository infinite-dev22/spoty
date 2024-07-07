package inc.nomard.spoty.core.views.layout;

import inc.nomard.spoty.core.views.layout.navigation.*;
import javafx.stage.*;
import lombok.*;

public class AppManager {
    @Getter
    @Setter
    private static Stage primaryStage;
    @Getter
    @Setter
    private static GlassPane morphPane;
    @Getter
    @Setter
    private static Navigation navigation;
}
