package org.infinite.spoty.views.settings.branches;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.models.Branch;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.data.SampleData.branchSampleData;

public class BranchesController implements Initializable {
    @FXML
    public MFXTextField branchSearchBar;
    @FXML
    public HBox branchActionsPane;
    @FXML
    public MFXButton branchImportBtn;
    @FXML
    public MFXTableView<Branch> branchTable;
    @FXML
    public BorderPane branchContentPane;
    private Dialog<ButtonType> dialog;

    public BranchesController(Stage stage) {
        Platform.runLater(() -> {
            try {
                branchFormDialogPane(stage);
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
        MFXTableColumn<Branch> branchName = new MFXTableColumn<>("Name", true, Comparator.comparing(Branch::getBranchName));
        MFXTableColumn<Branch> branchPhone = new MFXTableColumn<>("Phone", true, Comparator.comparing(Branch::getBranchPhoneNumber));
        MFXTableColumn<Branch> branchCity = new MFXTableColumn<>("City", true, Comparator.comparing(Branch::getBranchCity));
        MFXTableColumn<Branch> branchTown = new MFXTableColumn<>("Town", true, Comparator.comparing(Branch::getBranchTown));
        MFXTableColumn<Branch> branchLocation = new MFXTableColumn<>("Location", true, Comparator.comparing(Branch::getBranchLocation));
        MFXTableColumn<Branch> branchEmail = new MFXTableColumn<>("Email", true, Comparator.comparing(Branch::getBranchEmail));

        branchName.setRowCellFactory(branch -> new MFXTableRowCell<>(Branch::getBranchName));
        branchPhone.setRowCellFactory(branch -> new MFXTableRowCell<>(Branch::getBranchPhoneNumber));
        branchCity.setRowCellFactory(branch -> new MFXTableRowCell<>(Branch::getBranchCity));
        branchTown.setRowCellFactory(branch -> new MFXTableRowCell<>(Branch::getBranchTown));
        branchLocation.setRowCellFactory(branch -> new MFXTableRowCell<>(Branch::getBranchLocation));
        branchEmail.setRowCellFactory(branch -> new MFXTableRowCell<>(Branch::getBranchEmail));

        branchTable.getTableColumns().addAll(branchName, branchPhone, branchCity, branchTown, branchLocation, branchEmail);
        branchTable.getFilters().addAll(
                new StringFilter<>("Name", Branch::getBranchName),
                new StringFilter<>("Phone", Branch::getBranchPhoneNumber),
                new StringFilter<>("City", Branch::getBranchCity),
                new StringFilter<>("Town", Branch::getBranchTown),
                new StringFilter<>("Location", Branch::getBranchLocation),
                new StringFilter<>("Email", Branch::getBranchEmail)
        );
        getBranchTable();
        branchTable.setItems(branchSampleData());
    }

    private void getBranchTable() {
        branchTable.setPrefSize(1200, 1000);
        branchTable.features().enableBounceEffect();
        branchTable.autosizeColumnsOnInitialization();
        branchTable.features().enableSmoothScrolling(0.5);
    }

    @FXML
    private void branchCreateBtnClicked() {
        dialog.showAndWait();
    }

    private void branchFormDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/BranchForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }
}
