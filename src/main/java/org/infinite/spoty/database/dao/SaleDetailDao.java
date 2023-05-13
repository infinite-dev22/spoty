package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.SaleDetail;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class SaleDetailDao {
    public static int saveSaleDetail(SaleDetail obj) {
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

    public static int updateSaleDetail(SaleDetail obj, long id) {
        Transaction transaction = null;
        SaleDetail saleDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            saleDetail = session.load(SaleDetail.class, id);
            saleDetail.setDate(obj.getDate());
            saleDetail.setRef(obj.getRef());
            saleDetail.setProduct(obj.getProduct());
            saleDetail.setSerialNumber(obj.getSerialNumber());
            saleDetail.setPrice(obj.getPrice());
            saleDetail.setSaleUnit(obj.getSaleUnit());
            saleDetail.setTaxType(obj.getTaxType());
            saleDetail.setNetTax(obj.getNetTax());
            saleDetail.setDiscount(obj.getDiscount());
            saleDetail.setDiscountType(obj.getDiscountType());
            saleDetail.setQuantity(obj.getQuantity());
            saleDetail.setTotal(obj.getTotal());
            saleDetail.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // saleDetail.setUpdatedBy();
            session.update(saleDetail);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static SaleDetail findSaleDetail(long id) {
        Transaction transaction = null;
        SaleDetail saleDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            saleDetail = session.load(SaleDetail.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return saleDetail;
    }

    public static ObservableList<SaleDetail> getSaleDetail() {
        Transaction transaction = null;
        ObservableList<SaleDetail> purchaseCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseCategories = FXCollections.observableList(session.createQuery("from SaleDetail").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseCategories;
    }
    public static int deleteSaleDetail(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(SaleDetail.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
