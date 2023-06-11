package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.Currency;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

@SuppressWarnings("unchecked")
public class CurrencyDao {
    public static int saveCurrency(Currency obj) {
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

    public static int updateCurrency(Currency obj, long id) {
        Transaction transaction = null;
        Currency currency;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            currency = session.load(Currency.class, id);
            currency.setCode(obj.getCode());
            currency.setName(obj.getName());
            currency.setSymbol(obj.getSymbol());
            currency.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // currency.setUpdatedBy();
            session.update(currency);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static Currency findCurrency(long id) {
        Transaction transaction = null;
        Currency currency;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            currency = session.load(Currency.class, id);
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
            currencys = FXCollections.observableList(session.createQuery("from Currency").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return currencys;
    }

    public static int deleteCurrency(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(Currency.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
