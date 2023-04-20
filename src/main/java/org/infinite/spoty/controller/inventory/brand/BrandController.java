package org.infinite.spoty.controller.inventory.brand;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.util.converter.ByteStringConverter;
import org.infinite.spoty.model.Brand;
import org.infinite.spoty.model.Category;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.data.SampleData.brandSampleData;

public class BrandController implements Initializable {

    public MFXTableView<Brand> brandTable;

    @FXML
    public BorderPane brandContentPane;

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
            brandContentPane.setCenter(getBrandTable());
            setupTable();
        })).start();
    }

    private void setupTable() {
        MFXTableColumn<Brand> brandName = new MFXTableColumn<>("Name", true, Comparator.comparing(Brand::getBrandName));
        MFXTableColumn<Brand> brandDescription = new MFXTableColumn<>("Description", true, Comparator.comparing(Brand::getBrandDescription));

        brandName.setRowCellFactory(brand -> new MFXTableRowCell<>(Brand::getBrandName));
        brandName.setRowCellFactory(brand -> new MFXTableRowCell<>(Brand::getBrandDescription));

        brandTable.getTableColumns().addAll(brandName, brandDescription);
        brandTable.getFilters().addAll(
                new StringFilter<>("Name", Brand::getBrandName),
                new StringFilter<>("Description", Brand::getBrandDescription)
        );

        brandTable.setItems(brandSampleData());
    }

    private MFXTableView<Brand> getBrandTable() {
        brandTable = new MFXTableView<>();
        brandTable.setPrefSize(1000, 1000);
        brandTable.autosizeColumnsOnInitialization();
        brandTable.features().enableBounceEffect();
        brandTable.features().enableSmoothScrolling(0.5);
        return brandTable;
    }
}
