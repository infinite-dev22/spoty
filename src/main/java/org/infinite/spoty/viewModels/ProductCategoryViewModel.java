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

package org.infinite.spoty.viewModels;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.dao.ProductCategoryDao;
import org.infinite.spoty.database.models.ProductCategory;

public class ProductCategoryViewModel {
  private static final IntegerProperty id = new SimpleIntegerProperty(0);
  private static final StringProperty code = new SimpleStringProperty("");
  private static final StringProperty title = new SimpleStringProperty("");
  private static final StringProperty name = new SimpleStringProperty("");
  public static ObservableList<ProductCategory> categoriesList =
      FXCollections.observableArrayList();

  public static Integer getId() {
    return id.get();
  }

  public static void setId(Integer id) {
    ProductCategoryViewModel.id.set(id);
  }

  public static IntegerProperty idProperty() {
    return id;
  }

  public static String getTitle() {
    return title.get();
  }

  public static void setTitle(String title) {
    ProductCategoryViewModel.title.set(title);
  }

  public static StringProperty titleProperty() {
    return title;
  }

  public static String getCode() {
    return code.get();
  }

  public static void setCode(String code) {
    ProductCategoryViewModel.code.set(code);
  }

  public static StringProperty codeProperty() {
    return code;
  }

  public static String getName() {
    return name.get();
  }

  public static void setName(String name) {
    ProductCategoryViewModel.name.set(name);
  }

  public static StringProperty nameProperty() {
    return name;
  }

  public static void saveProductCategory() {
    ProductCategory productCategory = new ProductCategory(getCode(), getName());
    ProductCategoryDao.saveProductCategory(productCategory);
    categoriesList.clear();
    clearProductCategoryData();
    getItems();
  }

  public static void clearProductCategoryData() {
    setId(0);
    setCode("");
    setName("");
  }

  public static ObservableList<ProductCategory> getItems() {
    categoriesList.clear();
    categoriesList.addAll(ProductCategoryDao.getProductCategories());
    return categoriesList;
  }

  public static void getItem(int productCategoryID) {
    ProductCategory productCategory = ProductCategoryDao.findProductCategory(productCategoryID);
    setId(productCategory.getId());
    setCode(productCategory.getCode());
    setName(productCategory.getName());
    getItems();
  }

  public static void updateItem(int productCategoryID) {
    ProductCategory productCategory = new ProductCategory(getCode(), getName());
    ProductCategoryDao.updateProductCategory(productCategory, productCategoryID);
    clearProductCategoryData();
    getItems();
  }
}
