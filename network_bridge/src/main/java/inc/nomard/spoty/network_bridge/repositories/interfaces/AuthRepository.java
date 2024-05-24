package inc.nomard.spoty.network_bridge.repositories.interfaces;

import inc.nomard.spoty.network_bridge.models.*;
import javafx.concurrent.Task;

import java.io.IOException;
import java.net.http.HttpResponse;

public interface AuthRepository {

    Task<HttpResponse<String>> login(LoginModel loginModel) throws IOException, InterruptedException;

    Task<HttpResponse<String>> signup(SignupModel signUpModel) throws IOException, InterruptedException;
}
