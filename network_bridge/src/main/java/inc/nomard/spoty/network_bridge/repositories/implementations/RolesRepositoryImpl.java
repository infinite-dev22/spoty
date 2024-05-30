package inc.nomard.spoty.network_bridge.repositories.implementations;

import com.google.gson.Gson;
import inc.nomard.spoty.network_bridge.auth.ProtectedGlobals;
import inc.nomard.spoty.network_bridge.end_points.EndPoints;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.repositories.interfaces.RoleRepository;
import javafx.concurrent.Task;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Log
public class RolesRepositoryImpl extends ProtectedGlobals implements RoleRepository {
    @Override
    public Task<HttpResponse<String>> fetchAllRoles() {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.Roles.allRoles))
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
    public Task<HttpResponse<String>> fetchRole(FindModel findModel) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.Roles.roleById))
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
    public Task<HttpResponse<String>> postRole(Object object) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.Roles.addRole))
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
    public Task<HttpResponse<String>> putRole(Object object) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.Roles.updateRole))
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
    public Task<HttpResponse<String>> deleteRole(FindModel findModel) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.Roles.deleteRole))
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
    public Task<HttpResponse<String>> fetchAllPermissions() {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.Roles.allPermissions))
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
    public Task<HttpResponse<String>> fetchPermission(FindModel findModel) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.Roles.permissionById))
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
    public Task<HttpResponse<String>> postPermission(Object object) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.Roles.addPermission))
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
    public Task<HttpResponse<String>> putPermission(Object object) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.Roles.updatePermission))
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
    public Task<HttpResponse<String>> deletePermission(FindModel findModel) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.Roles.deletePermission))
                        .header("Authorization", authToken)
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .method("DELETE", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModel)))
                        .build();

                return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            }
        };
    }
}
