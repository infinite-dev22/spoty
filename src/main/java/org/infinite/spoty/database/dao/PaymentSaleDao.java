package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.PaymentSale;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class PaymentSaleDao {
    public static void savePaymentSale(PaymentSale obj) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            obj.setCreatedAt(new Date());
            // TODO: created by should be a system user.
            // obj.setCreatedBy();
            session.persist(obj);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static void updatePaymentSale(PaymentSale obj, int id) {
        Transaction transaction = null;
        PaymentSale paymentSale;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            paymentSale = session.get(PaymentSale.class, id);
            paymentSale.setUser(obj.getUser());
            paymentSale.setDate(obj.getDate());
            paymentSale.setRef(obj.getRef());
            paymentSale.setSale(obj.getSale());
            paymentSale.setPaymentMethod(obj.getPaymentMethod());
            paymentSale.setAmount(obj.getAmount());
            paymentSale.setChange(obj.getChange());
            paymentSale.setNotes(obj.getNotes());
            paymentSale.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // paymentSale.setUpdatedBy();
            session.merge(paymentSale);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static PaymentSale findPaymentSale(int id) {
        Transaction transaction = null;
        PaymentSale paymentSale;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            paymentSale = session.get(PaymentSale.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return paymentSale;
    }

    public static ObservableList<PaymentSale> getPaymentSale() {
        Transaction transaction = null;
        ObservableList<PaymentSale> paymentSales;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            paymentSales = FXCollections.observableList(
                    session.createQuery("from PaymentSale", PaymentSale.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return paymentSales;
    }

    public static void deletePaymentSale(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(PaymentSale.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
