package inc.nomard.spoty.network_bridge.repositories.interfaces;

import inc.nomard.spoty.network_bridge.models.*;
import java.io.*;
import java.net.http.*;
import java.util.*;
import java.util.concurrent.*;

public interface DashboardRepository {
    CompletableFuture<HttpResponse<String>> kpis();
    CompletableFuture<HttpResponse<String>> yearlyExpenses();
    CompletableFuture<HttpResponse<String>> monthlyExpenses();
    CompletableFuture<HttpResponse<String>> weeklyExpenses();
    CompletableFuture<HttpResponse<String>> yearlyIncomes();
    CompletableFuture<HttpResponse<String>> monthlyIncomes();
    CompletableFuture<HttpResponse<String>> weeklyIncomes();
    CompletableFuture<HttpResponse<String>> topProducts();
    CompletableFuture<HttpResponse<String>> recentOrders();
    CompletableFuture<HttpResponse<String>> stockAlerts();
}
