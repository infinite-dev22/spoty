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
import org.infinite.spoty.database.models.PurchaseMaster;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class PurchaseMasterDao {
    public static void savePurchaseMaster(PurchaseMaster obj) {
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

    public static void updatePurchaseMaster(PurchaseMaster obj, int id) {
        Transaction transaction = null;
        PurchaseMaster purchaseMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseMaster = session.get(PurchaseMaster.class, id);
            purchaseMaster.setUser(obj.getUser());
            purchaseMaster.setRef(obj.getRef());
            purchaseMaster.setDate(obj.getDate());
            purchaseMaster.setSupplier(obj.getSupplier());
            purchaseMaster.setBranch(obj.getBranch());
            purchaseMaster.setTaxRate(obj.getTaxRate());
            purchaseMaster.setNetTax(obj.getNetTax());
            purchaseMaster.setDiscount(obj.getDiscount());
            purchaseMaster.setShipping(obj.getShipping());
            purchaseMaster.setPaid(obj.getPaid());
            purchaseMaster.setTotal(obj.getTotal());
            purchaseMaster.setStatus(obj.getStatus());
            purchaseMaster.setPaymentStatus(obj.getPaymentStatus());
            purchaseMaster.setNotes(obj.getNotes());
            purchaseMaster.setPurchaseDetails(obj.getPurchaseDetails());
            purchaseMaster.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // purchaseMaster.setUpdatedBy();
            session.merge(purchaseMaster);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static PurchaseMaster findPurchaseMaster(int id) {
        Transaction transaction = null;
        PurchaseMaster purchaseMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseMaster = session.get(PurchaseMaster.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseMaster;
    }

    public static ObservableList<PurchaseMaster> fetchPurchaseMasters() {
        Transaction transaction = null;
        ObservableList<PurchaseMaster> purchaseCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseCategories = FXCollections.observableList(
                    session.createQuery("from PurchaseMaster", PurchaseMaster.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseCategories;
    }

    public static void deletePurchaseMaster(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(PurchaseMaster.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
