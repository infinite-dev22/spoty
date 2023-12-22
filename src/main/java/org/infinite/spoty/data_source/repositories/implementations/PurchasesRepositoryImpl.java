package org.infinite.spoty.data_source.repositories.implementations;

import com.google.gson.Gson;
import org.infinite.spoty.data_source.auth.ProtectedGlobals;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.interfaces.MasterDetailRepository;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.values.EndPoints;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class PurchasesRepositoryImpl extends ProtectedGlobals implements MasterDetailRepository {
    @Override
    public HttpResponse<String> fetchAllMaster() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Purchases.allPurchaseMasters))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, PurchasesRepositoryImpl.class);
        }

        return response;
    }

    @Override
    public HttpResponse<String> fetchMaster(FindModel findModel) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Purchases.purchaseMasterById))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModel)))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, PurchasesRepositoryImpl.class);
        }

        return response;
    }

    @Override
    public HttpResponse<String> searchMaster(SearchModel searchModel) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Purchases.searchPurchaseMasters))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.ofString(new Gson().toJson(searchModel)))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, PurchasesRepositoryImpl.class);
        }

        return response;
    }

    @Override
    public HttpResponse<String> postMaster(Object object) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Purchases.addPurchaseMaster))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(new Gson().toJson(object)))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, PurchasesRepositoryImpl.class);
        }

        return response;
    }

    @Override
    public HttpResponse<String> putMaster(Object object) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Purchases.updatePurchaseMaster))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("PUT", HttpRequest.BodyPublishers.ofString(new Gson().toJson(object)))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, PurchasesRepositoryImpl.class);
        }

        return response;
    }

    @Override
    public HttpResponse<String> deleteMaster(FindModel findModel) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Purchases.deletePurchaseMaster))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModel)))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, PurchasesRepositoryImpl.class);
        }

        return response;
    }

    @Override
    public HttpResponse<String> deleteMultipleMasters(ArrayList<FindModel> findModelList) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Purchases.deletePurchaseMasters))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModelList)))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, PurchasesRepositoryImpl.class);
        }

        return response;
    }

    @Override
    public HttpResponse<String> fetchAllDetail() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Purchases.allPurchaseDetails))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, PurchasesRepositoryImpl.class);
        }

        return response;
    }

    @Override
    public HttpResponse<String> fetchDetail(FindModel findModel) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Purchases.purchaseDetailById))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModel)))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, PurchasesRepositoryImpl.class);
        }

        return response;
    }

    @Override
    public HttpResponse<String> searchDetail(SearchModel searchModel) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Purchases.searchPurchaseDetails))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.ofString(new Gson().toJson(searchModel)))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, PurchasesRepositoryImpl.class);
        }

        return response;
    }

    @Override
    public HttpResponse<String> postDetail(Object object) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Purchases.addPurchaseDetail))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(new Gson().toJson(object)))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, PurchasesRepositoryImpl.class);
        }

        return response;
    }

    @Override
    public HttpResponse<String> putDetail(Object object) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Purchases.updatePurchaseDetail))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("PUT", HttpRequest.BodyPublishers.ofString(new Gson().toJson(object)))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, PurchasesRepositoryImpl.class);
        }

        return response;
    }

    @Override
    public HttpResponse<String> deleteDetail(FindModel findModel) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Purchases.deletePurchaseDetail))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModel)))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, PurchasesRepositoryImpl.class);
        }

        return response;
    }

    @Override
    public HttpResponse<String> deleteMultipleDetails(ArrayList<FindModel> findModelList) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Purchases.deletePurchaseDetails))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModelList)))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, PurchasesRepositoryImpl.class);
        }

        return response;
    }
}
