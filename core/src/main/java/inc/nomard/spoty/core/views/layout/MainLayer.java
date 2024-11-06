package inc.nomard.spoty.core.views.layout;

import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.values.PreloadedData;
import inc.nomard.spoty.core.views.layout.navigation.Navigation;
import inc.nomard.spoty.core.views.layout.navigation.SideBar;
import inc.nomard.spoty.core.views.util.NodeUtils;
import inc.nomard.spoty.core.views.util.Page;
import inc.nomard.spoty.network_bridge.auth.ProtectedGlobals;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Objects;

@Slf4j
public class MainLayer extends BorderPane {
    private final PageView subLayerPane = new PageView();  // Handles pages
    private final WindowHeader windowHeader = new WindowHeader();  // Top header section
    private final Navigation navigation;
    private final SideBar sidebar;

    public MainLayer() {
        navigation = new Navigation();  // Initialize navigation object
        AppManager.setNavigation(navigation);  // Set navigation globally
        sidebar = new SideBar(navigation);  // Initialize sidebar with navigation
        createView();  // Build the UI layout
        initListeners();  // Setup navigation listeners
        navigation.navigate(Navigation.DEFAULT_PAGE);  // Start at default page
    }

    private AnchorPane buildCenter() {
        var pane = new AnchorPane();  // Main content area
        BorderPane.setMargin(pane, new Insets(0d, 0d, 0d, 5d));
        pane.getChildren().addAll(windowHeader, subLayerPane);  // Add header and page container
        return pane;
    }

    private void initAppBar() {
        var notificationsBtn = new Button(null, new FontIcon(FontAwesomeRegular.BELL));
        var feedbackBtn = new Button(null, new FontIcon(FontAwesomeRegular.COMMENT));
        var helpBtn = new Button(null, new FontIcon(FontAwesomeRegular.QUESTION_CIRCLE));

        // Styling the buttons
        notificationsBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.FLAT);
        feedbackBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.FLAT);
        helpBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.FLAT);

        // Add tooltips for buttons
        notificationsBtn.setTooltip(new Tooltip("Notification"));
        feedbackBtn.setTooltip(new Tooltip("Feedback"));
        helpBtn.setTooltip(new Tooltip("Help"));

        // Add the buttons to the window header
        windowHeader.addCenterNode(notificationsBtn);
        windowHeader.addRightNodes(feedbackBtn, helpBtn);
    }

    private void loadPage(Class<? extends Page> pageClass) {
        try {
            final Page prevPage = (Page) subLayerPane.getChildren().stream()
                    .filter(c -> c instanceof Page)
                    .findFirst()
                    .orElse(null);  // Get the current page if any
            final Page nextPage = pageClass.getDeclaredConstructor().newInstance();  // Create new instance of the next page

            // If this is the first page load, no transition needed
            if (getScene() == null) {
                subLayerPane.add(nextPage.getView());
                return;
            }

            // Reset the previous page if one exists
            Objects.requireNonNull(prevPage);
            prevPage.reset();

            // Add the new page with a fade-in animation
            subLayerPane.add(nextPage.getView());
            subLayerPane.remove(prevPage.getView());  // Remove the old page
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            throw new RuntimeException(e);  // Catch any reflection exceptions and rethrow
        }
    }

    private void createView() {
        NodeUtils.setAnchors(this, new Insets(0d));  // Anchor this BorderPane to its parent
        this.setLeft(sidebar);  // Set sidebar to the left
        this.setCenter(buildCenter());  // Set the center area
        initAppBar();  // Initialize the app bar (header buttons)
        initActions();  // Initialize user actions for sidebar
        sidebar.setProfileImage((Objects.nonNull(ProtectedGlobals.user.getAvatar())
                && !ProtectedGlobals.user.getAvatar().isEmpty()
                && !ProtectedGlobals.user.getAvatar().isBlank()) ? ProtectedGlobals.user.getAvatar()
                : PreloadedData.userPlaceholderImage.getUrl());  // Set user's profile image
        sidebar.setUsername(ProtectedGlobals.user.getName());  // Set the username
        sidebar.setDesignation(ProtectedGlobals.role.getLabel());  // Set user's designation (role)
    }

    private void initListeners() {
        navigation.selectedPageProperty().addListener((_, _, val) -> {
            if (val != null) {
                loadPage(val);  // Load the page when navigation changes
            }
        });
    }

    private void initActions() {
        sidebar.setViewProfileOnAction(() -> {
            // Example action for viewing profile
            // navigation.navigate(Pages.getUserProfilePane());
        });
        sidebar.setLogOutOnAction(() -> {
            // Example action for logging out
        });
    }
}
