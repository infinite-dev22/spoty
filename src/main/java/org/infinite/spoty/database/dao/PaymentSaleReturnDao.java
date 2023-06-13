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
import org.infinite.spoty.database.models.PaymentSaleReturn;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class PaymentSaleReturnDao {
    public static void savePaymentSaleReturn(PaymentSaleReturn obj) {
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

    public static void updatePaymentSaleReturn(PaymentSaleReturn obj, int id) {
        Transaction transaction = null;
        PaymentSaleReturn paymentSaleReturn;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            paymentSaleReturn = session.getReference(PaymentSaleReturn.class, id);
            paymentSaleReturn.setUser(obj.getUser());
            paymentSaleReturn.setDate(obj.getDate());
            paymentSaleReturn.setRef(obj.getRef());
            paymentSaleReturn.setSaleReturn(obj.getSaleReturn());
            paymentSaleReturn.setPaymentMethod(obj.getPaymentMethod());
            paymentSaleReturn.setAmount(obj.getAmount());
            paymentSaleReturn.setChange(obj.getChange());
            paymentSaleReturn.setNotes(obj.getNotes());
            paymentSaleReturn.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // paymentSaleReturn.setUpdatedBy();
            session.merge(paymentSaleReturn);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static PaymentSaleReturn findPaymentSaleReturn(int id) {
        Transaction transaction = null;
        PaymentSaleReturn paymentSaleReturn;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            paymentSaleReturn = session.get(PaymentSaleReturn.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return paymentSaleReturn;
    }

    public static ObservableList<PaymentSaleReturn> getPaymentSaleReturn() {
        Transaction transaction = null;
        ObservableList<PaymentSaleReturn> paymentSaleReturns;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            paymentSaleReturns = FXCollections.observableList(
                    session.createQuery("from PaymentSaleReturn", PaymentSaleReturn.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return paymentSaleReturns;
    }

    public static void deletePaymentSaleReturn(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(PaymentSaleReturn.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
