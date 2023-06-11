package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.PaymentPurchaseReturn;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class PaymentPurchaseReturnDao {
    public static void savePaymentPurchaseReturn(PaymentPurchaseReturn obj) {
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

    public static void updatePaymentPurchaseReturn(PaymentPurchaseReturn obj, int id) {
        Transaction transaction = null;
        PaymentPurchaseReturn paymentPurchaseReturn;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            paymentPurchaseReturn = session.get(PaymentPurchaseReturn.class, id);
            paymentPurchaseReturn.setUser(obj.getUser());
            paymentPurchaseReturn.setDate(obj.getDate());
            paymentPurchaseReturn.setRef(obj.getRef());
            paymentPurchaseReturn.setPurchaseReturn(obj.getPurchaseReturn());
            paymentPurchaseReturn.setPaymentMethod(obj.getPaymentMethod());
            paymentPurchaseReturn.setAmount(obj.getAmount());
            paymentPurchaseReturn.setChange(obj.getChange());
            paymentPurchaseReturn.setNotes(obj.getNotes());
            paymentPurchaseReturn.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // paymentPurchaseReturn.setUpdatedBy();
            session.merge(paymentPurchaseReturn);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static PaymentPurchaseReturn findPaymentPurchaseReturn(int id) {
        Transaction transaction = null;
        PaymentPurchaseReturn paymentPurchaseReturn;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            paymentPurchaseReturn = session.get(PaymentPurchaseReturn.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return paymentPurchaseReturn;
    }

    public static ObservableList<PaymentPurchaseReturn> getPaymentPurchaseReturn() {
        Transaction transaction = null;
        ObservableList<PaymentPurchaseReturn> paymentPurchaseReturns;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            paymentPurchaseReturns = FXCollections.observableList(
                    session.createQuery("from PaymentPurchaseReturn", PaymentPurchaseReturn.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return paymentPurchaseReturns;
    }

    public static void deletePaymentPurchaseReturn(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(PaymentPurchaseReturn.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
