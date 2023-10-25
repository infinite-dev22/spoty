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

import fr.brouillard.oss.cssfx.CSSFX;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.components.navigation.Pages;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.database.management.SQLiteTableCreator;
import org.infinite.spoty.startup.Dialogs;
import org.infinite.spoty.startup.SpotyPaths;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.utils.SpotyThreader;
import org.infinite.spoty.values.strings.Labels;
import org.infinite.spoty.viewModels.*;
import org.infinite.spoty.views.BaseController;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotyResourceLoader.fxmlLoader;
import static org.infinite.spoty.utils.SpotyThreader.singleThreadCreator;

public class SplashScreenController implements Initializable {
    @FXML
    public Label applicationName;
    @FXML
    public AnchorPane splashScreenPane;
    @FXML
    public Label companyName;

    public static void checkFunctions() {
        var tableCreate = tableCreate();
        var dataInit = dataInit();

        try {
            tableCreate.join();
            dataInit.join();
            startApp();
        } catch (InterruptedException e) {
            SpotyLogger.writeToFile(e, SplashScreenController.class);
        }
    }

    @NotNull
    private static Thread tableCreate() {
        return SpotyThreader.singleThreadCreator(
                "data-storage-man",
                () -> {
                    try {
                        SpotyPaths.createPaths();
                        SQLiteTableCreator.getInstance().createTablesIfNotExist();
                        SQLiteTableCreator.getInstance().seedDatabase();
                    } catch (SQLException e) {
                        SpotyLogger.writeToFile(e, SplashScreenController.class);
                    }
                });
    }

    private static void startApp() {
        Platform.runLater(
                () -> {
                    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                    try {
                        // handle CSS dynamically.
                        CSSFX.start();
                        Stage primaryStage = new Stage();
                        // Load app views.
                        Pages.setControllers(primaryStage);
                        Pages.setPanes();
                        // Load dialog views.
                        Dialogs.setControllers();
                        Dialogs.setDialogContent();
                        // Set base view.
                        FXMLLoader loader = fxmlLoader("fxml/Base.fxml");
                        loader.setControllerFactory(c -> BaseController.getInstance(primaryStage));
                        // Base view parent.
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        // Set application scene theme to MFX modern themes.
                        MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
                        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(scene);
                        // Fixes black edges showing in main app scene.
                        scene.setFill(null);
                        primaryStage.setScene(scene);
                        primaryStage.initStyle(StageStyle.TRANSPARENT);
                        // Set initial window size.
                        primaryStage.setHeight(primScreenBounds.getHeight());
                        primaryStage.setWidth(primScreenBounds.getWidth());
                        // Set window position to center of screen.
                        // This isn't necessary, just felt like adding it here.
                        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
                        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
                        // Set window title name, this name will only be seen when cursor hovers over app icon in
                        // taskbar. Not necessary too but added since other apps also do this.
                        primaryStage.setTitle(Labels.APP_NAME);
                        primaryStage.show();
                        // Initialize app notification handler.
                        SimpleNotificationHolder.setNotificationOwner(primaryStage);
                    } catch (IOException e) {
                        SpotyLogger.writeToFile(e, SplashScreenController.class);
                    }
                });
    }

