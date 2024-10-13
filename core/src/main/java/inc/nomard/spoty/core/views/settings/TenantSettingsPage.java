package inc.nomard.spoty.core.views.settings;

import inc.nomard.spoty.core.views.components.SpotyProgressSpinner;
import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.SpotyCoreResourceLoader;
import inc.nomard.spoty.core.viewModels.CurrencyViewModel;
import inc.nomard.spoty.core.viewModels.TenantSettingViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.label_components.controls.LabeledComboBox;
import inc.nomard.spoty.core.views.components.label_components.controls.LabeledTextField;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextArea;
import inc.nomard.spoty.core.views.layout.ModalContentHolder;
import inc.nomard.spoty.core.views.layout.SideModalPane;
import inc.nomard.spoty.core.views.pages.EmployeePage;
import inc.nomard.spoty.core.views.util.FunctionalStringConverter;
import inc.nomard.spoty.core.views.util.NodeUtils;
import inc.nomard.spoty.core.views.util.OutlinePage;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.UIUtils;
import inc.nomard.spoty.utils.navigation.Spacer;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.extern.log4j.Log4j2;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Currency;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;

@Log4j2
public class TenantSettingsPage extends OutlinePage {
    private final SideModalPane modalPane;
    public CustomButton saveBtn;
    public Circle companyLogo;
    public Label companyName,
            companyWeblink,
            fileLabel;
    public HBox linkIcon,
            uploadIcon,
            heroImage;
    public LabeledTextField companyNameTxt,
            companyWebLinkTxt,
            companyPhoneTxt,
            companyEmailTxt,
            companyPostalAddressTxt,
            companyAddressTxt,
            companyTwitter,
            companyFacebook,
            companyLinkedin;
    public ValidatableTextArea companyTagLine;
    public VBox content;
    public CheckBox reportsCheck,
            emailsCheck,
            receiptsCheck;
    public ScrollPane scrollPane;
    public LabeledComboBox<Currency> defaultCurrencyPicker;
    private Circle currentCompanyLogo;
    private VBox companyLogoBtn;
    private FileChooser fileChooser;

    public TenantSettingsPage() {
        modalPane = new SideModalPane();
        getChildren().addAll(modalPane, init());
        progress();
        CompletableFuture<Void> allDataInitialization = CompletableFuture.allOf(
                CompletableFuture.runAsync(CurrencyViewModel::getAllCurrencies),
                CompletableFuture.runAsync(() -> TenantSettingViewModel.getTenantSettings(null, null)));

        allDataInitialization.thenRun(this::onDataInitializationSuccess)
                .exceptionally(this::onDataInitializationFailure);
    }

    private Void onDataInitializationFailure(Throwable throwable) {
        SpotyLogger.writeToFile(throwable, EmployeePage.class);
        this.errorMessage("An error occurred while loading view");
        return null;
    }

    private void onDataInitializationSuccess() {
        modalPane.hide(true);
        modalPane.setPersistent(false);
    }

    private void errorMessage(String message) {
        SpotyUtils.errorMessage(message);
        modalPane.hide(true);
        modalPane.setPersistent(false);
    }

    public void progress() {
        var dialog = new ModalContentHolder(50, 50);
        dialog.getChildren().add(new SpotyProgressSpinner());
        dialog.setPadding(new Insets(5d));
        modalPane.setAlignment(Pos.CENTER);
        modalPane.show(dialog);
        modalPane.setPersistent(true);
    }

    private AnchorPane init() {
        var pane = new AnchorPane();
        pane.getStyleClass().add("card-flat");
        BorderPane.setAlignment(pane, Pos.CENTER);
        BorderPane.setMargin(pane, new Insets(0d));
        pane.getChildren().add(buildScrollPane());
        setIcons();
        setupComboBoxes();
        prefillForms();
        showData();
        addDocument();
        return pane;
    }

    private ScrollPane buildScrollPane() {
        scrollPane = new ScrollPane(buildContent());
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        componentSizing();
        NodeUtils.setAnchors(scrollPane, new Insets(0d));
        return scrollPane;
    }

