package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.Supplier;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class SupplierDao {
    public static int saveSupplier(Supplier obj) {
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

    public static int updateSupplier(Supplier obj, long id) {
        Transaction transaction = null;
        Supplier supplier;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            supplier = session.load(Supplier.class, id);
            supplier.setName(obj.getName());
            supplier.setCode(obj.getCode());
            supplier.setEmail(obj.getEmail());
            supplier.setPhone(obj.getPhone());
            supplier.setTaxNumber(obj.getTaxNumber());
            supplier.setAddress(obj.getAddress());
            supplier.setCity(obj.getCity());
            supplier.setCountry(obj.getCountry());
            supplier.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // supplier.setUpdatedBy();
            session.update(supplier);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static Supplier findSupplier(long id) {
        Transaction transaction = null;
        Supplier supplier;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            supplier = session.load(Supplier.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return supplier;
    }

    public static ObservableList<Supplier> fetchSuppliers() {
        Transaction transaction = null;
        ObservableList<Supplier> purchaseCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseCategories = FXCollections.observableList(session.createQuery("from Supplier").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseCategories;
    }

    public static int deleteSupplier(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(Supplier.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
