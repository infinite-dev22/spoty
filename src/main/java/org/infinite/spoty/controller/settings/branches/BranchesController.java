package org.infinite.spoty.controller.settings.branches;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.infinite.spoty.model.Branch;
import org.infinite.spoty.model.Branch;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.data.SampleData.branchSampleData;

public class BranchesController implements Initializable {
    private MFXTableView<Branch> branchesTable;
    
    @FXML
    public BorderPane branchContentPane;

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
            branchContentPane.setCenter(getBranchTable());
            setupTable();
        })).start();
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

        branchesTable.getTableColumns().addAll(branchName, branchPhone, branchCity, branchTown, branchLocation, branchEmail);
        branchesTable.getFilters().addAll(
                new StringFilter<>("Name", Branch::getBranchName),
                new StringFilter<>("Phone", Branch::getBranchPhoneNumber),
                new StringFilter<>("City", Branch::getBranchCity),
                new StringFilter<>("Town", Branch::getBranchTown),
                new StringFilter<>("Location", Branch::getBranchLocation),
                new StringFilter<>("Email", Branch::getBranchEmail)
        );

        branchesTable.setItems(branchSampleData());
    }

    private MFXTableView<Branch> getBranchTable() {
        branchesTable = new MFXTableView<>();
        branchesTable.setPrefSize(1200, 1000);
        branchesTable.features().enableBounceEffect();
        branchesTable.autosizeColumnsOnInitialization();
        branchesTable.features().enableSmoothScrolling(0.5);
        return branchesTable;
    }
}
