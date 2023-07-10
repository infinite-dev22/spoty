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
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.LongFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.components.navigation.Pages;
import org.infinite.spoty.components.notification.SimpleNotification;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.components.notification.enums.NotificationDuration;
import org.infinite.spoty.components.notification.enums.NotificationVariants;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.TransferDetail;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.TransferDetailViewModel;
import org.infinite.spoty.viewModels.TransferMasterViewModel;
import org.infinite.spoty.views.BaseController;

@SuppressWarnings("unchecked")
public class TransferMasterFormController implements Initializable {
  private static TransferMasterFormController instance;
  public MFXTextField transferMasterID = new MFXTextField();
  @FXML public MFXFilterComboBox<Branch> transferMasterFromBranch;
  @FXML public MFXFilterComboBox<Branch> transferMasterToBranch;
  @FXML public MFXDatePicker transferMasterDate;
  @FXML public MFXTableView<TransferDetail> transferDetailTable;
  @FXML public MFXTextField transferMasterNote;
  @FXML public BorderPane transferMasterFormContentPane;
  @FXML public Label transferMasterFormTitle;
  @FXML public MFXButton transferMasterProductAddBtn;
  @FXML public Label transferMasterDateValidationLabel;
  @FXML public Label transferMasterToBranchValidationLabel;
  @FXML public Label transferMasterFromBranchValidationLabel;
  @FXML public MFXButton transferMasterSaveBtn;
  private MFXStageDialog dialog;

