package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.UnitOfMeasure;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class UnitOfMeasureDao {
    public static int saveUnitOfMeasure(UnitOfMeasure obj) {
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

    public static int updateUnitOfMeasure(UnitOfMeasure obj, long id) {
        Transaction transaction = null;
        UnitOfMeasure unitOfMeasure;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            unitOfMeasure = session.load(UnitOfMeasure.class, id);
            unitOfMeasure.setName(obj.getName());
            unitOfMeasure.setShortName(obj.getShortName());
            unitOfMeasure.setBaseUnit(obj.getBaseUnit());
            unitOfMeasure.setOperator(obj.getOperator());
            unitOfMeasure.setOperatorValue(obj.getOperatorValue());
            unitOfMeasure.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // unitOfMeasure.setUpdatedBy();
            session.update(unitOfMeasure);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static UnitOfMeasure findUnitOfMeasure(long id) {
        Transaction transaction = null;
        UnitOfMeasure unitOfMeasure;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            unitOfMeasure = session.load(UnitOfMeasure.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return unitOfMeasure;
    }

    public static ObservableList<UnitOfMeasure> getUnitOfMeasure() {
        Transaction transaction = null;
        ObservableList<UnitOfMeasure> purchaseCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseCategories = FXCollections.observableList(session.createQuery("from UnitOfMeasure").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseCategories;
    }

    public static int deleteUnitOfMeasure(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(UnitOfMeasure.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
