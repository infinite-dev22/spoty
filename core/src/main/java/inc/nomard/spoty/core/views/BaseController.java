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
import inc.nomard.spoty.core.*;
import inc.nomard.spoty.core.components.glass_morphism.*;
import inc.nomard.spoty.core.components.navigation.*;
import inc.nomard.spoty.core.components.payment_plan_card.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.network_bridge.auth.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.mfxcomponents.theming.enums.*;
import io.github.palexdev.mfxcore.controls.Label;
import io.github.palexdev.mfxresources.fonts.*;
import java.net.*;
import java.time.*;
import java.util.*;
import javafx.application.*;
import javafx.beans.binding.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;
import javafx.util.Duration;
import org.controlsfx.control.*;
import org.kordamp.ikonli.javafx.*;

import lombok.extern.slf4j.*;

@Slf4j
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
    private final VBox vBox = new VBox();
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
    public StackPane rootPane;
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
    @FXML
    public GlassPane morphPane;
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

        designationLbl.setText(ProtectedGlobals.user.getRole().getLabel());
        designationLbl.setWrapText(true);
        designationLbl.setForceDisableTextEllipsis(true);

        userNameLbl.setText(ProtectedGlobals.user.getName());
        userNameLbl.setWrapText(true);
        userNameLbl.setForceDisableTextEllipsis(true);

        clubBouncer();

        viewProfile.setOnAction(actionEvent -> navigation.navigate(Pages.getUserProfilePane()));
        // logOut.setOnAction(actionEvent -> {});
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
        Notification notification = switch ((int) (Math.random() * 3)) {
            case 0 -> createMailNotification();
            case 1 -> new SlackNotification("DLSC GmbH\nDirk Lemmermann", "Please send the material I requested.");
            default -> new CalendarNotification("Calendar", "Meeting with shareholders");
        };

        if (randomizeTimeStamp) {
            notification.setDateTime(createTimeStamp());
        }

        return notification;
    }

    private void clubBouncer() {
        if (!ProtectedGlobals.activeTenancy) {
            morphPane.morph(true);
            var hBox1 = new HBox();
            var hBox2 = new HBox();
            var close = new io.github.palexdev.mfxcomponents.controls.buttons.MFXButton("Close");
            var trialPlanDetails = new ArrayList<String>();
            var regularPlanDetails = new ArrayList<String>();
            var goldenPlanDetails = new ArrayList<String>();

            trialPlanDetails.add("Unleash powerful sales automation features.");
            trialPlanDetails.add("Gain real-time insights into your sales pipeline.");
            trialPlanDetails.add("Manage your customer relationships effectively.");
            var trialPlan = new PaymentPlanCard(SpotyCoreResourceLoader.load("images/cabin.png"),
                    "Free Trial",
                    "Try It Free (7-Day Trial)",
                    "Unlock the potential of OpenSale ERP system with a no-obligation trial.",
                    "0",
                    trialPlanDetails,
                    "Try It Now",
                    Color.web("#C44900"),
                    ProtectedGlobals.canTry,
                    () -> PaymentsViewModel.startTrial(this::onActivity, this::onSuccess, this::onFailed));

            regularPlanDetails.add("Comprehensive reporting and analytics.");
            regularPlanDetails.add("Dedicated customer support.");
            regularPlanDetails.add("Start growing today!");
            var monthlyPlan = new PaymentPlanCard(SpotyCoreResourceLoader.load("images/home.png"),
                    "Pay as You Go",
                    "Boost Your Business (Monthly)",
                    "Maximize your sales efficiency and accelerate growth with our monthly subscription.",
                    "38.0/month",
                    regularPlanDetails,
                    "Proceed Now",
                    Color.web("#0D21A1"),
                    true,
                    () -> {
                    });

            goldenPlanDetails.add("Discounted annual pricing (save 10%+).");
            goldenPlanDetails.add("Unwavering commitment to your success.");
            goldenPlanDetails.add("Peace of mind knowing your sales platform scales with your business.");
            var yearlyPlan = new PaymentPlanCard(SpotyCoreResourceLoader.load("images/house.png"),
                    "Save Now",
                    "Scale with Certainty (Yearly)",
                    "Lock in guaranteed savings and long-term growth!",
                    "400.0/year",
                    goldenPlanDetails,
                    "Proceed Now",
                    Color.web("#0E7C7B"),
                    true,
                    () -> {
                    });

            hBox1.setAlignment(Pos.CENTER);
            hBox1.setSpacing(50);
            hBox1.getChildren().addAll(trialPlan, monthlyPlan, yearlyPlan);

            hBox2.setAlignment(Pos.CENTER);
            hBox2.getChildren().add(close);

            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().addAll(hBox1, hBox2);
            vBox.setSpacing(100);

            var closeIcon = new MFXFontIcon("fas-xmark");
            closeIcon.setColor(Color.RED);
            close.setGraphic(closeIcon);
            close.setVariants(ButtonVariants.OUTLINED);
            close.setOnAction(actionEvent1 -> buildDialog().showAndWait());
            StackPane.setAlignment(hBox1, Pos.CENTER);
            StackPane.setAlignment(hBox2, Pos.BOTTOM_CENTER);
            rootPane.getChildren().addAll(vBox);
        }
    }

    private MFXStageDialog buildDialog() {
        var cancel = new io.github.palexdev.mfxcomponents.controls.buttons.MFXButton("Cancel");
        var proceed = new io.github.palexdev.mfxcomponents.controls.buttons.MFXButton("Proceed");

        var dialogContent = new MFXGenericDialog("Confirm exit!", "Are you sure you want to exit and close this app?");
        dialogContent.addActions(proceed, cancel);

        cancel.setVariants(ButtonVariants.FILLED);
        proceed.setVariants(ButtonVariants.OUTLINED);
        cancel.setOnAction(GlobalActions::closeDialog);
        proceed.setOnAction(actionEvent -> {
            GlobalActions.closeDialog(actionEvent);
            morphPane.morph(false);
            rootPane.getChildren().removeAll(vBox);
            primaryStage.hide();
            primaryStage.close();
            SpotyThreader.disposeSpotyThreadPool();
            Platform.exit();
        });


        var dialog = MFXGenericDialogBuilder.build(dialogContent)
                .toStageDialogBuilder()
                .initOwner(primaryStage)
                .initModality(Modality.WINDOW_MODAL)
                .setOwnerNode(contentPane)
                .setScrimPriority(ScrimPriority.WINDOW)
                .setScrimOwner(true)
                .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
        return dialog;
    }

    private void onActivity() {
//        loginBtn.setDisable(true);
//        loginBtn.setManaged(false);
//        activityIndicator.setVisible(true);
//        activityIndicator.setManaged(true);
    }

    private void onSuccess() {
        morphPane.morph(false);
        rootPane.getChildren().removeAll(vBox);
    }

    private void onFailed() {
//        loginBtn.setDisable(false);
//        loginBtn.setManaged(true);
//        activityIndicator.setVisible(false);
//        activityIndicator.setManaged(false);
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
