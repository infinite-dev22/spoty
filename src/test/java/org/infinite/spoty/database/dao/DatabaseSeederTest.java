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

    @Test
    @Order(11)
    void purchaseInsert() {
        try {
            var purchaseMaster = new PurchaseMaster(null, null, "Confirmed",
                    "Approved", new Date());

            var purchaseDetail = new PurchaseDetail(purchaseMaster,
                    13234,
                    554,
                    "VAT",
                    20,
                    "Customer",
                    null,
                    "",
                    13788,
                    1);
            List<PurchaseDetail> list = new LinkedList<>();

            purchaseDetail.setCreatedAt(new Date());
            purchaseDetail.setCreatedBy("Tester One");
            purchaseDetail.setPurchase(purchaseMaster);

            list.add(purchaseDetail);

            purchaseMaster.setPurchaseDetails(list);
            purchaseMaster.setCreatedAt(new Date());
            purchaseMaster.setCreatedBy("Tester One");

            PurchaseMasterDao.savePurchaseMaster(purchaseMaster);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    @Order(12)
    void transferInsert() {
        try {
            var transferMaster = new TransferMaster(new Date(),
                    null,
                    null,
                    234889,
                    "Pending",
                    "Not Approved Yet.");

            var transferDetail = new TransferDetail(null,
                    1,
                    "",
                    "Test Description",
                    25645,
                    23645);
            List<TransferDetail> list = new LinkedList<>();

            transferDetail.setCreatedAt(new Date());
            transferDetail.setCreatedBy("Tester One");
            transferDetail.setTransfer(transferMaster);

            list.add(transferDetail);

            transferMaster.setTransferDetails(list);
            transferMaster.setCreatedAt(new Date());
            transferMaster.setCreatedBy("Tester One");

            TransferMasterDao.saveTransferMaster(transferMaster);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    @Order(13)
    void stockInInsert() {
        try {
            var stockInMaster = new StockInMaster(new Date(),
                    null,
                    "Pending",
                    "Not Approved Yet.");

            var stockInDetail = new StockInDetail(null,
                    1,
                    "",
                    "Test Description",
                    "Shelf A");
            List<StockInDetail> list = new LinkedList<>();

            stockInDetail.setCreatedAt(new Date());
            stockInDetail.setCreatedBy("Tester One");
            stockInDetail.setStockIn(stockInMaster);

            list.add(stockInDetail);

            stockInMaster.setStockInDetails(list);
            stockInMaster.setCreatedAt(new Date());
            stockInMaster.setCreatedBy("Tester One");

            StockInMasterDao.saveStockInMaster(stockInMaster);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    @Order(14)
    void saleInsert() {
        try {
            var saleMaster = new SaleMaster(null,
                    null,
                    "Pending",
                    "Testing.",
                    new Date());

            var saleDetail = new SaleDetail(null,
                    1,
                    "",
                    2.09,
                    "VAT",
                    20,
                    "Test.");
            List<SaleDetail> list = new LinkedList<>();

            saleDetail.setCreatedAt(new Date());
            saleDetail.setCreatedBy("Tester One");
            saleDetail.setSaleMaster(saleMaster);

            list.add(saleDetail);

            saleMaster.setSaleDetails(list);
            saleMaster.setCreatedAt(new Date());
            saleMaster.setCreatedBy("Tester One");

            SaleMasterDao.saveSaleMaster(saleMaster);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    @Order(15)
    void productInsert() {
        try {
            var productMaster = new ProductMaster("Type 01",
                    "Product Master",
                    20,
                    null,
                    null,
                    "Testing.",
                    false,
                    true);

            var productDetail = new ProductDetail(null,
                    null,
                    null,
                    "Product Detail",
                    2,
                    20.09,
                    50,
                    0.09,
                    "VAT",
                    20,
                    "Test2342");
            List<ProductDetail> list = new LinkedList<>();

            productDetail.setCreatedAt(new Date());
            productDetail.setCreatedBy("Tester One");
            productDetail.setProduct(productMaster);

            list.add(productDetail);

            productMaster.setProductDetails(list);
            productMaster.setCreatedAt(new Date());
            productMaster.setCreatedBy("Tester One");

            ProductMasterDao.saveProductMaster(productMaster);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    @Order(16)
    void userInsert() {
        try {
            var user = new User("Test",
                    "One",
                    "User_0ne",
                    null,
            true,
            true);
            user.setCreatedAt(new Date());
            user.setCreatedBy("Tester One");
            UserDao.saveUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }
}
