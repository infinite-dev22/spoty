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
import org.infinite.spoty.database.models.AdjustmentDetail;
import org.infinite.spoty.database.util.HibernateUtil;

public class AdjustmentDetailDao {
  public static void saveAdjustmentDetail(AdjustmentDetail obj) {
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

  public static void updateAdjustmentDetail(AdjustmentDetail obj, int id) {
    Transaction transaction = null;
    AdjustmentDetail adjustmentDetail;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      adjustmentDetail = session.get(AdjustmentDetail.class, id);
      adjustmentDetail.setAdjustment(obj.getAdjustment());
      adjustmentDetail.setProduct(obj.getProduct());
      adjustmentDetail.setAdjustmentType(obj.getAdjustmentType());
      adjustmentDetail.setQuantity(obj.getQuantity());
      adjustmentDetail.setUpdatedAt(new Date());
      // TODO: updated by should be a system user.
      // adjustmentDetail.setUpdatedBy();
      session.merge(adjustmentDetail);
      transaction.commit();
    } catch (HibernateError ex) {
      if (transaction != null) transaction.rollback();
      throw new RuntimeException(ex);
    }
  }

  public static AdjustmentDetail findAdjustmentDetail(int id) {
    Transaction transaction = null;
    AdjustmentDetail adjustmentDetail;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      adjustmentDetail = session.get(AdjustmentDetail.class, id);
      transaction.commit();
    } catch (HibernateError ex) {
      if (transaction != null) transaction.rollback();
      throw new RuntimeException(ex);
    }
    return adjustmentDetail;
  }

  public static ObservableList<AdjustmentDetail> getAdjustmentDetail() {
    Transaction transaction = null;
    ObservableList<AdjustmentDetail> adjustmentDetails;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      adjustmentDetails =
          FXCollections.observableList(
              session.createQuery("from AdjustmentDetail", AdjustmentDetail.class).stream()
                  .toList());
      transaction.commit();
    } catch (HibernateError ex) {
      if (transaction != null) transaction.rollback();
      throw new RuntimeException(ex);
    }
    return adjustmentDetails;
  }

  public static void deleteAdjustmentDetail(int id) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.remove(session.getReference(AdjustmentDetail.class, id));
      transaction.commit();
    } catch (HibernateError ex) {
      if (transaction != null) transaction.rollback();
      throw new RuntimeException(ex);
    }
  }
}
