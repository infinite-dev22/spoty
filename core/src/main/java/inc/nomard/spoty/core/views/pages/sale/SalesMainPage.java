package inc.nomard.spoty.core.views.pages.sale;

import inc.nomard.spoty.core.views.pages.sale.tabs.*;
import inc.nomard.spoty.core.views.util.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class SalesMainPage extends OutlinePage {
    public SalesMainPage() {
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
        return new Tab("Sales", new OrderPage());
    }

    private Tab buildReturnsTab() {
        return new Tab("Returns", new SaleReturnPage());
    }
}
