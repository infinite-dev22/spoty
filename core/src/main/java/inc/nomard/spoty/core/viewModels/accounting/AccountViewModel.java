package inc.nomard.spoty.core.viewModels.accounting;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import inc.nomard.spoty.core.viewModels.returns.purchases.PurchaseReturnMasterViewModel;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.network_bridge.dtos.accounting.Account;
import inc.nomard.spoty.network_bridge.dtos.response.ResponseModel;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;
import inc.nomard.spoty.network_bridge.repositories.implementations.accounting.AccountRepositoryImplAccount;
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
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Log4j2
public class AccountViewModel {
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
    private static final StringProperty accountName = new SimpleStringProperty("");
    private static final StringProperty accountNumber = new SimpleStringProperty("");
    private static final DoubleProperty credit = new SimpleDoubleProperty(0);
    private static final DoubleProperty debit = new SimpleDoubleProperty(0);
    private static final DoubleProperty balance = new SimpleDoubleProperty(0);
    private static final StringProperty description = new SimpleStringProperty("");
    private static final ObjectProperty<Account> account = new SimpleObjectProperty<>();
    private static final IntegerProperty totalPages = new SimpleIntegerProperty(0);
    private static final IntegerProperty pageNumber = new SimpleIntegerProperty(0);
    private static final IntegerProperty pageSize = new SimpleIntegerProperty(50);
    public static ObservableList<Account> accountsList = FXCollections.observableArrayList();
    private static final ListProperty<Account> ACCOUNTS = new SimpleListProperty<>(accountsList);
    public static AccountRepositoryImplAccount accountsRepository = new AccountRepositoryImplAccount();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        AccountViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static String getAccountName() {
        return accountName.get();
    }

    public static void setAccountName(String accountName) {
        AccountViewModel.accountName.set(accountName);
    }

    public static StringProperty accountNameProperty() {
        return accountName;
    }

    public static String getAccountNumber() {
        return accountNumber.get();
    }

    public static void setAccountNumber(String accountNumber) {
        AccountViewModel.accountNumber.set(accountNumber);
    }

    public static StringProperty accountNumberProperty() {
        return accountNumber;
    }

    public static Double getCredit() {
        return credit.get();
    }

    public static void setCredit(Double credit) {
        AccountViewModel.credit.set(credit);
    }

    public static DoubleProperty creditProperty() {
        return credit;
    }

    public static Double getDebit() {
        return debit.get();
    }

    public static void setDebit(Double debit) {
        AccountViewModel.debit.set(debit);
    }

    public static DoubleProperty debitProperty() {
        return debit;
    }

    public static Double getBalance() {
        return (balance.get() == 0d) ? 0d : balance.get();
    }

    public static void setBalance(Double balance) {
        AccountViewModel.balance.set(balance);
    }

    public static DoubleProperty balanceProperty() {
        return balance;
    }

    public static String getDescription() {
        return description.get();
    }

    public static void setDescription(String description) {
        AccountViewModel.description.set(description);
    }

    public static StringProperty descriptionProperty() {
        return description;
    }

    public static ObservableList<Account> getAccounts() {
        return ACCOUNTS.get();
    }

    public static void setAccounts(ObservableList<Account> accounts) {
        AccountViewModel.ACCOUNTS.set(accounts);
    }

    public static ListProperty<Account> accountsProperty() {
        return ACCOUNTS;
    }

    public static Integer getTotalPages() {
        return totalPages.get();
    }

    public static void setTotalPages(Integer totalPages) {
        AccountViewModel.totalPages.set(totalPages);
    }

    public static IntegerProperty totalPagesProperty() {
        return totalPages;
    }

    public static Integer getPageNumber() {
        return pageNumber.get();
    }

    public static void setPageNumber(Integer pageNumber) {
        AccountViewModel.pageNumber.set(pageNumber);
    }

    public static IntegerProperty pageNumberProperty() {
        return pageNumber;
    }

    public static Integer getPageSize() {
        return pageSize.get();
    }

    public static void setPageSize(Integer pageSize) {
        AccountViewModel.pageSize.set(pageSize);
    }

    public static IntegerProperty pageSizeProperty() {
        return pageSize;
    }

