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
    public static int savePaymentSaleReturn(PaymentSaleReturn obj) {
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

    public static int updatePaymentSaleReturn(PaymentSaleReturn obj, long id) {
        Transaction transaction = null;
        PaymentSaleReturn paymentSaleReturn;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            paymentSaleReturn = session.load(PaymentSaleReturn.class, id);
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
            session.update(paymentSaleReturn);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static PaymentSaleReturn findPaymentSaleReturn(long id) {
        Transaction transaction = null;
        PaymentSaleReturn paymentSaleReturn;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            paymentSaleReturn = session.load(PaymentSaleReturn.class, id);
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
            paymentSaleReturns = FXCollections.observableList(session.createQuery("from PaymentSaleReturn").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return paymentSaleReturns;
    }
    public static int deletePaymentSaleReturn(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(PaymentSaleReturn.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
