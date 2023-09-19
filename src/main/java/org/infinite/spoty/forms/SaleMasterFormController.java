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

package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.LongFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.infinite.spoty.GlobalActions;
import org.infinite.spoty.components.navigation.Pages;
import org.infinite.spoty.components.notification.SimpleNotification;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.components.notification.enums.NotificationDuration;
import org.infinite.spoty.components.notification.enums.NotificationVariants;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.Customer;
import org.infinite.spoty.database.models.SaleDetail;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.values.strings.Values;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.CustomerViewModel;
import org.infinite.spoty.viewModels.SaleDetailViewModel;
import org.infinite.spoty.viewModels.SaleMasterViewModel;
import org.infinite.spoty.views.BaseController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.infinite.spoty.SpotyResourceLoader.fxmlLoader;
import static org.infinite.spoty.Validators.requiredValidator;

@SuppressWarnings("unchecked")
public class SaleMasterFormController implements Initializable {
    private static SaleMasterFormController instance;
    @FXML
    public Label saleFormTitle;
    @FXML
    public MFXDatePicker saleDate;
    @FXML
    public MFXFilterComboBox<Customer> saleCustomer;
    @FXML
    public MFXFilterComboBox<Branch> saleBranch;
    @FXML
    public MFXTableView<SaleDetail> saleDetailTable;
    @FXML
    public MFXTextField saleNote;
    @FXML
    public BorderPane saleFormContentPane;
    @FXML
    public MFXFilterComboBox<String> saleStatus;
    @FXML
    public MFXFilterComboBox<String> salePaymentStatus;
    @FXML
    public Label saleBranchValidationLabel;
    @FXML
    public Label saleCustomerValidationLabel;
    @FXML
    public Label saleDateValidationLabel;
    @FXML
    public Label saleStatusValidationLabel;
    @FXML
    public Label salePaymentStatusValidationLabel;
    @FXML
    public MFXButton saleMasterSaveBtn;
    private MFXStageDialog dialog;

