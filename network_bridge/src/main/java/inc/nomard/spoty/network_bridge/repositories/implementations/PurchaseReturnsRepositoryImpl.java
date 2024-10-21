package inc.nomard.spoty.network_bridge.repositories.implementations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import inc.nomard.spoty.network_bridge.auth.ProtectedGlobals;
import inc.nomard.spoty.network_bridge.end_points.EndPoints;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;
import inc.nomard.spoty.network_bridge.repositories.interfaces.SimpleRepository;
import inc.nomard.spoty.utils.adapters.LocalDateTimeTypeAdapter;
import inc.nomard.spoty.utils.adapters.LocalDateTypeAdapter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import inc.nomard.spoty.utils.SpotyThreader;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class PurchaseReturnsRepositoryImpl extends ProtectedGlobals implements SimpleRepository {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class,
                    new LocalDateTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class,
                    new LocalDateTimeTypeAdapter())
            .create();

    @Override
    public CompletableFuture<HttpResponse<String>> fetchAll(Integer pageNo, Integer pageSize) {
        if (pageNo == null) {
            pageNo = 0;
        }
        if (pageSize == null) {
            pageSize = 50;
        }

        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.PurchaseReturns.allPurchaseReturns + "?pageNo=" + pageNo + "&pageSize=" + pageSize))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return SpotyThreader.httpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> fetch(FindModel findModel) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.PurchaseReturns.purchaseReturnById))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModel)))
                .build();

        return SpotyThreader.httpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> search(SearchModel searchModel) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.PurchaseReturns.searchPurchaseReturns))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.ofString(new Gson().toJson(searchModel)))
                .build();

        return SpotyThreader.httpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> post(Object object) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.PurchaseReturns.addPurchaseReturn))
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
                .uri(URI.create(EndPoints.PurchaseReturns.updatePurchaseReturn))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("PUT", HttpRequest.BodyPublishers.ofString(gson.toJson(object)))
                .build();

        return SpotyThreader.httpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> delete(FindModel findModel) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.PurchaseReturns.deletePurchaseReturn))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModel)))
                .build();

        return SpotyThreader.httpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> deleteMultiple(ArrayList<FindModel> findModelList) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.PurchaseReturns.deletePurchaseReturns))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModelList)))
                .build();

        return SpotyThreader.httpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
}
