package inc.nomard.spoty.network_bridge.repositories.interfaces;

import java.net.http.*;
import java.util.concurrent.*;

public interface DashboardRepository {
    CompletableFuture<HttpResponse<String>> kpiEarnings();

    CompletableFuture<HttpResponse<String>> kpiPurchases();

    CompletableFuture<HttpResponse<String>> kpiProducts();

    CompletableFuture<HttpResponse<String>> kpiCustomers();

    CompletableFuture<HttpResponse<String>> kpiSuppliers();

    CompletableFuture<HttpResponse<String>> yearlyExpenses();

    CompletableFuture<HttpResponse<String>> monthlyExpenses();

    CompletableFuture<HttpResponse<String>> weeklyExpenses();

    CompletableFuture<HttpResponse<String>> yearlyIncomes();

    CompletableFuture<HttpResponse<String>> monthlyIncomes();

    CompletableFuture<HttpResponse<String>> monthlyRevenue();
    CompletableFuture<HttpResponse<String>> weeklyRevenue();

    CompletableFuture<HttpResponse<String>> topProducts();

    CompletableFuture<HttpResponse<String>> recentOrders();

    CompletableFuture<HttpResponse<String>> stockAlerts();
}
