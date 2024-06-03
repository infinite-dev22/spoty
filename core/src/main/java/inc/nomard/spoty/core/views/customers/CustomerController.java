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

package inc.nomard.spoty.core.views.customers;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.animations.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.core.views.previews.people.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxresources.fonts.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javafx.animation.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class CustomerController implements Initializable {
    private static CustomerController instance;
    @FXML
    public HBox customerActionsPane;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXTableView<Customer> customersTable;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox refresh;
    private MFXStageDialog dialog;
    private RotateTransition transition;
    private FXMLLoader viewFxmlLoader;
    private MFXStageDialog viewDialog;

    private CustomerController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        customerFormDialogPane(stage);
                        viewDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static CustomerController getInstance(Stage stage) {
        if (instance == null) instance = new CustomerController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setIcons();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Customer> customerName =
                new MFXTableColumn<>("Name", false, Comparator.comparing(Customer::getName));
        MFXTableColumn<Customer> customerPhone =
                new MFXTableColumn<>("Phone", false, Comparator.comparing(Customer::getPhone));
        MFXTableColumn<Customer> customerEmail =
                new MFXTableColumn<>("Email", false, Comparator.comparing(Customer::getEmail));
        MFXTableColumn<Customer> customerTax =
                new MFXTableColumn<>("Tax No.", false, Comparator.comparing(Customer::getTaxNumber));
        customerName.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getName));
        customerPhone.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getPhone));
        customerEmail.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getEmail));
        customerTax.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getTaxNumber));
        customerName.prefWidthProperty().bind(customersTable.widthProperty().multiply(.3));
        customerPhone.prefWidthProperty().bind(customersTable.widthProperty().multiply(.2));
        customerEmail.prefWidthProperty().bind(customersTable.widthProperty().multiply(.3));
        customerTax.prefWidthProperty().bind(customersTable.widthProperty().multiply(.2));
        customersTable
                .getTableColumns()
                .addAll(customerName, customerPhone, customerEmail, customerTax);
        customersTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", Customer::getName),
                        new StringFilter<>("Phone", Customer::getPhone),
                        new StringFilter<>("Email", Customer::getEmail),
                        new StringFilter<>("Tax No.", Customer::getTaxNumber));
        styleCustomerTable();
        if (CustomerViewModel.getCustomers().isEmpty()) {
            CustomerViewModel.getCustomers().addListener(
                    (ListChangeListener<Customer>)
                            c -> customersTable.setItems(CustomerViewModel.getCustomers()));
        } else {
            customersTable.itemsProperty().bindBidirectional(CustomerViewModel.customersProperty());
        }
    }

    private void styleCustomerTable() {
        customersTable.setPrefSize(1000, 1000);
        customersTable.features().enableBounceEffect();
        customersTable.features().enableSmoothScrolling(0.5);
        customersTable.setTableRowFactory(
                t -> {
                    MFXTableRow<Customer> row = new MFXTableRow<>(customersTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<Customer>) event.getSource())
                                        .show(
                                                customersTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<Customer> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(customersTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        MFXContextMenuItem view = new MFXContextMenuItem("View");
        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    CustomerViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
                    e.consume();
                });
        // Edit
        edit.setOnAction(
                e -> {
                    CustomerViewModel.getItem(obj.getData().getId(), () -> dialog.showAndWait(), this::errorMessage);
                    dialog.showAndWait();
                    e.consume();
                });
        // View
        view.setOnAction(
                event -> {
                    viewShow(obj.getData());
                    event.consume();
                });
        contextMenu.addItems(view, edit, delete);
        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    private void customerFormDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/CustomerForm.fxml");
        fxmlLoader.setControllerFactory(c -> CustomerFormController.getInstance());
        MFXGenericDialog dialogContent = fxmlLoader.load();
        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);
        dialogContent.setShowClose(false);
        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(contentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();
        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void customerCreateBtnClicked() {
        dialog.showAndWait();
    }

    private void onSuccess() {
        CustomerViewModel.getAllCustomers(null, null);
    }

    private void setIcons() {
        var refreshIcon = new MFXFontIcon("fas-arrows-rotate", 24);
        refresh.getChildren().addFirst(refreshIcon);
        transition = SpotyAnimations.rotateTransition(refreshIcon, Duration.millis(1000), 360);
        refreshIcon.setOnMouseClicked(mouseEvent -> CustomerViewModel.getAllCustomers(this::onSuccess, this::errorMessage));
    }

    private void viewDialogPane(Stage stage) throws IOException {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        viewFxmlLoader = fxmlLoader("views/previews/people/CustomerPreview.fxml");
        viewFxmlLoader.setControllerFactory(c -> new CustomerPreviewController());
        MFXGenericDialog genericDialog = viewFxmlLoader.load();
        genericDialog.setShowMinimize(false);
        genericDialog.setShowAlwaysOnTop(false);
        genericDialog.setHeaderText("Customer Details View");
        genericDialog.setPrefHeight(screenHeight * .98);
        genericDialog.setPrefWidth(700);
        viewDialog =
                MFXGenericDialogBuilder.build(genericDialog)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(contentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .setCenterInOwnerNode(false)
                        .setOverlayClose(true)
                        .get();
        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(viewDialog.getScene());
    }

    public void viewShow(Customer customer) {
        CustomerPreviewController controller = viewFxmlLoader.getController();
        controller.init(customer);
        viewDialog.showAndWait();
    }

    private void successMessage(String message) {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        AnchorPane.setRightAnchor(notification, 40.0);
        AnchorPane.setTopAnchor(notification, 10.0);
    }

    private void errorMessage(String message) {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
        notificationHolder.addMessage(notification);
        AnchorPane.setRightAnchor(notification, 40.0);
        AnchorPane.setTopAnchor(notification, 10.0);
    }
}
