package inc.nomard.spoty.core.views.settings.tenant_settings.tabs;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.viewModels.CurrencyViewModel;
import inc.nomard.spoty.core.viewModels.TenantSettingViewModel;
import inc.nomard.spoty.core.viewModels.hrm.employee.EmployeeViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.SpotyProgressSpinner;
import inc.nomard.spoty.core.views.components.label_components.controls.LabeledComboBox;
import inc.nomard.spoty.core.views.forms.ReviewerForm;
import inc.nomard.spoty.core.views.layout.ModalContentHolder;
import inc.nomard.spoty.core.views.layout.SideModalPane;
import inc.nomard.spoty.core.views.layout.navigation.Spacer;
import inc.nomard.spoty.core.views.settings.tenant_settings.widgets.ReviewerCard;
import inc.nomard.spoty.core.views.util.FunctionalStringConverter;
import inc.nomard.spoty.core.views.util.NodeUtils;
import inc.nomard.spoty.core.views.util.OutlinePage;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.utils.SpotyLogger;
import javafx.beans.property.Property;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Currency;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;

public class CompanySettings extends OutlinePage {
    private final SideModalPane modalPane;
    private CustomButton saveBtn;
    private VBox content;
    private CheckBox reviews;
    private ScrollPane scrollPane;
    private LabeledComboBox<Currency> defaultCurrencyPicker;

    public CompanySettings() {
        modalPane = new SideModalPane();
        getChildren().addAll(modalPane, init());
        modalPane.displayProperty().addListener((_, _, open) -> {
            if (!open) {
                modalPane.setAlignment(Pos.CENTER);
                modalPane.usePredefinedTransitionFactories(null);
            }
        });
    }

    private void onDataInitializationSuccess() {
        closeProgress();
    }

    private void errorMessage(String message) {
        SpotyUtils.errorMessage(message);
        closeProgress();
    }

    public void progress() {
        var dialog = new ModalContentHolder(50, 50);
        dialog.getChildren().add(new SpotyProgressSpinner());
        dialog.setPadding(new Insets(5d));
        modalPane.setAlignment(Pos.CENTER);
        modalPane.show(dialog);
        modalPane.setPersistent(true);
    }

    public void closeProgress() {
        modalPane.hide(true);
        modalPane.setPersistent(false);
    }

    private AnchorPane init() {
        var pane = new AnchorPane();
        pane.getStyleClass().add("card-flat-top");
        BorderPane.setAlignment(pane, Pos.CENTER);
        BorderPane.setMargin(pane, new Insets(0d));
        pane.getChildren().add(buildScrollPane());
        setupComboBoxes();
        return pane;
    }

    private ScrollPane buildScrollPane() {
        scrollPane = new ScrollPane(buildContent());
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        componentSizing();
        NodeUtils.setAnchors(scrollPane, new Insets(0d));
        return scrollPane;
    }

    private VBox buildContent() {
        content = new VBox(16d);
        content.setPadding(new Insets(16d));
        content.getChildren().addAll(buildHeader(),
                separator(), buildDefaultCurrency(),
                separator(), buildApprovals(),
                new Spacer(100, Orientation.VERTICAL));

        return content;
    }

    private HBox buildSection() {
        var hbox = new HBox();
        hbox.setSpacing(8d);
        return hbox;
    }

    private VBox buildSectionGroup() {
        var vbox = new VBox();
        vbox.setMinWidth(350d);
        vbox.setPrefWidth(450d);
        vbox.setMaxWidth(550d);
        vbox.setSpacing(8d);
        return vbox;
    }

    private VBox buildTitle() {
        var label = new Text("Company System Features");
        label.getStyleClass().addAll(Styles.TITLE_2);
        label.sceneProperty().addListener((_, _, newScene) -> {
            if (newScene != null) {
                // At this point, the parent is guaranteed to be set
                label.getParent().layoutBoundsProperty().addListener((_, _, newBounds) -> {
                    label.setWrappingWidth(newBounds.getWidth() * 0.8); // Set wrapping width to 80% of parent's width
                });
            }
        });
        var subLabel = new Text("""
                Features required or used at your company by your employees. \
                
                Note:
                These features will change for your entire system and will affect all employee work stations.""");
        subLabel.getStyleClass().addAll(Styles.TEXT, Styles.TEXT_SUBTLE);
        subLabel.sceneProperty().addListener((_, _, newScene) -> {
            if (newScene != null) {
                // At this point, the parent is guaranteed to be set
                subLabel.getParent().layoutBoundsProperty().addListener((_, _, newBounds) -> {
                    subLabel.setWrappingWidth(newBounds.getWidth() * 0.8); // Set wrapping width to 80% of parent's width
                });
            }
        });
        var vbox = buildSectionGroup();
        vbox.setMaxWidth(1000d);
        vbox.setPrefWidth(800d);
        vbox.setMinWidth(600d);
        vbox.getChildren().addAll(label, subLabel);
        return vbox;
    }

