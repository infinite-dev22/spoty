/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package org.infinite.spoty.database.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.PurchaseDetail;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class PurchaseDetailDao {
    public static void savePurchaseDetail(PurchaseDetail obj) {
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

    public static void updatePurchaseDetail(PurchaseDetail obj, int id) {
        Transaction transaction = null;
        PurchaseDetail purchaseDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseDetail = session.get(PurchaseDetail.class, id);
            purchaseDetail.setCost(obj.getCost());
            purchaseDetail.setPurchase(obj.getPurchase());
            purchaseDetail.setNetTax(obj.getNetTax());
            purchaseDetail.setTaxtType(obj.getTaxtType());
            purchaseDetail.setDiscount(obj.getDiscount());
            purchaseDetail.setDiscountType(obj.getDiscountType());
            purchaseDetail.setProduct(obj.getProduct());
            purchaseDetail.setSerialNumber(obj.getSerialNumber());
            purchaseDetail.setTotal(obj.getTotal());
            purchaseDetail.setQuantity(obj.getQuantity());
            purchaseDetail.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // purchaseDetailPdct.setUpdatedBy();
            session.merge(purchaseDetail);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static PurchaseDetail findPurchaseDetail(int id) {
        Transaction transaction = null;
        PurchaseDetail purchaseDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseDetail = session.get(PurchaseDetail.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseDetail;
    }

    public static ObservableList<PurchaseDetail> fetchPurchaseDetails() {
        Transaction transaction = null;
        ObservableList<PurchaseDetail> purchaseCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseCategories = FXCollections.observableList(
                    session.createQuery("from PurchaseDetail", PurchaseDetail.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseCategories;
    }

    public static void deletePurchaseDetail(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(PurchaseDetail.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
