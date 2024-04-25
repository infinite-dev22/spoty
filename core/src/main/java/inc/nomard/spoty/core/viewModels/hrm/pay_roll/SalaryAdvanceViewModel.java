package inc.nomard.spoty.core.viewModels.hrm.pay_roll;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import inc.nomard.spoty.core.viewModels.adapters.UnixEpochDateTypeAdapter;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.User;
import inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll.PaySlip;
import inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll.SalaryAdvance;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;
import inc.nomard.spoty.network_bridge.repositories.implementations.SalaryAdvancesRepositoryImpl;
import inc.nomard.spoty.utils.ParameterlessConsumer;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static inc.nomard.spoty.core.values.SharedResources.setTempId;

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

    public static void saveSalaryAdvance(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var salaryAdvance = SalaryAdvance.builder()
                .employee(getEmployee())
                .paySlip(getPaySlip())
                .status(getStatus())
                .salary(getSalary())
                .netSalary(getNetSalary())
                .build();

        var task = salaryAdvancesRepository.post(salaryAdvance);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
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

    public static void getAllSalaryAdvances(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var task = salaryAdvancesRepository.fetchAll();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<SalaryAdvance>>() {
                }.getType();
                ArrayList<SalaryAdvance> salaryAdvanceList = gson.fromJson(task.get().body(), listType);

                salaryAdvancesList.clear();
                salaryAdvancesList.addAll(salaryAdvanceList);

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, SalaryAdvanceViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getSalaryAdvance(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = salaryAdvancesRepository.fetch(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                var salaryAdvance = gson.fromJson(task.get().body(), SalaryAdvance.class);

                setId(salaryAdvance.getId());
                setEmployee(salaryAdvance.getEmployee());
                setPaySlip(salaryAdvance.getPaySlip());
                setStatus(salaryAdvance.getStatus());
                setSalary(salaryAdvance.getSalary());
                setNetSalary(salaryAdvance.getNetSalary());

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, SalaryAdvanceViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void searchItem(
            String search,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();
        var task = salaryAdvancesRepository.search(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<SalaryAdvance>>() {
                }.getType();
                ArrayList<SalaryAdvance> salaryAdvanceList = gson.fromJson(task.get()
                        .body(), listType);

                salaryAdvancesList.clear();
                salaryAdvancesList.addAll(salaryAdvanceList);

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, SalaryAdvanceViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateSalaryAdvance(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var salaryAdvance = SalaryAdvance.builder()
                .id(getId())
                .employee(getEmployee())
                .paySlip(getPaySlip())
                .status(getStatus())
                .salary(getSalary())
                .netSalary(getNetSalary())
                .build();

        var task = salaryAdvancesRepository.put(salaryAdvance);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onSuccess)) {
            task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        SpotyThreader.spotyThreadPool(task);
    }

//    public static void updateSalaryAdvance(long index) {
//        var salaryAdvancesRepository = new SalaryAdvancesRepositoryImpl();
////        Dao<SalaryAdvance, Long> salaryAdvanceDao = DaoManager.createDao(connectionSource, SalaryAdvance.class);
////
////        SalaryAdvance salaryAdvance = salaryAdvanceDao.queryForId(index);
////
////        salaryAdvance.setQuantity(getQuantity());
////        salaryAdvance.setUpdatedBy(getName());
////        salaryAdvance.setUpdatedAt(Date.now());
////
////        salaryAdvanceDao.update(salaryAdvance);
//
//        Platform.runLater(SalaryAdvanceViewModel::resetProperties);
//
//        getAllSalaryAdvances();
//    }

    public static void deleteSalaryAdvance(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = salaryAdvancesRepository.delete(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }
}