    private VBox buildSectionTitle(String title, String subTitle) {
        var label = new Label(title);
        label.getStyleClass().addAll(Styles.TITLE_4);
        label.setWrapText(true);
        var subLabel = new Label(subTitle);
        subLabel.getStyleClass().addAll(Styles.TEXT, Styles.TEXT_SUBTLE);
        subLabel.setWrapText(true);
        var vbox = buildSectionGroup();
        vbox.getChildren().addAll(label, subLabel);
        return vbox;
    }

    private Separator separator() {
        var separator = new Separator();
        separator.prefWidth(200d);
        return separator;
    }

    private Region spacer() {
        var region = new Region();
        region.maxWidth(140d);
        region.prefWidth(120d);
        return region;
    }

    private VBox buildCheckBox(Property<Boolean> property, String label, String subTitle) {
        var checkBox = new CheckBox(label);
        checkBox.selectedProperty()
                .bindBidirectional(property);
        var text = new Label(subTitle);
        text.getStyleClass().addAll(Styles.TEXT, Styles.TEXT_SUBTLE);
        text.setWrapText(true);
        var vbox = new VBox(10d, checkBox, text);
        vbox.setPrefWidth(250d);
        return vbox;
    }

    private HBox buildHeader() {
        // Save button.
        saveBtn = new CustomButton("Save Changes");
        saveBtn.getStyleClass().add(Styles.ACCENT);
        saveBtn.setOnAction(_ -> TenantSettingViewModel.updateTenantSettings(this::onSuccess, SpotyUtils::successMessage, this::errorMessage));
        var hbox = new HBox();
        hbox.setSpacing(16d);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(
                buildTitle(
                ),
                new Spacer(),
                saveBtn);
        return hbox;
    }

    private void onSuccess() {
        TenantSettingViewModel.getTenantSettings(null, null);
    }

    private HBox buildDefaultCurrency() {
        defaultCurrencyPicker = new LabeledComboBox<>();
        defaultCurrencyPicker.setLabel("Default Currency");
        defaultCurrencyPicker.setPrefWidth(400d);
        defaultCurrencyPicker.setValue(TenantSettingViewModel.getDefaultCurrency());
        var hbox = buildSection();
        hbox.getChildren().addAll(
                buildSectionTitle("Default Currency",
                        "Currency to be auto selected for transactions unless another is selected"),
                spacer(),
                defaultCurrencyPicker,
                spacer());
        return hbox;
    }

    private HBox buildApprovals() {
        var approvalChecks = buildApprovalCheckBoxes();
        approvalChecks.setDisable(true);
        var reviewLevels = buildReviewLevels();
        reviewLevels.setDisable(true);
        var reviewers = buildReviewers();
        reviewers.setDisable(true);

        reviews = new CheckBox("Enable reviews");
        reviews.selectedProperty().addListener((_, _, newValue) -> {
            if (newValue) {
                approvalChecks.setDisable(false);
                reviewLevels.setDisable(false);
                reviewers.setDisable(false);
            } else {
                approvalChecks.setDisable(true);
                reviewLevels.setDisable(true);
                reviewers.setDisable(true);
            }
        });
        reviews.selectedProperty().bindBidirectional(TenantSettingViewModel.reviewsProperty());
        var label = new Text("Enable reviews for your company");
        label.getStyleClass().addAll(Styles.TEXT, Styles.TEXT_SUBTLE);
        label.sceneProperty().addListener((_, _, newScene) -> {
            if (newScene != null) {
                // At this point, the parent is guaranteed to be set
                label.getParent().layoutBoundsProperty().addListener((_, _, newBounds) -> {
                    label.setWrappingWidth(newBounds.getWidth() * 0.8); // Set wrapping width to 80% of parent's width
                });
            }
        });
        var vbox = new VBox(10d, reviews, label, approvalChecks, reviewLevels, reviewers);
        HBox.setHgrow(vbox, Priority.ALWAYS);
        var hbox = buildSection();
        hbox.getChildren().addAll(
                buildSectionTitle("Reviews",
                        "Set up review/approval workflows for transactions like operations, ensuring compliance and " +
                                "control based on roles and hierarchies."),
                spacer(),
                vbox,
                spacer());
        return hbox;
    }

    private VBox buildApprovalCheckBoxes() {
        var flowPane = new FlowPane();
        flowPane.setAlignment(Pos.TOP_LEFT);
        flowPane.setHgap(16d);
        flowPane.setVgap(16d);
        flowPane.getChildren().addAll(buildCheckBox(TenantSettingViewModel.reviewAdjustmentsProperty(), "Approve Adjustments", "Approve adjustments"),
                buildCheckBox(TenantSettingViewModel.reviewPurchasesProperty(), "Approve Purchases", "Approve purchases"),
                buildCheckBox(TenantSettingViewModel.reviewPurchaseReturnsProperty(), "Approve Purchase Returns", "Approve purchase returns"),
                buildCheckBox(TenantSettingViewModel.reviewQuotationsProperty(), "Approve Quotations", "Approve quotations"),
                buildCheckBox(TenantSettingViewModel.reviewRequisitionsProperty(), "Approve Requisitions", "Approve requisitions"),
                buildCheckBox(TenantSettingViewModel.reviewSalesProperty(), "Approve Sales", "Approve sales"),
                buildCheckBox(TenantSettingViewModel.reviewSaleReturnsProperty(), "Approve Sale Returns", "Approve sale returns"),
                buildCheckBox(TenantSettingViewModel.reviewStockInsProperty(), "Approve StockIns", "Approve stock ins"));
        return new VBox(separator(), flowPane);
    }

