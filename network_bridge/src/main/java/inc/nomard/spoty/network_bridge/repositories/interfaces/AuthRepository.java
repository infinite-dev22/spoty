package inc.nomard.spoty.network_bridge.repositories.interfaces;

import inc.nomard.spoty.network_bridge.models.*;
import java.io.*;
import java.net.http.*;
import java.util.concurrent.*;

public interface AuthRepository {

    CompletableFuture<HttpResponse<String>> login(LoginModel loginModel) throws IOException, InterruptedException;

    CompletableFuture<HttpResponse<String>> signup(SignupModel signUpModel) throws IOException, InterruptedException;
}
