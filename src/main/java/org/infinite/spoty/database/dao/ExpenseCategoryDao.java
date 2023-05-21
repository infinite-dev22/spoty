package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.ExpenseCategory;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class ExpenseCategoryDao {
    public static int saveExpenseCategory(ExpenseCategory obj) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            obj.setCreatedAt(new Date());
            // TODO: created by should be a system user.
            // obj.setCreatedBy();
            session.save(obj);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static int updateExpenseCategory(ExpenseCategory obj, long id) {
        Transaction transaction = null;
        ExpenseCategory expenseCategory;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            expenseCategory = session.load(ExpenseCategory.class, id);
            expenseCategory.setUser(obj.getUser());
            expenseCategory.setName(obj.getName());
            expenseCategory.setDescription(obj.getDescription());
            expenseCategory.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // expenseCategory.setUpdatedBy();
            session.update(expenseCategory);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static ExpenseCategory findExpenseCategory(long id) {
        Transaction transaction = null;
        ExpenseCategory expenseCategory;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            expenseCategory = session.load(ExpenseCategory.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return expenseCategory;
    }

    public static ObservableList<ExpenseCategory> getExpenseCategories() {
        Transaction transaction = null;
        ObservableList<ExpenseCategory> expenseCategorys;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            expenseCategorys = FXCollections.observableList(session.createQuery("from ExpenseCategory").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return expenseCategorys;
    }
    public static int deleteExpenseCategory(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(ExpenseCategory.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
