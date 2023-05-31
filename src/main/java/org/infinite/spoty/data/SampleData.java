package org.infinite.spoty.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.models.*;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SampleData implements Serializable {
    public static List<QuickStats> quickStatsSampleData() {
        List<QuickStats> samples = new ArrayList<>();

        QuickStats quickStats = new QuickStats();
        quickStats.setTitle("2050");
        quickStats.setSubtitle("Total Orders");
        samples.add(quickStats);

        quickStats = new QuickStats();
        quickStats.setTitle("3250");
        quickStats.setSubtitle("Total Expense");
        samples.add(quickStats);

        quickStats = new QuickStats();
        quickStats.setTitle("87.5%");
        quickStats.setSubtitle("Total Revenue");
        samples.add(quickStats);

        quickStats = new QuickStats();
        quickStats.setTitle("2550");
        quickStats.setSubtitle("New Users");
        samples.add(quickStats);

        return samples;
    }

    public static ObservableList<SaleReturn> saleReturnSampleData() {
        ObservableList<SaleReturn> saleReturnList = FXCollections.observableArrayList();
        String dateFormat = "yyyy-MM-dd";

        try {
            SaleReturn saleReturn = new SaleReturn();
            saleReturn.setSaleReturnDate(new SimpleDateFormat(dateFormat).parse("2023-04-12"));
            saleReturn.setSaleReturnReference("SR_1111");
            saleReturn.setSaleReturnCustomer("Thomas M. Martin");
            saleReturn.setSaleReturnBranch("Branch 1");
            saleReturn.setSaleReturnSaleRef("SL_1115");
            saleReturn.setSaleReturnStatus("Pending");
            saleReturn.setSaleReturnGrandTotal(1780.00);
            saleReturn.setSaleReturnAmountPaid(1780.00);
            saleReturn.setSaleReturnAmountDue(0.00);
            saleReturn.setSaleReturnPaymentStatus("Paid");
            saleReturnList.add(saleReturn);

            saleReturn = new SaleReturn();
            saleReturn.setSaleReturnDate(new SimpleDateFormat(dateFormat).parse("2023-04-13"));
            saleReturn.setSaleReturnReference("SR_1112");
            saleReturn.setSaleReturnCustomer("walk-in-customer");
            saleReturn.setSaleReturnBranch("Branch 2");
            saleReturn.setSaleReturnSaleRef("SL_1117");
            saleReturn.setSaleReturnStatus("Completed");
            saleReturn.setSaleReturnGrandTotal(1650.00);
            saleReturn.setSaleReturnAmountPaid(1000.00);
            saleReturn.setSaleReturnAmountDue(650.00);
            saleReturn.setSaleReturnPaymentStatus("Partial");
            saleReturnList.add(saleReturn);

            saleReturn = new SaleReturn();
            saleReturn.setSaleReturnDate(new SimpleDateFormat(dateFormat).parse("2023-04-14"));
            saleReturn.setSaleReturnReference("SR_1113");
            saleReturn.setSaleReturnCustomer("Fred C. Rasmussen");
            saleReturn.setSaleReturnBranch("Branch 2");
            saleReturn.setSaleReturnSaleRef("SL_1113");
            saleReturn.setSaleReturnStatus("Ordered");
            saleReturn.setSaleReturnGrandTotal(1874.80);
            saleReturn.setSaleReturnAmountPaid(1874.80);
            saleReturn.setSaleReturnAmountDue(0.00);
            saleReturn.setSaleReturnPaymentStatus("Unpaid");
            saleReturnList.add(saleReturn);

            saleReturn = new SaleReturn();
            saleReturn.setSaleReturnDate(new SimpleDateFormat(dateFormat).parse("2023-04-15"));
            saleReturn.setSaleReturnReference("SR_1114");
            saleReturn.setSaleReturnCustomer("Phyliss J. Polite");
            saleReturn.setSaleReturnBranch("Branch 2");
            saleReturn.setSaleReturnSaleRef("SL_1114");
            saleReturn.setSaleReturnStatus("Completed");
            saleReturn.setSaleReturnGrandTotal(1792.80);
            saleReturn.setSaleReturnAmountPaid(1792.80);
            saleReturn.setSaleReturnAmountDue(0.00);
            saleReturn.setSaleReturnPaymentStatus("Paid");
            saleReturnList.add(saleReturn);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return saleReturnList;
    }

    public static ObservableList<PurchaseReturn> purchaseReturnSampleData() {
        ObservableList<PurchaseReturn> purchaseReturnList = FXCollections.observableArrayList();
        String dateFormat = "yyyy-MM-dd";

        try {
            PurchaseReturn purchaseReturn = new PurchaseReturn();
            purchaseReturn.setPurchaseReturnDate(new SimpleDateFormat(dateFormat).parse("2023-04-10"));
            purchaseReturn.setPurchaseReturnReference("PR_1118");
            purchaseReturn.setPurchaseReturnSupplier("IT Supply");
            purchaseReturn.setPurchaseReturnBranch("Branch 1");
            purchaseReturn.setPurchaseReturnRef("PL_1118");
            purchaseReturn.setPurchaseReturnStatus("Received");
            purchaseReturn.setPurchaseReturnGrandTotal(1888.00);
            purchaseReturn.setPurchaseReturnAmountPaid(1888.00);
            purchaseReturn.setPurchaseReturnAmountDue(0.00);
            purchaseReturn.setPurchaseReturnPaymentStatus("Paid");
            purchaseReturnList.add(purchaseReturn);

            purchaseReturn = new PurchaseReturn();
            purchaseReturn.setPurchaseReturnDate(new SimpleDateFormat(dateFormat).parse("2023-04-11"));
            purchaseReturn.setPurchaseReturnReference("PR_1117");
            purchaseReturn.setPurchaseReturnSupplier("Corwin-Pfeffer");
            purchaseReturn.setPurchaseReturnBranch("Branch 1");
            purchaseReturn.setPurchaseReturnRef("PL_1117");
            purchaseReturn.setPurchaseReturnStatus("Pending");
            purchaseReturn.setPurchaseReturnGrandTotal(2243.00);
            purchaseReturn.setPurchaseReturnAmountPaid(0.00);
            purchaseReturn.setPurchaseReturnAmountDue(2243.00);
            purchaseReturn.setPurchaseReturnPaymentStatus("Unpaid");
            purchaseReturnList.add(purchaseReturn);

            purchaseReturn = new PurchaseReturn();
            purchaseReturn.setPurchaseReturnDate(new SimpleDateFormat(dateFormat).parse("2023-04-12"));
            purchaseReturn.setPurchaseReturnReference("PR_1116");
            purchaseReturn.setPurchaseReturnSupplier("IT Supply");
            purchaseReturn.setPurchaseReturnBranch("Branch 2");
            purchaseReturn.setPurchaseReturnRef("PL_1112");
            purchaseReturn.setPurchaseReturnStatus("Ordered");
            purchaseReturn.setPurchaseReturnGrandTotal(2304.00);
            purchaseReturn.setPurchaseReturnAmountPaid(0.00);
            purchaseReturn.setPurchaseReturnAmountDue(2304.00);
            purchaseReturn.setPurchaseReturnPaymentStatus("Unpaid");
            purchaseReturnList.add(purchaseReturn);

            purchaseReturn = new PurchaseReturn();
            purchaseReturn.setPurchaseReturnDate(new SimpleDateFormat(dateFormat).parse("2023-04-13"));
            purchaseReturn.setPurchaseReturnReference("PR_1115");
            purchaseReturn.setPurchaseReturnSupplier("IT Supply");
            purchaseReturn.setPurchaseReturnBranch("Branch 2");
            purchaseReturn.setPurchaseReturnRef("PL_1114");
            purchaseReturn.setPurchaseReturnStatus("Received");
            purchaseReturn.setPurchaseReturnGrandTotal(2960.00);
            purchaseReturn.setPurchaseReturnAmountPaid(2960.00);
            purchaseReturn.setPurchaseReturnAmountDue(0.00);
            purchaseReturn.setPurchaseReturnPaymentStatus("Paid");
            purchaseReturnList.add(purchaseReturn);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return purchaseReturnList;
    }

    public static ObservableList<User> userSampleData() {
        ObservableList<User> userList = FXCollections.observableArrayList();

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName("John");
        user.setUserEmail("johndoe@gmail.com");
        user.setUserPhoneNumber("0123456789");
        user.setUserStatus("Active");
        userList.add(user);

        user = new User();
        user.setFirstName("William");
        user.setLastName("Castillo");
        user.setUserName("William Castillo");
        user.setUserEmail("admin@example.com");
        user.setUserPhoneNumber("0123456789");
        user.setUserStatus("Active");
        userList.add(user);

        user = new User();
        user.setFirstName("Seller");
        user.setLastName("Seller");
        user.setUserName("Seller");
        user.setUserEmail("Seller@example.com");
        user.setUserPhoneNumber("0123456789");
        user.setUserStatus("Inactive");
        userList.add(user);

        return userList;
    }

    public static ObservableList<RoleMaster> roleMasterSampleData() {
        ObservableList<RoleMaster> roleMasterList = FXCollections.observableArrayList();

        RoleMaster roleMaster = new RoleMaster();
        roleMaster.setRole("Owner");
        roleMaster.setDescription("Administrator role");
        roleMasterList.add(roleMaster);

        return roleMasterList;
    }

    public static ObservableList<Currency> currencySampleData() {
        ObservableList<Currency> currencyList = FXCollections.observableArrayList();

        Currency currency = new Currency();
        currency.setCurrencyName("US Dollar");
        currency.setCurrencySymbol("$");
        currency.setCurrencyCode("USD");
        currencyList.add(currency);

        return currencyList;
    }
}
