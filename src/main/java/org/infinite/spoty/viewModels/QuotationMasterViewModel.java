package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.QuotationMasterDao;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.Customer;
import org.infinite.spoty.database.models.QuotationMaster;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QuotationMasterViewModel {
    public static final ObservableList<QuotationMaster> quotationMasterList = FXCollections.observableArrayList();
    private static final IntegerProperty id = new SimpleIntegerProperty(0);
    private static final StringProperty date = new SimpleStringProperty("");
    private static final ObjectProperty<Customer> customer = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty status = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        QuotationMasterViewModel.id.set(id);
    }

    public static IntegerProperty idProperty() {
        return id;
    }

    public static Date getDate() {
        try {
            return new SimpleDateFormat("MMM dd, yyyy").parse(date.get());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setDate(String date) {
        QuotationMasterViewModel.date.set(date);
    }

    public static StringProperty dateProperty() {
        return date;
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

    public static String getStatus() {
        return status.get();
    }

    public static void setStatus(String status) {
        QuotationMasterViewModel.status.set(status);
    }

    public static StringProperty statusProperty() {
        return status;
    }

    public static void resetProperties() {
        setId(0);
        setDate("");
        setCustomer(null);
        setBranch(null);
        setStatus("");
        setNote("");
    }

    public static void saveQuotationMaster() {
        QuotationMaster quotationMaster = new QuotationMaster(getDate(), getCustomer(), getBranch(), getStatus(), getNote());
        quotationMaster.setQuotationDetails(QuotationDetailViewModel.quotationDetailsTempList);
        QuotationMasterDao.saveQuotationMaster(quotationMaster);
        resetProperties();
        QuotationDetailViewModel.quotationDetailsTempList.clear();
        getQuotationMasters();
    }

    public static ObservableList<QuotationMaster> getQuotationMasters() {
        quotationMasterList.clear();
        quotationMasterList.addAll(QuotationMasterDao.fetchQuotationMasters());
        return quotationMasterList;
    }

    public static void getItem(int quotationMasterID) {
        QuotationMaster quotationMaster = QuotationMasterDao.findQuotationMaster(quotationMasterID);
        setId(quotationMaster.getId());
        setDate(quotationMaster.getLocaleDate());
        setCustomer(quotationMaster.getCustomer());
        setBranch(quotationMaster.getBranch());
        setStatus(quotationMaster.getStatus());
        setNote(quotationMaster.getNotes());
        QuotationDetailViewModel.quotationDetailsTempList.addAll(quotationMaster.getQuotationDetails());
        quotationMaster.getQuotationDetails().forEach(System.out::println);
        getQuotationMasters();
    }

    public static void updateItem(int quotationMasterID) {
        QuotationMaster quotationMaster = new QuotationMaster(getDate(), getCustomer(), getBranch(), getStatus(), getNote());
        quotationMaster.setQuotationDetails(QuotationDetailViewModel.quotationDetailsTempList);
        QuotationMasterDao.updateQuotationMaster(quotationMaster, quotationMasterID);
        resetProperties();
        QuotationDetailViewModel.quotationDetailsTempList.clear();
        getQuotationMasters();
    }
}
