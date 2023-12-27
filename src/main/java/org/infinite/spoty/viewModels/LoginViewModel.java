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

package org.infinite.spoty.viewModels;

import com.google.gson.Gson;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.infinite.spoty.data_source.models.APIResponseModel;
import org.infinite.spoty.data_source.models.LoginModel;
import org.infinite.spoty.data_source.repositories.implementations.AuthRepositoryImpl;

import java.io.IOException;


public class LoginViewModel {
    private static final StringProperty username = new SimpleStringProperty("");
    private static final StringProperty password = new SimpleStringProperty("");

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

    public static APIResponseModel login() throws IOException, InterruptedException {
        var authRepository = new AuthRepositoryImpl();

        var loginDetails =
                LoginModel.builder()
                        .email(getUsername())
                        .password(getPassword())
                        .build();

        var response = authRepository.login(loginDetails);

        if (response.statusCode() == 200) {
            return new Gson().fromJson(response.body(), APIResponseModel.class);
        } else {
            return new Gson().fromJson(response.body(), APIResponseModel.class);
        }
    }
}
