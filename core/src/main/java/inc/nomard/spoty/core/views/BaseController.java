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

import com.dlsc.gemsfx.infocenter.*;
import inc.nomard.spoty.core.SpotyCoreResourceLoader;
import inc.nomard.spoty.core.components.navigation.Navigation;
import inc.nomard.spoty.core.components.navigation.Pages;
import inc.nomard.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCircleToggleNode;
import io.github.palexdev.materialfx.controls.MFXContextMenu;
import io.github.palexdev.materialfx.controls.MFXContextMenuItem;
import io.github.palexdev.mfxcore.controls.Label;
import io.github.palexdev.mfxresources.fonts.IconsProviders;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

public class BaseController implements Initializable {
    public static Navigation navigation;
    private static BaseController instance;
    public final Stage primaryStage;
    private final InfoCenterView infoCenterView = new InfoCenterView();
    private final NotificationGroup<Mail, MailNotification> mailGroup = new NotificationGroup<>("Mail");
    private final NotificationGroup<Object, SlackNotification> slackGroup = new NotificationGroup<>("Slack");
    private final NotificationGroup<Object, CalendarNotification> calendarGroup = new NotificationGroup<>("Calendar");
    private final MFXContextMenuItem viewProfile = new MFXContextMenuItem("View profile");
    private final MFXContextMenuItem logOut = new MFXContextMenuItem("Log out");
    private final PopOver popOver = new PopOver();
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
    @FXML
    public MFXFontIcon moreActions;
    private double xOffset;
    private double yOffset;

    private BaseController(Stage stage) {
        this.primaryStage = stage;
    }

    public static BaseController getInstance(Stage stage) {
        if (instance == null) instance = new BaseController(stage);
        return instance;
    }

    private static ImageView createImageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(42);
        imageView.setFitHeight(42);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    @FXML
    void closeIconClicked() {
        primaryStage.hide();
        primaryStage.close();
        SpotyThreader.disposeSpotyThreadPool();
        Platform.exit();
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
        initNotifications();
        initApp();
    }

    public void initializeLoader() {
        navigation = Navigation.getInstance(contentPane);
        navBar.getChildren().add(navigation.createNavigation());
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
        var arrowIcon = new FontIcon("fas-angle-left");
        arrowIcon.getStyleClass().add("nav-toggle-arrow");
        sidebarToggle.setGraphic(arrowIcon);
        sidebarToggle.setText("");

        designationLbl.setText("Super Administrator");
        userNameLbl.setText("John Doe");

        viewProfile.setOnAction(actionEvent -> navigation.navigate(Pages.getUserProfilePane()));
    }

    public void moreActionsClicked(MouseEvent event) {
        var contextMenu = new MFXContextMenu(moreActions);
        contextMenu.getItems().addAll(viewProfile, logOut);
        moreActions.setCursor(Cursor.HAND);
        contextMenu.show(
                moreActions.getScene().getWindow(),
                event.getScreenX(),
                event.getScreenY());
    }

    // NOTIFICATIONS.
    private void initNotifications() {
        slackGroup.setSortOrder(0);
        calendarGroup.setSortOrder(1);
        mailGroup.setSortOrder(2);

        slackGroup.maximumNumberOfNotificationsProperty().bind(Bindings.createIntegerBinding(() -> slackGroup.isPinned() ? 3 : 10, slackGroup.pinnedProperty()));
        calendarGroup.maximumNumberOfNotificationsProperty().bind(Bindings.createIntegerBinding(() -> calendarGroup.isPinned() ? 3 : 10, calendarGroup.pinnedProperty()));
        mailGroup.maximumNumberOfNotificationsProperty().bind(Bindings.createIntegerBinding(() -> mailGroup.isPinned() ? 3 : 10, mailGroup.pinnedProperty()));

        slackGroup.setViewFactory(n -> {
            NotificationView<Object, SlackNotification> view = new NotificationView<>(n);
            view.setGraphic(createImageView(new Image(Objects.requireNonNull(SpotyCoreResourceLoader.load("notification/slack.png")))));
            return view;
        });

        calendarGroup.setViewFactory(n -> {
            NotificationView<Object, CalendarNotification> view = new NotificationView<>(n);
            view.setGraphic(createImageView(new Image(Objects.requireNonNull(SpotyCoreResourceLoader.load("notification/calendar.png")))));
            Region region = new Region();
            region.setMinHeight(200);
            region.setBackground(new Background(new BackgroundImage(new Image(Objects.requireNonNull(SpotyCoreResourceLoader.load("notification/map.png"))), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, false, true))));
            StackPane stackPane = new StackPane(region);
            stackPane.setStyle("-fx-border-color: grey;");
            view.setContent(stackPane);
            return view;
        });

