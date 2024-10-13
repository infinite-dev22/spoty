package inc.nomard.spoty.core.views.pages.leave;

import inc.nomard.spoty.core.views.pages.leave.tabs.CalendarPage;
import inc.nomard.spoty.core.views.pages.leave.tabs.TablePage;
import inc.nomard.spoty.core.views.util.NodeUtils;
import inc.nomard.spoty.core.views.util.OutlinePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class LeaveMainPage extends OutlinePage {
    public LeaveMainPage() {
        addNode(init());
    }

    private BorderPane init() {
        BorderPane pane = new BorderPane();
        pane.setPrefSize(600, 400);
        pane.setCenter(buildCenter());
        return pane;
    }

    private AnchorPane buildCenter() {
        AnchorPane pane = new AnchorPane();
        BorderPane.setAlignment(pane, Pos.CENTER);
        pane.getChildren().add(buildTabPane());
        return pane;
    }

    private TabPane buildTabPane() {

        TabPane tabbedPane = new TabPane();
        tabbedPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabbedPane.setMinWidth(450);
        tabbedPane.getTabs().addAll(buildPurchasesTab(), buildReturnsTab());
        NodeUtils.setAnchors(tabbedPane, new Insets(0d));
        return tabbedPane;
    }

    private Tab buildPurchasesTab() {
        return new Tab("Calendar", new CalendarPage());
    }

    private Tab buildReturnsTab() {
        return new Tab("Table", new TablePage());
    }
}
