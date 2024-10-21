package inc.nomard.spoty.core.views.layout.navigation;

import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.utils.SpotyThreader;
import inc.nomard.spoty.utils.flavouring.AppConfig;
import inc.nomard.spoty.utils.functional_paradigm.SpotyGotFunctional;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

public class SideBar extends VBox {
    final MenuItem viewProfile = new MenuItem("View profile");
    final MenuItem logOut = new MenuItem("Log out");
    private final Stage stage = AppManager.getPrimaryStage();
    private final NavTree navTree;
    private Circle circle;
    private Label username;
    private Label designation;
    private FontIcon moreActions;
    private ContextMenu contextMenu;
    private Boolean isMinimised = false;

    public SideBar(Navigation navigation) {
        super();
        navTree = navigation.createNavigation();
        init();

        navigation.selectedPageProperty().addListener((obs, old, val) -> {
            if (val != null) {
                navTree.getSelectionModel().select(navigation.getTreeItemForPage(val));
            }
        });
    }

    private void init() {
        var pane = new StackPane(navTree);
        pane.getStyleClass().add("navbar");
        pane.setMaxHeight(1.7976931348623157E308);
        pane.setMaxWidth(1.7976931348623157E308);
        VBox.setVgrow(pane, Priority.ALWAYS);
        this.setPrefHeight(640);
        this.setPrefWidth(250);
        this.setPadding(new Insets(0d, 0d, 5d, 0d));
        this.setSpacing(10d);
        VBox.setVgrow(this, Priority.ALWAYS);
        this.getChildren().addAll(buildWindowAction(), buildUserProfile(), pane, buildFooter());
        this.getStyleClass().addAll("sidebar");
    }

    private FontIcon buildFontIcon(SpotyGotFunctional.ParameterlessConsumer onAction, String styleClass) {
        var icon = new FontIcon(FontAwesomeSolid.CIRCLE);
        icon.setIconSize(15);
        icon.setOnMouseClicked(event -> onAction.run());
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    private FontIcon buildCloseIcon() {
        return buildFontIcon(this::closeWindow, "close-icon");
    }

    private FontIcon buildMinimizeIcon() {
        return buildFontIcon(this::minimizeWindow, "minimize-icon");
    }

    private HBox buildWindowAction() {
        var hbox = new HBox();
        hbox.setSpacing(10d);
        hbox.setPadding(new Insets(10d));
        hbox.getChildren().addAll(buildCloseIcon(), buildMinimizeIcon());
        return hbox;
    }

    private Circle buildProfileImage() {
        circle = new Circle();
        circle.setCache(true);
        circle.setCacheHint(CacheHint.SPEED);
        circle.setFill(Color.web("#4e4f5400"));
        circle.setStroke(Color.web("#ffffff00"));
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStrokeWidth(0d);
        circle.setRadius(30d);
        return circle;
    }

    private Label buildUserName() {
        username = new Label();
        username.setWrapText(true);
        username.getStyleClass().add(Styles.TITLE_4);
        return username;
    }

    private Label buildDesignation() {
        designation = new Label();
        designation.setWrapText(true);
        designation.getStyleClass().add(Styles.TEXT_SUBTLE);
        return designation;
    }

    private VBox buildProfileDetails() {
        var vbox = new VBox();
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setSpacing(10d);
        vbox.getChildren().addAll(buildUserName(), buildDesignation());
        return vbox;
    }

    private FontIcon buildProfileActionsMenu() {
        moreActions = new FontIcon(FontAwesomeSolid.ELLIPSIS_V);
        moreActions.getStyleClass().add("side-bar-more-actions");
        buildContextMenu();
        moreActions.setOnMouseClicked(this::moreActions);
        return moreActions;
    }

    private void buildContextMenu() {
        contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(viewProfile, logOut);
        moreActions.setCursor(Cursor.HAND);
    }

    private HBox buildProfileUIHolder() {
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(10d);
        hbox.getChildren().addAll(buildProfileImage(), buildProfileDetails(), new Spacer(), buildProfileActionsMenu());
        return hbox;
    }

    private Button buildSidebarToggle() {
        var arrowIcon = new FontIcon(FontAwesomeSolid.ANGLE_LEFT);
        arrowIcon.getStyleClass().add("nav-toggle-arrow");
        var toggle = new Button(null);
        toggle.setGraphic(arrowIcon);
        toggle.getStyleClass().add("nav-toggle");
        StackPane.setAlignment(toggle, Pos.CENTER_RIGHT);
        StackPane.setMargin(toggle, new Insets(40d, -45d, 0d, 0d));
//        toggle.setVisible(false);
//        toggle.setManaged(false);
        toggle.setOnAction(event -> this.toggleSidebar());
        return toggle;
    }

    private StackPane buildUserProfile() {
        var vbox = new VBox(buildProfileUIHolder());
        vbox.setSpacing(10d);
        vbox.setPadding(new Insets(20d));
        vbox.getChildren().addAll();
        var pane = new StackPane();
        pane.getChildren().addAll(vbox, buildSidebarToggle());
        return pane;
    }

    private void closeWindow() {
        stage.hide();
        stage.close();
        SpotyThreader.disposeSpotyThreadPool();
        Platform.exit();
    }

    private HBox buildFooter() {
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10d);
        var versionText = new Label(AppConfig.getAppVersion());
        versionText.getStyleClass().add("text");
        hbox.getChildren().add(versionText);
        return hbox;
    }

    private void minimizeWindow() {
        stage.setIconified(true);
    }

    public void setProfileImage(Image image) {
        circle.setFill(new ImagePattern(image));
    }

    public void setUsername(String username) {
        this.username.setText(username);
    }

    public void setDesignation(String designation) {
        this.designation.setText(designation);
    }

    private void moreActions(MouseEvent event) {
        contextMenu.show(moreActions.getScene().getWindow(),
                event.getScreenX(),
                event.getScreenY());
    }

    public void setViewProfileOnAction(SpotyGotFunctional.ParameterlessConsumer action) {
        viewProfile.setOnAction(event -> action.run());
    }

    public void setLogOutOnAction(SpotyGotFunctional.ParameterlessConsumer action) {
        logOut.setOnAction(event -> action.run());
    }

//    private void toggleSidebar() {
//        if (isMinimised) {
//            this.setPrefWidth(250);
//            this.isMinimised = false;
//        } else {
//            this.setPrefWidth(100);
//            this.isMinimised = true;
//        }
//    }

    private void toggleSidebar() {
        double targetWidth = isMinimised ? this.getPrefWidth() : 100;

        // Animate width change
        var timeline = new Timeline();
        var keyValue = new KeyValue(this.prefWidthProperty(), targetWidth);
        var keyFrame = new KeyFrame(Duration.millis(300), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();

        isMinimised = !isMinimised;
    }
}
