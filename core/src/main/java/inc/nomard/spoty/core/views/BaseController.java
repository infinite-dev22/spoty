package inc.nomard.spoty.core.views;

import com.dlsc.gemsfx.infocenter.*;
import inc.nomard.spoty.core.*;
import inc.nomard.spoty.core.values.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.layout.navigation.*;
import inc.nomard.spoty.network_bridge.auth.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
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
import lombok.*;
import lombok.extern.java.*;
import org.controlsfx.control.*;
import org.kordamp.ikonli.javafx.*;

@Log
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
        var image = (Objects.nonNull(ProtectedGlobals.user)
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
        imageHolder.setFill(new ImagePattern(image));
        initializeLoader();
        initAppBar();
        initNotifications();
        initApp();
    }

    public void initializeLoader() {
//        navigation = Navigation.getInstance(primaryStage, contentPane);
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
//        viewProfile.setOnAction(actionEvent -> navigation.navigate(Pages.getUserProfilePane()));
        logOut.setOnAction(actionEvent -> {
//            morphPane.morph(true);
//            // Base view parent.
//            navigation.navigate(Pages.getLoginPane());
        });
    }

    public void moreActionsClicked(MouseEvent event) {
        var contextMenu = new MFXContextMenu(moreActions);
        contextMenu.getItems().addAll(viewProfile, logOut);
        moreActions.setCursor(Cursor.HAND);
        contextMenu.show(moreActions.getScene().getWindow(),
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
                    () -> PaymentsViewModel.startTrial(this::onSuccess, this::successMessage, this::errorMessage));
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
            close.setOnAction(actionEvent -> new InformativeDialog(() -> {
                GlobalActions.closeDialog(actionEvent);
                morphPane.morph(false);
                rootPane.getChildren().removeAll(vBox);
                primaryStage.hide();
                primaryStage.close();
                SpotyThreader.disposeSpotyThreadPool();
                Platform.exit();
            }, primaryStage, contentPane));
            StackPane.setAlignment(hBox1, Pos.CENTER);
            StackPane.setAlignment(hBox2, Pos.BOTTOM_CENTER);
            rootPane.getChildren().addAll(vBox);
        }
    }

    private void successMessage(String message) {
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
    }

    private void onSuccess() {
        morphPane.morph(false);
        rootPane.getChildren().removeAll(vBox);
    }

    private void errorMessage(String message) {
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
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

    @Setter
    @Getter
    public static class Mail {
        private String title;
        private String description;
        private ZonedDateTime dateTime;

        public Mail(String title, String description, ZonedDateTime dateTime) {
            this.title = title;
            this.description = description;
            this.dateTime = dateTime;
        }

    }
}
