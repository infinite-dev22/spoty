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

package inc.nomard.spoty.core.views.forms;

import inc.nomard.spoty.core.components.navigation.Pages;
import inc.nomard.spoty.core.components.notification.SimpleNotification;
import inc.nomard.spoty.core.components.notification.SimpleNotificationHolder;
import inc.nomard.spoty.core.components.notification.enums.NotificationDuration;
import inc.nomard.spoty.core.components.notification.enums.NotificationVariants;
import inc.nomard.spoty.core.startup.Dialogs;
import inc.nomard.spoty.core.values.strings.Values;
import inc.nomard.spoty.core.viewModels.BranchViewModel;
import inc.nomard.spoty.core.viewModels.CustomerViewModel;
import inc.nomard.spoty.core.viewModels.quotations.QuotationDetailViewModel;
import inc.nomard.spoty.core.viewModels.quotations.QuotationMasterViewModel;
import inc.nomard.spoty.core.views.BaseController;
import inc.nomard.spoty.network_bridge.dtos.Branch;
import inc.nomard.spoty.network_bridge.dtos.Customer;
import inc.nomard.spoty.network_bridge.dtos.quotations.QuotationDetail;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

import static inc.nomard.spoty.core.Validators.requiredValidator;

@SuppressWarnings("unchecked")
public class QuotationMasterFormController implements Initializable {
    private static QuotationMasterFormController instance;
    @FXML
    public Label quotationFormTitle;
    @FXML
    public MFXDatePicker quotationDate;
    @FXML
    public MFXFilterComboBox<Customer> quotationCustomer;
    @FXML
    public MFXFilterComboBox<Branch> quotationBranch;
    @FXML
    public MFXTableView<QuotationDetail> quotationDetailTable;
    @FXML
    public MFXTextField quotationNote;
    @FXML
    public BorderPane quotationFormContentPane;
    @FXML
    public MFXFilterComboBox<String> quotationStatus;
    @FXML
    public Label quotationDateValidationLabel;
    @FXML
    public Label quotationCustomerValidationLabel;
    @FXML
    public Label quotationBranchValidationLabel;
    @FXML
    public Label quotationStatusValidationLabel;
    @FXML
    public MFXButton quotationMasterSaveBtn;
    private MFXStageDialog dialog;

