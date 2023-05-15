package org.infinite.spoty.views.settings.branches;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.virtualizedfx.table.TableRow;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.infinite.spoty.database.dao.BranchDao;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.viewModels.BranchFormViewModel;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

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

    public void setupTable() {
        // TODO: Create ZipCode and Country Columns.
        MFXTableColumn<Branch> branchName = new MFXTableColumn<>("Name", true, Comparator.comparing(Branch::getName));
        MFXTableColumn<Branch> branchPhone = new MFXTableColumn<>("Phone", true, Comparator.comparing(Branch::getPhone));
        MFXTableColumn<Branch> branchCity = new MFXTableColumn<>("City", true, Comparator.comparing(Branch::getCity));
        MFXTableColumn<Branch> branchTown = new MFXTableColumn<>("Town", true, Comparator.comparing(Branch::getTown));
        MFXTableColumn<Branch> branchLocation = new MFXTableColumn<>("Location", true, Comparator.comparing(Branch::getZipCode));
        MFXTableColumn<Branch> branchEmail = new MFXTableColumn<>("Email", true, Comparator.comparing(Branch::getEmail));

        branchName.setRowCellFactory(branch -> new MFXTableRowCell<>(Branch::getName));
        branchPhone.setRowCellFactory(branch -> new MFXTableRowCell<>(Branch::getPhone));
        branchCity.setRowCellFactory(branch -> new MFXTableRowCell<>(Branch::getCity));
        branchTown.setRowCellFactory(branch -> new MFXTableRowCell<>(Branch::getTown));
        branchLocation.setRowCellFactory(branch -> new MFXTableRowCell<>(Branch::getZipCode));
        branchEmail.setRowCellFactory(branch -> new MFXTableRowCell<>(Branch::getEmail));

        branchTable.getTableColumns().addAll(branchName, branchPhone, branchCity, branchTown, branchLocation, branchEmail);
        branchTable.getFilters().addAll(
                new StringFilter<>("Name", Branch::getName),
                new StringFilter<>("Phone", Branch::getPhone),
                new StringFilter<>("City", Branch::getCity),
                new StringFilter<>("Town", Branch::getTown),
                new StringFilter<>("Location", Branch::getZipCode),
                new StringFilter<>("Email", Branch::getEmail)
        );
        getBranchTable();
        branchTable.setItems(BranchFormViewModel.getItems());


        BranchFormViewModel.branchesList.addListener((ListChangeListener<Branch>) c -> {
            // DO NOT TEMPER WITH THE LINES BELOW.
            // Basically just prints out to STDIO but seems to be the real deal. Not sure why it even works.
            System.out.println("List updated");
            // Fetches new data from the database, runs multiple times till StackoverFlow Exception. not sure why.
            // branchTable.setItems(BranchFormViewModel.getItems());
            // Doesn't clear the ObservableList, not sure why.
            // BranchFormViewModel.branchesList.clear();
        });
    }

    private void getBranchTable() {
        branchTable.setPrefSize(1200, 1000);
        branchTable.features().enableBounceEffect();
        branchTable.autosizeColumnsOnInitialization();
        branchTable.features().enableSmoothScrolling(0.5);
    }

    @FXML
    private void branchCreateBtnClicked() {
        BranchFormViewModel.setTitle("Create Branch");
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
