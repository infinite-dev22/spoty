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
import org.infinite.spoty.database.models.TransferMaster;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class TransferMasterDao {
    public static void saveTransferMaster(TransferMaster obj) {
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

    public static void updateTransferMaster(TransferMaster obj, int id) {
        Transaction transaction = null;
        TransferMaster transferMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            transferMaster = session.get(TransferMaster.class, id);
            transferMaster.setUser(obj.getUser());
            transferMaster.setRef(obj.getRef());
            transferMaster.setDate(obj.getDate());
            transferMaster.setFromBranch(obj.getFromBranch());
            transferMaster.setToBranch(obj.getToBranch());
//            transferMaster.setTransferDetails(obj.getTransferDetails());
            transferMaster.setShipping(obj.getShipping());
            transferMaster.setTotal(obj.getTotal());
            transferMaster.setStatus(obj.getStatus());
            transferMaster.setApprovedBy(obj.getApprovedBy());
            transferMaster.setApprovalDate(obj.getApprovalDate());
            transferMaster.setReceivedBy(obj.getReceivedBy());
            transferMaster.setReceiveDate(obj.getReceiveDate());
            transferMaster.setNotes(obj.getNotes());
            transferMaster.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // transferMaster.setUpdatedBy();
            session.merge(transferMaster);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static TransferMaster findTransferMaster(int id) {
        Transaction transaction = null;
        TransferMaster transferMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            transferMaster = session.get(TransferMaster.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return transferMaster;
    }

    public static ObservableList<TransferMaster> fetchTransferMasters() {
        Transaction transaction = null;
        ObservableList<TransferMaster> saleCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            saleCategories = FXCollections.observableList(
                    session.createQuery("from TransferMaster", TransferMaster.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return saleCategories;
    }

    public static void deleteTransferMaster(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(TransferMaster.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
