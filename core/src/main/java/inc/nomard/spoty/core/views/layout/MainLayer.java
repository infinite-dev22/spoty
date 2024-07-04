package inc.nomard.spoty.core.views.layout;

import inc.nomard.spoty.core.*;
import inc.nomard.spoty.core.values.*;
import inc.nomard.spoty.core.views.layout.navigation.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.auth.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.mfxresources.fonts.*;
import java.util.*;
import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.util.*;

public class MainLayer extends BorderPane {
    static final int PAGE_TRANSITION_DURATION = 500;
    private final PageView subLayerPane = new PageView();
    private final WindowHeader windowHeader = new WindowHeader();
    private final Navigation navigation;
    private final SideBar sidebar;

    public MainLayer() {
        navigation = new Navigation();
        sidebar = new SideBar(navigation);
        createView();
        initListeners();
        navigation.navigate(Navigation.DEFAULT_PAGE);
    }

    private AnchorPane buildCenter() {
        var pane = new AnchorPane();
        pane.getStyleClass().add("card");
        BorderPane.setMargin(pane, new Insets(5d, 5d, 5d, 10d));
        pane.getChildren().addAll(windowHeader, subLayerPane);
        return pane;
    }

    private void initAppBar() {
        var notificationIcon = new MFXFontIcon();
        var feedbackIcon = new MFXFontIcon();
        var helpIcon = new MFXFontIcon();

        notificationIcon.setIconsProvider(IconsProviders.FONTAWESOME_REGULAR);
        feedbackIcon.setIconsProvider(IconsProviders.FONTAWESOME_REGULAR);
        helpIcon.setIconsProvider(IconsProviders.FONTAWESOME_REGULAR);

        notificationIcon.setDescription("far-bell");
        feedbackIcon.setDescription("far-comment");
        helpIcon.setDescription("far-circle-question");

        MFXButton notificationsBtn = new MFXButton();
        MFXButton feedbackBtn = new MFXButton("Feedback");
        MFXButton helpBtn = new MFXButton("Help");

        notificationsBtn.setText("");
        notificationsBtn.setGraphic(notificationIcon);
        feedbackBtn.setGraphic(feedbackIcon);
        helpBtn.setGraphic(helpIcon);
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
        NodeUtils.setAnchors(this, new Insets(2d));
        this.setLeft(sidebar);
        this.setCenter(buildCenter());
        initAppBar();
        initActions();
        sidebar.setProfileImage(getUserProfileImage());
        sidebar.setUsername(ProtectedGlobals.user.getName());
        sidebar.setDesignation(ProtectedGlobals.user.getRole().getLabel());
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