    private VBox buildContent() {
        var vbox = new VBox();
        vbox.setSpacing(16d);
        vbox.setPadding(new Insets(16d));
        vbox.getChildren().addAll(buildHero(),
                buildCompanyProfile(),
                separator(), buildCompanyPublicProfiles(),
                separator(), buildCompanyTagLine(),
                separator(), buildCompanyLogo(),
                separator(), buildCompanyBranding(),
                separator(), buildCompanySocialProfiles(),
                separator(), buildCompanyDefaults());
        content = new VBox(vbox);
        content.setSpacing(16d);

        return content;
    }

    private HBox heroImage() {
        heroImage = new HBox();
        heroImage.setCache(true);
        heroImage.setCacheHint(CacheHint.SPEED);
        heroImage.setMaxHeight(140d);
        heroImage.setMinHeight(140d);
        heroImage.setPrefHeight(140d);
        heroImage.getStyleClass().add(Styles.ACCENT);
        UIUtils.anchor(heroImage, 0d, 0d, null, 0d);
        return heroImage;
    }

    private Circle buildCompanyLogoHolder() {
        companyLogo = new Circle();
        companyLogo.setCache(true);
        companyLogo.setCacheHint(CacheHint.SPEED);
        companyLogo.setFill(Color.web("#4e4f5400"));
        companyLogo.setRadius(100d);
        companyLogo.setStroke(Color.web("#ffffff00"));
        companyLogo.setStrokeType(StrokeType.INSIDE);
        companyLogo.setStrokeWidth(0d);
        return companyLogo;
    }

    private VBox buildCompanyDetailsPreview() {
        companyName = new Label();
        companyName.getStyleClass().add("h3");

        companyWeblink = new Label();
        companyWeblink.setTextFill(Color.web("#908686"));
        companyWeblink.setWrapText(true);
        companyName.getStyleClass().add("fs-italic");
        linkIcon = new HBox();
        linkIcon.setAlignment(Pos.CENTER);
        linkIcon.setCursor(Cursor.HAND);
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(16d);
        hbox.getChildren().addAll(companyWeblink, linkIcon);
        var vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(8d);
        vbox.getChildren().addAll(companyName, hbox);
        vbox.setPadding(new Insets(40d, 0d, 0d, 0d));
        return vbox;
    }

    private HBox buildCompanyProfilePreview() {
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(16d);
        hbox.setPadding(new Insets(0d, 0d, 0d, 16d));
        UIUtils.anchor(hbox, null, 0d, 0d, 0d);
        hbox.getChildren().addAll(buildCompanyLogoHolder(), buildCompanyDetailsPreview());
        return hbox;
    }

    private AnchorPane buildHero() {
        var hero = new AnchorPane();
        hero.setPrefHeight(250d);
        VBox.setVgrow(hero, Priority.ALWAYS);
        hero.getChildren().addAll(heroImage(), buildCompanyProfilePreview());
        return hero;
    }

    private HBox buildSection() {
        var hbox = new HBox();
        hbox.setSpacing(8d);
        return hbox;
    }

    private VBox buildSectionGroup() {
        var vbox = new VBox();
        vbox.setPrefWidth(450d);
        vbox.setSpacing(8d);
        return vbox;
    }

