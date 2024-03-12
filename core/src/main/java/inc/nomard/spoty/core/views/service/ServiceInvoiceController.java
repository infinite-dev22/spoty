package inc.nomard.spoty.core.views.service;

import inc.nomard.spoty.core.components.animations.SpotyAnimations;
import inc.nomard.spoty.core.viewModels.BankViewModel;
import inc.nomard.spoty.core.viewModels.ServiceInvoiceViewModel;
import inc.nomard.spoty.core.views.forms.ServiceInvoiceFormController;
import inc.nomard.spoty.network_bridge.dtos.ServiceInvoice;
import inc.nomard.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.fxmlLoader;

public class ServiceInvoiceController implements Initializable {
    private static ServiceInvoiceController instance;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXButton importBtn;
    @FXML
    public MFXButton createBtn;
    @FXML
    public MFXTableView<ServiceInvoice> masterTable;
    @FXML
    public HBox refresh;
    private MFXStageDialog dialog;
    private RotateTransition transition;

    private ServiceInvoiceController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        customerFormDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static ServiceInvoiceController getInstance(Stage stage) {
        if (instance == null) instance = new ServiceInvoiceController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setIcons();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<ServiceInvoice> serviceName =
                new MFXTableColumn<>("Customer Name", false, Comparator.comparing(ServiceInvoice::getCustomerName));
        MFXTableColumn<ServiceInvoice> charge =
                new MFXTableColumn<>("Date", false, Comparator.comparing(ServiceInvoice::getDate));
        MFXTableColumn<ServiceInvoice> description =
                new MFXTableColumn<>("Description", false, Comparator.comparing(ServiceInvoice::getDescription));

        serviceName.setRowCellFactory(customer -> new MFXTableRowCell<>(ServiceInvoice::getCustomerName));
        charge.setRowCellFactory(customer -> new MFXTableRowCell<>(ServiceInvoice::getDate));
        description.setRowCellFactory(customer -> new MFXTableRowCell<>(ServiceInvoice::getDescription));

        serviceName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.31));
        charge.prefWidthProperty().bind(masterTable.widthProperty().multiply(.31));
        description.prefWidthProperty().bind(masterTable.widthProperty().multiply(.31));

        masterTable
                .getTableColumns()
                .addAll(serviceName, charge, description);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Customer Name", ServiceInvoice::getCustomerName));
        styleServiceInvoiceTable();

        if (ServiceInvoiceViewModel.getServiceInvoices().isEmpty()) {
            ServiceInvoiceViewModel.getServiceInvoices().addListener(
                    (ListChangeListener<ServiceInvoice>)
                            c -> masterTable.setItems(ServiceInvoiceViewModel.serviceInvoicesList));
        } else {
            masterTable.itemsProperty().bindBidirectional(ServiceInvoiceViewModel.servicesProperty());
        }
    }

    private void styleServiceInvoiceTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<ServiceInvoice> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<ServiceInvoice>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<ServiceInvoice> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            ServiceInvoiceViewModel.deleteItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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
                            ServiceInvoiceViewModel.getItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
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
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/ServiceInvoiceForm.fxml");
        fxmlLoader.setControllerFactory(c -> ServiceInvoiceFormController.getInstance());

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
        transition.playFromStart();
        transition.setOnFinished((ActionEvent event) -> transition.playFromStart());
    }

    private void onSuccess() {
        transition.setOnFinished(null);
    }

    private void onFailed() {
        transition.setOnFinished(null);
    }

    private void setIcons() {
        var refreshIcon = new MFXFontIcon("fas-arrows-rotate", 24);
        refresh.getChildren().addFirst(refreshIcon);

        transition = SpotyAnimations.rotateTransition(refreshIcon, Duration.millis(1000), 360);

        refreshIcon.setOnMouseClicked(mouseEvent -> ServiceInvoiceViewModel.getAllServiceInvoices(this::onAction, this::onSuccess, this::onFailed));
    }
}
