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
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXElevatedButton;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.database.dao.TransferDetailDao;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.TransferDetail;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.TransferDetailViewModel;
import org.infinite.spoty.viewModels.TransferMasterViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

@SuppressWarnings("unchecked")
public class TransferMasterFormController implements Initializable {
    public MFXTextField transferDetailID = new MFXTextField();
    public MFXTextField transferMasterID = new MFXTextField();
    @FXML
    public MFXFilterComboBox<Branch> transferMasterFromBranchId;
    public MFXFilterComboBox<Branch> transferMasterToBranchId;
    @FXML
    public MFXDatePicker transferMasterDate;
    @FXML
    public MFXTableView<TransferDetail> transferDetailTable;
    @FXML
    public MFXTextField transferMasterNote;
    @FXML
    public AnchorPane transferMasterFormContentPane;
    @FXML
    public Label transferMasterFormTitle;
    @FXML
    public MFXElevatedButton transferMasterProductAddBtn;
    private Dialog<ButtonType> dialog;

    public TransferMasterFormController(Stage stage) {
        Platform.runLater(() -> {
            try {
                quotationProductDialogPane(stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input listeners.
        transferMasterFromBranchId.textProperty().addListener((observable, oldValue, newValue) -> transferMasterFromBranchId.setTrailingIcon(null));
        transferMasterToBranchId.textProperty().addListener((observable, oldValue, newValue) -> transferMasterToBranchId.setTrailingIcon(null));
        transferMasterDate.textProperty().addListener((observable, oldValue, newValue) -> transferMasterDate.setTrailingIcon(null));
        // Input binding.
        transferMasterID.textProperty().bindBidirectional(TransferMasterViewModel.idProperty(), new NumberStringConverter());
        transferMasterFromBranchId.valueProperty().bindBidirectional(TransferMasterViewModel.fromBranchProperty());
        transferMasterFromBranchId.setItems(BranchViewModel.branchesList);
        transferMasterFromBranchId.setConverter(new StringConverter<>() {
            @Override
            public String toString(Branch object) {
                if (object != null)
                    return object.getName();
                else
                    return null;
            }

            @Override
            public Branch fromString(String string) {
                return null;
            }
        });
        transferMasterToBranchId.valueProperty().bindBidirectional(TransferMasterViewModel.toBranchProperty());
        transferMasterToBranchId.setItems(BranchViewModel.branchesList);
        transferMasterToBranchId.setConverter(new StringConverter<>() {
            @Override
            public String toString(Branch object) {
                if (object != null)
                    return object.getName();
                else
                    return null;
            }

            @Override
            public Branch fromString(String string) {
                return null;
            }
        });
        transferMasterDate.textProperty().bindBidirectional(TransferMasterViewModel.dateProperty());
        transferMasterNote.textProperty().bindBidirectional(TransferMasterViewModel.noteProperty());
        transferMasterAddProductBtnClicked();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<TransferDetail> productName = new MFXTableColumn<>("Product", false, Comparator.comparing(TransferDetail::getProductDetailName));
        MFXTableColumn<TransferDetail> productQuantity = new MFXTableColumn<>("Quantity", false, Comparator.comparing(TransferDetail::getQuantity));
//        MFXTableColumn<TransferDetail> transferMasterType = new MFXTableColumn<>("Transfer Type", false, Comparator.comparing(TransferDetail::getTransferType));

        productName.setRowCellFactory(product -> new MFXTableRowCell<>(TransferDetail::getProductDetailName));
        productQuantity.setRowCellFactory(product -> new MFXTableRowCell<>(TransferDetail::getQuantity));
//        transferMasterType.setRowCellFactory(product -> new MFXTableRowCell<>(TransferDetail::getTransferType));

        productName.prefWidthProperty().bind(transferDetailTable.widthProperty().multiply(.4));
        productQuantity.prefWidthProperty().bind(transferDetailTable.widthProperty().multiply(.4));
//        transferMasterType.prefWidthProperty().bind(transferMasterProductsTable.widthProperty().multiply(.4));

        transferDetailTable.getTableColumns().addAll(productName, productQuantity); // , transferMasterType);
        transferDetailTable.getFilters().addAll(
                new StringFilter<>("Name", TransferDetail::getProductDetailName),
                new IntegerFilter<>("Quantity", TransferDetail::getQuantity)
//                new StringFilter<>("Category", TransferDetail::getTransferType)
        );
        getTransferDetailTable();
        transferDetailTable.setItems(TransferDetailViewModel.transferDetailsTempList);
    }

    private void getTransferDetailTable() {
        transferDetailTable.setPrefSize(1000, 1000);
        transferDetailTable.features().enableBounceEffect();
        transferDetailTable.features().enableSmoothScrolling(0.5);

        transferDetailTable.setTableRowFactory(t -> {
            MFXTableRow<TransferDetail> row = new MFXTableRow<>(transferDetailTable, t);
            EventHandler<ContextMenuEvent> eventHandler = event -> {
                showContextMenu((MFXTableRow<TransferDetail>) event.getSource())
                        .show(transferDetailTable.getScene().getWindow(), event.getScreenX(), event.getScreenY());
                event.consume();
            };
            row.setOnContextMenuRequested(eventHandler);
            return row;
        });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<TransferDetail> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(transferDetailTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(e -> {
            TransferDetailViewModel.getItem(obj.getData(), TransferDetailViewModel.transferDetailsTempList.indexOf(obj.getData()));
            try {
                if (Integer.parseInt(transferDetailID.getText()) > 0)
                    TransferDetailDao.deleteTransferDetail(Integer.parseInt(transferDetailID.getText()));
            } catch (NumberFormatException ignored) {
                TransferDetailViewModel.removeTransferDetail(TransferDetailViewModel.transferDetailsTempList.indexOf(obj.getData()));
            }
            TransferDetailViewModel.getTransferDetails();
            e.consume();
        });
        // Edit
        edit.setOnAction(e -> {
            TransferDetailViewModel.getItem(obj.getData(), TransferDetailViewModel.transferDetailsTempList.indexOf(obj.getData()));
            dialog.showAndWait();
            e.consume();
        });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    private void transferMasterAddProductBtnClicked() {
        transferMasterProductAddBtn.setOnAction(e -> dialog.showAndWait());
    }

    private void quotationProductDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/TransferDetailForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }

    public void transferMasterSaveBtnClicked() {
        MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

        if (transferMasterFromBranchId.getText().length() == 0) {
            transferMasterFromBranchId.setTrailingIcon(icon);
        }
        if (transferMasterToBranchId.getText().length() == 0) {
            transferMasterToBranchId.setTrailingIcon(icon);
        }
        if (transferMasterDate.getText().length() == 0) {
            transferMasterDate.setTrailingIcon(icon);
        }
        if (transferDetailTable.getTableColumns().isEmpty()) {
            // Notify table can't be empty
            System.out.println("Table can't be empty");
        }
        if (transferMasterFromBranchId.getText().length() > 0
                && transferMasterToBranchId.getText().length() > 0
                && transferMasterDate.getText().length() > 0
                && !transferDetailTable.getTableColumns().isEmpty()) {
            if (Integer.parseInt(transferMasterID.getText()) > 0) {
                TransferMasterViewModel.updateItem(Integer.parseInt(transferMasterID.getText()));
                transferMasterCancelBtnClicked();
            } else
                TransferMasterViewModel.saveTransferMaster();
            TransferMasterViewModel.resetProperties();
            TransferDetailViewModel.transferDetailsTempList.clear();
        }
    }

    public void transferMasterCancelBtnClicked() {
        TransferMasterViewModel.resetProperties();
        TransferDetailViewModel.transferDetailsTempList.clear();
        ((StackPane) transferMasterFormContentPane.getParent()).getChildren().get(0).setVisible(true);
        ((StackPane) transferMasterFormContentPane.getParent()).getChildren().remove(1);
    }
}
