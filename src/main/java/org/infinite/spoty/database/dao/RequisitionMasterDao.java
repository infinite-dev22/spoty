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
import org.infinite.spoty.database.models.RequisitionMaster;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class RequisitionMasterDao {
    public static void saveRequisitionMaster(RequisitionMaster obj) {
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

    public static void updateRequisitionMaster(RequisitionMaster obj, int id) {
        Transaction transaction = null;
        RequisitionMaster requisitionMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            requisitionMaster = session.get(RequisitionMaster.class, id);
            requisitionMaster.setDate(obj.getDate());
            requisitionMaster.setSupplier(obj.getSupplier());
            requisitionMaster.setUser(obj.getUser());
            requisitionMaster.setBranch(obj.getBranch());
            requisitionMaster.setShipVia(obj.getShipVia());
            requisitionMaster.setShipMethod(obj.getShipMethod());
            requisitionMaster.setShippingTerms(obj.getShippingTerms());
            requisitionMaster.setDeliveryDate(obj.getDeliveryDate());
            requisitionMaster.setNotes(obj.getNotes());
            requisitionMaster.setStatus(obj.getStatus());
            requisitionMaster.setTotalCost(obj.getTotalCost());
            requisitionMaster.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // requisitionMaster.setUpdatedBy();
            session.merge(requisitionMaster);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static RequisitionMaster findRequisitionMaster(int id) {
        Transaction transaction = null;
        RequisitionMaster requisitionMaster;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            requisitionMaster = session.get(RequisitionMaster.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return requisitionMaster;
    }

    public static ObservableList<RequisitionMaster> fetchRequisitionMasters() {
        Transaction transaction = null;
        ObservableList<RequisitionMaster> requisitionMasters;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            requisitionMasters = FXCollections.observableList(
                    session.createQuery("from RequisitionMaster", RequisitionMaster.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return requisitionMasters;
    }

    public static void deleteRequisitionMaster(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(RequisitionMaster.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
