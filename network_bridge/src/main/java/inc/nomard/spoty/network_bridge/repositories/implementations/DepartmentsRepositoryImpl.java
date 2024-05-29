package inc.nomard.spoty.network_bridge.repositories.implementations;

import com.google.gson.*;
import inc.nomard.spoty.network_bridge.auth.*;
import inc.nomard.spoty.network_bridge.end_points.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.interfaces.*;
import java.io.*;
import java.net.*;
import java.net.http.*;
import java.util.*;
import javafx.concurrent.*;

import lombok.*;
import lombok.extern.slf4j.*;

@Slf4j
public class DepartmentsRepositoryImpl extends ProtectedGlobals implements SimpleRepository {
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
                        .uri(URI.create(EndPoints.Departments.allDepartments))
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
                        .uri(URI.create(EndPoints.Departments.departmentById))
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
                        .uri(URI.create(EndPoints.Departments.searchDepartments))
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
                        .uri(URI.create(EndPoints.Departments.addDepartment))
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
                        .uri(URI.create(EndPoints.Departments.updateDepartment))
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
                        .uri(URI.create(EndPoints.Departments.deleteDepartment))
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
                        .uri(URI.create(EndPoints.Departments.deleteDepartments))
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
