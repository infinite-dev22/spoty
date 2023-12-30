package org.infinite.spoty.data_source.repositories.implementations;

import com.google.gson.Gson;
import org.infinite.spoty.data_source.auth.ProtectedGlobals;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.interfaces.MasterDetailRepository;
import org.infinite.spoty.values.EndPoints;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class QuotationsRepositoryImpl extends ProtectedGlobals implements MasterDetailRepository {
    @Override
    public HttpResponse<String> fetchAllMaster() throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Quotations.allQuotationMasters))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public HttpResponse<String> fetchMaster(FindModel findModel) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Quotations.quotationMasterById))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModel)))
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public HttpResponse<String> searchMaster(SearchModel searchModel) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Quotations.searchQuotationMasters))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.ofString(new Gson().toJson(searchModel)))
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public HttpResponse<String> postMaster(Object object) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Quotations.addQuotationMaster))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(new Gson().toJson(object)))
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public HttpResponse<String> putMaster(Object object) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Quotations.updateQuotationMaster))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("PUT", HttpRequest.BodyPublishers.ofString(new Gson().toJson(object)))
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public HttpResponse<String> deleteMaster(FindModel findModel) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Quotations.deleteQuotationMaster))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModel)))
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public HttpResponse<String> deleteMultipleMasters(ArrayList<FindModel> findModelList) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Quotations.deleteQuotationMasters))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModelList)))
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public HttpResponse<String> fetchAllDetail() throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Quotations.allQuotationDetails))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public HttpResponse<String> fetchDetail(FindModel findModel) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Quotations.quotationDetailById))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModel)))
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public HttpResponse<String> searchDetail(SearchModel searchModel) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Quotations.searchQuotationDetails))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.ofString(new Gson().toJson(searchModel)))
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public HttpResponse<String> postDetail(Object object) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Quotations.addQuotationDetail))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(new Gson().toJson(object)))
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public HttpResponse<String> putDetail(Object object) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Quotations.updateQuotationDetail))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("PUT", HttpRequest.BodyPublishers.ofString(new Gson().toJson(object)))
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public HttpResponse<String> deleteDetail(FindModel findModel) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Quotations.deleteQuotationDetail))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModel)))
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public HttpResponse<String> deleteMultipleDetails(ArrayList<FindModel> findModelList) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Quotations.deleteQuotationDetails))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModelList)))
                .build();

        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }
}
