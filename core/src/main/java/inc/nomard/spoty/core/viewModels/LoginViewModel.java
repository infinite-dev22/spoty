package inc.nomard.spoty.core.viewModels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import inc.nomard.spoty.core.viewModels.hrm.employee.DepartmentViewModel;
import inc.nomard.spoty.network_bridge.auth.ProtectedGlobals;
import inc.nomard.spoty.network_bridge.models.LoginModel;
import inc.nomard.spoty.network_bridge.models.LoginResponseModel;
import inc.nomard.spoty.network_bridge.repositories.implementations.AuthRepositoryImpl;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.adapters.LocalDateTimeTypeAdapter;
import inc.nomard.spoty.utils.adapters.LocalDateTypeAdapter;
import inc.nomard.spoty.utils.adapters.LocalTimeTypeAdapter;
import inc.nomard.spoty.utils.connectivity.Connectivity;
import inc.nomard.spoty.utils.functional_paradigm.SpotyGotFunctional;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;

@Slf4j
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
            if (response.statusCode() == 200) {
                Platform.runLater(() -> {
                    ProtectedGlobals.authToken = loginResponse.getToken();
                    ProtectedGlobals.user = loginResponse.getUser();
                    ProtectedGlobals.role = loginResponse.getRole();
                    successMessage.showMessage("Authentication successful");
                    onSuccess.run();
                });
            } else if (response.statusCode() == 401) {
                Platform.runLater(() -> errorMessage.showMessage("Access denied"));
            } else if (response.statusCode() == 404) {
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
