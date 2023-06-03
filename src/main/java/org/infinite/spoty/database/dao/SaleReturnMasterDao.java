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
    public static int saveSaleReturnMaster(SaleReturnMaster obj) {
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

    public static int updateSaleReturnMaster(SaleReturnMaster obj, long id) {
        Transaction transaction = null;
        SaleReturnMaster saleReturnMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            saleReturnMaster = session.load(SaleReturnMaster.class, id);
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
            session.update(saleReturnMaster);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static SaleReturnMaster findSaleReturnMaster(long id) {
        Transaction transaction = null;
        SaleReturnMaster saleReturnMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            saleReturnMaster = session.load(SaleReturnMaster.class, id);
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
            saleCategories = FXCollections.observableList(session.createQuery("from SaleReturnMaster").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return saleCategories;
    }
    public static int deleteSaleReturnMaster(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(SaleReturnMaster.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
