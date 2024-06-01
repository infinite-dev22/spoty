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
import com.google.gson.reflect.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import java.lang.reflect.*;
import java.net.http.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.extern.java.*;

@Log
public class ProductCategoryViewModel {
    public static final ProductCategoriesRepositoryImpl productCategoriesRepository = new ProductCategoriesRepositoryImpl();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty description = new SimpleStringProperty("");
    private static final StringProperty title = new SimpleStringProperty("");
    private static final StringProperty name = new SimpleStringProperty("");
    public static ObservableList<ProductCategory> categoriesList =
            FXCollections.observableArrayList();
    public static final ListProperty<ProductCategory> categories =
            new SimpleListProperty<>(categoriesList);
    public static ObservableList<ProductCategory> categoriesComboBoxList =
            FXCollections.observableArrayList();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        ProductCategoryViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static String getTitle() {
        return title.get();
    }

    public static void setTitle(String title) {
        ProductCategoryViewModel.title.set(title);
    }

    public static StringProperty titleProperty() {
        return title;
    }

    public static String getDescription() {
        return description.get();
    }

    public static void setDescription(String description) {
        ProductCategoryViewModel.description.set(description);
    }

    public static StringProperty descriptionProperty() {
        return description;
    }

    public static String getName() {
        return name.get();
    }

    public static void setName(String name) {
        ProductCategoryViewModel.name.set(name);
    }

    public static StringProperty nameProperty() {
        return name;
    }

    public static ObservableList<ProductCategory> getCategories() {
        return categories.get();
    }

    public static void setCategories(ObservableList<ProductCategory> categories) {
        ProductCategoryViewModel.categories.set(categories);
    }

    public static ListProperty<ProductCategory> categoriesProperty() {
        return categories;
    }

    public static void saveProductCategory(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                           SpotyGotFunctional.MessageConsumer successMessage,
                                           SpotyGotFunctional.MessageConsumer errorMessage) {
        var productCategory = ProductCategory.builder()
                .name(getName())
                .description(getDescription())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = productCategoriesRepository.post(productCategory);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201 || response.statusCode() == 204) {
                // Process the successful response
                successMessage.showMessage("Product category created successfully");
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
            SpotyLogger.writeToFile(throwable, ProductCategoryViewModel.class);
            return null;
        });
    }

    public static void clearProductCategoryData() {
        setId(0);
        setDescription("");
        setName("");
    }

    public static void getAllProductCategories(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                               SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = productCategoriesRepository.fetchAll();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201) {
                // Process the successful response
                Type listType = new TypeToken<ArrayList<ProductCategory>>() {
                }.getType();
                ArrayList<ProductCategory> productCategoryList = gson.fromJson(response.body(), listType);
                categoriesList.clear();
                categoriesList.addAll(productCategoryList);
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
            SpotyLogger.writeToFile(throwable, ProductCategoryViewModel.class);
            return null;
        });
    }

    public static void getItem(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = productCategoriesRepository.fetch(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                var productCategory = gson.fromJson(response.body(), ProductCategory.class);
                setId(productCategory.getId());
                setDescription(productCategory.getDescription());
                setName(productCategory.getName());
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
            SpotyLogger.writeToFile(throwable, ProductCategoryViewModel.class);
            return null;
        });
    }

    public static void searchItem(
            String search, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var searchModel = SearchModel.builder().search(search).build();
        CompletableFuture<HttpResponse<String>> responseFuture = productCategoriesRepository.search(searchModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Type listType = new TypeToken<ArrayList<ProductCategory>>() {
                }.getType();
                ArrayList<ProductCategory> categoryList = gson.fromJson(
                        response.body(), listType);
                categoriesList.clear();
                categoriesList.addAll(categoryList);
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
            SpotyLogger.writeToFile(throwable, ProductCategoryViewModel.class);
            return null;
        });
    }

    public static void updateItem(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                  SpotyGotFunctional.MessageConsumer successMessage,
                                  SpotyGotFunctional.MessageConsumer errorMessage) {
        var productCategory = ProductCategory.builder()
                .id(getId())
                .name(getName())
                .description(getDescription())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = productCategoriesRepository.put(productCategory);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                successMessage.showMessage("Product category updated successfully");
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
            SpotyLogger.writeToFile(throwable, ProductCategoryViewModel.class);
            return null;
        });
    }

    public static void deleteItem(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer successMessage,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = productCategoriesRepository.delete(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                successMessage.showMessage("Product category deleted successfully");
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
            SpotyLogger.writeToFile(throwable, ProductCategoryViewModel.class);
            return null;
        });
    }
}
