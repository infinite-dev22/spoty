package inc.nomard.spoty.network_bridge.repositories.implementations;

import com.google.gson.Gson;
import inc.nomard.spoty.network_bridge.auth.ProtectedGlobals;
import inc.nomard.spoty.network_bridge.end_points.EndPoints;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;
import inc.nomard.spoty.network_bridge.repositories.interfaces.SimpleRepository;
import javafx.concurrent.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import lombok.*;
import lombok.extern.slf4j.*;

@Slf4j
public class SaleTermsAndConditionsRepositoryImpl extends ProtectedGlobals implements SimpleRepository {
    @Override
    public Task<HttpResponse<String>> fetchAll() {
        return new Task<>() {
            @Override
            @SneakyThrows
            protected HttpResponse<String> call() {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.SaleTermsAndConditions.allSaleTermsAndConditions))
                        .header("Authorization", authToken)
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .method("GET", HttpRequest.BodyPublishers.noBody())
                        .build();

                return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            }
        };
    }

    @Override
    public Task<HttpResponse<String>> fetch(FindModel findModel) {
        return new Task<>() {
            @Override
            @SneakyThrows
            protected HttpResponse<String> call() {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.SaleTermsAndConditions.saleTermAndConditionById))
                        .header("Authorization", authToken)
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .method("GET", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModel)))
                        .build();

                return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            }
        };
    }

    @Override
    public Task<HttpResponse<String>> search(SearchModel searchModel) {
        return new Task<>() {
            @Override
            @SneakyThrows
            protected HttpResponse<String> call() {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.SaleTermsAndConditions.searchSaleTermsAndConditions))
                        .header("Authorization", authToken)
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .method("GET", HttpRequest.BodyPublishers.ofString(new Gson().toJson(searchModel)))
                        .build();

                return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            }
        };
    }

    @Override
    public Task<HttpResponse<String>> post(Object object) {
        return new Task<>() {
            @Override
            @SneakyThrows
            protected HttpResponse<String> call() {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.SaleTermsAndConditions.addSaleTermAndCondition))
                        .header("Authorization", authToken)
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .method("POST", HttpRequest.BodyPublishers.ofString(new Gson().toJson(object)))
                        .build();

                return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            }
        };
    }

    @Override
    public Task<HttpResponse<String>> put(Object object) {
        return new Task<>() {
            @Override
            @SneakyThrows
            protected HttpResponse<String> call() {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.SaleTermsAndConditions.updateSaleTermAndCondition))
                        .header("Authorization", authToken)
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .method("PUT", HttpRequest.BodyPublishers.ofString(new Gson().toJson(object)))
                        .build();

                return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            }
        };
    }

    @Override
    public Task<HttpResponse<String>> delete(FindModel findModel) {
        return new Task<>() {
            @Override
            @SneakyThrows
            protected HttpResponse<String> call() {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.SaleTermsAndConditions.deleteSaleTermAndCondition))
                        .header("Authorization", authToken)
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .method("DELETE", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModel)))
                        .build();

                return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            }
        };
    }

    @Override
    public Task<HttpResponse<String>> deleteMultiple(ArrayList<FindModel> findModelList) {
        return new Task<>() {
            @Override
            @SneakyThrows
            protected HttpResponse<String> call() {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.SaleTermsAndConditions.deleteSaleTermsAndConditions))
                        .header("Authorization", authToken)
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .method("DELETE", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModelList)))
                        .build();

                return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            }
        };
    }
}
