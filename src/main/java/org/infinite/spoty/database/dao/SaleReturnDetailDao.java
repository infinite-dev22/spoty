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

import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.infinite.spoty.database.models.SaleReturnDetail;
import org.infinite.spoty.database.util.HibernateUtil;

public class SaleReturnDetailDao {
  public static void saveSaleReturnDetail(SaleReturnDetail obj) {
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

  public static void updateSaleReturnDetail(SaleReturnDetail obj, int id) {
    Transaction transaction = null;
    SaleReturnDetail saleReturnDetail;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      saleReturnDetail = session.get(SaleReturnDetail.class, id);
      saleReturnDetail.setSaleReturn(obj.getSaleReturn());
      saleReturnDetail.setProduct(obj.getProduct());
      saleReturnDetail.setPrice(obj.getPrice());
      saleReturnDetail.setSaleUnit(obj.getSaleUnit());
      saleReturnDetail.setTaxType(obj.getTaxType());
      saleReturnDetail.setNetTax(obj.getNetTax());
      saleReturnDetail.setDiscount(obj.getDiscount());
      saleReturnDetail.setDiscountType(obj.getDiscountType());
      saleReturnDetail.setSerialNumber(obj.getSerialNumber());
      saleReturnDetail.setQuantity(obj.getQuantity());
      saleReturnDetail.setTotal(obj.getTotal());
      saleReturnDetail.setUpdatedAt(new Date());
      // TODO: updated by should be a system user.
      // saleReturnDetail.setUpdatedBy();
      session.merge(saleReturnDetail);
      transaction.commit();
    } catch (HibernateError ex) {
      if (transaction != null) transaction.rollback();
      throw new RuntimeException(ex);
    }
  }

  public static SaleReturnDetail findSaleReturnDetail(int id) {
    Transaction transaction = null;
    SaleReturnDetail saleReturnDetail;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      saleReturnDetail = session.get(SaleReturnDetail.class, id);
      transaction.commit();
    } catch (HibernateError ex) {
      if (transaction != null) transaction.rollback();
      throw new RuntimeException(ex);
    }
    return saleReturnDetail;
  }

  public static ObservableList<SaleReturnDetail> fetchSaleReturnDetails() {
    Transaction transaction = null;
    ObservableList<SaleReturnDetail> saleCategories;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      saleCategories =
          FXCollections.observableList(
              session.createQuery("from SaleReturnDetail", SaleReturnDetail.class).stream()
                  .toList());
      transaction.commit();
    } catch (HibernateError ex) {
      if (transaction != null) transaction.rollback();
      throw new RuntimeException(ex);
    }
    return saleCategories;
  }

  public static void deleteSaleReturnDetail(int id) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.remove(session.getReference(SaleReturnDetail.class, id));
      transaction.commit();
    } catch (HibernateError ex) {
      if (transaction != null) transaction.rollback();
      throw new RuntimeException(ex);
    }
  }
}
