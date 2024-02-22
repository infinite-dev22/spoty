package inc.normad.spoty.network_bridge.repositories.interfaces;

import inc.normad.spoty.network_bridge.models.LoginModel;
import inc.normad.spoty.network_bridge.models.SignUpModel;
import javafx.concurrent.Task;

import java.io.IOException;
import java.net.http.HttpResponse;

public interface AuthRepository {

    Task<HttpResponse<String>> login(LoginModel loginModel) throws IOException, InterruptedException;

    Task<HttpResponse<String>> signup(SignUpModel signUpModel) throws IOException, InterruptedException;
}
