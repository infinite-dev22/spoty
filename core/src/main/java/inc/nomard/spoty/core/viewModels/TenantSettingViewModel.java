package inc.nomard.spoty.core.viewModels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import inc.nomard.spoty.network_bridge.dtos.Reviewer;
import inc.nomard.spoty.network_bridge.dtos.TenantSettings;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Employee;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.repositories.implementations.TenantSettingRepositoryImpl;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.adapters.LocalDateTimeTypeAdapter;
import inc.nomard.spoty.utils.adapters.LocalDateTypeAdapter;
import inc.nomard.spoty.utils.adapters.LocalTimeTypeAdapter;
import inc.nomard.spoty.utils.adapters.UnixEpochDateTypeAdapter;
import inc.nomard.spoty.utils.connectivity.Connectivity;
import inc.nomard.spoty.utils.functional_paradigm.SpotyGotFunctional;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Currency;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class TenantSettingViewModel {
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
    private static final StringProperty websiteLink = new SimpleStringProperty("");
    private static final StringProperty phoneNumber = new SimpleStringProperty("");
    private static final StringProperty email = new SimpleStringProperty("");
    private static final StringProperty supportEmail = new SimpleStringProperty("");
    private static final StringProperty infoEmail = new SimpleStringProperty("");
    private static final StringProperty hrEmail = new SimpleStringProperty("");
    private static final StringProperty salesEmail = new SimpleStringProperty("");
    private static final StringProperty postalAddress = new SimpleStringProperty("");
    private static final StringProperty physicalAddress = new SimpleStringProperty("");
    private static final StringProperty tagLine = new SimpleStringProperty("");
    private static final BooleanProperty reportLogo = new SimpleBooleanProperty(false);
    private static final BooleanProperty emailLogo = new SimpleBooleanProperty(false);
    private static final BooleanProperty receiptLogo = new SimpleBooleanProperty(false);
    private static final BooleanProperty reviews = new SimpleBooleanProperty(false);
    private static final BooleanProperty reviewAdjustments = new SimpleBooleanProperty(false);
    private static final BooleanProperty reviewRequisitions = new SimpleBooleanProperty(false);
    private static final BooleanProperty reviewTransfers = new SimpleBooleanProperty(false);
    private static final BooleanProperty reviewStockIns = new SimpleBooleanProperty(false);
    private static final BooleanProperty reviewQuotations = new SimpleBooleanProperty(false);
    private static final BooleanProperty reviewPurchases = new SimpleBooleanProperty(false);
    private static final BooleanProperty reviewSales = new SimpleBooleanProperty(false);
    private static final BooleanProperty reviewSaleReturns = new SimpleBooleanProperty(false);
    private static final BooleanProperty reviewPurchaseReturns = new SimpleBooleanProperty(false);
    private static final StringProperty twitter = new SimpleStringProperty("");
    private static final StringProperty facebook = new SimpleStringProperty("");
    private static final StringProperty linkedIn = new SimpleStringProperty("");
    private static final IntegerProperty reviewLevels = new SimpleIntegerProperty();
    private static final ObjectProperty<Currency> defaultCurrency = new SimpleObjectProperty<>(null);
    private static final StringProperty logo = new SimpleStringProperty("");
    private static final ObjectProperty<Employee> employee = new SimpleObjectProperty<>();
    private static final IntegerProperty reviewLevel = new SimpleIntegerProperty();
    public static ObservableList<Reviewer> reviewersList = FXCollections.observableArrayList();
    private static final ListProperty<Reviewer> reviewers = new SimpleListProperty<>(reviewersList);
    public static TenantSettingRepositoryImpl tenantSettingRepository = new TenantSettingRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        TenantSettingViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        TenantSettingViewModel.nameProperty().set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static String getWebsiteLink() {
        return websiteLink.get();
    }

    public static void setWebsiteLink(String websiteLink) {
        TenantSettingViewModel.websiteLink.set(websiteLink);
    }

    public static StringProperty websiteLinkProperty() {
        return websiteLink;
    }

    public static String getPhoneNumber() {
        return phoneNumber.get();
    }

    public static void setPhoneNumber(String phoneNumber) {
        TenantSettingViewModel.phoneNumber.set(phoneNumber);
    }

    public static StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public static String getEmail() {
        return email.get();
    }

    public static void setEmail(String email) {
        TenantSettingViewModel.email.set(email);
    }

    public static StringProperty emailProperty() {
        return email;
    }

    public static String getSupportEmail() {
        return supportEmail.get();
    }

    public static void setSupportEmail(String supportEmail) {
        TenantSettingViewModel.supportEmail.set(supportEmail);
    }

    public static StringProperty supportEmailProperty() {
        return supportEmail;
    }

    public static String getInfoEmail() {
        return infoEmail.get();
    }

    public static void setInfoEmail(String infoEmail) {
        TenantSettingViewModel.infoEmail.set(infoEmail);
    }

    public static StringProperty infoEmailProperty() {
        return infoEmail;
    }

    public static String getHrEmail() {
        return hrEmail.get();
    }

    public static void setHrEmail(String hrEmail) {
        TenantSettingViewModel.hrEmail.set(hrEmail);
    }

    public static StringProperty hrEmailProperty() {
        return hrEmail;
    }

    public static String getSalesEmail() {
        return salesEmail.get();
    }

    public static void setSalesEmail(String salesEmail) {
        TenantSettingViewModel.salesEmail.set(salesEmail);
    }

    public static StringProperty salesEmailProperty() {
        return salesEmail;
    }

    public static String getPostalAddress() {
        return postalAddress.get();
    }

    public static void setPostalAddress(String postalAddress) {
        TenantSettingViewModel.postalAddress.set(postalAddress);
    }

    public static StringProperty postalAddressProperty() {
        return postalAddress;
    }

    public static String getPhysicalAddress() {
        return physicalAddress.get();
    }

    public static void setPhysicalAddress(String physicalAddress) {
        TenantSettingViewModel.physicalAddress.set(physicalAddress);
    }

    public static StringProperty physicalAddressProperty() {
        return physicalAddress;
    }

    public static String getTagLine() {
        return tagLine.get();
    }

    public static void setTagLine(String tagLine) {
        TenantSettingViewModel.tagLine.set(tagLine);
    }

    public static StringProperty tagLineProperty() {
        return tagLine;
    }

    public static String getLogo() {
        return logo.get();
    }

    public static void setLogo(String logo) {
        TenantSettingViewModel.logo.set(logo);
    }

    public static StringProperty logoProperty() {
        return logo;
    }

    public static Integer getReviewLevels() {
        return reviewLevels.get();
    }

    public static void setReviewLevels(Integer reviewLevels) {
        TenantSettingViewModel.reviewLevels.set(reviewLevels);
    }

    public static IntegerProperty reviewLevelsProperty() {
        return reviewLevels;
    }

    public static boolean isReportLogo() {
        return reportLogo.get();
    }

    public static void setReportLogo(boolean reportLogo) {
        TenantSettingViewModel.reportLogo.set(reportLogo);
    }

    public static BooleanProperty reportLogoProperty() {
        return reportLogo;
    }

    public static boolean isEmailLogo() {
        return emailLogo.get();
    }

    public static void setEmailLogo(boolean emailLogo) {
        TenantSettingViewModel.emailLogo.set(emailLogo);
    }

    public static BooleanProperty emailLogoProperty() {
        return emailLogo;
    }

    public static boolean isReceiptLogo() {
        return receiptLogo.get();
    }

    public static void setReceiptLogo(boolean receiptLogo) {
        TenantSettingViewModel.receiptLogo.set(receiptLogo);
    }

    public static BooleanProperty receiptLogoProperty() {
        return receiptLogo;
    }

    public static String getTwitter() {
        return twitter.get();
    }

    public static void setTwitter(String twitter) {
        TenantSettingViewModel.twitter.set(twitter);
    }

    public static StringProperty twitterProperty() {
        return twitter;
    }

    public static String getFacebook() {
        return facebook.get();
    }

    public static void setFacebook(String facebook) {
        TenantSettingViewModel.facebook.set(facebook);
    }

    public static StringProperty facebookProperty() {
        return facebook;
    }

    public static String getLinkedIn() {
        return linkedIn.get();
    }

    public static void setLinkedIn(String linkedIn) {
        TenantSettingViewModel.linkedIn.set(linkedIn);
    }

    public static StringProperty linkedInProperty() {
        return linkedIn;
    }

    public static Currency getDefaultCurrency() {
        return defaultCurrency.get();
    }

    public static void setDefaultCurrency(Currency defaultCurrency) {
        TenantSettingViewModel.defaultCurrency.set(defaultCurrency);
    }

    public static ObjectProperty<Currency> defaultCurrencyProperty() {
        return defaultCurrency;
    }

    public static ObservableList<Reviewer> getReviewers() {
        return reviewers.get();
    }

    public static void setReviewers(ObservableList<Reviewer> reviewers) {
        TenantSettingViewModel.reviewers.set(reviewers);
    }

    public static ListProperty<Reviewer> reviewersProperty() {
        return reviewers;
    }


    public static boolean isReviews() {
        return reviews.get();
    }

    public static void setReviews(boolean reviews) {
        TenantSettingViewModel.reviews.set(reviews);
    }

    public static BooleanProperty reviewsProperty() {
        return reviews;
    }


    public static boolean isReviewAdjustments() {
        return reviewAdjustments.get();
    }

    public static void setReviewAdjustments(boolean reviewAdjustments) {
        TenantSettingViewModel.reviewAdjustments.set(reviewAdjustments);
    }

    public static BooleanProperty reviewAdjustmentsProperty() {
        return reviewAdjustments;
    }

    public static boolean isReviewRequisitions() {
        return reviewRequisitions.get();
    }

    public static void setReviewRequisitions(boolean reviewRequisitions) {
        TenantSettingViewModel.reviewRequisitions.set(reviewRequisitions);
    }

    public static BooleanProperty reviewRequisitionsProperty() {
        return reviewRequisitions;
    }

    public static boolean isReviewTransfers() {
        return reviewTransfers.get();
    }

    public static void setReviewTransfers(boolean reviewTransfers) {
        TenantSettingViewModel.reviewTransfers.set(reviewTransfers);
    }

    public static BooleanProperty reviewTransfersProperty() {
        return reviewTransfers;
    }

    public static boolean isReviewStockIns() {
        return reviewStockIns.get();
    }

    public static void setReviewStockIns(boolean reviewStockIns) {
        TenantSettingViewModel.reviewStockIns.set(reviewStockIns);
    }

    public static BooleanProperty reviewStockInsProperty() {
        return reviewStockIns;
    }

    public static boolean isReviewQuotations() {
        return reviewQuotations.get();
    }

    public static void setReviewQuotations(boolean reviewQuotations) {
        TenantSettingViewModel.reviewQuotations.set(reviewQuotations);
    }

    public static BooleanProperty reviewQuotationsProperty() {
        return reviewQuotations;
    }

    public static boolean isReviewPurchases() {
        return reviewPurchases.get();
    }

    public static void setReviewPurchases(boolean reviewPurchases) {
        TenantSettingViewModel.reviewPurchases.set(reviewPurchases);
    }

    public static BooleanProperty reviewPurchasesProperty() {
        return reviewPurchases;
    }

    public static boolean isReviewSales() {
        return reviewSales.get();
    }

    public static void setReviewSales(boolean reviewSales) {
        TenantSettingViewModel.reviewSales.set(reviewSales);
    }

    public static BooleanProperty reviewSalesProperty() {
        return reviewSales;
    }

    public static boolean isReviewSaleReturns() {
        return reviewSaleReturns.get();
    }

    public static void setReviewSaleReturns(boolean reviewSaleReturns) {
        TenantSettingViewModel.reviewSaleReturns.set(reviewSaleReturns);
    }

    public static BooleanProperty reviewSaleReturnsProperty() {
        return reviewSaleReturns;
    }

    public static boolean isReviewPurchaseReturns() {
        return reviewPurchaseReturns.get();
    }

    public static void setReviewPurchaseReturns(boolean reviewPurchaseReturns) {
        TenantSettingViewModel.reviewPurchaseReturns.set(reviewPurchaseReturns);
    }

    public static BooleanProperty reviewPurchaseReturnsProperty() {
        return reviewPurchaseReturns;
    }

    public static Employee getEmployee() {
        return employee.get();
    }

    public static void setEmployee(Employee employee) {
        TenantSettingViewModel.employee.set(employee);
    }

    public static ObjectProperty<Employee> employeeProperty() {
        return employee;
    }

    public static Integer getReviewLevel() {
        return reviewLevel.get();
    }

    public static void setReviewLevel(Integer reviewLevel) {
        TenantSettingViewModel.reviewLevel.set(reviewLevel);
    }

    public static IntegerProperty reviewLevelProperty() {
        return reviewLevel;
    }

    public static void clearReviewData() {
        setEmployee(null);
        setReviewLevel(0);
    }

    public static void addTenantSettings(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                         SpotyGotFunctional.MessageConsumer successMessage,
                                         SpotyGotFunctional.MessageConsumer errorMessage) {

        var tenantSettings =
                TenantSettings.builder()
                        .name(getName())
                        .websiteLink(getWebsiteLink())
                        .phoneNumber(getPhoneNumber())
                        .email(getEmail())
                        .supportEmail(getSupportEmail())
                        .infoEmail(getInfoEmail())
                        .hrEmail(getHrEmail())
                        .salesEmail(getSalesEmail())
                        .postalAddress(getPostalAddress())
                        .physicalAddress(getPhysicalAddress())
                        .tagLine(getTagLine())
                        .reportLogo(isReportLogo())
                        .emailLogo(isEmailLogo())
                        .receiptLogo(isReceiptLogo())
                        .twitter(getTwitter())
                        .facebook(getFacebook())
                        .linkedIn(getLinkedIn())
                        .logo(getLogo())
                        .reviewAdjustments(isReviewAdjustments())
                        .reviewRequisitions(isReviewRequisitions())
                        .reviewTransfers(isReviewTransfers())
                        .reviewStockIns(isReviewStockIns())
                        .reviewQuotations(isReviewQuotations())
                        .reviewPurchases(isReviewPurchases())
                        .reviewSaleReturns(isReviewSaleReturns())
                        .reviewPurchaseReturns(isReviewPurchaseReturns())
                        .reviewers(getReviewers())
                        .defaultCurrency(getDefaultCurrency())
                        .build();
        CompletableFuture<HttpResponse<String>> responseFuture = tenantSettingRepository.post(tenantSettings);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Tenant settings created successfully");
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
            SpotyLogger.writeToFile(throwable, TenantSettingViewModel.class);
            return null;
        });
    }

    public static void removeReviewer(Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                      SpotyGotFunctional.MessageConsumer successMessage,
                                      SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = tenantSettingRepository.removeReviewer(FindModel.builder().id(index).build());
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201 || response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Reviewer removed successfully");
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
            SpotyLogger.writeToFile(throwable, TenantSettingViewModel.class);
            return null;
        });
    }

    public static void addReviewer(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                   SpotyGotFunctional.MessageConsumer successMessage,
                                   SpotyGotFunctional.MessageConsumer errorMessage) {
        var employee = Reviewer.builder()
                .employee(getEmployee())
                .level(getReviewLevel())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = tenantSettingRepository.addReviewer(employee);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201 || response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Reviewer Added successfully");
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
            SpotyLogger.writeToFile(throwable, TenantSettingViewModel.class);
            return null;
        });
    }

    public static void editReviewer(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                   SpotyGotFunctional.MessageConsumer successMessage,
                                   SpotyGotFunctional.MessageConsumer errorMessage) {
        var employee = Reviewer.builder()
                .employee(getEmployee())
                .level(getReviewLevel())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = tenantSettingRepository.editReviewer(employee);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201 || response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Reviewer Added successfully");
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
            SpotyLogger.writeToFile(throwable, TenantSettingViewModel.class);
            return null;
        });
    }

    public static void getItem(Reviewer reviewer,
                               SpotyGotFunctional.ParameterlessConsumer onSuccess,
                               SpotyGotFunctional.MessageConsumer errorMessage) {
        var employee = Reviewer.builder()
                .employee(getEmployee())
                .level(getReviewLevel())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = tenantSettingRepository.addReviewer(employee);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201 || response.statusCode() == 200 || response.statusCode() == 204) {
                setEmployee(reviewer.getEmployee());
                setReviewLevel(reviewer.getLevel());
                Platform.runLater(onSuccess::run);
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
            SpotyLogger.writeToFile(throwable, TenantSettingViewModel.class);
            return null;
        });
    }

    public static void getTenantSettings(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                         SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = tenantSettingRepository.fetch();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    TenantSettings tenantSettings = gson.fromJson(response.body(), TenantSettings.class);
                    setId(tenantSettings.getId());
                    setName(tenantSettings.getName());
                    setWebsiteLink(tenantSettings.getWebsiteLink());
                    setPhoneNumber(tenantSettings.getPhoneNumber());
                    setEmail(tenantSettings.getEmail());
                    setSupportEmail(tenantSettings.getSupportEmail());
                    setInfoEmail(tenantSettings.getInfoEmail());
                    setHrEmail(tenantSettings.getHrEmail());
                    setSalesEmail(tenantSettings.getSalesEmail());
                    setPostalAddress(tenantSettings.getPostalAddress());
                    setPhysicalAddress(tenantSettings.getPhysicalAddress());
                    setTagLine(tenantSettings.getTagLine());
                    setReportLogo(tenantSettings.getReportLogo());
                    setEmailLogo(tenantSettings.getEmailLogo());
                    setReceiptLogo(tenantSettings.getReceiptLogo());
                    setTwitter(tenantSettings.getTwitter());
                    setFacebook(tenantSettings.getFacebook());
                    setLinkedIn(tenantSettings.getLinkedIn());
                    setLogo(tenantSettings.getLogo());
                    setReviews(tenantSettings.getReview());

                    setReviewAdjustments(tenantSettings.getReviewAdjustments());
                    setReviewRequisitions(tenantSettings.getReviewRequisitions());
                    setReviewTransfers(tenantSettings.getReviewTransfers());
                    setReviewStockIns(tenantSettings.getReviewStockIns());
                    setReviewQuotations(tenantSettings.getReviewQuotations());
                    setReviewPurchases(tenantSettings.getReviewPurchases());
                    setReviewSales(tenantSettings.getReviewSales());
                    setReviewSaleReturns(tenantSettings.getReviewSaleReturns());
                    setReviewPurchaseReturns(tenantSettings.getReviewPurchaseReturns());
                    setReviewLevels(tenantSettings.getApprovalLevels());
                    setReviewers(FXCollections.observableArrayList(tenantSettings.getReviewers()));
                    setDefaultCurrency(tenantSettings.getDefaultCurrency());
                    if (Objects.nonNull(onSuccess)) {
                        onSuccess.run();
                    }
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
            SpotyLogger.writeToFile(throwable, TenantSettingViewModel.class);
            return null;
        });
    }

    public static void updateTenantSettings(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                            SpotyGotFunctional.MessageConsumer successMessage,
                                            SpotyGotFunctional.MessageConsumer errorMessage) {
        var tenantSettings = TenantSettings.builder()
                .id(getId())
                .name(getName())
                .websiteLink(getWebsiteLink())
                .phoneNumber(getPhoneNumber())
                .email(getEmail())
                .postalAddress(getPostalAddress())
                .physicalAddress(getPhysicalAddress())
                .tagLine(getTagLine())
                .logo(getLogo())
                .reportLogo(isReportLogo())
                .emailLogo(isEmailLogo())
                .receiptLogo(isReceiptLogo())
                .twitter(getTwitter())
                .facebook(getFacebook())
                .linkedIn(getLinkedIn())
                .defaultCurrency(getDefaultCurrency())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = tenantSettingRepository.put(tenantSettings);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("TenantSettings details updated successfully");
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
            SpotyLogger.writeToFile(throwable, TenantSettingViewModel.class);
            return null;
        });
    }

    public static void deleteTenantSettings(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer successMessage,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = tenantSettingRepository.delete();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Branch deleted successfully");
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
            SpotyLogger.writeToFile(throwable, TenantSettingViewModel.class);
            return null;
        });
    }
}
