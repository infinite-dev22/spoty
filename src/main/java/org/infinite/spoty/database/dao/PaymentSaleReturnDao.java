package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.PaymentSaleReturn;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class PaymentSaleReturnDao {
    public static void savePaymentSaleReturn(PaymentSaleReturn obj) {
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

    public static void updatePaymentSaleReturn(PaymentSaleReturn obj, int id) {
        Transaction transaction = null;
        PaymentSaleReturn paymentSaleReturn;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            paymentSaleReturn = session.getReference(PaymentSaleReturn.class, id);
            paymentSaleReturn.setUser(obj.getUser());
            paymentSaleReturn.setDate(obj.getDate());
            paymentSaleReturn.setRef(obj.getRef());
            paymentSaleReturn.setSaleReturn(obj.getSaleReturn());
            paymentSaleReturn.setPaymentMethod(obj.getPaymentMethod());
            paymentSaleReturn.setAmount(obj.getAmount());
            paymentSaleReturn.setChange(obj.getChange());
            paymentSaleReturn.setNotes(obj.getNotes());
            paymentSaleReturn.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // paymentSaleReturn.setUpdatedBy();
            session.merge(paymentSaleReturn);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static PaymentSaleReturn findPaymentSaleReturn(int id) {
        Transaction transaction = null;
        PaymentSaleReturn paymentSaleReturn;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            paymentSaleReturn = session.get(PaymentSaleReturn.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return paymentSaleReturn;
    }

    public static ObservableList<PaymentSaleReturn> getPaymentSaleReturn() {
        Transaction transaction = null;
        ObservableList<PaymentSaleReturn> paymentSaleReturns;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            paymentSaleReturns = FXCollections.observableList(
                    session.createQuery("from PaymentSaleReturn", PaymentSaleReturn.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return paymentSaleReturns;
    }

    public static void deletePaymentSaleReturn(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(PaymentSaleReturn.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
