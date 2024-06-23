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
import inc.nomard.spoty.core.wrappers.*;
import inc.nomard.spoty.network_bridge.dtos.dashboard.*;
import inc.nomard.spoty.network_bridge.dtos.sales.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;
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
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    new UnixEpochDateTypeAdapter())
            .create();
    private static final ObjectProperty<DashboardKPIModelWrapper> earnings = new SimpleObjectProperty<>(new DashboardKPIModelWrapper(new DashboardKPIModel("-", 0.00)));
    private static final ObjectProperty<DashboardKPIModelWrapper> purchases = new SimpleObjectProperty<>(new DashboardKPIModelWrapper(new DashboardKPIModel("-", 0.00)));
    private static final ObjectProperty<DashboardKPIModelWrapper> products = new SimpleObjectProperty<>(new DashboardKPIModelWrapper(new DashboardKPIModel("-", 0.00)));
    private static final ObjectProperty<DashboardKPIModelWrapper> customers = new SimpleObjectProperty<>(new DashboardKPIModelWrapper(new DashboardKPIModel("-", 0.00)));
    private static final ObjectProperty<DashboardKPIModelWrapper> suppliers = new SimpleObjectProperty<>(new DashboardKPIModelWrapper(new DashboardKPIModel("-", 0.00)));
    private static final ObservableList<LineChartModel> expensesList = FXCollections.observableArrayList();
    private static final ObservableList<LineChartModel> incomesList = FXCollections.observableArrayList();
    private static final ObservableList<LineChartModel> revenuesList = FXCollections.observableArrayList();
    private static final ObservableList<ProductSalesModel> topProductsList = FXCollections.observableArrayList();
    private static final ObservableList<SaleMaster> recentOrdersList = FXCollections.observableArrayList();
    private static final ObservableList<StockAlertModel> stockAlertsList = FXCollections.observableArrayList();
    private static final ListProperty<LineChartModel> expenses = new SimpleListProperty<>(expensesList);
    private static final ListProperty<LineChartModel> incomes = new SimpleListProperty<>(incomesList);
    private static final ListProperty<LineChartModel> revenues = new SimpleListProperty<>(revenuesList);
    private static final ListProperty<ProductSalesModel> topProducts = new SimpleListProperty<>(topProductsList);
    private static final ListProperty<SaleMaster> recentOrders = new SimpleListProperty<>(recentOrdersList);
    private static final ListProperty<StockAlertModel> stockAlerts = new SimpleListProperty<>(stockAlertsList);
    private static final DashboardRepositoryImpl dashboardRepository = new DashboardRepositoryImpl();
    private static final ObservableList<DashboardKPIModelWrapper> kpisList = FXCollections.observableArrayList();

    static {
        kpisList.add(earnings.get());
        kpisList.add(purchases.get());
        kpisList.add(products.get());
        kpisList.add(customers.get());
        kpisList.add(suppliers.get());
    }

    public static ObservableList<DashboardKPIModelWrapper> getKPIs() {
        return kpisList;
    }

    public static void setEarnings(DashboardKPIModel model) {
        earnings.set(new DashboardKPIModelWrapper(model));
        kpisList.set(0, earnings.get());
    }

    public static void setPurchases(DashboardKPIModel model) {
        purchases.set(new DashboardKPIModelWrapper(model));
        kpisList.set(1, purchases.get());
    }

    public static void setProducts(DashboardKPIModel model) {
        products.set(new DashboardKPIModelWrapper(model));
        kpisList.set(2, products.get());
    }

    public static void setCustomers(DashboardKPIModel model) {
        customers.set(new DashboardKPIModelWrapper(model));
        kpisList.set(3, customers.get());
    }

    public static void setSuppliers(DashboardKPIModel model) {
        suppliers.set(new DashboardKPIModelWrapper(model));
        kpisList.set(4, suppliers.get());
    }

    public static ObjectProperty<DashboardKPIModelWrapper> earningsProperty() {
        return earnings;
    }

    public static ObjectProperty<DashboardKPIModelWrapper> purchasesProperty() {
        return purchases;
    }

    public static ObjectProperty<DashboardKPIModelWrapper> productsProperty() {
        return products;
    }

    public static ObjectProperty<DashboardKPIModelWrapper> customersProperty() {
        return customers;
    }

    public static ObjectProperty<DashboardKPIModelWrapper> suppliersProperty() {
        return suppliers;
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

    public static ObservableList<LineChartModel> getRevenues() {
        return revenues.get();
    }

    public static void setRevenues(ObservableList<LineChartModel> revenues) {
        DashboardViewModel.revenues.set(revenues);
    }

    public static ListProperty<LineChartModel> revenuesProperty() {
        return revenues;
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

    public static void getTotalEarnings(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                        SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = dashboardRepository.kpiEarnings();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    var kpi = new Gson().fromJson(response.body(), DashboardKPIModel.class);
                    setEarnings(kpi);
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

    public static void getTotalPurchases(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                         SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = dashboardRepository.kpiPurchases();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    var kpi = new Gson().fromJson(response.body(), DashboardKPIModel.class);
                    setPurchases(kpi);
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

    public static void getCountProducts(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                        SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = dashboardRepository.kpiProducts();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    var kpi = new Gson().fromJson(response.body(), DashboardKPIModel.class);
                    setProducts(kpi);
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

    public static void getCountCustomers(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                         SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = dashboardRepository.kpiCustomers();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    var kpi = new Gson().fromJson(response.body(), DashboardKPIModel.class);
                    setCustomers(kpi);
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

    public static void getCountSuppliers(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                         SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = dashboardRepository.kpiSuppliers();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    var kpi = new Gson().fromJson(response.body(), DashboardKPIModel.class);
                    setSuppliers(kpi);
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

    public static void getMonthlyRevenue(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                         SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = dashboardRepository.monthlyRevenue();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<LineChartModel>>() {
                    }.getType();
                    ArrayList<LineChartModel> revenueList = new Gson().fromJson(response.body(), listType);
                    revenuesList.clear();
                    revenuesList.addAll(revenueList);
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

    public static void getWeeklyRevenue(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                        SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = dashboardRepository.weeklyRevenue();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    Type listType = new TypeToken<ArrayList<LineChartModel>>() {
                    }.getType();
                    ArrayList<LineChartModel> revenueList = new Gson().fromJson(response.body(), listType);
                    revenuesList.clear();
                    revenuesList.addAll(revenueList);
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
                    ArrayList<SaleMaster> recentOrderList = gson.fromJson(response.body(), listType);
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
