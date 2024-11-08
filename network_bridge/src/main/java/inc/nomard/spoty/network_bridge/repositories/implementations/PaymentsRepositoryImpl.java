package inc.nomard.spoty.network_bridge.repositories.implementations;

import com.google.gson.Gson;
import inc.nomard.spoty.network_bridge.auth.ProtectedGlobals;
import inc.nomard.spoty.network_bridge.end_points.EndPoints;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.repositories.interfaces.PaymentsRepository;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import inc.nomard.spoty.utils.SpotyThreader;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class PaymentsRepositoryImpl extends ProtectedGlobals implements PaymentsRepository {
    // Trylla, Trilla, Trylli.
    // Freilla.
    // Friella.
    @Override
    public CompletableFuture<HttpResponse<String>> cardPay(Object object) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Payments.cardPay))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(new Gson().toJson(object)))
                .build();

        return SpotyThreader.httpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> mtnMomoPay(Object object) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Payments.mtnMomoPay))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(new Gson().toJson(object)))
                .build();

        return SpotyThreader.httpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> airtelMomoPay(Object object) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Payments.airtelMoPay))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(new Gson().toJson(object)))
                .build();

        return SpotyThreader.httpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> startTrial(FindModel findModel) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Payments.trial))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("PUT", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModel)))
                .build();

        return SpotyThreader.httpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
}
