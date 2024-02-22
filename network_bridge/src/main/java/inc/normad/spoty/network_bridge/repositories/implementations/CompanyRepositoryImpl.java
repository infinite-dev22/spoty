package inc.normad.spoty.network_bridge.repositories.implementations;

import com.google.gson.Gson;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import inc.normad.spoty.network_bridge.auth.ProtectedGlobals;
import inc.normad.spoty.network_bridge.models.FindModel;
import inc.normad.spoty.network_bridge.models.SearchModel;
import inc.normad.spoty.network_bridge.repositories.interfaces.CompanyRepository;
import inc.normad.spoty.utils.SpotyLogger;
import inc.normad.spoty.network_bridge.end_points.EndPoints;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CompanyRepositoryImpl extends ProtectedGlobals implements CompanyRepository {
    @Override
    public Task<HttpResponse<String>> fetchAll() {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.Organisations.allOrganisations))
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
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.Organisations.designationById))
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
    public Image getCompanyLogo(String imageLink) throws IOException {
        Image image = null;

        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            if (Files.exists(Paths.get(System.getProperty("user.home")
                    + "\\AppData\\Local\\ZenmartERP\\system\\caches\\images\\company-logo.png"))) {

                image = new Image(String.valueOf(Paths.get(System.getProperty("user.home")
                        + "\\AppData\\Local\\ZenmartERP\\system\\caches\\images\\company-logo.png").toFile().toURI().toURL()));
            } else {
                try (InputStream in = URI.create(imageLink).toURL().openStream()) {
                    Files.copy(in, Paths.get(System.getProperty("user.home")
                            + "\\AppData\\Local\\ZenmartERP\\system\\caches\\images\\company-logo.png"));
                    image = new Image(String.valueOf(Paths.get(System.getProperty("user.home")
                            + "\\AppData\\Local\\ZenmartERP\\system\\caches\\images\\company-logo.png").toFile().toURI().toURL()));
                } catch (IOException e) {
                    SpotyLogger.writeToFile(e, CompanyRepositoryImpl.class);
                }
            }
        } else {
            if (Files.exists(Paths.get(
                    System.getProperty("user.home") + "/.config/ZenmartERP/system/caches/images/company-logo.png"))) {

                image = new Image(String.valueOf(Paths.get(
                        System.getProperty("user.home") + "/.config/ZenmartERP/system/caches/images/company-logo.png").toFile().toURI().toURL()));
            } else {
                try (InputStream in = URI.create(imageLink).toURL().openStream()) {
                    Files.copy(in, Paths.get(
                            System.getProperty("user.home") + "/.config/ZenmartERP/system/caches/images/company-logo.png"));
                    image = new Image(String.valueOf(Paths.get(
                            System.getProperty("user.home") + "/.config/ZenmartERP/system/caches/images/company-logo.png").toFile().toURI().toURL()));
                } catch (IOException e) {
                    SpotyLogger.writeToFile(e, CompanyRepositoryImpl.class);
                }
            }
        }
        return image;
    }

    @Override
    public Task<HttpResponse<String>> search(SearchModel searchModel) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.Organisations.searchOrganisations))
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
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.Organisations.addOrganisation))
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
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.Organisations.updateOrganisation))
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
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.Organisations.deleteOrganisation))
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
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.Organisations.deleteOrganisations))
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