    @NotNull
    private static Thread dataInit() {
        return singleThreadCreator(
                "data-tracker",
                () -> {
                    try {
                        AdjustmentMasterViewModel.getAllAdjustmentMasters();
                        BranchViewModel.getAllBranches();
                        BrandViewModel.getItems();
                        CurrencyViewModel.getAllCurrencies();
                        CustomerViewModel.getAllCustomers();
                        ExpenseCategoryViewModel.getAllCategories();
                        ExpenseViewModel.getAllExpenses();
                        ProductCategoryViewModel.getItems();
                        ProductViewModel.getAllProducts();
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
                        RoleViewModel.getAllRoles();
                        // Initialize Permissions.
                        PermissionsViewModel.setAccessBrands();
                        PermissionsViewModel.setAccessBranchSettings();
                        PermissionsViewModel.setAccessBranchSettings();
                        PermissionsViewModel.setAccessBranchStockChartsReports();
                        PermissionsViewModel.setAccessCurrencySettings();
                        PermissionsViewModel.setAccessCustomersReports();
                        PermissionsViewModel.setAccessCustomerRankingsReports();
                        PermissionsViewModel.setViewPermissions();
                        PermissionsViewModel.setAccessPaymentsPurchasesReports();
                        PermissionsViewModel.setViewSystemSettings();
                        PermissionsViewModel.setViewAdjustments();
                        PermissionsViewModel.setViewPurchases();
                        PermissionsViewModel.setViewBranch();
                        PermissionsViewModel.setCreateBackup();
                        PermissionsViewModel.setDeleteBranch();
                        PermissionsViewModel.setAccessPurchasesReports();
                        PermissionsViewModel.setViewUsers();
                        PermissionsViewModel.setViewSales();
                        PermissionsViewModel.setViewPOSSettings();
                        PermissionsViewModel.setViewCurrency();
                        PermissionsViewModel.setViewBackupSettings();
                        PermissionsViewModel.setEditUsers();
                        PermissionsViewModel.setEditSystemSettings();
                        PermissionsViewModel.setEditSales();
                        PermissionsViewModel.setEditPOSSettings();
                        PermissionsViewModel.setEditCurrency();
                        PermissionsViewModel.setEditBranch();
                        PermissionsViewModel.setCreateProducts();
                        PermissionsViewModel.setDeleteUsers();
                        PermissionsViewModel.setDeleteSales();
                        PermissionsViewModel.setDeleteCurrency();
                        PermissionsViewModel.setCreateSales();
                        PermissionsViewModel.setCreateCurrency();
                        PermissionsViewModel.setCreateBranch();
                        PermissionsViewModel.setViewTransfers();
                        PermissionsViewModel.setEditPermissions();
                        PermissionsViewModel.setViewSaleReturns();
                        PermissionsViewModel.setDeletePermissions();
                        PermissionsViewModel.setCreateUsers();
                        PermissionsViewModel.setViewQuotations();
                        PermissionsViewModel.setViewPurchaseReturns();
                        PermissionsViewModel.setViewExpenses();
                        PermissionsViewModel.setEditTransfers();
                        PermissionsViewModel.setEditExpenses();
                        PermissionsViewModel.setEditSaleReturns();
                        PermissionsViewModel.setEditQuotations();
                        PermissionsViewModel.setEditPurchaseReturns();
                        PermissionsViewModel.setEditAdjustments();
                        PermissionsViewModel.setDeleteTransfers();
                        PermissionsViewModel.setDeleteExpenses();
                        PermissionsViewModel.setCreatePermissions();
                        PermissionsViewModel.setDeleteSaleReturns();
                        PermissionsViewModel.setDeletePurchaseReturns();
                        PermissionsViewModel.setCreateExpenses();
                        PermissionsViewModel.setDeleteQuotations();
                        PermissionsViewModel.setCreatePurchaseReturns();
                        PermissionsViewModel.setEditPurchases();
                        PermissionsViewModel.setDeletePurchases();
                        PermissionsViewModel.setDeleteAdjustments();
                        PermissionsViewModel.setCreateTransfers();
                        PermissionsViewModel.setCreateSaleReturns();
                        PermissionsViewModel.setCreateQuotations();
                        PermissionsViewModel.setCreateAdjustments();
                        PermissionsViewModel.setCreatePurchases();
                        PermissionsViewModel.setPosAccess();
                        PermissionsViewModel.setDashboardAccess();
                        PermissionsViewModel.setAccessPaymentsSalesReports();
                        PermissionsViewModel.setAccessProductCategories();
                        PermissionsViewModel.setAccessUnitsOfMeasure();
                        PermissionsViewModel.setAccessPurchasesReturnsPaymentsReports();
                        PermissionsViewModel.setAccessSalesReports();
                        PermissionsViewModel.setAccessSalesReturnsPaymentsReports();
                        PermissionsViewModel.setCreateCustomers();
                        PermissionsViewModel.setCreateSuppliers();
                        PermissionsViewModel.setDeleteCustomers();
                        PermissionsViewModel.setDeleteProducts();
                        PermissionsViewModel.setDeleteSuppliers();
                        PermissionsViewModel.setAccessProductQuantityReports();
                        PermissionsViewModel.setEditCustomers();
                        PermissionsViewModel.setEditProducts();
                        PermissionsViewModel.setEditSuppliers();
                        PermissionsViewModel.setAccessSuppliersReports();
                        PermissionsViewModel.setImportCustomers();
                        PermissionsViewModel.setImportProducts();
                        PermissionsViewModel.setImportSuppliers();
                        PermissionsViewModel.setViewCustomers();
                        PermissionsViewModel.setViewProducts();
                        PermissionsViewModel.setAccessProductSalesReports();
                        PermissionsViewModel.setAccessProfitsAndLossesReports();
                        PermissionsViewModel.setAccessProductPurchasesReports();
                        PermissionsViewModel.setAccessProductsReports();
                        PermissionsViewModel.setAccessStocksReports();
                        PermissionsViewModel.setAccessTopSellingProductsReports();
                        PermissionsViewModel.setAccessUsersReports();
                        PermissionsViewModel.setViewSuppliers();
                        PermissionsViewModel.setViewStockIn();
                        PermissionsViewModel.setCreateStockIn();
                        PermissionsViewModel.setEditStockIn();
                        PermissionsViewModel.setDeleteStockIn();
                        PermissionsViewModel.setViewRequisition();
                        PermissionsViewModel.setCreateRequisition();
                        PermissionsViewModel.setEditRequisition();
                        PermissionsViewModel.setDeleteRequisition();
                    } catch (SQLException e) {
                        SpotyLogger.writeToFile(e, SplashScreenController.class);
                    }
                });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        companyName.setText(Labels.COMPANY_NAME);
        applicationName.setText(Labels.APP_NAME);
    }
}
