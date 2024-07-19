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
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxresources.fonts.*;
import java.util.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class AccountPage extends OutlinePage {
    private MFXTextField searchBar;
    private MFXTableView<Account> masterTable;
    private MFXProgressSpinner progress;
    private MFXButton createBtn;

    public AccountPage() {
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
        masterTable = new MFXTableView<>();
        AnchorPane.setBottomAnchor(masterTable, 0d);
        AnchorPane.setLeftAnchor(masterTable, 0d);
        AnchorPane.setRightAnchor(masterTable, 0d);
        AnchorPane.setTopAnchor(masterTable, 10d);
        return new AnchorPane(masterTable);
    }

    private void setupTable() {
        MFXTableColumn<Account> accountName =
                new MFXTableColumn<>("Account Name", false, Comparator.comparing(Account::getAccountName));
        MFXTableColumn<Account> accountNumber =
                new MFXTableColumn<>("Account Number", false, Comparator.comparing(Account::getAccountNumber));
        MFXTableColumn<Account> credit =
                new MFXTableColumn<>("Credit", false, Comparator.comparing(Account::getCredit));
        MFXTableColumn<Account> debit =
                new MFXTableColumn<>("Debit", false, Comparator.comparing(Account::getDebit));
        MFXTableColumn<Account> balance =
                new MFXTableColumn<>("Balance", false, Comparator.comparing(Account::getBalance));
        MFXTableColumn<Account> description =
                new MFXTableColumn<>("Description", false, Comparator.comparing(Account::getDescription));

        accountName.setRowCellFactory(customer -> new MFXTableRowCell<>(Account::getAccountName));
        accountNumber.setRowCellFactory(customer -> new MFXTableRowCell<>(Account::getAccountNumber));
        credit.setRowCellFactory(customer -> new MFXTableRowCell<>(Account::getCredit));
        debit.setRowCellFactory(customer -> new MFXTableRowCell<>(Account::getDebit));
        balance.setRowCellFactory(customer -> new MFXTableRowCell<>(Account::getBalance));
        description.setRowCellFactory(customer -> new MFXTableRowCell<>(Account::getDescription));

        accountName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        accountNumber.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        credit.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        debit.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        balance.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        description.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));

        masterTable
                .getTableColumns()
                .addAll(accountName, accountNumber, credit, debit, balance, description);
        masterTable
                .getFilters()
                .addAll(new StringFilter<>("Account Name", Account::getAccountName),
                        new StringFilter<>("Account Number", Account::getAccountNumber),
                        new StringFilter<>("Balance", Account::getBalance));
        styleAccountTable();

        if (AccountViewModel.getAccounts().isEmpty()) {
            AccountViewModel.getAccounts().addListener(
                    (ListChangeListener<Account>)
                            c -> masterTable.setItems(AccountViewModel.accountsList));
        } else {
            masterTable.itemsProperty().bindBidirectional(AccountViewModel.accountsProperty());
        }
    }

    private void styleAccountTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<Account> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<Account>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<Account> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        MFXContextMenuItem deposit = new MFXContextMenuItem("Deposit");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            AccountViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getData().getAccountName(), this));
        // Edit
        edit.setOnAction(
                event -> {
                    AccountViewModel.getItem(obj.getData().getId(), () -> SpotyDialog.createDialog(new AccountForm(), this).showAndWait(), this::errorMessage);
                    event.consume();
                });
        // Deposit
        deposit.setOnAction(
                event -> {
                    AccountViewModel.getItem(obj.getData().getId(), () -> SpotyDialog.createDialog(new AccountForm(), this).showAndWait(), this::errorMessage);
                    event.consume();
                });

        contextMenu.addItems(deposit, edit, delete);

        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    public void createBtnAction() {
        createBtn.setOnAction(event -> SpotyDialog.createDialog(new AccountForm(), this).showAndWait());
    }

    private void onSuccess() {
        AccountViewModel.getAllAccounts(null, null);
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