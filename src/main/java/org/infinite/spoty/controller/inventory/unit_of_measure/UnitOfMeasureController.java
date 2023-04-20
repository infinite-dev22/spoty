package org.infinite.spoty.controller.inventory.unit_of_measure;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.infinite.spoty.model.UnitOfMeasure;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.data.SampleData.brandSampleData;
import static org.infinite.spoty.data.SampleData.uomSampleData;

public class UnitOfMeasureController implements Initializable {

    public MFXTableView<UnitOfMeasure> uomTable;
    
    @FXML
    public BorderPane uomContentPane;

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
            uomContentPane.setCenter(getUnitOfMeasureTable());
            setupTable();

            uomTable.autosizeColumnsOnInitialization();
        })).start();
    }

    private void setupTable() {
        MFXTableColumn<UnitOfMeasure> uomName = new MFXTableColumn<>("Name", true, Comparator.comparing(UnitOfMeasure::getUomName));
        MFXTableColumn<UnitOfMeasure> uomShortName = new MFXTableColumn<>("Short Name", true, Comparator.comparing(UnitOfMeasure::getUomShortName));
        MFXTableColumn<UnitOfMeasure> uomBaseUnit = new MFXTableColumn<>("Base Unit", true, Comparator.comparing(UnitOfMeasure::getUomBaseUnit));
        MFXTableColumn<UnitOfMeasure> uomOperator = new MFXTableColumn<>("Operator", true, Comparator.comparing(UnitOfMeasure::getUomOperator));
        MFXTableColumn<UnitOfMeasure> uomOperationValue = new MFXTableColumn<>("Operation Value", true, Comparator.comparing(UnitOfMeasure::getUomOperationValue));

        uomName.setRowCellFactory(brand -> new MFXTableRowCell<>(UnitOfMeasure::getUomName));
        uomShortName.setRowCellFactory(brand -> new MFXTableRowCell<>(UnitOfMeasure::getUomShortName));
        uomBaseUnit.setRowCellFactory(brand -> new MFXTableRowCell<>(UnitOfMeasure::getUomBaseUnit));
        uomOperator.setRowCellFactory(brand -> new MFXTableRowCell<>(UnitOfMeasure::getUomOperator));
        uomOperationValue.setRowCellFactory(brand -> new MFXTableRowCell<>(UnitOfMeasure::getUomOperationValue));

        uomTable.getTableColumns().addAll(uomName, uomShortName, uomBaseUnit, uomOperator, uomOperationValue);
        uomTable.getFilters().addAll(
                new StringFilter<>("Name", UnitOfMeasure::getUomName),
                new StringFilter<>("Short Name", UnitOfMeasure::getUomShortName),
                new StringFilter<>("Base Unit", UnitOfMeasure::getUomBaseUnit),
                new StringFilter<>("Operator", UnitOfMeasure::getUomOperator),
                new DoubleFilter<>("Operation Value", UnitOfMeasure::getUomOperationValue)
        );

        uomTable.setItems(uomSampleData());
    }

    private MFXTableView<UnitOfMeasure> getUnitOfMeasureTable() {
        uomTable = new MFXTableView<>();
        uomTable.setPrefSize(1000, 1000);
        uomTable.features().enableBounceEffect();
        uomTable.features().enableSmoothScrolling(0.5);
        return uomTable;
    }
}
