package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.SaleDetail;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

@SuppressWarnings("unchecked")
public class SaleDetailDao {
    public static void saveSaleDetail(SaleDetail obj) {
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

    public static void updateSaleDetail(SaleDetail obj, int id) {
        Transaction transaction = null;
        SaleDetail saleDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            saleDetail = session.get(SaleDetail.class, id);
            saleDetail.setRef(obj.getRef());
            saleDetail.setProduct(obj.getProduct());
            saleDetail.setSerialNumber(obj.getSerialNumber());
            saleDetail.setPrice(obj.getPrice());
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
    }

    public static SaleDetail findSaleDetail(int id) {
        Transaction transaction = null;
        SaleDetail saleDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            saleDetail = session.get(SaleDetail.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return saleDetail;
    }

    public static ObservableList<SaleDetail> fetchSaleDetails() {
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

    public static void deleteSaleDetail(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.get(SaleDetail.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
