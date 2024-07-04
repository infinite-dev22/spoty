package inc.nomard.spoty.core.startup;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.views.forms.*;
import io.github.palexdev.materialfx.dialogs.*;
import java.io.*;
import javafx.fxml.*;
import javafx.stage.*;
import lombok.extern.java.*;

@Log
public class Dialogs {
    private static final FXMLLoader quotationDetailFormLoader = fxmlLoader("views/forms/QuotationDetailForm.fxml");

    private static MFXGenericDialog quotationDialogContent;
    private static MFXGenericDialog printableDialogContent;

    public static void setControllers(Stage stage) {
        quotationDetailFormLoader.setControllerFactory(c -> new QuotationDetailFormController());
    }

    public static void setDialogContent() throws IOException {
        quotationDialogContent = quotationDetailFormLoader.load();
        quotationDialogContent.setShowMinimize(false);
        quotationDialogContent.setShowAlwaysOnTop(false);
        quotationDialogContent.setShowClose(false);
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
