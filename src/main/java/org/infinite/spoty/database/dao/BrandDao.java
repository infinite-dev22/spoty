package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.Brand;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class BrandDao {
    // Image is a byte use file handling.
    // TODO: Store files in a folder and keep the path reference as a string in the DB for performance.
    public static void saveBrand(Brand obj) {
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
    }

    public static int updateBrand(Brand obj, long id) {
        Transaction transaction = null;
        Brand brand;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            brand = session.load(Brand.class, id);
            brand.setName(obj.getName());
            brand.setDescription(obj.getDescription());
//            brand.setImage(obj.getImage());
            brand.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // brand.setUpdatedBy();
            session.update(brand);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static Brand findBrand(long id) {
        Transaction transaction = null;
        Brand brand = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            brand = session.load(Brand.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return brand;
    }

    public static ObservableList<Brand> getBrands() {
        Transaction transaction = null;
        ObservableList<Brand> brands;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            brands = FXCollections.observableList(session.createQuery("from Brand").list());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return brands;
    }
    public static int deleteBrand(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(Brand.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