    public static void saveAccount(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                   SpotyGotFunctional.MessageConsumer successMessage,
                                   SpotyGotFunctional.MessageConsumer errorMessage) {
        var account =
                Account.builder()
                        .accountName(getAccountName())
                        .accountNumber(getAccountNumber())
                        .credit(getCredit())
                        .debit(getDebit())
                        .balance(getBalance())
                        .description(getDescription())
                        .build();
        CompletableFuture<HttpResponse<String>> responseFuture = accountsRepository.post(account);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Account created successfully");
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
        }).exceptionally(throwable -> handleException(throwable, errorMessage));
    }

    public static void clearAccountData() {
        setId(0L);
        setDescription("");
        setAccountNumber("");
        setAccountName("");
        setBalance(0d);
        setCredit(0d);
        setDebit(0d);
    }

    public static void getAllAccounts(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                      SpotyGotFunctional.MessageConsumer errorMessage, Integer pageNo, Integer pageSize) {
        CompletableFuture<HttpResponse<String>> responseFuture = accountsRepository.fetchAll(pageNo, pageSize);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type type = new TypeToken<ResponseModel<Account>>() {
                    }.getType();
                    ResponseModel<Account> responseModel = gson.fromJson(response.body(), type);
                    setTotalPages(responseModel.getTotalPages());
                    setPageNumber(responseModel.getPageable().getPageNumber());
                    setPageSize(responseModel.getPageable().getPageSize());
                    ArrayList<Account> accountList = responseModel.getContent();
                    accountsList.clear();
                    accountsList.addAll(accountList);
                    if (Objects.nonNull(onSuccess)) {
                        onSuccess.run();
                    }
                });
            } else {
                handleNon200Status(response, errorMessage);
            }
        }).exceptionally(throwable -> handleException(throwable, errorMessage));
    }

    public static void getItem(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = accountsRepository.fetch(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    var account = gson.fromJson(response.body(), Account.class);
                    setId(account.getId());
                    setAccountName(account.getAccountName());
                    setAccountNumber(account.getAccountNumber());
                    setDescription(account.getDescription());
                    setCredit(account.getCredit());
                    setDebit(account.getDebit());
                    setBalance(account.getBalance());
                    onSuccess.run();
                });
            } else {
                handleNon200Status(response, errorMessage);
            }
        }).exceptionally(throwable -> handleException(throwable, errorMessage));
    }

    public static void searchItem(
            String search, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var searchModel = SearchModel.builder().search(search).build();
        CompletableFuture<HttpResponse<String>> responseFuture = accountsRepository.search(searchModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<Account>>() {
                    }.getType();
                    ArrayList<Account> accountList = gson.fromJson(
                            response.body(), listType);
                    accountsList.clear();
                    accountsList.addAll(accountList);
                    onSuccess.run();
                });
            } else {
                handleNon200Status(response, errorMessage);
            }
        }).exceptionally(throwable -> handleException(throwable, errorMessage));
    }

    public static void updateItem(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                  SpotyGotFunctional.MessageConsumer successMessage,
                                  SpotyGotFunctional.MessageConsumer errorMessage) {
        var account = Account.builder()
                .id(getId())
                .accountName(getAccountName())
                .accountNumber(getAccountNumber())
                .credit(getCredit())
                .debit(getDebit())
                .balance(getBalance())
                .description(getDescription())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = accountsRepository.put(account);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Account updated successfully");
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
        }).exceptionally(throwable -> handleException(throwable, errorMessage));
    }

    public static void deposit(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                               SpotyGotFunctional.MessageConsumer successMessage,
                               SpotyGotFunctional.MessageConsumer errorMessage) {
        var account = Account.builder()
                .id(getId())
                .balance(getBalance())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = accountsRepository.deposit(account);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Account deposit successful");
                });
            } else {
                handleNon200Status(response, errorMessage);
            }
        }).exceptionally(throwable -> handleException(throwable, errorMessage));
    }

    public static void deleteItem(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer successMessage,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = accountsRepository.delete(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Account deleted successfully");
                });
            } else {
                handleNon200Status(response, errorMessage);
            }
        }).exceptionally(throwable -> handleException(throwable, errorMessage));
    }

    private static void handleResponse(HttpResponse<String> response, SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                       SpotyGotFunctional.MessageConsumer successMessage,
                                       SpotyGotFunctional.MessageConsumer errorMessage) {
        log.info("STATUS: " + response.statusCode() + " BODY: " + response.body());
        Platform.runLater(() -> {
            switch (response.statusCode()) {
                case 200:
                case 201:
                case 204:
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                    if (successMessage != null) {
                        successMessage.showMessage("Operation successful");
                    }
                    break;
                default:
                    handleNon200Status(response, errorMessage);
                    break;
            }
        });
    }

    private static void handleNon200Status(HttpResponse<String> response, SpotyGotFunctional.MessageConsumer errorMessage) {
        log.info("STATUS: " + response.statusCode() + " BODY: " + response.body());
        Platform.runLater(() -> {
            String message = SpotyUtils.getHTTPStatus(response.statusCode(), null);
            if (errorMessage != null) {
                errorMessage.showMessage(message);
            }
        });
    }

    private static Void handleException(Throwable throwable, SpotyGotFunctional.MessageConsumer errorMessage) {
        log.error(throwable.getMessage());
        Platform.runLater(() -> {
            SpotyLogger.writeToFile(throwable, PurchaseReturnMasterViewModel.class);
            String message = Connectivity.isConnectedToInternet() ? "An error occurred" : "No Internet Connection";
            if (errorMessage != null) {
                errorMessage.showMessage(message);
            }
        });
        return null;
    }
}
