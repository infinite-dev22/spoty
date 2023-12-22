package org.infinite.spoty.data_source.repositories.implementations;

import com.google.gson.Gson;
import org.infinite.spoty.data_source.auth.ProtectedGlobals;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.interfaces.SimpleRepository;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.values.EndPoints;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class CustomersRepositoryImpl extends ProtectedGlobals implements SimpleRepository {
    @Override
    public HttpResponse<String> fetchAll() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Customers.allCustomers))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, CustomersRepositoryImpl.class);
        }

        return response;
    }

    @Override
    public HttpResponse<String> fetch(FindModel findModel) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Customers.customerById))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModel)))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, CustomersRepositoryImpl.class);
        }

        return response;
    }

    @Override
    public HttpResponse<String> search(SearchModel searchModel) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Customers.searchCustomers))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.ofString(new Gson().toJson(searchModel)))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, CustomersRepositoryImpl.class);
        }

        return response;
    }

    @Override
    public HttpResponse<String> post(Object object) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Customers.addCustomer))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(new Gson().toJson(object)))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, CustomersRepositoryImpl.class);
        }

        return response;
    }

    @Override
    public HttpResponse<String> put(Object object) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Customers.updateCustomer))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("PUT", HttpRequest.BodyPublishers.ofString(new Gson().toJson(object)))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, CustomersRepositoryImpl.class);
        }

        return response;
    }

    @Override
    public HttpResponse<String> delete(FindModel findModel) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Customers.deleteCustomer))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModel)))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, CustomersRepositoryImpl.class);
        }

        return response;
    }

    @Override
    public HttpResponse<String> deleteMultiple(ArrayList<FindModel> findModelList) {
//        var request = HttpRequest.newBuilder()
//                .uri(URI.create(EndPoints.Customers.deleteCustomers))
//                .header("Authorization", authToken)
//                .header("Accept", "application/json")
//                .header("Content-Type", "application/json")
//                .method("DELETE", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModelList)))
//                .build();
//
//        HttpResponse<String> response = null;
//        try {
//            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
//        } catch (IOException | InterruptedException e) {
//            SpotyLogger.writeToFile(e, BranchImpl.class);
//        }
//
//        return response;
        return null;
    }
}
