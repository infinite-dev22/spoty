package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.TransferDetail;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class TransferDetailDao {
    public static int saveTransferDetail(TransferDetail obj) {
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

    public static int updateTransferDetail(TransferDetail obj, long id) {
        Transaction transaction = null;
        TransferDetail transferDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            transferDetail = session.load(TransferDetail.class, id);
            transferDetail.setTransfer(obj.getTransfer());
            transferDetail.setProduct(obj.getProduct());
            transferDetail.setQuantity(obj.getQuantity());
            transferDetail.setSerialNo(obj.getSerialNo());
            transferDetail.setDescription(obj.getDescription());
            transferDetail.setPrice(obj.getPrice());
            transferDetail.setTotal(obj.getTotal());
            transferDetail.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // transferDetail.setUpdatedBy();
            session.update(transferDetail);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }

    public static TransferDetail findTransferDetail(long id) {
        Transaction transaction = null;
        TransferDetail transferDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            transferDetail = session.load(TransferDetail.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return transferDetail;
    }

    public static ObservableList<TransferDetail> getTransferDetail() {
        Transaction transaction = null;
        ObservableList<TransferDetail> saleCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            saleCategories = FXCollections.observableList(session.createQuery("from TransferDetail").stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return saleCategories;
    }
    public static int deleteTransferDetail(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(TransferDetail.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return 1;
    }
}
