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
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;
import inc.nomard.spoty.utils.connectivity.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import java.net.http.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.application.*;
import javafx.beans.property.*;
import lombok.extern.java.*;

@Log
public class SignupViewModel {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    new UnixEpochDateTypeAdapter())
            .create();
    private static final StringProperty firstName = new SimpleStringProperty("");
    private static final StringProperty lastName = new SimpleStringProperty("");
    private static final StringProperty otherName = new SimpleStringProperty("");
    private static final StringProperty phone = new SimpleStringProperty("");
    private static final StringProperty email = new SimpleStringProperty("");
    private static final StringProperty password = new SimpleStringProperty("");
    private static final StringProperty confirmPassword = new SimpleStringProperty("");
    private static final AuthRepositoryImpl authRepository = new AuthRepositoryImpl();

    public static String getFirstName() {
        return firstName.get();
    }

    public static void setFirstName(String firstName) {
        SignupViewModel.firstName.set(firstName);
    }

    public static StringProperty firstNameProperty() {
        return firstName;
    }

    public static String getLastName() {
        return lastName.get();
    }

    public static void setLastName(String lastName) {
        SignupViewModel.lastName.set(lastName);
    }

    public static StringProperty lastNameProperty() {
        return lastName;
    }

    public static String getOtherName() {
        return otherName.get();
    }

    public static void setOtherName(String otherName) {
        SignupViewModel.otherName.set(otherName);
    }

    public static StringProperty otherNameProperty() {
        return otherName;
    }

    public static String getPhone() {
        return phone.get();
    }

    public static void setPhone(String phone) {
        SignupViewModel.phone.set(phone);
    }

    public static StringProperty phoneProperty() {
        return phone;
    }

    public static String getEmail() {
        return email.get();
    }

    public static void setEmail(String email) {
        SignupViewModel.email.set(email);
    }

    public static StringProperty emailProperty() {
        return email;
    }

    public static String getPassword() {
        return password.get();
    }

    public static void setPassword(String password) {
        SignupViewModel.password.set(password);
    }

    public static StringProperty passwordProperty() {
        return password;
    }

    public static String getConfirmPassword() {
        return confirmPassword.get();
    }

    public static void setConfirmPassword(String confirmPassword) {
        SignupViewModel.confirmPassword.set(confirmPassword);
    }

    public static StringProperty confirmPasswordProperty() {
        return confirmPassword;
    }

    public static void resetProperties() {
        setFirstName("");
        setLastName("");
        setOtherName("");
        setPhone("");
        setEmail("");
        setPassword("");
        setConfirmPassword("");
    }

    public static void registerUser(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                    SpotyGotFunctional.MessageConsumer successMessage,
                                    SpotyGotFunctional.MessageConsumer errorMessage) {
        var signupDetails =
                SignupModel.builder()
                        .firstName(getFirstName())
                        .lastName(getLastName())
                        .otherName(getOtherName())
                        .phone(getPhone())
                        .email(getEmail())
                        .password(getPassword())
                        .confirmPassword(getConfirmPassword())
                        .build();
        CompletableFuture<HttpResponse<String>> responseFuture = authRepository.signup(signupDetails);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201 || response.statusCode() == 204) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Account created successfully");
                });
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Access denied"));
                }
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Resource not found"));
                }
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An error occurred"));
                }
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            if (Connectivity.isConnectedToInternet()) {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("An in app error occurred"));
                }
            } else {
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("No Internet Connection"));
                }
            }
            SpotyLogger.writeToFile(throwable, DepartmentViewModel.class);
            return null;
        });
    }
}
