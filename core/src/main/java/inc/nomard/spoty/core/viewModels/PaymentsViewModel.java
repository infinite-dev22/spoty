package inc.nomard.spoty.core.viewModels;

import inc.nomard.spoty.network_bridge.auth.*;
import inc.nomard.spoty.network_bridge.dtos.payments.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.connectivity.*;
import inc.nomard.spoty.utils.functional_paradigm.*;

import java.net.http.*;
import java.util.*;
import java.util.concurrent.*;

import javafx.application.*;
import javafx.beans.property.*;
import lombok.extern.java.*;

@Log
public class PaymentsViewModel {
    private static final StringProperty cardNumber = new SimpleStringProperty("");
    private static final StringProperty cvv = new SimpleStringProperty("");
    private static final StringProperty expiryMonth = new SimpleStringProperty("");
    private static final StringProperty expiryYear = new SimpleStringProperty("");
    private static final StringProperty amount = new SimpleStringProperty("");
    private static final StringProperty firstName = new SimpleStringProperty("");
    private static final StringProperty lastName = new SimpleStringProperty("");
    private static final StringProperty fullName = new SimpleStringProperty("");
    private static final StringProperty reference = new SimpleStringProperty("");
    private static final StringProperty clientIp = new SimpleStringProperty("");
    private static final StringProperty email = new SimpleStringProperty("");
    private static final StringProperty pin = new SimpleStringProperty("");
    private static final StringProperty city = new SimpleStringProperty("");
    private static final StringProperty address = new SimpleStringProperty("");
    private static final StringProperty state = new SimpleStringProperty("");
    private static final StringProperty country = new SimpleStringProperty("");
    private static final StringProperty zipcode = new SimpleStringProperty("");
    private static final StringProperty planName = new SimpleStringProperty("");
    private static final BooleanProperty recurring = new SimpleBooleanProperty(false);
    private static final StringProperty phoneNumber = new SimpleStringProperty("");
    private static final StringProperty deviceFingerPrint = new SimpleStringProperty("");
    private static final PaymentsRepositoryImpl paymentsRepository = new PaymentsRepositoryImpl();

    public static String getCardNumber() {
        return cardNumber.get();
    }

    public static void setCardNumber(String cardNumber) {
        PaymentsViewModel.cardNumber.set(cardNumber);
    }

    public static StringProperty cardNumberProperty() {
        return cardNumber;
    }

    public static String getCvv() {
        return cvv.get();
    }

    public static void setCvv(String cvv) {
        PaymentsViewModel.cvv.set(cvv);
    }

    public static StringProperty cvvProperty() {
        return cvv;
    }

    public static String getExpiryMonth() {
        return expiryMonth.get();
    }

    public static void setExpiryMonth(String expiryMonth) {
        PaymentsViewModel.expiryMonth.set(expiryMonth);
    }

    public static StringProperty expiryMonthProperty() {
        return expiryMonth;
    }

    public static String getExpiryYear() {
        return expiryYear.get();
    }

    public static void setExpiryYear(String expiryYear) {
        PaymentsViewModel.expiryYear.set(expiryYear);
    }

    public static StringProperty expiryYearProperty() {
        return expiryYear;
    }

    public static String getAmount() {
        return amount.get();
    }

    public static void setAmount(String amount) {
        PaymentsViewModel.amount.set(amount);
    }

    public static StringProperty amountProperty() {
        return amount;
    }

    public static String getFirstName() {
        return firstName.get();
    }

    public static void setFirstName(String firstName) {
        PaymentsViewModel.firstName.set(firstName);
    }

    public static StringProperty firstNameProperty() {
        return firstName;
    }

    public static String getLastName() {
        return lastName.get();
    }

    public static void setLastName(String lastName) {
        PaymentsViewModel.lastName.set(lastName);
    }

    public static StringProperty lastNameProperty() {
        return lastName;
    }

    public static String getFullName() {
        return fullName.get();
    }

    public static void setFullName(String fullName) {
        PaymentsViewModel.fullName.set(fullName);
    }

    public static StringProperty fullNameProperty() {
        return fullName;
    }

    public static String getReference() {
        return reference.get();
    }

    public static void setReference(String reference) {
        PaymentsViewModel.reference.set(reference);
    }

    public static StringProperty referenceProperty() {
        return reference;
    }

    public static String getClientIp() {
        return clientIp.get();
    }

    public static void setClientIp(String clientIp) {
        PaymentsViewModel.clientIp.set(clientIp);
    }

    public static StringProperty clientIpProperty() {
        return clientIp;
    }

    public static String getEmail() {
        return email.get();
    }

    public static void setEmail(String email) {
        PaymentsViewModel.email.set(email);
    }

    public static StringProperty emailProperty() {
        return email;
    }

    public static String getPin() {
        return pin.get();
    }

    public static void setPin(String pin) {
        PaymentsViewModel.pin.set(pin);
    }

    public static StringProperty pinProperty() {
        return pin;
    }

    public static String getCity() {
        return city.get();
    }

    public static void setCity(String city) {
        PaymentsViewModel.city.set(city);
    }

    public static StringProperty cityProperty() {
        return city;
    }

    public static String getAddress() {
        return address.get();
    }

    public static void setAddress(String address) {
        PaymentsViewModel.address.set(address);
    }

    public static StringProperty addressProperty() {
        return address;
    }

    public static String getState() {
        return state.get();
    }

    public static void setState(String state) {
        PaymentsViewModel.state.set(state);
    }

    public static StringProperty stateProperty() {
        return state;
    }

