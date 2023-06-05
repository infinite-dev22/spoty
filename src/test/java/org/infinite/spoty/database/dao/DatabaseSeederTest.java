package org.infinite.spoty.database.dao;

import org.infinite.spoty.database.models.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Date;

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
}
