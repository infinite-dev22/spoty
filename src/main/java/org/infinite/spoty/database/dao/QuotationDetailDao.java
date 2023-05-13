package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.QuotationDetail;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class QuotationDetailDao {
    public static int saveQuotationDetail(QuotationDetail obj) {
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

    public static int updateQuotationDetail(QuotationDetail obj, long id) {
        Transaction transaction = null;
        QuotationDetail quotationDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            quotationDetail = session.load(QuotationDetail.class, id);
            quotationDetail.setPrice(obj.getPrice());
            quotationDetail.setSaleUnit(obj.getSaleUnit());
            quotationDetail.setProduct(obj.getProduct());
            quotationDetail.setQuotation(obj.getQuotation());
            quotationDetail.setTaxType(obj.getTaxType());
            quotationDetail.setNetTax(obj.getNetTax());
            quotationDetail.setDiscount(obj.getDiscount());
            quotationDetail.setDiscountType(obj.getDiscountType());
            quotationDetail.setQuantity(obj.getQuantity());
            quotationDetail.setTotal(obj.getTotal());
            quotationDetail.setSerialNumber(obj.getSerialNumber());
            quotationDetail.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // quotationDetail.setUpdatedBy();
            session.update(quotationDetail);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static QuotationDetail findQuotationDetail(long id) {
        Transaction transaction = null;
        QuotationDetail quotationDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            quotationDetail = session.load(QuotationDetail.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return quotationDetail;
    }

    public static ObservableList<QuotationDetail> getQuotationDetail() {
        Transaction transaction = null;
        ObservableList<QuotationDetail> purchaseCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseCategories = FXCollections.observableList(session.createQuery("from QuotationDetail").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseCategories;
    }
    public static int deleteQuotationDetail(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(QuotationDetail.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