    public static String getCountry() {
        return country.get();
    }

    public static void setCountry(String country) {
        PaymentsViewModel.country.set(country);
    }

    public static StringProperty countryProperty() {
        return country;
    }

    public static String getZipcode() {
        return zipcode.get();
    }

    public static void setZipcode(String zipcode) {
        PaymentsViewModel.zipcode.set(zipcode);
    }

    public static StringProperty zipcodeProperty() {
        return zipcode;
    }

    public static String getPlanName() {
        return planName.get();
    }

    public static void setPlanName(String planName) {
        PaymentsViewModel.planName.set(planName);
    }

    public static StringProperty planNameProperty() {
        return planName;
    }

    public static Boolean getRecurring() {
        return recurring.get();
    }

    public static void setRecurring(Boolean recurring) {
        PaymentsViewModel.recurring.set(recurring);
    }

    public static BooleanProperty recurringProperty() {
        return recurring;
    }

    public static String getPhoneNumber() {
        return phoneNumber.get();
    }

    public static void setPhoneNumber(String phoneNumber) {
        PaymentsViewModel.phoneNumber.set(phoneNumber);
    }

    public static StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public static String getDeviceFingerPrint() {
        return deviceFingerPrint.get();
    }

    public static void setDeviceFingerPrint(String deviceFingerPrint) {
        PaymentsViewModel.deviceFingerPrint.set(deviceFingerPrint);
    }

    public static StringProperty deviceFingerPrintProperty() {
        return deviceFingerPrint;
    }

    public static void resetProperties() {
        setCountry("");
        setCardNumber("");
        setCvv("");
        setExpiryMonth("");
        setExpiryYear("");
        setAmount("");
        setFirstName("");
        setLastName("");
        setFullName("");
        setReference("");
        setClientIp("");
        setEmail("");
        setPin("");
        setCity("");
        setAddress("");
        setState("");
        setCountry("");
        setZipcode("");
        setPlanName("");
        setRecurring(false);
        setPhoneNumber("");
        setDeviceFingerPrint("");
    }

    public static void cardPayment(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                   SpotyGotFunctional.MessageConsumer successMessage,
                                   SpotyGotFunctional.MessageConsumer errorMessage) {
        var payload = CardModel.builder()
                .card(getCardNumber())
                .cvv(getCvv())
                .expiryMonth(getExpiryMonth())
                .expiryYear(getExpiryYear())
                .amount(getAmount())
                .firstName(getFirstName())
                .lastName(getLastName())
                .fullName(getFullName())
                .reference(getReference())
                .clientIp(getClientIp())
                .email(getEmail())
                .pin(getPin())
                .city(getCity())
                .address(getAddress())
                .state(getState())
                .country(getCountry())
                .zipcode(getZipcode())
                .planName(getPlanName())
                .recurring(getRecurring())
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = paymentsRepository.cardPay(payload);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Payment created successfully");
                });
            } else if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Payment processed successfully");
                });
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Resource not found"));
                }
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is on our side");
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is on our side");
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
            SpotyLogger.writeToFile(throwable, PaymentsViewModel.class);
            return null;
        });
    }

    public static void mtnMomoPayment(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                      SpotyGotFunctional.MessageConsumer successMessage,
                                      SpotyGotFunctional.MessageConsumer errorMessage) {
        var payload = MoMoModel.builder()
                .amount(getAmount())
                .email(getEmail())
                .phoneNumber(getPhoneNumber())
                .fullName(getFullName())
                .clientIp(getClientIp())
                .deviceFingerPrint(getDeviceFingerPrint())
                .planName(getPlanName())
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = paymentsRepository.mtnMomoPay(payload);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Payment created successfully");
                });
            } else if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Payment processed successfully");
                });
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Resource not found"));
                }
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is on our side");
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is on our side");
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
            SpotyLogger.writeToFile(throwable, PaymentsViewModel.class);
            return null;
        });
    }

    public static void airtelMomoPayment(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                         SpotyGotFunctional.MessageConsumer successMessage,
                                         SpotyGotFunctional.MessageConsumer errorMessage) {
        var payload = MoMoModel.builder()
                .amount(getAmount())
                .email(getEmail())
                .phoneNumber(getPhoneNumber())
                .fullName(getFullName())
                .clientIp(getClientIp())
                .deviceFingerPrint(getDeviceFingerPrint())
                .planName(getPlanName())
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = paymentsRepository.airtelMomoPay(payload);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Payment created successfully");
                });
            } else if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Payment processed successfully");
                });
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Resource not found"));
                }
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is on our side");
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is on our side");
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
            SpotyLogger.writeToFile(throwable, PaymentsViewModel.class);
            return null;
        });
    }

    public static void startTrial(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                  SpotyGotFunctional.MessageConsumer successMessage,
                                  SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = new FindModel(ProtectedGlobals.user.getTenant().getId());
        CompletableFuture<HttpResponse<String>> responseFuture = paymentsRepository.startTrial(findModel);
        responseFuture.thenAccept(response -> {
            if (response.statusCode() == 200) {
                // Process the successful response
                Platform.runLater(() -> {
                    onSuccess.run();
                    successMessage.showMessage("Action processed successfully");
                });
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                if (Objects.nonNull(errorMessage)) {
                    Platform.runLater(() -> errorMessage.showMessage("Resource not found"));
                }
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is on our side");
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is on our side");
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
            SpotyLogger.writeToFile(throwable, PaymentsViewModel.class);
            return null;
        });
    }
}
