package inc.nomard.spoty.core.views.settings.tenant_settings.widgets;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.values.PreloadedData;
import inc.nomard.spoty.core.viewModels.TenantSettingViewModel;
import inc.nomard.spoty.core.views.components.SpotyProgressSpinner;
import inc.nomard.spoty.core.views.forms.ReviewerForm;
import inc.nomard.spoty.core.views.layout.ModalContentHolder;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.network_bridge.dtos.Reviewer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.CacheHint;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * ReviewerCard is a custom UI component that displays information about a reviewer,
 * including their name, review level, and avatar, in a structured card layout.
 * This class utilizes JavaFX's layout management, bindings, and caching for optimal performance.
 */
public class ReviewerCard extends StackPane {
    private static final double IMAGE_SIZE = 50.0;
    private static final double CARD_SPACING = 10.0;
    private final ObjectProperty<Reviewer> reviewerProperty = new SimpleObjectProperty<>();
    private ModalPane modalPane;
    private Label reviewerNameLbl, reviewerLevelLbl;
    private Button editBtn, removeBtn;
    private ImageView avatarImageView;
    private SpotyProgressSpinner progress;

    /**
     * Constructor to initialize the ReviewerCard with a specific reviewer.
     *
     * @param reviewer The reviewer object to be displayed in the card.
     */
    public ReviewerCard(Reviewer reviewer, ModalPane modalPane) {
        this();
        this.modalPane = modalPane;
        setReviewer(reviewer);
    }

    /**
     * Default constructor that initializes the UI components and layout.
     */
    public ReviewerCard() {
        attachReviewerListeners();

        // Set the style for the card and enable caching for performance
        this.getStyleClass().add("card-flat");
        this.setCache(true);
        this.setCacheHint(CacheHint.SPEED);

        // Add the avatar and labels to the card's layout
        getChildren().addAll(buildBody(), createActionsHolder(), buildProgress());
        reviewerActions();
    }

    public HBox buildBody() {
        // Initialize labels to display reviewer name and level
        reviewerNameLbl = new Label();
        reviewerNameLbl.setWrapText(true);
        reviewerNameLbl.getStyleClass().addAll(Styles.TEXT, Styles.TEXT_BOLD);

        reviewerLevelLbl = new Label();
        reviewerLevelLbl.setWrapText(true);
        reviewerLevelLbl.getStyleClass().addAll(Styles.TEXT, Styles.TEXT_SUBTLE);

        editBtn = new Button(null, new FontIcon(FontAwesomeRegular.EDIT));
        editBtn.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT);

        removeBtn = new Button(null, new FontIcon(FontAwesomeSolid.TRASH_ALT));
        removeBtn.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT, Styles.DANGER);

        // Add the avatar and labels to the card's layout
        var hbox = new HBox(getReviewerAvatar(), createLabelsHolder());

        // Align content in the center with defined spacing
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(CARD_SPACING);
        hbox.setPadding(new Insets(8, 8, 8, 8));
        hbox.setPrefWidth(270);
        hbox.setMinWidth(250);

        return hbox;
    }

    /**
     * Creates a VBox to hold reviewer name and level labels with proper styling and alignment.
     *
     * @return A configured VBox with reviewer information.
     */
    private VBox createLabelsHolder() {
        VBox labelsHolder = new VBox(reviewerNameLbl, reviewerLevelLbl);
        labelsHolder.setAlignment(Pos.CENTER_LEFT);
        labelsHolder.setPadding(new Insets(5, 5, 0, 5));
        labelsHolder.setMaxWidth(160); // Limit width for a cleaner look
        return labelsHolder;
    }

    /**
     * Creates a VBox to hold reviewer actions with proper styling and alignment.
     *
     * @return A configured VBox with reviewer actions.
     */
    private HBox createActionsHolder() {
        var actionsHolder = new HBox(5, editBtn, removeBtn);
        actionsHolder.setAlignment(Pos.TOP_RIGHT);
        actionsHolder.setPadding(new Insets(0, 5, 0, 5));
        actionsHolder.setMaxWidth(30); // Limit width for a cleaner look
        actionsHolder.setMaxHeight(20); // Limit height for a cleaner look
        actionsHolder.setVisible(false);
        actionsHolder.setManaged(false);
        setOnMouseEntered(_ -> {
            actionsHolder.setVisible(true);
            actionsHolder.setManaged(true);
        });
        setOnMouseExited(_ -> {
            actionsHolder.setVisible(false);
            actionsHolder.setManaged(false);
        });
        actionsHolder.getStyleClass().addAll("reviewer-action-handler");
        StackPane.setAlignment(actionsHolder, Pos.TOP_RIGHT);
        return actionsHolder;
    }

    private SpotyProgressSpinner buildProgress() {
        progress = new SpotyProgressSpinner();
        progress.setMinSize(30d, 30d);
        progress.setPrefSize(30d, 30d);
        progress.setMaxSize(30d, 30d);
        progress.setVisible(false);
        progress.setManaged(false);
        return progress;
    }

    /**
     * Sets up listeners to update the UI when the reviewer property changes.
     */
    private void attachReviewerListeners() {
        reviewerProperty.addListener((_, _, newReviewer) -> {
            if (newReviewer != null) {
                // Update name and level labels with reviewer's details
                reviewerNameLbl.setText(newReviewer.getEmployee().getName());
                reviewerLevelLbl.setText("Review Level " + newReviewer.getLevel());

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
     * Adds functionality to the reviewer actions.
     */
    private void reviewerActions() {
        removeBtn.setOnAction(_ -> {
            progress.setVisible(true);
            progress.setManaged(true);
            TenantSettingViewModel.removeReviewer(getReviewer().getId(), this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
        });
        editBtn.setOnAction(
                event -> {
                    TenantSettingViewModel.getItem(getReviewer(), this::showDialog, this::errorMessage);
                    event.consume();
                });
    }

    private void showDialog() {
        var dialog = new ModalContentHolder(500, -1);
        dialog.getChildren().add(new ReviewerForm(modalPane, 1));
        dialog.setPadding(new Insets(5d));
        modalPane.setAlignment(Pos.TOP_RIGHT);
        modalPane.usePredefinedTransitionFactories(Side.RIGHT);
        modalPane.setOutTransitionFactory(node -> Animations.fadeOutRight(node, Duration.millis(400)));
        modalPane.setInTransitionFactory(node -> Animations.slideInRight(node, Duration.millis(400)));
        modalPane.show(dialog);
        modalPane.setPersistent(true);
    }

    private void onSuccess() {
        TenantSettingViewModel.getTenantSettings(null, null);
        progress.setVisible(false);
        progress.setManaged(false);
    }

    private void errorMessage(String message) {
        SpotyUtils.errorMessage(message);
        progress.setManaged(false);
        progress.setVisible(false);
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
