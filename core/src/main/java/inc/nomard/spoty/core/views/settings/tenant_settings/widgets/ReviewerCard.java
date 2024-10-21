package inc.nomard.spoty.core.views.settings.tenant_settings.widgets;

import inc.nomard.spoty.core.values.PreloadedData;
import inc.nomard.spoty.network_bridge.dtos.Reviewer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class ReviewerCard extends HBox {
    private static final Double IMAGE_SIZE = 160d;
    private static final Double ARC_SIZE = 20d;
    private static final Double SPACING = 10d;
    protected final IntegerProperty indexProperty = new SimpleIntegerProperty();
    private final Label reviewerNameLbl;
    private final Label reviewerLevelLbl;
    private final ObjectProperty<Reviewer> reviewerProperty = new SimpleObjectProperty<>();

    public ReviewerCard(Reviewer reviewer) {
        this();
        this.reviewerProperty.set(reviewer);
    }

    public ReviewerCard() {
        reviewerNameLbl = new Label();
        reviewerLevelLbl = new Label();

        var labelsHolder = new VBox(
                reviewerNameLbl,
                reviewerLevelLbl);
        labelsHolder.setAlignment(Pos.CENTER);
        labelsHolder.setPadding(new Insets(0d, 5d, 0d, 5d));
        labelsHolder.setMaxWidth(160);

        initializeLabels();
        attachListeners();

        this.getStyleClass().add("pos-reviewer-card");

        getChildren().addAll(
                getReviewerAvatar(),
                labelsHolder);

        this.setCache(true);
        this.setCacheHint(CacheHint.SPEED);
    }

    private void initializeLabels() {
        reviewerNameLbl.setWrapText(true);
        reviewerNameLbl.setLabelFor(this);
        reviewerLevelLbl.setWrapText(true);
        reviewerLevelLbl.setLabelFor(this);

        reviewerNameLbl.getStyleClass().add("pos-reviewer-card-name");
        reviewerLevelLbl.getStyleClass().add("pos-reviewer-card-price");

        setAlignment(Pos.CENTER);
        setSpacing(SPACING);
    }

    private void attachListeners() {
        this.reviewerProperty.addListener((_, _, reviewer) -> {
            reviewerNameLbl.setText(reviewer.getEmployee().getName());
            reviewerLevelLbl.setText("Approval Level " + reviewer.getLevel());
        });
    }

    private ImageView getReviewerAvatar() {
        var clip = new Rectangle(IMAGE_SIZE, IMAGE_SIZE);
        clip.setArcWidth(ARC_SIZE);
        clip.setArcHeight(ARC_SIZE);

        var imageView = new ImageView(PreloadedData.userPlaceholderImage);
        imageView.setFitWidth(IMAGE_SIZE);
        imageView.setFitHeight(IMAGE_SIZE);
        imageView.setClip(clip);

        reviewerProperty.addListener((_, _, newValue) -> {
            var image = new Image(newValue.getEmployee().getAvatar(), IMAGE_SIZE, IMAGE_SIZE, true, true, true);
            image.progressProperty().addListener((_, _, newProgress) -> {
                if (newProgress.doubleValue() >= 1.0 && !image.isError()) {
                    imageView.setImage(image); // Set the loaded image
                } else if (image.isError()) {
                    imageView.setImage(PreloadedData.imageErrorPlaceholderImage);
                }
            });
        });

        return imageView;
    }

    public Reviewer getReviewer() {
        return this.reviewerProperty.get();
    }

    public void setReviewer(Reviewer reviewer) {
        this.reviewerProperty.set(reviewer);
    }

    public ObjectProperty<Reviewer> reviewerProperty() {
        return this.reviewerProperty;
    }
}
