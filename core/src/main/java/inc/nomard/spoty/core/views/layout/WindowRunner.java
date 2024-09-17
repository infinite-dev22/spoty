package inc.nomard.spoty.core.views.layout;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.GlobalActions;
import inc.nomard.spoty.core.SpotyCoreResourceLoader;
import inc.nomard.spoty.core.viewModels.PaymentsViewModel;
import inc.nomard.spoty.core.viewModels.SubscriptionViewModel;
import inc.nomard.spoty.core.views.components.InformativeDialog;
import inc.nomard.spoty.core.views.layout.message.SpotyMessage;
import inc.nomard.spoty.core.views.layout.message.enums.MessageDuration;
import inc.nomard.spoty.core.views.layout.message.enums.MessageVariants;
import inc.nomard.spoty.core.views.pages.AuthScreen;
import inc.nomard.spoty.network_bridge.auth.SubscriptionProbe;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class WindowRunner extends ApplicationWindowImpl {
    final VBox vBox = new VBox();
    private final Stage stage = AppManager.getPrimaryStage();

    public WindowRunner() {
        super();
        addNode(new MainLayer());
        CompletableFuture.runAsync(() -> SubscriptionViewModel.troll(this::clubBouncer, null))
                .exceptionally(this::onDataInitializationFailure);
    }

    private void clubBouncer() {
        if (SubscriptionProbe.blockAccess) {
            var hBox1 = new HBox();
            var hBox2 = new HBox();
            var close = new Button("Close");
            var trialPlanDetails = new ArrayList<String>();
            var regularPlanDetails = new ArrayList<String>();
            var goldenPlanDetails = new ArrayList<String>();
            trialPlanDetails.add("Unleash powerful sales automation features.");
            trialPlanDetails.add("Gain real-time insights into your sales pipeline.");
            trialPlanDetails.add("Manage your customer relationships effectively.");
            var trialPlan = new PaymentPlanCard(SpotyCoreResourceLoader.load("images/cabin.png"),
                    "Free Trial",
                    "Try It Free (30-Day Trial)",
                    "Unlock the potential of OpenSale ERP system with a no-obligation trial.",
                    "0",
                    trialPlanDetails,
                    "Try It Now",
                    Color.web("#C44900"),
                    SubscriptionProbe.canTry,
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
            close.getStyleClass().add(Styles.BUTTON_OUTLINED);
            close.setOnAction(actionEvent -> new InformativeDialog(() -> {
                GlobalActions.closeDialog(actionEvent);
                this.setMorph(false);
                this.getChildren().removeAll(vBox);
                stage.hide();
                stage.close();
                SpotyThreader.disposeSpotyThreadPool();
                Platform.exit();
            }, stage, vBox));
            StackPane.setAlignment(hBox1, Pos.CENTER);
            StackPane.setAlignment(hBox2, Pos.BOTTOM_CENTER);
            vBox.getStyleClass().add("shade-app");
            this.getChildren().addAll(vBox);
        }
    }

    private Void onDataInitializationFailure(Throwable throwable) {
        SpotyLogger.writeToFile(throwable, AuthScreen.class);
        return null;
    }

    private void successMessage(String message) {
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon(FontAwesomeSolid.CHECK_CIRCLE)
                        .type(MessageVariants.SUCCESS)
                        .build();
        notification.setMinWidth(300);
        notification.setMinHeight(60);
        notification.setPrefWidth(400);
        notification.setPrefHeight(60);
        notification.setMaxWidth(500);
        notification.setMaxHeight(60);
        StackPane.setAlignment(notification, Pos.TOP_CENTER);

        var in = Animations.slideInDown(notification, Duration.millis(250));
        if (!this.getChildren().contains(notification)) {
            this.getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> delay(notification));
        }
    }

    private void delay(SpotyMessage message) {
        Duration delay = Duration.seconds(4);

        KeyFrame keyFrame = new KeyFrame(delay, event -> {
            var out = Animations.slideOutUp(message, Duration.millis(250));
            out.playFromStart();
            out.setOnFinished(actionEvent -> this.getChildren().remove(message));
        });

        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }

    private void onSuccess() {
        this.setMorph(false);
        this.getChildren().removeAll(vBox);
    }

    private void errorMessage(String message) {
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon(FontAwesomeSolid.EXCLAMATION_TRIANGLE)
                        .type(MessageVariants.ERROR)
                        .build();
        notification.setMinWidth(300);
        notification.setMinHeight(60);
        notification.setPrefWidth(400);
        notification.setPrefHeight(60);
        notification.setMaxWidth(500);
        notification.setMaxHeight(60);
        StackPane.setAlignment(notification, Pos.TOP_CENTER);

        var in = Animations.slideInDown(notification, Duration.millis(250));
        if (!this.getChildren().contains(notification)) {
            this.getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> delay(notification));
        }
    }
}
