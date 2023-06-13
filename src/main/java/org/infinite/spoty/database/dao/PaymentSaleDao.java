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
import org.infinite.spoty.database.models.PaymentSale;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class PaymentSaleDao {
    public static void savePaymentSale(PaymentSale obj) {
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

    public static void updatePaymentSale(PaymentSale obj, int id) {
        Transaction transaction = null;
        PaymentSale paymentSale;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            paymentSale = session.get(PaymentSale.class, id);
            paymentSale.setUser(obj.getUser());
            paymentSale.setDate(obj.getDate());
            paymentSale.setRef(obj.getRef());
            paymentSale.setSale(obj.getSale());
            paymentSale.setPaymentMethod(obj.getPaymentMethod());
            paymentSale.setAmount(obj.getAmount());
            paymentSale.setChange(obj.getChange());
            paymentSale.setNotes(obj.getNotes());
            paymentSale.setUpdatedAt(new Date());
            // TODO: updated by should be a system user.
            // paymentSale.setUpdatedBy();
            session.merge(paymentSale);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static PaymentSale findPaymentSale(int id) {
        Transaction transaction = null;
        PaymentSale paymentSale;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            paymentSale = session.get(PaymentSale.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return paymentSale;
    }

    public static ObservableList<PaymentSale> getPaymentSale() {
        Transaction transaction = null;
        ObservableList<PaymentSale> paymentSales;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            paymentSales = FXCollections.observableList(
                    session.createQuery("from PaymentSale", PaymentSale.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return paymentSales;
    }

    public static void deletePaymentSale(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(PaymentSale.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
