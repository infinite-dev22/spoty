package org.infinite.spoty.data_source.repositories.implementations;

import com.google.gson.Gson;
import javafx.scene.image.Image;
import org.infinite.spoty.data_source.auth.ProtectedGlobals;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.interfaces.CompanyRepository;
import org.infinite.spoty.startup.SpotyPaths;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.values.EndPoints;

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
    public HttpResponse<String> fetchAll() throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Organisations.allOrganisations))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public HttpResponse<String> fetch(FindModel findModel) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Organisations.designationById))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModel)))
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
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
    public HttpResponse<String> search(SearchModel searchModel) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Organisations.searchOrganisations))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.ofString(new Gson().toJson(searchModel)))
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public HttpResponse<String> post(Object object) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Organisations.addOrganisation))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(new Gson().toJson(object)))
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public HttpResponse<String> put(Object object) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Organisations.updateOrganisation))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("PUT", HttpRequest.BodyPublishers.ofString(new Gson().toJson(object)))
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public HttpResponse<String> delete(FindModel findModel) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Organisations.deleteOrganisation))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModel)))
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public HttpResponse<String> deleteMultiple(ArrayList<FindModel> findModelList) throws IOException, InterruptedException {
//        var request = HttpRequest.newBuilder()
//                .uri(URI.create(EndPoints.Organisations.deleteOrganisations))
//                .header("Authorization", authToken)
//                .header("Accept", "application/json")
//                .header("Content-Type", "application/json")
//                .method("DELETE", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModelList)))
//                .build();
//
//        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return null;
    }
}
