/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package inc.nomard.spoty.core.viewModels.dashboard;

import com.google.gson.*;
import com.google.gson.reflect.*;
import inc.nomard.spoty.network_bridge.dtos.dashboard.*;
import inc.nomard.spoty.network_bridge.dtos.sales.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.connectivity.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import java.lang.reflect.*;
import java.net.http.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.extern.java.*;

@Log
public class DashboardViewModel {
    private static final ObservableList<DashboardKPIModel> kpisList = FXCollections.observableArrayList();
    private static final ObservableList<LineChartModel> expensesList = FXCollections.observableArrayList();
    private static final ObservableList<LineChartModel> incomesList = FXCollections.observableArrayList();
    private static final ObservableList<ProductSalesModel> topProductsList = FXCollections.observableArrayList();
    private static final ObservableList<SaleMaster> recentOrdersList = FXCollections.observableArrayList();
    private static final ObservableList<StockAlertModel> stockAlertsList = FXCollections.observableArrayList();
    private static final ListProperty<DashboardKPIModel> kpis = new SimpleListProperty<>(kpisList);
    private static final ListProperty<LineChartModel> expenses = new SimpleListProperty<>(expensesList);
    private static final ListProperty<LineChartModel> incomes = new SimpleListProperty<>(incomesList);
    private static final ListProperty<ProductSalesModel> topProducts = new SimpleListProperty<>(topProductsList);
    private static final ListProperty<SaleMaster> recentOrders = new SimpleListProperty<>(recentOrdersList);
    private static final ListProperty<StockAlertModel> stockAlerts = new SimpleListProperty<>(stockAlertsList);
    private static final DashboardRepositoryImpl dashboardRepository = new DashboardRepositoryImpl();

    public static ObservableList<DashboardKPIModel> getKPIs() {
        return kpis.get();
    }

    public static void setKPIs(ObservableList<DashboardKPIModel> kpis) {
        DashboardViewModel.kpis.set(kpis);
    }

    public static ListProperty<DashboardKPIModel> kpisProperty() {
        return kpis;
    }

    public static ObservableList<LineChartModel> getExpenses() {
        return expenses.get();
    }

    public static void setExpenses(ObservableList<LineChartModel> expenses) {
        DashboardViewModel.expenses.set(expenses);
    }

    public static ListProperty<LineChartModel> expensesProperty() {
        return expenses;
    }

    public static ObservableList<LineChartModel> getIncomes() {
        return incomes.get();
    }

    public static void setIncomes(ObservableList<LineChartModel> incomes) {
        DashboardViewModel.incomes.set(incomes);
    }

    public static ListProperty<LineChartModel> incomesProperty() {
        return incomes;
    }

    public static ObservableList<ProductSalesModel> getTopProducts() {
        return topProducts.get();
    }

    public static void setTopProducts(ObservableList<ProductSalesModel> topProducts) {
        DashboardViewModel.topProducts.set(topProducts);
    }

    public static ListProperty<ProductSalesModel> topProductsProperty() {
        return topProducts;
    }

    public static ObservableList<SaleMaster> getRecentOrders() {
        return recentOrders.get();
    }

    public static void setRecentOrders(ObservableList<SaleMaster> recentOrders) {
        DashboardViewModel.recentOrders.set(recentOrders);
    }

    public static ListProperty<SaleMaster> recentOrdersProperty() {
        return recentOrders;
    }

    public static ObservableList<StockAlertModel> getStockAlerts() {
        return stockAlerts.get();
    }

    public static void setStockAlerts(ObservableList<StockAlertModel> stockAlerts) {
        DashboardViewModel.stockAlerts.set(stockAlerts);
    }

    public static ListProperty<StockAlertModel> stockAlertsProperty() {
        return stockAlerts;
    }

    public static void resetProperties() {
    }

