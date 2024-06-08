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

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.components.navigation.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.transfers.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.transfers.*;
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
public class TransferMasterFormController implements Initializable {
    private static TransferMasterFormController instance;
    private final Stage stage;
    @FXML
    public MFXFilterComboBox<Branch> fromBranch,
            toBranch;
    @FXML
    public MFXDatePicker date;
    @FXML
    public MFXTableView<TransferDetail> detailTable;
    @FXML
    public MFXTextField note;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXButton addBtn,
            saveBtn,
            cancelBtn;
    @FXML
    public Label title,
            dateValidationLabel,
            toBranchValidationLabel,
            fromBranchValidationLabel;
    private MFXStageDialog dialog;

    private TransferMasterFormController(Stage stage) {
        this.stage = stage;
        Platform.runLater(
                () -> {
                    try {
                        quotationProductDialogPane();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static TransferMasterFormController getInstance(Stage stage) {
        if (instance == null) {
            synchronized (TransferMasterFormController.class) {
                instance = new TransferMasterFormController(stage);
            }
        }
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input binding.
        fromBranch
                .valueProperty()
                .bindBidirectional(TransferMasterViewModel.fromBranchProperty());
        toBranch
                .valueProperty()
                .bindBidirectional(TransferMasterViewModel.toBranchProperty());
        date.textProperty().bindBidirectional(TransferMasterViewModel.dateProperty());
        note.textProperty().bindBidirectional(TransferMasterViewModel.noteProperty());

        // ComboBox Converters.
        StringConverter<Branch> branchConverter =
                FunctionalStringConverter.to(branch -> (branch == null) ? "" : branch.getName());

        // ComboBox Filter Functions.
        Function<String, Predicate<Branch>> branchFilterFunction =
                searchStr ->
                        branch -> StringUtils.containsIgnoreCase(branchConverter.toString(branch), searchStr);

        // ComboBox properties.
        fromBranch.setItems(BranchViewModel.getBranches());
        fromBranch.setConverter(branchConverter);
        fromBranch.setFilterFunction(branchFilterFunction);

        toBranch.setItems(BranchViewModel.getBranches());
        toBranch.setConverter(branchConverter);
        toBranch.setFilterFunction(branchFilterFunction);

        // input validators.
        requiredValidator();

        transferMasterAddProductBtnClicked();

        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<TransferDetail> productName =
                new MFXTableColumn<>(
                        "Product", false, Comparator.comparing(TransferDetail::getProductName));
        MFXTableColumn<TransferDetail> stock =
                new MFXTableColumn<>(
                        "Stock", false, Comparator.comparing(TransferDetail::getProductQuantity));
        MFXTableColumn<TransferDetail> productQuantity =
                new MFXTableColumn<>("Quantity", false, Comparator.comparing(TransferDetail::getQuantity));

        productName.setRowCellFactory(product -> new MFXTableRowCell<>(TransferDetail::getProductName));
        productQuantity.setRowCellFactory(
                product -> new MFXTableRowCell<>(TransferDetail::getQuantity));
        stock.setRowCellFactory(
                product -> new MFXTableRowCell<>(TransferDetail::getProductQuantity));

        productName.prefWidthProperty().bind(detailTable.widthProperty().multiply(.5));
        productQuantity.prefWidthProperty().bind(detailTable.widthProperty().multiply(.5));
        stock.prefWidthProperty().bind(detailTable.widthProperty().multiply(.5));

        detailTable.getTableColumns().addAll(productName, productQuantity, stock);

        detailTable
                .getFilters()
                .addAll(new StringFilter<>("Name", TransferDetail::getProductName));

        getTransferDetailTable();

        if (TransferDetailViewModel.getTransferDetails().isEmpty()) {
            TransferDetailViewModel.getTransferDetails()
                    .addListener(
                            (ListChangeListener<TransferDetail>)
                                    change -> detailTable.setItems(TransferDetailViewModel.getTransferDetails()));
        } else {
            detailTable
                    .itemsProperty()
                    .bindBidirectional(TransferDetailViewModel.transferDetailsProperty());
        }
    }

    private void getTransferDetailTable() {
        detailTable.setPrefSize(1000, 1000);
        detailTable.features().enableBounceEffect();
        detailTable.features().enableSmoothScrolling(0.5);

        detailTable.setTableRowFactory(
                transferDetail -> {
                    MFXTableRow<TransferDetail> row = new MFXTableRow<>(detailTable, transferDetail);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<TransferDetail>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<TransferDetail> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(detailTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                event -> {
                    SpotyThreader.spotyThreadPool(
                            () ->
                                    TransferDetailViewModel.removeTransferDetail(
                                            obj.getData().getId(),
                                            TransferDetailViewModel.transferDetailsList.indexOf(obj.getData())));
                    event.consume();
                });
        // Edit
        edit.setOnAction(
                event -> {
                    SpotyThreader.spotyThreadPool(
                            () -> {
                                try {
                                    TransferDetailViewModel.getTransferDetail(obj.getData());
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

    private void transferMasterAddProductBtnClicked() {
        addBtn.setOnAction(e -> dialog.showAndWait());
    }

    private void quotationProductDialogPane() throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/TransferDetailForm.fxml");
        fxmlLoader.setControllerFactory(c -> TransferDetailFormController.getInstance(stage));

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

        if (!detailTable.isDisabled()
                && TransferDetailViewModel.transferDetailsList.isEmpty()) {
            SpotyMessage notification =
                    new SpotyMessage.MessageBuilder("Table can't be Empty")
                            .duration(MessageDuration.SHORT)
                            .icon("fas-triangle-exclamation")
                            .type(MessageVariants.ERROR)
                            .build();
        }
        List<Constraint> dateConstraints = date.validate();
        List<Constraint> toBranchConstraints = toBranch.validate();
        List<Constraint> fromBranchConstraints = fromBranch.validate();
        if (!dateConstraints.isEmpty()) {
            dateValidationLabel.setManaged(true);
            dateValidationLabel.setVisible(true);
            dateValidationLabel.setText(dateConstraints.getFirst().getMessage());
            date.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (!toBranchConstraints.isEmpty()) {
            toBranchValidationLabel.setManaged(true);
            toBranchValidationLabel.setVisible(true);
            toBranchValidationLabel.setText(toBranchConstraints.getFirst().getMessage());
            toBranch.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (!fromBranchConstraints.isEmpty()) {
            fromBranchValidationLabel.setManaged(true);
            fromBranchValidationLabel.setVisible(true);
            fromBranchValidationLabel.setText(fromBranchConstraints.getFirst().getMessage());
            fromBranch.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (dateConstraints.isEmpty()
                && toBranchConstraints.isEmpty()
                && fromBranchConstraints.isEmpty()) {
            if (TransferMasterViewModel.getId() > 0) {
                TransferMasterViewModel.updateTransfer(this::onSuccess, this::successMessage, this::errorMessage);
                return;
            }
            TransferMasterViewModel.saveTransferMaster(this::onSuccess, this::successMessage, this::errorMessage);
        }
    }

    public void cancelBtnClicked() {
        BaseController.navigation.navigate(Pages.getTransferPane());
        TransferMasterViewModel.resetProperties();
        toBranchValidationLabel.setVisible(false);
        fromBranchValidationLabel.setVisible(false);
        dateValidationLabel.setVisible(false);
        toBranchValidationLabel.setManaged(false);
        fromBranchValidationLabel.setManaged(false);
        dateValidationLabel.setManaged(false);
        date.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        toBranch.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        fromBranch.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        fromBranch.clearSelection();
        toBranch.clearSelection();
        date.setValue(null);
    }

    private void onSuccess() {
        cancelBtnClicked();
        TransferMasterViewModel.resetProperties();
        TransferMasterViewModel.getAllTransferMasters(null, null);
        ProductViewModel.getAllProducts(null, null);
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint dateConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Date is required")
                        .setCondition(date.textProperty().length().greaterThan(0))
                        .get();
        date.getValidator().constraint(dateConstraint);
        Constraint fromBranchConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("From Branch is required")
                        .setCondition(fromBranch.textProperty().length().greaterThan(0))
                        .get();
        fromBranch.getValidator().constraint(fromBranchConstraint);
        Constraint toBranchConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("To Branch is required")
                        .setCondition(toBranch.textProperty().length().greaterThan(0))
                        .get();
        toBranch.getValidator().constraint(toBranchConstraint);
        // Display error.
        date
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                dateValidationLabel.setManaged(false);
                                dateValidationLabel.setVisible(false);
                                date.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        fromBranch
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                fromBranchValidationLabel.setManaged(false);
                                fromBranchValidationLabel.setVisible(false);
                                fromBranch.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        toBranch
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                toBranchValidationLabel.setManaged(false);
                                toBranchValidationLabel.setVisible(false);
                                toBranch.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
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
}
