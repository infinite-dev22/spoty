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
import org.infinite.spoty.database.models.ProductMaster;
import org.infinite.spoty.database.util.HibernateUtil;

public class ProductMasterDao {
  public static void saveProductMaster(ProductMaster obj) {
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

  public static void updateProductMaster(ProductMaster obj, int id) {
    Transaction transaction = null;
    ProductMaster productMaster;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      productMaster = session.get(ProductMaster.class, id);
      productMaster.setCode(obj.getCode());
      productMaster.setBarcodeType(obj.getBarcodeType());
      productMaster.setName(obj.getName());
      productMaster.setCategory(obj.getCategory());
      productMaster.setBrand(obj.getBrand());
      productMaster.canHaveVariants(obj.hasVariant());
      productMaster.setNotForSale(obj.isNotForSale());
      productMaster.setProductDetails(obj.getProductDetails());
      obj.getProductDetails()
          .forEach(
              productDetail -> {
                if (productDetail.getProduct() == null) productDetail.setProduct(productMaster);
              });
      productMaster.setUpdatedAt(new Date());
      // TODO: updated by should be a system user.
      // productMaster.setUpdatedBy();
      session.merge(productMaster);
      transaction.commit();
    } catch (HibernateError ex) {
      if (transaction != null) transaction.rollback();
      throw new RuntimeException(ex);
    }
  }

  public static ProductMaster findProductMaster(int id) {
    Transaction transaction = null;
    ProductMaster productMaster;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      productMaster = session.get(ProductMaster.class, id);
      transaction.commit();
    } catch (HibernateError ex) {
      if (transaction != null) transaction.rollback();
      throw new RuntimeException(ex);
    }
    return productMaster;
  }

  public static ObservableList<ProductMaster> getProductMaster() {
    Transaction transaction = null;
    ObservableList<ProductMaster> productCategories;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      productCategories =
          FXCollections.observableList(
              session.createQuery("from ProductMaster", ProductMaster.class).stream().toList());
      transaction.commit();
    } catch (HibernateError ex) {
      if (transaction != null) transaction.rollback();
      throw new RuntimeException(ex);
    }
    return productCategories;
  }

  public static void deleteProductMaster(int id) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.remove(session.getReference(ProductMaster.class, id));
      transaction.commit();
    } catch (HibernateError ex) {
      if (transaction != null) transaction.rollback();
      throw new RuntimeException(ex);
    }
  }
}
