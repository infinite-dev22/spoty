package org.infinite.spoty.controller.inventory.quotation;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.infinite.spoty.SpotResourceLoader;
import org.infinite.spoty.forms.QuotationFormController;
import org.infinite.spoty.model.Quotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.data.SampleData.quotationSampleData;

public class QuotationController implements Initializable {
    private final Stage stage;
    @FXML
    public MFXTextField quotationSearchBar;
    @FXML
    public HBox quotationActionsPane;
    @FXML
    public MFXButton quotationImportBtn;
    @FXML
    public BorderPane quotationContentPane;
    @FXML
    private MFXTableView<Quotation> quotationsTable;

    public QuotationController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Quotation> quotationDate = new MFXTableColumn<>("Date", true, Comparator.comparing(Quotation::getQuotationDate));
        MFXTableColumn<Quotation> quotationReference = new MFXTableColumn<>("Reference", true, Comparator.comparing(Quotation::getQuotationReference));
        MFXTableColumn<Quotation> quotationCustomer = new MFXTableColumn<>("Customer", true, Comparator.comparing(Quotation::getQuotationCustomer));
        MFXTableColumn<Quotation> quotationBranch = new MFXTableColumn<>("Branch", true, Comparator.comparing(Quotation::getQuotationBranch));
        MFXTableColumn<Quotation> quotationStatus = new MFXTableColumn<>("Status", true, Comparator.comparing(Quotation::getQuotationStatus));
        MFXTableColumn<Quotation> quotationGrandTotal = new MFXTableColumn<>("Grand Total", true, Comparator.comparing(Quotation::getQuotationGrandTotal));

        quotationDate.setRowCellFactory(quotation -> new MFXTableRowCell<>(Quotation::getQuotationDate));
        quotationReference.setRowCellFactory(quotation -> new MFXTableRowCell<>(Quotation::getQuotationReference));
        quotationCustomer.setRowCellFactory(quotation -> new MFXTableRowCell<>(Quotation::getQuotationCustomer));
        quotationBranch.setRowCellFactory(quotation -> new MFXTableRowCell<>(Quotation::getQuotationBranch));
        quotationStatus.setRowCellFactory(quotation -> new MFXTableRowCell<>(Quotation::getQuotationStatus));
        quotationGrandTotal.setRowCellFactory(quotation -> new MFXTableRowCell<>(Quotation::getQuotationGrandTotal));

        quotationsTable.getTableColumns().addAll(quotationDate, quotationReference, quotationCustomer, quotationBranch, quotationStatus, quotationGrandTotal);
        quotationsTable.getFilters().addAll(
                new StringFilter<>("Reference", Quotation::getQuotationReference),
                new StringFilter<>("Customer", Quotation::getQuotationCustomer),
                new StringFilter<>("Branch", Quotation::getQuotationBranch),
                new StringFilter<>("Status", Quotation::getQuotationStatus),
                new DoubleFilter<>("Grand Total", Quotation::getQuotationGrandTotal)
        );
        getQuotationTable();
        quotationsTable.setItems(quotationSampleData());
    }

    private void getQuotationTable() {
        quotationsTable.setPrefSize(1000, 1000);
        quotationsTable.features().enableBounceEffect();
        quotationsTable.autosizeColumnsOnInitialization();
        quotationsTable.features().enableSmoothScrolling(0.5);
    }

    public void quotationCreateBtnClicked() {
        FXMLLoader loader = fxmlLoader("forms/QuotationForm.fxml");
        loader.setControllerFactory(c -> new QuotationFormController(stage));
        Logger logger = LoggerFactory.getLogger(this.getClass());

        try {
            AnchorPane productFormPane = loader.load();
            ((StackPane) quotationContentPane.getParent()).getChildren().add(productFormPane);
            ((StackPane) quotationContentPane.getParent()).getChildren().get(0).setVisible(false);
        } catch (IOException ex) {
            logger.error(ex.getLocalizedMessage());
        }
    }
}
