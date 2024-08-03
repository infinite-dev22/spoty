package inc.nomard.spoty.core.views.settings;

import atlantafx.base.controls.*;
import atlantafx.base.util.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.Currency;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxresources.fonts.*;
import java.util.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class CurrencyPage extends OutlinePage {
    private CustomTextField searchBar;
    private MFXTableView<Currency> masterTable;
    private MFXProgressSpinner progress;
    private Button createBtn;

    public CurrencyPage() {
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
        searchBar = new CustomTextField();
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
        createBtn.setDefaultButton(true);
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
        masterTable = new MFXTableView<>();
        NodeUtils.setAnchors(masterTable, new Insets(0d));
        return new AnchorPane(masterTable);
    }

    private void setupTable() {
        MFXTableColumn<Currency> currencyName =
                new MFXTableColumn<>("Name", false, Comparator.comparing(Currency::getName));
        MFXTableColumn<Currency> currencyCode =
                new MFXTableColumn<>("Code", false, Comparator.comparing(Currency::getCode));
        MFXTableColumn<Currency> currencySymbol =
                new MFXTableColumn<>("Symbol", false, Comparator.comparing(Currency::getSymbol));

        currencyName.setRowCellFactory(currency -> new MFXTableRowCell<>(Currency::getName));
        currencyCode.setRowCellFactory(currency -> new MFXTableRowCell<>(Currency::getCode));
        currencySymbol.setRowCellFactory(currency -> new MFXTableRowCell<>(Currency::getSymbol));

        currencyName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.34));
        currencyCode.prefWidthProperty().bind(masterTable.widthProperty().multiply(.34));
        currencySymbol.prefWidthProperty().bind(masterTable.widthProperty().multiply(.34));

        masterTable.getTableColumns().addAll(currencyName, currencyCode, currencySymbol);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", Currency::getName),
                        new StringFilter<>("Code", Currency::getCode),
                        new StringFilter<>("Symbol", Currency::getSymbol));

        getCurrencyTable();

        if (CurrencyViewModel.getCurrencies().isEmpty()) {
            CurrencyViewModel.getCurrencies()
                    .addListener(
                            (ListChangeListener<Currency>)
                                    c -> masterTable.setItems(CurrencyViewModel.getCurrencies()));
        } else {
            masterTable.itemsProperty().bindBidirectional(CurrencyViewModel.currencyProperty());
        }
    }

    private void getCurrencyTable() {
        masterTable.setPrefSize(1200, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<Currency> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event ->
                                    showContextMenu((MFXTableRow<Currency>) event.getSource())
                                            .show(
                                                    this.getScene().getWindow(),
                                                    event.getScreenX(),
                                                    event.getScreenY());
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<Currency> obj) {
        var contextMenu = new MFXContextMenu(masterTable);
        var delete = new MFXContextMenuItem("Delete");
        var edit = new MFXContextMenuItem("Edit");
        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            CurrencyViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getData().getName(), this));
        // Edit
        edit.setOnAction(
                e -> {
                    CurrencyViewModel.getItem(obj.getData().getId(), this::createBtnAction, this::errorMessage);

                    e.consume();
                });
        contextMenu.addItems(edit, delete);
        return contextMenu;
    }

    private void createBtnAction() {
        createBtn.setOnAction(event -> SpotyDialog.createDialog(new CurrencyForm(), this).showAndWait());
    }

    private void onSuccess() {
        CurrencyViewModel.getAllCurrencies(null, null);
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

    private void setIcons() {
        searchBar.setRight(new MFXFontIcon("fas-magnifying-glass"));
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                CurrencyViewModel.getAllCurrencies(null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            CurrencyViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
                progress.setManaged(false);
            }, this::errorMessage);
        });
    }
}
