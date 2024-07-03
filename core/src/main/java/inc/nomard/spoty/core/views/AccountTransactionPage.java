package inc.nomard.spoty.core.views;

import atlantafx.base.util.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxresources.fonts.*;
import java.util.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class AccountTransactionPage extends OutlinePage {
    private MFXTextField searchBar;
    private MFXTableView<AccountTransaction> masterTable;

    public AccountTransactionPage() {
        super();
        addNode(init());
    }

    public BorderPane init() {
        var pane = new BorderPane();
        pane.setTop(buildTop());
        pane.setCenter(buildCenter());
        setIcons();
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
        searchBar = new MFXTextField();
        searchBar.setPromptText("Search accounts");
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
        var hbox = new HBox();
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
        masterTable = new MFXTableView<>();
        AnchorPane.setBottomAnchor(masterTable, 0d);
        AnchorPane.setLeftAnchor(masterTable, 0d);
        AnchorPane.setRightAnchor(masterTable, 0d);
        AnchorPane.setTopAnchor(masterTable, 10d);
        return new AnchorPane(masterTable);
    }

    private void setupTable() {
        MFXTableColumn<AccountTransaction> accountName =
                new MFXTableColumn<>("Account Name", false, Comparator.comparing(AccountTransaction::getAccountName));
        MFXTableColumn<AccountTransaction> transactionType =
                new MFXTableColumn<>("Type", false, Comparator.comparing(AccountTransaction::getTransactionType));
        MFXTableColumn<AccountTransaction> credit =
                new MFXTableColumn<>("Credit", false, Comparator.comparing(AccountTransaction::getCredit));
        MFXTableColumn<AccountTransaction> debit =
                new MFXTableColumn<>("Debit", false, Comparator.comparing(AccountTransaction::getDebit));
        MFXTableColumn<AccountTransaction> amount =
                new MFXTableColumn<>("Amount", false, Comparator.comparing(AccountTransaction::getAmount));
        MFXTableColumn<AccountTransaction> note =
                new MFXTableColumn<>("Note", false, Comparator.comparing(AccountTransaction::getNote));
        MFXTableColumn<AccountTransaction> transactionDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(AccountTransaction::getTransactionDate));

        accountName.setRowCellFactory(customer -> new MFXTableRowCell<>(AccountTransaction::getAccountName));
        transactionType.setRowCellFactory(customer -> new MFXTableRowCell<>(AccountTransaction::getTransactionType));
        credit.setRowCellFactory(customer -> new MFXTableRowCell<>(AccountTransaction::getCredit));
        debit.setRowCellFactory(customer -> new MFXTableRowCell<>(AccountTransaction::getDebit));
        amount.setRowCellFactory(customer -> new MFXTableRowCell<>(AccountTransaction::getAmount));
        note.setRowCellFactory(customer -> new MFXTableRowCell<>(AccountTransaction::getNote));
        transactionDate.setRowCellFactory(customer -> new MFXTableRowCell<>(AccountTransaction::getTransactionDate));

        accountName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        transactionType.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        credit.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        debit.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        amount.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        note.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));
        transactionDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));

        masterTable
                .getTableColumns()
                .addAll(accountName, transactionDate, transactionType, credit, debit, amount, note);
        masterTable
                .getFilters()
                .addAll(new StringFilter<>("Account Name", AccountTransaction::getAccountName),
                        new StringFilter<>("Type", AccountTransaction::getTransactionType));
        styleBankTable();

        if (AccountTransactionViewModel.getTransactions().isEmpty()) {
            AccountTransactionViewModel.getTransactions().addListener(
                    (ListChangeListener<AccountTransaction>)
                            c -> masterTable.setItems(AccountTransactionViewModel.transactionsList));
        } else {
            masterTable.itemsProperty().bindBidirectional(AccountTransactionViewModel.transactionsProperty());
        }
    }

    private void styleBankTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);
    }

    private void setIcons() {
        searchBar.setTrailingIcon(new MFXFontIcon("fas-magnifying-glass"));
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
