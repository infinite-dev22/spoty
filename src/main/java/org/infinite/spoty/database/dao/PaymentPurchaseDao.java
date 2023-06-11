package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.PaymentPurchase;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class PaymentPurchaseDao {
    public static void savePaymentPurchase(PaymentPurchase obj) {
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

    public static int updatePaymentPurchase(PaymentPurchase obj, int id) {
        Transaction transaction = null;
        PaymentPurchase paymentPurchase;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            paymentPurchase = session.get(PaymentPurchase.class, id);
            paymentPurchase.setUser(obj.getUser());
            paymentPurchase.setDate(obj.getDate());
            paymentPurchase.setRef(obj.getRef());
            paymentPurchase.setPurchase(obj.getPurchase());
            paymentPurchase.setPaymentMethod(obj.getPaymentMethod());
            paymentPurchase.setAmount(obj.getAmount());
            paymentPurchase.setChange(obj.getChange());
            paymentPurchase.setNotes(obj.getNotes());
            paymentPurchase.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // paymentPurchase.setUpdatedBy();
            session.merge(paymentPurchase);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static PaymentPurchase findPaymentPurchase(int id) {
        Transaction transaction = null;
        PaymentPurchase paymentPurchase;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            paymentPurchase = session.get(PaymentPurchase.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return paymentPurchase;
    }

    public static ObservableList<PaymentPurchase> getPaymentPurchase() {
        Transaction transaction = null;
        ObservableList<PaymentPurchase> paymentPurchases;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            paymentPurchases = FXCollections.observableList(
                    session.createQuery("from PaymentPurchase", PaymentPurchase.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return paymentPurchases;
    }

    public static void deletePaymentPurchase(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(PaymentPurchase.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
