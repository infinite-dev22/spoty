package inc.nomard.spoty.core.views.settings.tenant_settings.widgets;

import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.values.PreloadedData;
import inc.nomard.spoty.network_bridge.dtos.Reviewer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 * ReviewerCard is a custom UI component that displays information about a reviewer,
 * including their name, approval level, and avatar, in a structured card layout.
 * This class utilizes JavaFX's layout management, bindings, and caching for optimal performance.
 */
public class ReviewerCard extends HBox {

    // Constants for layout and image settings
    private static final double IMAGE_SIZE = 50.0;
    private static final double CARD_SPACING = 10.0;

    // UI elements for displaying reviewer information
    private final Text reviewerNameLbl;
    private final Text reviewerLevelLbl;

    // Property to hold the current reviewer, allowing for easy binding and updates
    private final ObjectProperty<Reviewer> reviewerProperty = new SimpleObjectProperty<>();

    // ImageView to display the reviewer's avatar
    private ImageView avatarImageView;

    /**
     * Constructor to initialize the ReviewerCard with a specific reviewer.
     *
     * @param reviewer The reviewer object to be displayed in the card.
     */
    public ReviewerCard(Reviewer reviewer) {
        this(); // Call the default constructor to set up the UI
        setReviewer(reviewer); // Set the provided reviewer
    }

    /**
     * Default constructor that initializes the UI components and layout.
     */
    public ReviewerCard() {
        // Initialize labels to display reviewer name and level
        reviewerNameLbl = new Text();
        reviewerNameLbl.getStyleClass().addAll(Styles.TEXT_BOLD);

        reviewerLevelLbl = new Text();
        reviewerLevelLbl.getStyleClass().addAll(Styles.TEXT_SUBTLE, Styles.TEXT_SMALL);

        attachReviewerListeners();

        // Set the style for the card and enable caching for performance
        this.getStyleClass().add("card-flat");
        this.setCache(true);
        this.setCacheHint(CacheHint.SPEED);

        // Add the avatar and labels to the card's layout
        getChildren().addAll(getReviewerAvatar(), createLabelsHolder());

        // Align content in the center with defined spacing
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(CARD_SPACING);
        setPadding(new Insets(8, 8, 8, 8));
        setPrefWidth(270);
        setMinWidth(250);
    }

    /**
     * Creates a VBox to hold reviewer name and level labels with proper styling and alignment.
     *
     * @return A configured VBox with reviewer information.
     */
    private VBox createLabelsHolder() {
        VBox labelsHolder = new VBox(reviewerNameLbl, reviewerLevelLbl);
        labelsHolder.setAlignment(Pos.CENTER_LEFT);
        labelsHolder.setPadding(new Insets(0, 5, 0, 5));
        labelsHolder.setMaxWidth(160); // Limit width for a cleaner look
        return labelsHolder;
    }

    /**
     * Sets up listeners to update the UI when the reviewer property changes.
     */
    private void attachReviewerListeners() {
        reviewerProperty.addListener((_, _, newReviewer) -> {
            if (newReviewer != null) {
                // Update name and level labels with reviewer's details
                reviewerNameLbl.setText(newReviewer.getEmployee().getName());
                reviewerLevelLbl.setText("Approval Level " + newReviewer.getLevel());

                // Load and display the reviewer's avatar with appropriate error handling
                loadReviewerAvatar(newReviewer);
            }
        });
    }

    /**
     * Loads and sets the avatar image for the reviewer, with error handling for image loading.
     *
     * @param reviewer The reviewer whose avatar needs to be displayed.
     */
    private void loadReviewerAvatar(Reviewer reviewer) {
        Image avatarImage = new Image(
                reviewer.getEmployee().getAvatar() != null
                        && !reviewer.getEmployee().getAvatar().isEmpty()
                        && !reviewer.getEmployee().getAvatar().isBlank() ?
                        reviewer.getEmployee().getAvatar()
                        : PreloadedData.userPlaceholderImage.getUrl(),
                IMAGE_SIZE, IMAGE_SIZE, true, true, true
        );

        // Handle image loading progress and error cases
        avatarImage.progressProperty().addListener((_, _, newProgress) -> {
            if (newProgress.doubleValue() >= 1.0 && !avatarImage.isError()) {
                avatarImageView.setImage(avatarImage); // Successfully loaded image
            } else if (avatarImage.isError()) {
                avatarImageView.setImage(PreloadedData.imageErrorPlaceholderImage); // Fallback image
            }
        });
    }

    /**
     * Creates and returns an ImageView with a circular clip for the reviewer's avatar.
     *
     * @return An ImageView configured with a circular clip and placeholder image.
     */
    private ImageView getReviewerAvatar() {
        avatarImageView = new ImageView(PreloadedData.userPlaceholderImage);
        avatarImageView.setFitWidth(IMAGE_SIZE);
        avatarImageView.setFitHeight(IMAGE_SIZE);

        // Create a circular clip for the avatar
        Circle avatarClip = new Circle(IMAGE_SIZE / 2, IMAGE_SIZE / 2, IMAGE_SIZE / 2);
        avatarImageView.setClip(avatarClip);

        return avatarImageView;
    }

    /**
     * Gets the current reviewer displayed in the card.
     *
     * @return The current Reviewer object.
     */
    public Reviewer getReviewer() {
        return reviewerProperty.get();
    }

    /**
     * Sets the reviewer to be displayed in the card.
     *
     * @param reviewer The Reviewer object to set.
     */
    public void setReviewer(Reviewer reviewer) {
        this.reviewerProperty.set(reviewer);
    }

    /**
     * Provides access to the reviewer property for external binding.
     *
     * @return The ObjectProperty for the reviewer.
     */
    public ObjectProperty<Reviewer> reviewerProperty() {
        return this.reviewerProperty;
    }
}
