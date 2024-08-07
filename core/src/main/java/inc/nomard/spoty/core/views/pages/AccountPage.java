package inc.nomard.spoty.core.views.pages;

import atlantafx.base.util.*;
import inc.nomard.spoty.core.viewModels.accounting.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.accounting.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import java.util.*;
import java.util.stream.*;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class AccountPage extends OutlinePage {
    private TextField searchBar;
    private TableView<Account> masterTable;
    private MFXProgressSpinner progress;
    private Button createBtn;
    private TableColumn<Account, String> accountName;
    private TableColumn<Account, String> accountNumber;
    private TableColumn<Account, Account> credit;
    private TableColumn<Account, Account> debit;
    private TableColumn<Account, Account> balance;
    private TableColumn<Account, String> description;

    public AccountPage() {
        super();
        addNode(init());
        AccountViewModel.getAllAccounts(this::onDataInitializationSuccess, this::errorMessage);
    }

    private void onDataInitializationSuccess() {
        progress.setManaged(false);
        progress.setVisible(false);
    }

    public BorderPane init() {
        var pane = new BorderPane();
        pane.setTop(buildTop());
        pane.setCenter(buildCenter());
//        setIcons();
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
        searchBar.setPromptText("Search accounts");
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
        masterTable = new TableView<>();
        NodeUtils.setAnchors(masterTable, new Insets(0d));
        return new AnchorPane(masterTable);
    }

    private void setupTable() {
        accountName = new TableColumn<>("Account Name");
        accountNumber = new TableColumn<>("Account Number");
        credit = new TableColumn<>("Credit");
        debit = new TableColumn<>("Debit");
        balance = new TableColumn<>("Balance");
        description = new TableColumn<>("Description");

        accountName.setEditable(false);
        accountNumber.setEditable(false);
        credit.setEditable(false);
        debit.setEditable(false);
        balance.setEditable(false);
        description.setEditable(false);

        accountName.setSortable(true);
        accountNumber.setSortable(true);
        credit.setSortable(true);
        debit.setSortable(true);
        balance.setSortable(true);
        description.setSortable(true);

        accountName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        accountNumber.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        credit.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        debit.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        balance.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        description.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));

        setupTableColumns();

        var columnList = new LinkedList<>(Stream.of(accountName, accountNumber, credit, debit, balance, description).toList());
        masterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        masterTable.getColumns().addAll(columnList);
        styleAccountTable();

        masterTable.setItems(AccountViewModel.accountsList);
    }

    private void styleAccountTable() {
        masterTable.setPrefSize(1000, 1000);

        masterTable.setRowFactory(
                t -> {
                    TableRow<Account> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<Account>) event.getSource())
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

    private ContextMenu showContextMenu(TableRow<Account> obj) {
        var contextMenu = new ContextMenu();
        var delete = new MenuItem("Delete");
        var edit = new MenuItem("Edit");
        var deposit = new MenuItem("Deposit");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            AccountViewModel.deleteItem(obj.getItem().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getAccountName(), this));
        // Edit
        edit.setOnAction(
                event -> {
                    AccountViewModel.getItem(obj.getItem().getId(), () -> SpotyDialog.createDialog(new AccountForm(), this).showAndWait(), this::errorMessage);
                    event.consume();
                });
        // Deposit
        deposit.setOnAction(
                event -> {
                    AccountViewModel.getItem(obj.getItem().getId(), () -> SpotyDialog.createDialog(new AccountForm(), this).showAndWait(), this::errorMessage);
                    event.consume();
                });
        contextMenu.getItems().addAll(edit, delete);
        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    public void createBtnAction() {
        createBtn.setOnAction(event -> SpotyDialog.createDialog(new AccountForm(), this).showAndWait());
    }

    private void onSuccess() {
        AccountViewModel.getAllAccounts(null, null);
    }

//    private void setIcons() {
//        searchBar.setRight(new MFXFontIcon("fas-magnifying-glass"));
//    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                AccountViewModel.getAllAccounts(null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            AccountViewModel.searchItem(nv, () -> {
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

    private void setupTableColumns() {
        accountName.setCellValueFactory(new PropertyValueFactory<>("accountName"));
        accountNumber.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        credit.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        credit.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Account item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getCredit()));
            }
        });
        debit.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        debit.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Account item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getDebit()));
            }
        });
        balance.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        balance.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(Account item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getBalance()));
            }
        });
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
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

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}
