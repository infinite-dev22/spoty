package inc.nomard.spoty.core.views.layout;

import atlantafx.base.theme.*;
import inc.nomard.spoty.core.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.network_bridge.auth.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.mfxresources.fonts.*;
import java.util.*;
import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;

public class WindowRunner extends ApplicationWindowImpl {
    final VBox vBox = new VBox();
    private final Stage stage = AppManager.getPrimaryStage();

    public WindowRunner() {
        super();

        addNode(new MainLayer());
        clubBouncer();
    }

    private void clubBouncer() {
        if (!ProtectedGlobals.activeTenancy) {
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
            close.getStyleClass().add(Styles.BUTTON_OUTLINED);
            close.setOnAction(actionEvent -> new InformativeDialog(() -> {
                GlobalActions.closeDialog(actionEvent);
                this.setMorph(false);
                this.getChildren().removeAll(vBox);
                stage.hide();
                stage.close();
                SpotyThreader.disposeSpotyThreadPool();
                Platform.exit();
            }, stage, this));
            StackPane.setAlignment(hBox1, Pos.CENTER);
            StackPane.setAlignment(hBox2, Pos.BOTTOM_CENTER);
            this.getChildren().addAll(vBox);
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
        this.setMorph(false);
        this.getChildren().removeAll(vBox);
    }

    private void errorMessage(String message) {
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
    }
}
