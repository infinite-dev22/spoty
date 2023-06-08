package org.infinite.spoty.views.inventory.quotation;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.infinite.spoty.database.dao.QuotationMasterDao;
import org.infinite.spoty.database.models.QuotationMaster;
import org.infinite.spoty.forms.QuotationFormController;
import org.infinite.spoty.viewModels.QuotationMasterViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

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
    private MFXTableView<QuotationMaster> quotationsTable;

    public QuotationController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<QuotationMaster> quotationDate = new MFXTableColumn<>("Date", false, Comparator.comparing(QuotationMaster::getDate));
        MFXTableColumn<QuotationMaster> quotationReference = new MFXTableColumn<>("Reference", false, Comparator.comparing(QuotationMaster::getRef));
        MFXTableColumn<QuotationMaster> quotationCustomer = new MFXTableColumn<>("Customer", false, Comparator.comparing(QuotationMaster::getCustomerName));
        MFXTableColumn<QuotationMaster> quotationBranch = new MFXTableColumn<>("Branch", false, Comparator.comparing(QuotationMaster::getBranchName));
        MFXTableColumn<QuotationMaster> quotationStatus = new MFXTableColumn<>("Status", false, Comparator.comparing(QuotationMaster::getStatus));
        MFXTableColumn<QuotationMaster> quotationGrandTotal = new MFXTableColumn<>("Grand Total", false, Comparator.comparing(QuotationMaster::getTotal));

        quotationDate.setRowCellFactory(quotation -> new MFXTableRowCell<>(QuotationMaster::getDate));
        quotationReference.setRowCellFactory(quotation -> new MFXTableRowCell<>(QuotationMaster::getRef));
        quotationCustomer.setRowCellFactory(quotation -> new MFXTableRowCell<>(QuotationMaster::getCustomerName));
        quotationBranch.setRowCellFactory(quotation -> new MFXTableRowCell<>(QuotationMaster::getBranchName));
        quotationStatus.setRowCellFactory(quotation -> new MFXTableRowCell<>(QuotationMaster::getStatus));
        quotationGrandTotal.setRowCellFactory(quotation -> new MFXTableRowCell<>(QuotationMaster::getTotal));

        quotationDate.prefWidthProperty().bind(quotationsTable.widthProperty().multiply(.17));
        quotationReference.prefWidthProperty().bind(quotationsTable.widthProperty().multiply(.17));
        quotationCustomer.prefWidthProperty().bind(quotationsTable.widthProperty().multiply(.17));
        quotationBranch.prefWidthProperty().bind(quotationsTable.widthProperty().multiply(.17));
        quotationStatus.prefWidthProperty().bind(quotationsTable.widthProperty().multiply(.17));
        quotationGrandTotal.prefWidthProperty().bind(quotationsTable.widthProperty().multiply(.17));

        quotationsTable.getTableColumns().addAll(quotationDate, quotationReference, quotationCustomer, quotationBranch, quotationStatus, quotationGrandTotal);
        quotationsTable.getFilters().addAll(
                new StringFilter<>("Reference", QuotationMaster::getRef),
                new StringFilter<>("Customer", QuotationMaster::getCustomerName),
                new StringFilter<>("Branch", QuotationMaster::getBranchName),
                new StringFilter<>("Status", QuotationMaster::getStatus),
                new DoubleFilter<>("Grand Total", QuotationMaster::getTotal)
        );
        getQuotationMasterTable();
        quotationsTable.setItems(QuotationMasterViewModel.getQuotationMasters());
    }

    private void getQuotationMasterTable() {
        quotationsTable.setPrefSize(1000, 1000);
        quotationsTable.features().enableBounceEffect();
        quotationsTable.features().enableSmoothScrolling(0.5);

        quotationsTable.setTableRowFactory(t -> {
            MFXTableRow<QuotationMaster> row = new MFXTableRow<>(quotationsTable, t);
            EventHandler<ContextMenuEvent> eventHandler = event -> {
                showContextMenu((MFXTableRow<QuotationMaster>) event.getSource()).show(quotationsTable.getParent(), event.getScreenX(), event.getScreenY());
                event.consume();
            };
            row.setOnContextMenuRequested(eventHandler);
            return row;
        });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<QuotationMaster> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(quotationsTable);
        MFXContextMenuItem view = new MFXContextMenuItem("View");
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(e -> {
            QuotationMasterDao.deleteQuotationMaster(obj.getData().getId());
            QuotationMasterViewModel.getQuotationMasters();
            e.consume();
        });
        // Edit
        edit.setOnAction(e -> {
            QuotationMasterViewModel.getItem(obj.getData().getId());
            quotationCreateBtnClicked();
            e.consume();
        });

        contextMenu.addItems(view, edit, delete);

        return contextMenu;
    }

    public void quotationCreateBtnClicked() {
        FXMLLoader loader = fxmlLoader("forms/QuotationForm.fxml");
        loader.setControllerFactory(c -> new QuotationFormController(stage));
        try {
            AnchorPane productFormPane = loader.load();
            ((StackPane) quotationContentPane.getParent()).getChildren().add(productFormPane);
            ((StackPane) quotationContentPane.getParent()).getChildren().get(0).setVisible(false);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
