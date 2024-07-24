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
import java.util.*;
import java.util.stream.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class ExpensePage extends OutlinePage {
    private TextField searchBar;
    private TableView<Expense> tableView;
    private MFXProgressSpinner progress;
    private Button createBtn;

    public ExpensePage() {
        addNode(init());
        progress.setManaged(true);
        progress.setVisible(true);
        ExpensesViewModel.getAllExpenses(this::onDataInitializationSuccess, this::errorMessage);
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
        searchBar.setPromptText("Search expenses");
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
        TableColumn<Expense, String> expenseDate = new TableColumn<>("Date");
        TableColumn<Expense, String> expenseName = new TableColumn<>("Name");
        TableColumn<Expense, Double> expenseAmount = new TableColumn<>("Amount");
        TableColumn<Expense, String> expenseCategory = new TableColumn<>("Category");

        expenseDate.prefWidthProperty().bind(tableView.widthProperty().multiply(.25));
        expenseName.prefWidthProperty().bind(tableView.widthProperty().multiply(.25));
        expenseAmount.prefWidthProperty().bind(tableView.widthProperty().multiply(.25));
        expenseCategory.prefWidthProperty().bind(tableView.widthProperty().multiply(.25));

        var columnList = new LinkedList<>(Stream.of(expenseName, expenseCategory, expenseDate, expenseAmount).toList());
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tableView.getColumns().addAll(columnList);
        styleExpenseTable();

        tableView.setItems(ExpensesViewModel.getExpenses());
    }

    private void styleExpenseTable() {
        tableView.setPrefSize(1200, 1000);
        tableView.setRowFactory(
                t -> {
                    TableRow<Expense> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<Expense>) event.getSource())
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

    private MFXContextMenu showContextMenu(TableRow<Expense> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(tableView);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            ExpensesViewModel.deleteItem(obj.getItem().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getName(), this));
        // Edit
        edit.setOnAction(
                e -> {
                    ExpensesViewModel.getItem(obj.getItem().getId(), () -> SpotyDialog.createDialog(new ExpenseForm(), this).showAndWait(), this::errorMessage);
                    e.consume();
                });
        contextMenu.addItems(edit, delete);
        return contextMenu;
    }

    public void createBtnAction() {
        createBtn.setOnAction(event -> SpotyDialog.createDialog(new ExpenseForm(), this).showAndWait());
    }

    private void onSuccess() {
        ExpensesViewModel.getAllExpenses(null, null);
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                ExpensesViewModel.getAllExpenses(null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            ExpensesViewModel.searchItem(nv, () -> {
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
}
