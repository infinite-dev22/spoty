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

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import java.util.Date;
import org.infinite.spoty.database.connection.SQLiteConnection;
import org.infinite.spoty.database.models.*;
import org.infinite.spoty.viewModels.BrandViewModel;
import org.infinite.spoty.viewModels.ProductCategoryViewModel;
import org.infinite.spoty.viewModels.UOMViewModel;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DatabaseSeederTest {
    ProductCategory productCategory;
    Brand brand;
    UnitOfMeasure uom;
    ExpenseCategory expenseCategory;
    Customer customer;
    Supplier supplier;
    Branch branch;
    ProductDetail productDetail;
    SQLiteConnection connection = SQLiteConnection.getInstance();
    ConnectionSource connectionSource = connection.getConnection();

    @Test
    @Order(1)
    void branchInsert() {
        try {
            Dao<Branch, Long> productMasterDao = DaoManager.createDao(connectionSource, Branch.class);

            branch = new Branch("Default Branch",
                    "St Helena", "123456789012",
                    "defaultbranch@email.com", "Burgaw", "5675676");
            branch.setCreatedAt(new Date());
            branch.setCreatedBy("User One");
            productMasterDao.create(branch);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }
    @Test
    @Order(2)
    void productCategoryInsert() {
        try {
            ProductCategoryViewModel.setCode("PC-001");
            ProductCategoryViewModel.setName("SoftDrinks");

            ProductCategoryViewModel.saveProductCategory();
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    @Order(3)
    void brandInsert() {
        try {
            BrandViewModel.setName("CocaCola");
            BrandViewModel.setDescription("A SoftDrink & Beverage global franchise");
            BrandViewModel.saveBrand();
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    @Order(4)
    void uomInsert() {
        try {
            uom = new UnitOfMeasure("Litre", "ltr", null, "", 0);

            UOMViewModel.setName("Litre");
            UOMViewModel.setShortName("ltr");

            UOMViewModel.saveUOM();
        } catch (Exception e) {
            throw new AssertionError();
        }
    }
//
//    @Test
//    @Order(5)
//    void productInsert() {
//        try {
//            ProductCategoryViewModel.getItem(1);
//                    BrandViewModel.getItem(1);
//            var productMaster = new ProductMaster("Type 01",
//                    "Coke",
//                    4488000,
//                    ProductCategoryViewModel.categoriesList.get(1),
//                    BrandViewModel.brandsList.get(1),
//                    "Approved.",
//                    false,
//                    true);
//
//
//
//            productDetail = new ProductDetail(UnitOfMeasureDao.findUnitOfMeasure(1),
//                    "Zero Sugar",
//                    2244,
//                    4488000,
//                    2000,
//                    4.29,
//                    "VAT",
//                    70,
//                    "6724856672");
//            List<ProductDetail> list = new LinkedList<>();
//
//            productDetail.setCreatedAt(new Date());
//            productDetail.setCreatedBy("User One");
//            productDetail.setProduct(productMaster);
//
//            list.add(productDetail);
//
//            productMaster.setProductDetails(list);
//            productMaster.setCreatedAt(new Date());
//            productMaster.setCreatedBy("User One");
//
//            ProductMasterDao.saveProductMaster(productMaster);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new AssertionError();
//        }
//    }
//
//    @Test
//    @Order(6)
//    void expenseInsert() {
//        try {
//            expenseCategory = new ExpenseCategory("Transport", "This is spent on transportation of employees monthly.");
//            expenseCategory.setCreatedAt(new Date());
//            expenseCategory.setCreatedBy("User One");
//            ExpenseCategoryDao.saveExpenseCategory(expenseCategory);
//            var expense = new Expense(new Date(), expenseCategory, 1500060.95);
//            expense.setName("Board of Governors Meeting, San Francisco.");
//            expense.setBranch(BranchDao.findBranch(1));
//            expense.setCreatedAt(new Date());
//            expense.setCreatedBy("User One");
//            ExpenseDao.saveExpense(expense);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new AssertionError();
//        }
//    }
//
//    @Test
//    @Order(7)
//    void customerInsert() {
//        try {
//            customer = new Customer("John Doe", "johndoe@email.com", "+1234567890",
//                    "Arkansas", "TJ Robert Downy Street", "TXN-5685655555555",
//                    "USA");
//            customer.setCreatedAt(new Date());
//            customer.setCreatedBy("User One");
//            CustomerDao.saveCustomer(customer);
//        } catch (Exception e) {
//            throw new AssertionError();
//        }
//    }
//
//    @Test
//    @Order(8)
//    void supplierInsert() {
//        try {
//            supplier = new Supplier("Jeffery Doe", "jefferydoe@email.com", "+123456789012",
//                    "TXN-5685655555555", "17th Loop Lane Street", "Birmingham",
//                    "UK");
//            supplier.setCreatedAt(new Date());
//            supplier.setCreatedBy("User One");
//            SupplierDao.saveSupplier(supplier);
//        } catch (Exception e) {
//            throw new AssertionError();
//        }
//    }
//
//    @Test
//    @Order(9)
//    void adjustmentInsert() {
//        try {
//            var adjustmentDetail = new AdjustmentDetail(ProductDetailDao.findProductDetail(1), 12, "Increment");
//            var adjustmentMaster = new AdjustmentMaster(BranchDao.findBranch(1), "Approved.", new Date());
//            List<AdjustmentDetail> list = new LinkedList<>();
//
//            adjustmentDetail.setCreatedAt(new Date());
//            adjustmentDetail.setCreatedBy("User One");
//            adjustmentDetail.setAdjustment(adjustmentMaster);
//
//            list.add(adjustmentDetail);
//
//            adjustmentMaster.setAdjustmentDetails(list);
//            adjustmentMaster.setCreatedAt(new Date());
//            adjustmentMaster.setCreatedBy("User One");
//
//            AdjustmentMasterDao.saveAdjustmentMaster(adjustmentMaster);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new AssertionError();
//        }
//    }
//
//    @Test
//    @Order(10)
//    void quotationInsert() {
//        try {
//            var quotationDetail = new QuotationDetail(ProductDetailDao.findProductDetail(1), UnitOfMeasureDao.findUnitOfMeasure(1), 6.54, 25, 98);
//            var quotationMaster = new QuotationMaster(new Date(), CustomerDao.findCustomer(1), BranchDao.findBranch(1), "Pending", "Approved.");
//            List<QuotationDetail> list = new LinkedList<>();
//
//            quotationDetail.setCreatedAt(new Date());
//            quotationDetail.setCreatedBy("User One");
//            quotationDetail.setQuotation(quotationMaster);
//
//            list.add(quotationDetail);
//
//            quotationMaster.setQuotationDetails(list);
//            quotationMaster.setCreatedAt(new Date());
//            quotationMaster.setCreatedBy("User One");
//
//            QuotationMasterDao.saveQuotationMaster(quotationMaster);
//        } catch (Exception e) {
//            throw new AssertionError();
//        }
//    }
//
//    @Test
//    @Order(11)
//    void requisitionInsert() {
//        try {
//            var requisitionMaster = new RequisitionMaster(new Date(), SupplierDao.findSupplier(1), BranchDao.findBranch(1), "TTF",
//                    "Air", "Secure", new Date(), "Confirmed.", "Approved.", 450000);
//            var requisitionDetail = new RequisitionDetail(ProductDetailDao.findProductDetail(1),
//                    requisitionMaster,
//                    300,
//                    "Only retaining stock left.");
//            List<RequisitionDetail> list = new LinkedList<>();
//
//            requisitionDetail.setCreatedAt(new Date());
//            requisitionDetail.setCreatedBy("User One");
//            requisitionDetail.setRequisition(requisitionMaster);
//
//            list.add(requisitionDetail);
//
//            requisitionMaster.setRequisitionDetails(list);
//            requisitionMaster.setCreatedAt(new Date());
//            requisitionMaster.setCreatedBy("User One");
//
//            RequisitionMasterDao.saveRequisitionMaster(requisitionMaster);
//        } catch (Exception e) {
//            throw new AssertionError();
//        }
//    }
//
//    @Test
//    @Order(12)
//    void purchaseInsert() {
//        try {
//            var purchaseMaster = new PurchaseMaster(SupplierDao.findSupplier(1), BranchDao.findBranch(1), "Confirmed.",
//                    "Approved.", new Date());
//
//            var purchaseDetail = new PurchaseDetail(purchaseMaster,
//                    13234,
//                    ProductDetailDao.findProductDetail(1),
//                    1);
//            List<PurchaseDetail> list = new LinkedList<>();
//
//            purchaseDetail.setCreatedAt(new Date());
//            purchaseDetail.setCreatedBy("User One");
//            purchaseDetail.setPurchase(purchaseMaster);
//
//            list.add(purchaseDetail);
//
//            purchaseMaster.setPurchaseDetails(list);
//            purchaseMaster.setCreatedAt(new Date());
//            purchaseMaster.setCreatedBy("User One");
//
//            PurchaseMasterDao.savePurchaseMaster(purchaseMaster);
//        } catch (Exception e) {
//            throw new AssertionError();
//        }
//    }
//
//    @Test
//    @Order(13)
//    void transferInsert() {
//        try {
//            var transferMaster = new TransferMaster(new Date(),
//                    BranchDao.findBranch(1),
//                    BranchDao.findBranch(1),
//                    234889,
//                    "Pending",
//                    "Approved.");
//
//            var transferDetail = new TransferDetail(ProductDetailDao.findProductDetail(1),
//                    1,
//                    "5756756778",
//                    "Good.",
//                    25645,
//                    23645);
//            List<TransferDetail> list = new LinkedList<>();
//
//            transferDetail.setCreatedAt(new Date());
//            transferDetail.setCreatedBy("User One");
//            transferDetail.setTransfer(transferMaster);
//
//            list.add(transferDetail);
//
//            transferMaster.setTransferDetails(list);
//            transferMaster.setCreatedAt(new Date());
//            transferMaster.setCreatedBy("User One");
//
//            TransferMasterDao.saveTransferMaster(transferMaster);
//        } catch (Exception e) {
//            throw new AssertionError();
//        }
//    }
//
//    @Test
//    @Order(14)
//    void stockInInsert() {
//        try {
//            var stockInMaster = new StockInMaster(new Date(),
//                    BranchDao.findBranch(1),
//                    "Pending",
//                    "Approved.");
//
//            var stockInDetail = new StockInDetail(ProductDetailDao.findProductDetail(1),
//                    1,
//                    "",
//                    "Good",
//                    "Shelf A");
//            List<StockInDetail> list = new LinkedList<>();
//
//            stockInDetail.setCreatedAt(new Date());
//            stockInDetail.setCreatedBy("User One");
//            stockInDetail.setStockIn(stockInMaster);
//
//            list.add(stockInDetail);
//
//            stockInMaster.setStockInDetails(list);
//            stockInMaster.setCreatedAt(new Date());
//            stockInMaster.setCreatedBy("User One");
//
//            StockInMasterDao.saveStockInMaster(stockInMaster);
//        } catch (Exception e) {
//            throw new AssertionError();
//        }
//    }
//
//    @Test
//    @Order(15)
//    void saleInsert() {
//        try {
//            var saleMaster = new SaleMaster(CustomerDao.findCustomer(1),
//                    BranchDao.findBranch(1),
//                    "Pending",
//                    "Partial",
//                    "Approved..",
//                    new Date());
//
//            var saleDetail = new SaleDetail(ProductDetailDao.findProductDetail(1),
//                    41,
//                    "",
//                    2.09,
//                    "VAT",
//                    10,
//                    "Customs.");
//            List<SaleDetail> list = new LinkedList<>();
//
//            saleDetail.setCreatedAt(new Date());
//            saleDetail.setCreatedBy("User One");
//            saleDetail.setSaleMaster(saleMaster);
//
//            list.add(saleDetail);
//
//            saleMaster.setSaleDetails(list);
//            saleMaster.setCreatedAt(new Date());
//            saleMaster.setCreatedBy("User One");
//
//            SaleMasterDao.saveSaleMaster(saleMaster);
//        } catch (Exception e) {
//            throw new AssertionError();
//        }
//    }
//
//    @Test
//    @Order(16)
//    void productUpdate() {
//        try {
//            List<ProductDetail> list = new LinkedList<>();
//            int i = 0;
//            var pM = ProductMasterDao.findProductMaster(1);
//            var pD = pM.getProductDetails();
//            pD.forEach(e -> {
//                e.setName("Coke: " + i);
//                list.add(e);
//            });
//            list.forEach(ii -> ProductDetailDao.updateProductDetail(ii, ii.getId()));
//            pM.setProductDetails(pD);
//            pM.setProductDetails(list);
//            ProductMasterDao.updateProductMaster(pM, pM.getId());
//        } catch (Exception e) {
//            throw new AssertionError();
//        }
//    }

    // @Test
    // @Order(17)
    // void userInsert() {
    //     try {
    //         var user = new User("Test",
    //                 "One",
    //                 "User_0ne",
    //                 null,
    //         true,
    //         true);
    //         user.setCreatedAt(new Date());
    //         user.setCreatedBy("Tester One");
    //         UserDao.saveUser(user);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         throw new AssertionError();
    //     }
    // }
}
