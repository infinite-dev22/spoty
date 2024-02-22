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

package inc.normad.spoty.accounting.navigation;

import inc.normad.spoty.accounting.views.BankReconciliationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;
import inc.normad.spoty.accounting.views.*;

import java.io.IOException;

import static inc.normad.spoty.utils.SpotyResourceLoader.fxmlLoader;

public class Pages {
    // Account
    private static final FXMLLoader bankReconciliationLoader = fxmlLoader("views/account/BankReconciliation.fxml");
    private static final FXMLLoader cashAdjustmentLoader = fxmlLoader("views/account/CashAdjustment.fxml");
    private static final FXMLLoader cashPaymentLoader = fxmlLoader("views/account/CashPayment.fxml");
    private static final FXMLLoader chartOfAccountLoader = fxmlLoader("views/account/ChartOfAccount.fxml");
    private static final FXMLLoader contraVoucherLoader = fxmlLoader("views/account/ContraVoucher.fxml");
    private static final FXMLLoader creditVoucherLoader = fxmlLoader("views/account/CreditVoucher.fxml");
    private static final FXMLLoader customerReceiveLoader = fxmlLoader("views/account/CustomerReceive.fxml");
    private static final FXMLLoader debitVoucherLoader = fxmlLoader("views/account/DebitVoucher.fxml");
    private static final FXMLLoader financialYearLoader = fxmlLoader("views/account/FinancialYear.fxml");
    private static final FXMLLoader journalVoucherLoader = fxmlLoader("views/account/JournalVoucher.fxml");
    private static final FXMLLoader openingBalanceLoader = fxmlLoader("views/account/OpeningBalance.fxml");
    private static final FXMLLoader paymentMethodsLoader = fxmlLoader("views/account/PaymentMethods.fxml");
    private static final FXMLLoader preDefinedAccountsLoader = fxmlLoader("views/account/PreDefinedAccounts.fxml");
    private static final FXMLLoader servicePaymentLoader = fxmlLoader("views/account/ServicePayment.fxml");
    private static final FXMLLoader subAccountLoader = fxmlLoader("views/account/SubAccount.fxml");
    private static final FXMLLoader supplierPaymentLoader = fxmlLoader("views/account/SupplierPayment.fxml");
    private static final FXMLLoader voucherApprovalLoader = fxmlLoader("views/account/VoucherApproval.fxml");

    @Getter
    // Account
    private static BorderPane bankReconciliationPane;

    @Getter
    private static BorderPane cashAdjustmentPane;

    @Getter
    private static BorderPane cashPaymentPane;

    @Getter
    private static BorderPane chartOfAccountPane;

    @Getter
    private static BorderPane contraVoucherPane;

    @Getter
    private static BorderPane creditVoucherPane;

    @Getter
    private static BorderPane customerReceivePane;

    @Getter
    private static BorderPane debitVoucherPane;

    @Getter
    private static BorderPane financialYearPane;

    @Getter
    private static BorderPane journalVoucherPane;

    @Getter
    private static BorderPane openingBalancePane;

    @Getter
    private static BorderPane paymentMethodsPane;

    @Getter
    private static BorderPane preDefinedAccountsPane;

    @Getter
    private static BorderPane servicePaymentPane;

    @Getter
    private static BorderPane subAccountPane;

    @Getter
    private static BorderPane supplierPaymentPane;

    @Getter
    private static BorderPane voucherApprovalPane;

    private static void setAccounts(Stage stage) {
        bankReconciliationLoader.setControllerFactory(e -> new BankReconciliationController());
        cashAdjustmentLoader.setControllerFactory(e -> new CashAdjustmentController());
        cashPaymentLoader.setControllerFactory(e -> new CashPaymentController());
        chartOfAccountLoader.setControllerFactory(e -> new ChartOfAccountController());
        contraVoucherLoader.setControllerFactory(e -> new ContraVoucherController());
        creditVoucherLoader.setControllerFactory(e -> new CreditVoucherController());
        customerReceiveLoader.setControllerFactory(e -> new CustomerReceiveController());
        debitVoucherLoader.setControllerFactory(e -> new DebitVoucherController());
        financialYearLoader.setControllerFactory(e -> new FinancialYearController());
        journalVoucherLoader.setControllerFactory(e -> new JournalVoucherController());
        openingBalanceLoader.setControllerFactory(e -> new OpeningBalanceController());
        paymentMethodsLoader.setControllerFactory(e -> new PaymentMethodsController());
        preDefinedAccountsLoader.setControllerFactory(e -> new PreDefinedAccountsController());
        servicePaymentLoader.setControllerFactory(e -> new ServicePaymentController());
        subAccountLoader.setControllerFactory(e -> new SubAccountController());
        supplierPaymentLoader.setControllerFactory(e -> new ServicePaymentController());
        voucherApprovalLoader.setControllerFactory(e -> new VoucherApprovalController());
    }

    public static void setPanes() throws IOException {
        // Account
        bankReconciliationPane = bankReconciliationLoader.load();
        cashAdjustmentPane = cashAdjustmentLoader.load();
        cashPaymentPane = cashPaymentLoader.load();
        chartOfAccountPane = chartOfAccountLoader.load();
        contraVoucherPane = contraVoucherLoader.load();
        creditVoucherPane = creditVoucherLoader.load();
        customerReceivePane = customerReceiveLoader.load();
        debitVoucherPane = debitVoucherLoader.load();
        financialYearPane = financialYearLoader.load();
        journalVoucherPane = journalVoucherLoader.load();
        openingBalancePane = openingBalanceLoader.load();
        paymentMethodsPane = paymentMethodsLoader.load();
        preDefinedAccountsPane = preDefinedAccountsLoader.load();
        servicePaymentPane = servicePaymentLoader.load();
        subAccountPane = subAccountLoader.load();
        supplierPaymentPane = supplierPaymentLoader.load();
        voucherApprovalPane = voucherApprovalLoader.load();
    }

    public static void setControllers(Stage stage) {
        setAccounts(stage);
    }
}
