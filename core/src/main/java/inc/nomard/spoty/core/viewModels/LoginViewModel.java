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
import inc.nomard.spoty.network_bridge.auth.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;

import java.util.*;
import java.util.concurrent.*;

import javafx.beans.property.*;
import lombok.extern.java.Log;

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

    public static void login(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed,
            ParameterlessConsumer onBadCredentials) {
        var loginDetails =
                LoginModel.builder()
                        .email(getEmail())
                        .password(getPassword())
                        .build();
        var task = authRepository.login(loginDetails);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> {
            try {
                var response = gson.fromJson(task.get().body(), LoginResponseModel.class);
                if (response.getStatus() == 200) {
                    ProtectedGlobals.authToken = response.getToken();
                    ProtectedGlobals.trial = response.getUser().getUserProfile().getTenant().isTrial();
                    ProtectedGlobals.canTry = response.getUser().getUserProfile().getTenant().isCanTry();
                    ProtectedGlobals.newTenancy = response.getUser().getUserProfile().getTenant().isNewTenancy();
                    ProtectedGlobals.activeTenancy = response.isActiveTenancy();
                    ProtectedGlobals.message = response.getMessage();
                    ProtectedGlobals.user = response.getUser();
                    onSuccess.run();
                } else if (response.getStatus() == 401) {
                    ProtectedGlobals.authToken = response.getToken();
                    ProtectedGlobals.trial = response.getUser().getUserProfile().getTenant().isTrial();
                    ProtectedGlobals.canTry = response.getUser().getUserProfile().getTenant().isCanTry();
                    ProtectedGlobals.newTenancy = response.getUser().getUserProfile().getTenant().isNewTenancy();
                    ProtectedGlobals.newTenancy = response.isNewTenancy();
                    ProtectedGlobals.activeTenancy = response.isActiveTenancy();
                    ProtectedGlobals.message = response.getMessage();
                    ProtectedGlobals.user = response.getUser();
                    onSuccess.run();
                } else if (response.getStatus() == 404) {
                    ProtectedGlobals.authToken = response.getToken();
                    ProtectedGlobals.trial = response.getUser().getUserProfile().getTenant().isTrial();
                    ProtectedGlobals.canTry = response.getUser().getUserProfile().getTenant().isCanTry();
                    ProtectedGlobals.newTenancy = response.getUser().getUserProfile().getTenant().isNewTenancy();
                    ProtectedGlobals.activeTenancy = response.isActiveTenancy();
                    ProtectedGlobals.message = response.getMessage();
                    ProtectedGlobals.user = response.getUser();
                    onSuccess.run();
                } else if (response.getMessage().toLowerCase().contains("bad credentials")) {
                    ProtectedGlobals.message = response.getMessage();
                    onBadCredentials.run();
                } else {
                    {
                        ProtectedGlobals.message = response.getMessage();
                        onFailed.run();
                        System.err.println("The task failed with the following exception:");
                        task.getException().printStackTrace(System.err);
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        task.setOnFailed(workerStateEvent -> {
            LoginResponseModel response;
            try {
                response = gson.fromJson(task.get().body(), LoginResponseModel.class);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            ProtectedGlobals.message = response.getMessage();
            onFailed.run();
            System.err.println("The task failed with the following exception:");
            task.getException().printStackTrace(System.err);
        });
        SpotyThreader.spotyThreadPool(task);
    }
}
