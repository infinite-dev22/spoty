package inc.nomard.spoty.core.views.pages;

import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.viewModels.accounting.AccountViewModel;
import inc.nomard.spoty.core.views.components.DeleteConfirmationDialog;
import inc.nomard.spoty.core.views.forms.AccountForm;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.SpotyDialog;
import inc.nomard.spoty.core.views.layout.message.SpotyMessage;
import inc.nomard.spoty.core.views.layout.message.enums.MessageDuration;
import inc.nomard.spoty.core.views.layout.message.enums.MessageVariants;
import inc.nomard.spoty.core.views.util.OutlinePage;
import inc.nomard.spoty.network_bridge.dtos.accounting.Account;
import inc.nomard.spoty.utils.AppUtils;
import inc.nomard.spoty.utils.navigation.Spacer;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;
import lombok.extern.java.Log;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Stream;

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
        AccountViewModel.getAllAccounts(this::onDataInitializationSuccess, this::errorMessage, null, null);
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

    private VBox buildCenter() {
        masterTable = new TableView<>();
        VBox.setVgrow(masterTable, Priority.ALWAYS);
        HBox.setHgrow(masterTable, Priority.ALWAYS);
        var paging = new HBox(new Spacer(), buildPagination(), new Spacer(), buildPageSize());
        paging.setPadding(new Insets(0d, 20d, 0d, 5d));
        paging.setAlignment(Pos.CENTER);
        if (AccountViewModel.getTotalPages() > 0) {
            paging.setVisible(true);
            paging.setManaged(true);
        } else {
            paging.setVisible(false);
            paging.setManaged(false);
        }
        AccountViewModel.totalPagesProperty().addListener((observableValue, oldNum, newNum) -> {
            if (AccountViewModel.getTotalPages() > 0) {
                paging.setVisible(true);
                paging.setManaged(true);
            } else {
                paging.setVisible(false);
                paging.setManaged(false);
            }
        });
        var centerHolder = new VBox(masterTable, paging);
        VBox.setVgrow(centerHolder, Priority.ALWAYS);
        HBox.setHgrow(centerHolder, Priority.ALWAYS);
        return centerHolder;
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
        AccountViewModel.getAllAccounts(null, null, null, null);
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
                AccountViewModel.getAllAccounts(null, null, null, null);
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
        displayNotification(message, MessageVariants.SUCCESS, FontAwesomeSolid.CHECK_CIRCLE);
    }

    private void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, FontAwesomeSolid.EXCLAMATION_TRIANGLE);
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

    private void displayNotification(String message, MessageVariants type, Ikon icon) {
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

    private Pagination buildPagination() {
        var pagination = new Pagination(AccountViewModel.getTotalPages(), 0);
        pagination.setMaxPageIndicatorCount(5);
        pagination.pageCountProperty().bindBidirectional(AccountViewModel.totalPagesProperty());
        pagination.setPageFactory(pageNum -> {
            progress.setManaged(true);
            progress.setVisible(true);
            AccountViewModel.getAllAccounts(() -> {
                progress.setManaged(false);
                progress.setVisible(false);
            }, null, pageNum, AccountViewModel.getPageSize());
            AccountViewModel.setPageNumber(pageNum);
            return new StackPane(); // null isn't allowed
        });
        return pagination;
    }

    private ComboBox<Integer> buildPageSize() {
        var pageSize = new ComboBox<Integer>();
        pageSize.setItems(FXCollections.observableArrayList(25, 50, 75, 100));
        pageSize.valueProperty().bindBidirectional(AccountViewModel.pageSizeProperty().asObject());
        pageSize.valueProperty().addListener(
                (observableValue, integer, t1) -> {
                    progress.setManaged(true);
                    progress.setVisible(true);
                    AccountViewModel
                            .getAllAccounts(
                                    () -> {
                                        progress.setManaged(false);
                                        progress.setVisible(false);
                                    },
                                    null,
                                    AccountViewModel.getPageNumber(),
                                    t1);
                    AccountViewModel.setPageSize(t1);
                });
        return pageSize;
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}
