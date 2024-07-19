package inc.nomard.spoty.core.views.pages;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.previews.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxresources.fonts.*;
import java.io.*;
import java.util.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class CustomerPage extends OutlinePage {
    private MFXTextField searchBar;
    private MFXTableView<Customer> tableView;
    private MFXProgressSpinner progress;
    private MFXButton createBtn;
    private FXMLLoader viewFxmlLoader;
    private MFXStageDialog viewDialog;

    public CustomerPage() {
        super();
        try {
            viewDialogPane();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        addNode(init());
    }

    public BorderPane init() {
        var pane = new BorderPane();
        pane.setTop(buildTop());
        pane.setCenter(buildCenter());
        setIcons();
        setSearchBar();
        setupTable();
        createBtnAction();
        return pane;
    }

    private HBox buildLeftTop() {
        progress = new MFXProgressSpinner();
        progress.setMinSize(30d, 30d);
        progress.setPrefSize(30d, 30d);
        progress.setMaxSize(30d, 30d);
        progress.setVisible(false);
        progress.setManaged(false);
        var hbox = new HBox(progress);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private HBox buildCenterTop() {
        searchBar = new MFXTextField();
        searchBar.setPromptText("Search customers");
        searchBar.setFloatMode(FloatMode.DISABLED);
        searchBar.setMinWidth(300d);
        searchBar.setPrefWidth(500d);
        searchBar.setMaxWidth(700d);
        var hbox = new HBox(searchBar);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private HBox buildRightTop() {
        createBtn = new MFXButton("Create");
        createBtn.getStyleClass().add("filled");
        var hbox = new HBox(createBtn);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private HBox buildTop() {
        var hbox = new HBox();
        hbox.getStyleClass().add("card-flat");
        BorderPane.setAlignment(hbox, Pos.CENTER);
        hbox.setPadding(new Insets(5d));
        hbox.getChildren().addAll(buildLeftTop(), buildCenterTop(), buildRightTop());
        return hbox;
    }

    private AnchorPane buildCenter() {
        tableView = new MFXTableView<>();
        AnchorPane.setBottomAnchor(tableView, 0d);
        AnchorPane.setLeftAnchor(tableView, 0d);
        AnchorPane.setRightAnchor(tableView, 0d);
        AnchorPane.setTopAnchor(tableView, 10d);
        return new AnchorPane(tableView);
    }

    private void setupTable() {
        MFXTableColumn<Customer> customerName =
                new MFXTableColumn<>("Name", false, Comparator.comparing(Customer::getName));
        MFXTableColumn<Customer> customerPhone =
                new MFXTableColumn<>("Phone", false, Comparator.comparing(Customer::getPhone));
        MFXTableColumn<Customer> customerEmail =
                new MFXTableColumn<>("Email", false, Comparator.comparing(Customer::getEmail));
        MFXTableColumn<Customer> customerTax =
                new MFXTableColumn<>("Tax No.", false, Comparator.comparing(Customer::getTaxNumber));
        customerName.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getName));
        customerPhone.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getPhone));
        customerEmail.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getEmail));
        customerTax.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getTaxNumber));
        customerName.prefWidthProperty().bind(tableView.widthProperty().multiply(.3));
        customerPhone.prefWidthProperty().bind(tableView.widthProperty().multiply(.2));
        customerEmail.prefWidthProperty().bind(tableView.widthProperty().multiply(.3));
        customerTax.prefWidthProperty().bind(tableView.widthProperty().multiply(.2));
        tableView
                .getTableColumns()
                .addAll(customerName, customerPhone, customerEmail, customerTax);
        tableView
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", Customer::getName),
                        new StringFilter<>("Phone", Customer::getPhone),
                        new StringFilter<>("Email", Customer::getEmail),
                        new StringFilter<>("Tax No.", Customer::getTaxNumber));
        styleCustomerTable();
        if (CustomerViewModel.getCustomers().isEmpty()) {
            CustomerViewModel.getCustomers().addListener(
                    (ListChangeListener<Customer>)
                            c -> tableView.setItems(CustomerViewModel.getCustomers()));
        } else {
            tableView.itemsProperty().bindBidirectional(CustomerViewModel.customersProperty());
        }
    }

    private void styleCustomerTable() {
        tableView.setPrefSize(1000, 1000);
        tableView.features().enableBounceEffect();
        tableView.features().enableSmoothScrolling(0.5);
        tableView.setTableRowFactory(
                t -> {
                    MFXTableRow<Customer> row = new MFXTableRow<>(tableView, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<Customer>) event.getSource())
                                        .show(
                                                tableView.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<Customer> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(tableView);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        MFXContextMenuItem view = new MFXContextMenuItem("View");
        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            CustomerViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getData().getName(), this));
        // Edit
        edit.setOnAction(
                e -> {
                    CustomerViewModel.getItem(obj.getData().getId(), () -> SpotyDialog.createDialog(new CustomerForm(), this).showAndWait(), this::errorMessage);
                    SpotyDialog.createDialog(new CustomerForm(), this).showAndWait();
                    e.consume();
                });
        // View
        view.setOnAction(
                event -> {
                    viewShow(obj.getData());
                    event.consume();
                });
        contextMenu.addItems(view, edit, delete);
        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    public void createBtnAction() {
        createBtn.setOnAction(event -> SpotyDialog.createDialog(new CustomerForm(), this).showAndWait());
    }

    private void onSuccess() {
        CustomerViewModel.getAllCustomers(null, null);
    }

    private void setIcons() {
        searchBar.setTrailingIcon(new MFXFontIcon("fas-magnifying-glass"));
    }

    private void viewDialogPane() throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/CustomerPreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new CustomerPreviewController());
        MFXGenericDialog dialogContent = viewFxmlLoader.load();
        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);
        dialogContent.setHeaderText("Customer Details View");
        dialogContent.setPrefHeight(screenHeight * .98);
        dialogContent.setPrefWidth(700);
        viewDialog = SpotyDialog.createDialog(dialogContent, this);
    }

    public void viewShow(Customer customer) {
        CustomerPreviewController controller = viewFxmlLoader.getController();
        controller.init(customer);
        viewDialog.showAndWait();
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                CustomerViewModel.getAllCustomers(null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            CustomerViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
                progress.setManaged(false);
            }, this::errorMessage);
        });
    }

    private void successMessage(String message) {
        displayNotification(message, MessageVariants.SUCCESS, "fas-circle-check");
    }

    private void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, "fas-triangle-exclamation");
    }

    private void displayNotification(String message, MessageVariants type, String icon) {
        SpotyMessage notification = new SpotyMessage.MessageBuilder(message)
                .duration(MessageDuration.SHORT)
                .icon(icon)
                .type(type)
                .height(60)
                .build();
        AnchorPane.setTopAnchor(notification, 5.0);
        AnchorPane.setRightAnchor(notification, 5.0);

        var in = Animations.slideInDown(notification, Duration.millis(250));
        if (!AppManager.getMorphPane().getChildren().contains(notification)) {
            AppManager.getMorphPane().getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> SpotyMessage.delay(notification));
        }
    }
}