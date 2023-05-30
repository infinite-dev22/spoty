package org.infinite.spoty.views.inventory.adjustment;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
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
import org.infinite.spoty.database.models.AdjustmentMaster;
import org.infinite.spoty.forms.AdjustmentFormController;
import org.infinite.spoty.viewModels.AdjustmentMasterViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

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
    private MFXTableView<AdjustmentMaster> adjustmentsTable;

    public AdjustmentController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<AdjustmentMaster> adjustmentDate = new MFXTableColumn<>("Date", false, Comparator.comparing(AdjustmentMaster::getDate));
        MFXTableColumn<AdjustmentMaster> adjustmentReference = new MFXTableColumn<>("Reference", false, Comparator.comparing(AdjustmentMaster::getRef));
        MFXTableColumn<AdjustmentMaster> adjustmentBranch = new MFXTableColumn<>("Branch", false, Comparator.comparing(AdjustmentMaster::getBranchName));
//        MFXTableColumn<AdjustmentMaster> adjustmentTotalProducts = new MFXTableColumn<>("Total Products", false, Comparator.comparing(AdjustmentMaster::getAdjustmentMasterTotalProducts));

        adjustmentDate.setRowCellFactory(adjustment -> new MFXTableRowCell<>(AdjustmentMaster::getDate));
        adjustmentReference.setRowCellFactory(adjustment -> new MFXTableRowCell<>(AdjustmentMaster::getRef));
        adjustmentBranch.setRowCellFactory(adjustment -> new MFXTableRowCell<>(AdjustmentMaster::getBranchName));

        adjustmentDate.prefWidthProperty().bind(adjustmentsTable.widthProperty().multiply(.4));
        adjustmentReference.prefWidthProperty().bind(adjustmentsTable.widthProperty().multiply(.4));
        adjustmentBranch.prefWidthProperty().bind(adjustmentsTable.widthProperty().multiply(.4));
//        adjustmentTotalProducts.setRowCellFactory(adjustment -> new MFXTableRowCell<>(AdjustmentMaster::getAdjustmentMasterTotalProducts));

        adjustmentsTable.getTableColumns().addAll(adjustmentDate, adjustmentReference, adjustmentBranch); //, adjustmentTotalProducts);
        adjustmentsTable.getFilters().addAll(
                new StringFilter<>("Reference", AdjustmentMaster::getRef),
                new StringFilter<>("Branch", AdjustmentMaster::getBranchName)
//                new DoubleFilter<>("Total Products", AdjustmentMaster::getAdjustmentMasterTotalProducts)
        );
        getAdjustmentMasterTable();
        adjustmentsTable.setItems(AdjustmentMasterViewModel.getAdjustmentMasters());
    }

    private void getAdjustmentMasterTable() {
        adjustmentsTable.setPrefSize(1000, 1000);
        adjustmentsTable.features().enableBounceEffect();
        adjustmentsTable.autosizeColumnsOnInitialization();
        adjustmentsTable.features().enableSmoothScrolling(0.5);
    }

    public void adjustmentCreateBtnClicked() {
        FXMLLoader loader = fxmlLoader("forms/AdjustmentMasterForm.fxml");
        loader.setControllerFactory(c -> new AdjustmentFormController(stage));

        try {
            AnchorPane productFormPane = loader.load();
            ((StackPane) adjustmentContentPane.getParent()).getChildren().add(productFormPane);
            ((StackPane) adjustmentContentPane.getParent()).getChildren().get(0).setVisible(false);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
