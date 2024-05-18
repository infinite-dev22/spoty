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

package inc.nomard.spoty.core.startup;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.core.views.printable.general.*;
import io.github.palexdev.materialfx.dialogs.*;
import java.io.*;
import javafx.fxml.*;

public class Dialogs {
    private static final FXMLLoader quotationDetailFormLoader = fxmlLoader("views/forms/QuotationDetailForm.fxml");
    private static final FXMLLoader purchaseDetailFormLoader = fxmlLoader("views/forms/PurchaseDetailForm.fxml");
    private static final FXMLLoader adjustmentDetailFormLoader = fxmlLoader("views/forms/AdjustmentDetailForm.fxml");
    private static final FXMLLoader requisitionDetailFormLoader = fxmlLoader("views/forms/RequisitionDetailForm.fxml");
    private static final FXMLLoader saleDetailFormLoader = fxmlLoader("views/forms/SaleDetailForm.fxml");
    private static final FXMLLoader stockInDetailFormLoader = fxmlLoader("views/forms/StockInDetailForm.fxml");
    private static final FXMLLoader transferDetailFormLoader = fxmlLoader("views/forms/TransferDetailForm.fxml");
    private static final FXMLLoader bankFormLoader = fxmlLoader("views/forms/BankForm.fxml");
    private static final FXMLLoader beneficiaryTypeFormLoader = fxmlLoader("views/forms/BeneficiaryTypeForm.fxml");
    private static final FXMLLoader beneficiaryBadgeFormLoader = fxmlLoader("views/forms/BeneficiaryBadgeForm.fxml");
    private static final FXMLLoader branchFormLoader = fxmlLoader("views/forms/BranchForm.fxml");
    private static final FXMLLoader brandFormLoader = fxmlLoader("views/forms/BrandForm.fxml");
    private static final FXMLLoader currencyFormLoader = fxmlLoader("views/forms/CurrencyForm.fxml");
    private static final FXMLLoader customerFormLoader = fxmlLoader("views/forms/CustomerForm.fxml");
    private static final FXMLLoader designationFormLoader = fxmlLoader("views/forms/DesignationForm.fxml");
    private static final FXMLLoader emailFormLoader = fxmlLoader("views/forms/EmailForm.fxml");
    private static final FXMLLoader employmentStatusFormLoader = fxmlLoader("views/forms/EmploymentStatusForm.fxml");
    private static final FXMLLoader expenseCategoryFormLoader = fxmlLoader("views/forms/ExpenseCategoryForm.fxml");
    private static final FXMLLoader expenseFormLoader = fxmlLoader("views/forms/ExpenseForm.fxml");
    private static final FXMLLoader leaveRequestFormLoader = fxmlLoader("views/forms/LeaveRequestForm.fxml");
    private static final FXMLLoader productCategoryFormLoader = fxmlLoader("views/forms/ProductCategoryForm.fxml");
    private static final FXMLLoader productFormLoader = fxmlLoader("views/forms/ProductForm.fxml");
    private static final FXMLLoader roleFormLoader = fxmlLoader("views/forms/RoleForm.fxml");
    private static final FXMLLoader salaryAdvanceFormLoader = fxmlLoader("views/forms/SalaryAdvanceForm.fxml");
    private static final FXMLLoader supplierFormLoader = fxmlLoader("views/forms/SupplierForm.fxml");
    private static final FXMLLoader taxFormLoader = fxmlLoader("views/forms/TaxForm.fxml");
    private static final FXMLLoader uomFormLoader = fxmlLoader("views/forms/UOMForm.fxml");
    private static final FXMLLoader userFormLoader = fxmlLoader("views/forms/UserForm.fxml");
    private static final FXMLLoader printableLoader = fxmlLoader("views/printable/general/General.fxml");

    private static MFXGenericDialog quotationDialogContent;
    private static MFXGenericDialog printableDialogContent;

    public static void setControllers() {
        quotationDetailFormLoader.setControllerFactory(c -> QuotationDetailFormController.getInstance());
        purchaseDetailFormLoader.setControllerFactory(c -> PurchaseDetailFormController.getInstance());
        printableLoader.setControllerFactory(c -> GeneralViewController.getInstance());
    }

    public static void setDialogContent() throws IOException {
        quotationDialogContent = quotationDetailFormLoader.load();
        quotationDialogContent.setShowMinimize(false);
        quotationDialogContent.setShowAlwaysOnTop(false);
        quotationDialogContent.setShowClose(false);

        printableDialogContent = printableLoader.load();
    }

    public static MFXGenericDialog getQuotationDialogContent() {
        quotationDialogContent.setShowMinimize(false);
        quotationDialogContent.setShowAlwaysOnTop(false);
        return quotationDialogContent;
    }

    public static MFXGenericDialog getGeneralPrintableFxmlLoader() {
        printableDialogContent.setShowMinimize(false);
        printableDialogContent.setShowAlwaysOnTop(false);
        return printableDialogContent;
    }
}