        mailGroup.setViewFactory(n -> {
            NotificationView<Mail, MailNotification> view = new NotificationView<>(n);
            view.setGraphic(createImageView(new Image(Objects.requireNonNull(SpotyCoreResourceLoader.load("notification/mail.png")))));
            return view;
        });

        infoCenterView.getGroups().setAll(mailGroup, slackGroup, calendarGroup);
        infoCenterView.setPadding(new Insets(0));

        for (int i = 0; i < 10; i++) {
            assignNotification(createNotification(true));
        }

        notificationsBtn.setOnAction(evt -> {
            if (popOver.isShowing()) {
                popOver.hide(Duration.millis(300));
            }
            if (!popOver.isShowing()) {
                popOver.show(notificationsBtn);
            }
        });

        infoCenterView.getStylesheets().add(Objects.requireNonNull(SpotyCoreResourceLoader.load("notification/scene.css")));
        popOver.setAnimated(true);
        popOver.setTitle("Notifications");
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        popOver.setArrowIndent(10);
        popOver.setCornerRadius(20);
        popOver.setFadeInDuration(Duration.millis(300));
        popOver.setFadeOutDuration(Duration.millis(300));
        popOver.setHeaderAlwaysVisible(true);
        popOver.setMaxHeight(700);
        popOver.setHideOnEscape(true);
        popOver.setContentNode(infoCenterView);
    }

    private void assignNotification(Notification<?> notification) {
        if (notification instanceof MailNotification) {
            mailGroup.getNotifications().add((MailNotification) notification);
        } else if (notification instanceof CalendarNotification) {
            calendarGroup.getNotifications().add((CalendarNotification) notification);
        } else if (notification instanceof SlackNotification) {
            slackGroup.getNotifications().add((SlackNotification) notification);
        }
    }

    private Notification<?> createNotification(boolean randomizeTimeStamp) {
        Notification notification;
        switch ((int) (Math.random() * 3)) {
            case 0:
                notification = createMailNotification();
                break;
            case 1:
                notification = new SlackNotification("DLSC GmbH\nDirk Lemmermann", "Please send the material I requested.");
                break;
            case 2:
            default:
                notification = new CalendarNotification("Calendar", "Meeting with shareholders");
        }

        if (randomizeTimeStamp) {
            notification.setDateTime(createTimeStamp());
        }

        return notification;
    }

    private MailNotification createMailNotification() {
        Mail mail = new Mail("Purchase Order #8774911", "Dear Mr. Smith, the following order has been received by our service counter.", ZonedDateTime.now());
        MailNotification mailNotification = new MailNotification(mail);

        NotificationAction<Mail> openMailAction = new NotificationAction<>("Open", (notification) -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Opening Mail ...");
            alert.initOwner(infoCenterView.getScene().getWindow());
            alert.show();
            return Notification.OnClickBehaviour.HIDE_AND_REMOVE;
        });

        NotificationAction<Mail> deleteMailAction = new NotificationAction<>("Delete", (notification) -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Deleting Mail ...");
            alert.initOwner(infoCenterView.getScene().getWindow());
            alert.show();
            return Notification.OnClickBehaviour.HIDE_AND_REMOVE;
        });

        mailNotification.getActions().add(openMailAction);
        mailNotification.getActions().add(deleteMailAction);

        return mailNotification;
    }

    private ZonedDateTime createTimeStamp() {
        ZonedDateTime time = ZonedDateTime.now();
        time = time.minusDays((int) (Math.random() * 2));
        time = time.minusMinutes((int) (Math.random() * 30));
        time = time.minusSeconds((int) (Math.random() * 45));
        return time;
    }

    public static class CalendarNotification extends Notification<Object> {

        public CalendarNotification(String title, String description) {
            super(title, description);
            setOnClick(notification -> OnClickBehaviour.HIDE_AND_REMOVE);
        }
    }

    public static class SlackNotification extends Notification<Object> {

        public SlackNotification(String title, String description) {
            super(title, description);
            setOnClick(notification -> OnClickBehaviour.REMOVE);
        }
    }

    public static class MailNotification extends Notification<Mail> {

        public MailNotification(Mail mail) {
            super(mail.getTitle(), mail.getDescription(), mail.getDateTime());
            setUserObject(mail);
            setOnClick(notification -> OnClickBehaviour.NONE);
        }
    }

    public static class Mail {

        private String title;
        private String description;
        private ZonedDateTime dateTime;

        public Mail(String title, String description, ZonedDateTime dateTime) {
            this.title = title;
            this.description = description;
            this.dateTime = dateTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public ZonedDateTime getDateTime() {
            return dateTime;
        }

        public void setDateTime(ZonedDateTime dateTime) {
            this.dateTime = dateTime;
        }
    }
}
