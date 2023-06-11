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
    public static void saveSupplier(Supplier obj) {
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

    public static void updateSupplier(Supplier obj, int id) {
        Transaction transaction = null;
        Supplier supplier;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            supplier = session.get(Supplier.class, id);
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
            session.merge(supplier);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static Supplier findSupplier(int id) {
        Transaction transaction = null;
        Supplier supplier;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            supplier = session.get(Supplier.class, id);
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
            purchaseCategories = FXCollections.observableList(
                    session.createQuery("from Supplier", Supplier.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseCategories;
    }

    public static void deleteSupplier(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(Supplier.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
