/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package inc.nomard.spoty.core.views;

import inc.nomard.spoty.core.SpotyCoreResourceLoader;
import inc.nomard.spoty.core.components.navigation.Navigation;
import inc.nomard.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCircleToggleNode;
import io.github.palexdev.mfxcore.controls.Label;
import io.github.palexdev.mfxresources.fonts.IconsProviders;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class BaseController implements Initializable {
    public static Navigation navigation;
    private static BaseController instance;
    public final Stage primaryStage;
    @FXML
    public MFXFontIcon closeIcon;
    @FXML
    public MFXFontIcon minimizeIcon;
    @FXML
    public StackPane contentPane;
    @FXML
    public StackPane navBar;
    @FXML
    public HBox windowHeader;
    @FXML
    public AnchorPane rootPane;
    @FXML
    public MFXButton notificationsBtn;
    @FXML
    public MFXButton feedbackBtn;
    @FXML
    public MFXButton helpBtn;
    @FXML
    public Circle imageHolder;
    @FXML
    public Label designationLbl;
    @FXML
    public Label userNameLbl;
    @FXML
    public MFXCircleToggleNode sidebarToggle;
    private double xOffset;
    private double yOffset;
    private Node arrowIcon;
    private static final PseudoClass GROUP = PseudoClass.getPseudoClass("group");

    private BaseController(Stage stage) {
        this.primaryStage = stage;
    }

    public static BaseController getInstance(Stage stage) {
        if (instance == null) instance = new BaseController(stage);
        return instance;
    }

    @FXML
    void closeIconClicked() {
        primaryStage.hide();
        primaryStage.close();
        SpotyThreader.disposeSpotyThreadPool();
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void minimizeIconClicked() {
        primaryStage.setIconified(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var image = new Image(
                SpotyCoreResourceLoader.load("images/user-place-holder.png"),
                10000,
                10000,
                true,
                true
        );
        imageHolder.setFill(new ImagePattern(image));

        initializeLoader();
        initAppBar();
        initApp();
    }

    public void initializeLoader() {
        navigation = Navigation.getInstance(contentPane);
        navBar.getChildren().add(navigation.createNavigation());
    }

    private void initAppBar() {
        MFXFontIcon notificationIcon = new MFXFontIcon();
        MFXFontIcon feedbackIcon = new MFXFontIcon();
        MFXFontIcon helpIcon = new MFXFontIcon();
        notificationIcon.setIconsProvider(IconsProviders.FONTAWESOME_REGULAR);
        feedbackIcon.setIconsProvider(IconsProviders.FONTAWESOME_REGULAR);
        helpIcon.setIconsProvider(IconsProviders.FONTAWESOME_REGULAR);
        notificationIcon.setDescription("far-bell");
        feedbackIcon.setDescription("far-comment");
        helpIcon.setDescription("far-circle-question");

        notificationsBtn.setText("");

        notificationsBtn.setGraphic(notificationIcon);
        feedbackBtn.setGraphic(feedbackIcon);
        helpBtn.setGraphic(helpIcon);

        notificationsBtn.setTooltip(new Tooltip("Notification"));
        feedbackBtn.setTooltip(new Tooltip("Feedback"));
        helpBtn.setTooltip(new Tooltip("Help"));

        windowHeader.setOnMousePressed(
                event -> {
                    xOffset = primaryStage.getX() - event.getScreenX();
                    yOffset = primaryStage.getY() - event.getScreenY();
                });
        windowHeader.setOnMouseDragged(
                event -> {
                    primaryStage.setX(event.getScreenX() + xOffset);
                    primaryStage.setY(event.getScreenY() + yOffset);
                });
    }

    private void initApp() {
        arrowIcon = new FontIcon("fas-angle-left");
        arrowIcon.getStyleClass().add("nav-toggle-arrow");
        sidebarToggle.setGraphic(arrowIcon);
//        if (sidebarToggle.isArmed()) {
//        } else if (!sidebarToggle.isArmed()) {
//
//        }
        sidebarToggle.setText("");

        designationLbl.setText("Super Administrator");
        userNameLbl.setText("John Doe");
    }
}
