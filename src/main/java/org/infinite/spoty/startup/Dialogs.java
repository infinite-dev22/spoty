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

package org.infinite.spoty.startup;

import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import javafx.fxml.FXMLLoader;
import org.infinite.spoty.views.forms.QuotationDetailFormController;
import org.infinite.spoty.views.printable.general.GeneralViewController;

import java.io.IOException;

import static org.infinite.spoty.SpotyResourceLoader.fxmlLoader;

public class Dialogs {
    private static final FXMLLoader fxmlLoader = fxmlLoader("views/forms/QuotationDetailForm.fxml");
    private static final FXMLLoader printableLoader = fxmlLoader("views/printable/general/General.fxml");

    private static MFXGenericDialog quotationDialogContent;
    private static MFXGenericDialog printableDialogContent;

    public static void setControllers() {
        fxmlLoader.setControllerFactory(c -> QuotationDetailFormController.getInstance());
        printableLoader.setControllerFactory(c -> GeneralViewController.getInstance());
    }

    public static void setDialogContent() throws IOException {
        quotationDialogContent = fxmlLoader.load();
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
