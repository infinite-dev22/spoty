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

package inc.nomard.spoty.reporting.navigation;

import inc.nomard.spoty.reporting.accounting.*;
import inc.nomard.spoty.reporting.general.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;

import static inc.nomard.spoty.reporting.SpotyReportingResourceLoader.fxmlLoader;

public class Pages {
    // Stock Report
    private static final FXMLLoader stockReportLoader = fxmlLoader("views/stock_report/StockReport.fxml");
    // Account Report
    private static final FXMLLoader balanceSheetLoader = fxmlLoader("views/account/report/BalanceSheet.fxml");
    private static final FXMLLoader bankBookLoader = fxmlLoader("views/account/report/BankBook.fxml");
    private static final FXMLLoader cashBookLoader = fxmlLoader("views/account/report/CashBook.fxml");
    private static final FXMLLoader coaPrintLoader = fxmlLoader("views/account/report/CoaPrint.fxml");
    private static final FXMLLoader dayBookLoader = fxmlLoader("views/account/report/DayBook.fxml");
    private static final FXMLLoader expenditureStatementLoader = fxmlLoader("views/account/report/ExpenditureStatement.fxml");
    private static final FXMLLoader fixedAssetScheduleLoader = fxmlLoader("views/account/report/FixedAssetSchedule.fxml");
    private static final FXMLLoader generalLedgerLoader = fxmlLoader("views/account/report/GeneralLedger.fxml");
    private static final FXMLLoader incomeStatementLoader = fxmlLoader("views/account/report/IncomeStatement.fxml");
    private static final FXMLLoader profitLossLoader = fxmlLoader("views/account/report/ProfitLoss.fxml");
    private static final FXMLLoader receiptAndPaymentLoader = fxmlLoader("views/account/report/ReceiptAndPayment.fxml");
    private static final FXMLLoader subLedgerLoader = fxmlLoader("views/account/report/SubLedger.fxml");
    private static final FXMLLoader trialBalanceLoader = fxmlLoader("views/account/report/TrialBalance.fxml");
    private static final FXMLLoader bankReconciliationReportLoader = fxmlLoader("views/account/report/BankReconciliationReport.fxml");
    // Reports
    private static final FXMLLoader closingLoader = fxmlLoader("views/report/Closing.fxml");
    private static final FXMLLoader closingReportLoader = fxmlLoader("views/report/ClosingReport.fxml");
    private static final FXMLLoader dailyCustomerReportLoader = fxmlLoader("views/report/DailyCustomerReport.fxml");
    private static final FXMLLoader dailyReportLoader = fxmlLoader("views/report/DailyReport.fxml");
    private static final FXMLLoader dueReportLoader = fxmlLoader("views/report/DueReport.fxml");
    private static final FXMLLoader profitReportLoader = fxmlLoader("views/report/ProfitReport.fxml");
    private static final FXMLLoader purchaseReportLoader = fxmlLoader("views/report/PurchaseReport.fxml");
    private static final FXMLLoader salesReportLoader = fxmlLoader("views/report/SalesReport.fxml");
    private static final FXMLLoader salesReturnLoader = fxmlLoader("views/report/SalesReturn.fxml");
    private static final FXMLLoader shippingCostReportLoader = fxmlLoader("views/report/ShippingCostReport.fxml");
    private static final FXMLLoader taxReportLoader = fxmlLoader("views/report/TaxReport.fxml");
    private static final FXMLLoader userSalesReportLoader = fxmlLoader("views/report/UserSalesReport.fxml");

    @Getter
    // Stock Report
    private static BorderPane stockReportPane;

    @Getter
    // Account Report
    private static BorderPane balanceSheetPane;

    @Getter
    private static BorderPane bankBookPane;

    @Getter
    private static BorderPane cashBookPane;

    @Getter
    private static BorderPane coaPrintPane;

    @Getter
    private static BorderPane dayBookPane;

    @Getter
    private static BorderPane expenditureStatementPane;

    @Getter
    private static BorderPane fixedAssetSchedulePane;

    @Getter
    private static BorderPane generalLedgerPane;

    @Getter
    private static BorderPane incomeStatementPane;

    @Getter
    private static BorderPane profitLossPane;

    @Getter
    private static BorderPane receiptAndPaymentPane;

    @Getter
    private static BorderPane subLedgerPane;

    @Getter
    private static BorderPane trialBalancePane;

