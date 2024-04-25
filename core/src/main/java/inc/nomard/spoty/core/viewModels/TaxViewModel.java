package inc.nomard.spoty.core.viewModels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import inc.nomard.spoty.core.viewModels.adapters.UnixEpochDateTypeAdapter;
import inc.nomard.spoty.network_bridge.dtos.Tax;
import inc.nomard.spoty.network_bridge.dtos.Tax;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;
import inc.nomard.spoty.network_bridge.repositories.implementations.TaxesRepositoryImpl;
import inc.nomard.spoty.utils.ParameterlessConsumer;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class TaxViewModel {
    public static final ObservableList<Tax> taxesList = FXCollections.observableArrayList();
    public static final ObservableList<Tax> taxesComboBoxList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<Tax> taxes = new SimpleListProperty<>(taxesList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final DoubleProperty percentage = new SimpleDoubleProperty();
    private static final TaxesRepositoryImpl taxesRepository = new TaxesRepositoryImpl();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        TaxViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        TaxViewModel.name.set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static Double getPercentage() {
        return percentage.get();
    }

    public static void setPercentage(Double percentage) {
        TaxViewModel.percentage.set(percentage);
    }

    public static DoubleProperty percentageProperty() {
        return percentage;
    }

    public static ObservableList<Tax> getTaxes() {
        return taxes.get();
    }

    public static void setTaxes(ObservableList<Tax> taxes) {
        TaxViewModel.taxes.set(taxes);
    }

    public static ListProperty<Tax> taxesProperty() {
        return taxes;
    }

    public static void resetProperties() {
        setId(0);
        setName("");
        setPercentage(0.00);
    }

    public static void saveTax(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var tax = Tax.builder()
                .name(getName())
                .percentage(getPercentage())
                .build();

        var task = taxesRepository.post(tax);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getTaxes(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        var task = taxesRepository.fetchAll();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<Tax>>() {
                }.getType();
                ArrayList<Tax> productList = gson.fromJson(task.get().body(), listType);

                taxesList.clear();
                taxesList.addAll(productList);

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, TaxViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getTax(
            Long index,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = taxesRepository.fetch(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                var tax = gson.fromJson(task.get().body(), Tax.class);

                setId(tax.getId());
                setName(tax.getName());
                setPercentage(tax.getPercentage());

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, BankViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void searchTax(
            String search,
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();
        var task = taxesRepository.search(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<Tax>>() {
                }.getType();
                ArrayList<Tax> currencyList = gson.fromJson(task.get()
                        .body(), listType);

                taxesList.clear();
                taxesList.addAll(currencyList);

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, TaxViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateTax(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var tax = Tax.builder()
                .id(getId())
                .name(getName())
                .percentage(getPercentage())
                .build();

        var task = taxesRepository.put(tax);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }

    public static void deleteTax(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = taxesRepository.delete(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> onFailed.run());
        SpotyThreader.spotyThreadPool(task);
    }
}
