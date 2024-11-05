package inc.nomard.spoty.core.views.pages;

import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.viewModels.accounting.AccountTransactionViewModel;
import inc.nomard.spoty.core.views.components.SpotyProgressSpinner;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.message.SpotyMessage;
import inc.nomard.spoty.core.views.layout.message.enums.MessageDuration;
import inc.nomard.spoty.core.views.layout.message.enums.MessageVariants;
import inc.nomard.spoty.core.views.layout.navigation.Spacer;
import inc.nomard.spoty.core.views.util.OutlinePage;
import inc.nomard.spoty.network_bridge.dtos.accounting.AccountTransaction;
import inc.nomard.spoty.utils.AppUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
public class AccountTransactionPage extends OutlinePage {
    private TextField searchBar;
    private TableView<AccountTransaction> masterTable;
    private SpotyProgressSpinner progress;
    private TableColumn<AccountTransaction, AccountTransaction> accountName;
    private TableColumn<AccountTransaction, String> transactionType;
    private TableColumn<AccountTransaction, AccountTransaction> credit;
    private TableColumn<AccountTransaction, AccountTransaction> debit;
    private TableColumn<AccountTransaction, AccountTransaction> amount;
    private TableColumn<AccountTransaction, String> note;
    private TableColumn<AccountTransaction, AccountTransaction> transactionDate;

    public AccountTransactionPage() {
        super();
        addNode(init());
        progress.setManaged(true);
        progress.setVisible(true);
        AccountTransactionViewModel.getAllTransactions(this::onDataInitializationSuccess, this::errorMessage, null, null);
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
        return pane;
    }

    private HBox buildLeftTop() {
        progress = new SpotyProgressSpinner();
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
        searchBar.setPromptText("Search account transactions");
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
        var hbox = new HBox();
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
        var paging = new HBox(buildPageSize(), new Spacer(), buildPagination());
        paging.setPadding(new Insets(0d, 20d, 0d, 5d));
        paging.setAlignment(Pos.CENTER);
        if (AccountTransactionViewModel.getTotalPages() > 0) {
            paging.setVisible(true);
            paging.setManaged(true);
        } else {
            paging.setVisible(false);
            paging.setManaged(false);
        }
        AccountTransactionViewModel.totalPagesProperty().addListener((_, _, _) -> {
            if (AccountTransactionViewModel.getTotalPages() > 0) {
                paging.setVisible(true);
                paging.setManaged(true);
            } else {
                paging.setVisible(false);
                paging.setManaged(false);
            }
        });
        var centerHolder = new VBox(masterTable, paging);
        centerHolder.getStyleClass().add("card-flat-top");
        VBox.setVgrow(centerHolder, Priority.ALWAYS);
        HBox.setHgrow(centerHolder, Priority.ALWAYS);
        return centerHolder;
    }

    private void setupTable() {
        accountName = new TableColumn<>("Account Name");
        transactionType = new TableColumn<>("Type");
        credit = new TableColumn<>("Credit");
        debit = new TableColumn<>("Debit");
        amount = new TableColumn<>("Amount");
        note = new TableColumn<>("Note");
        transactionDate = new TableColumn<>("Date");

        accountName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));
        transactionType.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        credit.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        debit.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        amount.prefWidthProperty().bind(masterTable.widthProperty().multiply(.15));
        note.prefWidthProperty().bind(masterTable.widthProperty().multiply(.3));
        transactionDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));

        setupTableColumns();

        var columnList = new LinkedList<>(Stream.of(accountName, transactionDate, transactionType, credit, debit, amount, note).toList());
        masterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        masterTable.getColumns().addAll(columnList);
        styleBankTable();
        masterTable.setItems(AccountTransactionViewModel.transactionsList);
        masterTable.getStyleClass().addAll(Styles.STRIPED, Tweaks.EDGE_TO_EDGE);
    }

    private void styleBankTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.setRowFactory(
                _ -> new TableRow<>() {
                    @Override
                    public void updateItem(AccountTransaction item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setStyle("");
                        } else {
                        }
                    }
                });
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((_, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                AccountTransactionViewModel.getAllTransactions(null, this::errorMessage, null, null);
            }
            AccountTransactionViewModel.transactionsList.addAll(AccountTransactionViewModel.getTransactions().stream()
                    .filter(accountTransaction -> Objects.equals(accountTransaction.getAccountName(), nv))
                    .filter(accountTransaction -> Objects.equals(accountTransaction.getTransactionType(), nv))
                    .toList());
        });
    }

    private void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, FontAwesomeSolid.EXCLAMATION_TRIANGLE);
        progress.setManaged(false);
        progress.setVisible(false);
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
            in.setOnFinished(_ -> SpotyMessage.delay(notification));
        }
    }

    private void setupTableColumns() {
        accountName.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        accountName.setCellFactory(_ -> new TableCell<>() {
            @Override
            public void updateItem(AccountTransaction item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getAccountName());
            }
        });
        transactionType.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        credit.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        credit.setCellFactory(_ -> new TableCell<>() {
            @Override
            public void updateItem(AccountTransaction item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getCredit()));
            }
        });
        debit.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        debit.setCellFactory(_ -> new TableCell<>() {
            @Override
            public void updateItem(AccountTransaction item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getDebit()));
            }
        });
        amount.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        amount.setCellFactory(_ -> new TableCell<>() {
            @Override
            public void updateItem(AccountTransaction item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : AppUtils.decimalFormatter().format(item.getAmount()));
            }
        });
        note.setCellValueFactory(new PropertyValueFactory<>("note"));
        transactionDate.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        transactionDate.setCellFactory(_ -> new TableCell<>() {
            @Override
            public void updateItem(AccountTransaction item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getTransactionDate()) ? null : item.getTransactionDate().format(dtf));
            }
        });
    }

    private Pagination buildPagination() {
        var pagination = new Pagination(AccountTransactionViewModel.getTotalPages(), 0);
        pagination.setMaxPageIndicatorCount(6);
        pagination.pageCountProperty().bindBidirectional(AccountTransactionViewModel.totalPagesProperty());
        pagination.setPageFactory(pageNum -> {
            progress.setManaged(true);
            progress.setVisible(true);
            AccountTransactionViewModel.getAllTransactions(() -> {
                progress.setManaged(false);
                progress.setVisible(false);
            }, null, pageNum, AccountTransactionViewModel.getPageSize());
            AccountTransactionViewModel.setPageNumber(pageNum);
            return new StackPane(); // null isn't allowed
        });
        return pagination;
    }

    private HBox buildPageSize() {
        var pageSize = new ComboBox<Integer>();
        pageSize.setItems(FXCollections.observableArrayList(25, 50, 75, 100));
        pageSize.valueProperty().bindBidirectional(AccountTransactionViewModel.pageSizeProperty().asObject());
        pageSize.valueProperty().addListener(
                (_, _, t1) -> {
                    progress.setManaged(true);
                    progress.setVisible(true);
                    AccountTransactionViewModel
                            .getAllTransactions(
                                    () -> {
                                        progress.setManaged(false);
                                        progress.setVisible(false);
                                    },
                                    null,
                                    AccountTransactionViewModel.getPageNumber(),
                                    t1);
                    AccountTransactionViewModel.setPageSize(t1);
                });
        var hbox = new HBox(10, new Text("Rows per page:"), pageSize);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }
}
