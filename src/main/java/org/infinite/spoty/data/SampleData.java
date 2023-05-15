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

    public static ObservableList<Category> categorySampleData() {
        ObservableList<Category> categoryList = FXCollections.observableArrayList();

        Category category = new Category();
        category.setCategoryCode("CA6");
        category.setCategoryName("Fruits");
        categoryList.add(category);

        category = new Category();
        category.setCategoryCode("CA5");
        category.setCategoryName("Shoes");
        categoryList.add(category);

        category = new Category();
        category.setCategoryCode("CA4");
        category.setCategoryName("T-Shirts");
        categoryList.add(category);

        category = new Category();
        category.setCategoryCode("CA3");
        category.setCategoryName("Jackets");
        categoryList.add(category);

        category = new Category();
        category.setCategoryCode("CA2");
        category.setCategoryName("Computers");
        categoryList.add(category);

        category = new Category();
        category.setCategoryCode("CA1");
        category.setCategoryName("Accessories");
        categoryList.add(category);

        return categoryList;
    }

    public static ObservableList<Brand> brandSampleData() {
        ObservableList<Brand> brandList = FXCollections.observableArrayList();

        Brand brand = new Brand();
        brand.setBrandName("Nike");
        brand.setBrandDescription("Fashion");
        brandList.add(brand);

        brand = new Brand();
        brand.setBrandName("Jordan");
        brand.setBrandDescription("Fashion");
        brandList.add(brand);

        brand = new Brand();
        brand.setBrandName("Prada");
        brand.setBrandDescription("Fashion");
        brandList.add(brand);

        brand = new Brand();
        brand.setBrandName("Adidas");
        brand.setBrandDescription("Fashion");
        brandList.add(brand);

        brand = new Brand();
        brand.setBrandName("SamSung");
        brand.setBrandDescription("Electronics");
        brandList.add(brand);

        brand = new Brand();
        brand.setBrandName("Apple");
        brand.setBrandDescription("Electronics");
        brandList.add(brand);

        brand = new Brand();
        brand.setBrandName("Hisense");
        brand.setBrandDescription("Electronics");
        brandList.add(brand);

        brand = new Brand();
        brand.setBrandName("Nokia");
        brand.setBrandDescription("Smartphones");
        brandList.add(brand);

        brand = new Brand();
        brand.setBrandName("Huawei");
        brand.setBrandDescription("Smartphones");
        brandList.add(brand);

        brand = new Brand();
        brand.setBrandName("Colgate");
        brand.setBrandDescription("Oral Hygiene");
        brandList.add(brand);

        return brandList;
    }

    public static ObservableList<UnitOfMeasure> uomSampleData() {
        ObservableList<UnitOfMeasure> uomList = FXCollections.observableArrayList();

        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setUomName("Kilograms");
        unitOfMeasure.setUomShortName("kg");
        unitOfMeasure.setUomOperationValue(1.0);
        uomList.add(unitOfMeasure);

        unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setUomName("grams");
        unitOfMeasure.setUomShortName("g");
        unitOfMeasure.setUomBaseUnit("Kilograms");
        unitOfMeasure.setUomOperator("/");
        unitOfMeasure.setUomOperationValue(1000.0);
        uomList.add(unitOfMeasure);

        unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setUomName("Litres");
        unitOfMeasure.setUomShortName("ltr");
        unitOfMeasure.setUomOperationValue(1.0);
        uomList.add(unitOfMeasure);

        return uomList;
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

    public static ObservableList<Purchase> purchaseSampleData() {
        ObservableList<Purchase> purchaseList = FXCollections.observableArrayList();
        String dateFormat = "yyyy-MM-dd";

        try {
            Purchase purchase = new Purchase();
            purchase.setPurchaseDate(new SimpleDateFormat(dateFormat).parse("2023-04-10"));
            purchase.setPurchaseReference("PR_1118");
            purchase.setPurchaseSupplier("IT Supply");
            purchase.setPurchaseBranch("Branch 1");
            purchase.setPurchaseStatus("Received");
            purchase.setPurchaseGrandTotal(1888.00);
            purchase.setPurchaseAmountPaid(1888.00);
            purchase.setPurchaseAmountDue(0.00);
            purchase.setPurchasePaymentStatus("Paid");
            purchaseList.add(purchase);

            purchase = new Purchase();
            purchase.setPurchaseDate(new SimpleDateFormat(dateFormat).parse("2023-04-11"));
            purchase.setPurchaseReference("PR_1117");
            purchase.setPurchaseSupplier("Corwin-Pfeffer");
            purchase.setPurchaseBranch("Branch 1");
            purchase.setPurchaseStatus("Pending");
            purchase.setPurchaseGrandTotal(2243.00);
            purchase.setPurchaseAmountPaid(0.00);
            purchase.setPurchaseAmountDue(2243.00);
            purchase.setPurchasePaymentStatus("Unpaid");
            purchaseList.add(purchase);

            purchase = new Purchase();
            purchase.setPurchaseDate(new SimpleDateFormat(dateFormat).parse("2023-04-12"));
            purchase.setPurchaseReference("PR_1116");
            purchase.setPurchaseSupplier("IT Supply");
            purchase.setPurchaseBranch("Branch 2");
            purchase.setPurchaseStatus("Ordered");
            purchase.setPurchaseGrandTotal(2304.00);
            purchase.setPurchaseAmountPaid(0.00);
            purchase.setPurchaseAmountDue(2304.00);
            purchase.setPurchasePaymentStatus("Unpaid");
            purchaseList.add(purchase);

            purchase = new Purchase();
            purchase.setPurchaseDate(new SimpleDateFormat(dateFormat).parse("2023-04-13"));
            purchase.setPurchaseReference("PR_1115");
            purchase.setPurchaseSupplier("IT Supply");
            purchase.setPurchaseBranch("Branch 2");
            purchase.setPurchaseStatus("Received");
            purchase.setPurchaseGrandTotal(2960.00);
            purchase.setPurchaseAmountPaid(2960.00);
            purchase.setPurchaseAmountDue(0.00);
            purchase.setPurchasePaymentStatus("Paid");
            purchaseList.add(purchase);

            purchase = new Purchase();
            purchase.setPurchaseDate(new SimpleDateFormat(dateFormat).parse("2023-04-14"));
            purchase.setPurchaseReference("PR_1114");
            purchase.setPurchaseSupplier("Fruits Supply");
            purchase.setPurchaseBranch("Branch 1");
            purchase.setPurchaseStatus("Received");
            purchase.setPurchaseGrandTotal(2716.00);
            purchase.setPurchaseAmountPaid(2716.00);
            purchase.setPurchaseAmountDue(0.00);
            purchase.setPurchasePaymentStatus("Paid");
            purchaseList.add(purchase);

            purchase = new Purchase();
            purchase.setPurchaseDate(new SimpleDateFormat(dateFormat).parse("2023-04-15"));
            purchase.setPurchaseReference("PR_1113");
            purchase.setPurchaseSupplier("Fruits Supply");
            purchase.setPurchaseBranch("Branch 1");
            purchase.setPurchaseStatus("Pending");
            purchase.setPurchaseGrandTotal(2640.00);
            purchase.setPurchaseAmountPaid(0.00);
            purchase.setPurchaseAmountDue(2640.00);
            purchase.setPurchasePaymentStatus("Unpaid");
            purchaseList.add(purchase);

            purchase = new Purchase();
            purchase.setPurchaseDate(new SimpleDateFormat(dateFormat).parse("2023-04-16"));
            purchase.setPurchaseReference("PR_1112");
            purchase.setPurchaseSupplier("Schulist-Hickle");
            purchase.setPurchaseBranch("Branch 2");
            purchase.setPurchaseStatus("Received");
            purchase.setPurchaseGrandTotal(2270.00);
            purchase.setPurchaseAmountPaid(2270.00);
            purchase.setPurchaseAmountDue(0.00);
            purchase.setPurchasePaymentStatus("Paid");
            purchaseList.add(purchase);

            purchase = new Purchase();
            purchase.setPurchaseDate(new SimpleDateFormat(dateFormat).parse("2023-04-17"));
            purchase.setPurchaseReference("PR_1111");
            purchase.setPurchaseSupplier("Corwin-Pfeffer");
            purchase.setPurchaseBranch("Branch 1");
            purchase.setPurchaseStatus("Received");
            purchase.setPurchaseGrandTotal(1160.00);
            purchase.setPurchaseAmountPaid(1000.00);
            purchase.setPurchaseAmountDue(160.00);
            purchase.setPurchasePaymentStatus("Partial");
            purchaseList.add(purchase);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return purchaseList;
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

    public static ObservableList<Expense> expenseSampleData() {
        ObservableList<Expense> expenseList = FXCollections.observableArrayList();
        String dateFormat = "yyyy-MM-dd";

        try {
            Expense expense = new Expense();
            expense.setExpenseDate(new SimpleDateFormat(dateFormat).parse("2023-04-15"));
            expense.setExpenseReference("EXP_1113");
            expense.setExpenseName("Petrol");
            expense.setExpenseAmount(50.00);
            expense.setExpenseCategory("Petrol");
            expense.setExpenseBranch("Branch 1");
            expenseList.add(expense);

            expense = new Expense();
            expense.setExpenseDate(new SimpleDateFormat(dateFormat).parse("2023-04-15"));
            expense.setExpenseReference("EXP_1112");
            expense.setExpenseName("Office Supplies");
            expense.setExpenseAmount(200.00);
            expense.setExpenseCategory("Office Expenses & Postage");
            expense.setExpenseBranch("Branch 1");
            expenseList.add(expense);

            expense = new Expense();
            expense.setExpenseDate(new SimpleDateFormat(dateFormat).parse("2023-04-15"));
            expense.setExpenseReference("EXP_1111");
            expense.setExpenseName("Petrol for vehicle");
            expense.setExpenseAmount(100.00);
            expense.setExpenseCategory("Petrol");
            expense.setExpenseBranch("Branch 1");
            expenseList.add(expense);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return expenseList;
    }

    public static ObservableList<ExpenseCategory> expenseCategorySampleData() {
        ObservableList<ExpenseCategory> expenseCategoryList = FXCollections.observableArrayList();

        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setCategoryName("Other Expenses");
        expenseCategory.setCategoryDescription("Other expenses");
        expenseCategoryList.add(expenseCategory);

        expenseCategory = new ExpenseCategory();
        expenseCategory.setCategoryName("Petrol");
        expenseCategory.setCategoryDescription("Petrol");
        expenseCategoryList.add(expenseCategory);

        expenseCategory = new ExpenseCategory();
        expenseCategory.setCategoryName("Meals & Entertainment");
        expenseCategory.setCategoryDescription("Meals & Entertainment");
        expenseCategoryList.add(expenseCategory);

        expenseCategory = new ExpenseCategory();
        expenseCategory.setCategoryName("Office Expenses & Postage");
        expenseCategory.setCategoryDescription("Office Expenses & Postage");
        expenseCategoryList.add(expenseCategory);

        expenseCategory = new ExpenseCategory();
        expenseCategory.setCategoryName("Employee Benefits");
        expenseCategory.setCategoryDescription("Employee Benefits");
        expenseCategoryList.add(expenseCategory);

        return expenseCategoryList;
    }

    public static ObservableList<Customer> customerSampleData() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        Customer customer = new Customer();
        customer.setCustomerCode(104);
        customer.setCustomerName("Fred C. Rasmussen");
        customer.setCustomerPhoneNumber("040 33 61 47");
        customer.setCustomerEmail("Fred@example.com");
        customer.setCustomerTotalSaleDue(1874.80);
        customerList.add(customer);

        customer = new Customer();
        customer.setCustomerCode(103);
        customer.setCustomerName("Phyliss J. Polite");
        customer.setCustomerPhoneNumber("0454 12 34 45");
        customer.setCustomerEmail("Phyliss@example.com");
        customerList.add(customer);

        customer = new Customer();
        customer.setCustomerCode(102);
        customer.setCustomerName("Thomas M. Martin");
        customer.setCustomerPhoneNumber("01.12.34.45.55");
        customer.setCustomerEmail("Thomas@example.com");
        customer.setCustomerTotalSaleDue(2648.00);
        customerList.add(customer);

        customer = new Customer();
        customer.setCustomerCode(101);
        customer.setCustomerName("Beverly B. Huber");
        customer.setCustomerPhoneNumber("123-345-432");
        customer.setCustomerEmail("Beverly@example.com");
        customer.setCustomerTotalSaleDue(520.00);
        customerList.add(customer);

        customer = new Customer();
        customer.setCustomerCode(105);
        customer.setCustomerName("walk-in-customer");
        customer.setCustomerPhoneNumber("123456780");
        customer.setCustomerEmail("walk-in-customer@example.com");
        customer.setCustomerTotalSaleDue(650.00);
        customerList.add(customer);

        return customerList;
    }

    public static ObservableList<Supplier> supplierSampleData() {
        ObservableList<Supplier> supplierList = FXCollections.observableArrayList();

        Supplier supplier = new Supplier();
        supplier.setSupplierCode(104);
        supplier.setSupplierName("IT Supply");
        supplier.setSupplierPhoneNumber("360-723-2330");
        supplier.setSupplierEmail("Fred@example.com");
        supplier.setSupplierTotalPurchaseDue(1874.80);
        supplierList.add(supplier);

        supplier = new Supplier();
        supplier.setSupplierCode(103);
        supplier.setSupplierName("Phyliss J. Polite");
        supplier.setSupplierPhoneNumber("0454 12 34 45");
        supplier.setSupplierEmail("Phyliss@example.com");
        supplierList.add(supplier);

        supplier = new Supplier();
        supplier.setSupplierCode(102);
        supplier.setSupplierName("Thomas M. Martin");
        supplier.setSupplierPhoneNumber("01.12.34.45.55");
        supplier.setSupplierEmail("Thomas@example.com");
        supplier.setSupplierTotalPurchaseDue(2648.00);
        supplierList.add(supplier);

        supplier = new Supplier();
        supplier.setSupplierCode(101);
        supplier.setSupplierName("Beverly B. Huber");
        supplier.setSupplierPhoneNumber("123-345-432");
        supplier.setSupplierEmail("Beverly@example.com");
        supplier.setSupplierTotalPurchaseDue(520.00);
        supplierList.add(supplier);

        supplier = new Supplier();
        supplier.setSupplierCode(105);
        supplier.setSupplierName("walk-in-supplier");
        supplier.setSupplierPhoneNumber("123456780");
        supplier.setSupplierEmail("walk-in-supplier@example.com");
        supplier.setSupplierTotalPurchaseDue(650.00);
        supplierList.add(supplier);

        return supplierList;
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
