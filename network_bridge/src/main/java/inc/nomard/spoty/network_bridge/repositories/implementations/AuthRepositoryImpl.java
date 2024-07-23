package inc.nomard.spoty.network_bridge.repositories.implementations;

import com.google.gson.*;
import inc.nomard.spoty.network_bridge.end_points.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.interfaces.*;
import java.net.*;
import java.net.http.*;
import java.util.concurrent.*;
import lombok.extern.java.*;

import javafx.util.Duration;
@Log
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
