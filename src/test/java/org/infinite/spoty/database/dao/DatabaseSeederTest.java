package org.infinite.spoty.database.dao;

import org.infinite.spoty.database.models.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DatabaseSeederTest {
    @Test
    @Order(1)
    void productCategoryInsert() {
        try {
            var productCategory = new ProductCategory("PC-001", "Test Product Category 1");
            productCategory.setCreatedAt(new Date());
            productCategory.setCreatedBy("Tester One");
            ProductCategoryDao.saveProductCategory(productCategory);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    @Order(2)
    void brandInsert() {
        try {
            var brand = new Brand("Test Brand 1", "This is a test brand.");
            brand.setCreatedAt(new Date());
            brand.setCreatedBy("Tester One");
            BrandDao.saveBrand(brand);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    @Order(3)
    void uomInsert() {
        try {
            var uom = new UnitOfMeasure("Test UOM 1", "TUOM1", null, "", 0);
            uom.setCreatedAt(new Date());
            uom.setCreatedBy("Tester One");
            UnitOfMeasureDao.saveUnitOfMeasure(uom);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    @Order(4)
    void expenseInsert() {
        try {
            var expenseCategory = new ExpenseCategory("Test Category 1", "This is the first test category");
            expenseCategory.setCreatedAt(new Date());
            expenseCategory.setCreatedBy("Tester One");
            ExpenseCategoryDao.saveExpenseCategory(expenseCategory);
            var expense = new Expense(new Date(), expenseCategory, 1560.95);
            expense.setCreatedAt(new Date());
            expense.setCreatedBy("Tester One");
            ExpenseDao.saveExpense(expense);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    @Order(5)
    void customerInsert() {
        try {
            var customer = new Customer("Test Customer 1", "test1@email.com", "+1234567890",
                    "Test City 1", "Test Address 1", "TXN-5685655555555",
                    "Test Country 1");
            customer.setCreatedAt(new Date());
            customer.setCreatedBy("Tester One");
            CustomerDao.saveCustomer(customer);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    @Order(6)
    void supplierInsert() {
        try {
            var supplier = new Supplier("Test Supplier 1", "test1@email.com", "+123456789012",
                    "TXN-5685655555555", "Test Address 1", "Test City 1",
                    "Test Country 1");
            supplier.setCreatedAt(new Date());
            supplier.setCreatedBy("Tester One");
            SupplierDao.saveSupplier(supplier);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    @Order(7)
    void adjustmentInsert() {
        try {
            var adjustmentDetail = new AdjustmentDetail(null, 12, "Increment");
            var adjustmentMaster = new AdjustmentMaster(null, "Test Adjustment Master", new Date());
            List<AdjustmentDetail> list = new LinkedList<>();

            adjustmentDetail.setCreatedAt(new Date());
            adjustmentDetail.setCreatedBy("Tester One");
            adjustmentDetail.setAdjustment(adjustmentMaster);

            list.add(adjustmentDetail);

            adjustmentMaster.setAdjustmentDetails(list);
            adjustmentMaster.setCreatedAt(new Date());
            adjustmentMaster.setCreatedBy("Tester One");

            AdjustmentMasterDao.saveAdjustmentMaster(adjustmentMaster);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    @Order(8)
    void quotationInsert() {
        try {
            var quotationDetail = new QuotationDetail(null, 6.54, 25, 29898);
            var quotationMaster = new QuotationMaster(new Date(), null, null, "Pending", "No customer yet.");
            List<QuotationDetail> list = new LinkedList<>();

            quotationDetail.setCreatedAt(new Date());
            quotationDetail.setCreatedBy("Tester One");
            quotationDetail.setQuotation(quotationMaster);

            list.add(quotationDetail);

            quotationMaster.setQuotationDetails(list);
            quotationMaster.setCreatedAt(new Date());
            quotationMaster.setCreatedBy("Tester One");

            QuotationMasterDao.saveQuotationMaster(quotationMaster);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    @Order(9)
    void requisitionInsert() {
        try {
            var requisitionMaster = new RequisitionMaster(new Date(), null, null, "TTF",
                    "Air", "Secure", new Date(), "Confirmed", "Approved", 15647765.0843);
            var requisitionDetail = new RequisitionDetail(null,
                    requisitionMaster,
                    13234,
                    "This is a Test Product 1");
            List<RequisitionDetail> list = new LinkedList<>();

            requisitionDetail.setCreatedAt(new Date());
            requisitionDetail.setCreatedBy("Tester One");
            requisitionDetail.setRequisition(requisitionMaster);

            list.add(requisitionDetail);

            requisitionMaster.setRequisitionDetails(list);
            requisitionMaster.setCreatedAt(new Date());
            requisitionMaster.setCreatedBy("Tester One");

            RequisitionMasterDao.saveRequisitionMaster(requisitionMaster);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    @Order(10)
    void branchInsert() {
        try {
            var branch = new Branch("Test Branch One",
                    "Test City 1", "123456789012",
                    "test1.branch@email.com", "Test Town 1", "5675676");
            branch.setCreatedAt(new Date());
            branch.setCreatedBy("Tester One");
            BranchDao.saveBranch(branch);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }
}
