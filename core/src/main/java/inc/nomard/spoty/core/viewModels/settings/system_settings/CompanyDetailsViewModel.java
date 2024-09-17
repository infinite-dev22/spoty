package inc.nomard.spoty.core.viewModels.settings.system_settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import inc.nomard.spoty.network_bridge.dtos.Company;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.repositories.implementations.CompanyRepositoryImpl;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.adapters.LocalDateTimeTypeAdapter;
import inc.nomard.spoty.utils.adapters.LocalDateTypeAdapter;
import inc.nomard.spoty.utils.adapters.LocalTimeTypeAdapter;
import inc.nomard.spoty.utils.adapters.UnixEpochDateTypeAdapter;
import inc.nomard.spoty.utils.connectivity.Connectivity;
import inc.nomard.spoty.utils.functional_paradigm.SpotyGotFunctional;
import javafx.application.Platform;
import javafx.beans.property.*;
import lombok.extern.java.Log;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Currency;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Log
public class CompanyDetailsViewModel {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    new UnixEpochDateTypeAdapter())
            .registerTypeAdapter(LocalDate.class,
                    new LocalDateTypeAdapter())
            .registerTypeAdapter(LocalTime.class,
                    new LocalTimeTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class,
                    new LocalDateTimeTypeAdapter())
            .create();
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final StringProperty website = new SimpleStringProperty("");
    private static final StringProperty phone = new SimpleStringProperty("");
    private static final StringProperty email = new SimpleStringProperty("");
    private static final StringProperty postalAddress = new SimpleStringProperty("");
    private static final StringProperty physicalAddress = new SimpleStringProperty("");
    private static final StringProperty tagLine = new SimpleStringProperty("");
    private static final StringProperty logo = new SimpleStringProperty("");
    private static final BooleanProperty logoOnReports = new SimpleBooleanProperty(false);
    private static final BooleanProperty logoOnEmails = new SimpleBooleanProperty(false);
    private static final BooleanProperty logoOnReceipts = new SimpleBooleanProperty(false);
    private static final StringProperty twitter = new SimpleStringProperty("");
    private static final StringProperty facebook = new SimpleStringProperty("");
    private static final StringProperty linkedin = new SimpleStringProperty("");
    private static final ObjectProperty<Currency> defaultCurrency = new SimpleObjectProperty<>(null);
    public static CompanyRepositoryImpl companyRepository = new CompanyRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        CompanyDetailsViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        CompanyDetailsViewModel.nameProperty().set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static String getWebsite() {
        return website.get();
    }

    public static void setWebsite(String website) {
        CompanyDetailsViewModel.website.set(website);
    }

    public static StringProperty websiteProperty() {
        return website;
    }

    public static String getPhone() {
        return phone.get();
    }

    public static void setPhone(String phone) {
        CompanyDetailsViewModel.phone.set(phone);
    }

    public static StringProperty phoneProperty() {
        return phone;
    }

    public static String getEmail() {
        return email.get();
    }

    public static void setEmail(String email) {
        CompanyDetailsViewModel.email.set(email);
    }

    public static StringProperty emailProperty() {
        return email;
    }

    public static String getPostalAddress() {
        return postalAddress.get();
    }

    public static void setPostalAddress(String postalAddress) {
        CompanyDetailsViewModel.postalAddress.set(postalAddress);
    }

    public static StringProperty postalAddressProperty() {
        return postalAddress;
    }

    public static String getPhysicalAddress() {
        return physicalAddress.get();
    }

    public static void setPhysicalAddress(String physicalAddress) {
        CompanyDetailsViewModel.physicalAddress.set(physicalAddress);
    }

    public static StringProperty physicalAddressProperty() {
        return physicalAddress;
    }

    public static String getTagLine() {
        return tagLine.get();
    }

    public static void setTagLine(String tagLine) {
        CompanyDetailsViewModel.tagLine.set(tagLine);
    }

    public static StringProperty tagLineProperty() {
        return tagLine;
    }

    public static String getLogo() {
        return logo.get();
    }

    public static void setLogo(String logo) {
        CompanyDetailsViewModel.logo.set(logo);
    }

    public static StringProperty logoProperty() {
        return logo;
    }

    public static boolean isLogoOnReports() {
        return logoOnReports.get();
    }

    public static void setLogoOnReports(boolean logoOnReports) {
        CompanyDetailsViewModel.logoOnReports.set(logoOnReports);
    }

    public static BooleanProperty logoOnReportsProperty() {
        return logoOnReports;
    }

    public static boolean isLogoOnEmails() {
        return logoOnEmails.get();
    }

    public static void setLogoOnEmails(boolean logoOnEmails) {
        CompanyDetailsViewModel.logoOnEmails.set(logoOnEmails);
    }

    public static BooleanProperty logoOnEmailsProperty() {
        return logoOnEmails;
    }

    public static boolean isLogoOnReceipts() {
        return logoOnReceipts.get();
    }

    public static void setLogoOnReceipts(boolean logoOnReceipts) {
        CompanyDetailsViewModel.logoOnReceipts.set(logoOnReceipts);
    }

    public static BooleanProperty logoOnReceiptsProperty() {
        return logoOnReceipts;
    }

    public static String getTwitter() {
        return twitter.get();
    }

    public static void setTwitter(String twitter) {
        CompanyDetailsViewModel.twitter.set(twitter);
    }

    public static StringProperty twitterProperty() {
        return twitter;
    }

    public static String getFacebook() {
        return facebook.get();
    }

    public static void setFacebook(String facebook) {
        CompanyDetailsViewModel.facebook.set(facebook);
    }

    public static StringProperty facebookProperty() {
        return facebook;
    }

    public static String getLinkedin() {
        return linkedin.get();
    }

    public static void setLinkedin(String linkedin) {
        CompanyDetailsViewModel.linkedin.set(linkedin);
    }

    public static StringProperty linkedinProperty() {
        return linkedin;
    }

    public static Currency getDefaultCurrency() {
        return defaultCurrency.get();
    }

    public static void setDefaultCurrency(Currency defaultCurrency) {
        CompanyDetailsViewModel.defaultCurrency.set(defaultCurrency);
    }

    public static ObjectProperty<Currency> defaultCurrencyProperty() {
        return defaultCurrency;
    }

    public static void saveCompany(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                   SpotyGotFunctional.MessageConsumer successMessage,
                                   SpotyGotFunctional.MessageConsumer errorMessage) {

        var company =
                Company.builder()
                        .name(getName())
                        .website(getWebsite())
                        .phone(getPhone())
                        .email(getEmail())
                        .postalAddress(getPostalAddress())
                        .physicalAddress(getPhysicalAddress())
                        .tagLine(getTagLine())
                        .logo(getLogo())
                        .logoOnReports(isLogoOnReports())
                        .logoOnEmails(isLogoOnEmails())
                        .logoOnReceipts(isLogoOnReceipts())
                        .twitter(getTwitter())
                        .facebook(getFacebook())
                        .linkedin(getLinkedin())
                        .defaultCurrency(getDefaultCurrency())
                        .build();
        CompletableFuture<HttpResponse<String>> responseFuture = companyRepository.post(company);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Company created successfully");
                });
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Access denied"));
                }
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Resource not found"));
                }
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An error occurred"));
                }
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            if (Connectivity.isConnectedToInternet()) {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An in app error occurred"));
                }
            } else {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("No Internet Connection"));
                }
            }
            SpotyLogger.writeToFile(throwable, CompanyDetailsViewModel.class);
            return null;
        });
    }

    public static void clearCompanyData() {
        setId(0L);
        setName("");
        setWebsite("");
        setPhone("");
        setEmail("");
        setPostalAddress("");
        setPhysicalAddress("");
        setTagLine("");
        setLogo("");
        setLogoOnReports(false);
        setLogoOnEmails(false);
        setLogoOnReceipts(false);
        setTwitter("");
        setFacebook("");
        setLinkedin("");
        setDefaultCurrency(null);
    }

    public static void getCompany(Long index,
                                  SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                  SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = companyRepository.fetch(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Company company = gson.fromJson(response.body(), Company.class);
                    setId(company.getId());
                    setName(company.getName());
                    setWebsite(company.getWebsite());
                    setPhone(company.getPhone());
                    setEmail(company.getEmail());
                    setPostalAddress(company.getPostalAddress());
                    setPhysicalAddress(company.getPhysicalAddress());
                    setTagLine(company.getTagLine());
                    setLogo(company.getLogo());
                    setLogoOnReports(company.isLogoOnReports());
                    setLogoOnEmails(company.isLogoOnEmails());
                    setLogoOnReceipts(company.isLogoOnReceipts());
                    setTwitter(company.getTwitter());
                    setFacebook(company.getFacebook());
                    setLinkedin(company.getLinkedin());
                    setDefaultCurrency(company.getDefaultCurrency());
                    onSuccess.run();
                });
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Access denied"));
                }
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Resource not found"));
                }
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An error occurred"));
                }
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            if (Connectivity.isConnectedToInternet()) {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An in app error occurred"));
                }
            } else {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("No Internet Connection"));
                }
            }
            SpotyLogger.writeToFile(throwable, CompanyDetailsViewModel.class);
            return null;
        });
    }

    public static void updateItem(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                  SpotyGotFunctional.MessageConsumer successMessage,
                                  SpotyGotFunctional.MessageConsumer errorMessage) {
        var company = Company.builder()
                .id(getId())
                .name(getName())
                .website(getWebsite())
                .phone(getPhone())
                .email(getEmail())
                .postalAddress(getPostalAddress())
                .physicalAddress(getPhysicalAddress())
                .tagLine(getTagLine())
                .logo(getLogo())
                .logoOnReports(isLogoOnReports())
                .logoOnEmails(isLogoOnEmails())
                .logoOnReceipts(isLogoOnReceipts())
                .twitter(getTwitter())
                .facebook(getFacebook())
                .linkedin(getLinkedin())
                .defaultCurrency(getDefaultCurrency())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = companyRepository.put(company);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Company details updated successfully");
                });
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Access denied"));
                }
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Resource not found"));
                }
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An error occurred"));
                }
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            if (Connectivity.isConnectedToInternet()) {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An in app error occurred"));
                }
            } else {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("No Internet Connection"));
                }
            }
            SpotyLogger.writeToFile(throwable, CompanyDetailsViewModel.class);
            return null;
        });
    }
}
