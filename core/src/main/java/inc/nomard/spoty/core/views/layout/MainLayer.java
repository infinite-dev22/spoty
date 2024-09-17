package inc.nomard.spoty.core.views.layout;

import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.SpotyCoreResourceLoader;
import inc.nomard.spoty.core.values.PreloadedData;
import inc.nomard.spoty.core.views.layout.navigation.Navigation;
import inc.nomard.spoty.core.views.layout.navigation.SideBar;
import inc.nomard.spoty.core.views.util.NodeUtils;
import inc.nomard.spoty.core.views.util.Page;
import inc.nomard.spoty.network_bridge.auth.ProtectedGlobals;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Objects;

public class MainLayer extends BorderPane {
    static final int PAGE_TRANSITION_DURATION = 500;
    private final PageView subLayerPane = new PageView();
    private final WindowHeader windowHeader = new WindowHeader();
    private final Navigation navigation;
    private final SideBar sidebar;

    public MainLayer() {
        navigation = new Navigation();
        AppManager.setNavigation(navigation);
        sidebar = new SideBar(navigation);
        createView();
        initListeners();
        navigation.navigate(Navigation.DEFAULT_PAGE);
    }

    private AnchorPane buildCenter() {
        var pane = new AnchorPane();
        BorderPane.setMargin(pane, new Insets(0d, 0d, 0d, 5d));
        pane.getChildren().addAll(windowHeader, subLayerPane);
        return pane;
    }

    private void initAppBar() {
        var notificationsBtn = new Button(null, new FontIcon(FontAwesomeRegular.BELL));
        var feedbackBtn = new Button(null, new FontIcon(FontAwesomeRegular.COMMENT));
        var helpBtn = new Button(null, new FontIcon(FontAwesomeRegular.QUESTION_CIRCLE));

        notificationsBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.FLAT);
        feedbackBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.FLAT);
        helpBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.FLAT);

        notificationsBtn.setTooltip(new Tooltip("Notification"));
        feedbackBtn.setTooltip(new Tooltip("Feedback"));
        helpBtn.setTooltip(new Tooltip("Help"));

        windowHeader.addCenterNode(notificationsBtn);
        windowHeader.addRightNodes(feedbackBtn, helpBtn);
    }

    private void loadPage(Class<? extends Page> pageClass) {
        try {
            final Page prevPage = (Page) subLayerPane.getChildren().stream()
                    .filter(c -> c instanceof Page)
                    .findFirst()
                    .orElse(null);
            final Page nextPage = pageClass.getDeclaredConstructor().newInstance();

            // startup, no prev page, no animation
            if (getScene() == null) {
                subLayerPane.getChildren().add(nextPage.getView());
                return;
            }

            Objects.requireNonNull(prevPage);

            // reset previous page, e.g. to free resources
            prevPage.reset();

            // animate switching between pages
            subLayerPane.getChildren().add(nextPage.getView());
            subLayerPane.getChildren().remove(prevPage.getView());
            var transition = new FadeTransition(Duration.millis(PAGE_TRANSITION_DURATION), nextPage.getView());
            transition.setFromValue(0.0);
            transition.setToValue(1.0);
            transition.setOnFinished(t -> {
                if (nextPage instanceof Pane nextPane) {
                    nextPane.toFront();
                }
            });
            transition.play();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createView() {
        NodeUtils.setAnchors(this, new Insets(0d));
        this.setLeft(sidebar);
        this.setCenter(buildCenter());
        initAppBar();
        initActions();
        sidebar.setProfileImage(getUserProfileImage());
        sidebar.setUsername(ProtectedGlobals.user.getName());
        sidebar.setDesignation(ProtectedGlobals.role.getLabel());
    }

    private Image getUserProfileImage() {
        return (Objects.nonNull(ProtectedGlobals.user)
                && Objects.nonNull(ProtectedGlobals.user.getAvatar())
                && !ProtectedGlobals.user.getAvatar().isEmpty()
                && !ProtectedGlobals.user.getAvatar().isBlank())
                ? new Image(
                SpotyCoreResourceLoader.load(ProtectedGlobals.user.getAvatar()),
                10000,
                10000,
                true,
                true)
                : PreloadedData.userPlaceholderImage;
    }

    private void initListeners() {
        navigation.selectedPageProperty().addListener((obs, old, val) -> {
            if (val != null) {
                loadPage(val);
            }
        });
    }

    private void initActions() {
        sidebar.setViewProfileOnAction(() -> {
//            navigation.navigate(Pages.getUserProfilePane());
        });
        sidebar.setLogOutOnAction(() -> {
        });
    }
}
