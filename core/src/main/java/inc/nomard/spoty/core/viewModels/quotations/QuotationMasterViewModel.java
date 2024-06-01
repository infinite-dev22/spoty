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

package inc.nomard.spoty.core.viewModels.quotations;

import com.google.gson.*;
import com.google.gson.reflect.*;
import static inc.nomard.spoty.core.values.SharedResources.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.quotations.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.http.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.*;
import lombok.extern.java.*;

@Log
public class QuotationMasterViewModel {
    @Getter
    public static final ObservableList<QuotationMaster> quotationsList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<QuotationMaster> quotations =
            new SimpleListProperty<>(quotationsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<Customer> customer = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty taxRate = new SimpleStringProperty("");
    private static final StringProperty discount = new SimpleStringProperty("");
    private static final StringProperty shippingFee = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");
    private static final QuotationsRepositoryImpl quotationRepository = new QuotationsRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        QuotationMasterViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        QuotationMasterViewModel.branch.set(branch);
    }

    public static ObjectProperty<Branch> branchProperty() {
        return branch;
    }

    public static String getNote() {
        return note.get();
    }

    public static void setNote(String note) {
        QuotationMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
    }

    public static Customer getCustomer() {
        return customer.get();
    }

    public static void setCustomer(Customer customer) {
        QuotationMasterViewModel.customer.set(customer);
    }

    public static ObjectProperty<Customer> customerProperty() {
        return customer;
    }

    public static String getTaxRate() {
        return taxRate.get();
    }

    public static void setTaxRate(String taxRate) {
        QuotationMasterViewModel.taxRate.set(taxRate);
    }

    public static StringProperty taxRateProperty() {
        return taxRate;
    }

    public static String getDiscount() {
        return discount.get();
    }

    public static void setDiscount(String discount) {
        QuotationMasterViewModel.discount.set(discount);
    }

    public static StringProperty discountProperty() {
        return discount;
    }

    public static String getShippingFee() {
        return shippingFee.get();
    }

    public static void setShippingFee(String shippingFee) {
        QuotationMasterViewModel.shippingFee.set(shippingFee);
    }

    public static StringProperty shippingFeeProperty() {
        return shippingFee;
    }

    public static ObservableList<QuotationMaster> getQuotations() {
        return quotations.get();
    }

    public static void setQuotations(ObservableList<QuotationMaster> quotations) {
        QuotationMasterViewModel.quotations.set(quotations);
    }

    public static ListProperty<QuotationMaster> quotationsProperty() {
        return quotations;
    }

    public static void resetProperties() {
        setId(0L);
        setCustomer(null);
        setBranch(null);
        setTaxRate("");
        setDiscount("");
        setShippingFee("");
        setNote("");
        PENDING_DELETES.clear();
    }

    public static void saveQuotationMaster(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                           SpotyGotFunctional.MessageConsumer successMessage,
                                           SpotyGotFunctional.MessageConsumer errorMessage) {
        var quotationMaster = QuotationMaster.builder()
                .customer(getCustomer())
                .notes(getNote())
                .taxRate(Double.parseDouble(getTaxRate()))
                .discount(Double.parseDouble(getDiscount()))
                .shippingFee(Double.parseDouble(getShippingFee()))
                .build();
        if (!QuotationDetailViewModel.quotationDetailsList.isEmpty()) {
            quotationMaster.setQuotationDetails(
                    QuotationDetailViewModel.getQuotationDetailsList());
        }
        CompletableFuture<HttpResponse<String>> responseFuture = quotationRepository.post(quotationMaster);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 201) {
                // Process the successful response
                successMessage.showMessage("Quotation created successfully");
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
            SpotyLogger.writeToFile(throwable, QuotationMasterViewModel.class);
            return null;
        });
    }

    public static void getAllQuotationMasters(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                              SpotyGotFunctional.MessageConsumer errorMessage) {
        CompletableFuture<HttpResponse<String>> responseFuture = quotationRepository.fetchAll();
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Type listType = new TypeToken<ArrayList<QuotationMaster>>() {
                }.getType();
                ArrayList<QuotationMaster> quotationMasterList = gson.fromJson(response.body(), listType);
                quotationsList.clear();
                quotationsList.addAll(quotationMasterList);
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
            SpotyLogger.writeToFile(throwable, QuotationMasterViewModel.class);
            return null;
        });
    }

    public static void getQuotationMaster(Long index,
                                          SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                          SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = quotationRepository.fetch(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                var quotationMaster = gson.fromJson(response.body(), QuotationMaster.class);
                setId(quotationMaster.getId());
                setNote(quotationMaster.getNotes());
                setTaxRate(getTaxRate());
                setDiscount(getDiscount());
                setShippingFee(getShippingFee());
                QuotationDetailViewModel.quotationDetailsList.clear();
                QuotationDetailViewModel.quotationDetailsList.addAll(quotationMaster.getQuotationDetails());
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
            SpotyLogger.writeToFile(throwable, QuotationMasterViewModel.class);
            return null;
        });
    }

    public static void searchItem(
            String search, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var searchModel = SearchModel.builder().search(search).build();
        CompletableFuture<HttpResponse<String>> responseFuture = quotationRepository.search(searchModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200) {
                // Process the successful response
                Type listType = new TypeToken<ArrayList<QuotationMaster>>() {
                }.getType();
                ArrayList<QuotationMaster> quotationMasterList = gson.fromJson(
                        response.body(), listType);
                quotationsList.clear();
                quotationsList.addAll(quotationMasterList);
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
            SpotyLogger.writeToFile(throwable, QuotationMasterViewModel.class);
            return null;
        });
    }

    public static void updateItem(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                  SpotyGotFunctional.MessageConsumer successMessage,
                                  SpotyGotFunctional.MessageConsumer errorMessage) {
        var quotationMaster = QuotationMaster.builder()
                .customer(getCustomer())
                .notes(getNote())
                .taxRate(Double.parseDouble(getTaxRate()))
                .discount(Double.parseDouble(getDiscount()))
                .shippingFee(Double.parseDouble(getShippingFee()))
                .build();
        if (!PENDING_DELETES.isEmpty()) {
            quotationMaster.setChildrenToDelete(PENDING_DELETES);
        }
        if (!QuotationDetailViewModel.getQuotationDetailsList().isEmpty()) {
            quotationMaster.setQuotationDetails(QuotationDetailViewModel.getQuotationDetailsList());
        }
        CompletableFuture<HttpResponse<String>> responseFuture = quotationRepository.put(quotationMaster);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 201 || response.statusCode() == 204) {
                // Process the successful response
                successMessage.showMessage("Quotation updated successfully");
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
            SpotyLogger.writeToFile(throwable, QuotationMasterViewModel.class);
            return null;
        });
    }

    public static void deleteItem(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer successMessage,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();
        CompletableFuture<HttpResponse<String>> responseFuture = quotationRepository.delete(findModel);
        responseFuture.thenAccept(response -> {
            // Handle successful response
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Process the successful response
                successMessage.showMessage("Quotation deleted successfully");
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
            SpotyLogger.writeToFile(throwable, QuotationMasterViewModel.class);
            return null;
        });
    }

}
