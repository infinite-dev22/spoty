package org.infinite.spoty.controller.inventory.unit_of_measure;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.model.UnitOfMeasure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.data.SampleData.uomSampleData;

public class UnitOfMeasureController implements Initializable {
    @FXML
    public MFXTableView<UnitOfMeasure> uomTable;
    @FXML
    public MFXTextField uomSearchBar;
    @FXML
    public HBox uomActionsPane;
    @FXML
    public MFXButton uomImportBtn;
    private Dialog<ButtonType> dialog;

    public UnitOfMeasureController(Stage stage) {
        Platform.runLater(() -> {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            try {
                uomFormDialogPane(stage);
            } catch (IOException ex) {
                logger.error(ex.getLocalizedMessage());
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
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
        getUnitOfMeasureTable();
        uomTable.setItems(uomSampleData());
    }

    private void getUnitOfMeasureTable() {
        uomTable.setPrefSize(1000, 1000);
        uomTable.features().enableBounceEffect();
        uomTable.features().enableSmoothScrolling(0.5);
        uomTable.autosizeColumnsOnInitialization();
    }

    private void uomFormDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/UOMForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }

    public void uomCreateBtnClicked() {
        dialog.showAndWait();
    }
}
