package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.StockInMaster;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class StockInMasterDao {
    public static void saveStockInMaster(StockInMaster obj) {
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

    public static void updateStockInMaster(StockInMaster obj, int id) {
        Transaction transaction = null;
        StockInMaster stockInMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            stockInMaster = session.get(StockInMaster.class, id);
            stockInMaster.setUser(obj.getUser());
            stockInMaster.setRef(obj.getRef());
            stockInMaster.setDate(obj.getDate());
            stockInMaster.setBranch(obj.getBranch());
//            stockInMaster.setStockInDetails(obj.getStockInDetails());
            stockInMaster.setShipping(obj.getShipping());
            stockInMaster.setTotal(obj.getTotal());
            stockInMaster.setStatus(obj.getStatus());
            stockInMaster.setApprovedBy(obj.getApprovedBy());
            stockInMaster.setApprovalDate(obj.getApprovalDate());
            stockInMaster.setReceivedBy(obj.getReceivedBy());
            stockInMaster.setReceiveDate(obj.getReceiveDate());
            stockInMaster.setNotes(obj.getNotes());
            stockInMaster.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // stockInMaster.setUpdatedBy();
            session.merge(stockInMaster);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static StockInMaster findStockInMaster(int id) {
        Transaction transaction = null;
        StockInMaster stockInMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            stockInMaster = session.get(StockInMaster.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return stockInMaster;
    }

    public static ObservableList<StockInMaster> fetchStockInMasters() {
        Transaction transaction = null;
        ObservableList<StockInMaster> saleCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            saleCategories = FXCollections.observableList(
                    session.createQuery("from StockInMaster", StockInMaster.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return saleCategories;
    }

    public static void deleteStockInMaster(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(StockInMaster.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
