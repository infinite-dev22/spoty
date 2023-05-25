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

    public static ObservableList<Product> productSampleData() {
        ObservableList<Product> productList = FXCollections.observableArrayList();

        Product product = new Product();
        product.setProductName("Pineapple");
        product.setProductCode(71934224);
        product.setProductCategory("Fruits");
        product.setProductBrand("N/D");
        productList.add(product);

        product = new Product();
        product.setProductName("Orange");
        product.setProductCode(80256894);
        product.setProductCategory("Fruits");
        product.setProductBrand("N/D");
        productList.add(product);

        product = new Product();
        product.setProductName("Strawberry");
        product.setProductCode(53755440);
        product.setProductCategory("Fruits");
        product.setProductBrand("N/D");
        productList.add(product);

        return productList;
    }

    public static ObservableList<Adjustment> adjustmentSampleData() {
        ObservableList<Adjustment> adjustmentList = FXCollections.observableArrayList();
        String dateFormat = "yyyy-MM-dd";

        try {
            Adjustment adjustment = new Adjustment();
            adjustment.setAdjustmentDate(new SimpleDateFormat(dateFormat).parse("2023-04-14"));
            adjustment.setAdjustmentReference("AD_1114");
            adjustment.setAdjustmentBranch("Branch 2");
            adjustment.setAdjustmentTotalProducts(6.00);
            adjustmentList.add(adjustment);

            adjustment = new Adjustment();
            adjustment.setAdjustmentDate(new SimpleDateFormat(dateFormat).parse("2023-04-15"));
            adjustment.setAdjustmentReference("AD_1113");
            adjustment.setAdjustmentBranch("Branch 1");
            adjustment.setAdjustmentTotalProducts(1.00);
            adjustmentList.add(adjustment);

            adjustment = new Adjustment();
            adjustment.setAdjustmentDate(new SimpleDateFormat(dateFormat).parse("2023-04-16"));
            adjustment.setAdjustmentReference("AD_1112");
            adjustment.setAdjustmentBranch("Branch 1");
            adjustment.setAdjustmentTotalProducts(6.00);
            adjustmentList.add(adjustment);

            adjustment = new Adjustment();
            adjustment.setAdjustmentDate(new SimpleDateFormat(dateFormat).parse("2023-04-17"));
            adjustment.setAdjustmentReference("AD_1111");
            adjustment.setAdjustmentBranch("Branch 1");
            adjustment.setAdjustmentTotalProducts(1.00);
            adjustmentList.add(adjustment);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return adjustmentList;
    }

    public static ObservableList<Quotation> quotationSampleData() {
        ObservableList<Quotation> quotationList = FXCollections.observableArrayList();
        String dateFormat = "yyyy-MM-dd";

        try {
            Quotation quotation = new Quotation();
            quotation.setQuotationDate(new SimpleDateFormat(dateFormat).parse("2023-04-13"));
            quotation.setQuotationReference("QT_1115");
            quotation.setQuotationCustomer("Fred C. Rasmussen");
            quotation.setQuotationBranch("Branch 1");
            quotation.setQuotationStatus("Sent");
            quotation.setQuotationGrandTotal(322.00);
            quotationList.add(quotation);

            quotation = new Quotation();
            quotation.setQuotationDate(new SimpleDateFormat(dateFormat).parse("2023-04-14"));
            quotation.setQuotationReference("QT_1114");
            quotation.setQuotationCustomer("Phyliss J. Polite");
            quotation.setQuotationBranch("Branch 1");
            quotation.setQuotationStatus("Pending");
            quotation.setQuotationGrandTotal(680.00);
            quotationList.add(quotation);

            quotation = new Quotation();
            quotation.setQuotationDate(new SimpleDateFormat(dateFormat).parse("2023-04-15"));
            quotation.setQuotationReference("QT_1113");
            quotation.setQuotationCustomer("Thomas M. Martin");
            quotation.setQuotationBranch("Branch 1");
            quotation.setQuotationStatus("Sent");
            quotation.setQuotationGrandTotal(1500.00);
            quotationList.add(quotation);

            quotation = new Quotation();
            quotation.setQuotationDate(new SimpleDateFormat(dateFormat).parse("2023-04-16"));
            quotation.setQuotationReference("QT_1112");
            quotation.setQuotationCustomer("Beverly B. Huber");
            quotation.setQuotationBranch("Branch 2");
            quotation.setQuotationStatus("Pending");
            quotation.setQuotationGrandTotal(34.00);
            quotationList.add(quotation);

            quotation = new Quotation();
            quotation.setQuotationDate(new SimpleDateFormat(dateFormat).parse("2023-04-17"));
            quotation.setQuotationReference("QT_1111");
            quotation.setQuotationCustomer("walk-in-customer");
            quotation.setQuotationBranch("Branch 1");
            quotation.setQuotationStatus("Sent");
            quotation.setQuotationGrandTotal(1000.00);
            quotationList.add(quotation);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return quotationList;
    }

    public static ObservableList<Sale> saleSampleData() {
        ObservableList<Sale> saleList = FXCollections.observableArrayList();
        String dateFormat = "yyyy-MM-dd";

        try {
            Sale sale = new Sale();
            sale.setSaleDate(new SimpleDateFormat(dateFormat).parse("2023-04-12"));
            sale.setSaleReference("SL_1117");
            sale.setSaleAddedBy("William Castillo");
            sale.setSaleCustomer("Thomas M. Martin");
            sale.setSaleBranch("Branch 1");
            sale.setSaleStatus("Pending");
            sale.setSaleGrandTotal(1780.00);
            sale.setSaleAmountPaid(1780.00);
            sale.setSaleAmountDue(0.00);
            sale.setSalePaymentStatus("Paid");
            saleList.add(sale);

            sale = new Sale();
            sale.setSaleDate(new SimpleDateFormat(dateFormat).parse("2023-04-13"));
            sale.setSaleReference("SL_1116");
            sale.setSaleAddedBy("William Castillo");
            sale.setSaleCustomer("walk-in-customer");
            sale.setSaleBranch("Branch 2");
            sale.setSaleStatus("Completed");
            sale.setSaleGrandTotal(1650.00);
            sale.setSaleAmountPaid(1000.00);
            sale.setSaleAmountDue(650.00);
            sale.setSalePaymentStatus("Partial");
            saleList.add(sale);

            sale = new Sale();
            sale.setSaleDate(new SimpleDateFormat(dateFormat).parse("2023-04-14"));
            sale.setSaleReference("SL_1115");
            sale.setSaleAddedBy("William Castillo");
            sale.setSaleCustomer("Fred C. Rasmussen");
            sale.setSaleBranch("Branch 2");
            sale.setSaleStatus("Ordered");
            sale.setSaleGrandTotal(1874.80);
            sale.setSaleAmountPaid(1874.80);
            sale.setSaleAmountDue(0.00);
            sale.setSalePaymentStatus("Unpaid");
            saleList.add(sale);

            sale = new Sale();
            sale.setSaleDate(new SimpleDateFormat(dateFormat).parse("2023-04-15"));
            sale.setSaleReference("SL_1114");
            sale.setSaleAddedBy("William Castillo");
            sale.setSaleCustomer("Phyliss J. Polite");
            sale.setSaleBranch("Branch 2");
            sale.setSaleStatus("Completed");
            sale.setSaleGrandTotal(1792.80);
            sale.setSaleAmountPaid(1792.80);
            sale.setSaleAmountDue(0.00);
            sale.setSalePaymentStatus("Paid");
            saleList.add(sale);

            sale = new Sale();
            sale.setSaleDate(new SimpleDateFormat(dateFormat).parse("2023-04-16"));
            sale.setSaleReference("SL_1113");
            sale.setSaleAddedBy("William Castillo");
            sale.setSaleCustomer("Thomas M. Martin");
            sale.setSaleBranch("Branch 1");
            sale.setSaleStatus("Pending");
            sale.setSaleGrandTotal(2648.00);
            sale.setSaleAmountPaid(2648.00);
            sale.setSaleAmountDue(0.00);
            sale.setSalePaymentStatus("Unpaid");
            saleList.add(sale);

            sale = new Sale();
            sale.setSaleDate(new SimpleDateFormat(dateFormat).parse("2023-04-17"));
            sale.setSaleReference("SL_1112");
            sale.setSaleAddedBy("William Castillo");
            sale.setSaleCustomer("Beverly B. Huber");
            sale.setSaleBranch("Branch 2");
            sale.setSaleStatus("Completed");
            sale.setSaleGrandTotal(2520.00);
            sale.setSaleAmountPaid(2000.00);
            sale.setSaleAmountDue(520.00);
            sale.setSalePaymentStatus("Partial");
            saleList.add(sale);

            sale = new Sale();
            sale.setSaleDate(new SimpleDateFormat(dateFormat).parse("2023-04-18"));
            sale.setSaleReference("SL_1111");
            sale.setSaleAddedBy("William Castillo");
            sale.setSaleCustomer("walk-in-customer");
            sale.setSaleBranch("Branch 1");
            sale.setSaleStatus("Completed");
            sale.setSaleGrandTotal(1780.00);
            sale.setSaleAmountPaid(1780.00);
            sale.setSaleAmountDue(0.00);
            sale.setSalePaymentStatus("Paid");
            saleList.add(sale);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return saleList;
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

//    public static ObservableList<Branch> branchSampleData() {
//        ObservableList<Branch> branchList = FXCollections.observableArrayList();
//
//        Branch branch = new Branch();
//        branch.setBranchName("Branch 1");
//        branch.setBranchPhoneNumber("0987886768");
//        branch.setBranchCity("Kampala City");
//        branch.setBranchTown("Kampala");
//        branch.setBranchLocation("Mabirizi Complex");
//        branch.setBranchEmail("branchone@email.com");
//        branchList.add(branch);
//
//        return branchList;
//    }

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
