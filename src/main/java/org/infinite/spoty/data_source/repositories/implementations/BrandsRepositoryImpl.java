package org.infinite.spoty.data_source.repositories.implementations;

import com.google.gson.Gson;
import org.infinite.spoty.data_source.auth.ProtectedGlobals;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.interfaces.SimpleRepository;
import org.infinite.spoty.values.EndPoints;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class BrandsRepositoryImpl extends ProtectedGlobals implements SimpleRepository {
    @Override
    public HttpResponse<String> fetchAll() throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Brands.allBrands))
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
                .uri(URI.create(EndPoints.Brands.brandById))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModel)))
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public HttpResponse<String> search(SearchModel searchModel) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Brands.searchBrands))
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
                .uri(URI.create(EndPoints.Brands.addBrand))
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
                .uri(URI.create(EndPoints.Brands.updateBrand))
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
                .uri(URI.create(EndPoints.Brands.deleteBrand))
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
//                .uri(URI.create(EndPoints.Brands.deleteBrands))
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
