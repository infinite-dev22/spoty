package inc.nomard.spoty.network_bridge.repositories.implementations;

import inc.nomard.spoty.network_bridge.auth.*;
import inc.nomard.spoty.network_bridge.end_points.*;
import inc.nomard.spoty.network_bridge.repositories.interfaces.*;
import java.net.*;
import java.net.http.*;
import java.util.concurrent.*;
import lombok.extern.java.*;

@Log
public class DashboardRepositoryImpl extends ProtectedGlobals implements DashboardRepository {
    @Override
    public CompletableFuture<HttpResponse<String>> kpiEarnings() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Dashboard.kpiEarnings))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
    @Override
    public CompletableFuture<HttpResponse<String>> kpiPurchases() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Dashboard.kpiPurchases))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
    @Override
    public CompletableFuture<HttpResponse<String>> kpiProducts() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Dashboard.kpiProducts))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
    @Override
    public CompletableFuture<HttpResponse<String>> kpiCustomers() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Dashboard.kpiCustomers))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
    @Override
    public CompletableFuture<HttpResponse<String>> kpiSuppliers() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Dashboard.kpiSuppliers))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> yearlyExpenses() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Dashboard.yearlyExpenses))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> monthlyExpenses() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Dashboard.monthlyExpenses))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> weeklyExpenses() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Dashboard.weeklyExpenses))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> yearlyIncomes() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Dashboard.yearlyIncomes))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> monthlyIncomes() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Dashboard.monthlyIncomes))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> monthlyRevenue() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Dashboard.monthlyRevenue))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> weeklyRevenue() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Dashboard.weeklyRevenue))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> topProducts() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Dashboard.topProducts))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> recentOrders() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Dashboard.recentOrders))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> stockAlerts() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Dashboard.stockAlerts))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
}
