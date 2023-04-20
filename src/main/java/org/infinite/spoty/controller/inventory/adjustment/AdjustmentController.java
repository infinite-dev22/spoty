package org.infinite.spoty.controller.inventory.adjustment;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.infinite.spoty.model.Adjustment;
import org.infinite.spoty.model.Adjustment;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.data.SampleData.adjustmentSampleData;

public class AdjustmentController implements Initializable {
    
    private MFXTableView<Adjustment> adjustmentsTable;
    
    @FXML
    public BorderPane adjustmentContentPane;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> Platform.runLater(() -> {
            adjustmentContentPane.setCenter(getAdjustmentTable());
            setupTable();
        })).start();
    }

    private void setupTable() {
        MFXTableColumn<Adjustment> adjustmentDate = new MFXTableColumn<>("Date", true, Comparator.comparing(Adjustment::getAdjustmentDate));
        MFXTableColumn<Adjustment> adjustmentReference = new MFXTableColumn<>("Reference", true, Comparator.comparing(Adjustment::getAdjustmentReference));
        MFXTableColumn<Adjustment> adjustmentWarehouse = new MFXTableColumn<>("Warehouse", true, Comparator.comparing(Adjustment::getAdjustmentWarehouse));
        MFXTableColumn<Adjustment> adjustmentTotalProducts = new MFXTableColumn<>("Total Products", true, Comparator.comparing(Adjustment::getAdjustmentTotalProducts));

        adjustmentDate.setRowCellFactory(adjustment -> new MFXTableRowCell<>(Adjustment::getAdjustmentDate));
        adjustmentReference.setRowCellFactory(adjustment -> new MFXTableRowCell<>(Adjustment::getAdjustmentReference));
        adjustmentWarehouse.setRowCellFactory(adjustment -> new MFXTableRowCell<>(Adjustment::getAdjustmentWarehouse));
        adjustmentTotalProducts.setRowCellFactory(adjustment -> new MFXTableRowCell<>(Adjustment::getAdjustmentTotalProducts));

        adjustmentsTable.getTableColumns().addAll(adjustmentDate, adjustmentReference, adjustmentWarehouse, adjustmentTotalProducts);
        adjustmentsTable.getFilters().addAll(
                new StringFilter<>("Reference", Adjustment::getAdjustmentReference),
                new StringFilter<>("Warehouse", Adjustment::getAdjustmentWarehouse),
                new DoubleFilter<>("Total Products", Adjustment::getAdjustmentTotalProducts)
        );

        adjustmentsTable.setItems(adjustmentSampleData());
    }

    private MFXTableView<Adjustment> getAdjustmentTable() {
        adjustmentsTable = new MFXTableView<>();
        adjustmentsTable.setPrefSize(1000, 1000);
        adjustmentsTable.features().enableBounceEffect();
        adjustmentsTable.autosizeColumnsOnInitialization();
        adjustmentsTable.features().enableSmoothScrolling(0.5);
        return adjustmentsTable;
    }
}
