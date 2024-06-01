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

import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.components.navigation.*;
import inc.nomard.spoty.core.startup.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.quotations.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.quotations.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.materialfx.utils.*;
import io.github.palexdev.materialfx.utils.others.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
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
import lombok.extern.java.*;

@SuppressWarnings("unchecked")
@Log
public class QuotationMasterFormController implements Initializable {
    private static QuotationMasterFormController instance;
    @FXML
    public Label quotationFormTitle;
    @FXML
    public MFXFilterComboBox<Customer> customer;
    @FXML
    public MFXTableView<QuotationDetail> quotationDetailTable;
    @FXML
    public MFXTextField quotationNote;
    @FXML
    public BorderPane quotationFormContentPane;
    @FXML
    public MFXFilterComboBox<String> status;
    @FXML
    public Label customerValidationLabel,
            statusValidationLabel;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
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
        customer
                .valueProperty()
                .bindBidirectional(QuotationMasterViewModel.customerProperty());
        quotationNote.textProperty().bindBidirectional(QuotationMasterViewModel.noteProperty());

        // ComboBox Converters.
        StringConverter<Customer> customerConverter =
                FunctionalStringConverter.to(customer -> (customer == null) ? "" : customer.getName());

        // ComboBox Filter Functions.
        Function<String, Predicate<Customer>> customerFilterFunction =
                searchStr ->
                        customer ->
                                StringUtils.containsIgnoreCase(customerConverter.toString(customer), searchStr);

        // Combo box properties.
        customer.setConverter(customerConverter);
        customer.setFilterFunction(customerFilterFunction);
        if (CustomerViewModel.getCustomers().isEmpty()) {
            CustomerViewModel.getCustomers()
                    .addListener(
                            (ListChangeListener<Customer>)
                                    c -> customer.setItems(CustomerViewModel.getCustomers()));
        } else {
            customer.itemsProperty().bindBidirectional(CustomerViewModel.customersProperty());
        }

        status.setItems(FXCollections.observableArrayList(Values.QUOTATION_TYPE));

        // input validators.
        requiredValidator();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<QuotationDetail> productName =
                new MFXTableColumn<>(
                        "Product", false, Comparator.comparing(QuotationDetail::getProductName));
        MFXTableColumn<QuotationDetail> productPrice =
                new MFXTableColumn<>("Price", false, Comparator.comparing(QuotationDetail::getProductPrice));
        MFXTableColumn<QuotationDetail> productQuantity =
                new MFXTableColumn<>("Quantity", false, Comparator.comparing(QuotationDetail::getQuantity));

        productName.setRowCellFactory(
                product -> new MFXTableRowCell<>(QuotationDetail::getProductName));
        productPrice.setRowCellFactory(product -> new MFXTableRowCell<>(QuotationDetail::getProductPrice));
        productQuantity.setRowCellFactory(
                product -> new MFXTableRowCell<>(QuotationDetail::getQuantity));

        productName.prefWidthProperty().bind(quotationDetailTable.widthProperty().multiply(.25));
        productPrice.prefWidthProperty().bind(quotationDetailTable.widthProperty().multiply(.25));
        productQuantity.prefWidthProperty().bind(quotationDetailTable.widthProperty().multiply(.25));

        quotationDetailTable
                .getTableColumns()
                .addAll(productName, productPrice, productQuantity);

        quotationDetailTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Product", QuotationDetail::getProductName),
                        new IntegerFilter<>("Quantity", QuotationDetail::getQuantity),
                        new DoubleFilter<>("Price", QuotationDetail::getProductPrice));

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
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();

        if (!quotationDetailTable.isDisabled()
                && QuotationDetailViewModel.quotationDetailsList.isEmpty()) {
            SpotyMessage notification =
                    new SpotyMessage.MessageBuilder("Table can't be Empty")
                            .duration(MessageDuration.SHORT)
                            .icon("fas-triangle-exclamation")
                            .type(MessageVariants.ERROR)
                            .build();
            notificationHolder.addMessage(notification);
        }
        List<Constraint> customerConstraints = customer.validate();
        List<Constraint> statusConstraints = status.validate();
        if (!customerConstraints.isEmpty()) {
            customerValidationLabel.setManaged(true);
            customerValidationLabel.setVisible(true);
            customerValidationLabel.setText(customerConstraints.getFirst().getMessage());
            customer.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (!statusConstraints.isEmpty()) {
            statusValidationLabel.setManaged(true);
            statusValidationLabel.setVisible(true);
            statusValidationLabel.setText(statusConstraints.getFirst().getMessage());
            status.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (customerConstraints.isEmpty()
                && statusConstraints.isEmpty()) {
            if (QuotationMasterViewModel.getId() > 0) {
                QuotationMasterViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                return;
            }
            QuotationMasterViewModel.saveQuotationMaster(this::onSuccess, this::successMessage, this::errorMessage);
        }
    }

    public void cancelBtnClicked() {
        BaseController.navigation.navigate(Pages.getQuotationPane());

        QuotationMasterViewModel.resetProperties();

        customerValidationLabel.setVisible(false);
        statusValidationLabel.setVisible(false);

        customerValidationLabel.setManaged(false);
        statusValidationLabel.setManaged(false);

        customer.clearSelection();
        status.clearSelection();

        customer.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        status.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);

        customer.clearSelection();
        status.clearSelection();
    }

    public void addBtnClicked() {
        try {
            dialog.showAndWait();
        } catch (Exception e) {
            SpotyLogger.writeToFile(e, QuotationMasterFormController.class);
        }
    }

    private void onSuccess() {
        cancelBtnClicked();
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint customerConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Customer is required")
                        .setCondition(customer.textProperty().length().greaterThan(0))
                        .get();
        customer.getValidator().constraint(customerConstraint);
        Constraint statusConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Color is required")
                        .setCondition(status.textProperty().length().greaterThan(0))
                        .get();
        status.getValidator().constraint(statusConstraint);
        // Display error.
        customer
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                customerValidationLabel.setManaged(false);
                                customerValidationLabel.setVisible(false);
                                customer.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        status
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                statusValidationLabel.setManaged(false);
                                statusValidationLabel.setVisible(false);
                                status.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }

    private void successMessage(String message) {
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