    private VBox buildSectionTitle(String title, String subTitle) {
        var label = new Label(title);
        label.setWrapText(true);
        label.getStyleClass().add("h5");
        var subLabel = new Label(subTitle);
        subLabel.setWrapText(true);
        subLabel.getStyleClass().add("disabled-text");
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

    private HBox buildCompanyProfile() {
        // Save button.
        saveBtn = new CustomButton("_Save Changes");
        saveBtn.getStyleClass().add(Styles.ACCENT);
        saveBtn.setOnAction(mouseEvent -> TenantSettingViewModel.updateTenantSettings(this::onSuccess, SpotyUtils::successMessage, this::errorMessage));
        var hbox = new HBox();
        hbox.setSpacing(16d);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(
                buildSectionTitle("Company profile",
                        "Update your company logo and details here."),
                new Spacer(),
                saveBtn);
        return hbox;
    }

    private void onSuccess() {
        TenantSettingViewModel.getTenantSettings(null, null);
    }

    private HBox buildCompanyPublicProfiles() {
        // Company name text box.
        companyNameTxt = new LabeledTextField();
        companyNameTxt.setLabel("Company name");
        companyNameTxt.setPrefWidth(400d);
        // Website link text box.
        companyWebLinkTxt = new LabeledTextField();
        companyWebLinkTxt.setLabel("Website link");
        companyWebLinkTxt.setPrefWidth(400d);
        // Phone text box.
        companyPhoneTxt = new LabeledTextField();
        companyPhoneTxt.setLabel("Phone");
        companyPhoneTxt.setPrefWidth(400d);
        // Email text box.
        companyEmailTxt = new LabeledTextField();
        companyEmailTxt.setLabel("Email");
        companyEmailTxt.setPrefWidth(400d);
        // Postal Address text box.
        companyPostalAddressTxt = new LabeledTextField();
        companyPostalAddressTxt.setLabel("Postal Address (P.O.Box)");
        companyPostalAddressTxt.setPrefWidth(400d);
        // Company Address text box.
        companyAddressTxt = new LabeledTextField();
        companyAddressTxt.setLabel("Physical address");
        companyAddressTxt.setPrefWidth(400d);
        var vbox = new VBox();
        vbox.setSpacing(16d);
        vbox.getChildren().addAll(companyNameTxt,
                companyWebLinkTxt,
                companyPhoneTxt,
                companyEmailTxt,
                companyPostalAddressTxt,
                companyAddressTxt);
        var hbox = buildSection();
        hbox.setPrefWidth(400d);
        hbox.getChildren().addAll(
                buildSectionTitle("Social profiles",
                        "Add your company's social profiles."),
                spacer(),
                vbox);
        return hbox;
    }

    private HBox buildCompanyTagLine() {
        companyTagLine = new ValidatableTextArea();
        companyTagLine.setPrefWidth(400d);
        companyTagLine.setPrefHeight(100d);
        companyTagLine.setMinHeight(100d);
        companyTagLine.setWrapText(true);
//        companyTagLine.setPrompt("Your company's tagline e.g. Just Do It, Think Different, Quality never goes out of style, etc.");
        var label = new Label("Tag line");
        var hbox = buildSection();
        hbox.getChildren().addAll(
                buildSectionTitle("Tag line",
                        "A quick snapshot of your company"),
                spacer(),
                new VBox(label, companyTagLine));
        return hbox;
    }

    private Circle buildImagePreview() {
        currentCompanyLogo = new Circle();
        currentCompanyLogo.setCache(true);
        currentCompanyLogo.setCacheHint(CacheHint.SPEED);
        currentCompanyLogo.setRadius(50d);
        currentCompanyLogo.setStrokeType(StrokeType.INSIDE);
        currentCompanyLogo.setStrokeWidth(0d);
        return currentCompanyLogo;
    }

    private HBox buildDropTarget() {
        // File icon.
        uploadIcon = new HBox();
        uploadIcon.setAlignment(Pos.CENTER);
        // Label/file name.
        fileLabel = new Label("Click to upload or Drag and drop png, jpg or gif. (max. size 800 x 400 px)");
        fileLabel.setTextAlignment(TextAlignment.CENTER);
        fileLabel.setWrapText(true);
        // Drop target and file explorer button.
        companyLogoBtn = new VBox();
        companyLogoBtn.setAlignment(Pos.CENTER);
        companyLogoBtn.setPrefWidth(400);
        companyLogoBtn.setSpacing(8d);
        companyLogoBtn.getStyleClass().add("card-flat");
        companyLogoBtn.setPadding(new Insets(16));
        companyLogoBtn.setCursor(Cursor.HAND);
        companyLogoBtn.getChildren().addAll(uploadIcon, fileLabel);
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(16d);
        hbox.getChildren().add(companyLogoBtn);
        return hbox;
    }

    private HBox buildLogoUI() {
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(16d);
        hbox.getChildren().addAll(buildImagePreview(), buildDropTarget());
        return hbox;
    }

    private HBox buildCompanyLogo() {
        var hbox = buildSection();
        hbox.getChildren().addAll(
                buildSectionTitle("Company logo",
                        "Update your company logo."),
                spacer(),
                buildLogoUI());
        return hbox;
    }

    private VBox buildCheckBoxes() {
        reportsCheck = new CheckBox("Add logo on reports.");
        emailsCheck = new CheckBox("Add logo to emails.");
        receiptsCheck = new CheckBox("Add logo on receipts.");
        var vbox = new VBox();
        vbox.setSpacing(8d);
        vbox.getChildren().addAll(reportsCheck, emailsCheck, receiptsCheck);
        return vbox;
    }

    private HBox buildCompanyBranding() {
        var vbox = new VBox();
        HBox.setHgrow(vbox, Priority.ALWAYS);
        vbox.setSpacing(8d);
        vbox.setMinWidth(100d);
        vbox.setPrefWidth(200d);
        vbox.setMaxWidth(200d);
        vbox.getChildren().addAll(buildCheckBoxes());
        var hbox = buildSection();
        hbox.getChildren().addAll(
                buildSectionTitle("Branding",
                        "Add your company's logo to documents, reports, emails, etc..."),
                spacer(),
                vbox);
        return hbox;
    }

    private HBox buildCompanySocialProfiles() {
        // Twitter text box.
        var twitterIcon = new FontIcon(FontAwesomeBrands.TWITTER);
        companyTwitter = new LabeledTextField();
        companyTwitter.setPrefWidth(400d);
        companyTwitter.setLabel("Twitter(X)");
        companyTwitter.setRight(twitterIcon);
        // Facebook text box.
        var facebookIcon = new FontIcon(FontAwesomeBrands.FACEBOOK_F);
        companyFacebook = new LabeledTextField();
        companyFacebook.setPrefWidth(400d);
        companyFacebook.setLabel("Facebook");
        companyFacebook.setRight(facebookIcon);
        // LinkedIn text box.
        var linkedinIcon = new FontIcon(FontAwesomeBrands.LINKEDIN);
        companyLinkedin = new LabeledTextField();
        companyLinkedin.setPrefWidth(400d);
        companyLinkedin.setLabel("LinkedIn");
        companyLinkedin.setRight(linkedinIcon);
        var vbox = new VBox();
        vbox.setSpacing(16d);
        vbox.getChildren().addAll(companyTwitter, companyFacebook, companyLinkedin);
        var hbox = buildSection();
        hbox.setPrefWidth(400d);
        hbox.getChildren().addAll(
                buildSectionTitle("Social profiles",
                        "Add your company's social profiles."),
                spacer(),
                vbox);
        return hbox;
    }

    private HBox buildCompanyDefaults() {
        defaultCurrencyPicker = new LabeledComboBox<Currency>();
        defaultCurrencyPicker.setLabel("Default Currency");
        defaultCurrencyPicker.setPrefWidth(400d);
        var hbox = new HBox();
        hbox.setPrefHeight(200d);
        hbox.setSpacing(8d);
        hbox.getChildren().addAll(
                buildSectionTitle("Company Defaults",
                        "Add your company's default settings. These will be used for certain functionalities" +
                                " in the applications run time and processing."),
                spacer(),
                defaultCurrencyPicker);
        return hbox;
    }

    private void setIcons() {
        var link = new FontIcon(FontAwesomeSolid.LINK);
        link.setIconColor(Color.web("#00AEFF"));
        linkIcon.getChildren().add(link);
    }

    private void componentSizing() {
        scrollPane.widthProperty().addListener((observableValue, ov, nv) -> {
            content.setPrefWidth(nv.doubleValue());
            heroImage.setMinWidth(nv.doubleValue());
            heroImage.setPrefWidth(nv.doubleValue());
            heroImage.setMaxWidth(nv.doubleValue());
        });
    }

    private void prefillForms() {
        companyNameTxt
                .textProperty()
                .bindBidirectional(TenantSettingViewModel.nameProperty());
        companyWebLinkTxt
                .textProperty()
                .bindBidirectional(TenantSettingViewModel.websiteLinkProperty());
        companyPhoneTxt
                .textProperty()
                .bindBidirectional(TenantSettingViewModel.phoneNumberProperty());
        companyEmailTxt
                .textProperty()
                .bindBidirectional(TenantSettingViewModel.emailProperty());
        companyPostalAddressTxt
                .textProperty()
                .bindBidirectional(TenantSettingViewModel.postalAddressProperty());
        companyAddressTxt
                .textProperty()
                .bindBidirectional(TenantSettingViewModel.physicalAddressProperty());
        companyTagLine
                .textProperty()
                .bindBidirectional(TenantSettingViewModel.tagLineProperty());
        companyTagLine
                .textProperty()
                .bindBidirectional(TenantSettingViewModel.tagLineProperty());
        reportsCheck.selectedProperty()
                .bindBidirectional(TenantSettingViewModel.reportLogoProperty());
        emailsCheck
                .selectedProperty()
                .bindBidirectional(TenantSettingViewModel.emailLogoProperty());
        receiptsCheck
                .selectedProperty()
                .bindBidirectional(TenantSettingViewModel.receiptLogoProperty());
        companyTwitter
                .textProperty()
                .bindBidirectional(TenantSettingViewModel.twitterProperty());
        companyFacebook
                .textProperty()
                .bindBidirectional(TenantSettingViewModel.facebookProperty());
        companyLinkedin
                .textProperty()
                .bindBidirectional(TenantSettingViewModel.linkedInProperty());

        defaultCurrencyPicker.setValue(TenantSettingViewModel.getDefaultCurrency());
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
                                    c -> defaultCurrencyPicker.setItems(CurrencyViewModel.getCurrencies()));
        } else {
            defaultCurrencyPicker.itemsProperty().bindBidirectional(CurrencyViewModel.currencyProperty());
        }
    }

    private void showData() {
        if (Objects.nonNull(TenantSettingViewModel.getLogo()) && !TenantSettingViewModel.getLogo().isEmpty() && !TenantSettingViewModel.getLogo().isBlank()) {
            var image = new Image(
                    TenantSettingViewModel.getLogo(),
                    10000,
                    10000,
                    true,
                    true
            );
            companyLogo.setFill(new ImagePattern(image));
            currentCompanyLogo.setFill(new ImagePattern(image));
        } else {
            var image = new Image(
                    SpotyCoreResourceLoader.load("images/user-place-holder.png"),
                    10000,
                    10000,
                    true,
                    true
            );
            companyLogo.setFill(new ImagePattern(image));
            currentCompanyLogo.setFill(new ImagePattern(image));
        }
        if (TenantSettingViewModel.nameProperty().get().isEmpty()) {
            companyName.setText("Company Name");
        } else {
            companyName
                    .textProperty()
                    .bindBidirectional(TenantSettingViewModel.nameProperty());
        }
        if (TenantSettingViewModel.nameProperty().get().isEmpty()) {
            companyWeblink.setText("Company Website");
        } else {
            companyWeblink
                    .textProperty()
                    .bindBidirectional(TenantSettingViewModel.websiteLinkProperty());
        }
    }

    private void addDocument() {
        var upload = new FontIcon(FontAwesomeRegular.FILE_IMAGE);
        upload.setIconSize(60);
        upload.setIconColor(Color.web("#C2C2C2"));
        uploadIcon.getChildren().add(upload);

        if (Objects.equals(fileChooser, null)) {
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.png, *.jpeg)", "*.*.png", "*.jpeg");
            fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(extFilter);
        }
        companyLogoBtn.setOnMouseClicked(event -> {
            var file = fileChooser.showOpenDialog(new Stage());
            if (Objects.nonNull(file)) {
                fileLabel.setText(file.getName());
            }
        });


        companyLogoBtn.setOnDragOver(event -> {
            if (event.getGestureSource() != companyLogoBtn
                    && event.getDragboard().hasFiles()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        companyLogoBtn.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                fileLabel.setText(db.getFiles().getFirst().getName());
                System.out.println("Dropped: " + db.getString() + " " + db.getFiles().toString());
                success = true;
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);
            event.consume();
        });
    }
}
