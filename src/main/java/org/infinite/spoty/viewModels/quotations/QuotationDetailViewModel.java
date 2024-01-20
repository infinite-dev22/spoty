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

package org.infinite.spoty.viewModels.quotations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.infinite.spoty.data_source.dtos.Product;
import org.infinite.spoty.data_source.dtos.UnitOfMeasure;
import org.infinite.spoty.data_source.dtos.quotations.QuotationDetail;
import org.infinite.spoty.data_source.dtos.quotations.QuotationMaster;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;
import org.infinite.spoty.data_source.repositories.implementations.QuotationsRepositoryImpl;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.viewModels.adapters.UnixEpochDateTypeAdapter;
import org.infinite.spoty.viewModels.adjustments.AdjustmentDetailViewModel;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;

import static org.infinite.spoty.values.SharedResources.*;

public class QuotationDetailViewModel {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    // TODO: Add more fields according to DB design and necessity.
    @Getter
    public static final ObservableList<QuotationDetail> quotationDetailsList =
            FXCollections.observableArrayList();
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
        resetProperties();
    }

    public static void saveQuotationDetails() {
        quotationDetailsList.forEach(quotationDetail -> {
            try {
                quotationsRepository.postDetail(quotationDetail);  // TODO: Add post multiple details.
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Platform.runLater(quotationDetailsList::clear);
    }

    public static void searchItem(String search) throws IOException, InterruptedException {
        var searchModel = new SearchModel();
        searchModel.setSearch(search);

        Type listType = new TypeToken<ArrayList<QuotationDetail>>() {
        }.getType();
        quotationDetailsList.clear();
        ArrayList<QuotationDetail> adjustmentDetailList = gson.fromJson(
                quotationsRepository.searchDetail(searchModel).body(), listType);
        quotationDetailsList.addAll(adjustmentDetailList);
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
        resetProperties();
    }

    public static void getAllQuotationDetails() throws IOException, InterruptedException {
        Type listType = new TypeToken<ArrayList<QuotationDetail>>() {
        }.getType();
        quotationDetailsList.clear();
        ArrayList<QuotationDetail> quotationDetailList = gson.fromJson(
                quotationsRepository.fetchAllDetail().body(), listType);
        quotationDetailsList.addAll(quotationDetailList);
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

    public static void updateQuotationDetails() throws IOException, InterruptedException {
        // TODO: Add save multiple on API.
        quotationDetailsList.forEach(
                adjustmentDetail -> {
                    try {
                        quotationsRepository.putDetail(adjustmentDetail);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, AdjustmentDetailViewModel.class);
                    }
                });
        getAllQuotationDetails();
    }

    public static void removeQuotationDetail(Long index, int tempIndex) {
        Platform.runLater(() -> quotationDetailsList.remove(tempIndex));
        PENDING_DELETES.add(index);
    }

    public static void deleteQuotationDetails(@NotNull LinkedList<Long> indexes) throws IOException, InterruptedException {
        LinkedList<FindModel> findModelList = new LinkedList<>();
        indexes.forEach(index -> findModelList.add(new FindModel(index)));
        quotationsRepository.deleteMultipleDetails(findModelList);
    }
}
