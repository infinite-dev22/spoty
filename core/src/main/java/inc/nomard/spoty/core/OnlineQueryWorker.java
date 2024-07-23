package inc.nomard.spoty.core;

import inc.nomard.spoty.core.viewModels.dashboard.*;
import java.util.concurrent.*;
import javafx.concurrent.*;

public class OnlineQueryWorker {
    public static ScheduledService<Void> fetchDataTask() {
        return new ScheduledService<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
//                        CompletableFuture.runAsync(() -> DashboardViewModel.getTotalEarnings(null, null));
//                        CompletableFuture.runAsync(() -> DashboardViewModel.getTotalPurchases(null, null));
//                        CompletableFuture.runAsync(() -> DashboardViewModel.getCountProducts(null, null));
//                        CompletableFuture.runAsync(() -> DashboardViewModel.getCountCustomers(null, null));
//                        CompletableFuture.runAsync(() -> DashboardViewModel.getCountSuppliers(null, null));
//                        CompletableFuture.runAsync(() -> DashboardViewModel.getWeeklyRevenue(null, null));
//                        CompletableFuture.runAsync(() -> DashboardViewModel.getMonthlyRevenue(null, null));
//                        CompletableFuture.runAsync(() -> DashboardViewModel.getTopProducts(null, null));
//                        CompletableFuture.runAsync(() -> DashboardViewModel.getRecentOrders(null, null));
//                        CompletableFuture.runAsync(() -> DashboardViewModel.getStockAlerts(null, null));
                        return null;
                    }
                };
            }
        };
    }
}