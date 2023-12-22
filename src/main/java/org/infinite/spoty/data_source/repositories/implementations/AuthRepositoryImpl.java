package org.infinite.spoty.data_source.repositories.implementations;

import com.google.gson.Gson;
import org.infinite.spoty.data_source.models.LoginModel;
import org.infinite.spoty.data_source.models.SignUpModel;
import org.infinite.spoty.data_source.repositories.interfaces.AuthRepository;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.values.EndPoints;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthRepositoryImpl implements AuthRepository {
    @Override
    public HttpResponse<String> login(LoginModel loginModel) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Auth.login))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(new Gson().toJson(loginModel)))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, ServicesRepositoryImpl.class);
        }

        return response;
    }

    @Override
    public HttpResponse<String> signup(SignUpModel signUpModel) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Auth.signup))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(new Gson().toJson(signUpModel)))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, ServicesRepositoryImpl.class);
        }

        return response;
    }
}
