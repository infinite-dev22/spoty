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
    public static int saveStockInDetail(StockInDetail obj) {
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

    public static int updateStockInDetail(StockInDetail obj, long id) {
        Transaction transaction = null;
        StockInDetail stockInDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            stockInDetail = session.load(StockInDetail.class, id);
            stockInDetail.setStockIn(obj.getStockIn());
            stockInDetail.setProduct(obj.getProduct());
            stockInDetail.setQuantity(obj.getQuantity());
            stockInDetail.setSerialNo(obj.getSerialNo());
            stockInDetail.setDescription(obj.getDescription());
            stockInDetail.setLocation(obj.getLocation());
            stockInDetail.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // stockInDetail.setUpdatedBy();
            session.update(stockInDetail);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static StockInDetail findStockInDetail(long id) {
        Transaction transaction = null;
        StockInDetail stockInDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            stockInDetail = session.load(StockInDetail.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return stockInDetail;
    }

    public static ObservableList<StockInDetail> getStockInDetail() {
        Transaction transaction = null;
        ObservableList<StockInDetail> saleCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            saleCategories = FXCollections.observableList(session.createQuery("from StockInDetail").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return saleCategories;
    }

    public static int deleteStockInDetail(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(StockInDetail.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
