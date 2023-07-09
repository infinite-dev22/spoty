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

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.Validators.requiredValidator;

import io.github.palexdev.materialfx.controls.MFXContextMenu;
import io.github.palexdev.materialfx.controls.MFXContextMenuItem;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableRow;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.LongFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.components.navigation.Pages;
import org.infinite.spoty.components.notification.SimpleNotification;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.components.notification.enums.NotificationDuration;
import org.infinite.spoty.components.notification.enums.NotificationVariants;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.Customer;
import org.infinite.spoty.database.models.SaleDetail;
import org.infinite.spoty.values.strings.Values;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.CustomerViewModel;
import org.infinite.spoty.viewModels.SaleDetailViewModel;
import org.infinite.spoty.viewModels.SaleMasterViewModel;
import org.infinite.spoty.views.BaseController;

@SuppressWarnings("unchecked")
public class SaleMasterFormController implements Initializable {
  private static SaleMasterFormController instance;
  public MFXTextField saleMasterID = new MFXTextField();
  @FXML public Label saleFormTitle;
  @FXML public MFXDatePicker saleDate;
  @FXML public MFXFilterComboBox<Customer> saleCustomer;
  @FXML public MFXFilterComboBox<Branch> saleBranch;
  @FXML public MFXTableView<SaleDetail> saleDetailTable;
  @FXML public MFXTextField saleNote;
  @FXML public BorderPane saleFormContentPane;
  @FXML public MFXFilterComboBox<String> saleStatus;
  @FXML public MFXFilterComboBox<String> salePaymentStatus;
  @FXML public Label saleBranchValidationLabel;
  @FXML public Label saleCustomerValidationLabel;
  @FXML public Label saleDateValidationLabel;
  @FXML public Label saleStatusValidationLabel;
  @FXML public Label salePaymentStatusValidationLabel;
  @FXML public MFXButton saleMasterSaveBtn;
  private Dialog<ButtonType> dialog;

  private SaleMasterFormController(Stage stage) {
    Platform.runLater(
        () -> {
          try {
            saleProductDialogPane(stage);
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        });
  }

  public static SaleMasterFormController getInstance(Stage stage) {
    if (instance == null) instance = new SaleMasterFormController(stage);
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Set items to combo boxes and display custom text.
    saleCustomer.setItems(CustomerViewModel.getCustomersComboBoxList());
    saleCustomer.setOnShowing(
        e -> saleCustomer.setItems(CustomerViewModel.getCustomersComboBoxList()));
    saleCustomer.setConverter(
        new StringConverter<>() {
          @Override
          public String toString(Customer object) {
            if (object != null) return object.getName();
            else return null;
          }

          @Override
          public Customer fromString(String string) {
            return null;
          }
        });

    saleBranch.setItems(BranchViewModel.getBranchesComboBoxList());
    saleBranch.setOnShowing(e -> saleBranch.setItems(BranchViewModel.getBranchesComboBoxList()));
    saleBranch.setConverter(
        new StringConverter<>() {
          @Override
          public String toString(Branch object) {
            if (object != null) return object.getName();
            else return null;
          }

          @Override
          public Branch fromString(String string) {
            return null;
          }
        });
    saleStatus.setItems(FXCollections.observableArrayList(Values.SALESTATUSES));
    salePaymentStatus.setItems(FXCollections.observableArrayList(Values.PAYMENTSTATUSES));

    // Bi~Directional Binding.
    saleMasterID
        .textProperty()
        .bindBidirectional(SaleMasterViewModel.idProperty(), new NumberStringConverter());
    saleDate.textProperty().bindBidirectional(SaleMasterViewModel.dateProperty());
    saleCustomer.valueProperty().bindBidirectional(SaleMasterViewModel.customerProperty());
    saleBranch.valueProperty().bindBidirectional(SaleMasterViewModel.branchProperty());
    saleStatus.textProperty().bindBidirectional(SaleMasterViewModel.saleStatusProperty());
    salePaymentStatus.textProperty().bindBidirectional(SaleMasterViewModel.payStatusProperty());

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
    fxmlLoader.setControllerFactory(c -> SaleDetailFormController.getInstance(stage));

    dialog = new Dialog<>();
    dialog.setDialogPane(fxmlLoader.load());
    dialog.initOwner(stage);
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initStyle(StageStyle.UNDECORATED);
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
      if (Integer.parseInt(saleMasterID.getText()) > 0) {
        SaleMasterViewModel.updateItem(Integer.parseInt(saleMasterID.getText()));

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
      SaleMasterViewModel.saveSaleMaster();

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
    MFXTableColumn<SaleDetail> tax =
        new MFXTableColumn<>("Tax", false, Comparator.comparing(SaleDetail::getNetTax));
    MFXTableColumn<SaleDetail> discount =
        new MFXTableColumn<>("Discount", false, Comparator.comparing(SaleDetail::getDiscount));
    // Set table column data.
    product.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(SaleDetail::getProductName));
    quantity.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(SaleDetail::getQuantity));
    tax.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(SaleDetail::getNetTax));
    discount.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(SaleDetail::getDiscount));
    // Set table column width.
    product.prefWidthProperty().bind(saleDetailTable.widthProperty().multiply(.25));
    quantity.prefWidthProperty().bind(saleDetailTable.widthProperty().multiply(.25));
    tax.prefWidthProperty().bind(saleDetailTable.widthProperty().multiply(.25));
    discount.prefWidthProperty().bind(saleDetailTable.widthProperty().multiply(.25));
    // Set table filter.
    saleDetailTable.getTableColumns().addAll(product, quantity, tax, discount);
    saleDetailTable
        .getFilters()
        .addAll(
            new StringFilter<>("Product", SaleDetail::getProductName),
            new LongFilter<>("Quantity", SaleDetail::getQuantity),
            new DoubleFilter<>("Tax", SaleDetail::getNetTax),
            new DoubleFilter<>("Discount", SaleDetail::getDiscount));
    styleTable();
    // Populate table.
    saleDetailTable.setItems(SaleDetailViewModel.getSaleDetailList());
  }

  private void styleTable() {
    saleDetailTable.setPrefSize(1000, 1000);
    saleDetailTable.features().enableBounceEffect();
    saleDetailTable.features().enableSmoothScrolling(0.5);

    saleDetailTable.setTableRowFactory(
        t -> {
          MFXTableRow<SaleDetail> row = new MFXTableRow<>(saleDetailTable, t);
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
        e -> {
          SaleDetailViewModel.removeSaleDetail(
              obj.getData().getId(), SaleDetailViewModel.saleDetailList.indexOf(obj.getData()));
          e.consume();
        });
    // Edit
    edit.setOnAction(
        e -> {
          SaleDetailViewModel.getItem(
              obj.getData().getId(), SaleDetailViewModel.saleDetailList.indexOf(obj.getData()));
          dialog.showAndWait();
          e.consume();
        });

    contextMenu.addItems(edit, delete);

    return contextMenu;
  }
}
