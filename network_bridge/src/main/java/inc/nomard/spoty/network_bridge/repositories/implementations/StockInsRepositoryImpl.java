package inc.nomard.spoty.network_bridge.repositories.implementations;

import com.google.gson.Gson;
import inc.nomard.spoty.network_bridge.auth.ProtectedGlobals;
import inc.nomard.spoty.network_bridge.end_points.EndPoints;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;
import inc.nomard.spoty.network_bridge.repositories.interfaces.MasterDetailRepository;
import javafx.concurrent.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedList;

public class StockInsRepositoryImpl extends ProtectedGlobals implements MasterDetailRepository {
    @Override
    public Task<HttpResponse<String>> fetchAllMaster() {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.StockIns.allStockInMasters))
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
    public Task<HttpResponse<String>> fetchMaster(FindModel findModel) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.StockIns.stockInMasterById))
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
    public Task<HttpResponse<String>> searchMaster(SearchModel searchModel) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.StockIns.searchStockInMasters))
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
    public Task<HttpResponse<String>> postMaster(Object object) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.StockIns.addStockInMaster))
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
    public Task<HttpResponse<String>> putMaster(Object object) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.StockIns.updateStockInMaster))
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
    public Task<HttpResponse<String>> deleteMaster(FindModel findModel) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.StockIns.deleteStockInMaster))
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
    public Task<HttpResponse<String>> deleteMultipleMasters(ArrayList<FindModel> findModelList) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.StockIns.deleteStockInMasters))
                        .header("Authorization", authToken)
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .method("DELETE", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModelList)))
                        .build();

                return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            }
        };
    }

    @Override
    public Task<HttpResponse<String>> fetchAllDetail() {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.StockIns.allStockInDetails))
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
    public Task<HttpResponse<String>> fetchDetail(FindModel findModel) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.StockIns.stockInDetailById))
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
    public Task<HttpResponse<String>> searchDetail(SearchModel searchModel) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.StockIns.searchStockInDetails))
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
    public Task<HttpResponse<String>> postDetail(Object object) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.StockIns.addStockInDetail))
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
    public Task<HttpResponse<String>> putDetail(Object object) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.StockIns.updateStockInDetail))
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
    public Task<HttpResponse<String>> deleteDetail(FindModel findModel) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.StockIns.deleteStockInDetail))
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
    public Task<HttpResponse<String>> deleteMultipleDetails(LinkedList<FindModel> findModelList) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.StockIns.deleteStockInDetails))
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