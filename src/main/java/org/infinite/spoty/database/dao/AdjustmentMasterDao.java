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
import org.infinite.spoty.database.models.AdjustmentMaster;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class AdjustmentMasterDao {
    public static void saveAdjustmentMaster(AdjustmentMaster obj) {
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

    public static void updateAdjustmentMaster(AdjustmentMaster obj, int id) {
        Transaction transaction = null;
        AdjustmentMaster adjustmentMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            adjustmentMaster = session.get(AdjustmentMaster.class, id);
            adjustmentMaster.setUser(obj.getUser());
            adjustmentMaster.setDate(obj.getDate());
            adjustmentMaster.setRef(obj.getRef());
            adjustmentMaster.setAdjustmentDetails(obj.getAdjustmentDetails());
            adjustmentMaster.setBranch(obj.getBranch());
            adjustmentMaster.setNotes(obj.getNotes());
            obj.getAdjustmentDetails().forEach(adjustmentDetail -> {
                if (adjustmentDetail.getAdjustment() == null) adjustmentDetail.setAdjustment(adjustmentMaster);
            });
            adjustmentMaster.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // adjustmentMaster.setUpdatedBy();
            session.merge(adjustmentMaster);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static AdjustmentMaster findAdjustmentMaster(int id) {
        Transaction transaction = null;
        AdjustmentMaster adjustmentMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            adjustmentMaster = session.get(AdjustmentMaster.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return adjustmentMaster;
    }

    public static ObservableList<AdjustmentMaster> fetchAdjustmentMasters() {
        Transaction transaction = null;
        ObservableList<AdjustmentMaster> adjustmentMasters;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            adjustmentMasters = FXCollections.observableList(session.createQuery("from AdjustmentMaster", AdjustmentMaster.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return adjustmentMasters;
    }

    public static void deleteAdjustmentMaster(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(AdjustmentMaster.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
