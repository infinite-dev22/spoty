package org.infinite.spoty.viewModels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.SaleMasterDao;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.Customer;
import org.infinite.spoty.database.models.SaleMaster;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaleMasterViewModel {
    public static final ObservableList<SaleMaster> saleMasterList = FXCollections.observableArrayList();
    private static final IntegerProperty id = new SimpleIntegerProperty(0);
    private static final StringProperty date = new SimpleStringProperty("");
    private static final StringProperty ref = new SimpleStringProperty("");
    private static final ObjectProperty<Customer> customer = new SimpleObjectProperty<>(null);
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty status = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");

    public static int getId() {
        return id.get();
    }

    public static void setId(int id) {
        SaleMasterViewModel.id.set(id);
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
        SaleMasterViewModel.date.set(date);
    }

    public static StringProperty dateProperty() {
        return date;
    }

    public static Customer getCustomer() {
        return customer.get();
    }

    public static void setCustomer(Customer customer) {
        SaleMasterViewModel.customer.set(customer);
    }

    public static ObjectProperty<Customer> customerProperty() {
        return customer;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        SaleMasterViewModel.branch.set(branch);
    }

    public static ObjectProperty<Branch> branchProperty() {
        return branch;
    }

    public static String getStatus() {
        return status.get();
    }

    public static void setStatus(String status) {
        SaleMasterViewModel.status.set(status);
    }

    public static StringProperty statusProperty() {
        return status;
    }

    public static String getNote() {
        return note.get();
    }

    public static void setNote(String note) {
        SaleMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
    }

    public static void resetProperties() {
        setId(0);
        setDate("");
        setCustomer(null);
        setBranch(null);
        setStatus("");
        setNote("");
    }

    public static void saveSaleMaster() {
        SaleMaster saleMaster = new SaleMaster(getCustomer(), getBranch(), getStatus(), getNote(), getDate());
        saleMaster.setSaleDetails(SaleDetailViewModel.saleDetailTempList);
        SaleMasterDao.saveSaleMaster(saleMaster);
        resetProperties();
        getSaleMasters();
    }

    public static ObservableList<SaleMaster> getSaleMasters() {
        saleMasterList.clear();
        saleMasterList.addAll(SaleMasterDao.fetchSaleMasters());
        return saleMasterList;
    }

}
