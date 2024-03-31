package inc.nomard.spoty.core.views.settings.system_settings;

import inc.nomard.spoty.core.SpotyCoreResourceLoader;
import inc.nomard.spoty.core.viewModels.CurrencyViewModel;
import inc.nomard.spoty.core.viewModels.settings.system_settings.CompanyDetailsViewModel;
import inc.nomard.spoty.network_bridge.dtos.Currency;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxcore.controls.Label;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

public class CompanyDetailsController implements Initializable {
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXButton cancelBtn;
    @FXML
    public MFXButton saveBtn;
    @FXML
    public Circle companyLogo;
    @FXML
    public Label companyName;
    @FXML
    public Label companyWeblink;
    @FXML
    public HBox linkIcon;
    @FXML
    public MFXTextField companyNameTxt;
    @FXML
    public MFXTextField companyWebLinkTxt;
    @FXML
    public MFXTextField companyPhoneTxt;
    @FXML
    public MFXTextField companyEmailTxt;
    @FXML
    public MFXTextField companyPostalAddressTxt;
    @FXML
    public MFXTextField companyAddressTxt;
    @FXML
    public MFXTextField CompanyTagLine;
    @FXML
    public Circle currentCompanyLogo;
    @FXML
    public VBox companyLogoBtn;
    @FXML
    public HBox uploadIcon;
    @FXML
    public MFXCheckbox reportsCheck;
    @FXML
    public MFXCheckbox emailsCheck;
    @FXML
    public MFXCheckbox receiptsCheck;
    @FXML
    public MFXTextField companyTwitter;
    @FXML
    public MFXTextField companyFacebook;
    @FXML
    public MFXTextField companyLinkedin;
    @FXML
    public MFXScrollPane scrollPane;
    @FXML
    public VBox content;
    @FXML
    public HBox heroImage;
    @FXML
    public MFXFilterComboBox<Currency> defaultCurrencyPicker;

    private void setIcons() {
        var link = new MFXFontIcon("fas-link", Color.web("#00AEFF"));
        var upload = new MFXFontIcon("fas-cloud-arrow-up", 60, Color.web("#C2C2C2"));
        linkIcon.getChildren().add(link);
        uploadIcon.getChildren().add(upload);
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
}
