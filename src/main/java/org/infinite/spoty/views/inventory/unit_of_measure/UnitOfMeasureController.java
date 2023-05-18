package org.infinite.spoty.views.inventory.unit_of_measure;

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
import org.infinite.spoty.database.models.UnitOfMeasure;
import org.infinite.spoty.viewModels.UOMFormViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

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
            try {
                uomFormDialogPane(stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<UnitOfMeasure> uomName = new MFXTableColumn<>("Name", true, Comparator.comparing(UnitOfMeasure::getName));
        MFXTableColumn<UnitOfMeasure> uomShortName = new MFXTableColumn<>("Short Name", true, Comparator.comparing(UnitOfMeasure::getShortName));
        MFXTableColumn<UnitOfMeasure> uomBaseUnit = new MFXTableColumn<>("Base Unit", true, Comparator.comparing(UnitOfMeasure::getBaseUnitName));
        MFXTableColumn<UnitOfMeasure> uomOperator = new MFXTableColumn<>("Operator", true, Comparator.comparing(UnitOfMeasure::getOperator));
        MFXTableColumn<UnitOfMeasure> uomOperationValue = new MFXTableColumn<>("Operation Value", true, Comparator.comparing(UnitOfMeasure::getOperatorValue));

        uomName.setRowCellFactory(brand -> new MFXTableRowCell<>(UnitOfMeasure::getName));
        uomShortName.setRowCellFactory(brand -> new MFXTableRowCell<>(UnitOfMeasure::getShortName));
        uomBaseUnit.setRowCellFactory(brand -> new MFXTableRowCell<>(UnitOfMeasure::getBaseUnit));
        uomOperator.setRowCellFactory(brand -> new MFXTableRowCell<>(UnitOfMeasure::getOperator));
        uomOperationValue.setRowCellFactory(brand -> new MFXTableRowCell<>(UnitOfMeasure::getOperatorValue));

        uomTable.getTableColumns().addAll(uomName, uomShortName, uomBaseUnit, uomOperator, uomOperationValue);
        uomTable.getFilters().addAll(
                new StringFilter<>("Name", UnitOfMeasure::getName),
                new StringFilter<>("Short Name", UnitOfMeasure::getShortName),
                new StringFilter<>("Base Unit", UnitOfMeasure::getBaseUnitName),
                new StringFilter<>("Operator", UnitOfMeasure::getOperator),
                new DoubleFilter<>("Operation Value", UnitOfMeasure::getOperatorValue)
        );
        getUnitOfMeasureTable();
        uomTable.setItems(UOMFormViewModel.getItems());
        System.out.println(UOMFormViewModel.getItems());
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
