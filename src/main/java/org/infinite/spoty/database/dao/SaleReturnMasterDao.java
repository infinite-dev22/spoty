package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.SaleReturnMaster;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class SaleReturnMasterDao {
    public static void saveSaleReturnMaster(SaleReturnMaster obj) {
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

    public static void updateSaleReturnMaster(SaleReturnMaster obj, int id) {
        Transaction transaction = null;
        SaleReturnMaster saleReturnMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            saleReturnMaster = session.get(SaleReturnMaster.class, id);
            saleReturnMaster.setUser(obj.getUser());
            saleReturnMaster.setDate(obj.getDate());
            saleReturnMaster.setRef(obj.getRef());
            saleReturnMaster.setCustomer(obj.getCustomer());
            saleReturnMaster.setBranch(obj.getBranch());
            saleReturnMaster.setTaxRate(obj.getTaxRate());
            saleReturnMaster.setNetTax(obj.getNetTax());
            saleReturnMaster.setDiscount(obj.getDiscount());
            saleReturnMaster.setTotal(obj.getTotal());
            saleReturnMaster.setPaid(obj.getPaid());
            saleReturnMaster.setPaymentStatus(obj.getPaymentStatus());
            saleReturnMaster.setStatus(obj.getStatus());
            saleReturnMaster.setNotes(obj.getNotes());
            saleReturnMaster.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // saleReturnMaster.setUpdatedBy();
            session.merge(saleReturnMaster);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static SaleReturnMaster findSaleReturnMaster(int id) {
        Transaction transaction = null;
        SaleReturnMaster saleReturnMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            saleReturnMaster = session.get(SaleReturnMaster.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return saleReturnMaster;
    }

    public static ObservableList<SaleReturnMaster> fetchSaleReturnMasters() {
        Transaction transaction = null;
        ObservableList<SaleReturnMaster> saleCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            saleCategories = FXCollections.observableList(
                    session.createQuery("from SaleReturnMaster", SaleReturnMaster.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return saleCategories;
    }

    public static void deleteSaleReturnMaster(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(SaleReturnMaster.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
