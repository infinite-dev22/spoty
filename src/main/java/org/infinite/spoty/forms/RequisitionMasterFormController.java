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

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
import org.infinite.spoty.database.models.RequisitionDetail;
import org.infinite.spoty.database.models.Supplier;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.RequisitionDetailViewModel;
import org.infinite.spoty.viewModels.RequisitionMasterViewModel;
import org.infinite.spoty.viewModels.SupplierViewModel;
import org.infinite.spoty.views.BaseController;

@SuppressWarnings("unchecked")
public class RequisitionMasterFormController implements Initializable {
  private static RequisitionMasterFormController instance;
  public MFXTextField requisitionMasterID = new MFXTextField();
  @FXML public MFXFilterComboBox<Branch> requisitionMasterBranch;
  @FXML public MFXDatePicker requisitionMasterDate;
  @FXML public MFXTableView<RequisitionDetail> requisitionDetailTable;
  @FXML public MFXTextField requisitionMasterNote;
  @FXML public BorderPane requisitionMasterFormContentPane;
  @FXML public Label requisitionMasterFormTitle;
  @FXML public MFXButton requisitionMasterProductAddBtn;
  @FXML public MFXFilterComboBox<Supplier> requisitionMasterSupplier;
  @FXML public MFXTextField requisitionMasterShipVia;
  @FXML public MFXTextField requisitionMasterShipMthd;
  @FXML public MFXTextField requisitionMasterShipTerms;
  @FXML public Label requisitionMasterBranchValidationLabel;
  @FXML public Label requisitionMasterSupplierValidationLabel;
  @FXML public Label requisitionMasterDateValidationLabel;
  private Dialog<ButtonType> dialog;

