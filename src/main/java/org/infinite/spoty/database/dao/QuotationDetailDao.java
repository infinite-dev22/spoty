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
import org.infinite.spoty.database.models.QuotationDetail;
import org.infinite.spoty.database.util.HibernateUtil;

import java.util.Date;

public class QuotationDetailDao {
    public static void saveQuotationDetail(QuotationDetail obj) {
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

    public static void updateQuotationDetail(QuotationDetail obj, int id) {
        Transaction transaction = null;
        QuotationDetail quotationDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            quotationDetail = session.get(QuotationDetail.class, id);
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
            session.merge(quotationDetail);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }

    public static QuotationDetail findQuotationDetail(int id) {
        Transaction transaction = null;
        QuotationDetail quotationDetail;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            quotationDetail = session.get(QuotationDetail.class, id);
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return quotationDetail;
    }

    public static ObservableList<QuotationDetail> fetchQuotationDetails() {
        Transaction transaction = null;
        ObservableList<QuotationDetail> purchaseCategories;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            purchaseCategories = FXCollections.observableList(
                    session.createQuery("from QuotationDetail", QuotationDetail.class).stream().toList());
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
        return purchaseCategories;
    }

    public static void deleteQuotationDetail(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.getReference(QuotationDetail.class, id));
            transaction.commit();
        } catch (HibernateError ex) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(ex);
        }
    }
}
