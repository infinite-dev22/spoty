package org.infinite.spoty.controller.settings.roles;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.infinite.spoty.model.RoleMaster;
import org.infinite.spoty.model.RoleMaster;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.data.SampleData.roleMasterSampleData;

public class RoleMasterController implements Initializable {
    private MFXTableView<RoleMaster> roleMasterTable;

    @FXML
    public BorderPane rolesContentPane;

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
            rolesContentPane.setCenter(getRoleMasterTable());
            setupTable();
        })).start();
    }

    private void setupTable() {
        MFXTableColumn<RoleMaster> roleMasterRole = new MFXTableColumn<>("Role", true, Comparator.comparing(RoleMaster::getRole));
        MFXTableColumn<RoleMaster> roleMasterDescription = new MFXTableColumn<>("Description", true, Comparator.comparing(RoleMaster::getDescription));

        roleMasterRole.setRowCellFactory(roleMaster -> new MFXTableRowCell<>(RoleMaster::getRole));
        roleMasterDescription.setRowCellFactory(roleMaster -> new MFXTableRowCell<>(RoleMaster::getDescription));

        roleMasterTable.getTableColumns().addAll(roleMasterRole, roleMasterDescription);
        roleMasterTable.getFilters().addAll(
                new StringFilter<>("Role", RoleMaster::getRole),
                new StringFilter<>("Description", RoleMaster::getDescription)
        );

        roleMasterTable.setItems(roleMasterSampleData());
    }

    private MFXTableView<RoleMaster> getRoleMasterTable() {
        roleMasterTable = new MFXTableView<>();
        roleMasterTable.setPrefSize(1200, 1000);
        roleMasterTable.features().enableBounceEffect();
        roleMasterTable.autosizeColumnsOnInitialization();
        roleMasterTable.features().enableSmoothScrolling(0.5);
        return roleMasterTable;
    }
}
