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

import static inc.nomard.spoty.core.values.SharedResources.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.quotations.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.*;
import lombok.extern.java.*;

@Log
public class QuotationDetailViewModel {
    // TODO: Add more fields according to DB design and necessity.
    @Getter
    public static final ObservableList<QuotationDetail> quotationDetailsList =
            FXCollections.observableArrayList();
    private static final ListProperty<QuotationDetail> quotationDetails =
            new SimpleListProperty<>(quotationDetailsList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private static final StringProperty quantity = new SimpleStringProperty();
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

    public static int getQuantity() {
        return Integer.parseInt(quantity.get());
    }

    public static void setQuantity(String quantity) {
        QuotationDetailViewModel.quantity.set(quantity);
    }

    public static StringProperty quantityProperty() {
        return quantity;
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
        setQuantity("");
    }

    public static void addQuotationDetails() {
        var quotationDetail = QuotationDetail.builder()
                .product(getProduct())
                .quantity(getQuantity())
                .build();
        quotationDetailsList.add(quotationDetail);
    }

    public static void updateQuotationDetail(Long index) {
        QuotationDetail quotationDetail = quotationDetailsList.get(Math.toIntExact(index));
        quotationDetail.setId(getId());
        quotationDetail.setProduct(getProduct());
        quotationDetail.setQuantity(getQuantity());
        quotationDetailsList.set(getTempId(), quotationDetail);
    }

    public static void getQuotationDetail(QuotationDetail quotationDetail) {
        setTempId(getQuotationDetails().indexOf(quotationDetail));
        setId(quotationDetail.getId());
        setProduct(quotationDetail.getProduct());
        setQuantity(String.valueOf(quotationDetail.getQuantity()));
    }

    public static void removeQuotationDetail(Long index, int tempIndex) {
        Platform.runLater(() -> quotationDetailsList.remove(tempIndex));
        PENDING_DELETES.add(index);
    }
}
