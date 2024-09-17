package inc.nomard.spoty.network_bridge.repositories.implementations;

import inc.nomard.spoty.network_bridge.auth.ProtectedGlobals;
import inc.nomard.spoty.network_bridge.end_points.EndPoints;
import lombok.extern.java.Log;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Log
public class SubscriptionRepositoryImpl extends ProtectedGlobals {
    public CompletableFuture<HttpResponse<String>> getStatus() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Subscription.status))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
}
