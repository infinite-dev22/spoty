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

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import static inc.nomard.spoty.core.Validators.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.components.navigation.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.sales.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.sales.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.materialfx.utils.*;
import io.github.palexdev.materialfx.utils.others.*;
import io.github.palexdev.materialfx.validation.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.slf4j.*;

@SuppressWarnings("unchecked")
@Slf4j
public class SaleMasterFormController implements Initializable {
    private static SaleMasterFormController instance;
    @FXML
    public Label title,
            branchValidationLabel,
            customerValidationLabel,
            dateValidationLabel,
            saleStatusValidationLabel,
            paymentStatusValidationLabel;
    @FXML
    public MFXDatePicker date;
    @FXML
    public MFXFilterComboBox<Customer> customer;
    @FXML
    public MFXFilterComboBox<Branch> branch;
    @FXML
    public MFXTableView<SaleDetail> detailTable;
    @FXML
    public MFXTextField note;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXFilterComboBox<String> saleStatus,
            paymentStatus;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
    private MFXStageDialog dialog;
    private List<Constraint> nameConstraints,
            colorConstraints,
            beneficiaryTypeConstraints;

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
        date.textProperty().bindBidirectional(SaleMasterViewModel.dateProperty());
        customer.valueProperty().bindBidirectional(SaleMasterViewModel.customerProperty());
        saleStatus.textProperty().bindBidirectional(SaleMasterViewModel.saleStatusProperty());
        paymentStatus.textProperty().bindBidirectional(SaleMasterViewModel.payStatusProperty());

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
        customer.setItems(CustomerViewModel.getCustomers());
        customer.setConverter(customerConverter);
        customer.setFilterFunction(customerFilterFunction);

        branch.setItems(BranchViewModel.getBranches());
        branch.setConverter(branchConverter);
        branch.setFilterFunction(branchFilterFunction);

        saleStatus.setItems(FXCollections.observableArrayList(Values.SALE_STATUSES));
        paymentStatus.setItems(FXCollections.observableArrayList(Values.PAYMENT_STATUSES));

        // input validators.
        requiredValidator(
                branch, "Branch is required.", branchValidationLabel, saveBtn);
        requiredValidator(
                customer, "Customer is required.", customerValidationLabel, saveBtn);
        requiredValidator(date, "Date is required.", dateValidationLabel, saveBtn);
        requiredValidator(
                saleStatus, "Sale Status is required.", saleStatusValidationLabel, saveBtn);
        requiredValidator(
                paymentStatus,
                "Payment status is required.",
                paymentStatusValidationLabel,
                saveBtn);

        setupTable();
    }

    private void saleProductDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/SaleDetailForm.fxml");
        fxmlLoader.setControllerFactory(c -> SaleDetailFormController.getInstance());

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

    public void saveBtnClicked() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();

        if (!detailTable.isDisabled() && SaleDetailViewModel.saleDetailsList.isEmpty()) {
            SpotyMessage notification =
                    new SpotyMessage.MessageBuilder("Table can't be Empty")
                            .duration(MessageDuration.SHORT)
                            .icon("fas-triangle-exclamation")
                            .type(MessageVariants.ERROR)
                            .build();
            notificationHolder.addMessage(notification);
            return;
        }
        if (!customerValidationLabel.isVisible()
                && !dateValidationLabel.isVisible()
                && !branchValidationLabel.isVisible()
                && !saleStatusValidationLabel.isVisible()
                && !paymentStatusValidationLabel.isVisible()) {
            if (SaleMasterViewModel.getId() > 0) {
                try {
                    SaleMasterViewModel.updateItem(this::onAction, this::onUpdatedSuccess, this::onFailed);
                } catch (Exception e) {
                    SpotyLogger.writeToFile(e, this.getClass());
                }
                return;
            }
            try {
                SaleMasterViewModel.saveSaleMaster(this::onAction, this::onAddSuccess, this::onFailed);
            } catch (Exception e) {
                SpotyLogger.writeToFile(e, this.getClass());
            }
            return;
        }
        onRequiredFieldsMissing();
    }

    public void cancelBtnClicked() {
        BaseController.navigation.navigate(Pages.getSalePane());
        SaleMasterViewModel.resetProperties();

        customer.clearSelection();
        branch.clearSelection();
        saleStatus.clearSelection();
        paymentStatus.clearSelection();

        branchValidationLabel.setVisible(false);
        customerValidationLabel.setVisible(false);
        dateValidationLabel.setVisible(false);
        saleStatusValidationLabel.setVisible(false);
        paymentStatusValidationLabel.setVisible(false);

        branchValidationLabel.setManaged(false);
        customerValidationLabel.setManaged(false);
        dateValidationLabel.setManaged(false);
        saleStatusValidationLabel.setManaged(false);
        paymentStatusValidationLabel.setManaged(false);
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
        product.prefWidthProperty().bind(detailTable.widthProperty().multiply(.25));
        quantity.prefWidthProperty().bind(detailTable.widthProperty().multiply(.25));
        discount.prefWidthProperty().bind(detailTable.widthProperty().multiply(.25));
        tax.prefWidthProperty().bind(detailTable.widthProperty().multiply(.25));
        price.prefWidthProperty().bind(detailTable.widthProperty().multiply(.25));
        totalPrice.prefWidthProperty().bind(detailTable.widthProperty().multiply(.25));

        // Set table filter.
        detailTable
                .getTableColumns()
                .addAll(product, quantity, discount, tax, price, totalPrice);
        detailTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Product", SaleDetail::getProductName),
                        new IntegerFilter<>("Quantity", SaleDetail::getQuantity),
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
                                    change -> detailTable.setItems(SaleDetailViewModel.getSaleDetails()));
        } else {
            detailTable.itemsProperty().bindBidirectional(SaleDetailViewModel.saleDetailsProperty());
        }
    }

    private void styleTable() {
        detailTable.setPrefSize(1000, 1000);
        detailTable.features().enableBounceEffect();
        detailTable.features().enableSmoothScrolling(0.5);

        detailTable.setTableRowFactory(
                saleDetail -> {
                    MFXTableRow<SaleDetail> row = new MFXTableRow<>(detailTable, saleDetail);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<SaleDetail>) event.getSource())
                                        .show(
                                                detailTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<SaleDetail> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(detailTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                event -> {
                    SaleDetailViewModel.removeSaleDetail(
                            obj.getData().getId(),
                            SaleDetailViewModel.saleDetailsList.indexOf(obj.getData()));

                    event.consume();
                });
        // Edit
        edit.setOnAction(
                event -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    SaleDetailViewModel.getSaleDetail(obj.getData());
                                } catch (Exception e) {
                                    SpotyLogger.writeToFile(e, this.getClass());
                                }
                            });

                    dialog.showAndWait();
                    event.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    private void onAction() {
//        cancelBtn.setDisable(true);
//        saveBtn.setDisable(true);
//        cancelBtn.setManaged(true);
//        saveBtn.setManaged(true);
    }

    private void onAddSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Sale added successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtnClicked();

        SaleMasterViewModel.getAllSaleMasters(null, null, null);
    }

    private void onUpdatedSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Sale updated successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtnClicked();

        SaleMasterViewModel.getAllSaleMasters(null, null, null);
    }

    private void onFailed() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("An error occurred")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
        notificationHolder.addMessage(notification);

        SaleMasterViewModel.getAllSaleMasters(null, null, null);
    }

    private void onRequiredFieldsMissing() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Required fields can't be null")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
        notificationHolder.addMessage(notification);

        SaleMasterViewModel.getAllSaleMasters(null, null, null);
    }
}
