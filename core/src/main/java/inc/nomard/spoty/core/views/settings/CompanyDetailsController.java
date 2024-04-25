package inc.nomard.spoty.core.views.settings;

import inc.nomard.spoty.core.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.settings.system_settings.*;
import inc.nomard.spoty.network_bridge.dtos.Currency;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.utils.*;
import io.github.palexdev.materialfx.utils.others.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxcore.controls.*;
import io.github.palexdev.mfxcore.controls.Label;
import io.github.palexdev.mfxresources.fonts.*;
import java.net.*;
import java.util.*;
import java.util.function.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;
import javafx.util.*;

public class CompanyDetailsController implements Initializable {
    private static CompanyDetailsController instance;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXButton cancelBtn,
            saveBtn;
    @FXML
    public Circle companyLogo,
            currentCompanyLogo;
    @FXML
    public Label companyName,
            companyWeblink,
            fileLabel;
    @FXML
    public HBox linkIcon,
            uploadIcon,
            heroImage;
    @FXML
    public MFXTextField companyNameTxt,
            companyWebLinkTxt,
            companyPhoneTxt,
            companyEmailTxt,
            companyPostalAddressTxt,
            companyAddressTxt,
            companyTwitter,
            companyFacebook,
            companyLinkedin;
    @FXML
    public TextArea CompanyTagLine;
    @FXML
    public VBox companyLogoBtn,
            content;
    @FXML
    public MFXCheckbox reportsCheck,
            emailsCheck,
            receiptsCheck;
    @FXML
    public MFXScrollPane scrollPane;
    @FXML
    public MFXFilterComboBox<Currency> defaultCurrencyPicker;
    private FileChooser fileChooser;

    public static CompanyDetailsController getInstance() {
        if (instance == null) instance = new CompanyDetailsController();
        return instance;
    }

    private void setIcons() {
        var link = new MFXFontIcon("fas-link", Color.web("#00AEFF"));
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setIcons();
        componentSizing();
        setupComboBoxes();
        prefillForms();
        showData();
        addDocument();
    }

    private void prefillForms() {
        companyNameTxt
                .textProperty()
                .bindBidirectional(CompanyDetailsViewModel.nameProperty());
        companyWebLinkTxt
                .textProperty()
                .bindBidirectional(CompanyDetailsViewModel.websiteProperty());
        companyPhoneTxt
                .textProperty()
                .bindBidirectional(CompanyDetailsViewModel.phoneProperty());
        companyEmailTxt
                .textProperty()
                .bindBidirectional(CompanyDetailsViewModel.emailProperty());
        companyPostalAddressTxt
                .textProperty()
                .bindBidirectional(CompanyDetailsViewModel.postalAddressProperty());
        companyAddressTxt
                .textProperty()
                .bindBidirectional(CompanyDetailsViewModel.physicalAddressProperty());
        CompanyTagLine
                .textProperty()
                .bindBidirectional(CompanyDetailsViewModel.tagLineProperty());
//        Company logo.
//        CompanyTagLine
//                .textProperty()
//                .bindBidirectional(CompanyDetailsViewModel.tagLineProperty());
        reportsCheck.selectedProperty()
                .bindBidirectional(CompanyDetailsViewModel.logoOnReportsProperty());
        emailsCheck
                .selectedProperty()
                .bindBidirectional(CompanyDetailsViewModel.logoOnEmailsProperty());
        receiptsCheck
                .selectedProperty()
                .bindBidirectional(CompanyDetailsViewModel.logoOnReceiptsProperty());
        companyTwitter
                .textProperty()
                .bindBidirectional(CompanyDetailsViewModel.twitterProperty());
        companyFacebook
                .textProperty()
                .bindBidirectional(CompanyDetailsViewModel.facebookProperty());
        companyLinkedin
                .textProperty()
                .bindBidirectional(CompanyDetailsViewModel.linkedinProperty());

        defaultCurrencyPicker.setValue(CompanyDetailsViewModel.getDefaultCurrency());
    }

    private void setupComboBoxes() {
        // Combo box Converter.
        StringConverter<Currency> currencyConverter =
                FunctionalStringConverter.to(currency -> (currency == null) ? "" : currency.getName());

        // Combo box Filter Function.
        Function<String, Predicate<Currency>> currencyFilterFunction =
                searchStr ->
                        currency ->
                                StringUtils.containsIgnoreCase(currencyConverter.toString(currency), searchStr);

        // Combo box properties.
        defaultCurrencyPicker.setItems(CurrencyViewModel.getCurrencies());
        defaultCurrencyPicker.setConverter(currencyConverter);
        defaultCurrencyPicker.setFilterFunction(currencyFilterFunction);
    }

    private void showData() {
        if (Objects.nonNull(CompanyDetailsViewModel.getLogo()) && !CompanyDetailsViewModel.getLogo().isEmpty() && !CompanyDetailsViewModel.getLogo().isBlank()) {
            var image = new Image(
                    CompanyDetailsViewModel.getLogo(),
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
        if (CompanyDetailsViewModel.nameProperty().get().isEmpty()) {
            companyName.setText("Company Name");
        } else {
            companyName
                    .textProperty()
                    .bindBidirectional(CompanyDetailsViewModel.nameProperty());
        }
        if (CompanyDetailsViewModel.nameProperty().get().isEmpty()) {
            companyWeblink.setText("Company Website");
        } else {
            companyWeblink
                    .textProperty()
                    .bindBidirectional(CompanyDetailsViewModel.websiteProperty());
        }
    }

    private void addDocument() {
        var upload = new MFXFontIcon();
        upload.setIconsProvider(IconsProviders.FONTAWESOME_REGULAR);
        upload.setDescription("far-file-image");
        upload.setSize(60);
        upload.setColor(Color.web("#C2C2C2"));
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
