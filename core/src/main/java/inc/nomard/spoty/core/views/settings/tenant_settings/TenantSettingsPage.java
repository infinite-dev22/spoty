package inc.nomard.spoty.core.views.settings.tenant_settings;

import inc.nomard.spoty.core.views.settings.tenant_settings.tabs.CompanyDetails;
import inc.nomard.spoty.core.views.settings.tenant_settings.tabs.CompanySettings;
import inc.nomard.spoty.core.views.util.NodeUtils;
import inc.nomard.spoty.core.views.util.OutlinePage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class TenantSettingsPage extends OutlinePage {
    public TenantSettingsPage() {
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
        tabbedPane.getTabs().addAll(buildDetailsTab(), buildDefaultsTab());
        NodeUtils.setAnchors(tabbedPane, new Insets(0d));
        return tabbedPane;
    }

    private Tab buildDetailsTab() {
        return new Tab("Company Details", new CompanyDetails());
    }

    private Tab buildDefaultsTab() {
        return new Tab("Company Defaults", new CompanySettings());
    }
}
