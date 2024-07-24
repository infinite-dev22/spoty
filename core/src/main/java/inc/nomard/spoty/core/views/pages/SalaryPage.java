package inc.nomard.spoty.core.views.pages;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.viewModels.hrm.pay_roll.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.previews.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.pay_roll.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class SalaryPage extends OutlinePage {
    private TextField searchBar;
    private TableView<SalaryAdvance> masterTable;
    private MFXProgressSpinner progress;
    private MFXStageDialog viewDialog;
    private FXMLLoader viewFxmlLoader;

    public SalaryPage() {
        try {
            productViewDialogPane();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        addNode(init());
        progress.setManaged(true);
        progress.setVisible(true);
        SalaryAdvanceViewModel.getAllSalaryAdvances(this::onDataInitializationSuccess, this::errorMessage);
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
        searchBar.setPromptText("Search salaries");
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

    private void productViewDialogPane() throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/SalaryAdvancePreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new SalaryAdvancePreviewController());

        MFXGenericDialog dialogContent = viewFxmlLoader.load();
        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);
        dialogContent.setShowClose(false);

        dialogContent.setPrefHeight(screenHeight * .98);
        dialogContent.setPrefWidth(700);
        viewDialog = SpotyDialog.createDialog(dialogContent, this);
    }

    private void setupTable() {
        TableColumn<SalaryAdvance, String> productName = new TableColumn<>("Employee");
        TableColumn<SalaryAdvance, String> productCategory = new TableColumn<>("Period");
        TableColumn<SalaryAdvance, String> productBrand = new TableColumn<>("Status");
        TableColumn<SalaryAdvance, String> productQuantity = new TableColumn<>("Salary");
        TableColumn<SalaryAdvance, String> productPrice = new TableColumn<>("Net Salary");

        productName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        productCategory.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        productBrand.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        productQuantity.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        productPrice.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));

        var columnList = new LinkedList<>(Stream.of(productName, productCategory, productBrand, productQuantity, productPrice).toList());
        masterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        masterTable.getColumns().addAll(columnList);
        styleTable();

        masterTable.setItems(SalaryAdvanceViewModel.getSalaryAdvances());
    }

    private void styleTable() {
        masterTable.setPrefSize(1000, 1000);

        masterTable.setRowFactory(
                t -> {
                    TableRow<SalaryAdvance> row = new TableRow<>();
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((TableRow<SalaryAdvance>) event.getSource())
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

    private MFXContextMenu showContextMenu(TableRow<SalaryAdvance> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem view = new MFXContextMenuItem("View");
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");

        // Actions
        // View
        view.setOnAction(event -> {
            try {
                productViewShow(obj.getItem());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            event.consume();
        });
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            SalaryAdvanceViewModel.deleteSalaryAdvance(obj.getItem().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getItem().getEmployeeName() + "'s salary", this));

        contextMenu.addItems(view, delete);

        return contextMenu;
    }

    private void onSuccess() {
        SalaryAdvanceViewModel.getAllSalaryAdvances(null, null);
    }

    public void productViewShow(SalaryAdvance product) {
        SalaryAdvancePreviewController controller = viewFxmlLoader.getController();
        controller.init(product);
        viewDialog.showAndWait();
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

    public void createBtnAction() {
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                SalaryAdvanceViewModel.getAllSalaryAdvances(null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            SalaryAdvanceViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
                progress.setManaged(false);
            }, this::errorMessage);
        });
    }
}
