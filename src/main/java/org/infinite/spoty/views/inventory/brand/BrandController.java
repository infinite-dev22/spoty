package org.infinite.spoty.views.inventory.brand;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
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
import org.infinite.spoty.models.Brand;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.data.SampleData.brandSampleData;

public class BrandController implements Initializable {
    @FXML
    public MFXTableView<Brand> brandTable;
    @FXML
    public MFXTextField brandSearchBar;
    @FXML
    public HBox brandActionsPane;
    @FXML
    public MFXButton brandImportBtn;
    private Dialog<ButtonType> dialog;

    public BrandController(Stage stage) {
        Platform.runLater(() -> {
            try {
                brandFormDialogPane(stage);
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
        MFXTableColumn<Brand> brandName = new MFXTableColumn<>("Name", true, Comparator.comparing(Brand::getBrandName));
        MFXTableColumn<Brand> brandDescription = new MFXTableColumn<>("Description", true, Comparator.comparing(Brand::getBrandDescription));

        brandName.setRowCellFactory(brand -> new MFXTableRowCell<>(Brand::getBrandName));
        brandName.setRowCellFactory(brand -> new MFXTableRowCell<>(Brand::getBrandDescription));

        brandTable.getTableColumns().addAll(brandName, brandDescription);
        brandTable.getFilters().addAll(
                new StringFilter<>("Name", Brand::getBrandName),
                new StringFilter<>("Description", Brand::getBrandDescription)
        );
        getBrandTable();
        brandTable.setItems(brandSampleData());
    }

    private void getBrandTable() {
        brandTable = new MFXTableView<>();
        brandTable.setPrefSize(1000, 1000);
        brandTable.autosizeColumnsOnInitialization();
        brandTable.features().enableBounceEffect();
        brandTable.features().enableSmoothScrolling(0.5);
    }

    private void brandFormDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/BrandForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }

    public void brandCreateBtnClicked() {
        dialog.showAndWait();
    }
}
