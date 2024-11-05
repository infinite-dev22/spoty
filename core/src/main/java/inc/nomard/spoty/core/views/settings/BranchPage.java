package inc.nomard.spoty.core.views.settings;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.viewModels.BranchViewModel;
import inc.nomard.spoty.core.viewModels.BrandViewModel;
import inc.nomard.spoty.core.viewModels.accounting.AccountViewModel;
import inc.nomard.spoty.core.views.components.DeleteConfirmationDialog;
import inc.nomard.spoty.core.views.components.SpotyProgressSpinner;
import inc.nomard.spoty.core.views.forms.BranchForm;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.ModalContentHolder;
import inc.nomard.spoty.core.views.layout.SideModalPane;
import inc.nomard.spoty.core.views.layout.message.SpotyMessage;
import inc.nomard.spoty.core.views.layout.message.enums.MessageDuration;
import inc.nomard.spoty.core.views.layout.message.enums.MessageVariants;
import inc.nomard.spoty.core.views.layout.navigation.Spacer;
import inc.nomard.spoty.core.views.util.OutlinePage;
import inc.nomard.spoty.network_bridge.dtos.Branch;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Objects;

@SuppressWarnings("unchecked")
@Slf4j
public class BranchPage extends OutlinePage {
    private final ModalPane modalPane;
    private CustomTextField searchBar;
    private TableView<Branch> masterTable;
    private SpotyProgressSpinner progress;

    public BranchPage() {
        modalPane = new SideModalPane();
        getChildren().addAll(modalPane, init());
        progress.setManaged(true);
        progress.setVisible(true);
        modalPane.displayProperty().addListener((_, _, open) -> {
            if (!open) {
                modalPane.setAlignment(Pos.CENTER);
                modalPane.usePredefinedTransitionFactories(null);
            }
        });
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
        setIcons();
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
        Button createBtn = new Button("Create");
        createBtn.setDefaultButton(true);
        createBtn.setOnAction(_ -> showDialog(0));
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
        var paging = new HBox(buildPageSize(), new Spacer(), buildPagination());
        paging.setPadding(new Insets(0d, 20d, 0d, 5d));
        paging.setAlignment(Pos.CENTER);
        BrandViewModel.totalPagesProperty().addListener((_, oldNum, newNum) -> {
            if (!(oldNum != null && (Integer) oldNum > 1) || !(newNum != null && (Integer) newNum > 1)) {
                paging.setVisible(false);
                paging.setManaged(false);
            } else {
                paging.setVisible(true);
                paging.setManaged(true);
            }
        });
        var centerHolder = new VBox(masterTable, paging);
        centerHolder.getStyleClass().add("card-flat-top");
        VBox.setVgrow(centerHolder, Priority.ALWAYS);
        HBox.setHgrow(centerHolder, Priority.ALWAYS);
        return centerHolder;
    }