    private void showDialog() {
        var dialog = new ModalContentHolder(500, -1);
        dialog.getChildren().add(new ReviewerForm(modalPane, 0));
        dialog.setPadding(new Insets(5d));
        modalPane.setAlignment(Pos.TOP_RIGHT);
        modalPane.usePredefinedTransitionFactories(Side.RIGHT);
        modalPane.setOutTransitionFactory(node -> Animations.fadeOutRight(node, Duration.millis(400)));
        modalPane.setInTransitionFactory(node -> Animations.slideInRight(node, Duration.millis(400)));
        modalPane.show(dialog);
        modalPane.setPersistent(true);
    }

    private VBox buildReviewLevels() {
        Spinner<Integer> reviewLevels = new Spinner<>();
        reviewLevels.setPrefWidth(100d);
        reviewLevels.getStyleClass().addAll(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        reviewLevels.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        reviewLevels.getValueFactory().valueProperty().bindBidirectional(TenantSettingViewModel.reviewLevelsProperty().asObject());

        var label = new Text("Tag line");
        label.getStyleClass().addAll(Styles.TEXT);
        var hbox = buildSection();
        hbox.getChildren().addAll(new VBox(
                        20,
                        buildSectionTitle("Review Levels",
                                "Number of times a document must be reviewed to pass"),
                        reviewLevels),
                spacer());
        return new VBox(separator(), hbox);
    }

    private VBox buildReviewers() {
        var title = buildSectionTitle("Reviewers", "Employees who can review work of other employees.Add up to 10+ reviewers");
        var addReviewerBtn = new Button(null, new FontIcon(FontAwesomeSolid.PLUS));
        addReviewerBtn.getStyleClass().add(Styles.ACCENT);
        addReviewerBtn.setOnAction(_ -> this.showDialog());
        var hbox = new HBox(title, new Spacer(), addReviewerBtn);
        var flowPane = new FlowPane();
        flowPane.setAlignment(Pos.TOP_LEFT);
        flowPane.setHgap(16d);
        flowPane.setVgap(16d);
        TenantSettingViewModel.reviewersProperty().addListener((_, _, _) -> {
            flowPane.getChildren().retainAll();
            flowPane.getChildren().addAll(
                    TenantSettingViewModel.getReviewers().stream()
                            .map(reviewer -> new ReviewerCard(reviewer, modalPane))
                            .toList()
            );
        });
        return new VBox(separator(),
                hbox,
                new Spacer(10d, Orientation.VERTICAL),
                flowPane);
    }

    private void componentSizing() {
        scrollPane.widthProperty().addListener((_, _, nv) -> content.setPrefWidth(nv.doubleValue()));
    }

    private void setupComboBoxes() {
        // Combo box Converter.
        StringConverter<Currency> currencyConverter =
                FunctionalStringConverter.to(currency -> (currency == null) ? "" : currency.getDisplayName() + " (" + currency.getSymbol() + ")");

        // Combo box Filter Function.
        Function<String, Predicate<Currency>> currencyFilterFunction =
                searchStr ->
                        currency ->
                                currencyConverter.toString(currency).toLowerCase().contains(searchStr);

        // Combo box properties.
        defaultCurrencyPicker.setConverter(currencyConverter);
        if (CurrencyViewModel.getCurrencies().isEmpty()) {
            CurrencyViewModel.getCurrencies()
                    .addListener(
                            (ListChangeListener<Currency>)
                                    _ -> defaultCurrencyPicker.setItems(CurrencyViewModel.getCurrencies()));
        } else {
            defaultCurrencyPicker.itemsProperty().bindBidirectional(CurrencyViewModel.currencyProperty());
        }
    }

    private Void onDataInitializationFailure(Throwable throwable) {
        SpotyLogger.writeToFile(throwable, CompanySettings.class);
        this.errorMessage("An error occurred while loading view");
        return null;
    }

    @Override
    public void onRendered() {
        super.onRendered();
        progress();
        CurrencyViewModel.getAllCurrencies();

        CompletableFuture<Void> allDataInitialization = CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> EmployeeViewModel.getAllEmployees(null, null, null, null)),
                CompletableFuture.runAsync(() -> TenantSettingViewModel.getTenantSettings(null, this::errorMessage)));

        allDataInitialization.thenRun(this::onDataInitializationSuccess)
                .exceptionally(this::onDataInitializationFailure);
    }
}
