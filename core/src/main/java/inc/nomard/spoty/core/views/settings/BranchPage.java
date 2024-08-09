package inc.nomard.spoty.core.views.settings;

import atlantafx.base.controls.*;
import atlantafx.base.util.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.utils.navigation.Spacer;
import io.github.palexdev.materialfx.controls.*;
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
public class BranchPage extends OutlinePage {
    private CustomTextField searchBar;
    private TableView<Branch> masterTable;
    private MFXProgressSpinner progress;
    private Button createBtn;

    public BranchPage() {
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

    private VBox buildCenter() {
        masterTable = new TableView<>();
        VBox.setVgrow(masterTable, Priority.ALWAYS);
        HBox.setHgrow(masterTable, Priority.ALWAYS);
        var paging = new HBox(new inc.nomard.spoty.utils.navigation.Spacer(), buildPagination(), new Spacer(), buildPageSize());
        paging.setPadding(new Insets(0d, 20d, 0d, 5d));
        paging.setAlignment(Pos.CENTER);
        BrandViewModel.totalPagesProperty().addListener((observableValue, oldNum, newNum) -> {
            if (!(oldNum != null && (Integer) oldNum > 1) || !(newNum != null && (Integer) newNum > 1)) {
                paging.setVisible(false);
                paging.setManaged(false);
            } else {
                paging.setVisible(true);
                paging.setManaged(true);
            }
        });
        var centerHolder = new VBox(masterTable, paging);
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

        if (BranchViewModel.getBranches().isEmpty()) {
            BranchViewModel.getBranches()
                    .addListener(
                            (ListChangeListener<Branch>)
                                    c -> masterTable.setItems(BranchViewModel.getBranches()));
        } else {
            masterTable.itemsProperty().bindBidirectional(BranchViewModel.branchesProperty());
        }
    }

    private void getBranchTable() {
        masterTable.setRowFactory(
                t -> {
                    TableRow<Branch> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event ->
                                    showContextMenu((TableRow<Branch>) event.getSource())
                                            .show(
                                                    this.getScene().getWindow(),
                                                    event.getScreenX(),
                                                    event.getScreenY());
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private ContextMenu showContextMenu(TableRow<Branch> obj) {
        var contextMenu = new ContextMenu();
        var delete = new MenuItem("Delete");
        var edit = new MenuItem("Edit");
        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            BranchViewModel.deleteItem(obj.getItem().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getName(), this));
        // Edit
        edit.setOnAction(
                e -> {
                    BranchViewModel.getItem(obj.getItem().getId(), this::createBtnAction, this::errorMessage);
                    e.consume();
                });
        contextMenu.getItems().addAll(edit, delete);
        return contextMenu;
    }

    private void createBtnAction() {
        createBtn.setOnAction(event -> {
            BranchViewModel.setTitle(Labels.CREATE);
            SpotyDialog.createDialog(new BranchForm(), this).showAndWait();
        });
    }

    private void onSuccess() {
        BranchViewModel.getAllBranches(null, null, null, null);
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
        pagination.setMaxPageIndicatorCount(5);
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

    private ComboBox<Integer> buildPageSize() {
        var pageSize = new ComboBox<Integer>();
        pageSize.setItems(FXCollections.observableArrayList(25, 50, 75, 100));
        pageSize.valueProperty().bindBidirectional(BrandViewModel.pageSizeProperty().asObject());
        pageSize.valueProperty().addListener(
                (observableValue, integer, t1) -> {
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
        return pageSize;
    }
}
