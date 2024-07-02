package inc.nomard.spoty.core.views.layout.navigation;

import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import inc.nomard.spoty.utils.navigation.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.mfxcore.controls.*;
import io.github.palexdev.mfxresources.fonts.*;
import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;

public class SideBar extends VBox {
    private final Stage stage;
    private final NavTree navTree;
    private Circle circle;
    private Label username;
    private Label designation;
    private MFXFontIcon moreActions;
    private Boolean isMinimised = false;

    public SideBar(Stage stage, Navigation navigation) {
        this.stage = stage;
        this.navTree = new NavTree(navigation);
        init();
    }

    private void init() {
        this.setPrefHeight(640);
        this.setPrefWidth(250);
        this.setPadding(new Insets(0d, 0d, 5d, 0d));
        this.setSpacing(10d);
        this.getChildren().addAll(buildWindowAction(), buildUserProfile(), navTree);
    }

    private MFXFontIcon buildFontIcon(SpotyGotFunctional.ParameterlessConsumer onAction, String styleClass) {
        var icon = new MFXFontIcon("fas-circle");
        icon.setSize(15.0);
        icon.setOnMouseClicked(event -> onAction.run());
        icon.getStyleClass().add(styleClass);
        return icon;
    }

    private MFXFontIcon buildCloseIcon() {
        return buildFontIcon(this::closeWindow, "close-icon");
    }

    private MFXFontIcon buildMinimizeIcon() {
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
        username.getStyleClass().addAll("h4", "text-white");
        return username;
    }

    private Label buildDesignation() {
        designation = new Label();
        designation.getStyleClass().addAll("text", "disabled-text");
        return designation;
    }

    private VBox buildProfileDetails() {
        var vbox = new VBox();
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setSpacing(10d);
        vbox.getChildren().addAll(buildUserName(), buildDesignation());
        return vbox;
    }

    private MFXFontIcon buildProfileActionsMenu() {
        moreActions = new MFXFontIcon("fas-ellipsis-vertical");
        moreActions.setSize(25d);
        moreActions.getStyleClass().add("side-bar-more-actions");
        return moreActions;
    }

    private HBox buildProfileUIHolder() {
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(10d);
        hbox.getChildren().addAll(buildProfileImage(), buildProfileDetails(), new Spacer(), buildProfileActionsMenu());
        return hbox;
    }

    private MFXCircleToggleNode buildSidebarToggle() {
        var toggle = new MFXCircleToggleNode();
        toggle.getStyleClass().add("nav-toggle");
        toggle.setSize(15d);
        StackPane.setAlignment(toggle, Pos.CENTER_RIGHT);
        StackPane.setMargin(toggle, new Insets(40d, -45d, 0d, 0d));
        toggle.setVisible(false);
        toggle.setManaged(false);
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

    public void moreActions(SpotyGotFunctional.ParameterlessConsumer onAction) {
        moreActions.setOnMouseClicked(event -> onAction.run());
    }

    private void toggleSidebar() {
        if (isMinimised) {
            this.setPrefWidth(250);
            this.isMinimised = false;
        } else {
            this.setPrefWidth(100);
            this.isMinimised = true;
        }
    }
}