    public static void getKPIs(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                               SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = dashboardRepository.kpis();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<DashboardKPIModel>>() {
                    }.getType();
                    ArrayList<DashboardKPIModel> kpiList = new Gson().fromJson(response.body(), listType);
                    kpisList.clear();
                    kpisList.addAll(kpiList);
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
            SpotyLogger.writeToFile(throwable, DashboardViewModel.class);
            return null;
        });
    }

    public static void getYearlyExpenses(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                         SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = dashboardRepository.yearlyExpenses();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<LineChartModel>>() {
                    }.getType();
                    ArrayList<LineChartModel> expenseList = new Gson().fromJson(response.body(), listType);
                    expensesList.clear();
                    expensesList.addAll(expenseList);
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
            SpotyLogger.writeToFile(throwable, DashboardViewModel.class);
            return null;
        });
    }

    public static void getMonthlyExpenses(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                          SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = dashboardRepository.monthlyExpenses();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<LineChartModel>>() {
                    }.getType();
                    ArrayList<LineChartModel> expenseList = new Gson().fromJson(response.body(), listType);
                    expensesList.clear();
                    expensesList.addAll(expenseList);
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
            SpotyLogger.writeToFile(throwable, DashboardViewModel.class);
            return null;
        });
    }

    public static void getWeeklyExpenses(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                         SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = dashboardRepository.weeklyExpenses();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<LineChartModel>>() {
                    }.getType();
                    ArrayList<LineChartModel> expenseList = new Gson().fromJson(response.body(), listType);
                    expensesList.clear();
                    expensesList.addAll(expenseList);
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
            SpotyLogger.writeToFile(throwable, DashboardViewModel.class);
            return null;
        });
    }

    public static void getYearlyIncomes(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                        SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = dashboardRepository.yearlyIncomes();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<LineChartModel>>() {
                    }.getType();
                    ArrayList<LineChartModel> incomeList = new Gson().fromJson(response.body(), listType);
                    incomesList.clear();
                    incomesList.addAll(incomeList);
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
            SpotyLogger.writeToFile(throwable, DashboardViewModel.class);
            return null;
        });
    }

    public static void getMonthlyIncomes(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                         SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = dashboardRepository.monthlyIncomes();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<LineChartModel>>() {
                    }.getType();
                    ArrayList<LineChartModel> incomeList = new Gson().fromJson(response.body(), listType);
                    incomesList.clear();
                    incomesList.addAll(incomeList);
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
            SpotyLogger.writeToFile(throwable, DashboardViewModel.class);
            return null;
        });
    }

    public static void getWeeklyIncomes(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                        SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = dashboardRepository.weeklyIncomes();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<LineChartModel>>() {
                    }.getType();
                    ArrayList<LineChartModel> incomeList = new Gson().fromJson(response.body(), listType);
                    incomesList.clear();
                    incomesList.addAll(incomeList);
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
            SpotyLogger.writeToFile(throwable, DashboardViewModel.class);
            return null;
        });
    }

    public static void getTopProducts(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                      SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = dashboardRepository.topProducts();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<ProductSalesModel>>() {
                    }.getType();
                    ArrayList<ProductSalesModel> topProductList = new Gson().fromJson(response.body(), listType);
                    topProductsList.clear();
                    topProductsList.addAll(topProductList);
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
            SpotyLogger.writeToFile(throwable, DashboardViewModel.class);
            return null;
        });
    }

    public static void getRecentOrders(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                       SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = dashboardRepository.recentOrders();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<SaleMaster>>() {
                    }.getType();
                    ArrayList<SaleMaster> recentOrderList = new Gson().fromJson(response.body(), listType);
                    recentOrdersList.clear();
                    recentOrdersList.addAll(recentOrderList);
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
            SpotyLogger.writeToFile(throwable, DashboardViewModel.class);
            return null;
        });
    }

    public static void getStockAlerts(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                      SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = dashboardRepository.stockAlerts();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<StockAlertModel>>() {
                    }.getType();
                    ArrayList<StockAlertModel> stockAlertList = new Gson().fromJson(response.body(), listType);
                    stockAlertsList.clear();
                    stockAlertsList.addAll(stockAlertList);
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
            SpotyLogger.writeToFile(throwable, DashboardViewModel.class);
            return null;
        });
    }
}
