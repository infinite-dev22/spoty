/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package inc.nomard.spoty.core.views;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.core.views.previews.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxcomponents.theming.enums.*;
import io.github.palexdev.mfxresources.fonts.*;
import java.io.*;
import java.util.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class ProductPage extends OutlinePage {
    private final Stage stage;
    private MFXTextField searchBar;
    private MFXTableView<Product> masterTable;
    private MFXProgressSpinner progress;
    private MFXButton createBtn;
    private MFXStageDialog formDialog;
    private MFXStageDialog viewDialog;
    private FXMLLoader viewFxmlLoader;

    public ProductPage(Stage stage) {
        this.stage = stage;
        Platform.runLater(
                () -> {
                    try {
                        productFormDialogPane(stage);
                        productViewDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
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
        createBtn.setVariants(ButtonVariants.FILLED);
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

    private void productViewDialogPane(Stage stage) throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/ProductPreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new ProductPreviewController());
        MFXGenericDialog genericDialog = viewFxmlLoader.load();

        genericDialog.setShowMinimize(false);
        genericDialog.setShowAlwaysOnTop(false);

        genericDialog.setPrefHeight(screenHeight * .98);
        genericDialog.setPrefWidth(700);

        viewDialog =
                MFXGenericDialogBuilder.build(genericDialog)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(this)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .setCenterInOwnerNode(false)
                        .setOverlayClose(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(viewDialog.getScene());
    }

    private void setupTable() {
        MFXTableColumn<Product> productName =
                new MFXTableColumn<>("Name", false, Comparator.comparing(Product::getName));
        MFXTableColumn<Product> productCategory =
                new MFXTableColumn<>("Category", false, Comparator.comparing(Product::getCategoryName));
        MFXTableColumn<Product> productBrand =
                new MFXTableColumn<>("Brand", false, Comparator.comparing(Product::getBrandName));
        MFXTableColumn<Product> costPrice =
                new MFXTableColumn<>("Cost Price", false, Comparator.comparing(Product::getCostPrice));
        MFXTableColumn<Product> salePrice =
                new MFXTableColumn<>("Sale Price", false, Comparator.comparing(Product::getSalePrice));
        MFXTableColumn<Product> productQuantity =
                new MFXTableColumn<>("Quantity", false, Comparator.comparing(Product::getQuantity));

        productName.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getName));
        productCategory.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getCategoryName));
        productBrand.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getBrandName));
        costPrice.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getCostPrice));
        salePrice.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getSalePrice));
        productQuantity.setRowCellFactory(product -> new MFXTableRowCell<>(Product::getQuantity));

        productName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        productCategory.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        productBrand.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        costPrice.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        salePrice.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        productQuantity.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));

        masterTable
                .getTableColumns()
                .addAll(productName, productCategory, productBrand, costPrice, salePrice, productQuantity);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", Product::getName),
                        new StringFilter<>("Category", Product::getCategoryName),
                        new StringFilter<>("Brand", Product::getBrandName));
        styleTable();

        if (ProductViewModel.getProducts().isEmpty()) {
            ProductViewModel.getProducts()
                    .addListener(
                            (ListChangeListener<Product>)
                                    c -> masterTable.setItems(ProductViewModel.getProducts()));
        } else {
            masterTable.itemsProperty().bindBidirectional(ProductViewModel.productsProperty());
        }
    }

    private void styleTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<Product> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<Product>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<Product> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem view = new MFXContextMenuItem("View");
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // View
        view.setOnAction(event -> {
            try {
                productViewShow(obj.getData());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            event.consume();
        });
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            ProductViewModel.deleteProduct(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getData().getName(), stage, this));
        // Edit
        edit.setOnAction(
                event -> {
                    ProductViewModel.getProduct(obj.getData().getId(), this::createBtnAction, this::errorMessage);
                    event.consume();
                });
        contextMenu.addItems(view, edit, delete);
        return contextMenu;
    }

    private void productFormDialogPane(Stage stage) throws IOException {
        FXMLLoader formFxmlLoader = fxmlLoader("views/forms/ProductForm.fxml");
        formFxmlLoader.setControllerFactory(c -> ProductFormController.getInstance(stage));

        MFXGenericDialog dialogContent = formFxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

        formDialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(this)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(formDialog.getScene());
    }

    public void createBtnAction() {
        createBtn.setOnAction(event -> formDialog.showAndWait());
    }

    public void productViewShow(Product product) {
        ProductPreviewController controller = viewFxmlLoader.getController();
        controller.init(product);
        viewDialog.showAndWait();
    }

    private void onSuccess() {
        ProductViewModel.getAllProducts(null, null);
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
        if (!BaseController.getInstance(stage).morphPane.getChildren().contains(notification)) {
            BaseController.getInstance(stage).morphPane.getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> SpotyMessage.delay(notification, stage));
        }
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
                ProductViewModel.getAllProducts(null, null);
            }
            progress.setVisible(true);
            ProductViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
            }, this::errorMessage);
        });
    }
}
