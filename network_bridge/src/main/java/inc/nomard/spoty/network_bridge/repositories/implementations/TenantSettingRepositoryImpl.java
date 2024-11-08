package inc.nomard.spoty.network_bridge.repositories.implementations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import inc.nomard.spoty.network_bridge.auth.ProtectedGlobals;
import inc.nomard.spoty.network_bridge.end_points.EndPoints;
import inc.nomard.spoty.network_bridge.repositories.interfaces.TenantSettingRepository;
import inc.nomard.spoty.utils.SpotyThreader;
import inc.nomard.spoty.utils.adapters.LocalDateTimeTypeAdapter;
import inc.nomard.spoty.utils.adapters.LocalDateTypeAdapter;
import inc.nomard.spoty.utils.adapters.LocalTimeTypeAdapter;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;

public class TenantSettingRepositoryImpl extends ProtectedGlobals implements TenantSettingRepository {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class,
                    new LocalDateTypeAdapter())
            .registerTypeAdapter(LocalTime.class,
                    new LocalTimeTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class,
                    new LocalDateTimeTypeAdapter())
            .create();

    @Override
    public CompletableFuture<HttpResponse<String>> fetch() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.TenantSettings.getTenantSettings))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return SpotyThreader.httpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> post(Object object) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.TenantSettings.addTenantSettings))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(gson.toJson(object)))
                .build();

        return SpotyThreader.httpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> put(Object object) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.TenantSettings.updateTenantSettings))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("PUT", HttpRequest.BodyPublishers.ofString(gson.toJson(object)))
                .build();

        return SpotyThreader.httpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> delete() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.TenantSettings.deleteTenantSettings))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.noBody())
                .build();

        return SpotyThreader.httpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> addReviewer(Object object) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.TenantSettings.addReviewer))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(gson.toJson(object)))
                .build();

        return SpotyThreader.httpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> editReviewer(Object object) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.TenantSettings.editReviewer))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(gson.toJson(object)))
                .build();

        return SpotyThreader.httpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> removeReviewer(Object object) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.TenantSettings.removeReviewer))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(gson.toJson(object)))
                .build();

        return SpotyThreader.httpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
}
