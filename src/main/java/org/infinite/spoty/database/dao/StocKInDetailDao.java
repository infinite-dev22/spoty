package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.StockInDetail;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class StocKInDetailDao {
    public static void saveStockInDetail(StockInDetail obj) {
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

    public static void updateStockInDetail(StockInDetail obj, int id) {
        Transaction transaction = null;
        StockInDetail stockInDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            stockInDetail = session.get(StockInDetail.class, id);
            stockInDetail.setStockIn(obj.getStockIn());
            stockInDetail.setProduct(obj.getProduct());
            stockInDetail.setQuantity(obj.getQuantity());
            stockInDetail.setSerialNo(obj.getSerialNo());
            stockInDetail.setDescription(obj.getDescription());
            stockInDetail.setLocation(obj.getLocation());
            stockInDetail.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // stockInDetail.setUpdatedBy();
            session.merge(stockInDetail);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static StockInDetail findStockInDetail(int id) {
        Transaction transaction = null;
        StockInDetail stockInDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            stockInDetail = session.get(StockInDetail.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return stockInDetail;
    }

    public static ObservableList<StockInDetail> fetchStockInDetails() {
        Transaction transaction = null;
        ObservableList<StockInDetail> saleCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            saleCategories = FXCollections.observableList(
                    session.createQuery("from StockInDetail", StockInDetail.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return saleCategories;
    }

    public static void deleteStockInDetail(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(StockInDetail.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
