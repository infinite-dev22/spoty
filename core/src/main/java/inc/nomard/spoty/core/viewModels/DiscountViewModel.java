package inc.nomard.spoty.core.viewModels;

import com.google.gson.*;
import com.google.gson.reflect.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.extern.slf4j.*;

@Slf4j
public class DiscountViewModel {
    public static final ObservableList<Discount> discountsList = FXCollections.observableArrayList();
    public static final ObservableList<Discount> discountsComboBoxList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<Discount> discounts = new SimpleListProperty<>(discountsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty name = new SimpleStringProperty("");
    private static final DoubleProperty percentage = new SimpleDoubleProperty();
    private static final DiscountsRepositoryImpl discountsRepository = new DiscountsRepositoryImpl();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        DiscountViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        DiscountViewModel.name.set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static Double getPercentage() {
        return percentage.get();
    }

    public static void setPercentage(Double percentage) {
        DiscountViewModel.percentage.set(percentage);
    }

    public static DoubleProperty percentageProperty() {
        return percentage;
    }

    public static ObservableList<Discount> getDiscounts() {
        return discounts.get();
    }

    public static void setDiscounts(ObservableList<Discount> discounts) {
        DiscountViewModel.discounts.set(discounts);
    }

    public static ListProperty<Discount> discountsProperty() {
        return discounts;
    }

    public static void resetProperties() {
        setId(0);
        setName("");
        setPercentage(0.00);
    }

    public static void saveDiscount(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var discount = Discount.builder()
                .name(getName())
                .percentage(getPercentage())
                .build();

        var task = discountsRepository.post(discount);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> {
            onFailed.run();
            System.err.println("The task failed with the following exception:");
            task.getException().printStackTrace(System.err);
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getDiscounts(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var task = discountsRepository.fetchAll();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> {
                onFailed.run();
                System.err.println("The task failed with the following exception:");
                task.getException().printStackTrace(System.err);
            });
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<Discount>>() {
                }.getType();
                ArrayList<Discount> productList = gson.fromJson(task.get().body(), listType);

                discountsList.clear();
                discountsList.addAll(productList);

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, DiscountViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getDiscount(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = discountsRepository.fetch(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> {
                onFailed.run();
                System.err.println("The task failed with the following exception:");
                task.getException().printStackTrace(System.err);
            });
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                var discount = gson.fromJson(task.get().body(), Discount.class);

                setId(discount.getId());
                setName(discount.getName());
                setPercentage(discount.getPercentage());

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, BankViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void searchDiscount(
            String search,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();
        var task = discountsRepository.search(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> {
                onFailed.run();
                System.err.println("The task failed with the following exception:");
                task.getException().printStackTrace(System.err);
            });
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<Discount>>() {
                }.getType();
                ArrayList<Discount> currencyList = gson.fromJson(task.get()
                        .body(), listType);

                discountsList.clear();
                discountsList.addAll(currencyList);

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, DiscountViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateDiscount(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var discount = Discount.builder()
                .id(getId())
                .name(getName())
                .percentage(getPercentage())
                .build();

        var task = discountsRepository.put(discount);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> {
            onFailed.run();
            System.err.println("The task failed with the following exception:");
            task.getException().printStackTrace(System.err);
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void deleteDiscount(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = discountsRepository.delete(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> {
            onFailed.run();
            System.err.println("The task failed with the following exception:");
            task.getException().printStackTrace(System.err);
        });
        SpotyThreader.spotyThreadPool(task);
    }
}
