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

package inc.normad.spoty.core.viewModels.quotations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import inc.normad.spoty.network_bridge.dtos.Product;
import inc.normad.spoty.network_bridge.dtos.UnitOfMeasure;
import inc.normad.spoty.network_bridge.dtos.quotations.QuotationDetail;
import inc.normad.spoty.network_bridge.dtos.quotations.QuotationMaster;
import inc.normad.spoty.network_bridge.models.FindModel;
import inc.normad.spoty.network_bridge.models.SearchModel;
import inc.normad.spoty.network_bridge.repositories.implementations.QuotationsRepositoryImpl;
import inc.normad.spoty.utils.ParameterlessConsumer;
import inc.normad.spoty.utils.SpotyLogger;
import inc.normad.spoty.utils.SpotyThreader;
import inc.normad.spoty.core.viewModels.adapters.UnixEpochDateTypeAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static inc.normad.spoty.core.values.SharedResources.*;

public class QuotationDetailViewModel {
    // TODO: Add more fields according to DB design and necessity.
    @Getter
    public static final ObservableList<QuotationDetail> quotationDetailsList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<QuotationDetail> quotationDetails =
            new SimpleListProperty<>(quotationDetailsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private static final ObjectProperty<UnitOfMeasure> saleUnit = new SimpleObjectProperty<>();
    private static final ObjectProperty<QuotationMaster> quotation = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty();
    private static final StringProperty netTax = new SimpleStringProperty();
    private static final StringProperty discount = new SimpleStringProperty();
    private static final QuotationsRepositoryImpl quotationsRepository = new QuotationsRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        QuotationDetailViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static Product getProduct() {
        return product.get();
    }

    public static void setProduct(Product product) {
        QuotationDetailViewModel.product.set(product);
    }

    public static ObjectProperty<Product> productProperty() {
        return product;
    }

    public static UnitOfMeasure getSaleUnit() {
        return saleUnit.get();
    }

    public static void setUnit(UnitOfMeasure saleUnit) {
        QuotationDetailViewModel.saleUnit.set(saleUnit);
    }

    public static ObjectProperty<UnitOfMeasure> saleUnitProperty() {
        return saleUnit;
    }

    public static QuotationMaster getQuotation() {
        return quotation.get();
    }

    public static void setQuotation(QuotationMaster quotation) {
        QuotationDetailViewModel.quotation.set(quotation);
    }

    public static ObjectProperty<QuotationMaster> quotationProperty() {
        return quotation;
    }

    public static int getQuantity() {
        return Integer.parseInt(quantity.get());
    }

    public static void setQuantity(String quantity) {
        QuotationDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
    }

    public static double getNetTax() {
        return (Objects.equals(netTax.get(), null) || netTax.get().isBlank())
                ? 0.0
                : Double.parseDouble(netTax.get());
    }

    public static void setNetTax(String netTax) {
        QuotationDetailViewModel.netTax.set(netTax);
    }

    public static StringProperty taxProperty() {
        return netTax;
    }

    public static double getDiscount() {
        return (Objects.equals(discount.get(), null) || discount.get().isBlank())
                ? 0.0
                : Double.parseDouble(discount.get());
    }

    public static void setDiscount(String discount) {
        QuotationDetailViewModel.discount.set(discount);
    }

    public static StringProperty discountProperty() {
        return discount;
    }

    public static ObservableList<QuotationDetail> getQuotationDetails() {
        return quotationDetails.get();
    }

    public static void setQuotationDetails(ObservableList<QuotationDetail> quotationDetails) {
        QuotationDetailViewModel.quotationDetails.set(quotationDetails);
    }

    public static ListProperty<QuotationDetail> quotationDetailsProperty() {
        return quotationDetails;
    }

    public static void resetProperties() {
        setId(0L);
        setTempId(-1);
        setProduct(null);
        setNetTax("");
        setDiscount("");
        setQuantity("");
    }

    public static void addQuotationDetails() {
        var quotationDetail = QuotationDetail.builder()
//                .price(getPrice())
                .saleUnit(getSaleUnit())
                .product(getProduct())
                .quotation(getQuotation())
                .netTax(getNetTax())
                .taxType(getProduct().getTaxType())
                .discount(getDiscount())
//                .discountType(getDiscountType())
//                .total(getTotalAmount())
                .quantity(getQuantity())
                .serialNumber(getProduct().getSerialNumber()) //TODO: Remove, not needed.
                .build();

        quotationDetailsList.add(quotationDetail);
    }

    public static void saveQuotationDetails(@Nullable ParameterlessConsumer onActivity, @Nullable ParameterlessConsumer onSuccess, @Nullable ParameterlessConsumer onFailed) {
        quotationDetailsList.forEach(quotationDetail -> {
            var task = quotationsRepository.postDetail(quotationDetail);  // TODO: Add post multiple details.
            if (Objects.nonNull(onActivity)) {
                task.setOnRunning(workerStateEvent -> onActivity.run());
            }
            if (Objects.nonNull(onSuccess)) {
                task.setOnFailed(workerStateEvent -> onSuccess.run());
            }
            if (Objects.nonNull(onFailed)) {
                task.setOnFailed(workerStateEvent -> onFailed.run());
            }
            SpotyThreader.spotyThreadPool(task);
        });

        quotationDetailsList.clear();
    }

    public static void searchItem(String search, @Nullable ParameterlessConsumer onActivity, @Nullable ParameterlessConsumer onFailed) throws IOException, InterruptedException {
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        var task = quotationsRepository.searchDetail(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> onFailed.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            Type listType = new TypeToken<ArrayList<QuotationDetail>>() {
            }.getType();
            ArrayList<QuotationDetail> adjustmentDetailList = new ArrayList<>();
            try {
                adjustmentDetailList = gson.fromJson(
                        task.get().body(), listType);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, QuotationDetailViewModel.class);
            }

            quotationDetailsList.clear();
            quotationDetailsList.addAll(adjustmentDetailList);
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateQuotationDetail(Long index) {
        QuotationDetail quotationDetail = quotationDetailsList.get(Math.toIntExact(index));
        quotationDetail.setProduct(getProduct());
        quotationDetail.setSaleUnit(getSaleUnit());
        quotationDetail.setNetTax(getNetTax());
        quotationDetail.setDiscount(getDiscount());
        quotationDetail.setQuantity(getQuantity());
        quotationDetail.setId(getId());
        quotationDetail.setQuotation(getQuotation());
        quotationDetailsList.remove(getTempId());
        quotationDetailsList.add(getTempId(), quotationDetail);
    }

    public static void getAllQuotationDetails(@Nullable ParameterlessConsumer onActivity) {
        Type listType = new TypeToken<ArrayList<QuotationDetail>>() {
        }.getType();
        var task = quotationsRepository.fetchAllDetail();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        task.setOnSucceeded(workerStateEvent -> {
            ArrayList<QuotationDetail> quotationDetailList = new ArrayList<>();
            try {
                quotationDetailList = gson.fromJson(
                        task.get().body(), listType);
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, QuotationDetailViewModel.class);
            }

            quotationDetailsList.clear();
            quotationDetailsList.addAll(quotationDetailList);
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getQuotationDetail(QuotationDetail quotationDetail) {
        Platform.runLater(
                () -> {
                    setTempId(getQuotationDetails().indexOf(quotationDetail));
                    setId(quotationDetail.getId());
                    setProduct(quotationDetail.getProduct());
                    setUnit(quotationDetail.getProduct().getUnit());
                    setNetTax(String.valueOf(quotationDetail.getNetTax()));
                    setDiscount(String.valueOf(quotationDetail.getDiscount()));
                    setQuantity(String.valueOf(quotationDetail.getQuantity()));
                    setQuotation(quotationDetail.getQuotation());
                });
    }

    public static void updateQuotationDetails(
            @Nullable ParameterlessConsumer onActivity,
            @Nullable ParameterlessConsumer onSuccess,
            @Nullable ParameterlessConsumer onFailed) {
        // TODO: Add save multiple on API.
        quotationDetailsList.forEach(
                adjustmentDetail -> {
                    var task = quotationsRepository.putDetail(adjustmentDetail);
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
                });
//        getAllQuotationDetails();
    }

    public static void removeQuotationDetail(Long index, int tempIndex) {
        Platform.runLater(() -> quotationDetailsList.remove(tempIndex));
        PENDING_DELETES.add(index);
    }

    public static void deleteQuotationDetails(@NotNull LinkedList<Long> indexes,
                                              @Nullable ParameterlessConsumer onActivity,
                                              @Nullable ParameterlessConsumer onSuccess,
                                              @Nullable ParameterlessConsumer onFailed) {
        LinkedList<FindModel> findModelList = new LinkedList<>();
        indexes.forEach(index -> findModelList.add(new FindModel(index)));

        var task = quotationsRepository.deleteMultipleDetails(findModelList);
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
}