    private QuotationMasterFormController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        quotationProductDialogPane(stage);
                    } catch (IOException e) {
                        SpotyLogger.writeToFile(e, this.getClass());
                    }
                });
    }

    public static QuotationMasterFormController getInstance(Stage stage) {
        if (instance == null) instance = new QuotationMasterFormController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form input binding.
        quotationDate.textProperty().bindBidirectional(QuotationMasterViewModel.dateProperty());
        quotationCustomer
                .valueProperty()
                .bindBidirectional(QuotationMasterViewModel.customerProperty());
        quotationBranch.valueProperty().bindBidirectional(QuotationMasterViewModel.branchProperty());
        quotationStatus.textProperty().bindBidirectional(QuotationMasterViewModel.statusProperty());
        quotationNote.textProperty().bindBidirectional(QuotationMasterViewModel.noteProperty());

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

        // Combo box properties.
        quotationCustomer.setItems(CustomerViewModel.getCustomers());
        quotationCustomer.setConverter(customerConverter);
        quotationCustomer.setFilterFunction(customerFilterFunction);

        quotationBranch.setItems(BranchViewModel.getBranches());
        quotationBranch.setConverter(branchConverter);
        quotationBranch.setFilterFunction(branchFilterFunction);

        quotationStatus.setItems(FXCollections.observableArrayList(Values.QUOTATION_TYPE));

        // input validators.
        requiredValidator(
                quotationBranch,
                "Branch is required.",
                quotationBranchValidationLabel,
                quotationMasterSaveBtn);
        requiredValidator(
                quotationCustomer,
                "Customer is required.",
                quotationCustomerValidationLabel,
                quotationMasterSaveBtn);
        requiredValidator(
                quotationDate, "Date is required.", quotationDateValidationLabel, quotationMasterSaveBtn);
        requiredValidator(
                quotationStatus,
                "Status is required.",
                quotationStatusValidationLabel,
                quotationMasterSaveBtn);

        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<QuotationDetail> productName =
                new MFXTableColumn<>(
                        "Product", false, Comparator.comparing(QuotationDetail::getProductName));
        MFXTableColumn<QuotationDetail> productQuantity =
                new MFXTableColumn<>("Quantity", false, Comparator.comparing(QuotationDetail::getQuantity));
        MFXTableColumn<QuotationDetail> productDiscount =
                new MFXTableColumn<>("Discount", false, Comparator.comparing(QuotationDetail::getDiscount));
        MFXTableColumn<QuotationDetail> productTax =
                new MFXTableColumn<>("Tax", false, Comparator.comparing(QuotationDetail::getNetTax));
        MFXTableColumn<QuotationDetail> productPrice =
                new MFXTableColumn<>("Price", false, Comparator.comparing(QuotationDetail::getPrice));
        MFXTableColumn<QuotationDetail> totalPrice =
                new MFXTableColumn<>(
                        "Total Price", false, Comparator.comparing(QuotationDetail::getTotal));

        productName.setRowCellFactory(
                product -> new MFXTableRowCell<>(QuotationDetail::getProductName));
        productQuantity.setRowCellFactory(
                product -> new MFXTableRowCell<>(QuotationDetail::getQuantity));
        productDiscount.setRowCellFactory(
                product -> new MFXTableRowCell<>(QuotationDetail::getDiscount));
        productTax.setRowCellFactory(product -> new MFXTableRowCell<>(QuotationDetail::getNetTax));
        productPrice.setRowCellFactory(product -> new MFXTableRowCell<>(QuotationDetail::getPrice));
        totalPrice.setRowCellFactory(product -> new MFXTableRowCell<>(QuotationDetail::getTotal));

        productName.prefWidthProperty().bind(quotationDetailTable.widthProperty().multiply(.25));
        productQuantity.prefWidthProperty().bind(quotationDetailTable.widthProperty().multiply(.25));
        productDiscount.prefWidthProperty().bind(quotationDetailTable.widthProperty().multiply(.25));
        productTax.prefWidthProperty().bind(quotationDetailTable.widthProperty().multiply(.25));
        productPrice.prefWidthProperty().bind(quotationDetailTable.widthProperty().multiply(.25));
        totalPrice.prefWidthProperty().bind(quotationDetailTable.widthProperty().multiply(.25));

        quotationDetailTable
                .getTableColumns()
                .addAll(
                        productName, productQuantity, productDiscount, productTax, productPrice, totalPrice);

        quotationDetailTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Product", QuotationDetail::getProductName),
                        new IntegerFilter<>("Quantity", QuotationDetail::getQuantity),
                        new DoubleFilter<>("Discount", QuotationDetail::getDiscount),
                        new DoubleFilter<>("Tax", QuotationDetail::getNetTax),
                        new DoubleFilter<>("Price", QuotationDetail::getPrice),
                        new DoubleFilter<>("Total Price", QuotationDetail::getTotal));

        getQuotationDetailTable();

        if (QuotationDetailViewModel.getQuotationDetails().isEmpty()) {
            QuotationDetailViewModel.getQuotationDetails()
                    .addListener(
                            (ListChangeListener<QuotationDetail>)
                                    change ->
                                            quotationDetailTable.setItems(
                                                    QuotationDetailViewModel.getQuotationDetails()));
        } else {
            quotationDetailTable
                    .itemsProperty()
                    .bindBidirectional(QuotationDetailViewModel.quotationDetailsProperty());
        }
    }

    private void getQuotationDetailTable() {
        quotationDetailTable.setPrefSize(1000, 1000);
        quotationDetailTable.features().enableBounceEffect();
        quotationDetailTable.features().enableSmoothScrolling(0.5);

        quotationDetailTable.setTableRowFactory(
                quotationDetail -> {
                    MFXTableRow<QuotationDetail> row = new MFXTableRow<>(quotationDetailTable, quotationDetail);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<QuotationDetail>) event.getSource())
                                        .show(
                                                quotationDetailTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<QuotationDetail> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(quotationDetailTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                event -> {
                    QuotationDetailViewModel.removeQuotationDetail(
                            obj.getData().getId(),
                            QuotationDetailViewModel.quotationDetailsList.indexOf(obj.getData()));
                    event.consume();
                });
        // Edit
        edit.setOnAction(
                event -> {
                    QuotationDetailViewModel.getQuotationDetail(obj.getData());
                    dialog.showAndWait();
                    event.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    private void quotationProductDialogPane(Stage stage) throws IOException {
        try {
            dialog =
                    MFXGenericDialogBuilder.build(Dialogs.getQuotationDialogContent())
                            .toStageDialogBuilder()
                            .initOwner(stage)
                            .initModality(Modality.WINDOW_MODAL)
                            .setOwnerNode(quotationFormContentPane)
                            .setScrimPriority(ScrimPriority.WINDOW)
                            .setScrimOwner(true)
                            .get();

            io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
        } catch (Exception e) {
            SpotyLogger.writeToFile(e, QuotationMasterFormController.class);
        }
    }

    public void saveBtnClicked() {
        SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

        if (!quotationDetailTable.isDisabled()
                && QuotationDetailViewModel.quotationDetailsList.isEmpty()) {
            SimpleNotification notification =
                    new SimpleNotification.NotificationBuilder("Table can't be Empty")
                            .duration(NotificationDuration.SHORT)
                            .icon("fas-triangle-exclamation")
                            .type(NotificationVariants.ERROR)
                            .build();
            notificationHolder.addNotification(notification);
            return;
        }

        if (!quotationBranchValidationLabel.isVisible()
                && !quotationCustomerValidationLabel.isVisible()
                && !quotationDateValidationLabel.isVisible()
                && !quotationStatusValidationLabel.isVisible()) {
            if (QuotationMasterViewModel.getId() > 0) {
                SpotyThreader.spotyThreadPool(() -> {
                    try {
                        QuotationMasterViewModel.updateItem(this::onAction, this::onSuccess, this::onFailed);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, this.getClass());
                    }
                });

                SimpleNotification notification =
                        new SimpleNotification.NotificationBuilder("Quotation updated successfully")
                                .duration(NotificationDuration.MEDIUM)
                                .icon("fas-circle-check")
                                .type(NotificationVariants.SUCCESS)
                                .build();
                notificationHolder.addNotification(notification);

                quotationCustomer.clearSelection();
                quotationBranch.clearSelection();
                quotationStatus.clearSelection();

                cancelBtnClicked();
                return;
            }
            SpotyThreader.spotyThreadPool(() -> {
                try {
                    QuotationMasterViewModel.saveQuotationMaster(this::onAction, this::onSuccess, this::onFailed);
                } catch (Exception e) {
                    SpotyLogger.writeToFile(e, this.getClass());
                }
            });

            SimpleNotification notification =
                    new SimpleNotification.NotificationBuilder("Quotation saved successfully")
                            .duration(NotificationDuration.MEDIUM)
                            .icon("fas-circle-check")
                            .type(NotificationVariants.SUCCESS)
                            .build();
            notificationHolder.addNotification(notification);

            quotationCustomer.clearSelection();
            quotationBranch.clearSelection();
            quotationStatus.clearSelection();

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
        BaseController.navigation.navigate(Pages.getQuotationPane());

        QuotationMasterViewModel.resetProperties();

        quotationBranchValidationLabel.setVisible(false);
        quotationCustomerValidationLabel.setVisible(false);
        quotationDateValidationLabel.setVisible(false);
        quotationStatusValidationLabel.setVisible(false);
        quotationCustomer.clearSelection();
        quotationBranch.clearSelection();
        quotationStatus.clearSelection();
    }

    public void addBtnClicked() {
        try {
            dialog.showAndWait();
        } catch (Exception e) {
            SpotyLogger.writeToFile(e, QuotationMasterFormController.class);
        }
    }

    private void onAction() {
        System.out.println("Loading quotation master...");
    }

    private void onSuccess() {
        System.out.println("Loaded quotation master...");
    }

    private void onFailed() {
        System.out.println("failed loading quotation master...");
    }
}