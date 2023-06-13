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
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import org.infinite.spoty.database.dao.SaleDetailDao;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.Customer;
import org.infinite.spoty.database.models.SaleDetail;
import org.infinite.spoty.values.strings.Values;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.CustomerViewModel;
import org.infinite.spoty.viewModels.SaleDetailViewModel;
import org.infinite.spoty.viewModels.SaleMasterViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

@SuppressWarnings("unchecked")
public class SaleMasterFormController implements Initializable {
    public MFXTextField saleDetailID = new MFXTextField();
    public MFXTextField saleMasterID = new MFXTextField();
    @FXML
    public Label saleFormTitle;
    @FXML
    public MFXDatePicker saleDate;
    @FXML
    public MFXFilterComboBox<Customer> saleCustomerId;
    @FXML
    public MFXFilterComboBox<Branch> saleBranchId;
    @FXML
    public MFXTableView<SaleDetail> saleDetailTable;
    @FXML
    public MFXTextField saleNote;
    @FXML
    public AnchorPane saleFormContentPane;
    @FXML
    public MFXFilterComboBox<String> saleStatus;
    @FXML
    public MFXFilterComboBox<String> salePaymentStatus;
    private Dialog<ButtonType> dialog;

    public SaleMasterFormController(Stage stage) {
        Platform.runLater(() -> {
            try {
                saleProductDialogPane(stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Add form input event listeners.
        saleDate.textProperty().addListener((observable, oldValue, newValue) -> saleDate.setTrailingIcon(null));
        saleCustomerId.textProperty().addListener((observable, oldValue, newValue) -> saleCustomerId.setTrailingIcon(null));
        saleBranchId.textProperty().addListener((observable, oldValue, newValue) -> saleBranchId.setTrailingIcon(null));
        saleStatus.textProperty().addListener((observable, oldValue, newValue) -> saleStatus.setTrailingIcon(null));
        // Set items to combo boxes and display custom text.
        saleCustomerId.setItems(CustomerViewModel.customersList);
        saleCustomerId.setConverter(new StringConverter<>() {
            @Override
            public String toString(Customer object) {
                if (object != null)
                    return object.getName();
                else
                    return null;
            }

            @Override
            public Customer fromString(String string) {
                return null;
            }
        });
        saleBranchId.setItems(BranchViewModel.branchesList);
        saleBranchId.setConverter(new StringConverter<>() {
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
        saleStatus.setItems(FXCollections.observableArrayList(Values.SALESTATUSES));
        salePaymentStatus.setItems(FXCollections.observableArrayList(Values.PAYMENTSTATUSES));
        // Bi~Directional Binding.
        saleMasterID.textProperty().bindBidirectional(SaleMasterViewModel.idProperty());
        saleDate.textProperty().bindBidirectional(SaleMasterViewModel.dateProperty());
        saleCustomerId.valueProperty().bindBidirectional(SaleMasterViewModel.customerProperty());
        saleBranchId.valueProperty().bindBidirectional(SaleMasterViewModel.branchProperty());
        saleStatus.textProperty().bindBidirectional(SaleMasterViewModel.saleStatusProperty());
        setupTable();
    }

    private void saleProductDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/SaleDetailForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }

    public void saveBtnClicked() {
        MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

        if (saleDate.getText().length() == 0) {
            saleDate.setTrailingIcon(icon);
        }
        if (saleCustomerId.getText().length() == 0) {
            saleCustomerId.setTrailingIcon(icon);
        }
        if (saleBranchId.getText().length() == 0) {
            saleBranchId.setTrailingIcon(icon);
        }
        if (saleStatus.getText().length() == 0) {
            saleStatus.setTrailingIcon(icon);
        }
        if (saleDetailTable.getTableColumns().isEmpty()) {
            // Notify table can't be empty
            System.out.println("Table can't be empty");
        }
        if (saleDate.getText().length() > 0
                && saleCustomerId.getText().length() > 0
                && saleBranchId.getText().length() > 0
                && saleStatus.getText().length() > 0
                && !saleDetailTable.getTableColumns().isEmpty()) {
            if (Integer.parseInt(saleMasterID.getText()) > 0) {
                SaleMasterViewModel.updateItem(Integer.parseInt(saleMasterID.getText()));
                cancelBtnClicked();
            } else
                SaleMasterViewModel.saveSaleMaster();
            SaleMasterViewModel.resetProperties();
            SaleDetailViewModel.saleDetailTempList.clear();
        }
    }

    public void cancelBtnClicked() {
        ((StackPane) saleFormContentPane.getParent().getParent()).getChildren().get(0).setVisible(true);
        ((StackPane) saleFormContentPane.getParent().getParent()).getChildren().remove(1);
        SaleMasterViewModel.resetProperties();
        saleDetailTable.getTableColumns().clear();
    }

    public void addBtnClicked() {
        dialog.showAndWait();
    }

    private void setupTable() {
        // Set table column titles.
        MFXTableColumn<SaleDetail> product = new MFXTableColumn<>("Product", false, Comparator.comparing(SaleDetail::getProductName));
        MFXTableColumn<SaleDetail> quantity = new MFXTableColumn<>("Quantity", false, Comparator.comparing(SaleDetail::getQuantity));
        MFXTableColumn<SaleDetail> tax = new MFXTableColumn<>("Tax", false, Comparator.comparing(SaleDetail::getNetTax));
        MFXTableColumn<SaleDetail> discount = new MFXTableColumn<>("Discount", false, Comparator.comparing(SaleDetail::getDiscount));
        // Set table column data.
        product.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(SaleDetail::getProductName));
        quantity.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(SaleDetail::getQuantity));
        tax.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(SaleDetail::getNetTax));
        discount.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(SaleDetail::getDiscount));
        //Set table column width.
        product.prefWidthProperty().bind(saleDetailTable.widthProperty().multiply(.25));
        quantity.prefWidthProperty().bind(saleDetailTable.widthProperty().multiply(.25));
        tax.prefWidthProperty().bind(saleDetailTable.widthProperty().multiply(.25));
        discount.prefWidthProperty().bind(saleDetailTable.widthProperty().multiply(.25));
        // Set table filter.
        saleDetailTable.getTableColumns().addAll(product, quantity, tax, discount);
        saleDetailTable.getFilters().addAll(
                new StringFilter<>("Product", SaleDetail::getProductName),
                new IntegerFilter<>("Quantity", SaleDetail::getQuantity),
                new DoubleFilter<>("Tax", SaleDetail::getNetTax),
                new DoubleFilter<>("Discount", SaleDetail::getDiscount)
        );
        styleTable();
        // Populate table.
        saleDetailTable.setItems(SaleDetailViewModel.saleDetailTempList);
    }

    private void styleTable() {
        saleDetailTable.setPrefSize(1000, 1000);
        saleDetailTable.features().enableBounceEffect();
        saleDetailTable.features().enableSmoothScrolling(0.5);

        saleDetailTable.setTableRowFactory(t -> {
            MFXTableRow<SaleDetail> row = new MFXTableRow<>(saleDetailTable, t);
            EventHandler<ContextMenuEvent> eventHandler = event -> {
                showContextMenu((MFXTableRow<SaleDetail>) event.getSource())
                        .show(saleDetailTable.getScene().getWindow(), event.getScreenX(), event.getScreenY());
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
        delete.setOnAction(e -> {
            SaleDetailViewModel.getItem(obj.getData(), SaleDetailViewModel.saleDetailTempList.indexOf(obj.getData()));
            try {
                if (Integer.parseInt(saleDetailID.getText()) > 0)
                    SaleDetailDao.deleteSaleDetail(Integer.parseInt(saleDetailID.getText()));
            } catch (NumberFormatException ignored) {
                SaleDetailViewModel.removeSaleDetail(SaleDetailViewModel.saleDetailTempList.indexOf(obj.getData()));
            }
            SaleDetailViewModel.getSaleDetails();
            e.consume();
        });
        // Edit
        edit.setOnAction(e -> {
            SaleDetailViewModel.getItem(obj.getData(), SaleDetailViewModel.saleDetailTempList.indexOf(obj.getData()));
            dialog.showAndWait();
            e.consume();
        });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }
}
