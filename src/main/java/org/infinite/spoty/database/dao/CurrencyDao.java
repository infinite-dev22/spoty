package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.Currency;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class CurrencyDao {
    public static void saveCurrency(Currency obj) {
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

    public static void updateCurrency(Currency obj, int id) {
        Transaction transaction = null;
        Currency currency;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            currency = session.get(Currency.class, id);
            currency.setCode(obj.getCode());
            currency.setName(obj.getName());
            currency.setSymbol(obj.getSymbol());
            currency.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // currency.setUpdatedBy();
            session.merge(currency);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static Currency findCurrency(int id) {
        Transaction transaction = null;
        Currency currency;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            currency = session.get(Currency.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return currency;
    }

    public static ObservableList<Currency> getCurrency() {
        Transaction transaction = null;
        ObservableList<Currency> currencys;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            currencys = FXCollections.observableList(session.createQuery("from Currency", Currency.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return currencys;
    }

    public static void deleteCurrency(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(Currency.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
