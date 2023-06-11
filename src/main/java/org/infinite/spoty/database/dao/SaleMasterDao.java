package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.SaleMaster;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class SaleMasterDao {
    public static void saveSaleMaster(SaleMaster obj) {
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

    public static void updateSaleMaster(SaleMaster obj, int id) {
        Transaction transaction = null;
        SaleMaster saleMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            saleMaster = session.get(SaleMaster.class, id);
            saleMaster.setUser(obj.getUser());
            saleMaster.setRef(obj.getRef());
            saleMaster.setDate(obj.getDate());
            saleMaster.setCustomer(obj.getCustomer());
            saleMaster.setBranch(obj.getBranch());
            saleMaster.setTaxRate(obj.getTaxRate());
            saleMaster.setNetTax(obj.getNetTax());
            saleMaster.setDiscount(obj.getDiscount());
            saleMaster.setTotal(obj.getTotal());
            saleMaster.setAmountPaid(obj.getAmountPaid());
            saleMaster.setAmountDue(obj.getAmountDue());
            saleMaster.setPaymentStatus(obj.getPaymentStatus());
            saleMaster.setSaleStatus(obj.getSaleStatus());
            saleMaster.setNotes(obj.getNotes());
            saleMaster.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // saleMaster.setUpdatedBy();
            session.merge(saleMaster);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static SaleMaster findSaleMaster(int id) {
        Transaction transaction = null;
        SaleMaster saleMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            saleMaster = session.get(SaleMaster.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return saleMaster;
    }

    public static ObservableList<SaleMaster> fetchSaleMasters() {
        Transaction transaction = null;
        ObservableList<SaleMaster> purchaseCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseCategories = FXCollections.observableList(
                    session.createQuery("from SaleMaster", SaleMaster.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseCategories;
    }

    public static void deleteSaleMaster(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(SaleMaster.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
