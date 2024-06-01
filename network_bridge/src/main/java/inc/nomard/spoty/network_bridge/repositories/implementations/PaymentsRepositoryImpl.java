package inc.nomard.spoty.network_bridge.repositories.implementations;

import com.google.gson.*;
import inc.nomard.spoty.network_bridge.auth.*;
import inc.nomard.spoty.network_bridge.end_points.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.interfaces.*;
import java.net.*;
import java.net.http.*;
import java.util.concurrent.*;
import lombok.extern.java.*;

@Log
public class PaymentsRepositoryImpl extends ProtectedGlobals implements PaymentsRepository {
    // Trylla, Trilla, Trylli.
    // Freilla.
    @Override
    public CompletableFuture<HttpResponse<String>> cardPay(Object object) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Payments.cardPay))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(new Gson().toJson(object)))
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
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

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
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

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> startTrial(FindModel findModel) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Payments.trial))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModel)))
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
}
