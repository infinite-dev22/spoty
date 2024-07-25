package inc.nomard.spoty.core.viewModels;

import com.google.gson.*;
import inc.nomard.spoty.core.viewModels.hrm.employee.*;
import inc.nomard.spoty.network_bridge.auth.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;
import inc.nomard.spoty.utils.connectivity.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import java.net.http.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.application.*;
import javafx.beans.property.*;
import lombok.extern.java.*;

@Log
public class LoginViewModel {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class,
                    new LocalDateTypeAdapter())
            .registerTypeAdapter(LocalTime.class,
                    new LocalTimeTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class,
                    new LocalDateTimeTypeAdapter())
            .create();
    private static final StringProperty email = new SimpleStringProperty("");
    private static final StringProperty password = new SimpleStringProperty("");
    private static final AuthRepositoryImpl authRepository = new AuthRepositoryImpl();

    public static String getPassword() {
        return password.get();
    }

    public static void setPassword(String password) {
        LoginViewModel.password.set(password);
    }

    public static StringProperty passwordProperty() {
        return password;
    }

    public static String getEmail() {
        return email.get();
    }

    public static void setEmail(String email) {
        LoginViewModel.email.set(email);
    }

    public static StringProperty emailProperty() {
        return email;
    }

    public static void resetProperties() {
        setEmail("");
        setPassword("");
    }

    public static void login(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                             SpotyGotFunctional.MessageConsumer successMessage,
                             SpotyGotFunctional.MessageConsumer errorMessage) {
        var loginDetails =
                LoginModel.builder()
                        .email(getEmail())
                        .password(getPassword())
                        .build();
        CompletableFuture<HttpResponse<String>> responseFuture = authRepository.login(loginDetails);
        responseFuture.thenAccept(response -> {
            var loginResponse = gson.fromJson(response.body(), LoginResponseModel.class);
            if (loginResponse.getStatus() == 200) {
                Platform.runLater(() -> {
                    ProtectedGlobals.authToken = loginResponse.getToken();
                    ProtectedGlobals.trial = loginResponse.getUser().getUserProfile().getTenant().isTrial();
                    ProtectedGlobals.canTry = loginResponse.getUser().getUserProfile().getTenant().isCanTry();
                    ProtectedGlobals.newTenancy = loginResponse.getUser().getUserProfile().getTenant().isNewTenancy();
                    ProtectedGlobals.activeTenancy = loginResponse.isActiveTenancy();
                    ProtectedGlobals.message = loginResponse.getMessage();
                    ProtectedGlobals.user = loginResponse.getUser();
                    successMessage.showMessage("Authentication successful");
                    onSuccess.run();
                });
            } else if (loginResponse.getStatus() == 401) {
                Platform.runLater(() -> errorMessage.showMessage("Access denied"));
            } else if (loginResponse.getStatus() == 404) {
                Platform.runLater(() -> errorMessage.showMessage("Resource not found"));
            } else if (response.statusCode() == 500 && loginResponse.getMessage().toLowerCase().contains("bad credentials")) {
                Platform.runLater(() -> errorMessage.showMessage("Wrong email or password"));
            } else if (response.statusCode() == 500) {
                Platform.runLater(() -> errorMessage.showMessage("An error occurred"));
            }
        }).exceptionally(throwable -> {
            if (Connectivity.isConnectedToInternet()) {
                Platform.runLater(() -> errorMessage.showMessage("An in app error occurred"));
            } else {
                Platform.runLater(() -> errorMessage.showMessage("No Internet Connection"));
            }
            SpotyLogger.writeToFile(throwable, DepartmentViewModel.class);
            return null;
        });
    }
}
