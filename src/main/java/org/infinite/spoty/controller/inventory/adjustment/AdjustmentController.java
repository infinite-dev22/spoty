package org.infinite.spoty.controller.inventory.adjustment;

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
import org.infinite.spoty.forms.AdjustmentFormController;
import org.infinite.spoty.model.Adjustment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.data.SampleData.adjustmentSampleData;

public class AdjustmentController implements Initializable {
    private final Stage stage;
    @FXML
    public BorderPane adjustmentContentPane;
    @FXML
    public MFXTextField adjustmentSearchBar;
    @FXML
    public HBox adjustmentActionsPane;
    @FXML
    public MFXButton adjustmentImportBtn;
    @FXML
    private MFXTableView<Adjustment> adjustmentsTable;

    public AdjustmentController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Adjustment> adjustmentDate = new MFXTableColumn<>("Date", true, Comparator.comparing(Adjustment::getAdjustmentDate));
        MFXTableColumn<Adjustment> adjustmentReference = new MFXTableColumn<>("Reference", true, Comparator.comparing(Adjustment::getAdjustmentReference));
        MFXTableColumn<Adjustment> adjustmentBranch = new MFXTableColumn<>("Branch", true, Comparator.comparing(Adjustment::getAdjustmentBranch));
        MFXTableColumn<Adjustment> adjustmentTotalProducts = new MFXTableColumn<>("Total Products", true, Comparator.comparing(Adjustment::getAdjustmentTotalProducts));

        adjustmentDate.setRowCellFactory(adjustment -> new MFXTableRowCell<>(Adjustment::getAdjustmentDate));
        adjustmentReference.setRowCellFactory(adjustment -> new MFXTableRowCell<>(Adjustment::getAdjustmentReference));
        adjustmentBranch.setRowCellFactory(adjustment -> new MFXTableRowCell<>(Adjustment::getAdjustmentBranch));
        adjustmentTotalProducts.setRowCellFactory(adjustment -> new MFXTableRowCell<>(Adjustment::getAdjustmentTotalProducts));

        adjustmentsTable.getTableColumns().addAll(adjustmentDate, adjustmentReference, adjustmentBranch, adjustmentTotalProducts);
        adjustmentsTable.getFilters().addAll(
                new StringFilter<>("Reference", Adjustment::getAdjustmentReference),
                new StringFilter<>("Branch", Adjustment::getAdjustmentBranch),
                new DoubleFilter<>("Total Products", Adjustment::getAdjustmentTotalProducts)
        );
        getAdjustmentTable();
        adjustmentsTable.setItems(adjustmentSampleData());
    }

    private void getAdjustmentTable() {
        adjustmentsTable.setPrefSize(1000, 1000);
        adjustmentsTable.features().enableBounceEffect();
        adjustmentsTable.autosizeColumnsOnInitialization();
        adjustmentsTable.features().enableSmoothScrolling(0.5);
    }

    public void adjustmentCreateBtnClicked() {
        FXMLLoader loader = fxmlLoader("forms/AdjustmentForm.fxml");
        loader.setControllerFactory(c -> new AdjustmentFormController(stage));
        Logger logger = LoggerFactory.getLogger(this.getClass());

        try {
            AnchorPane productFormPane = loader.load();
            ((StackPane) adjustmentContentPane.getParent()).getChildren().add(productFormPane);
            ((StackPane) adjustmentContentPane.getParent()).getChildren().get(0).setVisible(false);
        } catch (IOException ex) {
            logger.error(ex.getLocalizedMessage());
        }
    }
}
