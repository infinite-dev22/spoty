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

package inc.normad.spoty.core.viewModels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import inc.normad.spoty.core.viewModels.adapters.UnixEpochDateTypeAdapter;
import inc.normad.spoty.network_bridge.auth.ProtectedGlobals;
import inc.normad.spoty.network_bridge.models.APIResponseModel;
import inc.normad.spoty.network_bridge.models.LoginModel;
import inc.normad.spoty.network_bridge.repositories.implementations.AuthRepositoryImpl;
import inc.normad.spoty.utils.ParameterlessConsumer;
import inc.normad.spoty.utils.SpotyThreader;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;
import java.util.concurrent.ExecutionException;


public class LoginViewModel {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final StringProperty username = new SimpleStringProperty("");
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

    public static String getUsername() {
        return username.get();
    }

    public static void setUsername(String username) {
        LoginViewModel.username.set(username);
    }

    public static StringProperty usernameProperty() {
        return username;
    }

    public static void login(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {

        var loginDetails =
                LoginModel.builder()
                        .email(getUsername())
                        .password(getPassword())
                        .build();

        var task = authRepository.login(loginDetails);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> {
            try {
                var response = gson.fromJson(task.get().body(), APIResponseModel.class);
                if (response.getStatus() == 200) {
                    ProtectedGlobals.authToken = response.getToken();
                    onSuccess.run();
                } else {
                    onFailed.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }
}
