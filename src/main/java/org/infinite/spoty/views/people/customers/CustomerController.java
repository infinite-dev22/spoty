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

package org.infinite.spoty.views.people.customers;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.StringFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.database.dao.CustomerDao;
import org.infinite.spoty.database.models.Customer;
import org.infinite.spoty.viewModels.CustomerViewModel;

@SuppressWarnings("unchecked")
public class CustomerController implements Initializable {
  private static CustomerController instance;
  @FXML public MFXTextField customerSearchBar;
  @FXML public HBox customerActionsPane;
  @FXML public MFXButton customerImportBtn;
  @FXML public BorderPane customersContentPane;
  @FXML private MFXTableView<Customer> customersTable;
  private Dialog<ButtonType> dialog;

  private CustomerController(Stage stage) {
    Platform.runLater(
        () -> {
          try {
            customerFormDialogPane(stage);
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
    Platform.runLater(this::setupTable);
  }

  private void setupTable() {
    MFXTableColumn<Customer> customerCode =
        new MFXTableColumn<>("Code", false, Comparator.comparing(Customer::getCode));
    MFXTableColumn<Customer> customerName =
        new MFXTableColumn<>("Name", false, Comparator.comparing(Customer::getName));
    MFXTableColumn<Customer> customerPhone =
        new MFXTableColumn<>("Phone", false, Comparator.comparing(Customer::getPhone));
    MFXTableColumn<Customer> customerEmail =
        new MFXTableColumn<>("Email", false, Comparator.comparing(Customer::getEmail));
    MFXTableColumn<Customer> customerTax =
        new MFXTableColumn<>("Tax No.", false, Comparator.comparing(Customer::getTaxNumber));

    customerCode.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getCode));
    customerName.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getName));
    customerPhone.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getPhone));
    customerEmail.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getEmail));
    customerTax.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getTaxNumber));

    customerCode.prefWidthProperty().bind(customersTable.widthProperty().multiply(.1));
    customerName.prefWidthProperty().bind(customersTable.widthProperty().multiply(.3));
    customerPhone.prefWidthProperty().bind(customersTable.widthProperty().multiply(.2));
    customerEmail.prefWidthProperty().bind(customersTable.widthProperty().multiply(.2));
    customerTax.prefWidthProperty().bind(customersTable.widthProperty().multiply(.2));

    customersTable
        .getTableColumns()
        .addAll(customerCode, customerName, customerPhone, customerEmail, customerTax);
    customersTable
        .getFilters()
        .addAll(
            new StringFilter<>("Code", Customer::getCode),
            new StringFilter<>("Name", Customer::getName),
            new StringFilter<>("Phone", Customer::getPhone),
            new StringFilter<>("Email", Customer::getEmail),
            new StringFilter<>("Tax No.", Customer::getTaxNumber));
    styleCustomerTable();
    customersTable.setItems(CustomerViewModel.getCustomers());
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

    // Actions
    // Delete
    delete.setOnAction(
        e -> {
          CustomerDao.deleteCustomer(obj.getData().getId());
          CustomerViewModel.getCustomers();
          e.consume();
        });
    // Edit
    edit.setOnAction(
        e -> {
          CustomerViewModel.getItem(obj.getData().getId());
          dialog.showAndWait();
          e.consume();
        });

    contextMenu.addItems(edit, delete);

    if (contextMenu.isShowing()) contextMenu.hide();
    return contextMenu;
  }

  private void customerFormDialogPane(Stage stage) throws IOException {
    DialogPane dialogPane = fxmlLoader("forms/CustomerForm.fxml").load();
    dialog = new Dialog<>();
    dialog.setDialogPane(dialogPane);
    dialog.initOwner(stage);
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initStyle(StageStyle.UNDECORATED);
  }

  public void customerCreateBtnClicked() {
    dialog.showAndWait();
  }
}
