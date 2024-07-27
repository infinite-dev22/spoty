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
import io.github.palexdev.materialfx.dialogs.*;
import java.io.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.*;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class CustomerPage extends OutlinePage {
    private TextField searchBar;
    private TableView<Customer> tableView;
    private MFXProgressSpinner progress;
    private Button createBtn;
    private FXMLLoader viewFxmlLoader;
    private MFXStageDialog viewDialog;
    private TableColumn<Customer, String> name;
    private TableColumn<Customer, String> phone;
    private TableColumn<Customer, String> email;
    private TableColumn<Customer, String> address;
    private TableColumn<Customer, String> city;
    private TableColumn<Customer, String> country;
    private TableColumn<Customer, String> taxNumber;
    private TableColumn<Customer, Customer> createdBy;
    private TableColumn<Customer, Customer> createdAt;

    public CustomerPage() {
        super();
        try {
            viewDialogPane();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        addNode(init());
        progress.setManaged(true);
        progress.setVisible(true);
        CustomerViewModel.getAllCustomers(this::onDataInitializationSuccess, this::errorMessage);
    }

    private void onDataInitializationSuccess() {
        progress.setManaged(false);
        progress.setVisible(false);
    }

    public BorderPane init() {
        var pane = new BorderPane();
        pane.setTop(buildTop());
        pane.setCenter(buildCenter());
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
        searchBar = new TextField();
        searchBar.setPromptText("Search customers");
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
        createBtn = new Button("Create");
        var hbox = new HBox(createBtn);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private HBox buildTop() {
        var hbox = new HBox();
        hbox.getStyleClass().add("card-flat-bottom");
        BorderPane.setAlignment(hbox, Pos.CENTER);
        hbox.setPadding(new Insets(5d));
        hbox.getChildren().addAll(buildLeftTop(), buildCenterTop(), buildRightTop());
        return hbox;
    }

    private AnchorPane buildCenter() {
        tableView = new TableView<>();
        NodeUtils.setAnchors(tableView, new Insets(0d));
        return new AnchorPane(tableView);
    }

    private void setupTable() {
        name = new TableColumn<>("Name");
        phone = new TableColumn<>("Phone");
        email = new TableColumn<>("Email");
        address = new TableColumn<>("Address");
        city = new TableColumn<>("City");
        country = new TableColumn<>("Country");
        taxNumber = new TableColumn<>("Tax No.");
        createdBy = new TableColumn<>("Created By");
        createdAt = new TableColumn<>("Created At");

        name.prefWidthProperty().bind(tableView.widthProperty().multiply(.2));
        phone.prefWidthProperty().bind(tableView.widthProperty().multiply(.15));
        email.prefWidthProperty().bind(tableView.widthProperty().multiply(.15));
        address.prefWidthProperty().bind(tableView.widthProperty().multiply(.15));
        city.prefWidthProperty().bind(tableView.widthProperty().multiply(.15));
        country.prefWidthProperty().bind(tableView.widthProperty().multiply(.15));
        taxNumber.prefWidthProperty().bind(tableView.widthProperty().multiply(.2));
        createdBy.prefWidthProperty().bind(tableView.widthProperty().multiply(.15));
        createdAt.prefWidthProperty().bind(tableView.widthProperty().multiply(.15));

        setupTableColumns();

        var columnList = new LinkedList<>(Stream.of(name, phone, email, address, city, country, taxNumber, createdBy, createdAt).toList());
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tableView.getColumns().addAll(columnList);
        styleCustomerTable();

        tableView.setItems(CustomerViewModel.getCustomers());
    }

    private void styleCustomerTable() {
        tableView.setPrefSize(1000, 1000);
        tableView.setRowFactory(
                t -> {
                    TableRow<Customer> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<Customer>) event.getSource())
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

    private ContextMenu showContextMenu(TableRow<Customer> obj) {
        var contextMenu = new ContextMenu();
        var delete = new MenuItem("Delete");
        var edit = new MenuItem("Edit");
        var view = new MenuItem("View");
        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            CustomerViewModel.deleteItem(obj.getItem().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getName(), this));
        // Edit
        edit.setOnAction(
                e -> {
                    CustomerViewModel.getItem(obj.getItem().getId(), () -> SpotyDialog.createDialog(new CustomerForm(), this).showAndWait(), this::errorMessage);
                    SpotyDialog.createDialog(new CustomerForm(), this).showAndWait();
                    e.consume();
                });
        // View
        view.setOnAction(
                event -> {
                    viewShow(obj.getItem());
                    event.consume();
                });
        contextMenu.getItems().addAll(view, edit, delete);
        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    public void createBtnAction() {
        createBtn.setOnAction(event -> SpotyDialog.createDialog(new CustomerForm(), this).showAndWait());
    }

    private void onSuccess() {
        CustomerViewModel.getAllCustomers(null, null);
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
        progress.setManaged(false);
        progress.setVisible(false);
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

    private void setupTableColumns() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        city.setCellValueFactory(new PropertyValueFactory<>("city"));
        country.setCellValueFactory(new PropertyValueFactory<>("country"));
        taxNumber.setCellValueFactory(new PropertyValueFactory<>("taxNumber"));
        createdBy.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        createdBy.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Customer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedBy()) ? null : item.getCreatedBy().getName());
            }
        });
        createdAt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        createdAt.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Customer item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getCreatedAt()) ? null : item.getCreatedAt().format(dtf));
            }
        });
    }
}