  private RequisitionMasterFormController(Stage stage) {
    Platform.runLater(
        () -> {
          try {
            quotationProductDialogPane(stage);
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        });
  }

  public static RequisitionMasterFormController getInstance(Stage stage) {
    if (instance == null) instance = new RequisitionMasterFormController(stage);
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Input binding.
    requisitionMasterID
        .textProperty()
        .bindBidirectional(RequisitionMasterViewModel.idProperty(), new NumberStringConverter());
    requisitionMasterBranch
        .valueProperty()
        .bindBidirectional(RequisitionMasterViewModel.branchProperty());
    requisitionMasterBranch.setItems(BranchViewModel.getBranches());
    requisitionMasterBranch.setConverter(
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
      requisitionMasterSupplier
              .valueProperty()
              .bindBidirectional(RequisitionMasterViewModel.supplierProperty());
    requisitionMasterSupplier.setItems(SupplierViewModel.getSuppliers());
    requisitionMasterSupplier.setConverter(
        new StringConverter<>() {
          @Override
          public String toString(Supplier object) {
            if (object != null) return object.getName();
            else return null;
          }

          @Override
          public Supplier fromString(String string) {
            return null;
          }
        });
    requisitionMasterDate
        .textProperty()
        .bindBidirectional(RequisitionMasterViewModel.dateProperty());
    requisitionMasterShipVia
        .textProperty()
        .bindBidirectional(RequisitionMasterViewModel.shipViaProperty());
    requisitionMasterShipMthd
        .textProperty()
        .bindBidirectional(RequisitionMasterViewModel.shipMethodProperty());
    requisitionMasterShipTerms
        .textProperty()
        .bindBidirectional(RequisitionMasterViewModel.shippingTermsProperty());
    requisitionMasterNote
        .textProperty()
        .bindBidirectional(RequisitionMasterViewModel.noteProperty());
    // input validators.
    requiredValidator(
        requisitionMasterBranch, "Branch is required.", requisitionMasterBranchValidationLabel);
    requiredValidator(
        requisitionMasterSupplier,
        "Supplier is required.",
        requisitionMasterSupplierValidationLabel);
    requiredValidator(
        requisitionMasterDate, "Date is required.", requisitionMasterDateValidationLabel);
    requisitionMasterAddProductBtnClicked();
    Platform.runLater(this::setupTable);
  }

  private void setupTable() {
    MFXTableColumn<RequisitionDetail> productName =
        new MFXTableColumn<>(
            "Product", false, Comparator.comparing(RequisitionDetail::getProductDetailName));
    MFXTableColumn<RequisitionDetail> productQuantity =
        new MFXTableColumn<>(
            "Quantity", false, Comparator.comparing(RequisitionDetail::getQuantity));

    productName.setRowCellFactory(
        product -> new MFXTableRowCell<>(RequisitionDetail::getProductDetailName));
    productQuantity.setRowCellFactory(
        product -> new MFXTableRowCell<>(RequisitionDetail::getQuantity));

    productName.prefWidthProperty().bind(requisitionDetailTable.widthProperty().multiply(.4));
    productQuantity.prefWidthProperty().bind(requisitionDetailTable.widthProperty().multiply(.4));

    requisitionDetailTable.getTableColumns().addAll(productName, productQuantity);
    requisitionDetailTable
        .getFilters()
        .addAll(
            new StringFilter<>("Name", RequisitionDetail::getProductDetailName),
            new IntegerFilter<>("Quantity", RequisitionDetail::getQuantity));
    getRequisitionDetailTable();
    requisitionDetailTable.setItems(RequisitionDetailViewModel.requisitionDetailList);
  }

  private void getRequisitionDetailTable() {
    requisitionDetailTable.setPrefSize(1000, 1000);
    requisitionDetailTable.features().enableBounceEffect();
    requisitionDetailTable.features().enableSmoothScrolling(0.5);

    requisitionDetailTable.setTableRowFactory(
        t -> {
          MFXTableRow<RequisitionDetail> row = new MFXTableRow<>(requisitionDetailTable, t);
          EventHandler<ContextMenuEvent> eventHandler =
              event -> {
                showContextMenu((MFXTableRow<RequisitionDetail>) event.getSource())
                    .show(
                        requisitionDetailTable.getScene().getWindow(),
                        event.getScreenX(),
                        event.getScreenY());
                event.consume();
              };
          row.setOnContextMenuRequested(eventHandler);
          return row;
        });
  }

  private MFXContextMenu showContextMenu(MFXTableRow<RequisitionDetail> obj) {
    MFXContextMenu contextMenu = new MFXContextMenu(requisitionDetailTable);
    MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
    MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

    // Actions
    // Delete
    delete.setOnAction(
        e -> {
          RequisitionDetailViewModel.removeRequisitionDetail(
              obj.getData().getId(),
              RequisitionDetailViewModel.requisitionDetailList.indexOf(obj.getData()));
          e.consume();
        });
    // Edit
    edit.setOnAction(
        e -> {
          RequisitionDetailViewModel.getItem(
              obj.getData().getId(),
              RequisitionDetailViewModel.requisitionDetailList.indexOf(obj.getData()));
          dialog.showAndWait();
          e.consume();
        });

    contextMenu.addItems(edit, delete);

    return contextMenu;
  }

  private void requisitionMasterAddProductBtnClicked() {
    requisitionMasterProductAddBtn.setOnAction(e -> dialog.showAndWait());
  }

  private void quotationProductDialogPane(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = fxmlLoader("forms/RequisitionDetailForm.fxml");
    fxmlLoader.setControllerFactory(c -> RequisitionDetailFormController.getInstance(stage));

    dialog = new Dialog<>();
    dialog.setDialogPane(fxmlLoader.load());
    dialog.initOwner(stage);
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initStyle(StageStyle.UNDECORATED);
  }

  public void requisitionMasterSaveBtnClicked() {
    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

    if (!requisitionDetailTable.isDisabled()
        && RequisitionDetailViewModel.requisitionDetailList.isEmpty()) {
      SimpleNotification notification =
          new SimpleNotification.NotificationBuilder("Table can't be Empty")
              .duration(NotificationDuration.SHORT)
              .icon("fas-triangle-exclamation")
              .type(NotificationVariants.ERROR)
              .build();
      notificationHolder.addNotification(notification);
      return;
    }

    if (!requisitionMasterSupplierValidationLabel.isVisible()
        && !requisitionMasterDateValidationLabel.isVisible()
        && !requisitionMasterBranchValidationLabel.isVisible()) {
      if (Integer.parseInt(requisitionMasterID.getText()) > 0) {
        RequisitionMasterViewModel.updateItem(Integer.parseInt(requisitionMasterID.getText()));

        SimpleNotification notification =
            new SimpleNotification.NotificationBuilder("Requisition updated successfully")
                .duration(NotificationDuration.MEDIUM)
                .icon("fas-circle-check")
                .type(NotificationVariants.SUCCESS)
                .build();
        notificationHolder.addNotification(notification);

        requisitionMasterBranch.clearSelection();
        requisitionMasterSupplier.clearSelection();

        requisitionMasterCancelBtnClicked();
        return;
      }
      RequisitionMasterViewModel.saveRequisitionMaster();

      SimpleNotification notification =
          new SimpleNotification.NotificationBuilder("Requisition saved successfully")
              .duration(NotificationDuration.MEDIUM)
              .icon("fas-circle-check")
              .type(NotificationVariants.SUCCESS)
              .build();
      notificationHolder.addNotification(notification);

      requisitionMasterBranch.clearSelection();
      requisitionMasterSupplier.clearSelection();

      requisitionMasterCancelBtnClicked();
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

  public void requisitionMasterCancelBtnClicked() {
    BaseController.navigation.navigate(Pages.getRequisitionPane());
    RequisitionMasterViewModel.resetProperties();
    requisitionMasterBranchValidationLabel.setVisible(false);
    requisitionMasterSupplierValidationLabel.setVisible(false);
    requisitionMasterDateValidationLabel.setVisible(false);
    requisitionMasterBranch.clearSelection();
    requisitionMasterSupplier.clearSelection();
  }
}