  private TransferMasterFormController(Stage stage) {
    Platform.runLater(
        () -> {
          try {
            quotationProductDialogPane(stage);
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        });
  }

  public static TransferMasterFormController getInstance(Stage stage) {
    if (instance == null) instance = new TransferMasterFormController(stage);
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Input binding.
    transferMasterID
        .textProperty()
        .bindBidirectional(TransferMasterViewModel.idProperty(), new NumberStringConverter());
    transferMasterFromBranch
        .valueProperty()
        .bindBidirectional(TransferMasterViewModel.fromBranchProperty());
    transferMasterToBranch
        .valueProperty()
        .bindBidirectional(TransferMasterViewModel.toBranchProperty());
    transferMasterDate.textProperty().bindBidirectional(TransferMasterViewModel.dateProperty());
    transferMasterNote.textProperty().bindBidirectional(TransferMasterViewModel.noteProperty());

    // ComboBox properties.
    transferMasterFromBranch.setItems(BranchViewModel.getBranchesComboBoxList());
    transferMasterFromBranch.setOnShowing(
        e -> transferMasterFromBranch.setItems(BranchViewModel.getBranchesComboBoxList()));
    transferMasterFromBranch.setConverter(
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

    transferMasterToBranch.setItems(BranchViewModel.getBranchesComboBoxList());
    transferMasterToBranch.setOnShowing(
        e -> transferMasterToBranch.setItems(BranchViewModel.getBranchesComboBoxList()));
    transferMasterToBranch.setConverter(
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

    // input validators.
    requiredValidator(
        transferMasterToBranch,
        "Receiving branch is required.",
        transferMasterToBranchValidationLabel,
        transferMasterSaveBtn);
    requiredValidator(
        transferMasterFromBranch,
        "Supplying branch is required.",
        transferMasterFromBranchValidationLabel,
        transferMasterSaveBtn);
    requiredValidator(
        transferMasterDate,
        "Date is required.",
        transferMasterDateValidationLabel,
        transferMasterSaveBtn);

    transferMasterAddProductBtnClicked();

    Platform.runLater(this::setupTable);
  }

  private void setupTable() {
    MFXTableColumn<TransferDetail> productName =
        new MFXTableColumn<>(
            "Product", false, Comparator.comparing(TransferDetail::getProductDetailName));
    MFXTableColumn<TransferDetail> productQuantity =
        new MFXTableColumn<>("Quantity", false, Comparator.comparing(TransferDetail::getQuantity));

    productName.setRowCellFactory(
        product -> new MFXTableRowCell<>(TransferDetail::getProductDetailName));
    productQuantity.setRowCellFactory(
        product -> new MFXTableRowCell<>(TransferDetail::getQuantity));

    productName.prefWidthProperty().bind(transferDetailTable.widthProperty().multiply(.4));
    productQuantity.prefWidthProperty().bind(transferDetailTable.widthProperty().multiply(.4));

    transferDetailTable.getTableColumns().addAll(productName, productQuantity);

    transferDetailTable
        .getFilters()
        .addAll(
            new StringFilter<>("Name", TransferDetail::getProductDetailName),
            new LongFilter<>("Quantity", TransferDetail::getQuantity));

    getTransferDetailTable();

    if (TransferDetailViewModel.getTransferDetails().isEmpty()) {
      TransferDetailViewModel.getTransferDetails()
          .addListener(
              (ListChangeListener<TransferDetail>)
                  c -> transferDetailTable.setItems(TransferDetailViewModel.getTransferDetails()));
    } else {
      transferDetailTable
          .itemsProperty()
          .bindBidirectional(TransferDetailViewModel.transferDetailsProperty());
    }
  }

  private void getTransferDetailTable() {
    transferDetailTable.setPrefSize(1000, 1000);
    transferDetailTable.features().enableBounceEffect();
    transferDetailTable.features().enableSmoothScrolling(0.5);

    transferDetailTable.setTableRowFactory(
        t -> {
          MFXTableRow<TransferDetail> row = new MFXTableRow<>(transferDetailTable, t);
          EventHandler<ContextMenuEvent> eventHandler =
              event -> {
                showContextMenu((MFXTableRow<TransferDetail>) event.getSource())
                    .show(
                        transferDetailTable.getScene().getWindow(),
                        event.getScreenX(),
                        event.getScreenY());
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
    delete.setOnAction(
        e -> {
          TransferDetailViewModel.removeTransferDetail(
              obj.getData().getId(),
              TransferDetailViewModel.transferDetailsList.indexOf(obj.getData()));
          e.consume();
        });
    // Edit
    edit.setOnAction(
        e -> {
          TransferDetailViewModel.getItem(
              obj.getData().getId(),
              TransferDetailViewModel.transferDetailsList.indexOf(obj.getData()));
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
    FXMLLoader fxmlLoader = fxmlLoader("forms/TransferDetailForm.fxml");
    fxmlLoader.setControllerFactory(c -> TransferDetailFormController.getInstance());

    MFXGenericDialog dialogContent = fxmlLoader.load();

    dialogContent.setShowMinimize(false);
    dialogContent.setShowAlwaysOnTop(false);

    dialog =
        MFXGenericDialogBuilder.build(dialogContent)
            .toStageDialogBuilder()
            .initOwner(stage)
            .initModality(Modality.WINDOW_MODAL)
            .setOwnerNode(transferMasterFormContentPane)
            .setScrimPriority(ScrimPriority.WINDOW)
            .setScrimOwner(true)
            .get();

    io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
  }

  public void transferMasterSaveBtnClicked() {
    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

    if (!transferDetailTable.isDisabled()
        && TransferDetailViewModel.transferDetailsList.isEmpty()) {
      SimpleNotification notification =
          new SimpleNotification.NotificationBuilder("Table can't be Empty")
              .duration(NotificationDuration.SHORT)
              .icon("fas-triangle-exclamation")
              .type(NotificationVariants.ERROR)
              .build();
      notificationHolder.addNotification(notification);
      return;
    }
    if (!transferMasterToBranchValidationLabel.isVisible()
        && !transferMasterFromBranchValidationLabel.isVisible()
        && !transferMasterDateValidationLabel.isVisible()) {
      if (Integer.parseInt(transferMasterID.getText()) > 0) {
        TransferMasterViewModel.updateItem(Integer.parseInt(transferMasterID.getText()));

        SimpleNotification notification =
            new SimpleNotification.NotificationBuilder("Transfer updated successfully")
                .duration(NotificationDuration.MEDIUM)
                .icon("fas-circle-check")
                .type(NotificationVariants.SUCCESS)
                .build();
        notificationHolder.addNotification(notification);

        transferMasterFromBranch.clearSelection();
        transferMasterToBranch.clearSelection();

        transferMasterCancelBtnClicked();
        return;
      }
      TransferMasterViewModel.saveTransferMaster();

      SimpleNotification notification =
          new SimpleNotification.NotificationBuilder("Transfer saved successfully")
              .duration(NotificationDuration.MEDIUM)
              .icon("fas-circle-check")
              .type(NotificationVariants.SUCCESS)
              .build();
      notificationHolder.addNotification(notification);

      transferMasterFromBranch.clearSelection();
      transferMasterToBranch.clearSelection();

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

  public void transferMasterCancelBtnClicked() {
    BaseController.navigation.navigate(Pages.getTransferPane());
    TransferMasterViewModel.resetProperties();
    transferMasterToBranchValidationLabel.setVisible(false);
    transferMasterFromBranchValidationLabel.setVisible(false);
    transferMasterDateValidationLabel.setVisible(false);
    transferMasterFromBranch.clearSelection();
    transferMasterToBranch.clearSelection();
  }
}
