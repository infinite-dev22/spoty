package inc.nomard.spoty.core.viewModels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import inc.nomard.spoty.network_bridge.dtos.Approver;
import inc.nomard.spoty.network_bridge.dtos.TenantSettings;
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
    private static final BooleanProperty approvals = new SimpleBooleanProperty(false);
    private static final BooleanProperty approveAdjustments = new SimpleBooleanProperty(false);
    private static final BooleanProperty approveRequisitions = new SimpleBooleanProperty(false);
    private static final BooleanProperty approveTransfers = new SimpleBooleanProperty(false);
    private static final BooleanProperty approveStockIns = new SimpleBooleanProperty(false);
    private static final BooleanProperty approveQuotations = new SimpleBooleanProperty(false);
    private static final BooleanProperty approvePurchases = new SimpleBooleanProperty(false);
    private static final BooleanProperty approveSales = new SimpleBooleanProperty(false);
    private static final BooleanProperty approveSaleReturns = new SimpleBooleanProperty(false);
    private static final BooleanProperty approvePurchaseReturns = new SimpleBooleanProperty(false);
    private static final StringProperty twitter = new SimpleStringProperty("");
    private static final StringProperty facebook = new SimpleStringProperty("");
    private static final StringProperty linkedIn = new SimpleStringProperty("");
    private static final IntegerProperty approvalLevels = new SimpleIntegerProperty();
    private static final ObjectProperty<Currency> defaultCurrency = new SimpleObjectProperty<>(null);
    private static final StringProperty logo = new SimpleStringProperty("");
    public static ObservableList<Approver> approversList = FXCollections.observableArrayList();
    private static final ListProperty<Approver> approvers = new SimpleListProperty<>(approversList);
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

    public static Integer getApprovalLevels() {
        return approvalLevels.get();
    }

    public static void setApprovalLevels(Integer approvalLevels) {
        TenantSettingViewModel.approvalLevels.set(approvalLevels);
    }

    public static IntegerProperty approvalLevelsProperty() {
        return approvalLevels;
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

    public static ObservableList<Approver> getApprovers() {
        return approvers.get();
    }

    public static void setApprovers(ObservableList<Approver> approvers) {
        TenantSettingViewModel.approvers.set(approvers);
    }

    public static ListProperty<Approver> approversProperty() {
        return approvers;
    }


    public static boolean isApprovals() {
        return approvals.get();
    }

    public static void setApprovals(boolean approvals) {
        TenantSettingViewModel.approvals.set(approvals);
    }

    public static BooleanProperty approvalsProperty() {
        return approvals;
    }


    public static boolean isApproveAdjustments() {
        return approveAdjustments.get();
    }

    public static void setApproveAdjustments(boolean approveAdjustments) {
        TenantSettingViewModel.approveAdjustments.set(approveAdjustments);
    }

    public static BooleanProperty approveAdjustmentsProperty() {
        return approveAdjustments;
    }

    public static boolean isApproveRequisitions() {
        return approveRequisitions.get();
    }

    public static void setApproveRequisitions(boolean approveRequisitions) {
        TenantSettingViewModel.approveRequisitions.set(approveRequisitions);
    }

    public static BooleanProperty approveRequisitionsProperty() {
        return approveRequisitions;
    }

    public static boolean isApproveTransfers() {
        return approveTransfers.get();
    }

    public static void setApproveTransfers(boolean approveTransfers) {
        TenantSettingViewModel.approveTransfers.set(approveTransfers);
    }

    public static BooleanProperty approveTransfersProperty() {
        return approveTransfers;
    }

    public static boolean isApproveStockIns() {
        return approveStockIns.get();
    }

    public static void setApproveStockIns(boolean approveStockIns) {
        TenantSettingViewModel.approveStockIns.set(approveStockIns);
    }

    public static BooleanProperty approveStockInsProperty() {
        return approveStockIns;
    }

    public static boolean isApproveQuotations() {
        return approveQuotations.get();
    }

    public static void setApproveQuotations(boolean approveQuotations) {
        TenantSettingViewModel.approveQuotations.set(approveQuotations);
    }

    public static BooleanProperty approveQuotationsProperty() {
        return approveQuotations;
    }

    public static boolean isApprovePurchases() {
        return approvePurchases.get();
    }

    public static void setApprovePurchases(boolean approvePurchases) {
        TenantSettingViewModel.approvePurchases.set(approvePurchases);
    }

    public static BooleanProperty approvePurchasesProperty() {
        return approvePurchases;
    }

    public static boolean isApproveSales() {
        return approveSales.get();
    }

    public static void setApproveSales(boolean approveSales) {
        TenantSettingViewModel.approveSales.set(approveSales);
    }

    public static BooleanProperty approveSalesProperty() {
        return approveSales;
    }

    public static boolean isApproveSaleReturns() {
        return approveSaleReturns.get();
    }

    public static void setApproveSaleReturns(boolean approveSaleReturns) {
        TenantSettingViewModel.approveSaleReturns.set(approveSaleReturns);
    }

    public static BooleanProperty approveSaleReturnsProperty() {
        return approveSaleReturns;
    }

    public static boolean isApprovePurchaseReturns() {
        return approvePurchaseReturns.get();
    }

    public static void setApprovePurchaseReturns(boolean approvePurchaseReturns) {
        TenantSettingViewModel.approvePurchaseReturns.set(approvePurchaseReturns);
    }

    public static BooleanProperty approvePurchaseReturnsProperty() {
        return approvePurchaseReturns;
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
                        .approveAdjustments(isApproveAdjustments())
                        .approveRequisitions(isApproveRequisitions())
                        .approveTransfers(isApproveTransfers())
                        .approveStockIns(isApproveStockIns())
                        .approveQuotations(isApproveQuotations())
                        .approvePurchases(isApprovePurchases())
                        .approveSaleReturns(isApproveSaleReturns())
                        .approvePurchaseReturns(isApprovePurchaseReturns())
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
                    setApprovals(tenantSettings.getApprovals());
                    setApproveAdjustments(tenantSettings.getApproveAdjustments());
                    setApproveRequisitions(tenantSettings.getApproveRequisitions());
                    setApproveTransfers(tenantSettings.getApproveTransfers());
                    setApproveStockIns(tenantSettings.getApproveStockIns());
                    setApproveQuotations(tenantSettings.getApproveQuotations());
                    setApprovePurchases(tenantSettings.getApprovePurchases());
                    setApproveSales(tenantSettings.getApproveSales());
                    setApproveSaleReturns(tenantSettings.getApproveSaleReturns());
                    setApprovePurchaseReturns(tenantSettings.getApprovePurchaseReturns());
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