    private SaleMasterFormController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        saleProductDialogPane(stage);
                    } catch (IOException e) {
                        SpotyLogger.writeToFile(e, this.getClass());
                    }
                });
    }

    public static SaleMasterFormController getInstance(Stage stage) {
        if (instance == null) instance = new SaleMasterFormController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bi~Directional Binding.
        saleDate.textProperty().bindBidirectional(SaleMasterViewModel.dateProperty());
        saleCustomer.valueProperty().bindBidirectional(SaleMasterViewModel.customerProperty());
        saleBranch.valueProperty().bindBidirectional(SaleMasterViewModel.branchProperty());
        saleStatus.textProperty().bindBidirectional(SaleMasterViewModel.saleStatusProperty());
        salePaymentStatus.textProperty().bindBidirectional(SaleMasterViewModel.payStatusProperty());

        // ComboBox Converters.
        StringConverter<Customer> customerConverter =
                FunctionalStringConverter.to(customer -> (customer == null) ? "" : customer.getName());

        StringConverter<Branch> branchConverter =
                FunctionalStringConverter.to(branch -> (branch == null) ? "" : branch.getName());

        // ComboBox Filter Functions.
        Function<String, Predicate<Customer>> customerFilterFunction =
                searchStr ->
                        customer ->
                                StringUtils.containsIgnoreCase(customerConverter.toString(customer), searchStr);

        Function<String, Predicate<Branch>> branchFilterFunction =
                searchStr ->
                        branch -> StringUtils.containsIgnoreCase(branchConverter.toString(branch), searchStr);

        // Set items to combo boxes and display custom text.
        saleCustomer.setItems(CustomerViewModel.getCustomers());
        saleCustomer.setConverter(customerConverter);
        saleCustomer.setFilterFunction(customerFilterFunction);

        saleBranch.setItems(BranchViewModel.getBranches());
        saleBranch.setConverter(branchConverter);
        saleBranch.setFilterFunction(branchFilterFunction);

        saleStatus.setItems(FXCollections.observableArrayList(Values.SALE_STATUSES));
        salePaymentStatus.setItems(FXCollections.observableArrayList(Values.PAYMENT_STATUSES));

        // input validators.
        requiredValidator(
                saleBranch, "Branch is required.", saleBranchValidationLabel, saleMasterSaveBtn);
        requiredValidator(
                saleCustomer, "Customer is required.", saleCustomerValidationLabel, saleMasterSaveBtn);
        requiredValidator(saleDate, "Date is required.", saleDateValidationLabel, saleMasterSaveBtn);
        requiredValidator(
                saleStatus, "Sale Status is required.", saleStatusValidationLabel, saleMasterSaveBtn);
        requiredValidator(
                salePaymentStatus,
                "Payment status is required.",
                salePaymentStatusValidationLabel,
                saleMasterSaveBtn);

        setupTable();
    }

    private void saleProductDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("forms/SaleDetailForm.fxml");
        fxmlLoader.setControllerFactory(c -> SaleDetailFormController.getInstance());

        MFXGenericDialog dialogContent = fxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(saleFormContentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void saveBtnClicked() {
        SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

        if (!saleDetailTable.isDisabled() && SaleDetailViewModel.saleDetailList.isEmpty()) {
            SimpleNotification notification =
                    new SimpleNotification.NotificationBuilder("Table can't be Empty")
                            .duration(NotificationDuration.SHORT)
                            .icon("fas-triangle-exclamation")
                            .type(NotificationVariants.ERROR)
                            .build();
            notificationHolder.addNotification(notification);
            return;
        }
        if (!saleCustomerValidationLabel.isVisible()
                && !saleDateValidationLabel.isVisible()
                && !saleBranchValidationLabel.isVisible()
                && !saleStatusValidationLabel.isVisible()
                && !salePaymentStatusValidationLabel.isVisible()) {
            if (SaleMasterViewModel.getId() > 0) {
                GlobalActions.spotyThreadPool()
                        .execute(
                                () -> {
                                    try {
                                        SaleMasterViewModel.updateItem(SaleMasterViewModel.getId());
                                    } catch (SQLException e) {
                                        SpotyLogger.writeToFile(e, this.getClass());
                                    }
                                });

                SimpleNotification notification =
                        new SimpleNotification.NotificationBuilder("Sale updated successfully")
                                .duration(NotificationDuration.MEDIUM)
                                .icon("fas-circle-check")
                                .type(NotificationVariants.SUCCESS)
                                .build();
                notificationHolder.addNotification(notification);

                saleCustomer.clearSelection();
                saleBranch.clearSelection();
                saleStatus.clearSelection();
                salePaymentStatus.clearSelection();

                cancelBtnClicked();
                return;
            }
            GlobalActions.spotyThreadPool()
                    .execute(
                            () -> {
                                try {
                                    SaleMasterViewModel.saveSaleMaster();
                                } catch (SQLException e) {
                                    SpotyLogger.writeToFile(e, this.getClass());
                                }
                            });

            SimpleNotification notification =
                    new SimpleNotification.NotificationBuilder("Sale saved successfully")
                            .duration(NotificationDuration.MEDIUM)
                            .icon("fas-circle-check")
                            .type(NotificationVariants.SUCCESS)
                            .build();
            notificationHolder.addNotification(notification);

            saleCustomer.clearSelection();
            saleBranch.clearSelection();
            saleStatus.clearSelection();
            salePaymentStatus.clearSelection();

            cancelBtnClicked();
            return;
        }
        SimpleNotification notification =
                new SimpleNotification.NotificationBuilder("Required fields missing")
                        .duration(NotificationDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(NotificationVariants.ERROR)
                        .build();
        notificationHolder.addNotification(notification);
    }

    public void cancelBtnClicked() {
        BaseController.navigation.navigate(Pages.getSalePane());
        SaleMasterViewModel.resetProperties();

        saleCustomer.clearSelection();
        saleBranch.clearSelection();
        saleStatus.clearSelection();
        salePaymentStatus.clearSelection();

        saleBranchValidationLabel.setVisible(false);
        saleCustomerValidationLabel.setVisible(false);
        saleDateValidationLabel.setVisible(false);
        saleStatusValidationLabel.setVisible(false);
        salePaymentStatusValidationLabel.setVisible(false);
    }

    public void addBtnClicked() {
        dialog.showAndWait();
    }

    private void setupTable() {
        // Set table column titles.
        MFXTableColumn<SaleDetail> product =
                new MFXTableColumn<>("Product", false, Comparator.comparing(SaleDetail::getProductName));
        MFXTableColumn<SaleDetail> quantity =
                new MFXTableColumn<>("Quantity", false, Comparator.comparing(SaleDetail::getQuantity));
        MFXTableColumn<SaleDetail> discount =
                new MFXTableColumn<>("Discount", false, Comparator.comparing(SaleDetail::getDiscount));
        MFXTableColumn<SaleDetail> tax =
                new MFXTableColumn<>("Tax", false, Comparator.comparing(SaleDetail::getNetTax));
        MFXTableColumn<SaleDetail> price =
                new MFXTableColumn<>("Price", false, Comparator.comparing(SaleDetail::getPrice));
        MFXTableColumn<SaleDetail> totalPrice =
                new MFXTableColumn<>(
                        "Total Price", false, Comparator.comparing(SaleDetail::getSubTotalPrice));

        // Set table column data.
        product.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(SaleDetail::getProductName));
        quantity.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(SaleDetail::getQuantity));
        discount.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(SaleDetail::getDiscount));
        tax.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(SaleDetail::getNetTax));
        price.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(SaleDetail::getPrice));
        totalPrice.setRowCellFactory(
                purchaseDetail -> new MFXTableRowCell<>(SaleDetail::getSubTotalPrice));

        // Set table column width.
        product.prefWidthProperty().bind(saleDetailTable.widthProperty().multiply(.25));
        quantity.prefWidthProperty().bind(saleDetailTable.widthProperty().multiply(.25));
        discount.prefWidthProperty().bind(saleDetailTable.widthProperty().multiply(.25));
        tax.prefWidthProperty().bind(saleDetailTable.widthProperty().multiply(.25));
        price.prefWidthProperty().bind(saleDetailTable.widthProperty().multiply(.25));
        totalPrice.prefWidthProperty().bind(saleDetailTable.widthProperty().multiply(.25));

        // Set table filter.
        saleDetailTable
                .getTableColumns()
                .addAll(product, quantity, discount, tax, price, totalPrice);
        saleDetailTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Product", SaleDetail::getProductName),
                        new LongFilter<>("Quantity", SaleDetail::getQuantity),
                        new DoubleFilter<>("Discount", SaleDetail::getDiscount),
                        new DoubleFilter<>("Tax", SaleDetail::getNetTax),
                        new DoubleFilter<>("Price", SaleDetail::getPrice),
                        new DoubleFilter<>("Total Price", SaleDetail::getSubTotalPrice));

        styleTable();

        // Populate table.
        if (SaleDetailViewModel.getSaleDetails().isEmpty()) {
            SaleDetailViewModel.getSaleDetails()
                    .addListener(
                            (ListChangeListener<SaleDetail>)
                                    change -> saleDetailTable.setItems(SaleDetailViewModel.getSaleDetails()));
        } else {
            saleDetailTable.itemsProperty().bindBidirectional(SaleDetailViewModel.saleDetailsProperty());
        }
    }

    private void styleTable() {
        saleDetailTable.setPrefSize(1000, 1000);
        saleDetailTable.features().enableBounceEffect();
        saleDetailTable.features().enableSmoothScrolling(0.5);

        saleDetailTable.setTableRowFactory(
                saleDetail -> {
                    MFXTableRow<SaleDetail> row = new MFXTableRow<>(saleDetailTable, saleDetail);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<SaleDetail>) event.getSource())
                                        .show(
                                                saleDetailTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<SaleDetail> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(saleDetailTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                event -> {
                    SaleDetailViewModel.removeSaleDetail(
                            obj.getData().getId(),
                            SaleDetailViewModel.saleDetailList.indexOf(obj.getData()));

                    event.consume();
                });
        // Edit
        edit.setOnAction(
                event -> {
                    GlobalActions.spotyThreadPool()
                            .execute(
                                    () -> {
                                        try {
                                            SaleDetailViewModel.getSaleDetail(obj.getData());
                                        } catch (SQLException e) {
                                            SpotyLogger.writeToFile(e, this.getClass());
                                        }
                                    });

                    dialog.showAndWait();
                    event.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }
}
