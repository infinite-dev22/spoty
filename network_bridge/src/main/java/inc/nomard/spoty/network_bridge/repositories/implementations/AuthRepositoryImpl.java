package inc.nomard.spoty.network_bridge.repositories.implementations;

import com.google.gson.Gson;
import inc.nomard.spoty.network_bridge.end_points.EndPoints;
import inc.nomard.spoty.network_bridge.models.LoginModel;
import inc.nomard.spoty.network_bridge.models.SignupModel;
import inc.nomard.spoty.network_bridge.repositories.interfaces.AuthRepository;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class AuthRepositoryImpl implements AuthRepository {
    @Override
    public CompletableFuture<HttpResponse<String>> login(LoginModel loginModel) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Auth.login))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(new Gson().toJson(loginModel)))
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> signup(SignupModel signUpModel) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Auth.signup))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(new Gson().toJson(signUpModel)))
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
}
