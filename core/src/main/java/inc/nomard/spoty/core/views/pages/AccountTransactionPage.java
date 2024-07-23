package inc.nomard.spoty.core.views.pages;

import atlantafx.base.util.*;
import inc.nomard.spoty.core.viewModels.accounting.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.accounting.*;
import java.util.*;
import java.util.stream.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class AccountTransactionPage extends OutlinePage {
    private TextField searchBar;
    private TableView<AccountTransaction> masterTable;

    public AccountTransactionPage() {
        super();
        addNode(init());
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
        var hbox = new HBox();
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
        TableColumn<AccountTransaction, String> accountName = new TableColumn<>("Account Name");
        TableColumn<AccountTransaction, String> transactionType = new TableColumn<>("Type");
        TableColumn<AccountTransaction, Double> credit = new TableColumn<>("Credit");
        TableColumn<AccountTransaction, Double> debit = new TableColumn<>("Debit");
        TableColumn<AccountTransaction, Double> amount = new TableColumn<>("Amount");
        TableColumn<AccountTransaction, String> note = new TableColumn<>("Note");
        TableColumn<AccountTransaction, String> transactionDate = new TableColumn<>("Date");

        accountName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        transactionType.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        credit.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        debit.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        amount.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        note.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));
        transactionDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));

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
