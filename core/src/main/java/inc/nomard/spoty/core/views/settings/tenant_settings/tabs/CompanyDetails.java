package inc.nomard.spoty.core.views.settings.tenant_settings.tabs;

import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.SpotyCoreResourceLoader;
import inc.nomard.spoty.core.viewModels.TenantSettingViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.SpotyProgressSpinner;
import inc.nomard.spoty.core.views.components.label_components.controls.LabeledTextField;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextArea;
import inc.nomard.spoty.core.views.layout.ModalContentHolder;
import inc.nomard.spoty.core.views.layout.SideModalPane;
import inc.nomard.spoty.core.views.layout.navigation.Spacer;
import inc.nomard.spoty.core.views.util.NodeUtils;
import inc.nomard.spoty.core.views.util.OutlinePage;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.utils.UIUtils;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
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
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class CompanyDetails extends OutlinePage {
    private final SideModalPane modalPane;
    public CustomButton saveBtn;
    public Circle companyLogo;
    public Text companyName,
            fileLabel,
            companyWeblink;
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
    private Circle currentCompanyLogo;
    private VBox companyLogoBtn;
    private FileChooser fileChooser;

    public CompanyDetails() {
        modalPane = new SideModalPane();
        getChildren().addAll(modalPane, init());
        progress();
        CompletableFuture.runAsync(() -> TenantSettingViewModel.getTenantSettings(this::onDataInitializationSuccess, this::errorMessage));
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
        pane.getStyleClass().add("card-flat-top");
        BorderPane.setAlignment(pane, Pos.CENTER);
        BorderPane.setMargin(pane, new Insets(0d));
        pane.getChildren().add(buildScrollPane());
        setIcons();
        prefillForms();
        showData();
        addDocument();
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
        content.getChildren().addAll(buildHero(),
                buildCompanyProfile(),
                separator(), buildCompanyPublicProfiles(),
                separator(), buildCompanyTagLine(),
                separator(), buildCompanyLogo(),
                separator(), buildCompanyBranding(),
                separator(), buildCompanySocialProfiles(),
                new Spacer(100, Orientation.VERTICAL));

        return content;
    }

    private HBox heroImage() {
        heroImage = new HBox();
        heroImage.setCache(true);
        heroImage.setCacheHint(CacheHint.SPEED);
        heroImage.setMaxHeight(140d);
        heroImage.setMinHeight(140d);
        heroImage.setPrefHeight(140d);
        heroImage.getStyleClass().add("hero-image");
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
        companyName = new Text();
        companyName.getStyleClass().addAll(Styles.TITLE_1, Styles.TEXT_ITALIC);

        companyWeblink = new Text();
        companyWeblink.getStyleClass().addAll(Styles.TEXT, Styles.TEXT_SUBTLE);
        linkIcon = new HBox();
        linkIcon.setAlignment(Pos.CENTER);
        linkIcon.setCursor(Cursor.HAND);
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(16d);
        hbox.getChildren().addAll(companyWeblink, linkIcon);
        var vbox = new VBox();
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setSpacing(8d);
        vbox.getChildren().addAll(companyName, hbox);
        vbox.setPadding(new Insets(40d, 0d, 0d, 0d));
        return vbox;
    }

    private HBox buildCompanyProfilePreview() {
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(16d);
        hbox.setPadding(new Insets(50d, 0d, 0d, 16d));
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
        var label = new Text(title);
        label.getStyleClass().addAll(Styles.TITLE_4);
        label.sceneProperty().addListener((_, _, newScene) -> {
            if (newScene != null) {
                // At this point, the parent is guaranteed to be set
                label.getParent().layoutBoundsProperty().addListener((_, _, newBounds) -> {
                    label.setWrappingWidth(newBounds.getWidth() * 0.8); // Set wrapping width to 80% of parent's width
                });
            }
        });
        var subLabel = new Text(subTitle);
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
        saveBtn = new CustomButton("Save Changes");
        saveBtn.getStyleClass().add(Styles.ACCENT);
        saveBtn.setOnAction(_ -> TenantSettingViewModel.updateTenantSettings(this::onSuccess, SpotyUtils::successMessage, this::errorMessage));
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
                vbox,
                spacer());
        return hbox;
    }

    private HBox buildCompanyTagLine() {
        companyTagLine = new ValidatableTextArea();
        companyTagLine.setPrefWidth(400d);
        companyTagLine.setPrefHeight(100d);
        companyTagLine.setMinHeight(100d);
        companyTagLine.setWrapText(true);
//        companyTagLine.setPrompt("Your company's tagline e.g. Just Do It, Think Different, Quality never goes out of style, etc.");
        var label = new Text("Tag line");
        label.getStyleClass().addAll(Styles.TEXT);
        var hbox = buildSection();
        hbox.getChildren().addAll(
                buildSectionTitle("Tag line",
                        "A quick snapshot of your company"),
                spacer(),
                new VBox(label, companyTagLine),
                spacer());
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
        fileLabel = new Text("Click to upload or Drag and drop png, jpg/jpeg or webp. (max. size 800 x 400 px)");
        fileLabel.setTextAlignment(TextAlignment.CENTER);
        fileLabel.getStyleClass().addAll(Styles.TEXT);
        fileLabel.sceneProperty().addListener((_, _, newScene) -> {
            if (newScene != null) {
                // At this point, the parent is guaranteed to be set
                fileLabel.getParent().layoutBoundsProperty().addListener((_, _, newBounds) -> {
                    fileLabel.setWrappingWidth(newBounds.getWidth() * 0.8); // Set wrapping width to 80% of parent's width
                });
            }
        });
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
                buildLogoUI(),
                spacer());
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
                vbox,
                spacer());
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
                vbox,
                spacer());
        return hbox;
    }

    private void setIcons() {
        var link = new FontIcon(FontAwesomeSolid.LINK);
        link.setIconColor(Color.web("#00AEFF"));
        linkIcon.getChildren().add(link);
    }

    private void componentSizing() {
        scrollPane.widthProperty().addListener((_, _, nv) -> content.setPrefWidth(nv.doubleValue()));
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
        companyName
                .textProperty()
                .bindBidirectional(TenantSettingViewModel.nameProperty());
        companyWeblink
                .textProperty()
                .bindBidirectional(TenantSettingViewModel.websiteLinkProperty());
    }

    private void addDocument() {
        var upload = new FontIcon(FontAwesomeRegular.FILE_IMAGE);
        upload.setIconSize(60);
        upload.setIconColor(Color.web("#C2C2C2"));
        uploadIcon.getChildren().add(upload);

        if (Objects.equals(fileChooser, null)) {
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.png, *.jpeg, *.webp)", "*.*.png", "*.jpeg", "*.webp");
            fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(extFilter);
        }
        companyLogoBtn.setOnMouseClicked(_ -> {
            var file = fileChooser.showOpenDialog(new Stage());
            if (Objects.nonNull(file)) {
                if (SpotyUtils.getFileExtension(file).toLowerCase().contains("png")
                        || SpotyUtils.getFileExtension(file).toLowerCase().contains("jpg")
                        || SpotyUtils.getFileExtension(file).toLowerCase().contains("jpeg")
                        || SpotyUtils.getFileExtension(file).toLowerCase().contains("webp")) {
                    fileLabel.setText("File: " + file.getName());
                    try {
                        setLogoImage(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    SpotyUtils.errorMessage("Un-Supported file type: " + SpotyUtils.getFileExtension(file));
                }
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
            if (db.hasFiles() && db.getFiles().size() == 1) {
                if (SpotyUtils.getFileExtension(db.getFiles().getFirst()).toLowerCase().contains("png")
                        || SpotyUtils.getFileExtension(db.getFiles().getFirst()).toLowerCase().contains("jpg")
                        || SpotyUtils.getFileExtension(db.getFiles().getFirst()).toLowerCase().contains("jpeg")
                        || SpotyUtils.getFileExtension(db.getFiles().getFirst()).toLowerCase().contains("webp")) {
                    fileLabel.setText("File: " + db.getFiles().getFirst().getName());
                    try {
                        setLogoImage(db.getFiles().getFirst());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    success = true;
                } else {
                    SpotyUtils.errorMessage("Un-Supported file type: " + SpotyUtils.getFileExtension(db.getFiles().getFirst()));
                }
            } else if (db.getFiles().size() > 1) {
                SpotyUtils.errorMessage("Un-Supported file action, drop only single file");
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void setLogoImage(File file) throws IOException {
        var bufferedImage = ImageIO.read(file);
        Image logoImage = javafx.embed.swing.SwingFXUtils.toFXImage(bufferedImage, null);
        currentCompanyLogo.setFill(new ImagePattern(logoImage));
    }
}
