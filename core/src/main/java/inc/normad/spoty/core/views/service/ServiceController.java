package inc.normad.spoty.core.views.service;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import inc.normad.spoty.network_bridge.dtos.Service;
import inc.normad.spoty.utils.SpotyThreader;
import inc.normad.spoty.core.viewModels.ServiceViewModel;
import inc.normad.spoty.core.views.forms.ServiceFormController;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static inc.normad.spoty.core.SpotyCoreResourceLoader.fxmlLoader;

public class ServiceController implements Initializable {
    private static ServiceController instance;
    public BorderPane contentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXButton importBtn;
    @FXML
    public MFXButton createBtn;
    public MFXTableView<Service> masterTable;
    private MFXStageDialog dialog;

    private ServiceController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        customerFormDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static ServiceController getInstance(Stage stage) {
        if (instance == null) instance = new ServiceController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Service> serviceName =
                new MFXTableColumn<>("Service Name", false, Comparator.comparing(Service::getName));
        MFXTableColumn<Service> charge =
                new MFXTableColumn<>("Charge", false, Comparator.comparing(Service::getCharge));
        MFXTableColumn<Service> vat =
                new MFXTableColumn<>("VAT(%)", false, Comparator.comparing(Service::getVat));
        MFXTableColumn<Service> description =
                new MFXTableColumn<>("Description", false, Comparator.comparing(Service::getDescription));

        serviceName.setRowCellFactory(customer -> new MFXTableRowCell<>(Service::getName));
        charge.setRowCellFactory(customer -> new MFXTableRowCell<>(Service::getCharge));
        vat.setRowCellFactory(customer -> new MFXTableRowCell<>(Service::getVat));
        description.setRowCellFactory(customer -> new MFXTableRowCell<>(Service::getDescription));

        serviceName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.3));
        charge.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));
        vat.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));
        description.prefWidthProperty().bind(masterTable.widthProperty().multiply(.3));

        masterTable
                .getTableColumns()
                .addAll(serviceName, charge, vat, description);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Service Name", Service::getName),
                        new StringFilter<>("Charge", Service::getCharge),
                        new StringFilter<>("VAT(%)", Service::getVat));
        styleServiceTable();

        if (ServiceViewModel.getServices().isEmpty()) {
            ServiceViewModel.getServices().addListener(
                    (ListChangeListener<Service>)
                            c -> masterTable.setItems(ServiceViewModel.servicesList));
        } else {
            masterTable.itemsProperty().bindBidirectional(ServiceViewModel.servicesProperty());
        }
    }

    private void styleServiceTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<Service> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<Service>) event.getSource())
                                        .show(
                                                masterTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<Service> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            ServiceViewModel.deleteItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    e.consume();
                });
        // Edit
        edit.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            ServiceViewModel.getItem(obj.getData().getId(), this::onAction, this::onFailed);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    dialog.showAndWait();
                    e.consume();
                });

        contextMenu.addItems(edit, delete);

        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    private void customerFormDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/ServiceForm.fxml");
        fxmlLoader.setControllerFactory(c -> ServiceFormController.getInstance());

        MFXGenericDialog dialogContent = fxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(contentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void createBtnClicked() {
        dialog.showAndWait();
    }

    private void onAction() {
        System.out.println("Loading service...");
    }

    private void onSuccess() {
        System.out.println("Loaded service...");
    }

    private void onFailed() {
        System.out.println("failed loading service...");
    }
}
