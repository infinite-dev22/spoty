package inc.nomard.spoty.network_bridge.repositories.interfaces;

import inc.nomard.spoty.network_bridge.models.LoginModel;
import inc.nomard.spoty.network_bridge.models.SignupModel;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public interface AuthRepository {

    CompletableFuture<HttpResponse<String>> login(LoginModel loginModel) throws IOException, InterruptedException;

    CompletableFuture<HttpResponse<String>> signup(SignupModel signUpModel) throws IOException, InterruptedException;
}
