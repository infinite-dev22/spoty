package inc.nomard.spoty.core.viewModels.hrm.pay_roll;

import com.google.gson.*;
import com.google.gson.reflect.*;
import static inc.nomard.spoty.core.values.SharedResources.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import java.lang.reflect.*;
import java.net.http.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.extern.java.*;

@Log
public class SalaryAdvanceViewModel {
    public static final ObservableList<SalaryAdvance> salaryAdvancesList = FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<SalaryAdvance> salaryAdvances = new SimpleListProperty<>(salaryAdvancesList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<User> employee = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<PaySlip> paySlip = new SimpleObjectProperty<>(null);
    private static final StringProperty status = new SimpleStringProperty("");
    private static final DoubleProperty salary = new SimpleDoubleProperty(0);
    private static final DoubleProperty netSalary = new SimpleDoubleProperty(0);
    private static final SalaryAdvancesRepositoryImpl salaryAdvancesRepository = new SalaryAdvancesRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(long id) {
        SalaryAdvanceViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static User getEmployee() {
        return employee.get();
    }

    public static void setEmployee(User employee) {
        SalaryAdvanceViewModel.employee.set(employee);
    }

    public static ObjectProperty<User> employeeProperty() {
        return employee;
    }

    public static PaySlip getPaySlip() {
        return paySlip.get();
    }

    public static void setPaySlip(PaySlip paySlip) {
        SalaryAdvanceViewModel.paySlip.set(paySlip);
    }

    public static ObjectProperty<PaySlip> paySlipProperty() {
        return paySlip;
    }

    public static String getStatus() {
        return status.get();
    }

    public static void setStatus(String status) {
        SalaryAdvanceViewModel.status.set(status != null ? status : "");
    }

    public static StringProperty statusProperty() {
        return status;
    }

    public static double getSalary() {
        return salary.get();
    }

    public static void setSalary(double salary) {
        SalaryAdvanceViewModel.salary.set(salary);
    }

    public static DoubleProperty salaryProperty() {
        return salary;
    }

    public static double getNetSalary() {
        return netSalary.get();
    }

    public static void setNetSalary(double netSalary) {
        SalaryAdvanceViewModel.netSalary.set(netSalary);
    }

    public static DoubleProperty netSalaryProperty() {
        return netSalary;
    }

    public static ObservableList<SalaryAdvance> getSalaryAdvances() {
        return salaryAdvances.get();
    }

    public static void setSalaryAdvances(ObservableList<SalaryAdvance> salaryAdvances) {
        SalaryAdvanceViewModel.salaryAdvances.set(salaryAdvances);
    }

    public static ListProperty<SalaryAdvance> salaryAdvancesProperty() {
        return salaryAdvances;
    }

    public static void saveSalaryAdvance(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                         SpotyGotFunctional.MessageConsumer successMessage,
                                         SpotyGotFunctional.MessageConsumer errorMessage) {
        var salaryAdvance = SalaryAdvance.builder()
                .employee(getEmployee())
                .paySlip(getPaySlip())
                .status(getStatus())
                .salary(getSalary())
                .netSalary(getNetSalary())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = salaryAdvancesRepository.post(salaryAdvance);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201 || response.statusCode() == 204) {
                // Process the successful response
                successMessage.showMessage("Salary advance created successfully");
                onSuccess.run();
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, access denied");
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, resource not found");
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is definitely on our side");
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            errorMessage.showMessage("An error occurred, this is on your side");
            SpotyLogger.writeToFile(throwable, SalaryAdvanceViewModel.class);
            return null;
        });
    }

    public static void resetProperties() {
        Platform.runLater(
                () -> {
                    setId(0);
                    setTempId(-1);
                    setEmployee(null);
                    setPaySlip(null);
                    setStatus("");
                    setSalary(0);
                    setNetSalary(0);
                });
    }

    public static void getAllSalaryAdvances(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                            SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = salaryAdvancesRepository.fetchAll();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201) {
                // Process the successful response
                Type listType = new TypeToken<ArrayList<SalaryAdvance>>() {
                }.getType();
                ArrayList<SalaryAdvance> salaryAdvanceList = gson.fromJson(response.body(), listType);
                salaryAdvancesList.clear();
                salaryAdvancesList.addAll(salaryAdvanceList);
                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
                onSuccess.run();
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, access denied");
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, resource not found");
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is definitely on our side");
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            errorMessage.showMessage("An error occurred, this is on your side");
            SpotyLogger.writeToFile(throwable, SalaryAdvanceViewModel.class);
            return null;
        });
    }

    public static void getSalaryAdvance(Long index,
                                        SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                        SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = salaryAdvancesRepository.fetch(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                var salaryAdvance = gson.fromJson(response.body(), SalaryAdvance.class);
                setId(salaryAdvance.getId());
                setEmployee(salaryAdvance.getEmployee());
                setPaySlip(salaryAdvance.getPaySlip());
                setStatus(salaryAdvance.getStatus());
                setSalary(salaryAdvance.getSalary());
                setNetSalary(salaryAdvance.getNetSalary());
                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, access denied");
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, resource not found");
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is definitely on our side");
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            errorMessage.showMessage("An error occurred, this is on your side");
            SpotyLogger.writeToFile(throwable, SalaryAdvanceViewModel.class);
            return null;
        });
    }

    public static void searchItem(
            String search, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var searchModel = SearchModel.builder().search(search).build();
        CompletableFuture<HttpResponse<String>> responseFuture = salaryAdvancesRepository.search(searchModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Type listType = new TypeToken<ArrayList<SalaryAdvance>>() {
                }.getType();
                ArrayList<SalaryAdvance> salaryAdvanceList = gson.fromJson(
                        response.body(), listType);
                salaryAdvancesList.clear();
                salaryAdvancesList.addAll(salaryAdvanceList);
                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, access denied");
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, resource not found");
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is definitely on our side");
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            errorMessage.showMessage("An error occurred, this is on your side");
            SpotyLogger.writeToFile(throwable, SalaryAdvanceViewModel.class);
            return null;
        });
    }

    public static void updateSalaryAdvance(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                           SpotyGotFunctional.MessageConsumer successMessage,
                                           SpotyGotFunctional.MessageConsumer errorMessage) {
        var salaryAdvance = SalaryAdvance.builder()
                .id(getId())
                .employee(getEmployee())
                .paySlip(getPaySlip())
                .status(getStatus())
                .salary(getSalary())
                .netSalary(getNetSalary())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = salaryAdvancesRepository.put(salaryAdvance);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                successMessage.showMessage("Salary advance updated successfully");
                onSuccess.run();
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, access denied");
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, resource not found");
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is definitely on our side");
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            errorMessage.showMessage("An error occurred, this is on your side");
            SpotyLogger.writeToFile(throwable, SalaryAdvanceViewModel.class);
            return null;
        });
    }

    public static void deleteSalaryAdvance(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer successMessage,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = salaryAdvancesRepository.delete(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                successMessage.showMessage("Salary advance deleted successfully");
                onSuccess.run();
            } else if (response.statusCode() == 401) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, access denied");
            } else if (response.statusCode() == 404) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, resource not found");
            } else if (response.statusCode() == 500) {
                // Handle non-200 status codes
                errorMessage.showMessage("An error occurred, this is definitely on our side");
            }
        }).exceptionally(throwable -> {
            // Handle exceptions during the request (e.g., network issues)
            errorMessage.showMessage("An error occurred, this is on your side");
            SpotyLogger.writeToFile(throwable, SalaryAdvanceViewModel.class);
            return null;
        });
    }
}