    public void setupTable() {
        // TODO: Create ZipCode and Country Columns.
        TableColumn<Branch, String> branchName = new TableColumn<>("Name");
        TableColumn<Branch, String> branchPhone = new TableColumn<>("Phone");
        TableColumn<Branch, String> branchCity = new TableColumn<>("City");
        TableColumn<Branch, String> branchTown = new TableColumn<>("Town");
        TableColumn<Branch, String> branchEmail = new TableColumn<>("Email");

        branchName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));
        branchPhone.prefWidthProperty().bind(masterTable.widthProperty().multiply(.18));
        branchCity.prefWidthProperty().bind(masterTable.widthProperty().multiply(.18));
        branchTown.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));
        branchEmail.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));

        masterTable.getColumns()
                .addAll(branchName, branchPhone, branchCity, branchTown, branchEmail);

        getBranchTable();
        masterTable.setItems(BranchViewModel.getBranches());
        masterTable.getStyleClass().addAll(Styles.STRIPED, Tweaks.EDGE_TO_EDGE);
    }

    private void getBranchTable() {
        masterTable.setRowFactory(
                _ -> new TableRow<>() {
                    @Override
                    public void updateItem(Branch item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setStyle("");
                        } else {
                            EventHandler<ContextMenuEvent> eventHandler =
                                    event ->
                                            showContextMenu((TableRow<Branch>) event.getSource())
                                                    .show(
                                                            this.getScene().getWindow(),
                                                            event.getScreenX(),
                                                            event.getScreenY());
                            setOnContextMenuRequested(eventHandler);
                        }
                    }
                });
    }

    private ContextMenu showContextMenu(TableRow<Branch> obj) {
        var contextMenu = new ContextMenu();
        var delete = new MenuItem("Delete");
        var edit = new MenuItem("Edit");

        delete.setOnAction(event -> new DeleteConfirmationDialog(AppManager.getGlobalModalPane(), () -> {
            BranchViewModel.deleteItem(obj.getItem().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getName()).showDialog());
        edit.setOnAction(
                e -> {
                    BranchViewModel.getItem(obj.getItem().getId(), () -> this.showDialog(1), this::errorMessage);
                    e.consume();
                });
        contextMenu.getItems().addAll(edit, delete);
        return contextMenu;
    }

    private void showDialog(Integer reason) {
        var dialog = new ModalContentHolder(500, -1);
        dialog.getChildren().add(new BranchForm(modalPane, reason));
        dialog.setPadding(new Insets(5d));
        modalPane.setAlignment(Pos.TOP_RIGHT);
        modalPane.usePredefinedTransitionFactories(Side.RIGHT);
        modalPane.setOutTransitionFactory(node -> Animations.fadeOutRight(node, Duration.millis(400)));
        modalPane.setInTransitionFactory(node -> Animations.slideInRight(node, Duration.millis(400)));
        modalPane.show(dialog);
        modalPane.setPersistent(true);
    }

    private void onSuccess() {
        BranchViewModel.getAllBranches(null, null, null, null);
    }

    private void successMessage(String message) {
        displayNotification(message, MessageVariants.SUCCESS, FontAwesomeSolid.CHECK_CIRCLE);
    }

    private void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, FontAwesomeSolid.EXCLAMATION_TRIANGLE);
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

    private void setIcons() {
        searchBar.setRight(new FontIcon(FontAwesomeSolid.SEARCH));
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((_, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                BranchViewModel.getAllBranches(null, null, null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            BranchViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
                progress.setManaged(false);
            }, this::errorMessage);
        });
    }

    private Pagination buildPagination() {
        var pagination = new Pagination(BrandViewModel.getTotalPages(), 0);
        pagination.setMaxPageIndicatorCount(6);
        pagination.pageCountProperty().bindBidirectional(BrandViewModel.totalPagesProperty());
        pagination.setPageFactory(pageNum -> {
            progress.setManaged(true);
            progress.setVisible(true);
            BrandViewModel.getAllBrands(() -> {
                progress.setManaged(false);
                progress.setVisible(false);
            }, null, pageNum, BrandViewModel.getPageSize());
            BrandViewModel.setPageNumber(pageNum);
            return new StackPane(); // null isn't allowed
        });
        return pagination;
    }

    private HBox buildPageSize() {
        var pageSize = new ComboBox<Integer>();
        pageSize.setItems(FXCollections.observableArrayList(25, 50, 75, 100));
        pageSize.valueProperty().bindBidirectional(BrandViewModel.pageSizeProperty().asObject());
        pageSize.valueProperty().addListener(
                (_, _, t1) -> {
                    progress.setManaged(true);
                    progress.setVisible(true);
                    BrandViewModel
                            .getAllBrands(
                                    () -> {
                                        progress.setManaged(false);
                                        progress.setVisible(false);
                                    },
                                    null,
                                    BrandViewModel.getPageNumber(),
                                    t1);
                    BrandViewModel.setPageSize(t1);
                });
        var hbox = new HBox(10, new Text("Rows per page:"), pageSize);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }
}
