package inc.nomard.spoty.core.views.pages;

import atlantafx.base.util.*;
import inc.nomard.spoty.core.viewModels.accounting.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.accounting.*;
import io.github.palexdev.materialfx.controls.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.*;
import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class AccountTransactionPage extends OutlinePage {
    private TextField searchBar;
    private TableView<AccountTransaction> masterTable;
    private MFXProgressSpinner progress;
    private TableColumn<AccountTransaction, AccountTransaction> accountName;
    private TableColumn<AccountTransaction, String> transactionType;
    private TableColumn<AccountTransaction, Double> credit;
    private TableColumn<AccountTransaction, Double> debit;
    private TableColumn<AccountTransaction, Double> amount;
    private TableColumn<AccountTransaction, String> note;
    private TableColumn<AccountTransaction, AccountTransaction> transactionDate;

    public AccountTransactionPage() {
        super();
        addNode(init());
        progress.setManaged(true);
        progress.setVisible(true);
        AccountTransactionViewModel.getAllTransactions(this::onDataInitializationSuccess, this::errorMessage);
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

    private AnchorPane buildCenter() {
        masterTable = new TableView<>();
        NodeUtils.setAnchors(masterTable, new Insets(0d));
        return new AnchorPane(masterTable);
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
    }

    private void styleBankTable() {
        masterTable.setPrefSize(1000, 1000);
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                AccountTransactionViewModel.getAllTransactions(null, this::errorMessage);
            }
            AccountTransactionViewModel.transactionsList.addAll(AccountTransactionViewModel.getTransactions().stream()
                    .filter(accountTransaction -> Objects.equals(accountTransaction.getAccountName(), nv))
                    .filter(accountTransaction -> Objects.equals(accountTransaction.getTransactionType(), nv))
                    .toList());
        });
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
        accountName.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        accountName.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(AccountTransaction item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getAccountName());
            }
        });
        transactionType.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        credit.setCellValueFactory(new PropertyValueFactory<>("credit"));
        debit.setCellValueFactory(new PropertyValueFactory<>("debit"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        note.setCellValueFactory(new PropertyValueFactory<>("note"));
        transactionDate.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        transactionDate.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(AccountTransaction item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault());

                setText(empty || Objects.isNull(item) ? null : Objects.isNull(item.getTransactionDate()) ? null : item.getTransactionDate().format(dtf));
            }
        });
    }
}
