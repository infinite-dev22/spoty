package org.infinite.spoty.views.settings.branches;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.values.strings.Labels;
import org.infinite.spoty.viewModels.BranchFormViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
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

        branchName.prefWidthProperty().bind(branchTable.widthProperty().multiply(.2));
        branchPhone.prefWidthProperty().bind(branchTable.widthProperty().multiply(.14));
        branchCity.prefWidthProperty().bind(branchTable.widthProperty().multiply(.16));
        branchTown.prefWidthProperty().bind(branchTable.widthProperty().multiply(.16));
        branchLocation.prefWidthProperty().bind(branchTable.widthProperty().multiply(.16));
        branchEmail.prefWidthProperty().bind(branchTable.widthProperty().multiply(.18));

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
    }

    private void getBranchTable() {
        branchTable.setPrefSize(1200, 1000);
        branchTable.features().enableBounceEffect();
        branchTable.features().enableSmoothScrolling(0.5);

        branchTable.setTableRowFactory(t -> {
            MFXTableRow<Branch> row = new MFXTableRow<>(branchTable, t);
//            row.setOnMouseClicked(e -> {
//                if (e.getButton().equals(MouseButton.SECONDARY)) {
//                    showContextMenu().show(branchTable);
//                }
//            });
            // Context menu doesn't show, not yet figured out why but must be coz show() ain't called on it yet.
            // which when done results into an error.
            EventHandler<ContextMenuEvent> eventHandler = event -> showContextMenu();
            row.setOnContextMenuRequested(eventHandler);
            return row;
        });
    }

    private void showContextMenu() {
        MFXContextMenu contextMenu = new MFXContextMenu(branchTable);

        MFXContextMenuItem view = new MFXContextMenuItem("View");
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        contextMenu.addItems(view, edit, delete);
    }

    @FXML
    private void branchCreateBtnClicked() {
        BranchFormViewModel.setTitle(Labels.CREATE);
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
