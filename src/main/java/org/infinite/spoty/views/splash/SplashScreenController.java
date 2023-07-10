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

package org.infinite.spoty.views.splash;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.components.navigation.Pages.setControllers;
import static org.infinite.spoty.components.navigation.Pages.setPanes;

import fr.brouillard.oss.cssfx.CSSFX;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.database.management.SQLiteTableCreator;
import org.infinite.spoty.values.strings.Labels;
import org.infinite.spoty.viewModels.*;
import org.infinite.spoty.views.BaseController;

public class SplashScreenController implements Initializable {
  @FXML public Label applicationName;
  @FXML public AnchorPane splashScreenPane;
  @FXML public Label companyName;

  public static void checkFunctions() {
    Task<Void> databaseCreator =
        new Task<>() {
          @Override
          protected Void call() {
            try {
              SQLiteTableCreator.getInstance().createTablesIfNotExist();
            } catch (SQLException e) {
              throw new RuntimeException(e);
            }
            return null;
          }
        };

    Task<Void> initDataFetcher = new Task<>() {
      @Override
      protected Void call() {
        AdjustmentMasterViewModel.getAdjustmentMasters();
        BranchViewModel.getAllBranches();
        BrandViewModel.getItems();
        CurrencyViewModel.getAllCurrencies();
        CustomerViewModel.getAllCustomers();
        ExpenseCategoryViewModel.getAllCategories();
        ExpenseViewModel.getAllExpenses();
        ProductCategoryViewModel.getItems();
        ProductMasterViewModel.getProductMasters();
        PurchaseMasterViewModel.getPurchaseMasters();
        PurchaseReturnMasterViewModel.getPurchaseReturnMasters();
        QuotationMasterViewModel.getQuotationMasters();
        RequisitionMasterViewModel.getRequisitionMasters();
        SaleMasterViewModel.getSaleMasters();
        SaleReturnMasterViewModel.getSaleReturnMasters();
        StockInMasterViewModel.getStockInMasters();
        SupplierViewModel.getAllSuppliers();
        TransferMasterViewModel.getTransferMasters();
        UOMViewModel.getItems();
        UserViewModel.getAllUsers();
        return null;
      }
    };

    Thread thread1 = new Thread(databaseCreator);
    thread1.setDaemon(true);
    Thread thread2 = new Thread(initDataFetcher);
    thread2.setDaemon(true);

    try {
      thread1.start();
      thread1.join();
      thread2.start();
      thread2.join();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    Platform.runLater(
        () -> {
          try {
            CSSFX.start();
            Stage stage = new Stage();
            setControllers(stage);
            setPanes();
            FXMLLoader loader = fxmlLoader("fxml/Base.fxml");
            loader.setControllerFactory(c -> BaseController.getInstance(stage));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
            io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(scene);
            scene.setFill(null);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setMaximized(true);
            stage.setTitle(Labels.APP_NAME);
            stage.show();
            SimpleNotificationHolder.setNotificationOwner(stage);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    companyName.setText(Labels.COMPANY_NAME);
    applicationName.setText(Labels.APP_NAME);
  }
}
