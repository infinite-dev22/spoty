package inc.nomard.spoty.core.views.layout;

import inc.nomard.spoty.core.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import io.github.palexdev.mfxcomponents.controls.buttons.*;
import io.github.palexdev.mfxcomponents.theming.enums.*;
import io.github.palexdev.mfxcore.controls.*;
import java.util.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import lombok.*;
import lombok.extern.java.*;

@Setter
@Getter
@Builder

@Log
public class PaymentPlanCard extends VBox {
    private String imageUrl;
    private String planName;
    private String planSubName;
    private String planHighlight;
    private String planPrice;
    private ArrayList<String> planDetails;
    private String actionName;
    private Color planColor;
    private boolean canTry;
    private SpotyGotFunctional.ParameterlessConsumer onAction;

    public PaymentPlanCard() {
        buildUI();
    }

    public PaymentPlanCard(String imageUrl,
                           String planName,
                           String planSubName,
                           String planHighlight,
                           String planPrice,
                           ArrayList<String> planDetails,
                           String actionName,
                           Color planColor,
                           boolean canTry,
                           SpotyGotFunctional.ParameterlessConsumer onAction) {
        this.imageUrl = imageUrl;
        this.planName = planName;
        this.planSubName = planSubName;
        this.planHighlight = planHighlight;
        this.planPrice = planPrice;
        this.planDetails = planDetails;
        this.actionName = actionName;
        this.planColor = planColor;
        this.canTry = canTry;
        this.onAction = onAction;
        buildUI();
    }

    private void buildUI() {
        getChildren().addAll(
                buildPlanImage(),
                buildPlanName(),
                buildPlanHighlight(),
                buildPlanPrice(),
                buildPlanDetails(),
                buildPlanProceedAction()
        );
        setSpacing(40);
        if (canTry) {
            setStyle("-fx-border-color: rgb("
                    + planColor.getRed() * 255 + ", "
                    + planColor.getGreen() * 255 + ", "
                    + planColor.getBlue() * 255 +
                    ");");
        } else {
            setStyle("-fx-border-color: rgba("
                    + planColor.getRed() * 255 + ", "
                    + planColor.getGreen() * 255 + ", "
                    + planColor.getBlue() * 255 + ", "
                    + .5
                    + ");");
        }
        setMinSize(400, 700);
        setPrefSize(400, 700);
        setMaxSize(400, 700);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(40));
        getStyleClass().add("plan-card");
        getStylesheets().add(SpotyCoreResourceLoader.load("styles/base.css"));
        getStylesheets().add(SpotyCoreResourceLoader.load("styles/Common.css"));
        getStylesheets().add(SpotyCoreResourceLoader.load("styles/theming/Default.css"));
    }

    private ImageView buildPlanImage() {
        var themeItemImage = new Image(imageUrl, 150, 200, true, false);
        var themeItemImageView = new javafx.scene.image.ImageView(themeItemImage);
        themeItemImageView.setCache(true);
        themeItemImageView.setCacheHint(CacheHint.SPEED);
        themeItemImageView.setCache(true);
        themeItemImageView.setCacheHint(CacheHint.QUALITY);
        themeItemImageView.setSmooth(true);
        return themeItemImageView;
    }

    private VBox buildPlanName() {
        var nameLabel = new Label(planName);
        nameLabel.getStyleClass().add("plan-name");
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setWrapText(true);
        nameLabel.setForceDisableTextEllipsis(true);

        var subNameLabel = new Label(planSubName);
        subNameLabel.getStyleClass().add("plan-sub-name");
        subNameLabel.setAlignment(Pos.CENTER);
        subNameLabel.setWrapText(true);
        subNameLabel.setForceDisableTextEllipsis(true);
        if (canTry) {
            subNameLabel.setStyle("-fx-text-fill: rgb("
                    + planColor.getRed() * 255 + ", "
                    + planColor.getGreen() * 255 + ", "
                    + planColor.getBlue() * 255 +
                    ");");
        } else {
            subNameLabel.setStyle("-fx-text-fill: rgba("
                    + planColor.getRed() * 255 + ", "
                    + planColor.getGreen() * 255 + ", "
                    + planColor.getBlue() * 255 + ", "
                    + .5
                    + ");");
        }

        var vBox = new VBox(nameLabel, subNameLabel);
        vBox.setSpacing(5);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private Label buildPlanHighlight() {
        var label = new Label(planHighlight);
        label.getStyleClass().add("plan-highlight");
        label.setAlignment(Pos.CENTER);
        label.setWrapText(true);
        label.setForceDisableTextEllipsis(true);
        return label;
    }

    private Label buildPlanPrice() {
        var label = new Label("$ " + planPrice);
        label.getStyleClass().add("plan-price");
        label.setAlignment(Pos.CENTER);
        label.setWrapText(true);
        label.setForceDisableTextEllipsis(true);
        return label;
    }

    private VBox buildPlanDetails() {
        var vBox = new VBox();
        var labelsList = new ArrayList<Label>();
        for (String planDetail : planDetails) {
            var label = new Label("\u25CF " + planDetail);
            label.getStyleClass().add("plan-details");
            label.setWrapText(true);
            label.setForceDisableTextEllipsis(true);
            labelsList.add(label);
        }
        vBox.getChildren().addAll(labelsList);
        vBox.setSpacing(5);
//        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private MFXButton buildPlanProceedAction() {
        var button = new MFXButton(actionName);
        button.getStyleClass().add("filled");
        button.setStyle("-fx-background-color: rgb("
                + planColor.getRed() * 255 + ", "
                + planColor.getGreen() * 255 + ", "
                + planColor.getBlue() * 255 +
                ");" +
                "-fx-text-fill: white;");
        button.setOnAction(actionEvent -> onAction.run());

        if (canTry) {
            return button;
        } else {
            var disabledBtn = new MFXButton("Free trial no longer available");
            disabledBtn.getStyleClass().add("filled");
            disabledBtn.setStyle("-fx-background-color: rgba("
                    + planColor.getRed() * 255 + ", "
                    + planColor.getGreen() * 255 + ", "
                    + planColor.getBlue() * 255 + ", "
                    + .5
                    + ");" +
                    "-fx-text-fill: white;");
            disabledBtn.setDisable(true);
            return disabledBtn;
        }
    }
}
