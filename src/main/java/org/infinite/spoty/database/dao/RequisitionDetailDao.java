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
import org.infinite.spoty.database.models.RequisitionDetail;
import org.infinite.spoty.database.util.HibernateUtil;

public class RequisitionDetailDao {
  public static void saveRequisitionDetail(RequisitionDetail obj) {
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

  public static void updateRequisitionDetail(RequisitionDetail obj, int id) {
    Transaction transaction = null;
    RequisitionDetail requisitionDetail;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      requisitionDetail = session.get(RequisitionDetail.class, id);
      requisitionDetail.setRequisition(obj.getRequisition());
      requisitionDetail.setProduct(obj.getProduct());
      requisitionDetail.setDescription(obj.getDescription());
      requisitionDetail.setQuantity(obj.getQuantity());
      requisitionDetail.setUpdatedAt(new Date());
      // TODO: updated by should be a system user.
      // requisitionDetail.setUpdatedBy();
      session.merge(requisitionDetail);
      transaction.commit();
    } catch (HibernateError ex) {
      if (transaction != null) transaction.rollback();
      throw new RuntimeException(ex);
    }
  }

  public static RequisitionDetail findRequisitionDetail(int id) {
    Transaction transaction = null;
    RequisitionDetail requisitionDetail;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      requisitionDetail = session.get(RequisitionDetail.class, id);
      transaction.commit();
    } catch (HibernateError ex) {
      if (transaction != null) transaction.rollback();
      throw new RuntimeException(ex);
    }
    return requisitionDetail;
  }

  public static ObservableList<RequisitionDetail> fetchRequisitionDetails() {
    Transaction transaction = null;
    ObservableList<RequisitionDetail> requisitionDetails;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      requisitionDetails =
          FXCollections.observableList(
              session.createQuery("from RequisitionDetail", RequisitionDetail.class).stream()
                  .toList());
      transaction.commit();
    } catch (HibernateError ex) {
      if (transaction != null) transaction.rollback();
      throw new RuntimeException(ex);
    }
    return requisitionDetails;
  }

  public static void deleteRequisitionDetail(int id) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.remove(session.getReference(RequisitionDetail.class, id));
      transaction.commit();
    } catch (HibernateError ex) {
      if (transaction != null) transaction.rollback();
      throw new RuntimeException(ex);
    }
  }
}