    @Getter
    private static BorderPane bankReconciliationReportPane;

    @Getter
    // Reports
    private static BorderPane closingPane;

    @Getter
    private static BorderPane closingReportPane;

    @Getter
    private static BorderPane dailyCustomerReportPane;

    @Getter
    private static BorderPane dailyReportPane;

    @Getter
    private static BorderPane dueReportPane;

    @Getter
    private static BorderPane profitReportPane;

    @Getter
    private static BorderPane purchaseReportPane;

    @Getter
    private static BorderPane salesReportPane;

    @Getter
    private static BorderPane salesReturnPane;

    @Getter
    private static BorderPane shippingCostReportPane;

    @Getter
    private static BorderPane taxReportPane;

    @Getter
    private static BorderPane userSalesReportPane;

    private static void setStockReport(Stage stage) {
        stockReportLoader.setControllerFactory(e -> new StockReportController());
    }

    private static void setAccountReports(Stage stage) {
        coaPrintLoader.setControllerFactory(e -> new CoaPrintController());
        dayBookLoader.setControllerFactory(e -> new DayBookController());
        expenditureStatementLoader.setControllerFactory(e -> new ExpenditureStatementController());
        fixedAssetScheduleLoader.setControllerFactory(e -> new FixedAssetScheduleController());
        generalLedgerLoader.setControllerFactory(e -> new GeneralLedgerController());
        incomeStatementLoader.setControllerFactory(e -> new IncomeStatementController());
        profitLossLoader.setControllerFactory(e -> new ProfitLossController());
        receiptAndPaymentLoader.setControllerFactory(e -> new ReceiptAndPaymentController());
        subLedgerLoader.setControllerFactory(e -> new SubLedgerController());
        trialBalanceLoader.setControllerFactory(e -> new TrialBalanceController());
    }

    private static void setReports(Stage stage) {
        closingLoader.setControllerFactory(e -> new ClosingController());
        closingReportLoader.setControllerFactory(e -> new ClosingReportController());
        dailyCustomerReportLoader.setControllerFactory(e -> new DailyCustomerReportController());
        dailyReportLoader.setControllerFactory(e -> new DailyReportController());
        dueReportLoader.setControllerFactory(e -> new DueReportController());
        profitReportLoader.setControllerFactory(e -> new ProfitReportController());
        salesReportLoader.setControllerFactory(e -> new SalesReportController());
        salesReturnLoader.setControllerFactory(e -> new SalesReturnController());
        shippingCostReportLoader.setControllerFactory(e -> new ShippingCostReportController());
        taxReportLoader.setControllerFactory(e -> new TaxReportController());
        userSalesReportLoader.setControllerFactory(e -> new UserSalesReportController());
    }

    public static void setPanes() throws IOException {
        // Stock Report
        stockReportPane = stockReportLoader.load();
        // Account Report
        balanceSheetPane = balanceSheetLoader.load();
        bankBookPane = bankBookLoader.load();
        cashBookPane = cashBookLoader.load();
        coaPrintPane = coaPrintLoader.load();
        dayBookPane = dayBookLoader.load();
        expenditureStatementPane = expenditureStatementLoader.load();
        fixedAssetSchedulePane = fixedAssetScheduleLoader.load();
        generalLedgerPane = generalLedgerLoader.load();
        incomeStatementPane = incomeStatementLoader.load();
        profitLossPane = profitLossLoader.load();
        receiptAndPaymentPane = receiptAndPaymentLoader.load();
        subLedgerPane = subLedgerLoader.load();
        trialBalancePane = trialBalanceLoader.load();
        bankReconciliationReportPane = bankReconciliationReportLoader.load();
        // Reports
        closingPane = closingLoader.load();
        closingReportPane = closingReportLoader.load();
        dailyCustomerReportPane = dailyCustomerReportLoader.load();
        dailyReportPane = dailyReportLoader.load();
        dueReportPane = dueReportLoader.load();
        profitReportPane = profitReportLoader.load();
        purchaseReportPane = purchaseReportLoader.load();
        salesReportPane = salesReportLoader.load();
        salesReturnPane = salesReturnLoader.load();
        shippingCostReportPane = shippingCostReportLoader.load();
        taxReportPane = taxReportLoader.load();
        userSalesReportPane = userSalesReportLoader.load();
    }

    public static void setControllers(Stage stage) {
        setStockReport(stage);
        setAccountReports(stage);
        setReports(stage);
    }
}
