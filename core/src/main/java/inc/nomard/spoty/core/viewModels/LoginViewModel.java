/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package inc.nomard.spoty.core.viewModels;

import com.google.gson.*;
import inc.nomard.spoty.core.viewModels.hrm.employee.*;
import inc.nomard.spoty.network_bridge.auth.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import java.net.http.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.beans.property.*;
import lombok.extern.java.*;

@Log
public class LoginViewModel {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
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
                             SpotyGotFunctional.MessageConsumer errorMessage,
                             SpotyGotFunctional.ParameterlessConsumer onBadCredentials) {
        var loginDetails =
                LoginModel.builder()
                        .email(getEmail())
                        .password(getPassword())
                        .build();
        CompletableFuture<HttpResponse<String>> responseFuture = authRepository.login(loginDetails);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            var loginResponse = gson.fromJson(response.body(), LoginResponseModel.class);
            if (loginResponse.getStatus() == 200) {
                successMessage.showMessage("Authentication successful");
                ProtectedGlobals.authToken = loginResponse.getToken();
                ProtectedGlobals.trial = loginResponse.getUser().getUserProfile().getTenant().isTrial();
                ProtectedGlobals.canTry = loginResponse.getUser().getUserProfile().getTenant().isCanTry();
                ProtectedGlobals.newTenancy = loginResponse.getUser().getUserProfile().getTenant().isNewTenancy();
                ProtectedGlobals.activeTenancy = loginResponse.isActiveTenancy();
                ProtectedGlobals.message = loginResponse.getMessage();
                ProtectedGlobals.user = loginResponse.getUser();
                onSuccess.run();
            } else if (loginResponse.getStatus() == 401) {
                errorMessage.showMessage("An error occurred, access denied");
                ProtectedGlobals.authToken = loginResponse.getToken();
                ProtectedGlobals.trial = loginResponse.getUser().getUserProfile().getTenant().isTrial();
                ProtectedGlobals.canTry = loginResponse.getUser().getUserProfile().getTenant().isCanTry();
                ProtectedGlobals.newTenancy = loginResponse.getUser().getUserProfile().getTenant().isNewTenancy();
                ProtectedGlobals.newTenancy = loginResponse.isNewTenancy();
                ProtectedGlobals.activeTenancy = loginResponse.isActiveTenancy();
                ProtectedGlobals.message = loginResponse.getMessage();
                ProtectedGlobals.user = loginResponse.getUser();
                onSuccess.run();
            } else if (loginResponse.getStatus() == 404) {
                errorMessage.showMessage("An error occurred, resource not found");
                ProtectedGlobals.authToken = loginResponse.getToken();
                ProtectedGlobals.trial = loginResponse.getUser().getUserProfile().getTenant().isTrial();
                ProtectedGlobals.canTry = loginResponse.getUser().getUserProfile().getTenant().isCanTry();
                ProtectedGlobals.newTenancy = loginResponse.getUser().getUserProfile().getTenant().isNewTenancy();
                ProtectedGlobals.activeTenancy = loginResponse.isActiveTenancy();
                ProtectedGlobals.message = loginResponse.getMessage();
                ProtectedGlobals.user = loginResponse.getUser();
                onSuccess.run();
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is definitely on our side");
            } else if (loginResponse.getMessage().toLowerCase().contains("bad credentials")) {
                ProtectedGlobals.message = loginResponse.getMessage();
                onBadCredentials.run();
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            errorMessage.showMessage("An error occurred, this is on your side");
            SpotyLogger.writeToFile(throwable, DepartmentViewModel.class);
            return null;
        });
    }
}
