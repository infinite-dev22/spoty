package org.infinite.spoty.controller.settings.roles;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.infinite.spoty.model.RoleMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.data.SampleData.roleMasterSampleData;

public class RoleMasterController implements Initializable {
    @FXML
    public MFXButton roleImportBtn;
    @FXML
    public HBox roleActionsPane;
    @FXML
    public MFXTextField roleSearchBar;
    @FXML
    public BorderPane roleContentPane;
    @FXML
    private MFXTableView<RoleMaster> roleMasterTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
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
        getRoleMasterTable();
        roleMasterTable.setItems(roleMasterSampleData());
    }

    private void getRoleMasterTable() {
        roleMasterTable.setPrefSize(1200, 1000);
        roleMasterTable.features().enableBounceEffect();
        roleMasterTable.autosizeColumnsOnInitialization();
        roleMasterTable.features().enableSmoothScrolling(0.5);
    }

    public void roleCreateBtnClicked() {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        try {
            BorderPane productFormPane = fxmlLoader("forms/RoleSettingsForm.fxml").load();
            ((StackPane) roleContentPane.getParent()).getChildren().add(productFormPane);
            ((StackPane) roleContentPane.getParent()).getChildren().get(0).setVisible(false);
        } catch (IOException ex) {
            logger.error(ex.getLocalizedMessage());
        }
    }
}
