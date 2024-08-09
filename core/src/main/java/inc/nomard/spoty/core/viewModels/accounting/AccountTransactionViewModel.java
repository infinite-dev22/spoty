package inc.nomard.spoty.core.viewModels.accounting;

import com.google.gson.*;
import com.google.gson.reflect.*;
import inc.nomard.spoty.network_bridge.dtos.accounting.*;
import inc.nomard.spoty.network_bridge.dtos.response.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.accounting.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;
import inc.nomard.spoty.utils.connectivity.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import java.lang.reflect.*;
import java.net.http.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.extern.java.*;

@Log
public class AccountTransactionViewModel {
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
    public static ObservableList<AccountTransaction> transactionsList = FXCollections.observableArrayList();
    private static final ListProperty<AccountTransaction> TRANSACTIONS = new SimpleListProperty<>(transactionsList);
    public static AccountRepositoryImplAccount transactionsRepository = new AccountRepositoryImplAccount();
    private static final IntegerProperty totalPages = new SimpleIntegerProperty(0);
    private static final IntegerProperty pageNumber = new SimpleIntegerProperty(0);
    private static final IntegerProperty pageSize = new SimpleIntegerProperty(50);

    public static ObservableList<AccountTransaction> getTransactions() {
        return TRANSACTIONS.get();
    }

    public static void setTransactions(ObservableList<AccountTransaction> transactions) {
        AccountTransactionViewModel.TRANSACTIONS.set(transactions);
    }

    public static ListProperty<AccountTransaction> transactionsProperty() {
        return TRANSACTIONS;
    }

    public static Integer getTotalPages() {
        return totalPages.get();
    }

    public static void setTotalPages(Integer totalPages) {
        AccountTransactionViewModel.totalPages.set(totalPages);
    }

    public static IntegerProperty totalPagesProperty() {
        return totalPages;
    }

    public static Integer getPageNumber() {
        return pageNumber.get();
    }

    public static void setPageNumber(Integer pageNumber) {
        AccountTransactionViewModel.pageNumber.set(pageNumber);
    }

    public static IntegerProperty pageNumberProperty() {
        return pageNumber;
    }

    public static Integer getPageSize() {
        return pageSize.get();
    }

    public static void setPageSize(Integer pageSize) {
        AccountTransactionViewModel.pageSize.set(pageSize);
    }

    public static IntegerProperty pageSizeProperty() {
        return pageSize;
    }

    public static void getAllTransactions(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                          SpotyGotFunctional.MessageConsumer errorMessage, Integer pageNo, Integer pageSize) {
        CompletableFuture<HttpResponse<String>> responseFuture = transactionsRepository.fetchAllTransactions(pageNo, pageSize);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type type = new TypeToken<ResponseModel<AccountTransaction>>() {
                    }.getType();
                    ResponseModel<AccountTransaction> responseModel = gson.fromJson(response.body(), type);
                    setTotalPages(responseModel.getTotalPages());
                    setPageNumber(responseModel.getPageable().getPageNumber());
                    setPageSize(responseModel.getPageable().getPageSize());
                    ArrayList<AccountTransaction> transactionList = responseModel.getContent();
                    transactionsList.clear();
                    transactionsList.addAll(transactionList);
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
            SpotyLogger.writeToFile(throwable, AccountTransactionViewModel.class);
            return null;
        });
    }
}
