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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
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
import static org.infinite.spoty.Validators.requiredValidator;

@SuppressWarnings("unchecked")
public class SaleMasterFormController implements Initializable {
    private static SaleMasterFormController instance;
    public MFXTextField saleMasterID = new MFXTextField();
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
    public AnchorPane saleFormContentPane;
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
    private Dialog<ButtonType> dialog;

    private SaleMasterFormController(Stage stage) {
        Platform.runLater(() -> {
            try {
                saleProductDialogPane(stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public static SaleMasterFormController getInstance(Stage stage) {
        if (instance == null)
            instance = new SaleMasterFormController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set items to combo boxes and display custom text.
        saleCustomer.setItems(CustomerViewModel.customersList);
        saleCustomer.setConverter(new StringConverter<>() {
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
        saleBranch.setItems(BranchViewModel.branchesList);
        saleBranch.setConverter(new StringConverter<>() {
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
        saleMasterID.textProperty().bindBidirectional(SaleMasterViewModel.idProperty(), new NumberStringConverter());
        saleDate.textProperty().bindBidirectional(SaleMasterViewModel.dateProperty());
        saleCustomer.valueProperty().bindBidirectional(SaleMasterViewModel.customerProperty());
        saleBranch.valueProperty().bindBidirectional(SaleMasterViewModel.branchProperty());
        saleStatus.textProperty().bindBidirectional(SaleMasterViewModel.saleStatusProperty());
        // input validators.
        requiredValidator(saleBranch, "Branch is required.", saleBranchValidationLabel);
        requiredValidator(saleCustomer, "Customer is required.", saleCustomerValidationLabel);
        requiredValidator(saleDate, "Date is required.", saleDateValidationLabel);
        requiredValidator(saleStatus, "Sale Status is required.", saleStatusValidationLabel);
        requiredValidator(salePaymentStatus, "Payment status is required.", salePaymentStatusValidationLabel);
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
        if (!saleDetailTable.isDisabled() && SaleDetailViewModel.saleDetailTempList.isEmpty()) {
            // Notify table can't be empty
            System.out.println("Table can't be empty");
        }
        if (!saleCustomerValidationLabel.isVisible()
                && !saleDateValidationLabel.isVisible()
                && !saleBranchValidationLabel.isVisible()
                && !saleStatusValidationLabel.isVisible()
                && !salePaymentStatusValidationLabel.isVisible()) {
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
        SaleMasterViewModel.resetProperties();
        saleDetailTable.getTableColumns().clear();
        saleBranchValidationLabel.setVisible(false);
        saleCustomerValidationLabel.setVisible(false);
        saleDateValidationLabel.setVisible(false);
        saleStatusValidationLabel.setVisible(false);
        salePaymentStatusValidationLabel.setVisible(false);
        ((StackPane) saleFormContentPane.getParent().getParent()).getChildren().get(0).setVisible(true);
        ((StackPane) saleFormContentPane.getParent().getParent()).getChildren().remove(1);
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
            SaleDetailViewModel.removeSaleDetail(obj.getData().getId(),
                    SaleDetailViewModel.saleDetailTempList.indexOf(obj.getData()));
            SaleDetailViewModel.getSaleDetails();
            e.consume();
        });
        // Edit
        edit.setOnAction(e -> {
            SaleDetailViewModel.getItem(obj.getData().getId(), SaleDetailViewModel.saleDetailTempList.indexOf(obj.getData()));
            dialog.showAndWait();
            e.consume();
        });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }
}
