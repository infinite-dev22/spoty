package org.infinite.spoty.data_source.repositories.implementations;

import com.google.gson.Gson;
import javafx.concurrent.Task;
import org.infinite.spoty.data_source.models.LoginModel;
import org.infinite.spoty.data_source.models.SignUpModel;
import org.infinite.spoty.data_source.repositories.interfaces.AuthRepository;
import org.infinite.spoty.values.EndPoints;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthRepositoryImpl implements AuthRepository {
    @Override
    public Task<HttpResponse<String>> login(LoginModel loginModel) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.Auth.login))
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .method("POST", HttpRequest.BodyPublishers.ofString(new Gson().toJson(loginModel)))
                        .build();

                return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            }
        };
    }

    @Override
    public Task<HttpResponse<String>> signup(SignUpModel signUpModel) {
        return new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return taskCreate();
            }

            private HttpResponse<String> taskCreate() throws IOException, InterruptedException {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(EndPoints.Auth.signup))
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .method("POST", HttpRequest.BodyPublishers.ofString(new Gson().toJson(signUpModel)))
                        .build();

                return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            }
        };
    }
}
