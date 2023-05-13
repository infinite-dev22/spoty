package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.Expense;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class ExpenseDao {
    public static int saveExpense(Expense obj) {
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

    public static int updateExpense(Expense obj, long id) {
        Transaction transaction = null;
        Expense expense;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            expense = session.load(Expense.class, id);
            expense.setDate(obj.getDate());
            expense.setRef(obj.getRef());
            expense.setUser(obj.getUser());
            expense.setExpenseCategory(obj.getExpenseCategory());
            expense.setBranch(obj.getBranch());
            expense.setDetails(obj.getDetails());
            expense.setAmount(obj.getAmount());
            expense.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // expense.setUpdatedBy();
            session.update(expense);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static Expense findExpense(long id) {
        Transaction transaction = null;
        Expense expense = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            expense = session.load(Expense.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return expense;
    }

    public static ObservableList<Expense> getExpense() {
        Transaction transaction = null;
        ObservableList<Expense> expenses;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            expenses = FXCollections.observableList(session.createQuery("from Expense").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return expenses;
    }
    public static int deleteExpense(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(Expense.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
