package inc.nomard.spoty.core.views.settings;

import atlantafx.base.theme.*;
import inc.nomard.spoty.core.views.settings.app_settings.*;
import inc.nomard.spoty.core.views.util.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import lombok.extern.java.*;

@Log
public class AppSettingPage extends OutlinePage {
    public AppSettingPage() {
        addNode(init());
    }

    private BorderPane init() {
        BorderPane pane = new BorderPane();
        pane.setPrefSize(600, 400);
        // Set center pane to border pane
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
//        tabbedPane.getStyleClass().add(Styles.TABS_FLOATING);
//        tabbedPane.getStyleClass().add(Styles.TABS_CLASSIC);
        tabbedPane.setMinWidth(450);
        // Add tabs to tab pane
        tabbedPane.getTabs().addAll(buildEmailTab(), buildAppearanceTab(), buildPrintingTab(), buildReceiptsTab());
        // Add tab pane to center pane
        NodeUtils.setAnchors(tabbedPane, new Insets(0d));
        return tabbedPane;
    }

    private Tab buildEmailTab() {
        // Email Tab
        EmailPage emailPage = new EmailPage();
        AnchorPane tabContent = new AnchorPane();
        tabContent.getStyleClass().add("rounded");
        tabContent.getChildren().add(emailPage);
        Tab tab = new Tab("Email", tabContent);
        NodeUtils.setAnchors(emailPage, new Insets(0d));
        return tab;
    }

    private Tab buildAppearanceTab() {
        // Appearance & Language Tab
        AnchorPane tabContent = new AnchorPane();
        tabContent.getStyleClass().add("rounded");
        Appearance appearance = new Appearance();
        tabContent.getChildren().add(appearance);
        Tab tab = new Tab("Appearance & Language", tabContent);
        NodeUtils.setAnchors(appearance, new Insets(0d));
        return tab;
    }

    private Tab buildPrintingTab() {
        // Printers & Printing Tab
        Tab tab = new Tab("Printers & Printing");
        AnchorPane tabContent = new AnchorPane();
        tabContent.getStyleClass().add("rounded");
        tab.setContent(tabContent);
        return tab;
    }

    private Tab buildReceiptsTab() {
        // Reports & Receipts Tab
        Tab tab = new Tab("Reports & Receipts");
        AnchorPane tabContent = new AnchorPane();
        tabContent.getStyleClass().add("rounded");
        tab.setContent(tabContent);
        return tab;
    }
}
